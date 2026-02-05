/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.links.utils

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

internal fun <T> decodeFromMap(fields: Map<String, List<String>>, serializer: KSerializer<T>): T = MapDecoder(fields).decodeSerializableValue(serializer)

/**
 * High level serializer that supports only converting a [data] map to a serializable object.
 * Deserialization of primitives is not supported
 */
private class MapDecoder(private val data: Map<String, List<String>>) : Decoder {
    override val serializersModule: SerializersModule = EmptySerializersModule()
    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder = MapCompositeDecoder(data)
    override fun decodeInline(descriptor: SerialDescriptor): Decoder = this

    private fun notSupported(): Nothing = throw SerializationException("Decoding of primitive types not supported")
    override fun decodeBoolean(): Boolean = notSupported()
    override fun decodeByte(): Byte = notSupported()
    override fun decodeChar(): Char = notSupported()
    override fun decodeDouble(): Double = notSupported()
    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = notSupported()
    override fun decodeFloat(): Float = notSupported()
    override fun decodeInt(): Int = notSupported()
    override fun decodeLong(): Long = notSupported()
    override fun decodeShort(): Short = notSupported()
    override fun decodeString(): String = notSupported()

    @ExperimentalSerializationApi
    override fun decodeNotNullMark(): Boolean = notSupported()

    @ExperimentalSerializationApi
    override fun decodeNull(): Nothing = notSupported()
}

/**
 * First level decoder. Decodes elements into respective field of the serializable object.
 */
private class MapCompositeDecoder(private val data: Map<String, List<String>>) : CompositeDecoder {
    override val serializersModule: SerializersModule = EmptySerializersModule()

    /** Used by [decodeElementIndex] allowing iterating over the fields of the serializable object. */
    private val keysIterator = data.keys.iterator()

    private inline fun <reified T> get(descriptor: SerialDescriptor, index: Int, conversion: String.() -> T): T {
        val name = descriptor.getElementName(index)
        return get(name, { data[name]?.firstOrNull() }, conversion)
    }

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int = // iterate over available data
        if (keysIterator.hasNext()) {
            // try to find data in the descriptor
            val index = descriptor.getElementIndex(keysIterator.next())
            if (index == CompositeDecoder.UNKNOWN_NAME) {
                // descriptor does not contain a field, proceed to the next field
                decodeElementIndex(descriptor)
            } else {
                index // the field name found, return for processing
            }
        } else {
            CompositeDecoder.DECODE_DONE // no more data available, end of decoding
        }

    private fun getValueDecoder(descriptor: SerialDescriptor, index: Int): Decoder {
        val name = descriptor.getElementName(index)
        val values = data[name] ?: throw SerializationException("Element $name not found")
        return ValueDecoder(name, values)
    }

    override fun decodeInlineElement(descriptor: SerialDescriptor, index: Int): Decoder = getValueDecoder(descriptor, index)

    override fun <T> decodeSerializableElement(descriptor: SerialDescriptor, index: Int, deserializer: DeserializationStrategy<T>, previousValue: T?): T =
        getValueDecoder(descriptor, index).decodeSerializableValue(deserializer)

    @ExperimentalSerializationApi
    override fun <T : Any> decodeNullableSerializableElement(descriptor: SerialDescriptor, index: Int, deserializer: DeserializationStrategy<T?>, previousValue: T?): T? {
        val name = descriptor.getElementName(index)
        return if (data.containsKey(name)) {
            decodeSerializableElement(descriptor, index, deserializer, previousValue)
        } else {
            null
        }
    }

    override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean = get(descriptor, index, String::toBoolean)
    override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte = get(descriptor, index, String::toByte)
    override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char = get(descriptor, index, String::toChar)
    override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int): Double = get(descriptor, index, String::toDouble)
    override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int): Float = get(descriptor, index, String::toFloat)
    override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int = get(descriptor, index, String::toInt)
    override fun decodeLongElement(descriptor: SerialDescriptor, index: Int): Long = get(descriptor, index, String::toLong)
    override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short = get(descriptor, index, String::toShort)
    override fun decodeStringElement(descriptor: SerialDescriptor, index: Int): String = get(descriptor, index) { this }
    override fun endStructure(descriptor: SerialDescriptor) { }
}

/**
 * Second level decoder, decodes a serializable field. eg. enum, list, a value serialized into a primitive
 */
private class ValueDecoder(private val name: String, private val values: List<String>) : Decoder {
    override val serializersModule: SerializersModule = EmptySerializersModule()
    private inline fun <reified T> get(conversion: String.() -> T): T = get(name, { values.firstOrNull() }, conversion)
    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder = ListCompositeDecoder(values)
    override fun decodeBoolean(): Boolean = get(String::toBoolean)
    override fun decodeByte(): Byte = get(String::toByte)
    override fun decodeChar(): Char = get(String::toChar)
    override fun decodeDouble(): Double = get(String::toDouble)
    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = get {
        val index = enumDescriptor.getElementIndex(this)
        if (index == CompositeDecoder.UNKNOWN_NAME) throw SerializationException("$this can't be converted to ${enumDescriptor.serialName}")
        return index
    }
    override fun decodeFloat(): Float = get(String::toFloat)
    override fun decodeInline(descriptor: SerialDescriptor): Decoder = this
    override fun decodeInt(): Int = get(String::toInt)
    override fun decodeLong(): Long = get(String::toLong)
    override fun decodeShort(): Short = get(String::toShort)
    override fun decodeString(): String = get(String::toString)

    @ExperimentalSerializationApi
    override fun decodeNotNullMark(): Boolean = true

    @ExperimentalSerializationApi
    override fun decodeNull(): Nothing? = null
}

/**
 * Third level decoder, decodes list elements
 */
private class ListCompositeDecoder(private val values: List<String>) : CompositeDecoder {
    override val serializersModule: SerializersModule = EmptySerializersModule()

    private inline fun <reified T> get(index: Int, conversion: String.() -> T): T = get("#$index", { values.getOrNull(index) }, conversion)

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = values.size
    override fun decodeSequentially(): Boolean = true

    /** Used by [decodeElementIndex] allowing iterating over the fields of the serializable object. */
    private val indicesIterator = values.indices.iterator()
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int = if (indicesIterator.hasNext()) {
        indicesIterator.next()
    } else {
        CompositeDecoder.DECODE_DONE
    }

    override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean = get(index, String::toBoolean)
    override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte = get(index, String::toByte)
    override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char = get(index, String::toChar)
    override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int): Double = get(index, String::toDouble)
    override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int): Float = get(index, String::toFloat)
    override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int = get(index, String::toInt)
    override fun decodeLongElement(descriptor: SerialDescriptor, index: Int): Long = get(index, String::toLong)
    override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short = get(index, String::toShort)
    override fun decodeStringElement(descriptor: SerialDescriptor, index: Int): String = get(index) { this }
    override fun endStructure(descriptor: SerialDescriptor) { }
    private fun valueDecoderFor(index: Int): Decoder {
        val value = values.getOrNull(index) ?: throw SerializationException("Element #$index not found")
        return ValueDecoder("#$index", listOf(value))
    }
    override fun decodeInlineElement(descriptor: SerialDescriptor, index: Int): Decoder = valueDecoderFor(index)

    @ExperimentalSerializationApi
    override fun <T : Any> decodeNullableSerializableElement(descriptor: SerialDescriptor, index: Int, deserializer: DeserializationStrategy<T?>, previousValue: T?): T? =
        if (index in values.indices) {
            valueDecoderFor(index).decodeSerializableValue(deserializer)
        } else {
            null
        }
    override fun <T> decodeSerializableElement(descriptor: SerialDescriptor, index: Int, deserializer: DeserializationStrategy<T>, previousValue: T?): T =
        valueDecoderFor(index).decodeSerializableValue(deserializer)
}

private fun String.toChar(): Char {
    require(length == 1)
    return get(0)
}

private inline fun <reified T> get(name: String, valueProvider: () -> String?, conversion: String.() -> T): T {
    val value = valueProvider() ?: throw SerializationException("Element $name not found")
    return try {
        value.conversion()
    } catch (t: Throwable) {
        throw SerializationException("Element $name decoding: can't convert $value to ${T::class}", t)
    }
}
