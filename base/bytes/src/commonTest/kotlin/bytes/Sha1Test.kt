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

import kotlin.test.Test
import kotlin.test.assertEquals

class Sha1Test {

    @Test
    fun emptyInput() {
        assertSha1("", "da39a3ee5e6b4b0d3255bfef95601890afd80709")
    }

    @Test
    fun knownAnswerVectors() {
        assertSha1("abc", "a9993e364706816aba3e25717850c26c9cd0d89d")
        assertSha1(
            "The quick brown fox jumps over the lazy dog",
            "2fd4e1c67a2d28fced849ee1bb76e7391b93eb12",
        )
        // A single-bit difference in the input must produce a completely different digest.
        assertSha1(
            "The quick brown fox jumps over the lazy cog",
            "de9f2c7fd25e1b3afad3e85a0bd17d9b100db4b3",
        )
    }

    @Test
    fun paddingBoundaries() {
        // 55 bytes: length + padding + 8-byte length just fits into a single 64-byte block.
        assertSha1("a".repeat(55), "c1c8bbdc22796e28c0e15163d20899b65621d65a")
        // 56 bytes: no room for the length field in the first block, forcing a second padding block.
        assertSha1(
            "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq",
            "84983e441c3bd26ebaae4aa1f95129e5e54670f1",
        )
        // 63 bytes: one byte short of a full block, still a two-block message after padding.
        assertSha1("a".repeat(63), "03f09f5b158a7a8cdad920bddc29b81c18a551f5")
        // 64 bytes: exactly one block of data, padding spills into a whole extra block.
        assertSha1("a".repeat(64), "0098ba824b5c16427bd7a1122a5a442a25ec644d")
    }

    @Test
    fun multiBlockInput() {
        assertSha1(
            "abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmnoijklmnopjklmnopqklmnopqrlmnopqrsmnopqrstnopqrstu",
            "a49b2446a02c645bf419f995b67091253a04a259",
        )
    }

    @Test
    fun digestIsTwentyBytes() {
        assertEquals(20, "anything".encodeToByteArray().sha1().size)
    }

    private fun assertSha1(input: String, expectedHex: String) {
        assertEquals(expectedHex, input.encodeToByteArray().sha1().toHexString())
    }

    private fun ByteArray.toHexString() = joinToString("") { (it.toInt() and 0xFF).toString(16).padStart(2, '0') }
}
