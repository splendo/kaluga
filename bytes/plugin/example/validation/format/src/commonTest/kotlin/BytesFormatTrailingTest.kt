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
import com.splendo.kaluga.bytes.layout.TrailingAfterAbsent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

/**
 * The `@Trailing` tests every backend runs: values of every length a protocol that appends fields sends, as a root
 * value, nested as the last property of another and in type-length-value records.
 */
abstract class TrailingSuite(backendFactory: ByteLayoutBackendFactory) : BytesFormatTestBase(backendFactory) {

    private val status = TrailingFixturesStatusData(charging = true, level = 300, mode = null, voltage = 0x0102u, serviceVersion = 0x0304u, uptime = 5)
    private val statusBytes = bytes(0x03, 0x2C, 0x01, 0x02, 0x01, 0x04, 0x03, 0x05, 0x00)

    private val full = TrailingFixturesInfoDeviceData(0x0102u, 0x1122334455667788u, 0x0A0Bu)
    private val fullBytes = bytes(0x01, 0x0C, 0x02, 0x01, 0x88, 0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x0B, 0x0A)

    @Test
    fun readsAValueWithoutTheFieldsItsBytesDoNotReach() {
        validateEncoding(status, TrailingFixturesStatusData.serializer(), statusBytes)
        validateEncoding(status.copy(uptime = null), TrailingFixturesStatusData.serializer(), statusBytes.copyOf(7))
        validateEncoding(status.copy(serviceVersion = null, uptime = null), TrailingFixturesStatusData.serializer(), statusBytes.copyOf(5))
        validateEncoding(
            status.copy(mode = 7, uptime = 70000),
            TrailingFixturesStatusData.serializer(),
            bytes(0x17, 0x2C, 0x01, 0x07, 0x02, 0x01, 0x04, 0x03, 0x70, 0x11, 0x01, 0x00),
        )
        // The size bits of the absent number are set, as a writer may leave them, and do not bring it back.
        assertEquals(status.copy(uptime = null), createBackend().decode(TrailingFixturesStatusData.serializer(), bytes(0x13) + statusBytes.copyOfRange(1, 7)))
    }

    @Test
    fun rejectsAValueEndingInsideATrailingFieldOrBeforeTheTail() {
        for (size in listOf(4, 6, 8)) {
            assertFailsWith<ByteArrayEndedBeforeSerializationCompleted>("$size bytes") { createBackend().decode(TrailingFixturesStatusData.serializer(), statusBytes.copyOf(size)) }
        }
    }

    @Test
    fun rejectsATrailingFieldAfterANullOne() {
        val error = assertFailsWith<TrailingAfterAbsent> { createBackend().encode(TrailingFixturesStatusData.serializer(), status.copy(serviceVersion = null)) }
        assertEquals("uptime", error.name)
        assertEquals("serviceVersion", error.absent)
    }

    @Test
    fun boundsTheTailOfARecordByItsLength() {
        val info = TrailingFixturesInfoData.serializer()
        validateEncoding(full, info, fullBytes)
        validateEncoding(full.copy(word = null), info, bytes(0x01, 0x0A) + fullBytes.copyOfRange(2, 12))
        validateEncoding(full.copy(serial = null, word = null), info, bytes(0x01, 0x02, 0x02, 0x01))
        validateEncoding(TrailingFixturesInfoOtherData(5, 6, 0x77u), info, bytes(0x77, 0x02, 0x05, 0x06))
        validateEncoding(TrailingFixturesInfoOtherData(5, null, 0x77u), info, bytes(0x77, 0x01, 0x05))
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { createBackend().decode(info, bytes(0x01, 0x05, 0x02, 0x01, 0x88, 0x77, 0x66)) }
    }

    @Test
    fun readsRecordsOfOlderAndNewerVersionsBackToBack() {
        val infos = TrailingFixturesInfosData(3, listOf(TrailingFixturesInfoDeviceData(7u, null, null), TrailingFixturesInfoBatteryData(50u), full))
        validateEncoding(infos, TrailingFixturesInfosData.serializer(), bytes(0x03, 0x01, 0x02, 0x07, 0x00, 0x02, 0x01, 0x32) + fullBytes)
    }

    @Test
    fun readsLayoutsCollectionsAndStringsInTheTail() {
        val limits = TrailingFixturesLimitsData.serializer()
        validateEncoding(
            TrailingFixturesLimitsData(1, TrailingFixturesSpanData(-1, 1), listOf(2, 3), "ab"),
            limits,
            bytes(0x01, 0xFF, 0xFF, 0x01, 0x00, 0x02, 0x00, 0x03, 0x00, 0x02, 'a'.code, 'b'.code),
        )
        validateEncoding(TrailingFixturesLimitsData(1, TrailingFixturesSpanData(-1, 1), null, null), limits, bytes(0x01, 0xFF, 0xFF, 0x01, 0x00))
        validateEncoding(TrailingFixturesLimitsData(1, null, null, null), limits, bytes(0x01))
        assertNull(createBackend().decode(limits, bytes(0x01)).span)
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { createBackend().decode(limits, bytes(0x01, 0xFF, 0xFF, 0x01)) }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { createBackend().decode(limits, bytes(0x01, 0xFF, 0xFF, 0x01, 0x00, 0x02, 0x00)) }
    }

    @Test
    fun endsTheTailBeforeTheChecksumAndThePostfix() {
        val framed = TrailingFixturesFramedData.serializer()
        assertEquals(5, validateRoundTrip(TrailingFixturesFramedData(1, 0x0203), framed).size)
        assertEquals(3, validateRoundTrip(TrailingFixturesFramedData(1, null), framed).size)
    }

    protected fun bytes(vararg values: Int): ByteArray = ByteArray(values.size) { values[it].toByte() }
}

class BytesFormatTrailingTest : TrailingSuite(BytesFormatBackend.Factory)

class ViewTrailingTest : TrailingSuite(ViewBackend.Factory) {

    @Test
    fun readsAShorterValueThroughAnUncheckedView() {
        // A view created without checking, as a Bluetooth client creates one, reads nothing past the end of the value.
        val tailless = TrailingFixturesStatusView(bytes(0x03, 0x2C, 0x01, 0x02, 0x01))
        assertEquals(300, tailless.level)
        assertEquals(0x0102u, tailless.voltage)
        assertNull(tailless.serviceVersion)
        assertNull(tailless.uptime)
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { TrailingFixturesStatusView(bytes(0x03, 0x2C, 0x01, 0x02, 0x01, 0x04)).serviceVersion }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { TrailingFixturesLimitsView(bytes(0x01, 0xFF, 0xFF, 0x01)).span?.high }
    }

    @Test
    fun readsTheTailOfRecordsInPlace() {
        // The last record ends inside its serial: its length bounds the read, not the end of the array.
        val bytes = bytes(0x03, 0x01, 0x02, 0x07, 0x00, 0x02, 0x01, 0x32, 0x01, 0x04, 0x02, 0x01, 0x88, 0x77)
        val infos = TrailingFixturesInfosView(bytes + bytes(0x11, 0x22, 0x33, 0x44, 0x55, 0x66))
        assertNull((infos.infos[0] as TrailingFixtures.Info.Device).serial)
        assertEquals(50u, (infos.infos[1] as TrailingFixtures.Info.Battery).percent)
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { (infos.infos[2] as TrailingFixtures.Info.Device).serial }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { TrailingFixturesInfosView.wrap(bytes) }
    }
}
