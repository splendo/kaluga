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

package com.splendo.kaluga.bytes.layout

import com.splendo.kaluga.bytes.annotations.Length
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NumericCodecTest {

    private val lengths = setOf(Length.`8_BIT`, Length.`16_BIT`, Length.`32_BIT`)

    @Test
    fun selectsSmallestFittingLengthOrTheLast() {
        assertEquals(IndexedValue(0, Length.`8_BIT`), NumericCodec.selectLength(lengths, 200, signed = false))
        assertEquals(IndexedValue(1, Length.`16_BIT`), NumericCodec.selectLength(lengths, 200, signed = true))
        assertEquals(IndexedValue(2, Length.`32_BIT`), NumericCodec.selectLength(lengths, 100_000, signed = true))
    }

    @Test
    fun flagPackedValuesRoundTripWithSignExtension() {
        assertTrue(NumericCodec.fitsInBits(-4, 3, signed = true))
        assertFalse(NumericCodec.fitsInBits(4, 3, signed = true))
        assertEquals(-4L, NumericCodec.signExtend(0b100, 3, signed = true))
        assertEquals(4L, NumericCodec.signExtend(0b100, 3, signed = false))
    }

    @Test
    fun scalarAndRangeRoundTrip() {
        val scalar = NumericSettings.Scalar(setOf(Length.`16_BIT`), signed = true, multiplier = 1, decimalExponent = 2, binaryExponent = 0, offset = 0)
        assertEquals(2150.0, NumericCodec.scalarToWire(scalar, 21.5))
        assertEquals(21.5, NumericCodec.scalarFromWire(scalar, 2150))

        val range = NumericSettings.RangeEncoded(setOf(Length.`8_BIT`), min = 0.0, max = 255.0)
        assertEquals(128.0, NumericCodec.rangeToWire(range, 128.0))
        assertEquals(128.0, NumericCodec.rangeFromWire(range, 128))
    }

    @Test
    fun widthSelectionForFloatingPointEncodings() {
        assertEquals(Length.`32_BIT`, NumericCodec.decimalLength(0.5))
        assertEquals(Length.`64_BIT`, NumericCodec.decimalLength(0.1))
    }

    @Test
    fun invalidNumericSettingsAreRejected() {
        assertFailsWith<InvalidNumericSettings> { NumericSettings.Decimal(setOf(Length.`16_BIT`)) }
    }
}
