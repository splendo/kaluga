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

package com.splendo.kaluga.bytes.serialization

import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.medfloat.add
import com.splendo.kaluga.bytes.strings.add
import com.splendo.kaluga.bytes.strings.Encoding
import com.splendo.kaluga.bytes.strings.StringEncodingSettings
import com.splendo.kaluga.bytes.buildByteArray
import com.splendo.kaluga.bytes.strings.byteArraySize
import com.splendo.kaluga.bytes.isBitSet
import com.splendo.kaluga.bytes.medfloat.MedFloat16
import com.splendo.kaluga.bytes.medfloat.MedFloat32
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.layout.CollectionSettings
import com.splendo.kaluga.bytes.layout.UnsupportedCharEncoding
import com.splendo.kaluga.bytes.layout.FrameSettings
import com.splendo.kaluga.bytes.layout.NumericCodec
import com.splendo.kaluga.bytes.layout.NumericSettings
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule

internal class BinaryEncoder(
    private val binaryDescriptor: BinaryDescriptor,
    private val builder: BinaryBuilder,
    override val serializersModule: SerializersModule,
    // True only for the top-level frame. The root stuffed region is bounded by the whole payload, so it is stuffed
    // without a terminator; a nested region (default `false`) appends its terminator so the parent can find its end.
    private val isRootFrame: Boolean = false,
) : Encoder {

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        var markUnconstrained = false
        val classBuilder = ClassBinaryBuilder(binaryDescriptor) { markUnconstrained = true }
        return BinaryCompositeEncoder(
            binaryDescriptor,
            classBuilder,
            serializersModule,
            onFinishStructure = {
                val stuffing = binaryDescriptor.structureSettings.byteStuffing
                if (stuffing != null) {
                    binaryDescriptor.requireLeastSignificantFirstForStuffing()
                    // Stuffs only the body + checksum, leaving any prefix/postfix as an untouched frame. A nested region
                    // appends its terminator (or is marked unconstrained so it must be the last field); the root region
                    // is bounded by the whole payload and needs neither. Frame: [prefix][stuffed body+crc][term?][postfix].
                    val terminator = if (isRootFrame) null else binaryDescriptor.structureSettings.byteStuffingTerminator
                    val prefix = binaryDescriptor.structureSettings.prefix?.array ?: byteArrayOf()
                    val postfix = binaryDescriptor.structureSettings.postfix?.array ?: byteArrayOf()
                    val bodyAndChecksum = buildByteArray(binaryDescriptor.byteOrder, classBuilder.expectedSize - prefix.size - postfix.size) {
                        with(classBuilder) { buildBodyAndChecksum() }
                    }
                    val stuffed = stuffing.stuff(bodyAndChecksum)
                    builder.addAction(prefix.size + stuffed.size + (if (terminator != null) 1 else 0) + postfix.size) {
                        add(prefix)
                        add(stuffed)
                        terminator?.let { add(it) }
                        add(postfix)
                    }
                    if (terminator == null && !isRootFrame) {
                        builder.makeUnconstrained()
                    }
                } else {
                    builder.addAction(classBuilder.expectedSize) {
                        // Make sure the parent builder includes this builder in its build tree
                        with(classBuilder) {
                            build()
                        }
                    }
                }
                if (markUnconstrained || binaryDescriptor.sizePolymorphicMap.isNotEmpty() || binaryDescriptor.sizePolymorphicFallback != null) {
                    // A size-tagged sealed type marks the parent unconstrained after encoding:
                    // any sibling field following it cannot determine its length boundary, so
                    // DataAfterUnconstrainedData will fire if one is attempted. Prefix/Postfix/CRC
                    // on the parent are unaffected — they are written in build(), not addAction().
                    builder.makeUnconstrained()
                }
            },
        ) { index ->
            binaryDescriptor.children[index]
        }
    }

    override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder {
        val collectionSettings =
            binaryDescriptor.collectionSettings
                ?: CollectionSettings(CollectionSettings.NumericLength(setOf(Length.`8_BIT`)), false)

        if (binaryDescriptor.isNullable) {
            // Reaching here means the collection is not null, so it is only absent when [NullIfEmpty] turns an
            // empty one into a null. Without that annotation an empty collection is still present, and its count
            // is written below — clearing the flag would make the decoder return null and misread that count.
            builder.addFlag(binaryDescriptor.bitIndex, collectionSize > 0 || !collectionSettings.nullIfEmpty)
        }

        // Encode length if necessary
        when (val lengthMarking = collectionSettings.lengthMarking) {
            is CollectionSettings.LengthPrefix ->
                if (collectionSize > 0 || !collectionSettings.nullIfEmpty) {
                    builder.addAction(lengthMarking.endMarking.expectedByteSize(collectionSize.toUInt())) {
                        add(binaryDescriptor.encodeCollectionSize(lengthMarking.endMarking, collectionSize.toUInt()))
                    }
                }

            is CollectionSettings.NumericLength ->
                if (collectionSize > 0 || !collectionSettings.nullIfEmpty) {
                    builder.encodeNumericElement(
                        collectionSize,
                        binaryDescriptor,
                        NumericSettings.Natural(lengthMarking.supportedLengths, false),
                    )
                }

            is CollectionSettings.FlagIndexedLength -> {
                // Pack the count into the parent's flag header at the collection's bitIndex,
                // least-significant bit first (mirrors how flag-indexed integers and enum ordinals work).
                val raw = collectionSize.toLong()
                for (bit in 0 until lengthMarking.bits) {
                    builder.addFlag(binaryDescriptor.bitIndex + bit, (raw shr bit) and 1L == 1L)
                }
            }

            is CollectionSettings.Unmarked -> {}

            is CollectionSettings.TerminalMarked -> {}
        }

        // The terminator byte for a null-marked collection (null when the collection is not terminator-marked); used
        // only for the non-stuffed start-with-terminator safety check, as stuffed collections escape it.
        val terminator = (collectionSettings.lengthMarking as? CollectionSettings.TerminalMarked)?.terminator

        var markUnconstrained = false
        val binaryBuilder = when (descriptor.kind) {
            is StructureKind.LIST -> ListBinaryBuilder(binaryDescriptor, collectionSize, terminator) { markUnconstrained = true }
            is StructureKind.MAP -> MapBinaryBuilder(binaryDescriptor, collectionSize, terminator) { markUnconstrained = true }
            else -> throw IllegalArgumentException("SerialKind ${descriptor.kind} is not Supported as a Collection")
        }

        return BinaryCompositeEncoder(
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
                when (val lengthMarking = collectionSettings.lengthMarking) {
                    is CollectionSettings.TerminalMarked -> {
                        if (collectionSize > 0 || !collectionSettings.nullIfEmpty) {
                            builder.addAction(1) { add(lengthMarking.terminator) }
                        }
                    }

                    is CollectionSettings.Unmarked -> {
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
        val offset = if (binaryDescriptor.isNullable) 1 else 0
        if (binaryDescriptor.bitIndex >= 0 && binaryDescriptor.bitWidth > offset) {
            // Packed into the flags as the ordinal, least-significant bit first.
            for (bit in 0 until binaryDescriptor.bitWidth - offset) {
                builder.addFlag(binaryDescriptor.bitIndex + offset + bit, index.isBitSet(bit))
            }
        } else {
            binaryDescriptor.enumMap[index]?.array?.let { enumBytes ->
                val prefix = binaryDescriptor.structureSettings.prefix?.array ?: byteArrayOf()
                val postfix = binaryDescriptor.structureSettings.postfix?.array ?: byteArrayOf()
                builder.addAction(prefix.size + enumBytes.size + postfix.size) {
                    add(bytes = prefix)
                    add(bytes = enumBytes)
                    add(bytes = postfix)
                }
            }
        }
    }

    override fun encodeFloat(value: Float) {
        markNotNull()
        builder.encodeFloatElement(value, binaryDescriptor)
    }

    override fun encodeInline(descriptor: SerialDescriptor): Encoder {
        val settings = binaryDescriptor.structureSettings
        if (settings.prefix == null && settings.postfix == null && settings.checksumAlgorithm == null) return this

        // binaryDescriptor.structureSettings carries the value class's boundary (set by the registry).
        // ClassBinaryBuilder.build() handles prefix → body → checksum → postfix exactly as for structures.
        // The underlying encoder uses a descriptor with cleared structureSettings so that encodeEnum and
        // other methods that read structureSettings do not double-apply the boundary inside the body.
        val classBuilder = ClassBinaryBuilder(binaryDescriptor) {}
        val bodyDescriptor = binaryDescriptor.copy(structureSettings = FrameSettings(null, null, null))
        // A value class has no children, so it has no flag header of its own. Any bit its wrapped value needs
        // (nullability, @FlagIndex, @Size width selection) lives in the enclosing structure's header — the same
        // place as when the value class carries no frame, and the place the decoder reads it from. Only body
        // bytes go inside the frame, so route addFlag to the parent and everything else to the framed builder.
        val bodyBuilder = object : BinaryBuilder by classBuilder {
            override fun addFlag(index: Int, value: Boolean) = builder.addFlag(index, value)
        }
        val underlyingEncoder = BinaryEncoder(bodyDescriptor, bodyBuilder, serializersModule)
        var finalized = false
        fun finalizeToParent() {
            if (finalized) return
            finalized = true
            // A stuffed value class stuffs its body + checksum within the prefix/postfix, then appends its terminator
            // (or marks the parent unconstrained when unterminated) — the same framing a nested stuffed structure uses.
            // The root region is bounded by the whole payload, so it is stuffed without a terminator.
            val stuffing = settings.byteStuffing
            if (stuffing != null) {
                binaryDescriptor.requireLeastSignificantFirstForStuffing()
                val terminator = if (isRootFrame) null else settings.byteStuffingTerminator
                val prefix = settings.prefix?.array ?: byteArrayOf()
                val postfix = settings.postfix?.array ?: byteArrayOf()
                val bodyAndChecksum = buildByteArray(binaryDescriptor.byteOrder, classBuilder.expectedSize - prefix.size - postfix.size) {
                    with(classBuilder) { buildBodyAndChecksum() }
                }
                val stuffed = stuffing.stuff(bodyAndChecksum)
                builder.addAction(prefix.size + stuffed.size + (if (terminator != null) 1 else 0) + postfix.size) {
                    add(prefix)
                    add(stuffed)
                    terminator?.let { add(it) }
                    add(postfix)
                }
                if (terminator == null && !isRootFrame) builder.makeUnconstrained()
            } else {
                builder.addAction(classBuilder.expectedSize) { with(classBuilder) { build() } }
            }
        }

        return object : Encoder by underlyingEncoder {
            override fun encodeBoolean(value: Boolean) {
                underlyingEncoder.encodeBoolean(value)
                finalizeToParent()
            }
            override fun encodeByte(value: Byte) {
                underlyingEncoder.encodeByte(value)
                finalizeToParent()
            }
            override fun encodeChar(value: Char) {
                underlyingEncoder.encodeChar(value)
                finalizeToParent()
            }
            override fun encodeShort(value: Short) {
                underlyingEncoder.encodeShort(value)
                finalizeToParent()
            }
            override fun encodeInt(value: Int) {
                underlyingEncoder.encodeInt(value)
                finalizeToParent()
            }
            override fun encodeLong(value: Long) {
                underlyingEncoder.encodeLong(value)
                finalizeToParent()
            }
            override fun encodeFloat(value: Float) {
                underlyingEncoder.encodeFloat(value)
                finalizeToParent()
            }
            override fun encodeDouble(value: Double) {
                underlyingEncoder.encodeDouble(value)
                finalizeToParent()
            }
            override fun encodeString(value: String) {
                underlyingEncoder.encodeString(value)
                finalizeToParent()
            }
            override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
                underlyingEncoder.encodeEnum(enumDescriptor, index)
                finalizeToParent()
            }

            // A value class wrapping a structure/collection encodes its content through encodeSerializableValue; route it
            // through this wrapper (not the delegated underlyingEncoder) so the overridden beginStructure/beginCollection
            // flush the framed body to the parent.
            override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) = serializer.serialize(this, value)

            // A value class wrapping a nullable still has its frame on the wire when the value is absent — only the
            // body bytes drop out — so an absent value must flush too. Route the nullable dispatch through this
            // wrapper as well, since the delegate's default would call its own encodeNull and skip the frame.
            @ExperimentalSerializationApi
            override fun <T : Any> encodeNullableSerializableValue(serializer: SerializationStrategy<T>, value: T?) = if (value == null) {
                encodeNull()
            } else {
                encodeSerializableValue(serializer, value)
            }

            @ExperimentalSerializationApi
            override fun encodeNull() {
                underlyingEncoder.encodeNull()
                finalizeToParent()
            }

            override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
                val inner = underlyingEncoder.beginStructure(descriptor)
                return object : CompositeEncoder by inner {
                    override fun endStructure(descriptor: SerialDescriptor) {
                        inner.endStructure(descriptor)
                        finalizeToParent()
                    }
                }
            }

            override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder {
                val inner = underlyingEncoder.beginCollection(descriptor, collectionSize)
                return object : CompositeEncoder by inner {
                    override fun endStructure(descriptor: SerialDescriptor) {
                        inner.endStructure(descriptor)
                        finalizeToParent()
                    }
                }
            }
        }
    }

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

    // Called by kotlinx's nullable serializer before a non-null value. Primitives also mark themselves when they
    // encode, but a structure, collection or value class only gets its presence flag set here.
    @ExperimentalSerializationApi
    override fun encodeNotNullMark() = markNotNull()

    private fun markNotNull() {
        if (binaryDescriptor.isNullable) {
            builder.addFlag(binaryDescriptor.bitIndex, true)
        }
    }
}

private class BinaryCompositeEncoder(
    private val binaryDescriptor: BinaryDescriptor,
    private val builder: BinaryBuilder,
    override val serializersModule: SerializersModule,
    private val onFinishStructure: () -> Unit,
    private val getBinaryDescriptor: (Int) -> BinaryDescriptor,
) : CompositeEncoder {

    private fun BinaryBuilder.writeReservedBytes(count: Int) {
        if (count > 0) addAction(count) { add(bytes = ByteArray(count)) }
    }

    private inline fun withReserved(index: Int, block: () -> Unit) {
        val desc = getBinaryDescriptor(index)
        builder.writeReservedBytes(desc.reservedBefore)
        block()
        builder.writeReservedBytes(desc.reservedAfter)
    }

    override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean) = withReserved(index) {
        builder.encodeBooleanElement(value, getBinaryDescriptor(index))
    }

    override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte) = withReserved(index) {
        builder.encodeByteElement(value, getBinaryDescriptor(index))
    }

    override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char) = withReserved(index) {
        builder.encodeCharElement(value, getBinaryDescriptor(index))
    }

    override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double) = withReserved(index) {
        builder.encodeDoubleElement(value, getBinaryDescriptor(index))
    }

    override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float) = withReserved(index) {
        builder.encodeFloatElement(value, getBinaryDescriptor(index))
    }

    override fun encodeInlineElement(descriptor: SerialDescriptor, index: Int): Encoder {
        val desc = getBinaryDescriptor(index)
        builder.writeReservedBytes(desc.reservedBefore)
        // reservedAfter for inline elements is written by the nested encoder finishing
        return BinaryEncoder(desc, builder, serializersModule)
    }

    override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int) = withReserved(index) {
        builder.encodeIntElement(value, getBinaryDescriptor(index))
    }

    override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long) = withReserved(index) {
        builder.encodeLongElement(value, getBinaryDescriptor(index))
    }

    @ExperimentalSerializationApi
    override fun <T : Any> encodeNullableSerializableElement(descriptor: SerialDescriptor, index: Int, serializer: SerializationStrategy<T>, value: T?) {
        builder.addFlag(getBinaryDescriptor(index).bitIndex, value != null)
        value?.let {
            encodeSerializableElement(descriptor, index, serializer, value)
        }
    }

    override fun <T> encodeSerializableElement(descriptor: SerialDescriptor, index: Int, serializer: SerializationStrategy<T>, value: T) = withReserved(index) {
        when (descriptor.kind) {
            is PolymorphicKind.SEALED -> {
                val binaryDescriptor = getBinaryDescriptor(index).children.first { binaryDescriptor ->
                    binaryDescriptor.fieldName == serializer.descriptor.serialName
                }
                BinaryEncoder(binaryDescriptor, builder, serializersModule).encodeSerializableValue(serializer, value)
            }

            is PolymorphicKind.OPEN -> {
                val binaryDescriptor = binaryDescriptor.children.first { binaryDescriptor -> binaryDescriptor.fieldName == serializer.descriptor.serialName }
                BinaryEncoder(binaryDescriptor, builder, serializersModule).encodeSerializableValue(serializer, value)
            }

            else -> {
                val binaryDescriptor = getBinaryDescriptor(index)
                BinaryEncoder(binaryDescriptor, builder, serializersModule).encodeSerializableValue(serializer, value)
            }
        }
    }

    override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short) = withReserved(index) {
        builder.encodeShortElement(value, getBinaryDescriptor(index))
    }

    override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String) {
        if (descriptor.kind is PolymorphicKind && index == 0) {
            if (binaryDescriptor.sizePolymorphicMap.isNotEmpty() || binaryDescriptor.sizePolymorphicFallback != null) {
                // Size-polymorphic: the payload size is the implicit discriminator — nothing to write.
                return
            }
            // Byte-prefix-based: write the type identifier bytes.
            binaryDescriptor.polymorphicMap[value]?.array?.let {
                builder.addAction(it.size) {
                    add(it)
                }
            } ?: throw IllegalStateException("Polymorphic class for $value has not been annotated with SerializedByteValue")
        } else {
            withReserved(index) { builder.encodeStringElement(value, getBinaryDescriptor(index)) }
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        onFinishStructure()
    }
}

// A [LengthPrefix] collection count. The count VALUE is encoded in childByteOrder, but a [WithOverflow] sentinel is
// placed according to the accumulation direction — the direction the decoder scans the bytes in. The two coincide
// unless @ByteOrder(order, excludeStructure = true) is in play.
private fun BinaryDescriptor.encodeCollectionSize(endMarking: StringEncodingSettings.LengthPrefix, size: UInt): ByteArray {
    val bytes = endMarking.encodeSize(size, childByteOrder)
    // Only an overflowing WithOverflow prefix has a sentinel to place; anything else is just the count.
    if (byteOrder == childByteOrder || endMarking !is StringEncodingSettings.LengthPrefix.WithOverflow || bytes.size == 1) return bytes
    // encodeSize put the sentinel at the childByteOrder end; move it to the accumulation-direction end.
    return when (byteOrder) {
        ByteOrder.MOST_SIGNIFICANT_FIRST -> bytes.copyOfRange(1, bytes.size) + bytes[0]
        ByteOrder.LEAST_SIGNIFICANT_FIRST -> byteArrayOf(bytes[bytes.size - 1]) + bytes.copyOfRange(0, bytes.size - 1)
    }
}

internal fun BinaryBuilder.encodeBooleanElement(value: Boolean, binaryDescriptor: BinaryDescriptor) {
    val offset = if (binaryDescriptor.isNullable) 1 else 0
    if (binaryDescriptor.bitIndex >= 0 && binaryDescriptor.bitWidth > offset) {
        addFlag(binaryDescriptor.bitIndex + offset, value)
    } else {
        addBit(value)
    }
}

internal fun BinaryBuilder.encodeNumericElement(value: Number, binaryDescriptor: BinaryDescriptor, settings: NumericSettings) {
    when (settings) {
        is NumericSettings.Natural -> settings.inFlagsBits?.let { bits ->
            // Sub-byte value packed straight into the flag region, least-significant bit first (like an enum ordinal).
            val raw = value.toLong()
            require(NumericCodec.fitsInBits(raw, bits, settings.signed)) {
                "Value $raw does not fit in a ${if (settings.signed) "signed" else "unsigned"} $bits-bit flag field"
            }
            for (bit in 0 until bits) {
                addFlag(binaryDescriptor.bitIndex + bit, (raw shr bit) and 1L == 1L)
            }
        } ?: run {
            val supportedLengths = settings.supportedLengths
            // Grab desired length
            val lengthToAdd = when (supportedLengths.size) {
                0 -> throw IllegalArgumentException("Size should be set")

                1 -> supportedLengths.first()

                else -> {
                    val (lengthIndex, length) = NumericCodec.selectLength(supportedLengths, value, settings.signed)

                    // Set the flags according to the picked size
                    val offset = if (binaryDescriptor.isNullable) 1 else 0
                    for (offsetIndex in offset..<binaryDescriptor.bitWidth) {
                        addFlag(binaryDescriptor.bitIndex + offsetIndex, lengthIndex.isBitSet(offsetIndex - offset))
                    }

                    length
                }
            }
            addAction(lengthToAdd.bytes) {
                add(bytes = ByteArray(lengthToAdd.bytes).also { NumericCodec.writeNatural(it, 0, value, lengthToAdd, settings.signed, binaryDescriptor.childByteOrder) })
            }
        }

        is NumericSettings.RangeEncoded -> {
            val wireValue = NumericCodec.rangeToWire(settings, value)
            encodeNumericElement(wireValue, binaryDescriptor, NumericSettings.Natural(settings.supportedLengths, false))
        }

        is NumericSettings.Scalar -> {
            val scaledValue = NumericCodec.scalarToWire(settings, value)
            encodeNumericElement(scaledValue, binaryDescriptor, NumericSettings.Natural(settings.supportedLengths, settings.signed))
        }

        is NumericSettings.Decimal -> {
            val lengthToAdd = if (settings.supportedLengths.size > 1) {
                val flagIndex = binaryDescriptor.bitIndex + if (binaryDescriptor.isNullable) 1 else 0
                NumericCodec.decimalLength(value).also { addFlag(flagIndex, it == Length.`64_BIT`) }
            } else {
                settings.supportedLengths.first()
            }

            addAction(lengthToAdd.bytes) {
                // Pass childByteOrder explicitly rather than relying on the builder's own direction: under
                // @ByteOrder(order, excludeStructure = true) the two differ, and the decoder reads decimals
                // in childByteOrder.
                when (lengthToAdd) {
                    Length.`32_BIT` -> add(value.toFloat(), binaryDescriptor.childByteOrder)
                    Length.`64_BIT` -> add(value.toDouble(), binaryDescriptor.childByteOrder)
                    else -> throw IllegalArgumentException("Decimal only supports 32 and 64 bit encoding")
                }
            }
        }

        is NumericSettings.MedFloat -> {
            val lengthToAdd = if (settings.supportedLengths.size > 1) {
                val flagIndex = binaryDescriptor.bitIndex + if (binaryDescriptor.isNullable) 1 else 0
                NumericCodec.medFloatLength(value.toDouble()).also { addFlag(flagIndex, it == Length.`32_BIT`) }
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

private val BinaryDescriptor.isUnsigned: Boolean get() = when (numericSettings) {
    is NumericSettings.Natural -> !numericSettings.signed
    is NumericSettings.Scalar -> !numericSettings.signed
    is NumericSettings.RangeEncoded -> false
    is NumericSettings.Decimal -> false
    is NumericSettings.MedFloat -> false
    null -> false
}

internal fun BinaryBuilder.encodeByteElement(value: Byte, binaryDescriptor: BinaryDescriptor) = encodeNumericElement(
    if (binaryDescriptor.isUnsigned) value.toUByte().toShort() else value,
    binaryDescriptor,
    binaryDescriptor.numericSettings ?: NumericSettings.Natural(setOf(Length.`8_BIT`), false),
)
internal fun BinaryBuilder.encodeShortElement(value: Short, binaryDescriptor: BinaryDescriptor) = encodeNumericElement(
    if (binaryDescriptor.isUnsigned) value.toUShort().toInt() else value,
    binaryDescriptor,
    binaryDescriptor.numericSettings ?: NumericSettings.Natural(setOf(Length.`16_BIT`), false),
)
internal fun BinaryBuilder.encodeIntElement(value: Int, binaryDescriptor: BinaryDescriptor) = encodeNumericElement(
    if (binaryDescriptor.isUnsigned) value.toUInt().toLong() else value,
    binaryDescriptor,
    binaryDescriptor.numericSettings ?: NumericSettings.Natural(setOf(Length.`32_BIT`), false),
)
internal fun BinaryBuilder.encodeLongElement(value: Long, binaryDescriptor: BinaryDescriptor) = encodeNumericElement(
    value,
    binaryDescriptor,
    binaryDescriptor.numericSettings ?: NumericSettings.Natural(setOf(Length.`64_BIT`), false),
)

internal fun BinaryBuilder.encodeFloatElement(value: Float, binaryDescriptor: BinaryDescriptor) = encodeNumericElement(
    value,
    binaryDescriptor,
    binaryDescriptor.numericSettings ?: NumericSettings.Decimal(setOf(Length.`32_BIT`)),
)
internal fun BinaryBuilder.encodeDoubleElement(value: Double, binaryDescriptor: BinaryDescriptor) = encodeNumericElement(
    value,
    binaryDescriptor,
    binaryDescriptor.numericSettings ?: NumericSettings.Decimal(setOf(Length.`64_BIT`)),
)

internal fun BinaryBuilder.encodeStringElement(value: String, binaryDescriptor: BinaryDescriptor) {
    val encoding = binaryDescriptor.stringSettings?.encoding ?: Encoding.UTF_8
    val endMarking = binaryDescriptor.stringSettings?.endMarking ?: StringEncodingSettings.LengthPrefix.ByteLength
    val settings = StringEncodingSettings(endMarking, encoding)
    // byteOrder, not childByteOrder: a String's layout follows the accumulation direction, which is the direction
    // the decoder reads its bytes back in.
    addAction(value.byteArraySize(settings)) { add(value, settings, binaryDescriptor.byteOrder) }
    if (endMarking is StringEncodingSettings.NoMarking) {
        makeUnconstrained()
    }
}
internal fun BinaryBuilder.encodeCharElement(value: Char, binaryDescriptor: BinaryDescriptor) {
    val encoding = binaryDescriptor.stringSettings?.encoding ?: Encoding.ASCII
    if (encoding == Encoding.UTF_8) {
        throw UnsupportedCharEncoding("A Char has a fixed size, so it cannot be UTF-8")
    }
    addAction(encoding.byteSize) { add(value, encoding, binaryDescriptor.childByteOrder) }
}
