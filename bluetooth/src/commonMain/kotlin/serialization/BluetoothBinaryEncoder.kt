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

import com.splendo.kaluga.base.utils.ByteArrayBuilder
import com.splendo.kaluga.base.utils.ByteOrder
import com.splendo.kaluga.base.utils.buildByteArray
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
            (0..flagBits.keys.max()).forEach {
                builder.add(flagBits[it] ?: false)
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

    private fun encodeNumericElement(descriptor: SerialDescriptor, index: Int, value: Number, defaultByteSize: Int) {
        val flag = index.flag

    }

    override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte) {
        encodeNumericElement(descriptor, index, value, 1)
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
        override val serializersModule: SerializersModule = this@BluetoothBinaryCompositeEncoder.serializersModule

        @ExperimentalSerializationApi
        override fun encodeNull() {}

        override fun encodeBoolean(value: Boolean) {
            encodeBooleanElement(descriptor, index, value)
        }

        override fun encodeByte(value: Byte) {
            encodeByteElement(descriptor, index, value)
        }

        override fun encodeShort(value: Short) {
            encodeShortElement(descriptor, index, value)
        }

        override fun encodeChar(value: Char) {
            encodeCharElement(descriptor, index, value)
        }

        override fun encodeInt(value: Int) {
            encodeIntElement(descriptor, index, value)
        }

        override fun encodeLong(value: Long) {
            encodeLongElement(descriptor, index, value)
        }

        override fun encodeFloat(value: Float) {
            encodeFloatElement(descriptor, index, value)
        }

        override fun encodeDouble(value: Double) {
            encodeDoubleElement(descriptor, index, value)
        }

        override fun encodeString(value: String) {
            encodeStringElement(descriptor, index, value)
        }

        override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
            TODO()
        }

        override fun encodeInline(descriptor: SerialDescriptor): Encoder = this

        override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder = TODO()
    }

    override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int) {
        encodeNumericElement(descriptor, index, value, 4)
    }

    override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long) {
        encodeNumericElement(descriptor, index, value, 8)
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
            BluetoothBinaryEncoder(builder, indexOffset + index, serializersModule).encodeSerializableValue(serializer, it)
        }
    }

    override fun <T> encodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T
    ) {
        val newBuilder = BluetoothBinaryEncoder.BinaryBuilder(descriptor)
        BluetoothBinaryEncoder(newBuilder, 0, serializersModule).encodeSerializableValue(serializer, value)
        builder.addAction {
            add(buildByteArray(descriptor.byteOrder, newBuilder::build))
        }
        if (newBuilder.isOfUnconstrainedSize) {
            builder.makeUnconstrained()
        }
    }

    override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short) {
        encodeNumericElement(descriptor, index, value, 2)
    }

    override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String) {
        val fixedLength = descriptor.annotations.filterIsInstance<FixedLength>().firstOrNull()
        val lengthPrefix = descriptor.annotations.filterIsInstance<LengthPrefix>().firstOrNull()
        val nullTerminated = descriptor.annotations.filterIsInstance<NullTerminated>().firstOrNull()
        when {
            fixedLength != null -> {}
            lengthPrefix != null -> {
                
            }
            nullTerminated != null -> {
                builder.addAction {
                    val encoded = value.encodeToByteArray()
                    require(!encoded.contains(0x00)) { "Null Terminated Strings must not include U+0000" }
                    when (descriptor.byteOrder) {
                        ByteOrder.MOST_SIGNIFICANT_FIRST -> add(byteArrayOf(0x00) + encoded.reversedArray())
                        ByteOrder.LEAST_SIGNIFICANT_FIRST -> add(encoded + 0x00)
                    }
                }
            }
            else -> {
                builder.addAction { add(value, descriptor.byteOrder) }
                builder.makeUnconstrained()
            }
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {}
}

internal val SerialDescriptor.byteOrder: ByteOrder get() = annotations.filterIsInstance<com.splendo.kaluga.bluetooth.serialization.ByteOrder>().firstOrNull()?.order ?: ByteOrder.LEAST_SIGNIFICANT_FIRST