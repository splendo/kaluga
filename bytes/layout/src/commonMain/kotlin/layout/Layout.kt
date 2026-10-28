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

package com.splendo.kaluga.bytes.layout

import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.stuffing.ByteStuffingScheme
import com.splendo.kaluga.bytes.strings.Encoding
import com.splendo.kaluga.bytes.strings.StringEncodingSettings
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.toHexString
import com.splendo.kaluga.bytes.crc.CRC
import kotlin.jvm.JvmInline

/**
 * A fixed byte sequence in a layout, such as a prefix or a discriminator.
 */
@JvmInline
value class ByteArrayHolder(val array: ByteArray) {
    override fun toString(): String = array.toHexString(separator = " ")
}

/**
 * The compiled layout of one property (or of the root type) as produced by [LayoutCompiler].
 *
 * @property name the element name, or the serial name for the root and for polymorphic subtypes.
 * @property index the element index within the parent.
 * @property bitIndex the first bit this property owns in its parent's flag header, or `-1` when it owns none.
 * @property bitWidth the number of flag header bits this property owns, starting at [bitIndex].
 * The first of them is the presence bit when [isNullable] and [presenceFlagIndices] is empty; the others select a
 * size, hold an enum ordinal, a flag-packed number or a collection count.
 * @property byteOrder the direction in which this property's frame is accumulated.
 * @property valueByteOrder the byte order of the values inside this property, which differs from [byteOrder] when the
 * type uses `@ByteOrder(excludeStructure = true)`.
 * @property presenceFlagIndices the flag bits (owned by other properties) whose conjunction determines this property's
 * presence; empty for a property that owns its presence bit at [bitIndex] (or is not nullable).
 * @property reservedBefore padding bytes before the property on the wire.
 * @property reservedAfter padding bytes after the property on the wire.
 * @property frame prefix, postfix, checksum and byte stuffing wrapped around the property's body.
 * @property type what is encoded.
 */
data class FieldLayout(
    val name: String,
    val index: Int,
    val bitIndex: Int,
    val bitWidth: Int,
    val byteOrder: ByteOrder,
    val valueByteOrder: ByteOrder,
    val isNullable: Boolean,
    val presenceFlagIndices: List<Int> = emptyList(),
    val reservedBefore: Int = 0,
    val reservedAfter: Int = 0,
    val frame: FrameSettings,
    val type: TypeLayout,
) {

    /**
     * Number of bits in the flag header of this property's body: the highest bit any child owns or derives its presence from.
     */
    val headerBits: Int get() = type.children.maxOfOrNull {
        maxOf(
            if (it.bitIndex >= 0) it.bitIndex + it.bitWidth else 0,
            it.presenceFlagIndices.maxOrNull()?.plus(1) ?: 0,
        )
    } ?: 0

    /** Bytes written after the body: the checksum followed by the postfix. */
    val footerBytes: Int get() = (frame.checksum?.crc?.byteWidth ?: 0) + (frame.postfix?.array?.size ?: 0)

    /**
     * The total byte size of this layout when used as a root (flag header + body + prefix + postfix + checksum),
     * or `null` if any part of it has a value-dependent size.
     */
    val staticByteSize: Int? get() = staticRootByteSize()

    /**
     * The bytes this property contributes to its parent's body, including [reservedBefore] and [reservedAfter],
     * or `null` if that depends on the value. Flag header bits are not counted.
     */
    val staticFieldBytes: Int? get() = staticChildBodyBytes()
}

/**
 * Prefix, postfix, checksum and byte stuffing applied around a body.
 * @property checksum the checksum written right after the body (but before any postfix), in [FieldLayout.valueByteOrder].
 * @property byteStuffing when non-null, the body and checksum (class) or delimited content (property) are byte-stuffed.
 * @property byteStuffingTerminator the terminator marking the end of a nested stuffed structure (a delimiter scheme's
 * canonical delimiter, or an explicit `@Terminal`); `null` means the structure is unsized and bounded by the remaining payload.
 */
data class FrameSettings(
    val prefix: ByteArrayHolder?,
    val postfix: ByteArrayHolder?,
    val checksum: ChecksumSpec?,
    val byteStuffing: ByteStuffingScheme? = null,
    val byteStuffingTerminator: Byte? = null,
) {
    val checksumAlgorithm: CRC? get() = checksum?.crc
}

/**
 * The parameters of a CRC checksum, kept so the algorithm can be described and regenerated.
 */
data class ChecksumSpec(val width: Int, val polynomial: ULong, val init: ULong, val xorOut: ULong, val reflectIn: Boolean, val reflectOut: Boolean) {
    val crc: CRC = CRC(width, polynomial, init, xorOut, reflectIn, reflectOut)
}

/**
 * What a [FieldLayout] encodes. Its [children] follow the element structure of the [TypeShape] it was compiled from.
 */
sealed interface TypeLayout {

    val children: List<FieldLayout>

    /** A boolean: a flag bit when one is owned, otherwise a bit in the body. */
    data object Bool : TypeLayout {
        override val children: List<FieldLayout> get() = emptyList()
    }

    data class Numeric(val settings: NumericSettings) : TypeLayout {
        override val children: List<FieldLayout> get() = emptyList()
    }

    data class Text(val settings: StringSettings) : TypeLayout {
        override val children: List<FieldLayout> get() = emptyList()
    }

    data class Character(val encoding: Encoding) : TypeLayout {
        override val children: List<FieldLayout> get() = emptyList()
    }

    /**
     * @property values the byte identifier of each entry by ordinal, used when the enum is not flag-packed.
     * @property entries one layout per entry.
     */
    data class Enumeration(val values: Map<Int, ByteArrayHolder>, val entries: List<FieldLayout>) : TypeLayout {
        override val children: List<FieldLayout> get() = entries
    }

    data class Structure(val fields: List<FieldLayout>) : TypeLayout {
        override val children: List<FieldLayout> get() = fields
    }

    data class ListLayout(val settings: CollectionSettings, val item: FieldLayout) : TypeLayout {
        override val children: List<FieldLayout> get() = listOf(item)
    }

    data class MapLayout(val settings: CollectionSettings, val key: FieldLayout, val value: FieldLayout) : TypeLayout {
        override val children: List<FieldLayout> get() = listOf(key, value)
    }

    /**
     * A sealed hierarchy.
     * @property discriminators the byte prefix of each subtype by serial name, in declaration order.
     * @property sizeDispatch when non-empty, the subtype is selected by remaining payload byte count instead of a byte prefix.
     * @property sizeFallback the subtype used when no [sizeDispatch] entry matches; allows one variable-size subtype.
     * @property elements the `type` and `value` elements; the latter is [Contextual] with the subtypes as its children.
     */
    data class Sealed(val discriminators: Map<String, ByteArrayHolder>, val sizeDispatch: Map<Int, String>, val sizeFallback: String?, val elements: List<FieldLayout>) :
        TypeLayout {
        override val children: List<FieldLayout> get() = elements
    }

    /**
     * An open polymorphic type.
     * @property discriminators the byte prefix of each registered subtype by serial name, in registration order.
     * @property options one layout per registered subtype.
     */
    data class Open(val discriminators: Map<String, ByteArrayHolder>, val options: List<FieldLayout>) : TypeLayout {
        override val children: List<FieldLayout> get() = options
    }

    /** A pass-through wrapper, such as the `value` element of a [Sealed] type. */
    data class Contextual(val elements: List<FieldLayout>) : TypeLayout {
        override val children: List<FieldLayout> get() = elements
    }
}

/**
 * How a number is encoded.
 */
sealed class NumericSettings {

    abstract val supportedLengths: Set<Length>

    /**
     * Encoding as a simple number. The [Length] best matching the value will be picked
     * @property supportedLengths the [Length] values that are supported. The smallest fitting length will be picked.
     * @property signed if the value can be negative
     * @property inFlagsBits when non-null, the value is packed directly into the flag region across this many bits
     * (least-significant bit first), analogous to an enum's ordinal, rather than encoded as body bytes. Used for
     * sub-byte numeric subfields of a bit field; [supportedLengths] is then unused.
     */
    data class Natural(override val supportedLengths: Set<Length>, val signed: Boolean, val inFlagsBits: Int? = null) : NumericSettings() {
        init {
            validate(inFlagsBits != null || supportedLengths.isNotEmpty()) { "Must Support at least one Length" }
            if (inFlagsBits != null) validate(inFlagsBits in 1..Long.SIZE_BITS) { "A flag-packed numeric must occupy 1..${Long.SIZE_BITS} bits, was $inFlagsBits" }
        }
    }

    /**
     * Encoding as a Scalar Numeric value
     * @property supportedLengths the [Length] values that are supported. The smallest fitting length will be picked.
     * @property signed if the value can be negative
     * @property multiplier the multiplier to apply to the value before encoding
     * @property decimalExponent the decimal exponent with which to encode the value, so that encoded value is [value * 10^decimalExponent]
     * @property binaryExponent the binary exponent with which to encode the value, so that encoded value is [value * 2^binaryExponent]
     * @property offset the offset that will be applied to the value after all multiplications have completed
     */
    data class Scalar(override val supportedLengths: Set<Length>, val signed: Boolean, val multiplier: Int, val decimalExponent: Int, val binaryExponent: Int, val offset: Int) :
        NumericSettings() {
        init {
            validate(supportedLengths.isNotEmpty()) { "Must Support at least one Length" }
        }
    }

    /**
     * Encoding as a Decimal value
     * @property supportedLengths the [Length] values that are supported. The smallest fitting length will be picked. Can only contain 32 and 64 bit lengths
     */
    data class Decimal(override val supportedLengths: Set<Length>) : NumericSettings() {
        init {
            validate(supportedLengths.isNotEmpty()) { "Must Support at least one Length" }
            validate((supportedLengths - setOf(Length.`32_BIT`, Length.`64_BIT`)).isEmpty()) { "Decimal only supports 32 and 64 bit encoding" }
        }
    }

    /**
     * Encoding as a MedFloat value
     * @property supportedLengths the [Length] values that are supported. The smallest fitting length will be picked. Can only contain 16 and 32 bit lengths
     */
    data class MedFloat(override val supportedLengths: Set<Length>) : NumericSettings() {
        init {
            validate(supportedLengths.isNotEmpty()) { "Must Support at least one Length" }
            validate((supportedLengths - setOf(Length.`16_BIT`, Length.`32_BIT`)).isEmpty()) { "MedFloat only supports 16 and 32 bit encoding" }
        }
    }

    /**
     * Encoding as a proportional mapping across the full unsigned range of the wire integer.
     * `wire = round((value − min) / (max − min) × maxWireValue)` where `maxWireValue = 2^bits − 1`.
     * @property supportedLengths must contain exactly one entry, giving the wire bit-width.
     * @property min decoded value for wire = 0
     * @property max decoded value for wire = maxWireValue
     */
    data class RangeEncoded(override val supportedLengths: Set<Length>, val min: Double, val max: Double) : NumericSettings() {
        init {
            validate(supportedLengths.size == 1) { "@Range requires exactly one @Size, got $supportedLengths" }
            validate(max > min) { "@Range max ($max) must be greater than min ($min)" }
        }
        val length: Length get() = supportedLengths.first()
        val maxWireValue: ULong get() = ULong.MAX_VALUE shr (ULong.SIZE_BITS - length.bytes * Byte.SIZE_BITS)
    }
}

/**
 * The encoding settings to apply when encoding a String or Char
 * @property encoding the [Encoding] with which to encode each char
 * @property endMarking the [StringEncodingSettings.EndMarking] to use when encoding the string. Ignored when only encoding a [Char].
 */
data class StringSettings(val encoding: Encoding, val endMarking: StringEncodingSettings.EndMarking)

/**
 * The encoding settings to apply when encoding a Collection (List or Map)
 * @property lengthMarking the [LengthMarking] to use when encoding the collection.
 * @property nullIfEmpty if the collection should be encoded as null if it is empty. When `true` an additional null flag will be added to the flags header.
 * @property byteStuffing when non-null (only with [TerminalMarked]), the collection body is byte-stuffed so its terminator stays unambiguous.
 */
data class CollectionSettings(val lengthMarking: LengthMarking, val nullIfEmpty: Boolean, val byteStuffing: ByteStuffingScheme? = null) {

    sealed class LengthMarking
    data class LengthPrefix(val endMarking: StringEncodingSettings.LengthPrefix) : LengthMarking()

    /** The collection ends at the first (unescaped, when stuffed) [terminator] byte. Defaults to `0x00`. */
    data class TerminalMarked(val terminator: Byte = 0x00) : LengthMarking()
    data object Unmarked : LengthMarking()
    data class NumericLength(val supportedLengths: Set<Length>) : LengthMarking() {
        init {
            validate(supportedLengths.isNotEmpty()) { "Must Support at least one Length" }
        }
    }

    /**
     * The collection count is packed into [bits] bits of the parent structure's flag header,
     * starting at the collection property's own [FieldLayout.bitIndex].
     * [bits] is 8 for [ByteLength][StringEncodingSettings.LengthPrefix.ByteLength]
     * and 16 for [ShortLength][StringEncodingSettings.LengthPrefix.ShortLength].
     * No count bytes are written to the body; the list data follows the other body fields directly.
     */
    data class FlagIndexedLength(val bits: Int) : LengthMarking() {
        init {
            validate(bits in 1..32) { "FlagIndexedLength bit width must be 1..32, was $bits" }
        }
    }
}

private inline fun validate(condition: Boolean, message: () -> String) {
    if (!condition) throw InvalidNumericSettings(message())
}
