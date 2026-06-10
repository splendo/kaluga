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

package com.splendo.kaluga.bluetooth.serialization

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LengthTest {

    @Test
    fun testUnsigned32BitFitsFullUIntRange() {
        // Values above Int.MAX_VALUE still fit in an unsigned 32-bit value.
        // Regression: the upper bound used to be Int.MAX_VALUE instead of UInt.MAX_VALUE.
        assertTrue(Length.`32_BIT`.fits(Int.MAX_VALUE.toLong() + 1L, signed = false))
        assertTrue(Length.`32_BIT`.fits(3_000_000_000L, signed = false))
        assertTrue(Length.`32_BIT`.fits(UInt.MAX_VALUE.toLong(), signed = false))
    }

    @Test
    fun testUnsigned32BitDoesNotFitBeyondUIntMax() {
        assertFalse(Length.`32_BIT`.fits(UInt.MAX_VALUE.toLong() + 1L, signed = false))
    }

    @Test
    fun testSigned32BitBoundsUnchanged() {
        assertTrue(Length.`32_BIT`.fits(Int.MAX_VALUE.toLong(), signed = true))
        assertTrue(Length.`32_BIT`.fits(Int.MIN_VALUE.toLong(), signed = true))
        assertFalse(Length.`32_BIT`.fits(Int.MAX_VALUE.toLong() + 1L, signed = true))
        assertFalse(Length.`32_BIT`.fits(Int.MIN_VALUE.toLong() - 1L, signed = true))
    }
}
