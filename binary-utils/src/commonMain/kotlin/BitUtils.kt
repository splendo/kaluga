/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

import kotlin.math.max
import kotlin.math.min

// Note:
// For signed values using `shr` would be incorrect as it would use the whatever value
// sign bit have (MSB) to fill when shifting. In that case we need to use `ushr`
//
// However here we first cast to unsigned versions so using `shr` here is correct.

infix fun Byte.shl(bitCount: Int) = (toUInt() shl bitCount).toByte()
infix fun Byte.shr(bitCount: Int) = (toUInt() shr bitCount).toByte()
infix fun UByte.shl(bitCount: Int) = (toUInt() shl bitCount).toUByte()
infix fun UByte.shr(bitCount: Int) = (toUInt() shr bitCount).toUByte()
infix fun Short.shl(bitCount: Int) = (toUInt() shl bitCount).toShort()
infix fun Short.shr(bitCount: Int) = (toUInt() shr bitCount).toShort()
infix fun UShort.shl(bitCount: Int) = (toUInt() shl bitCount).toUShort()
infix fun UShort.shr(bitCount: Int) = (toUInt() shr bitCount).toUShort()

fun UByte.toBigEndianUShort() = toUShort() shl 8
fun UByte.toBigEndianUInt() = toUInt() shl 24

fun UByte.asHex(padLength: Int = UByte.SIZE_BITS / 4) =
    toString(16).padStart(padLength, '0')

fun Byte.asHex(padLength: Int = Byte.SIZE_BITS / 4) =
    toString(16).padStart(padLength, '0')

fun UShort.asHex(padLength: Int = UShort.SIZE_BITS / 4) =
    toString(16).padStart(padLength, '0')

fun Short.asHex(padLength: Int = Short.SIZE_BITS / 4) =
    toString(16).padStart(padLength, '0')

fun UInt.asHex(padLength: Int = UInt.SIZE_BITS / 4) =
    toString(16).padStart(padLength, '0')

fun Int.asHex(padLength: Int = Int.SIZE_BITS / 4) =
    toString(16).padStart(padLength, '0')

fun ULong.asHex(padLength: Int = ULong.SIZE_BITS / 4) =
    toString(16).padStart(padLength, '0')

fun Long.asHex(padLength: Int = Long.SIZE_BITS) =
    toString(16).padStart(padLength, '0')

fun UByte.asBin(padLength: Int = UByte.SIZE_BITS) =
    toString(2).padStart(padLength, '0')

fun Byte.asBin(padLength: Int = Byte.SIZE_BITS) =
    toString(2).padStart(padLength, '0')

fun UShort.asBin(padLength: Int = UShort.SIZE_BITS) =
    toString(2).padStart(padLength, '0')

fun Short.asBin(padLength: Int = Short.SIZE_BITS) =
    toString(2).padStart(padLength, '0')

fun UInt.asBin(padLength: Int = UInt.SIZE_BITS) =
    toString(2).padStart(padLength, '0')

fun Int.asBin(padLength: Int = Int.SIZE_BITS) =
    toString(2).padStart(padLength, '0')

fun ULong.asBin(padLength: Int = ULong.SIZE_BITS) =
    toString(2).padStart(padLength, '0')

fun Long.asBin(padLength: Int = Long.SIZE_BITS) =
    toString(2).padStart(padLength, '0')

/** This function returns [IntRange] that has values in increasing order */
internal fun IntRange.toAscending() = min(start, endInclusive)..max(start, endInclusive)
