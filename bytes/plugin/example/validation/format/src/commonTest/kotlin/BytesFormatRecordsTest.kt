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
import com.splendo.kaluga.bytes.toHexString
import kotlinx.serialization.KSerializer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

/**
 * The type-length-value tests every backend runs. A fallback holds a `ByteArray`, which DATA classes compare by
 * reference, so a decoded value is checked by the bytes it encodes back to.
 */
abstract class RecordsSuite(backendFactory: ByteLayoutBackendFactory) : BytesFormatTestBase(backendFactory) {

    private val reading = RecordFixturesReadingData.serializer()

    private val temperature = RecordFixturesReadingTemperatureData(1000, -5)
    private val temperatureBytes = bytes(0x20, 0x05, 0x01, 0xE8, 0x03, 0xFB, 0xFF)
    private val run = RecordFixturesReadingRunData(2, listOf(0x0102u, 0xFFFFu))
    private val runBytes = bytes(0x21, 0x06, 0x00, 0x02, 0x02, 0x01, 0xFF, 0xFF)
    private val note = RecordFixturesReadingNoteData(-70000, "hi")
    private val noteBytes = bytes(0x30, 0x07, 0x02, 0x90, 0xEE, 0xFE, 0xFF, 'h'.code, 'i'.code)
    private val other = RecordFixturesReadingOtherData(0x99u, byteArrayOf(1, 2, 3))
    private val otherBytes = bytes(0x99, 0x03, 0x01, 0x02, 0x03)
    private val markBytes = bytes(0x40, 0x00)

    private val chunk = RecordFixturesChunkData(1u, true, 256u, listOf(temperature, run, note, RecordFixturesReadingMarkData, other))
    private val chunkBytes = bytes(0x11, 0x00, 0x01, 0x00, 0x00) + temperatureBytes + runBytes + noteBytes + markBytes + otherBytes

    private fun <T> validateRecord(value: T, serializer: KSerializer<T>, expected: ByteArray): T {
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
    fun encodesEachRecordAsItsTypeItsLengthAndItsBytes() {
        assertEquals(temperature, validateRecord(temperature, reading, temperatureBytes))
        assertEquals(run, validateRecord(run, reading, runBytes))
        assertEquals(note, validateRecord(note, reading, noteBytes))
        assertEquals(RecordFixturesReadingMarkData, validateRecord(RecordFixturesReadingMarkData, reading, markBytes))
    }

    @Test
    fun keepsARecordOfATypeNoSubtypeHasInTheFallback() {
        val decoded = assertIs<RecordFixturesReadingOtherData>(validateRecord(other, reading, otherBytes))
        assertEquals(0x99u, decoded.type)
        assertContentEquals(byteArrayOf(1, 2, 3), decoded.value)
        validateRecord(RecordFixturesReadingOtherData(0x98u, ByteArray(0)), reading, bytes(0x98, 0x00))
    }

    @Test
    fun readsRecordsBackToBackToTheEndOfTheirMessage() {
        val decoded = validateRecord(chunk, RecordFixturesChunkData.serializer(), chunkBytes)
        assertEquals(chunk.readings.take(4), decoded.readings.take(4))
    }

    @Test
    fun letsFieldsFollowARecord() {
        validateRecord(RecordFixturesFollowedData(note, null, 7), RecordFixturesFollowedData.serializer(), bytes(0x00) + noteBytes + bytes(0x07))
        validateRecord(RecordFixturesFollowedData(other, run, -1), RecordFixturesFollowedData.serializer(), bytes(0x01) + otherBytes + runBytes + bytes(0xFF))
    }

    @Test
    fun skipsTheBytesARecordDoesNotRead() {
        // A later version appended two bytes to the temperature.
        val longer = bytes(0x00, 0x00, 0x00, 0x00, 0x00, 0x20, 0x06, 0x00, 0x02, 0x10, 0x00, 0xAA, 0xBB) + markBytes
        val decoded = createBackend().decode(RecordFixturesChunkData.serializer(), longer)
        assertEquals(listOf(RecordFixturesReadingTemperatureData(2, 16), RecordFixturesReadingMarkData), decoded.readings)
    }

    @Test
    fun rejectsARecordShorterThanItsTypeNeedsOrThanItsLength() {
        val header = bytes(0x00, 0x00, 0x00, 0x00, 0x00)
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { createBackend().decode(RecordFixturesChunkData.serializer(), header + bytes(0x20, 0x02, 0x00, 0x02)) }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { createBackend().decode(RecordFixturesChunkData.serializer(), header + bytes(0x40, 0x02, 0x00)) }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { createBackend().decode(RecordFixturesChunkData.serializer(), header + bytes(0x40)) }
    }

    @Test
    fun rejectsALengthThatDoesNotFitAndAFallbackWithAKnownType() {
        assertFailsWith<IllegalArgumentException> { createBackend().encode(reading, RecordFixturesReadingNoteData(0, "x".repeat(254))) }
        assertFailsWith<IllegalArgumentException> { createBackend().encode(reading, RecordFixturesReadingOtherData(0x20u, byteArrayOf(1))) }
    }

    @Test
    fun countsTheFrameOfASubtypeInItsLength() {
        val framed = RecordFixturesFramedData.serializer()
        val ended = RecordFixturesFramedEndedData(listOf(1, 2))
        val endedBytes = bytes(0x10, 0x03, 0x01, 0x02, 0x55)
        val summed = RecordFixturesFramedSummedData(7)
        val summedBytes = bytes(0x11, 0x02, 0x07, 0x15)
        assertEquals(ended, validateRecord(ended, framed, endedBytes))
        assertEquals(summed, validateRecord(summed, framed, summedBytes))
        val stream = RecordFixturesFramedStreamData(listOf(ended, RecordFixturesFramedEndedData(listOf(3)), summed, RecordFixturesFramedEndedData(emptyList())))
        val streamBytes = endedBytes + bytes(0x10, 0x02, 0x03, 0x55) + summedBytes + bytes(0x10, 0x01, 0x55)
        assertEquals(stream, validateRecord(stream, RecordFixturesFramedStreamData.serializer(), streamBytes))
        val followed = RecordFixturesFramedFollowedData(summed, 9)
        assertEquals(followed, validateRecord(followed, RecordFixturesFramedFollowedData.serializer(), summedBytes + bytes(0x09)))
        // The checksum of a subtype of a fixed size follows its body, before the bytes a later version appended.
        assertEquals(summed, createBackend().decode(framed, bytes(0x11, 0x03, 0x07, 0x15, 0xAA)))
    }

    @Test
    fun rejectsALengthThatCutsOffTheFrameOfASubtype() {
        val followed = RecordFixturesFramedFollowedData.serializer()
        // The length leaves the checksum out of the record, where the field after it is.
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { createBackend().decode(followed, bytes(0x11, 0x01, 0x07, 0x15)) }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { createBackend().decode(followed, bytes(0x10, 0x00, 0x55, 0x09)) }
    }

    @Test
    fun writesTwoByteTypesAndLengthsInTheirByteOrderInAFrame() {
        val message = RecordFixturesMessageData.serializer()
        for (value in listOf(RecordFixturesMessageValueData(0x0102), RecordFixturesMessageOtherData(0x0300u, listOf(4, 5)))) {
            val expected = BytesFormatBackend().encode(message, value)
            assertContentEquals(bytes(0x7E) + (if (value is RecordFixturesMessageValueData) bytes(0x01, 0x00) else bytes(0x03, 0x00)) + bytes(0x00, 0x02), expected.copyOf(5))
            validateRoundTrip(value, message, expected)
        }
    }

    @Test
    fun readsAnyOtherCommandIntoTheFallbackWithoutALength() {
        val command = RecordFixturesCommandData.serializer()
        validateEncoding(RecordFixturesCommandStartData(3), command, bytes(0x01, 0x03))
        validateEncoding(RecordFixturesCommandUnsupportedData(7u, listOf(1, 2)), command, bytes(0x07, 0x01, 0x02))
    }

    protected fun bytes(vararg values: Int): ByteArray = ByteArray(values.size) { values[it].toByte() }

    protected val chunkForViews: ByteArray get() = chunkBytes
}

class BytesFormatRecordsTest : RecordsSuite(BytesFormatBackend.Factory)

class ViewRecordsTest : RecordsSuite(ViewBackend.Factory) {

    @Test
    fun readsRecordsOfAListInPlace() {
        val view = RecordFixturesChunkView.wrap(chunkForViews)
        assertEquals(5, view.readings.size)
        assertEquals(-5, (view.readings[0] as RecordFixtures.Reading.Temperature).celsius.toInt())
        assertEquals(listOf<UShort>(0x0102u, 0xFFFFu), (view.readings[1] as RecordFixtures.Reading.Run).samples)
        assertEquals("hi", (view.readings[2] as RecordFixtures.Reading.Note).text)
        assertEquals(-70000, (view.readings[2] as RecordFixtures.Reading.Note).step)
        assertEquals(0x99u, (view.readings[4] as RecordFixtures.Reading.Other).type)
    }

    @Test
    fun refusesToSetTheTypeOfAFallbackToThatOfAnotherSubtype() {
        val view = RecordFixturesChunkView.wrap(chunkForViews.copyOf())
        val other = view.readings[4] as RecordFixturesReadingOtherSlice1
        assertFailsWith<IllegalArgumentException> { other.type = 0x20u }
        assertContentEquals(chunkForViews, view.bytes)
        other.type = 0x98u
        assertEquals(0x98u, (RecordFixturesChunkView.wrap(view.bytes).readings[4] as RecordFixtures.Reading.Other).type)

        val message = RecordFixturesMessageFrame.encode(RecordFixturesMessageOtherData(0x0300u, listOf(4, 5)))
        assertFailsWith<IllegalArgumentException> { (message.value as RecordFixturesMessageOtherSlice1).type = 0x0100u }
        (message.value as RecordFixturesMessageOtherSlice1).type = 0x0001u
        assertEquals(RecordFixturesMessageOtherData(0x0001u, listOf(4, 5)), RecordFixturesMessageFrame.wrap(message.bytes).toData())

        val command = RecordFixturesCommandFrame.encode(RecordFixturesCommandUnsupportedData(7u, listOf(1, 2)))
        assertFailsWith<IllegalArgumentException> { (command.value as RecordFixturesCommandUnsupportedSlice1).code = 0x01u }
        assertContentEquals(bytes(0x07, 0x01, 0x02), command.bytes)
    }

    @Test
    fun walksRecordsByTheirLength() {
        val bytes = chunkForViews
        val types = mutableListOf<Int>()
        val values = mutableListOf<Int>()
        var at = 5
        while (at < bytes.size) {
            types += bytes[at].toInt() and 0xFF
            values += RecordFixturesReadingFrame.valueEnd(bytes, at) - RecordFixturesReadingFrame.valueStart(bytes, at)
            at = RecordFixturesReadingFrame.end(bytes, at)
        }
        assertEquals(listOf(0x20, 0x21, 0x30, 0x40, 0x99), types)
        assertEquals(listOf(5, 6, 7, 0, 3), values)
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { RecordFixturesReadingFrame.end(bytes(0x20, 0x05, 0x00), 0) }
    }
}
