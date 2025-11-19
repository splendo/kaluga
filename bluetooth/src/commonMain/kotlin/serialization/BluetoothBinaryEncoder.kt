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
import com.splendo.kaluga.base.utils.toInt24
import com.splendo.kaluga.base.utils.toUInt24
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule

internal class BluetoothBinaryEncoder(
    private val builder: BinaryBuilder,
    private val indexOffset: Int = 0,
    override val serializersModule: SerializersModule,
) : AbstractEncoder() {

    class BinaryBuilder(descriptor: SerialDescriptor) {
        val layout = FlagLayoutRegistry.getLayout(descriptor)
        private val flagBits = layout.mapNotNull { layoutEntry ->
            if (layoutEntry.bitIndex >= 0) {
                (0..<layoutEntry.bitWidth).map {
                    layoutEntry.bitIndex + it to false
                }
            } else {
                null
            }
        }.flatten().sortedBy { (index, _) -> index }.toMap().toMutableMap()
        private val actions = mutableListOf<ByteArrayBuilder.() -> Unit>()
        private var _isOfUnconstrainedSize: Boolean = false
        val isOfUnconstrainedSize get() = _isOfUnconstrainedSize

        fun addFlag(index: Int, value: Boolean) {
            flagBits[index] = value
        }

        fun addAction(action: ByteArrayBuilder.() -> Unit) {
            require(!_isOfUnconstrainedSize) { "This object has data of an unconstrained size." }
            actions += action
        }

        fun makeUnconstrained() {
            _isOfUnconstrainedSize = true
        }

        fun build(builder: ByteArrayBuilder) {
            if (flagBits.isNotEmpty()) {
                (0..flagBits.keys.max()).forEach {
                    builder.add(flagBits[it] ?: false)
                }
            }
            actions.forEach { builder.apply(it) }
        }
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder = BluetoothBinaryCompositeEncoder(builder, indexOffset, serializersModule)
}

private class BluetoothBinaryCompositeEncoder(
    private val builder: BluetoothBinaryEncoder.BinaryBuilder,
    private val indexOffset: Int = 0,
    override val serializersModule: SerializersModule,
) : CompositeEncoder {

    private val Int.flag get() = builder.layout[this + indexOffset]

    override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean) {
        val flag = index.flag
        if (flag.bitIndex >= 0) {
            builder.addFlag(flag.bitIndex, value)
        } else {
            builder.addAction { add(value) }
        }
    }

    private fun encodeNumericElement(descriptor: SerialDescriptor, index: Int, value: Number, length: Length) {
        val annotations = descriptor.getElementAnnotations(index)
        val supportedLengths = annotations.filterIsInstance<Sizing>().map { it.length }
        val isSigned = annotations.filterIsInstance<Unsigned>().isEmpty()
        val lengthToAdd = when (supportedLengths.size) {
            0 -> length
            1 -> supportedLengths.first()
            else -> {
                val (lengthIndex, length) = supportedLengths.withIndex().firstOrNull { (_, length) ->
                    length.fits(value, isSigned)
                } ?: supportedLengths.withIndex().last()

                val flag = index.flag
                val offset = if (flag.isNullable) 1 else 0
                for (offsetIndex in offset..<flag.bitWidth) {
                    builder.addFlag(offsetIndex, lengthIndex.isBitSet(offsetIndex - offset))
                }

                length
            }
        }
        builder.addAction {
            val byteOrder = annotations.byteOrder
            when (lengthToAdd) {
                Length.`8_BIT` -> if (isSigned) add(value.toByte()) else add(value.toByte().toUByte())
                Length.`16_BIT` -> if (isSigned) add(value.toShort(), byteOrder) else add(value.toShort().toUShort(), byteOrder)
                Length.`24_BIT` -> if (isSigned) add(value.toInt().toInt24(), byteOrder) else add(value.toInt().toUInt().toUInt24(), byteOrder)
                Length.`32_BIT` -> if (isSigned) add(value.toInt(), byteOrder) else add(value.toInt().toUInt(), byteOrder)
                Length.`64_BIT` -> if (isSigned) add(value.toLong(), byteOrder) else add(value.toLong().toULong(), byteOrder)
            }
        }
    }

    override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte) {
        encodeNumericElement(descriptor, index, value, Length.`8_BIT`)
    }

    override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char) {
        builder.addAction { add(value) }
    }

    override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double) {
        TODO("Not yet implemented")
    }

    override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float) {
        TODO("Not yet implemented")
    }

    override fun encodeInlineElement(descriptor: SerialDescriptor, index: Int): Encoder = object : Encoder {
        val inlineIndex = index
        override val serializersModule: SerializersModule = this@BluetoothBinaryCompositeEncoder.serializersModule

        @ExperimentalSerializationApi
        override fun encodeNull() {}

        override fun encodeBoolean(value: Boolean) {
            encodeBooleanElement(descriptor, inlineIndex, value)
        }

        override fun encodeByte(value: Byte) {
            encodeByteElement(descriptor, inlineIndex, value)
        }

        override fun encodeShort(value: Short) {
            encodeShortElement(descriptor, inlineIndex, value)
        }

        override fun encodeChar(value: Char) {
            encodeCharElement(descriptor, inlineIndex, value)
        }

        override fun encodeInt(value: Int) {
            encodeIntElement(descriptor, inlineIndex, value)
        }

        override fun encodeLong(value: Long) {
            encodeLongElement(descriptor, inlineIndex, value)
        }

        override fun encodeFloat(value: Float) {
            encodeFloatElement(descriptor, inlineIndex, value)
        }

        override fun encodeDouble(value: Double) {
            encodeDoubleElement(descriptor, inlineIndex, value)
        }

        override fun encodeString(value: String) {
            encodeStringElement(descriptor, inlineIndex, value)
        }

        override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
            encodeIntElement(descriptor, inlineIndex, index)
        }

        override fun encodeInline(descriptor: SerialDescriptor): Encoder = this

        override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder = TODO()
    }

    override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int) {
        encodeNumericElement(descriptor, index, value, Length.`32_BIT`)
    }

    override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long) {
        encodeNumericElement(descriptor, index, value, Length.`64_BIT`)
    }

    @ExperimentalSerializationApi
    override fun <T : Any> encodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T?
    ) {
        val flag = index.flag
        builder.addFlag(flag.bitIndex, value != null)
        value?.let {
            serializer.serialize(
                object : AbstractEncoder() {
                    override val serializersModule: SerializersModule = this@BluetoothBinaryCompositeEncoder.serializersModule

                    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
                        TODO()
                    }

                    override fun encodeBoolean(value: Boolean) {
                        this@BluetoothBinaryCompositeEncoder.encodeBooleanElement(descriptor, index, value)
                    }

                    override fun encodeByte(value: Byte) {
                        this@BluetoothBinaryCompositeEncoder.encodeByteElement(descriptor, index, value)
                    }

                    override fun encodeChar(value: Char) {
                        this@BluetoothBinaryCompositeEncoder.encodeCharElement(descriptor, index, value)
                    }

                    override fun encodeDouble(value: Double) {
                        this@BluetoothBinaryCompositeEncoder.encodeDoubleElement(descriptor, index, value)
                    }

                    override fun encodeFloat(value: Float) {
                        this@BluetoothBinaryCompositeEncoder.encodeFloatElement(descriptor, index, value)
                    }

                    override fun encodeInline(descriptor: SerialDescriptor): Encoder {
                        TODO()
                    }

                    override fun encodeInt(value: Int) {
                        this@BluetoothBinaryCompositeEncoder.encodeIntElement(descriptor, index, value)
                    }

                    override fun encodeLong(value: Long) {
                        this@BluetoothBinaryCompositeEncoder.encodeLongElement(descriptor, index, value)
                    }

                    override fun encodeShort(value: Short) {
                        this@BluetoothBinaryCompositeEncoder.encodeShortElement(descriptor, index, value)
                    }

                    override fun encodeString(value: String) {
                        this@BluetoothBinaryCompositeEncoder.encodeStringElement(descriptor, index, value)
                    }
                },
                it
            )
        }
    }

    override fun <T> encodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T
    ) {
        val newBuilder = BluetoothBinaryEncoder.BinaryBuilder(descriptor.getElementDescriptor(index))
        BluetoothBinaryEncoder(newBuilder, 0, serializersModule).encodeSerializableValue(serializer, value)
        builder.addAction {
            add(buildByteArray(descriptor.getElementAnnotations(index).byteOrder, newBuilder::build))
        }
        if (newBuilder.isOfUnconstrainedSize) {
            builder.makeUnconstrained()
        }
    }

    override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short) {
        encodeNumericElement(descriptor, index, value, Length.`16_BIT`)
    }

    override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String) {
        val annotations = descriptor.getElementAnnotations(index)
        val encoding = annotations.filterIsInstance<Encoded>().firstOrNull()?.encoding ?: StringEncodingSettings.Encoding.UTF_8
        val endMarking = when {
            annotations.filterIsInstance<NullTerminated>().isNotEmpty() -> StringEncodingSettings.NullTerminated
            annotations.filterIsInstance<Sizing>().isNotEmpty() -> StringEncodingSettings.FixedLength(annotations.filterIsInstance<Sizing>().first().length.bytes)
            annotations.filterIsInstance<LengthPrefix>().isNotEmpty() -> annotations.filterIsInstance<LengthPrefix>().first().let { lengthPrefix ->
                StringEncodingSettings.LengthPrefix(lengthPrefix.lengthAsShort, lengthPrefix.canOverflow, lengthPrefix.sentinel)
            }
            else -> StringEncodingSettings.NoMarking
        }
        builder.addAction { add(value, StringEncodingSettings(endMarking, encoding), annotations.byteOrder) }
        if (endMarking is StringEncodingSettings.NoMarking) {
            builder.makeUnconstrained()
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {}
}

internal val List<Annotation>.byteOrder: ByteOrder get() = filterIsInstance<com.splendo.kaluga.bluetooth.serialization.ByteOrder>().firstOrNull()?.order ?: ByteOrder.LEAST_SIGNIFICANT_FIRST