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

import com.splendo.kaluga.bytes.annotations.Checksum
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests of [BytesFormat]'s own entry points, such as its builder, which a views backend does not have.
 */
class BytesFormatEntryPointTest {

    private object Fixtures {
        @Serializable
        @Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
        data class Checksummed(val value: Byte)
    }

    @Test
    fun derivedFormatKeepsChecksumValidation() {
        val bytes = BytesFormat.encodeToByteArray(Fixtures.Checksummed.serializer(), Fixtures.Checksummed(1))
        bytes[bytes.lastIndex] = (bytes.last() + 1).toByte()
        val lenient = BytesFormat { validateChecksum = false }
        assertEquals(Fixtures.Checksummed(1), lenient.decodeFromByteArray(Fixtures.Checksummed.serializer(), bytes))
        assertEquals(Fixtures.Checksummed(1), BytesFormat(from = lenient) {}.decodeFromByteArray(Fixtures.Checksummed.serializer(), bytes))
    }
}
