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

package com.splendo.kaluga.base.utils

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DecimalTest {

    @Test
    fun testConversion() {
        assertEquals(1, 1.toDecimal().toInt())
        assertEquals(0.1, 0.1.toDecimal().toDouble())
        assertEquals(0.1, "0.1".toDecimal().toDouble())
        assertEquals(1E-6, "1E-6".toDecimal().toDouble(), absoluteTolerance = 0.000000001)
        assertEquals("0.1", "0.1".toDecimal().toString())
    }

    @Test
    fun testCalculation() {
        assertEquals("0.3".toDecimal(), "0.1".toDecimal().plus("0.2".toDecimal()))
        assertEquals("2.0".toDecimal(), "1.99".toDecimal().round(1))
        assertEquals("1.9".toDecimal(), "1.91".toDecimal().round(1))

        assertEquals("2.0".toDecimal(), "1.5".toDecimal().round(0))
        assertEquals("1.0".toDecimal(), "1.5".toDecimal().round(0, RoundingMode.RoundDown))
        assertEquals("2.0".toDecimal(), "1.5".toDecimal().round(0, RoundingMode.RoundUp))

        assertEquals("1.0".toDecimal(), ("0.5".toDecimal() + "0.5".toDecimal()))
        assertEquals("1.1".toDecimal(), ("0.56".toDecimal() + "0.5".toDecimal()).round(1))
        assertEquals("1.0".toDecimal(), ("1.5".toDecimal() - "0.5".toDecimal()))
        assertEquals("1.1".toDecimal(), ("1.56".toDecimal() - "0.5".toDecimal()).round(1))
        assertEquals("0.5".toDecimal(), (5.toDecimal() / 10.toDecimal()))
        assertEquals("1.0".toDecimal(), ("0.5".toDecimal() * 2.toDecimal()))
        assertEquals("0.667".toDecimal(), ((1.toDecimal() / 3.toDecimal()).times(2.toDecimal(), 3, RoundingMode.RoundHalfEven)))
        assertEquals("0.333".toDecimal(), 1.toDecimal().div(3.toDecimal(), 3))
        assertEquals("0.333".toDecimal(), 1.toDecimal().div(3.toDecimal(), 3, RoundingMode.RoundDown))
        assertEquals("0.334".toDecimal(), 1.toDecimal().div(3.toDecimal(), 3, RoundingMode.RoundUp))

        assertEquals(Decimal.PositiveInfinity, 1.0.toDecimal() / 0.0.toDecimal())
        assertEquals(Decimal.PositiveInfinity, 1.0.toDecimal().div(0.0.toDecimal(), 3))
        assertEquals(Decimal.PositiveInfinity, 1.0.toDecimal().div(0.0.toDecimal(), 3, RoundingMode.RoundDown))

        assertEquals(Decimal.NegativeInfinity, (-1.0).toDecimal() / 0.0.toDecimal())
        assertEquals(Decimal.NegativeInfinity, (-1.0).toDecimal().div(0.0.toDecimal(), 3))
        assertEquals(Decimal.NegativeInfinity, (-1.0).toDecimal().div(0.0.toDecimal(), 3, RoundingMode.RoundDown))

        assertEquals(Decimal.NaN, 0.0.toDecimal() / 0.0.toDecimal())
        assertEquals(Decimal.NaN, 0.0.toDecimal().div(0.0.toDecimal(), 3))
        assertEquals(Decimal.NaN, 0.0.toDecimal().div(0.0.toDecimal(), 3, RoundingMode.RoundDown))

        assertEquals(Decimal.NaN, "Test".toDecimal())
    }

    @Test
    fun testComparison() {
        assertTrue("0.123456".toDecimal() < "1.23456".toDecimal())
        assertFalse("0.123456".toDecimal() >= "1.23456".toDecimal())
        assertTrue("1.23456".toDecimal() > "0.123456".toDecimal())
        assertFalse("1.23456".toDecimal() <= "0.123456".toDecimal())
        assertEquals("1.23456".toDecimal(), "1.23456".toDecimal())
    }
}
