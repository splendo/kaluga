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
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

internal fun <T> decodeFromList(list: List<Any>, deserializer: DeserializationStrategy<T>): T {
    val decoder = LinksDecoder(ArrayDeque(list))
    return decoder.decodeSerializableValue(deserializer)
}

internal class LinksDecoder(private val list: ArrayDeque<Any>, var elementsCount: Int = 0) : AbstractDecoder() {
    private var elementIndex = 0

    override val serializersModule: SerializersModule = EmptySerializersModule()

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (elementIndex == descriptor.elementsCount) return CompositeDecoder.DECODE_DONE
        return elementIndex++
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder = LinksDecoder(list, descriptor.elementsCount)

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = decodeInt().also {
        elementsCount = it
    }

    override fun decodeValue(): Any = list.removeFirst()

    override fun decodeInt(): Int = decodeValue().toString().toInt()

    override fun decodeBoolean(): Boolean = decodeValue().toString().toBoolean()

    override fun decodeFloat(): Float = decodeValue().toString().toFloat()

    override fun decodeDouble(): Double = decodeValue().toString().toDouble()

    override fun decodeLong(): Long = decodeValue().toString().toLong()

    override fun decodeChar(): Char = decodeValue().toString()[0]

    override fun decodeByte(): Byte = decodeValue().toString().toByte()

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int {
        val name = decodeValue().toString()
        val index = enumDescriptor.getElementIndex(name)
        if (index == CompositeDecoder.UNKNOWN_NAME) {
            throw SerializationException("${enumDescriptor.serialName} does not contain element with name '$name'")
        }

        return index
    }

    override fun decodeNotNullMark(): Boolean = decodeString() != "NULL"

    override fun decodeSequentially(): Boolean = true
}
