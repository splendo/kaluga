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

import com.splendo.kaluga.base.bytes.ByteOrder
import com.splendo.kaluga.base.bytes.Encoding
import com.splendo.kaluga.base.bytes.StringEncodingSettings
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialInfo

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * When applied, the encoded object will be prefixed by [value]
 * @property value the [ByteArray] to add as a Prefix
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.CLASS)
annotation class Prefix(val value: ByteArray)

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * When applied, the encoded object will be postfixed by [value]
 * @property value the [ByteArray] to add as a Postfix
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.CLASS)
annotation class Postfix(val value: ByteArray)

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * When applied to a [Serializable] enum class, variants are encoded by their ordinal plus [offset], using [size]
 * bytes in the byte order of the enclosing structure.
 * This avoids annotating every variant individually with [@SerializedByteValue][SerializedByteValue].
 *
 * A per-variant [@SerializedByteValue][SerializedByteValue] overrides [ByOrdinal] for that specific variant —
 * mixing is allowed but all resulting byte arrays must have the same byte length.
 *
 * @property offset value added to the ordinal before encoding. Use `offset = 1` for 1-based protocols.
 * @property size wire byte length of each ordinal value. Defaults to 1 byte (max 255 values + offset).
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.CLASS)
annotation class ByOrdinal(val offset: Int = 0, val size: Length = Length.`8_BIT`)

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * When applied to a property, [before] reserved (padding) bytes are silently skipped before decoding this
 * property and zero-filled bytes are written before encoding it; [after] bytes are skipped/written after it.
 *
 * Use this for Bluetooth profiles that include reserved or padding bytes that carry no information.
 * Unlike [@Postfix][Postfix], `@Reserved` does **not** validate the byte values on decode — any content is silently discarded.
 *
 * @property before number of reserved bytes immediately before this property in the wire format
 * @property after number of reserved bytes immediately after this property in the wire format
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class Reserved(val before: Int = 0, val after: Int = 0)

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * When applied, the position of the header flag(s) to be used for storing headers will be set to [index].
 * If applied to a Boolean, the boolean will be stored as a flag at [index] instead of within the body itself.
 * If applied to an enum, its ordinal is stored in the flags starting at [index], across enough bits for all its
 * cases (i.e. `ceil(log2(caseCount))`) unless a wider [FlagWidth] is given.
 * If applied to an integer with [@FlagWidth][FlagWidth] but no [@Size][Size], the integer value is packed
 * directly into the flag bits (sub-byte numeric subfield).
 * If applied to a List/Map together with [@LengthPrefix][LengthPrefix], the element count is packed into
 * the flag header bits starting at [index] (8 bits for byte-length, 16 bits for short-length).
 *
 * If the index was already claimed by another property a [FlagIndexException] may be thrown.
 *
 * @property index the position of the header flag(s) to be used for storing headers
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class FlagIndex(val index: Int)

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * Marks a nullable property whose presence is *derived* from the flag bits at [indices] rather than from a dedicated
 * flag bit of its own: it is present on the wire exactly when all of those bits are set, and reserves no flag bit
 * itself. The listed bits are owned (and reserved) by the properties or flags they belong to. Use for a field gated by
 * a compound condition such as "present if C1 and C2".
 *
 * @property indices the flag bit positions that must all be set for this property to be present
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class PresentWhenAllSet(vararg val indices: Int)

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * When applied, the width of the flags to be used by this element will be at minimum [bits] bits
 *
 * @property bits the number of bits the flag for this property will take up at a minimum
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class FlagWidth(val bits: Int = 1)

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * When applied, the [com.splendo.kaluga.base.bytes.ByteOrder] in which this element is encoded is set to [order].
 * When applied to a class or collection, a [InvalidByteOrderException] may be thrown if the byte order changed.
 *
 * @property order the [com.splendo.kaluga.base.bytes.ByteOrder] in which to encode the element.
 * @property excludeStructure when `true` and applied to a class, [order] propagates as the default byte order
 *   for all child property values but does **not** affect the class's own array accumulation direction.
 *   This allows all fields in a class to encode their values in [order] while the class itself is still
 *   written sequentially rather than reversed. Has no effect when applied to a property.
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
annotation class ByteOrder(val order: ByteOrder = ByteOrder.LEAST_SIGNIFICANT_FIRST, val excludeStructure: Boolean = false)

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * When applied to a String, a [StringEncodingSettings.LengthPrefix] will be used as the [StringEncodingSettings.endMarking].
 * Similar encoding will be used to add a length to a List/Map.
 * Defaults to [StringEncodingSettings.LengthPrefix.ByteLength]
 *
 * **Combined with [@FlagIndex][FlagIndex] on a List/Map**: instead of writing count bytes into the body,
 * the element count is packed into the parent structure's flag header at the bit position given by
 * [@FlagIndex][FlagIndex]. The bit width is 8 for the default [ByteLength][StringEncodingSettings.LengthPrefix.ByteLength]
 * or 16 for [ShortLength][StringEncodingSettings.LengthPrefix.ShortLength]. This lets the count
 * occupy flag-header bytes that are laid out before the body fields — useful when the wire format
 * places the count before unrelated body fields that precede the list data (e.g. a packet header
 * that has [count][2B] [otherField][4B] [anotherField][4B] [items…]).
 *
 * @property lengthAsShort if `true` will use [StringEncodingSettings.LengthPrefix.ShortLength] (16-bit count)
 * @property canOverflow if `true` will use [StringEncodingSettings.LengthPrefix.WithOverflow] (also 16-bit when combined with [@FlagIndex][FlagIndex])
 * @property sentinel the [Byte] to use as the sentinel for [StringEncodingSettings.LengthPrefix.WithOverflow]
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class LengthPrefix(val lengthAsShort: Boolean = false, val canOverflow: Boolean = false, val sentinel: Byte = 0xFF.toByte())

internal fun LengthPrefix.asLengthPrefix() = when {
    lengthAsShort -> StringEncodingSettings.LengthPrefix.ShortLength
    canOverflow -> StringEncodingSettings.LengthPrefix.WithOverflow(sentinel)
    else -> StringEncodingSettings.LengthPrefix.ByteLength
}

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * When applied to a [String] or [Char], [encoding] will be used for encoding each char.
 *
 * @property encoding the [Encoding] to use.
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class Encoded(val encoding: Encoding = Encoding.UTF_8)

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * When applied to a String or Collection, they will be marked with `0x00` to indicate completion.
 * Will throw an exception if:
 * - Encoding a String and it contains `\u0000`
 * - Encoding a List and an encoded item starts with `0x00`
 * - Encoding a Map and an encoded key starts with `0x00`
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class NullTerminated

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * When applied to a numeric value, will encode as an unsigned value.
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class Unsigned

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * When applied to a numeric value, will calculate an integer using (`value * [multiplier] * 2.pow([binaryExponent]) * 10.pow([decimalExponent]) + [offset])`)
 *
 * Combine with any [Size] to set the preferred [Length] of the value to be encoded
 *
 * @property multiplier the multiplier to multiply the value to be encoded with
 * @property decimalExponent the exponent of the decimal component the value will be multiplied with so that `scaled = value * 10.pow(decimalExponent)`
 * @property binaryExponent the exponent of the binary component the value will be multiplied with so that `scaled = value * 2.pow(binaryExponent)`
 * @property offset the offset the value will be offset by so that `scaled = value + offset`
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class Scalar(val multiplier: Int = 1, val decimalExponent: Int = 0, val binaryExponent: Int = 0, val offset: Int = 0)

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * When applied to a numeric (Float or Double) field, encodes the value as a proportional mapping
 * across the full unsigned range of the wire integer:
 *   `wire = round((value − min) / (max − min) × (2^bits − 1))`
 *   `value = wire / (2^bits − 1) × (max − min) + min`
 *
 * Use [@Size][Size] to control the wire bit-width (defaults to the field type's natural size).
 * A single [@Size][Size] is required; multiple sizes are not supported.
 *
 * Example — 16-bit full-range humidity (0x0000 = 0 %, 0xFFFF = 100 %):
 * ```kotlin
 * @Range(0.0, 100.0) @Size(Length.`16_BIT`) val percent: Double
 * ```
 *
 * @property min the decoded value corresponding to a wire value of 0x00…0
 * @property max the decoded value corresponding to the maximum wire value (0xFF…F for the given bit-width)
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class Range(val min: Double = 0.0, val max: Double = 1.0)

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * When applied to a numeric value, it will be encoded as either [com.splendo.kaluga.base.bytes.MedFloat16] or [com.splendo.kaluga.base.bytes.MedFloat32], depending on [Size]
 * Can only add [Length.`16_BIT`] or [Length.`32_BIT`] using [Size].
 * Size defaults to:
 * - [Length.`16_BIT`] for [Byte], [Short], and [Float]
 * - [Length.`32_BIT`] for [Int], [Long], and [Double]
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class MedFloat

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * When applied to a numeric value this will set [length] as the length to be encoded.
 * When applied to Double or Float, [length] must be either [Length.`32_BIT`] or [Length.`64_BIT`].
 * When combined with [MedFloat], [length] must be either [Length.`16_BIT`] or [Length.`32_BIT`].
 *
 * When applied to a Collection, will determine the length of the size indicating bytes.
 *
 * When this flag is repeated, the best fitting [length] will be picked and flags will be added to the header to indicate which value was picked
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Repeatable
@Target(AnnotationTarget.PROPERTY)
annotation class Size(val length: Length)

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * When applied to a String or Collection, no end marking will be applied.
 * This will result in a [DataAfterUnconstrainedData] exception if data (besides checksum or postfix) is added after this element.
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class Unsized

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * When applied to a Collection, a null flag will be added and set to 0 if the list is empty (resulting in no length being encoded in the body).
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class NullIfEmpty

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * When applied to a sealed polymorphic class, subtypes are identified by their remaining payload
 * byte count instead of a prefix byte identifier. The expected byte count for each subtype is
 * computed automatically from its field annotations — no manual size declaration is required.
 *
 * All subtypes must have statically-known sizes (fixed-width numerics and nested structures only).
 * A subtype with variable-size fields (unsized strings, collections, nullable body fields, etc.)
 * causes a [SerializationException] at descriptor construction time.
 *
 * A field of a [SizePolymorphic] type must be the last field in its parent structure; any sibling
 * that follows will throw [DataAfterUnconstrainedData] when encoding. [Prefix], [Postfix], and
 * [Checksum] on the parent class are unaffected — they are written in [BinaryBuilder.build], not
 * via [BinaryBuilder.addAction].
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.CLASS)
annotation class SizePolymorphic

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * Adds a [com.splendo.kaluga.base.crc.CRC] value of size [width] to the end of the body (before any prefix)
 * If the decoded checksum does not match the calculated checksum of the body and [BluetoothFormat.validateChecksum] is `true` will result in a [InvalidChecksumException].
 *
 * @see [com.splendo.kaluga.base.crc.CRC.invoke]
 *
 * @param width the width of the CRC in bits. Must be between 1 and 64 bits.
 * @param polynomial the polynomial used to compute the CRC.
 * @param init the initial value of the CRC.
 * @param xorOut the value to XOR with the result to get the final CRC.
 * @param reflectIn whether to reflect the input bytes before computing the CRC.
 * @param reflectOut whether to reflect the output bytes after computing the CRC.
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.CLASS)
annotation class Checksum(val width: Int, val polynomial: ULong, val init: ULong, val xorOut: ULong = 0u, val reflectIn: Boolean = false, val reflectOut: Boolean = false)

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * Can be added to elements of an Enum or Polymorphic class. This replaces serializing using the encoded SerialName with [value].
 *
 * All entries in the same sealed class or enum must supply the same number of bytes.
 * The format validates this at descriptor construction time and throws if lengths are inconsistent.
 *
 * @property value the byte sequence to use as an identifier. A single byte is the common case; multi-byte
 *   values allow 2-byte (or wider) discriminators as long as every entry in the class uses the same length.
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
annotation class SerializedByteValue(vararg val value: Byte)

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * Applies CSafe-style byte stuffing so a delimiter never appears literally in the content, using a
 * [com.splendo.kaluga.base.bytes.ByteStuffingScheme.CSafe] scheme. Only for [ByteOrder.LEAST_SIGNIFICANT_FIRST].
 *
 * - On a **class** the body and any [Checksum] are stuffed after encoding (and un-stuffed before decoding), while a
 *   [Prefix] and [Postfix] frame the stuffed region untouched — the CSafe frame `[start flag][stuffed body+checksum][stop flag]`.
 * - On a **String or Collection property** it is itself a `0x00`-terminated marking (no [NullTerminated] needed), stuffing the
 *   content so the terminator stays unambiguous. As CSafe does not escape `0x00`, use [ByteStuffedXor] there instead.
 *
 * The escaped values are the contiguous `[mask]`-aligned block sharing the high bits of [escapeByte]; defaults escape `0xF0..0xF3`.
 *
 * @property escapeByte the byte prepended to an escaped value (and itself always escaped)
 * @property mask the low-bit mask of the escaped block sharing the high bits of [escapeByte]
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class ByteStuffed(val escapeByte: Byte = 0xF3.toByte(), val mask: Byte = 0x03)

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * Terminates a String or Collection with [terminator] instead of the default `0x00`.
 *
 * - **With** a byte-stuffing annotation ([ByteStuffed] / [ByteStuffedXor]) the content is stuffed so it may contain
 *   [terminator] freely; the scheme must escape [terminator].
 * - **Without** stuffing it is a plain byte terminator: the encoded content must not contain [terminator] (encoding
 *   fails if it does), and for a Collection no item may start with it.
 *
 * @property terminator the byte marking the end of the String or Collection
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
annotation class Terminal(val terminator: Byte)

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * Applies PPP/HDLC-style byte stuffing, using a [com.splendo.kaluga.base.bytes.ByteStuffingScheme.Xor] scheme. Only for
 * [ByteOrder.LEAST_SIGNIFICANT_FIRST]. Applies in the same positions as [ByteStuffed]; because the delimiter is explicit,
 * this can protect a `0x00` terminator (use `delimiter = 0x00` on a String or Collection — no [NullTerminated] needed).
 *
 * Each escaped byte is stored XOR-ed with [xorKey]. [escapeByte] and [delimiter] are always escaped in addition to [additionalEscapedBytes].
 *
 * @property escapeByte the byte prepended to an escaped value (and itself always escaped)
 * @property delimiter the terminator byte the content is framed with (always escaped)
 * @property additionalEscapedBytes further byte values to escape beyond [escapeByte] and [delimiter]
 * @property xorKey the value XOR-ed with an escaped byte to produce its stored form
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class ByteStuffedXor(val escapeByte: Byte, val delimiter: Byte, val additionalEscapedBytes: ByteArray = [], val xorKey: Byte = 0x20)

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * Applies SLIP-style byte stuffing (RFC 1055), using a [com.splendo.kaluga.base.bytes.ByteStuffingScheme.Slip] scheme.
 * Only for [ByteOrder.LEAST_SIGNIFICANT_FIRST]. Applies in the same positions as [ByteStuffed]. Its natural delimiter is
 * [end] (`0xC0`), so on a String or Collection the terminator defaults to [end] (override with [Terminal] if needed).
 *
 * @property end the frame delimiter byte, escaped within the content (also the default terminator)
 * @property escapeByte the escape marker
 * @property escapedEnd the stored byte representing an escaped [end]
 * @property escapedEsc the stored byte representing an escaped [escapeByte]
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class ByteStuffedSlip(val end: Byte = 0xC0.toByte(), val escapeByte: Byte = 0xDB.toByte(), val escapedEnd: Byte = 0xDC.toByte(), val escapedEsc: Byte = 0xDD.toByte())

/**
 * Annotation added for serializing using [BluetoothFormat]
 *
 * Applies COBS byte stuffing (Consistent Overhead Byte Stuffing), using [com.splendo.kaluga.base.bytes.ByteStuffingScheme.Cobs].
 * Only for [ByteOrder.LEAST_SIGNIFICANT_FIRST]. Applies in the same positions as [ByteStuffed]. COBS eliminates `0x00`
 * from the output with ~1 byte overhead per 254, so on a String or Collection it uses the default `0x00` terminator.
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class ByteStuffedCobs
