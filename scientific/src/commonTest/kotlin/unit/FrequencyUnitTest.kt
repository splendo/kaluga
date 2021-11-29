/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.converter.electricConductance.div
import com.splendo.kaluga.scientific.converter.electricResistance.div
import com.splendo.kaluga.scientific.converter.decimal.div
import com.splendo.kaluga.scientific.converter.time.frequency
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class FrequencyUnitTest {

    @Test
    fun frequencyConversionTest() {
        assertScientificConversion(1, Hertz, 1e+9, Nanohertz)
        assertScientificConversion(1, Hertz, 1e+6, Microhertz)
        assertScientificConversion(1, Hertz, 1000.0, Millihertz)
        assertScientificConversion(1, Hertz, 100.0, Centihertz)
        assertScientificConversion(1, Hertz, 10.0, Decihertz)
        assertScientificConversion(1, Hertz, 0.1, Decahertz)
        assertScientificConversion(1, Hertz, 0.01, Hectohertz)
        assertScientificConversion(1, Hertz, 0.001, Kilohertz)
        assertScientificConversion(1, Hertz, 1e-6, Megahertz)
        assertScientificConversion(1, Hertz, 1e-9, Gigahertz)
        assertScientificConversion(1, Hertz, 60.0, BeatsPerMinute)
    }

    @Test
    fun frequencyFromConductanceAndCapacityTest() {
        assertEquals(1(Hertz), 2(Siemens) / 2(Farad))
    }

    @Test
    fun frequencyFromResistanceAndInductanceTest() {
        assertEquals(1(Hertz), 2(Ohm) / 2(Henry))
    }

    @Test
    fun frequencyFromInvertedTimeTest() {
        assertEquals(1(Hertz), 2 / 2(Second))
        assertEquals(1(Hertz), 2.toDecimal() / 2(Second))
        assertEquals(1(BeatsPerMinute), 2 / 2(Minute))
        assertEquals(1(BeatsPerMinute), 2.toDecimal() / 2(Minute))
        assertEquals(0.5(Hertz), 2(Second).frequency())
        assertEquals(0.5(BeatsPerMinute), 2(Minute).frequency())
    }
}