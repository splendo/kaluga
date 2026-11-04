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
import com.splendo.kaluga.bytes.view.DeltaCodedIntList
import com.splendo.kaluga.bytes.view.DeltaCodedShortList
import kotlinx.serialization.KSerializer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

/**
 * The delta-coded list tests every backend runs. Arrays and the fallback's `ByteArray` make DATA classes compare by
 * reference, so values holding one are checked by the bytes they encode back to.
 */
abstract class DeltaCodedSuite(backendFactory: ByteLayoutBackendFactory) : BytesFormatTestBase(backendFactory) {

    private val packet = DeltaCodedFixturesPacketData.serializer()

    private val raw = DeltaCodedFixturesPacketRawData(listOf(16, 18, 15))
    private val rawBytes = bytes(0x01, 0x06, 0x10, 0x00, 0x12, 0x00, 0x0F, 0x00)
    private val delta = DeltaCodedFixturesPacketDeltaData(listOf(16, 18, 15))
    private val deltaBytes = bytes(0x02, 0x04, 0x10, 0x00, 0x02, 0xFD)
    private val single = DeltaCodedFixturesPacketDeltaData(listOf(5000))
    private val singleBytes = bytes(0x02, 0x02, 0x88, 0x13)
    private val empty = DeltaCodedFixturesPacketDeltaData(emptyList())
    private val emptyBytes = bytes(0x02, 0x00)
    private val battery = DeltaCodedFixturesPacketBatteryData(100u)
    private val batteryBytes = bytes(0x03, 0x01, 0x64)
    private val other = DeltaCodedFixturesPacketOtherData(0x99u, byteArrayOf(1, 2))
    private val otherBytes = bytes(0x99, 0x02, 0x01, 0x02)

    private val stream = DeltaCodedFixturesStreamData(7u, listOf(delta, raw, single, battery, empty, other, delta))
    protected val streamBytes = bytes(0x07, 0x00) + deltaBytes + rawBytes + singleBytes + batteryBytes + emptyBytes + otherBytes + deltaBytes

    private fun <T> validateBytes(value: T, serializer: KSerializer<T>, expected: ByteArray): T {
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
    fun encodesTheFirstItemThenTheStepToEachNext() {
        val waveform = DeltaCodedFixturesWaveformData.serializer()
        validateEncoding(DeltaCodedFixturesWaveformData(listOf(16, 18, 15)), waveform, bytes(0x10, 0x00, 0x02, 0xFD))
        validateEncoding(DeltaCodedFixturesWaveformData(listOf(5000, 5001, 4999)), waveform, bytes(0x88, 0x13, 0x01, 0xFE))
        validateEncoding(DeltaCodedFixturesWaveformData(listOf(-2)), waveform, bytes(0xFE, 0xFF))
        val decoded = validateBytes(DeltaCodedFixturesChannelData(3u, shortArrayOf(16, 18, 15)), DeltaCodedFixturesChannelData.serializer(), bytes(0x03, 0x00, 0x10, 0x02, 0xFD))
        assertContentEquals(shortArrayOf(16, 18, 15), decoded.samples)
        val wide = validateBytes(DeltaCodedFixturesWideData(intArrayOf(0, 1000, -1000)), DeltaCodedFixturesWideData.serializer(), bytes(0, 0, 0, 0, 0xE8, 0x03, 0x30, 0xF8))
        assertContentEquals(intArrayOf(0, 1000, -1000), wide.values)
    }

    @Test
    fun wrapsStepsAroundTheSizeOfAnItem() {
        validateEncoding(
            DeltaCodedFixturesWaveformData(listOf(Short.MAX_VALUE, Short.MIN_VALUE, Short.MAX_VALUE)),
            DeltaCodedFixturesWaveformData.serializer(),
            bytes(0xFF, 0x7F, 0x01, 0xFF),
        )
        validateEncoding(DeltaCodedFixturesCounterData(listOf(65534, 65535, 0, 1)), DeltaCodedFixturesCounterData.serializer(), bytes(0xFE, 0xFF, 0x01, 0x01, 0x01))
    }

    @Test
    fun runsToTheEndOfEachRecordOfAStream() {
        assertEquals(delta, validateBytes(delta, packet, deltaBytes))
        assertEquals(single, validateBytes(single, packet, singleBytes))
        assertEquals(empty, validateBytes(empty, packet, emptyBytes))
        assertEquals(raw, validateBytes(raw, packet, rawBytes))
        val decoded = validateBytes(stream, DeltaCodedFixturesStreamData.serializer(), streamBytes)
        assertEquals(stream.packets.take(5), decoded.packets.take(5))
        assertIs<DeltaCodedFixturesPacketOtherData>(decoded.packets[5])
        assertEquals(delta, decoded.packets[6])
    }

    @Test
    fun rejectsBytesThatAreNotAFirstItemAndWholeSteps() {
        val backend = createBackend()
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { backend.decode(DeltaCodedFixturesWaveformData.serializer(), bytes(0x10)) }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { backend.decode(DeltaCodedFixturesWideData.serializer(), bytes(0, 0, 0, 0, 0x01)) }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> {
            backend.decode(DeltaCodedFixturesStreamData.serializer(), bytes(0x07, 0x00, 0x02, 0x01, 0x10) + batteryBytes)
        }
    }

    @Test
    fun throwsForAStepOrAnItemThatDoesNotFit() {
        val backend = createBackend()
        assertFailsWith<IllegalArgumentException> { backend.encode(DeltaCodedFixturesWaveformData.serializer(), DeltaCodedFixturesWaveformData(listOf(0, 128))) }
        assertFailsWith<IllegalArgumentException> { backend.encode(packet, DeltaCodedFixturesPacketDeltaData(listOf(100, -100))) }
        assertFailsWith<IllegalArgumentException> { backend.encode(DeltaCodedFixturesCounterData.serializer(), DeltaCodedFixturesCounterData(listOf(-1))) }
        assertFailsWith<IllegalArgumentException> { backend.encode(DeltaCodedFixturesCounterData.serializer(), DeltaCodedFixturesCounterData(listOf(0, 65536))) }
    }

    protected fun bytes(vararg values: Int): ByteArray = ByteArray(values.size) { values[it].toByte() }
}

class BytesFormatDeltaCodedTest : DeltaCodedSuite(BytesFormatBackend.Factory)

class ViewDeltaCodedTest : DeltaCodedSuite(ViewBackend.Factory) {

    @Test
    fun readsTheItemsInPlaceInOrder() {
        val view = DeltaCodedFixturesWaveformView.wrap(bytes(0x88, 0x13, 0x01, 0xFE))
        val samples: DeltaCodedShortList = view.samples
        assertEquals(3, samples.size)
        assertEquals(4999.toShort(), samples[2])
        var sum = 0
        samples.forEachShort { sum += it }
        assertEquals(15000, sum)
        assertContentEquals(shortArrayOf(0, 5000, 5001, 4999), samples.copyInto(ShortArray(4), 1))
        val counts: DeltaCodedIntList = DeltaCodedFixturesCounterView.wrap(bytes(0xFF, 0xFF, 0x01)).counts
        assertContentEquals(intArrayOf(65535, 0), counts.toIntArray())
    }

    @Test
    fun readsTheItemsOfEveryRecordOfAStream() {
        val view = DeltaCodedFixturesStreamView.wrap(streamBytes)
        assertEquals(7, view.packets.size)
        assertEquals(listOf<Short>(16, 18, 15), (view.packets[0] as DeltaCodedFixtures.Packet.Delta).samples)
        assertEquals(listOf<Short>(5000), (view.packets[2] as DeltaCodedFixtures.Packet.Delta).samples)
        assertEquals(emptyList(), (view.packets[4] as DeltaCodedFixtures.Packet.Delta).samples)
        assertEquals(listOf<Short>(16, 18, 15), (view.packets[6] as DeltaCodedFixtures.Packet.Delta).samples)
    }
}
