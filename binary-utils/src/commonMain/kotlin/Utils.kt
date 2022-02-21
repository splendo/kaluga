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

import com.splendo.kaluga.base.utils.bytesOf

/**
 * Returns numbers of bits for [Byte]
 * to be shifted (left or right) at given [index]
 * based on this [Endianness] inside group of [bitsCount] bits
 *
 * @see Endianness
 * */
internal fun Endianness.shift(index: Int, bitsCount: Int) =
    octetIndex(index, bitsCount) * Byte.SIZE_BITS

/**
 * Returns octet index for given [index] based on this [Endianness] inside group of [bitsCount] Bits
 *
 * @see Endianness
 */
internal fun Endianness.octetIndex(index: Int, bitsCount: Int) = when (this) {
    Endianness.MOST_SIGNIFICANT_FIRST -> index
    Endianness.LEAST_SIGNIFICANT_FIRST -> bitsCount / Byte.SIZE_BITS - index - 1
}

/** Converts this [String] into [ByteArray] */
fun String.toByteArray() = bytesOf(*map(Char::code).toIntArray())

/** Converts this [Byte] into [ByteArray] */
fun Byte.toByteArray() = byteArrayOf(this)

/** Converts this [Short] into [ByteArray] using given [numberingScheme] */
fun Short.toByteArray(numberingScheme: Endianness) = ByteArray(Short.SIZE_BYTES) {
    (this shr numberingScheme.shift(it, Short.SIZE_BITS)).toByte()
}

/** Converts this [Int] into [ByteArray] using given [numberingScheme] */
fun Int.toByteArray(numberingScheme: Endianness) = ByteArray(Int.SIZE_BYTES) {
    (this shr numberingScheme.shift(it, Int.SIZE_BITS)).toByte()
}

/** Converts this [Long] into [ByteArray] using given [numberingScheme] */
fun Long.toByteArray(numberingScheme: Endianness) = ByteArray(Long.SIZE_BYTES) {
    (this shr numberingScheme.shift(it, Long.SIZE_BITS)).toByte()
}
