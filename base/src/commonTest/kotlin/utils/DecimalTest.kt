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
    fun testCalculation() {
        assertEquals(0.1.toDecimal().plus(0.2.toDecimal()).toDouble(), 0.3)
        assertEquals(1.99.toDecimal().round(1).toDouble(), 2.0)
        assertEquals(1.91.toDecimal().round(1).toDouble(), 1.9)

        assertEquals(1.5.toDecimal().round(0).toDouble(), 2.0)
        assertEquals(1.5.toDecimal().round(0, RoundingMode.RoundDown).toDouble(), 1.0)
        assertEquals(1.5.toDecimal().round(0, RoundingMode.RoundUp).toDouble(), 2.0)

        assertEquals((0.5.toDecimal() + 0.5.toDecimal()).toDouble(), 1.0)
        assertEquals((0.56.toDecimal() + 0.5.toDecimal()).round(1).toDouble(), 1.1)
        assertEquals((1.5.toDecimal() - 0.5.toDecimal()).toDouble(), 1.0)
        assertEquals((1.56.toDecimal() - 0.5.toDecimal()).round(1).toDouble(), 1.1)
        assertEquals((5.toDecimal() / 10.toDecimal()).toDouble(), 0.5)
        assertEquals((0.5.toDecimal() * 2.toDecimal()).toDouble(), 1.0)
        assertEquals(((1.toDecimal() / 3.toDecimal()).times(2.toDecimal(), 3, RoundingMode.RoundHalfEven)).toDouble(), 0.667)
        assertEquals(1.toDecimal().div(3.toDecimal(), 3).toDouble(), 0.333)
        assertEquals(1.toDecimal().div(3.toDecimal(), 3, RoundingMode.RoundDown).toDouble(), 0.333)
        assertEquals(1.toDecimal().div(3.toDecimal(), 3, RoundingMode.RoundUp).toDouble(), 0.334)
    }

    @Test
    fun testComparison() {
        assertTrue(0.123456.toDecimal() < 1.23456.toDecimal())
        assertFalse(0.123456.toDecimal() >= 1.23456.toDecimal())
        assertTrue(1.23456.toDecimal() > 0.123456.toDecimal())
        assertFalse(1.23456.toDecimal() <= 0.123456.toDecimal())
        assertEquals(1.23456.toDecimal(), 1.23456.toDecimal())
    }
}
