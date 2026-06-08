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

import com.splendo.kaluga.base.bytes.Encoding
import com.splendo.kaluga.base.bytes.StringEncodingSettings
import com.splendo.kaluga.base.bytes.byteArraySize
import com.splendo.kaluga.base.bytes.isBitSet
import com.splendo.kaluga.base.utils.MedFloat16
import com.splendo.kaluga.base.utils.MedFloat32
import com.splendo.kaluga.base.utils.toInt24
import com.splendo.kaluga.base.utils.toUInt24
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import kotlin.math.pow
import kotlin.math.round

internal class BluetoothBinaryEncoder(
    private val binaryDescriptor: BluetoothBinaryDescriptor,
    private val builder: BinaryBuilder,
    override val serializersModule: SerializersModule,
) : Encoder {

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        var markUnconstrained = false
        val classBuilder = ClassBinaryBuilder(binaryDescriptor) { markUnconstrained = true }
        return BluetoothBinaryCompositeEncoder(
            binaryDescriptor,
            classBuilder,
            serializersModule,
            onFinishStructure = {
                builder.addAction(classBuilder.expectedSize) {
                    // Make sure the parent builder includes this builder in its build tree
                    with(classBuilder) {
                        build()
                    }
                }
                if (markUnconstrained) {
                    builder.makeUnconstrained()
                }
            },
        ) { index ->
            binaryDescriptor.children[index]
        }
    }

    override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder {
        if (binaryDescriptor.isNullable) {
            builder.addFlag(binaryDescriptor.bitIndex, collectionSize > 0)
        }
        val collectionSettings =
            binaryDescriptor.collectionSettings
                ?: BluetoothBinaryDescriptor.CollectionSettings(BluetoothBinaryDescriptor.CollectionSettings.NumericLength(setOf(Length.`8_BIT`)), false)

        // Encode length if necessary
        when (val lengthMarking = collectionSettings.lengthMarking) {
            is BluetoothBinaryDescriptor.CollectionSettings.LengthPrefix ->
                if (collectionSize > 0 || !collectionSettings.nullIfEmpty) {
                    builder.addAction(lengthMarking.endMarking.expectedByteSize(collectionSize.toUInt())) {
                        add(lengthMarking.endMarking.encodeSize(collectionSize.toUInt(), binaryDescriptor.byteOrder))
                    }
                }

            is BluetoothBinaryDescriptor.CollectionSettings.NumericLength ->
                if (collectionSize > 0 || !collectionSettings.nullIfEmpty) {
                    builder.encodeNumericElement(
                        collectionSize,
                        binaryDescriptor,
                        BluetoothBinaryDescriptor.NumericSettings.Natural(lengthMarking.supportedLengths, false),
                    )
                }

            is BluetoothBinaryDescriptor.CollectionSettings.Unmarked -> {}

            is BluetoothBinaryDescriptor.CollectionSettings.NullMarked -> {}
        }

        val isNullTerminated = collectionSettings.lengthMarking is BluetoothBinaryDescriptor.CollectionSettings.NullMarked

        var markUnconstrained = false
        val binaryBuilder = when (descriptor.kind) {
            is StructureKind.LIST -> ListBinaryBuilder(binaryDescriptor, collectionSize, isNullTerminated) { markUnconstrained = true }
            is StructureKind.MAP -> MapBinaryBuilder(binaryDescriptor, collectionSize, isNullTerminated) { markUnconstrained = true }
            else -> throw IllegalArgumentException("SerialKind ${descriptor.kind} is not Supported as a Collection")
        }

        return BluetoothBinaryCompositeEncoder(
            binaryDescriptor,
            binaryBuilder,
            serializersModule,
            onFinishStructure = {
                // Make sure the parent builder includes this builder in its build tree
                if (binaryBuilder.expectedSize > 0) {
                    builder.addAction(binaryBuilder.expectedSize) { with(binaryBuilder) { build() } }
                }
                if (markUnconstrained) {
                    builder.makeUnconstrained()
                }

                // When done, mark end if necessary
                when (collectionSettings.lengthMarking) {
                    is BluetoothBinaryDescriptor.CollectionSettings.NullMarked -> {
                        if (collectionSize > 0 || !collectionSettings.nullIfEmpty) {
                            builder.addAction(1) { add(0x00.toByte()) }
                        }
                    }

                    is BluetoothBinaryDescriptor.CollectionSettings.Unmarked -> {
                        if (collectionSize > 0 || !collectionSettings.nullIfEmpty) {
                            builder.makeUnconstrained()
                        }
                    }

                    else -> {}
                }
            },
        ) { index -> binaryBuilder.setIndex(index) }
    }

    override fun encodeBoolean(value: Boolean) {
        markNotNull()
        builder.encodeBooleanElement(value, binaryDescriptor)
    }

    override fun encodeByte(value: Byte) {
        markNotNull()
        builder.encodeByteElement(value, binaryDescriptor)
    }

    override fun encodeChar(value: Char) {
        markNotNull()
        builder.encodeCharElement(value, binaryDescriptor)
    }

    override fun encodeDouble(value: Double) {
        markNotNull()
        builder.encodeDoubleElement(value, binaryDescriptor)
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        markNotNull()
        binaryDescriptor.enumMap[index]?.array?.let {
            builder.addAction(it.size) {
                add(bytes = it)
            }
        }
    }

    override fun encodeFloat(value: Float) {
        markNotNull()
        builder.encodeFloatElement(value, binaryDescriptor)
    }

    override fun encodeInline(descriptor: SerialDescriptor): Encoder = this

    override fun encodeInt(value: Int) {
        markNotNull()
        builder.encodeIntElement(value, binaryDescriptor)
    }

    override fun encodeLong(value: Long) {
        markNotNull()
        builder.encodeLongElement(value, binaryDescriptor)
    }

    override fun encodeNull() {
        builder.addFlag(binaryDescriptor.bitIndex, false)
    }

    override fun encodeShort(value: Short) {
        markNotNull()
        builder.encodeShortElement(value, binaryDescriptor)
    }

    override fun encodeString(value: String) {
        markNotNull()
        builder.encodeStringElement(value, binaryDescriptor)
    }

    private fun markNotNull() {
        if (binaryDescriptor.isNullable) {
            builder.addFlag(binaryDescriptor.bitIndex, true)
        }
    }
}

private class BluetoothBinaryCompositeEncoder(
    private val binaryDescriptor: BluetoothBinaryDescriptor,
    private val builder: BinaryBuilder,
    override val serializersModule: SerializersModule,
    private val onFinishStructure: () -> Unit,
    private val getBinaryDescriptor: (Int) -> BluetoothBinaryDescriptor,
) : CompositeEncoder {

    override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean) {
        builder.encodeBooleanElement(value, getBinaryDescriptor(index))
    }

    override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte) {
        builder.encodeByteElement(value, getBinaryDescriptor(index))
    }

    override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char) {
        builder.encodeCharElement(value, getBinaryDescriptor(index))
    }

    override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double) {
        builder.encodeDoubleElement(value, getBinaryDescriptor(index))
    }

    override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float) {
        builder.encodeFloatElement(value, getBinaryDescriptor(index))
    }

    override fun encodeInlineElement(descriptor: SerialDescriptor, index: Int): Encoder = BluetoothBinaryEncoder(getBinaryDescriptor(index), builder, serializersModule)

    override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int) {
        builder.encodeIntElement(value, getBinaryDescriptor(index))
    }

    override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long) {
        builder.encodeLongElement(value, getBinaryDescriptor(index))
    }

    @ExperimentalSerializationApi
    override fun <T : Any> encodeNullableSerializableElement(descriptor: SerialDescriptor, index: Int, serializer: SerializationStrategy<T>, value: T?) {
        builder.addFlag(getBinaryDescriptor(index).bitIndex, value != null)
        value?.let {
            encodeSerializableElement(descriptor, index, serializer, value)
        }
    }

    override fun <T> encodeSerializableElement(descriptor: SerialDescriptor, index: Int, serializer: SerializationStrategy<T>, value: T) {
        when (descriptor.kind) {
            is PolymorphicKind.SEALED -> {
                val binaryDescriptor = getBinaryDescriptor(index).children.first { binaryDescriptor ->
                    binaryDescriptor.fieldName == serializer.descriptor.serialName
                }
                BluetoothBinaryEncoder(binaryDescriptor, builder, serializersModule).encodeSerializableValue(serializer, value)
            }

            is PolymorphicKind.OPEN -> {
                val binaryDescriptor = binaryDescriptor.children.first { binaryDescriptor -> binaryDescriptor.fieldName == serializer.descriptor.serialName }
                BluetoothBinaryEncoder(binaryDescriptor, builder, serializersModule).encodeSerializableValue(serializer, value)
            }

            else -> {
                val binaryDescriptor = getBinaryDescriptor(index)
                BluetoothBinaryEncoder(binaryDescriptor, builder, serializersModule).encodeSerializableValue(serializer, value)
            }
        }
    }

    override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short) {
        builder.encodeShortElement(value, getBinaryDescriptor(index))
    }

    override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String) {
        if (descriptor.kind is PolymorphicKind && index == 0) {
            // The first string of a Polymorphic kind is its type key. Encode its match in the polymorphicMap
            binaryDescriptor.polymorphicMap[value]?.array?.let {
                builder.addAction(it.size) {
                    add(it)
                }
            } ?: throw IllegalStateException("Polymorphic class for $value has not been annotated with SerializedByteValue")
        } else {
            builder.encodeStringElement(value, getBinaryDescriptor(index))
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        onFinishStructure()
    }
}

internal fun BinaryBuilder.encodeBooleanElement(value: Boolean, binaryDescriptor: BluetoothBinaryDescriptor) {
    val offset = if (binaryDescriptor.isNullable) 1 else 0
    if (binaryDescriptor.bitIndex >= 0 && binaryDescriptor.bitWidth > offset) {
        addFlag(binaryDescriptor.bitIndex + offset, value)
    } else {
        addBit(value)
    }
}

// Whether [this] is represented losslessly by a 32-bit float. Kotlin/JS has no true 32-bit Float, so
// `toFloat()` does not round to 32-bit precision there and a naive `this == toFloat()` would always be
// true — round-trip through the raw bits to obtain the genuine 32-bit value so the 32-bit-vs-64-bit
// sizing decision (and thus the byte layout) is identical on every platform.
private fun Number.fitsLosslesslyIn32BitFloat(): Boolean = toDouble() == Float.fromBits(toFloat().toRawBits()).toDouble()

internal fun BinaryBuilder.encodeNumericElement(value: Number, binaryDescriptor: BluetoothBinaryDescriptor, settings: BluetoothBinaryDescriptor.NumericSettings) {
    when (settings) {
        is BluetoothBinaryDescriptor.NumericSettings.Natural -> {
            val supportedLengths = settings.supportedLengths
            // Grab desired length
            val lengthToAdd = when (supportedLengths.size) {
                0 -> throw IllegalArgumentException("Size should be set")

                1 -> supportedLengths.first()

                else -> {
                    // Find smallest fitting match or take the last size
                    val (lengthIndex, length) = supportedLengths.withIndex().firstOrNull { (_, length) ->
                        length.fits(value, settings.signed)
                    } ?: supportedLengths.withIndex().last()

                    // Set the flags according to the picked size
                    val offset = if (binaryDescriptor.isNullable) 1 else 0
                    for (offsetIndex in offset..<binaryDescriptor.bitWidth) {
                        addFlag(binaryDescriptor.bitIndex + offsetIndex, lengthIndex.isBitSet(offsetIndex - offset))
                    }

                    length
                }
            }
            addAction(lengthToAdd.bytes) {
                // Encode based on length
                when (lengthToAdd) {
                    Length.`8_BIT` -> if (settings.signed) add(value.toByte()) else add(value.toByte().toUByte())

                    Length.`16_BIT` -> if (settings.signed) add(value.toShort(), binaryDescriptor.byteOrder) else add(value.toShort().toUShort(), binaryDescriptor.byteOrder)

                    Length.`24_BIT` -> if (settings.signed) {
                        add(
                            value.toInt().toInt24(),
                            binaryDescriptor.byteOrder,
                        )
                    } else {
                        add(value.toInt().toUInt().toUInt24(), binaryDescriptor.byteOrder)
                    }

                    Length.`32_BIT` -> if (settings.signed) add(value.toInt(), binaryDescriptor.byteOrder) else add(value.toInt().toUInt(), binaryDescriptor.byteOrder)

                    Length.`64_BIT` -> if (settings.signed) add(value.toLong(), binaryDescriptor.byteOrder) else add(value.toLong().toULong(), binaryDescriptor.byteOrder)
                }
            }
        }

        is BluetoothBinaryDescriptor.NumericSettings.Scalar -> {
            // Calculate scaled value and store it as a natural number. The scaled value is meant to be
            // integral; round to the nearest integer rather than letting the downstream `toByte()`/etc.
            // truncate it, so sub-ULP floating-point noise (which differs per platform — Kotlin/JS has no
            // true 32-bit Float and `pow` can differ by a ULP) can't tip a boundary value to the wrong
            // integer. Guarantees identical bytes on every platform.
            val scaledValue = round(settings.multiplier * value.toDouble() * 10.0.pow(settings.decimalExponent) * 2.0.pow(settings.binaryExponent) + settings.offset)
            encodeNumericElement(scaledValue, binaryDescriptor, BluetoothBinaryDescriptor.NumericSettings.Natural(settings.supportedLengths, settings.signed))
        }

        is BluetoothBinaryDescriptor.NumericSettings.Decimal -> {
            val lengthToAdd = if (settings.supportedLengths.size > 1) {
                val flagIndex = binaryDescriptor.bitIndex + if (binaryDescriptor.isNullable) 1 else 0
                if (value.fitsLosslesslyIn32BitFloat()) {
                    addFlag(flagIndex, false)
                    Length.`32_BIT`
                } else {
                    addFlag(flagIndex, true)
                    Length.`64_BIT`
                }
            } else {
                settings.supportedLengths.first()
            }

            addAction(lengthToAdd.bytes) {
                when (lengthToAdd) {
                    Length.`32_BIT` -> add(value.toFloat())
                    Length.`64_BIT` -> add(value.toDouble())
                    else -> throw IllegalArgumentException("Decimal only supports 16 and 32 bit encoding")
                }
            }
        }

        is BluetoothBinaryDescriptor.NumericSettings.MedFloat -> {
            val lengthToAdd = if (settings.supportedLengths.size > 1) {
                val flagIndex = binaryDescriptor.bitIndex + if (binaryDescriptor.isNullable) 1 else 0
                if (MedFloat16.canRepresent(value.toDouble())) {
                    addFlag(flagIndex, false)
                    Length.`16_BIT`
                } else {
                    addFlag(flagIndex, true)
                    Length.`32_BIT`
                }
            } else {
                settings.supportedLengths.first()
            }

            addAction(lengthToAdd.bytes) {
                when (lengthToAdd) {
                    Length.`16_BIT` -> add(MedFloat16(value.toDouble()))
                    Length.`32_BIT` -> add(MedFloat32(value.toDouble()))
                    else -> throw IllegalArgumentException("MedFloat only supports 16 and 32 bit encoding")
                }
            }
        }
    }
}

private val BluetoothBinaryDescriptor.isUnsigned: Boolean get() = when (numericSettings) {
    is BluetoothBinaryDescriptor.NumericSettings.Natural -> !numericSettings.signed
    is BluetoothBinaryDescriptor.NumericSettings.Scalar -> !numericSettings.signed
    is BluetoothBinaryDescriptor.NumericSettings.Decimal -> false
    is BluetoothBinaryDescriptor.NumericSettings.MedFloat -> false
    null -> false
}

internal fun BinaryBuilder.encodeByteElement(value: Byte, binaryDescriptor: BluetoothBinaryDescriptor) = encodeNumericElement(
    if (binaryDescriptor.isUnsigned) value.toUByte().toShort() else value,
    binaryDescriptor,
    binaryDescriptor.numericSettings ?: BluetoothBinaryDescriptor.NumericSettings.Natural(setOf(Length.`8_BIT`), false),
)
internal fun BinaryBuilder.encodeShortElement(value: Short, binaryDescriptor: BluetoothBinaryDescriptor) = encodeNumericElement(
    if (binaryDescriptor.isUnsigned) value.toUShort().toInt() else value,
    binaryDescriptor,
    binaryDescriptor.numericSettings ?: BluetoothBinaryDescriptor.NumericSettings.Natural(setOf(Length.`16_BIT`), false),
)
internal fun BinaryBuilder.encodeIntElement(value: Int, binaryDescriptor: BluetoothBinaryDescriptor) = encodeNumericElement(
    if (binaryDescriptor.isUnsigned) value.toUInt().toLong() else value,
    binaryDescriptor,
    binaryDescriptor.numericSettings ?: BluetoothBinaryDescriptor.NumericSettings.Natural(setOf(Length.`32_BIT`), false),
)
internal fun BinaryBuilder.encodeLongElement(value: Long, binaryDescriptor: BluetoothBinaryDescriptor) = encodeNumericElement(
    value,
    binaryDescriptor,
    binaryDescriptor.numericSettings ?: BluetoothBinaryDescriptor.NumericSettings.Natural(setOf(Length.`64_BIT`), false),
)

internal fun BinaryBuilder.encodeFloatElement(value: Float, binaryDescriptor: BluetoothBinaryDescriptor) = encodeNumericElement(
    value,
    binaryDescriptor,
    binaryDescriptor.numericSettings ?: BluetoothBinaryDescriptor.NumericSettings.Decimal(setOf(Length.`32_BIT`)),
)
internal fun BinaryBuilder.encodeDoubleElement(value: Double, binaryDescriptor: BluetoothBinaryDescriptor) = encodeNumericElement(
    value,
    binaryDescriptor,
    binaryDescriptor.numericSettings ?: BluetoothBinaryDescriptor.NumericSettings.Decimal(setOf(Length.`64_BIT`)),
)

internal fun BinaryBuilder.encodeStringElement(value: String, binaryDescriptor: BluetoothBinaryDescriptor) {
    val encoding = binaryDescriptor.stringSettings?.encoding ?: Encoding.UTF_8
    val endMarking = binaryDescriptor.stringSettings?.endMarking ?: StringEncodingSettings.LengthPrefix.ByteLength
    val settings = StringEncodingSettings(endMarking, encoding)
    addAction(value.byteArraySize(settings)) { add(value, settings, binaryDescriptor.byteOrder) }
    if (endMarking is StringEncodingSettings.NoMarking) {
        makeUnconstrained()
    }
}
internal fun BinaryBuilder.encodeCharElement(value: Char, binaryDescriptor: BluetoothBinaryDescriptor) {
    val encoding = binaryDescriptor.stringSettings?.encoding ?: Encoding.UTF_8
    addAction(encoding.byteSize) { add(value, encoding, binaryDescriptor.byteOrder) }
}
