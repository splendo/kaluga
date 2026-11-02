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

import com.splendo.kaluga.bytes.stuffing.ByteStuffingException
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
import com.splendo.kaluga.bytes.layout.InvalidChecksumException
import com.splendo.kaluga.bytes.layout.InvalidPostfix
import com.splendo.kaluga.bytes.layout.InvalidPrefix
import com.splendo.kaluga.bytes.serialization.BytesFormat
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * What generated views do beyond reading and writing a frame: keeping checksums up to date, validating, viewing part
 * of an array and converting stuffed frames from and to the wire. The bytes they produce are compared with
 * `BytesFormat`'s.
 */
class ViewBehaviourTest {

    private val data = NestedChecksumFixturesOuterData(NestedChecksumFixturesInnerData(3), 7)

    private fun expected(value: NestedChecksumFixturesOuterData) = BytesFormat.encodeToByteArray(NestedChecksumFixturesOuterData.serializer(), value)

    @Test
    fun updatesChecksumsAfterEverySetter() {
        val view = NestedChecksumFixturesOuterView.encode(data)
        assertContentEquals(expected(data), view.bytes)

        view.last = 9
        assertContentEquals(expected(data.copy(last = 9)), view.bytes)

        // A nested view updates its own checksum and then the outer one.
        (view.inner as NestedChecksumFixturesInnerSlice).count = 4
        assertContentEquals(expected(NestedChecksumFixturesOuterData(NestedChecksumFixturesInnerData(4), 9)), view.bytes)
        assertTrue(view.isChecksumValid())
    }

    @Test
    fun updatesChecksumsOnceAfterAnEdit() {
        val view = NestedChecksumFixturesOuterView.encode(data)
        view.edit {
            last = 11
            (inner as NestedChecksumFixturesInnerSlice).count = 5
        }
        assertContentEquals(expected(NestedChecksumFixturesOuterData(NestedChecksumFixturesInnerData(5), 11)), view.bytes)
    }

    @Test
    fun validatesCorruptedBytesLikeBytesFormat() {
        val bytes = expected(data)
        val badChecksum = bytes.copyOf().also { it[it.size - 4] = (it[it.size - 4] + 1).toByte() }
        assertFalse(NestedChecksumFixturesOuterView(badChecksum).isChecksumValid())
        assertFailsWith<InvalidChecksumException> { NestedChecksumFixturesOuterView(badChecksum).validate() }
        assertFailsWith<InvalidChecksumException> { NestedChecksumFixturesOuterView.wrap(badChecksum) }
        NestedChecksumFixturesOuterView.wrap(badChecksum, validateChecksum = false).validate(validateChecksum = false)

        val badInnerChecksum = bytes.copyOf().also { it[1] = (it[1] + 1).toByte() }
        assertFalse(NestedChecksumFixturesOuterView(badInnerChecksum).isChecksumValid())

        val badPostfix = bytes.copyOf().also { it[it.size - 1] = 0 }
        assertFailsWith<InvalidPostfix> { NestedChecksumFixturesOuterView.wrap(badPostfix) }
        val badPrefix = bytes.copyOf().also { it[0] = 0 }
        assertFailsWith<InvalidPrefix> { NestedChecksumFixturesOuterView.wrap(badPrefix) }
        for (corrupted in listOf(badChecksum, badPostfix, badPrefix)) {
            val decoded = runCatching { BytesFormat.decodeFromByteArray(NestedChecksumFixturesOuterData.serializer(), corrupted) }.exceptionOrNull()
            val wrapped = runCatching { NestedChecksumFixturesOuterView.wrap(corrupted) }.exceptionOrNull()
            assertEquals(decoded?.let { it::class }, wrapped?.let { it::class })
        }
    }

    @Test
    fun viewsARangeOfALargerArray() {
        val frame = expected(data)
        val bytes = byteArrayOf(0x55, 0x66) + frame + byteArrayOf(0x77)
        val view = NestedChecksumFixturesOuterView.wrapRange(bytes, 2, 2 + frame.size) as NestedChecksumFixturesOuterSlice
        assertEquals(data, view.toData())

        view.last = 12
        assertContentEquals(expected(data.copy(last = 12)), bytes.copyOfRange(2, 2 + frame.size))
        assertContentEquals(byteArrayOf(0x55, 0x66), bytes.copyOfRange(0, 2))
        assertEquals(0x77, bytes.last())

        assertFailsWith<IllegalArgumentException> { NestedChecksumFixturesOuterView.wrapRange(bytes, -1, frame.size) }
        assertFailsWith<IllegalArgumentException> { NestedChecksumFixturesOuterView.wrapRange(bytes, 2, bytes.size + 1) }
        assertFailsWith<IllegalArgumentException> { NestedChecksumFixturesOuterView.wrapRange(bytes, 3, 2) }
    }

    @Test
    fun convertsStuffedFramesFromAndToTheWire() {
        val frame = CSafeChecksummedFrameData(command = 0xF1, value = 0x05)
        val wire = BytesFormat.encodeToByteArray(CSafeChecksummedFrameData.serializer(), frame)
        assertContentEquals(wire, CSafeChecksummedFrameView.encodeToWire(frame))

        val view = CSafeChecksummedFrameView.fromWire(wire)
        assertEquals(frame, view.toData())
        assertContentEquals(wire, view.toWire())
        view.value = 0xF2
        assertContentEquals(BytesFormat.encodeToByteArray(CSafeChecksummedFrameData.serializer(), frame.copy(value = 0xF2)), view.toWire())

        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { CSafeChecksummedFrameView.fromWire(byteArrayOf(0xF1.toByte())) }
        assertFailsWith<ByteStuffingException> { CSafeChecksummedFrameView.fromWire(byteArrayOf(0xF1.toByte(), 0xF3.toByte(), 0xF2.toByte())) }
    }
}
