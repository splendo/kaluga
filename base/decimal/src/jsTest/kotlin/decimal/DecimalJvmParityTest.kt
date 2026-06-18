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

package com.splendo.kaluga.base.decimal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Behaviors that `java.math.BigDecimal` (JVM) and the JS port mirror exactly, but the
 * iOS `NSDecimalNumber`-backed implementation cannot match (different toString rules,
 * limited precision/exponent range, no exposure of the exact IEEE 754 expansion).
 */
class DecimalJvmParityTest {

    @Test
    fun testStringFormatting() {
        // Plain notation: integers and simple fractions, with trailing zeros stripped.
        assertEquals("1", "1".toDecimal().toString())
        assertEquals("0.1", "0.1".toDecimal().toString())
        assertEquals("1.5", "1.50".toDecimal().toString())

        // Adjusted exponent at the -6 boundary stays in plain notation.
        assertEquals("0.000001", "0.000001".toDecimal().toString())

        // Adjusted exponent below -6 switches to scientific notation.
        assertEquals("1E-7", "0.0000001".toDecimal().toString())

        // Integers with trailing zeros strip to a negative scale → scientific notation.
        assertEquals("1E+1", 10.toDecimal().toString())
        assertEquals("1E+3", 1000.toDecimal().toString())
        assertEquals("-1.23E+5", "-123000".toDecimal().toString())
    }

    @Test
    fun testDoubleConstructorExactness() {
        // Mirrors `java.math.BigDecimal(double)`: captures the exact binary value, not
        // the shorter decimal display. 0.1 is not representable in IEEE 754.
        assertEquals(
            "0.1000000000000000055511151231257827021181583404541015625",
            0.1.toDecimal().toString(),
        )
    }

    @Test
    fun testCompareToWidelyDifferentScales() {
        // Magnitude short-circuit avoids materializing a million-digit pow10.
        assertTrue("1E-1000000".toDecimal() < 1.toDecimal())
        assertTrue("1E+1000000".toDecimal() > 1.toDecimal())
    }
}
