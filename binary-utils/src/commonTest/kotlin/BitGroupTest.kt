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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BitGroupTest {

    @Test
    fun testGroup8BitsRead() {

        val bits = GroupOf8Bits(0b1010_0011u)

        assertEquals(8, bits.numberOfBits)
        assertEquals(1, bits.numberOfOctets)
        assertEquals(UByte.MAX_VALUE, bits.getFullMask())
        assertEquals("a3", bits.asHex)
        assertEquals("10100011", bits.asBin)

        assertTrue(bits[0])
        assertTrue(bits[1])
        assertFalse(bits[2])
        assertFalse(bits[3])
        assertFalse(bits[4])
        assertTrue(bits[5])
        assertFalse(bits[6])
        assertTrue(bits[7])

        assertEquals(0b11u, bits[0..2])
        assertEquals(0b011u, bits[0..3])
        assertEquals(0b0011u, bits[0..4])
        assertEquals(0b1010u, bits[4..7])
        assertEquals(0b10_1000u, bits[2..7])
        assertEquals(0b101_0001u, bits[1..7])
        assertEquals(0b1010_0011u, bits[0..7])
    }

    @Test
    fun testGroup8RegionMask() {
        val bits = GroupOf8Bits(0u)
        assertEquals(0b1111_1110u, bits.regionMask(0..0))
        assertEquals(0b1111_1100u, bits.regionMask(0..1))
        assertEquals(0b1110_0111u, bits.regionMask(3..4))
        assertEquals(0b0111_1111u, bits.regionMask(7..7))
        assertEquals(0b1111_1001u, bits.regionMask(1..2))
        assertEquals(0b0000_0000u, bits.regionMask(0..7))
    }

    @Test
    fun testGroup8ShiftInput() {
        val bits = GroupOf8Bits(0u)
        assertEquals(0b0000_0001u, bits.shiftInput(0b11u, 0..0))
        assertEquals(0b0000_0011u, bits.shiftInput(0b11u, 0..1))
        assertEquals(0b0000_0110u, bits.shiftInput(0b11u, 1..2))
        assertEquals(0b0000_1100u, bits.shiftInput(0b11u, 2..3))
        assertEquals(0b0101_0000u, bits.shiftInput(0b0101u, 4..7))
        assertEquals(0b0101_1010u, bits.shiftInput(0b0101_1010u, 0..7))
    }

    @Test
    fun testGroup16RegionMask() {
        val bits = GroupOf16Bits(0u, Endianness.LEAST_SIGNIFICANT_FIRST)
        assertEquals(0b1111_1111_1111_1110u, bits.regionMask(0..0))
        assertEquals(0b1111_1111_1111_1100u, bits.regionMask(0..1))
        assertEquals(0b1111_1111_0111_1111u, bits.regionMask(7..7))
        assertEquals(0b1111_1110_0111_1111u, bits.regionMask(7..8))
        assertEquals(0b1111_1111_1111_1001u, bits.regionMask(1..2))
        assertEquals(0b0011_1111_1111_1111u, bits.regionMask(14..15))
        assertEquals(0b0000_0000_0000_0000u, bits.regionMask(0..15))
    }

    @Test
    fun testGroup16ShiftInput() {
        val bits = GroupOf16Bits(0u, Endianness.LEAST_SIGNIFICANT_FIRST)
        assertEquals(0b0000_0001u, bits.shiftInput(0b11u, 0..0))
        assertEquals(0b0000_0011u, bits.shiftInput(0b11u, 0..1))
        assertEquals(0b0000_0110u, bits.shiftInput(0b11u, 1..2))
        assertEquals(0b0000_1100u, bits.shiftInput(0b11u, 2..3))
        assertEquals(0b0001_1000u, bits.shiftInput(0b11u, 3..4))
        assertEquals(0b0101_0000u, bits.shiftInput(0b0101u, 4..7))
        assertEquals(
            0b0000_0011_1100_0000u,
            bits.shiftInput(0b1111u, 6..9)
        )
        assertEquals(
            0b0000_1111_0000_0000u,
            bits.shiftInput(0b1111u, 8..11)
        )
        assertEquals(
            0b0000_1100_0000_0000u,
            bits.shiftInput(0b1111u, 10..11)
        )
        assertEquals(
            0b1111_0000_1111_0000u,
            bits.shiftInput(0b1111_0000_1111_0000u, 0..15)
        )
    }

    @Test
    fun testGroup8Write() {
        val bits = GroupOf8Bits(0u)
        for (index in 0..7) {
            assertFalse(bits[index])
            bits[index] = true
            assertTrue(bits[index])
        }
    }

    @Test
    fun testGroup16Write() {
        val bits = GroupOf16Bits(0u, Endianness.LEAST_SIGNIFICANT_FIRST)
        for (index in 0..15) {
            assertFalse(bits[index])
            bits[index] = true
            assertTrue(bits[index])
        }
    }

    @Test
    fun testGroup8Flip() {
        val bits = GroupOf8Bits(0u)
        for (index in 0..7) {
            assertFalse(bits[index])
            bits.flip(index)
            assertTrue(bits[index])
        }
    }

    @Test
    fun testGroup16Flip() {
        val bits = GroupOf16Bits(0u, Endianness.LEAST_SIGNIFICANT_FIRST)
        for (index in 0..15) {
            assertFalse(bits[index])
            bits.flip(index)
            assertTrue(bits[index])
        }
    }

    @Test
    fun testGroup16Bits() {

        val bits = GroupOf16Bits(
            0b1010_0011_1010_0011u,
            Endianness.LEAST_SIGNIFICANT_FIRST
        )

        assertEquals(16, bits.numberOfBits)
        assertEquals(2, bits.numberOfOctets)
        assertEquals(UShort.MAX_VALUE, bits.getFullMask())
        assertEquals("a3a3", bits.asHex)
        assertEquals("1010001110100011", bits.asBin)

        assertTrue(bits[0])
        assertTrue(bits[1])
        assertFalse(bits[2])
        assertFalse(bits[3])
        assertFalse(bits[4])
        assertTrue(bits[5])
        assertFalse(bits[6])
        assertTrue(bits[7])
        assertTrue(bits[8])
        assertTrue(bits[9])
        assertFalse(bits[10])
        assertFalse(bits[11])
        assertFalse(bits[12])
        assertTrue(bits[13])
        assertFalse(bits[14])
        assertTrue(bits[15])

        assertEquals(0b11u, bits[0..2])
        assertEquals(0b011u, bits[0..3])
        assertEquals(0b0011u, bits[0..4])
        assertEquals(0b1010u, bits[4..7])
        assertEquals(0b10_1000u, bits[2..7])
        assertEquals(0b101_0001u, bits[1..7])
        assertEquals(0b1010_0011u, bits[0..7])

        assertEquals(0b11u, bits[8..9])
        assertEquals(0b011u, bits[8..10])
        assertEquals(0b0011u, bits[8..11])
        assertEquals(0b1010u, bits[4..7])
        assertEquals(0b10_1000u, bits[2..7])
        assertEquals(0b101_0001_1101_0001u, bits[1..15])
        assertEquals(0b1010_0011_1010_0011u, bits[0..15])
    }

    @Test
    fun testGroup24Bits() {
        val bits = GroupOf24Bits(0x00f0f0u, Endianness.LEAST_SIGNIFICANT_FIRST)
        assertEquals(24, bits.numberOfBits)
        assertEquals(3, bits.numberOfOctets)
        assertEquals(0xFFFFFFu, bits.getFullMask())
        assertEquals("00f0f0", bits.asHex)
        assertEquals("000000001111000011110000", bits.asBin)

        assertEquals(0b00u, bits[0..1])
        assertEquals(0b0000u, bits[0..3])
        assertEquals(0b1111_0000u, bits[0..7])
    }

    @Test
    fun testGroup32Bits() {
        val bits = GroupOf32Bits(
            0xf0f0f0f0u,
            Endianness.LEAST_SIGNIFICANT_FIRST
        )
        assertEquals(32, bits.numberOfBits)
        assertEquals(4, bits.numberOfOctets)
        assertEquals(UInt.MAX_VALUE, bits.getFullMask())
        assertEquals("f0f0f0f0", bits.asHex)
        assertEquals("11110000111100001111000011110000", bits.asBin)

        assertEquals(0b00u, bits[0..1])
        assertEquals(0b0000u, bits[0..3])
        assertEquals(0b1111_0000u, bits[0..7])
    }

    @Test
    fun testGroup64Bits() {
        val bits = GroupOf64Bits(
            0xf0f0f0f0f0f0f0f0u,
            Endianness.LEAST_SIGNIFICANT_FIRST
        )
        assertEquals(64, bits.numberOfBits)
        assertEquals(8, bits.numberOfOctets)
        assertEquals(ULong.MAX_VALUE, bits.getFullMask())
        assertEquals("f0f0f0f0f0f0f0f0", bits.asHex)
        assertEquals(
            "1111000011110000111100001111000011110000111100001111000011110000",
            bits.asBin
        )

        assertEquals(0b00u, bits[0..1])
        assertEquals(0b0000u, bits[0..3])
        assertEquals(0b1111_0000u, bits[0..7])
    }
}
