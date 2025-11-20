/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */

package com.splendo.kaluga.bluetooth.serialization

import com.splendo.kaluga.base.bytes.ByteArrayBuilder
import com.splendo.kaluga.base.bytes.ByteOrder
import com.splendo.kaluga.base.bytes.StringEncodingSettings
import com.splendo.kaluga.base.bytes.buildByteArray
import com.splendo.kaluga.base.bytes.isBitSet
import com.splendo.kaluga.base.utils.MedFloat16
import com.splendo.kaluga.base.utils.MedFloat32
import com.splendo.kaluga.base.utils.toInt24
import com.splendo.kaluga.base.utils.toUInt24
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.internal.AbstractPolymorphicSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlin.math.pow

internal interface BinaryBuilder {
    fun addFlag(index: Int, value: Boolean)
    fun addAction(action: ByteArrayBuilder.() -> Unit)
    fun makeUnconstrained()

    fun build(): ByteArray
}

internal class StructureBinaryBuilder(
    val entry: FlagLayoutEntry,
    private val onUnconstrained: () -> Unit,
) : BinaryBuilder {
    private val flagBits = MutableList(
        entry.children.maxOf { entry ->
        entry.bitIndex + entry.bitWidth
    }.coerceAtLeast(0)) { false }
    private val actions = mutableListOf<ByteArrayBuilder.() -> Unit>()
    private var isOfUnconstrainedSize: Boolean = false

    override fun addFlag(index: Int, value: Boolean) {
        flagBits[index] = value
    }

    override fun addAction(action: ByteArrayBuilder.() -> Unit) {
        require(!isOfUnconstrainedSize) { "This object has data of an unconstrained size." }
        actions += action
    }

    override fun makeUnconstrained() {
        onUnconstrained()
        isOfUnconstrainedSize = true
    }

    override fun build(): ByteArray {
        val body = buildByteArray(entry.byteOrder) {
            flagBits.forEach {
                add(it)
            }
            actions.forEach { apply(it) }
        }
        val checksum = when (entry.blockSettings.checksumAlgorithm) {
            ChecksumAlgorithm.NONE -> byteArrayOf()
            ChecksumAlgorithm.CRC16 -> byteArrayOf()
            ChecksumAlgorithm.CRC32 -> byteArrayOf()
        }
        return buildByteArray(entry.byteOrder) {
            entry.blockSettings.prefix?.let {
                add(it.array)
            }
            add(body)
            add(checksum)
            entry.blockSettings.postfix?.let {
                add(it.array)
            }
        }
    }
}

internal fun BinaryBuilder.encodeBooleanElement(value: Boolean, entry: FlagLayoutEntry) {
    if (entry.bitIndex >= 0) {
        addFlag(entry.bitIndex, value)
    } else {
        addAction { add(value) }
    }
}

internal fun BinaryBuilder.encodeNumericElement(
    value: Number,
    entry: FlagLayoutEntry,
    settings: FlagLayoutEntry.NumericSettings,
) {
    when (settings) {
        is FlagLayoutEntry.NumericSettings.Natural -> {
            val supportedLengths = settings.supportedLengths
            val lengthToAdd = when (supportedLengths.size) {
                0 -> IllegalArgumentException("Size should be set")
                1 -> supportedLengths.first()
                else -> {
                    val (lengthIndex, length) = supportedLengths.withIndex().firstOrNull { (_, length) ->
                        length.fits(value, settings.signed)
                    } ?: supportedLengths.withIndex().last()

                    val offset = if (entry.isNullable) 1 else 0
                    for (offsetIndex in offset..<entry.bitWidth) {
                        addFlag(offsetIndex, lengthIndex.isBitSet(offsetIndex - offset))
                    }

                    length
                }
            }
            addAction {
                when (lengthToAdd) {
                    Length.`8_BIT` -> if (settings.signed) add(value.toByte()) else add(value.toByte().toUByte())
                    Length.`16_BIT` -> if (settings.signed) add(value.toShort(), entry.byteOrder) else add(value.toShort().toUShort(), entry.byteOrder)
                    Length.`24_BIT` -> if (settings.signed) add(value.toInt().toInt24(), entry.byteOrder) else add(value.toInt().toUInt().toUInt24(), entry.byteOrder)
                    Length.`32_BIT` -> if (settings.signed) add(value.toInt(), entry.byteOrder) else add(value.toInt().toUInt(), entry.byteOrder)
                    Length.`64_BIT` -> if (settings.signed) add(value.toLong(), entry.byteOrder) else add(value.toLong().toULong(), entry.byteOrder)
                }
            }
        }
        is FlagLayoutEntry.NumericSettings.Scalar -> {
            val scaledValue = settings.multiplier * value.toDouble() * 10.0.pow(settings.decimalExponent) * 2.0.pow(settings.binaryExponent) + settings.offset
            encodeNumericElement(scaledValue, entry,FlagLayoutEntry.NumericSettings.Natural(settings.supportedLengths, settings.signed))
        }
        is FlagLayoutEntry.NumericSettings.Decimal -> {
            val lengthToAdd = if (settings.supportedLengths.size > 1) {
                val flagIndex = entry.bitIndex + if (entry.isNullable) 1 else 0
                if (value.toDouble() >= Float.MIN_VALUE && value.toDouble() <= Float.MAX_VALUE) {
                    addFlag(flagIndex, false)
                    Length.`32_BIT`
                } else {
                    addFlag(flagIndex, true)
                    Length.`64_BIT`
                }
            } else settings.supportedLengths.first()

            addAction {
                when (lengthToAdd) {
                    Length.`32_BIT` -> add(value.toFloat())
                    Length.`64_BIT` -> add(value.toDouble())
                    else -> throw IllegalArgumentException("Decimal only supports 16 and 32 bit encoding")
                }
            }
        }
        is FlagLayoutEntry.NumericSettings.MedFloat -> {
            val lengthToAdd = if (settings.supportedLengths.size > 1) {

                val flagIndex = entry.bitIndex + if (entry.isNullable) 1 else 0
                if (value.toDouble() >= Float.MIN_VALUE && value.toDouble() <= Float.MAX_VALUE) {
                    addFlag(flagIndex, false)
                    Length.`16_BIT`
                } else {
                    addFlag(flagIndex, true)
                    Length.`32_BIT`
                }
            } else settings.supportedLengths.first()

            addAction {
                when (lengthToAdd) {
                    Length.`16_BIT` -> add(MedFloat16(value.toFloat()))
                    Length.`32_BIT` -> add(MedFloat32(value.toDouble()))
                    else -> throw IllegalArgumentException("MedFloat only supports 16 and 32 bit encoding")
                }
            }
        }
    }
}

internal fun BinaryBuilder.encodeByteElement(value: Byte, entry: FlagLayoutEntry) = encodeNumericElement(
    value,
    entry,
    entry.numericSettings ?: FlagLayoutEntry.NumericSettings.Natural(setOf(Length.`8_BIT`), false)
)
internal fun BinaryBuilder.encodeShortElement(value: Short, entry: FlagLayoutEntry) = encodeNumericElement(
    value,
    entry,
    entry.numericSettings ?: FlagLayoutEntry.NumericSettings.Natural(setOf(Length.`16_BIT`), false)
)
internal fun BinaryBuilder.encodeIntElement(value: Int, entry: FlagLayoutEntry) = encodeNumericElement(
    value,
    entry,
    entry.numericSettings ?: FlagLayoutEntry.NumericSettings.Natural(setOf(Length.`32_BIT`), false)
)
internal fun BinaryBuilder.encodeLongElement(value: Long, entry: FlagLayoutEntry) = encodeNumericElement(
    value,
    entry,
    entry.numericSettings ?: FlagLayoutEntry.NumericSettings.Natural(setOf(Length.`64_BIT`), false)
)

internal fun BinaryBuilder.encodeFloatElement(value: Float, entry: FlagLayoutEntry) = encodeNumericElement(
    value,
    entry,
    entry.numericSettings ?: FlagLayoutEntry.NumericSettings.Decimal(setOf(Length.`32_BIT`))
)
internal fun BinaryBuilder.encodeDoubleElement(value: Double, entry: FlagLayoutEntry) = encodeNumericElement(
    value,
    entry,
    entry.numericSettings ?: FlagLayoutEntry.NumericSettings.Decimal(setOf(Length.`64_BIT`))
)

internal fun BinaryBuilder.encodeStringElement(value: String, entry: FlagLayoutEntry) {
    val encoding = entry.stringSettings?.encoding  ?: StringEncodingSettings.Encoding.UTF_8
    val endMarking = entry.stringSettings?.endMarking ?: StringEncodingSettings.LengthPrefix()
    addAction { add(value, StringEncodingSettings(endMarking, encoding), entry.byteOrder) }
    if (endMarking is StringEncodingSettings.NoMarking) {
        makeUnconstrained()
    }
}
internal fun BinaryBuilder.encodeCharElement(value: Char, entry: FlagLayoutEntry) = encodeStringElement(value.toString(), entry)



internal class BluetoothBinaryEncoder(
    private val flag: FlagLayoutEntry,
    private val builder: BinaryBuilder,
    override val serializersModule: SerializersModule,
) : Encoder {

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        val builder = StructureBinaryBuilder(flag) { builder.makeUnconstrained() }.apply {
            builder.addAction { add(build()) }
        }
        return BluetoothBinaryCompositeEncoder(
            flag,
            builder,
            serializersModule,
            onFinishStructure = {
                when (descriptor.kind) {
                    is StructureKind.MAP,
                        is StructureKind.LIST -> {
                            when (flag.collectionSettings) {
                                is FlagLayoutEntry.CollectionSettings.NullMarked -> builder.addAction { add(0x00.toByte()) }
                                is FlagLayoutEntry.CollectionSettings.Unmarked -> { builder.makeUnconstrained() }
                                else -> {}
                            }
                        }
                    else -> {}
                }
            }
        ) { index ->
            when (descriptor.kind) {
                is PolymorphicKind -> flag.children[index]
                is StructureKind.CLASS -> flag.children[index]
                is StructureKind.LIST -> flag.children.first()
                is StructureKind.MAP -> if (index % 2 == 0) flag.children.first() else flag.children[1]
                else -> throw IllegalArgumentException("Structure ${descriptor.kind} is not Supported yet")
            }
        }
    }

    override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder {
        if (flag.isNullable) {
            builder.addFlag(flag.bitIndex, collectionSize > 0)
        }
        when (val collectionSettings = flag.collectionSettings ?: FlagLayoutEntry.CollectionSettings.NumericLength(setOf(Length.`8_BIT`))) {
            is FlagLayoutEntry.CollectionSettings.LengthPrefix ->
                builder.addAction { collectionSettings.endMarking.encodeSize(collectionSize.toUInt(), flag.byteOrder) }
            is FlagLayoutEntry.CollectionSettings.NumericLength -> builder.encodeNumericElement(collectionSize, flag, FlagLayoutEntry.NumericSettings.Natural(collectionSettings.supportedLengths, true))
            is FlagLayoutEntry.CollectionSettings.Unmarked -> {}
            is FlagLayoutEntry.CollectionSettings.NullMarked -> {}
        }
        return beginStructure(descriptor)
    }

    override fun encodeBoolean(value: Boolean) {
        builder.encodeBooleanElement(value, flag)
    }

    override fun encodeByte(value: Byte) {
        builder.encodeByteElement(value, flag)
    }

    override fun encodeChar(value: Char) {
        builder.encodeCharElement(value, flag)
    }

    override fun encodeDouble(value: Double) {
        builder.encodeDoubleElement(value, flag)
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        builder.addAction {
            if ((0 until enumDescriptor.elementsCount).all { enumDescriptor.getElementAnnotations(it).filterIsInstance<SerializedByteValue>().isNotEmpty() }) {
                add(enumDescriptor.getElementAnnotations(index).filterIsInstance<SerializedByteValue>().first().value)
            } else {
                add(enumDescriptor.getElementName(index), StringEncodingSettings(StringEncodingSettings.NoMarking, StringEncodingSettings.Encoding.UTF_8), flag.byteOrder)
            }
        }
    }

    override fun encodeFloat(value: Float) {
        builder.encodeFloatElement(value, flag)
    }

    override fun encodeInline(descriptor: SerialDescriptor): Encoder = this

    override fun encodeInt(value: Int) {
        builder.encodeIntElement(value, flag)
    }

    override fun encodeLong(value: Long) {
        builder.encodeLongElement(value, flag)
    }

    override fun encodeNull() {
        builder.addFlag(flag.bitIndex, false)
    }

    override fun encodeShort(value: Short) {
        builder.encodeShortElement(value, flag)
    }

    override fun encodeString(value: String) {
        builder.encodeStringElement(value, flag)
    }
}

private class BluetoothBinaryCompositeEncoder(
    private val flag: FlagLayoutEntry,
    private val builder: BinaryBuilder,
    override val serializersModule: SerializersModule,
    private val onFinishStructure: () -> Unit,
    private val getFlag: (Int) -> FlagLayoutEntry,
) : CompositeEncoder {

    override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean) {
        builder.encodeBooleanElement(value, getFlag(index))
    }

    override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte) {
        builder.encodeByteElement(value, getFlag(index))
    }

    override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char) {
        builder.encodeCharElement(value, getFlag(index))
    }

    override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double) {
        builder.encodeDoubleElement(value, getFlag(index))
    }

    override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float) {
        builder.encodeFloatElement(value, getFlag(index))
    }

    override fun encodeInlineElement(descriptor: SerialDescriptor, index: Int): Encoder = BluetoothBinaryEncoder(getFlag(index), builder, serializersModule)

    override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int) {
        builder.encodeIntElement(value, getFlag(index))
    }

    override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long) {
        builder.encodeLongElement(value, getFlag(index))
    }

    @ExperimentalSerializationApi
    override fun <T : Any> encodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T?
    ) {
        builder.addFlag(getFlag(index).bitIndex, value != null)
        value?.let {
            encodeSerializableElement(descriptor, index, serializer, value)
        }
    }

    override fun <T> encodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T
    ) {
        val flag = if (descriptor.kind is PolymorphicKind) {
            getFlag(index).children.first { entry -> entry.fieldName == serializer.descriptor.serialName }
        } else getFlag(index)
        BluetoothBinaryEncoder(flag, builder, serializersModule).encodeSerializableValue(serializer, value)
    }

    override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short) {
        builder.encodeShortElement(value, getFlag(index))
    }

    override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String) {
        if (descriptor.kind is PolymorphicKind && index == 0) {
            builder.addAction {
                flag.polymorphicMap[value]?.let {
                    add(it)
                } ?: add(value, StringEncodingSettings(StringEncodingSettings.NoMarking, StringEncodingSettings.Encoding.UTF_8), flag.byteOrder)
            }
        } else {
            builder.encodeStringElement(value, getFlag(index))
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        onFinishStructure()
    }
}
