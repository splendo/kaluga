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

package com.splendo.kaluga.base.utils

object ByteUtils {
    const val HEX_RADIX = 16
    const val BYTE_STRING_LENGTH = 2
}

/**
 * Returns [ByteArray] for given bytes
 */
fun bytesOf(vararg bytes: Int) = bytes.map(Int::toByte).toByteArray()

/**
 * Converts a [ByteArray] to a String representing the bytes as their hexadecimal value
 * @param separator The separator to use between elements
 * @return The String representing the [ByteArray]s hexadecimal value
 */
fun ByteArray.toHexString(separator: CharSequence = ""): String = asUByteArray().joinToString(separator) {
    it.toString(ByteUtils.HEX_RADIX).padStart(ByteUtils.BYTE_STRING_LENGTH, '0')
}

/**
 * Converts a hex [String] to [ByteArray]
 * @return The [ByteArray] or `null` if length is not even
 */
fun String.decodeHex(): ByteArray? {
    if (length.rem(ByteUtils.BYTE_STRING_LENGTH) != 0) return null

    return chunked(ByteUtils.BYTE_STRING_LENGTH) {
        it.toString().toInt(ByteUtils.HEX_RADIX).toByte()
    }.toByteArray()
}
