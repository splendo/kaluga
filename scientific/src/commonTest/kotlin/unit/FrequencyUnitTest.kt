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

import com.splendo.kaluga.scientific.assertEqualScientificValue
import com.splendo.kaluga.scientific.converter.electricConductance.div
import com.splendo.kaluga.scientific.converter.electricResistance.div
import com.splendo.kaluga.scientific.converter.time.frequency
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class FrequencyUnitTest {

    @Test
    fun frequencyConversionTest() {
        assertEquals(1e+9, Hertz.convert(1, Nanohertz))
        assertEquals(1e+6, Hertz.convert(1, Microhertz))
        assertEquals(1000.0, Hertz.convert(1, Millihertz))
        assertEquals(100.0, Hertz.convert(1, Centihertz))
        assertEquals(10.0, Hertz.convert(1, Decihertz))
        assertEquals(0.1, Hertz.convert(1, Decahertz))
        assertEquals(0.01, Hertz.convert(1, Hectohertz))
        assertEquals(0.001, Hertz.convert(1, Kilohertz))
        assertEquals(1e-6, Hertz.convert(1, Megahertz))
        assertEquals(1e-9, Hertz.convert(1, Gigahertz))
        assertEquals(60.0, Hertz.convert(1, BeatsPerMinute))
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
        assertEquals(1(BeatsPerMinute), 1(Minute).frequency())
        assertEquals(1(Hertz), 1(Second).frequency())
    }
}