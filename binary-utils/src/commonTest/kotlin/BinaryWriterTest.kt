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
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BinaryWriterTest {

    @Test
    fun testFixedBufferWriter() {
        val writer = BinaryWriter(
            ByteArrayWriter.Fixed(bufferSize = 32),
            Endianness.MOST_SIGNIFICANT_FIRST
        )
        assertContentEquals(byteArrayOf(), writer.toByteArray())
        writer.writeByte(0xfa.toByte())
        assertContentEquals(bytesOf(0xfa), writer.toByteArray())
        writer.writeByte(0x11.toByte())
        assertContentEquals(bytesOf(0xfa, 0x11), writer.toByteArray())
        writer.writeInt(10)
        assertContentEquals(bytesOf(0xfa, 0x11, 0x0a, 0, 0, 0), writer.toByteArray())
        writer.writeByte(0x22.toByte())
        writer.writeInt(8)
        assertContentEquals(
            bytesOf(0xfa, 0x11, 0x0a, 0, 0, 0, 0x22, 8, 0, 0, 0),
            writer.toByteArray()
        )
    }

    @Test
    fun testDynamicBufferWriter() {
        val writer = BinaryWriter(
            ByteArrayWriter.Dynamic(),
            Endianness.MOST_SIGNIFICANT_FIRST
        )
        assertContentEquals(byteArrayOf(), writer.toByteArray())
        for (index in 0 until 64) {
            writer.writeByte(index.toByte())
        }
        assertEquals(64, writer.toByteArray().size)
    }

    @Test
    fun testWriteDifferentTypes() {
        val writer = BinaryWriter(
            ByteArrayWriter.Fixed(bufferSize = 32),
            Endianness.MOST_SIGNIFICANT_FIRST
        )
        assertTrue(writer.toByteArray().isEmpty())
        writer.writeByte(42)
        assertContentEquals(bytesOf(42), writer.toByteArray())
        writer.writeShort(0x1020)
        assertContentEquals(
            bytesOf(42, 0x20, 0x10),
            writer.toByteArray()
        )
        writer.writeInt(0x30405060)
        assertContentEquals(
            bytesOf(42, 0x20, 0x10, 0x60, 0x50, 0x40, 0x30),
            writer.toByteArray()
        )
    }
}
