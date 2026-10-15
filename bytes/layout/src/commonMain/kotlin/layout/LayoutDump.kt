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
import com.splendo.kaluga.bytes.strings.StringEncodingSettings
import com.splendo.kaluga.bytes.annotations.Length

/**
 * A canonical, human-readable fingerprint of this layout: one line per [FieldLayout], children indented by two spaces.
 * A non-zero [FieldLayout.headerBits] is shown as `header`. The root line ends with its [FieldLayout.staticByteSize], every other line with its [FieldLayout.staticFieldBytes]
 * (`?` when value-dependent).
 *
 * Two layouts that encode identically dump identically, on every platform, so the dump can be compared across
 * independent compilations of the same type.
 */
@InternalBytesApi
fun FieldLayout.dump(): String = buildString { appendField(this@dump, 0) }

private fun StringBuilder.appendField(field: FieldLayout, depth: Int) {
    repeat(depth) { append("  ") }
    append(field.index).append(' ').append(field.name).append(": ")
    append(field.type.describe())
    append(" order=").append(field.byteOrder.short()).append('/').append(field.valueByteOrder.short())
    if (field.bitIndex >= 0) append(" bits=").append(field.bitIndex).append('+').append(field.bitWidth)
    if (field.isNullable) append(" nullable")
    if (field.presenceFlagIndices.isNotEmpty()) append(" presentWhen=").append(field.presenceFlagIndices.joinToString(",", "[", "]"))
    if (field.reservedBefore != 0 || field.reservedAfter != 0) append(" reserved=").append(field.reservedBefore).append('/').append(field.reservedAfter)
    field.frame.prefix?.let { append(" prefix=").append(it.hex()) }
    field.frame.postfix?.let { append(" postfix=").append(it.hex()) }
    field.frame.checksum?.let { append(" checksum=").append(it.describe()) }
    field.frame.byteStuffing?.let { append(" stuffing=").append(it.describe()) }
    field.frame.byteStuffingTerminator?.let { append(" terminator=").append(it.hex()) }
    if (field.headerBits > 0) append(" header=").append(field.headerBits)
    if (depth == 0) {
        append(" static=").append(field.staticByteSize?.toString() ?: "?")
    } else {
        append(" bytes=").append(field.staticFieldBytes?.toString() ?: "?")
    }
    append('\n')

    when (val type = field.type) {
        is TypeLayout.Enumeration -> type.values.forEach { (ordinal, bytes) -> appendDetail(depth, "value $ordinal=${bytes.hex()}") }

        is TypeLayout.Sealed -> {
            type.discriminators.forEach { (name, bytes) -> appendDetail(depth, "discriminator $name=${bytes.hex()}") }
            type.sizeDispatch.forEach { (size, name) -> appendDetail(depth, "size $size -> $name") }
            type.sizeFallback?.let { appendDetail(depth, "size * -> $it") }
        }

        is TypeLayout.Open -> type.discriminators.forEach { (name, bytes) -> appendDetail(depth, "discriminator $name=${bytes.hex()}") }

        else -> Unit
    }
    field.type.children.forEach { appendField(it, depth + 1) }
}

private fun StringBuilder.appendDetail(depth: Int, detail: String) {
    repeat(depth + 1) { append("  ") }
    append("= ").append(detail).append('\n')
}

private fun TypeLayout.describe(): String = when (this) {
    is TypeLayout.Bool -> "bool"
    is TypeLayout.Numeric -> settings.describe()
    is TypeLayout.Text -> "string(${settings.encoding} ${settings.endMarking.describe()})"
    is TypeLayout.Character -> "char($encoding)"
    is TypeLayout.Enumeration -> "enum"
    is TypeLayout.Structure -> "struct"
    is TypeLayout.ListLayout -> "list(${settings.describe()})"
    is TypeLayout.MapLayout -> "map(${settings.describe()})"
    is TypeLayout.Sealed -> "sealed"
    is TypeLayout.Open -> "open"
    is TypeLayout.Contextual -> "contextual"
}

private fun NumericSettings.describe(): String = when (this) {
    is NumericSettings.Natural -> if (inFlagsBits != null) {
        "natural(flags=$inFlagsBits ${signedness(signed)})"
    } else {
        "natural(${supportedLengths.describe()} ${signedness(signed)})"
    }

    is NumericSettings.Scalar ->
        "scalar(${supportedLengths.describe()} ${signedness(signed)} x$multiplier e10=$decimalExponent e2=$binaryExponent offset=$offset)"

    is NumericSettings.Decimal -> "decimal(${supportedLengths.describe()})"

    is NumericSettings.MedFloat -> "medfloat(${supportedLengths.describe()})"

    is NumericSettings.RangeEncoded -> "range(${supportedLengths.describe()} min=${min.canonical()} max=${max.canonical()})"
}

private fun CollectionSettings.describe(): String {
    val marking = when (val marking = lengthMarking) {
        is CollectionSettings.LengthPrefix -> marking.endMarking.describe()
        is CollectionSettings.TerminalMarked -> "terminated(${marking.terminator.hex()})"
        is CollectionSettings.Unmarked -> "unmarked"
        is CollectionSettings.NumericLength -> "count(${marking.supportedLengths.describe()})"
        is CollectionSettings.FlagIndexedLength -> "flagCount(${marking.bits})"
    }
    return buildString {
        append(marking)
        if (nullIfEmpty) append(" nullIfEmpty")
        byteStuffing?.let { append(" stuffing=").append(it.describe()) }
    }
}

private fun StringEncodingSettings.EndMarking.describe(): String = when (this) {
    is StringEncodingSettings.LengthPrefix.ByteLength -> "prefix8"
    is StringEncodingSettings.LengthPrefix.ShortLength -> "prefix16"
    is StringEncodingSettings.LengthPrefix.WithOverflow -> "prefixOverflow(${sentinel.hex()})"
    is StringEncodingSettings.NullTerminated -> "nullTerminated"
    is StringEncodingSettings.Terminated -> "terminated(${terminator.hex()})"
    is StringEncodingSettings.ByteStuffed -> "stuffed(${scheme.describe()} terminator=${terminator.hex()})"
    is StringEncodingSettings.NoMarking -> "unmarked"
    is StringEncodingSettings.FixedLength -> "fixed($length)"
}

private fun ByteStuffingScheme.describe(): String = when (this) {
    is ByteStuffingScheme.CSafe -> "csafe(escape=${escapeByte.hex()} mask=${mask.hex()})"

    is ByteStuffingScheme.Xor -> buildString {
        append("xor(escape=").append(escapeByte.hex()).append(" delimiter=").append(delimiter.hex())
        if (additionalEscapedBytes.isNotEmpty()) append(" additional=").append(additionalEscapedBytes.map { it.hex() }.sorted().joinToString(","))
        append(" key=").append(xorKey.hex()).append(')')
    }

    is ByteStuffingScheme.Slip -> "slip(end=${end.hex()} escape=${escapeByte.hex()} escapedEnd=${escapedEnd.hex()} escapedEsc=${escapedEsc.hex()})"

    is ByteStuffingScheme.Cobs -> "cobs"
}

private fun ChecksumSpec.describe(): String = buildString {
    append("crc").append(width)
    append("(poly=").append(polynomial.toString(16))
    append(" init=").append(init.toString(16))
    append(" xorOut=").append(xorOut.toString(16))
    if (reflectIn) append(" reflectIn")
    if (reflectOut) append(" reflectOut")
    append(')')
}

private fun Set<Length>.describe(): String = joinToString(",") { (it.bytes * Byte.SIZE_BITS).toString() }

private fun signedness(signed: Boolean) = if (signed) "signed" else "unsigned"

private fun ByteOrder.short(): String = when (this) {
    ByteOrder.LEAST_SIGNIFICANT_FIRST -> "LSB"
    ByteOrder.MOST_SIGNIFICANT_FIRST -> "MSB"
}

private fun ByteArrayHolder.hex(): String = if (array.isEmpty()) "[]" else array.joinToString(" ") { it.hex() }

private fun Byte.hex(): String = (toInt() and 0xFF).toString(16).padStart(2, '0')

// Double.toString differs between Kotlin/JS and the other platforms for integral values ("1" vs "1.0").
private fun Double.canonical(): String = if (this == kotlin.math.floor(this) && kotlin.math.abs(this) < 1e15) "${toLong()}.0" else toString()
