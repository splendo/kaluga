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

package com.splendo.kaluga.base.bytes

/** Gets the SHA-1 (FIPS 180-4) of a [ByteArray]. Returns a 20-byte digest. */
fun ByteArray.sha1(): ByteArray {
    var h0 = 0x67452301
    var h1 = 0xEFCDAB89.toInt()
    var h2 = 0x98BADCFE.toInt()
    var h3 = 0x10325476
    var h4 = 0xC3D2E1F0.toInt()

    // Pad: append 0x80, zero bytes, then 64-bit big-endian bit-length
    val bitLen = size.toLong() * 8
    val padLen = if (size % 64 < 56) 56 - size % 64 else 120 - size % 64
    val msg = ByteArray(size + padLen + 8)
    copyInto(msg)
    msg[size] = 0x80.toByte()
    for (i in 0..7) msg[msg.size - 8 + i] = (bitLen ushr (56 - i * 8)).toByte()

    val w = IntArray(80)
    for (chunk in msg.indices step 64) {
        for (i in 0..15) {
            w[i] = ((msg[chunk + i * 4].toInt() and 0xFF) shl 24) or
                ((msg[chunk + i * 4 + 1].toInt() and 0xFF) shl 16) or
                ((msg[chunk + i * 4 + 2].toInt() and 0xFF) shl 8) or
                (msg[chunk + i * 4 + 3].toInt() and 0xFF)
        }
        for (i in 16..79) {
            val x = w[i - 3] xor w[i - 8] xor w[i - 14] xor w[i - 16]
            w[i] = (x shl 1) or (x ushr 31)
        }
        var a = h0
        var b = h1
        var c = h2
        var d = h3
        var e = h4
        for (i in 0..79) {
            val f: Int
            val k: Int
            when (i) {
                in 0..19 -> {
                    f = (b and c) or (b.inv() and d)
                    k = 0x5A827999
                }

                in 20..39 -> {
                    f = b xor c xor d
                    k = 0x6ED9EBA1
                }

                in 40..59 -> {
                    f = (b and c) or (b and d) or (c and d)
                    k = 0x8F1BBCDC.toInt()
                }

                else -> {
                    f = b xor c xor d
                    k = 0xCA62C1D6.toInt()
                }
            }
            val temp = ((a shl 5) or (a ushr 27)) + f + e + k + w[i]
            e = d
            d = c
            c = (b shl 30) or (b ushr 2)
            b = a
            a = temp
        }
        h0 += a
        h1 += b
        h2 += c
        h3 += d
        h4 += e
    }

    return ByteArray(20) { i ->
        val word = when (i / 4) {
            0 -> h0
            1 -> h1
            2 -> h2
            3 -> h3
            else -> h4
        }
        (word ushr (24 - (i % 4) * 8)).toByte()
    }
}