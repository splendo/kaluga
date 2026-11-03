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

package com.splendo.kaluga.bytes.example.format

import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
import com.splendo.kaluga.bytes.layout.UnexpectedItemCount
import com.splendo.kaluga.bytes.toHexString
import kotlinx.serialization.KSerializer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * The `@Count` tests every backend runs. DATA classes compare arrays by reference, so a decoded value is checked by
 * the bytes it encodes back to and by its arrays' content.
 */
abstract class CountSuite(backendFactory: ByteLayoutBackendFactory) : BytesFormatTestBase(backendFactory) {

    private val coefficients = ByteArray(21) { (it * 7).toByte() }
    private val record = FixedCountFixturesRecordData(true, listOf(0x0102u, 0xFEFFu), coefficients, 0xABu, listOf(-1, 2, 0x0304), byteArrayOf(9, 8, 7, 6))
    private val recordBytes = byteArrayOf(0x01, 0x02, 0x01, 0xFF.toByte(), 0xFE.toByte()) + coefficients +
        byteArrayOf(0xAB.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x02, 0x00, 0x04, 0x03, 9, 8, 7, 6)

    private fun <T> validateCount(value: T, serializer: KSerializer<T>, expected: ByteArray): T {
        val backend = createBackend()
        val bytes = backend.encode(serializer, value)
        assertContentEquals(expected, bytes, "Expected ${expected.toHexString(separator = " ")} but got ${bytes.toHexString(separator = " ")}")
        val decoded = backend.decode(serializer, bytes)
        assertContentEquals(expected, backend.encode(serializer, decoded))

        val nested = Nested.serializer(serializer)
        val nestedBytes = backend.encode(nested, Nested(value))
        assertContentEquals(byteArrayOf(0x42, 0x23) + expected + 0x22, nestedBytes)
        assertContentEquals(nestedBytes, backend.encode(nested, backend.decode(nested, nestedBytes)))
        return decoded
    }

    @Test
    fun encodesFixedCountsBeforeBetweenAndAfterOtherFields() {
        val decoded = validateCount(record, FixedCountFixturesRecordData.serializer(), recordBytes)
        assertContentEquals(coefficients, decoded.coefficients)
        assertEquals(record.sequence, decoded.sequence)
        assertEquals(record.header, decoded.header)
        assertEquals(record.trims, decoded.trims)
        assertContentEquals(record.tail, decoded.tail)
    }

    @Test
    fun rejectsAnotherNumberOfItems() {
        assertFailsWith<UnexpectedItemCount> { createBackend().encode(FixedCountFixturesRecordData.serializer(), record.copy(coefficients = ByteArray(20))) }
        assertFailsWith<UnexpectedItemCount> { createBackend().encode(FixedCountFixturesRecordData.serializer(), record.copy(coefficients = ByteArray(22))) }
        assertFailsWith<UnexpectedItemCount> { createBackend().encode(FixedCountFixturesRecordData.serializer(), record.copy(trims = listOf(1))) }
    }

    @Test
    fun decodingTooFewBytesThrows() {
        // From the flag header on: BytesFormat does not bound its read of a header that is missing altogether.
        for (size in 1 until recordBytes.size) {
            assertFailsWith<ByteArrayEndedBeforeSerializationCompleted>("$size bytes") {
                createBackend().decode(FixedCountFixturesRecordData.serializer(), recordBytes.copyOf(size))
            }
        }
    }

    @Test
    fun tellsCalibrationBlocksApartBySize() {
        val serializer = FixedCountFixturesCalibrationData.serializer()
        val a = ByteArray(88) { it.toByte() }
        val decodedA = validateCount(FixedCountFixturesCalibrationAData(a), serializer, a)
        assertContentEquals(a, (decodedA as FixedCountFixturesCalibrationAData).block)

        val b = ByteArray(36) { (it + 100).toByte() }
        val decodedB = validateCount(FixedCountFixturesCalibrationBData(b), serializer, b)
        assertContentEquals(b, (decodedB as FixedCountFixturesCalibrationBData).block)

        val c = FixedCountFixturesCalibrationCData(List(14) { (it * 0x0101).toUShort() })
        val cBytes = ByteArray(28) { (it / 2).toByte() }
        assertEquals(c, validateCount(c, serializer, cBytes))

        val d = ByteArray(51) { (it * 5).toByte() }
        val decodedD = validateCount(FixedCountFixturesCalibrationDData(0x80u, d), serializer, byteArrayOf(0x80.toByte()) + d)
        assertEquals(0x80u.toUByte(), (decodedD as FixedCountFixturesCalibrationDData).index)
        assertContentEquals(d, decodedD.block)
    }

    @Test
    fun encodesFixedCountsInListItems() {
        val batch = FixedCountFixturesBatchData(
            listOf(FixedCountFixturesSampleData(byteArrayOf(1, 2, 3), 4), FixedCountFixturesSampleData(byteArrayOf(5, 6, 7), 8)),
            9,
        )
        val decoded = validateCount(batch, FixedCountFixturesBatchData.serializer(), byteArrayOf(2, 1, 2, 3, 4, 5, 6, 7, 8, 9))
        assertEquals(listOf(listOf<Byte>(1, 2, 3), listOf<Byte>(5, 6, 7)), decoded.samples.map { it.raw.toList() })
    }

    @Test
    fun aNullableFixedCountKeepsItsPresenceFlag() {
        validateEncoding(FixedCountFixturesOptionalData(null, 3), FixedCountFixturesOptionalData.serializer(), byteArrayOf(0x00, 0x03))
        validateEncoding(FixedCountFixturesOptionalData(listOf(1, 2), 3), FixedCountFixturesOptionalData.serializer(), byteArrayOf(0x01, 0x01, 0x02, 0x03))
        assertFailsWith<UnexpectedItemCount> { createBackend().encode(FixedCountFixturesOptionalData.serializer(), FixedCountFixturesOptionalData(emptyList(), 3)) }
    }
}

class BytesFormatCountTest : CountSuite(BytesFormatBackend.Factory)

class ViewCountTest : CountSuite(ViewBackend.Factory)
