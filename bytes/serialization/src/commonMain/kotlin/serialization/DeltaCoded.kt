/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bytes.serialization

import com.splendo.kaluga.bytes.layout.DeltaCodec
import com.splendo.kaluga.bytes.layout.NumericCodec
import com.splendo.kaluga.bytes.layout.NumericSettings
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.modules.SerializersModule

/** The size and signedness of the items of the delta-coded list [list], and the order they are written in. */
private class DeltaCodedItems(list: BinaryDescriptor) {
    private val item = list.children.first()
    private val settings = item.numericSettings as NumericSettings.Natural
    val width = settings.supportedLengths.single()
    val signed = settings.signed
    val step = list.collectionSettings!!.deltaCoding!!.step
    val order = item.childByteOrder

    // An unsigned item is taken as the bits of its type, as BytesFormat widens one before encoding it.
    fun wire(value: Byte): Long = if (signed) value.toLong() else value.toUByte().toLong()
    fun wire(value: Short): Long = if (signed) value.toLong() else value.toUShort().toLong()
    fun wire(value: Int): Long = if (signed) value.toLong() else value.toUInt().toLong()
}

/**
 * Writes the items of a delta-coded list as they are encoded: the first item, then the step to every next one. An
 * item of an unsigned type, which kotlinx encodes inline, arrives here as its signed counterpart.
 */
@OptIn(ExperimentalSerializationApi::class)
internal class DeltaCodedEncoder(list: BinaryDescriptor, private val builder: BinaryBuilder, override val serializersModule: SerializersModule) : AbstractEncoder() {

    private val items = DeltaCodedItems(list)
    private var index = 0
    private var previous = 0L

    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        this.index = index
        return true
    }

    override fun encodeByte(value: Byte) = add(items.wire(value))
    override fun encodeShort(value: Short) = add(items.wire(value))
    override fun encodeInt(value: Int) = add(items.wire(value))
    override fun encodeLong(value: Long) = add(value)
    override fun encodeValue(value: Any) = throw SerializationException("A delta-coded list holds integers, not $value")

    override fun endStructure(descriptor: SerialDescriptor) {
        builder.makeUnconstrained()
    }

    private fun add(value: Long) {
        val written = DeltaCodec.encode(index, previous, value, items.width, items.signed, items.step)
        previous = value
        val length = if (index == 0) items.width else items.step
        val bytes = ByteArray(length.bytes).also { NumericCodec.writeNatural(it, 0, written, length, index > 0 || items.signed, items.order) }
        builder.addAction(bytes.size) { add(bytes = bytes) }
    }
}

/** Reads a delta-coded list to the end of the bytes [decoder] has left: the first item, then the step to every next one. */
@OptIn(ExperimentalSerializationApi::class)
internal class DeltaCodedDecoder(list: BinaryDescriptor, private val decoder: BinaryDescriptorDecoder, override val serializersModule: SerializersModule) : AbstractDecoder() {

    private val items = DeltaCodedItems(list)

    // Reading no bytes moves past a byte the Booleans before the list filled partly, which holds none of it.
    private val size = decoder.nextBytes(0).let { DeltaCodec.count(decoder.remainingPayloadBytes(), items.width, items.step) }
    private var index = 0
    private var value = 0L

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int = if (index < size) index else DECODE_DONE

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = size

    override fun decodeByte(): Byte = next().toByte()
    override fun decodeShort(): Short = next().toShort()
    override fun decodeInt(): Int = next().toInt()
    override fun decodeLong(): Long = next()
    override fun decodeValue(): Any = throw SerializationException("A delta-coded list holds integers")

    override fun endStructure(descriptor: SerialDescriptor) {}

    private fun next(): Long {
        val length = if (index == 0) items.width else items.step
        val read = NumericCodec.readNatural(decoder.nextBytes(length.bytes), 0, length, index > 0 || items.signed, items.order)
        value = if (index == 0) read else DeltaCodec.next(value, read, items.width, items.signed)
        index++
        return value
    }
}

internal val BinaryDescriptor.isDeltaCoded: Boolean get() = collectionSettings?.deltaCoding != null
