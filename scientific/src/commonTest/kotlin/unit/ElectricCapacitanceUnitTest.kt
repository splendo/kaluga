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

import com.splendo.kaluga.scientific.converter.electricCharge.div
import com.splendo.kaluga.scientific.converter.electricConductance.div
import com.splendo.kaluga.scientific.converter.time.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class ElectricCapacitanceUnitTest {

    @Test
    fun electricCapacitanceConversionTest() {
        assertScientificConversion(1, Farad, 1000000000.0, Nanofarad)
        assertScientificConversion(1, Farad, 1000000.0, Microfarad)
        assertScientificConversion(1, Farad, 1000.0, Millifarad)
        assertScientificConversion(1, Farad, 100.0, Centifarad)
        assertScientificConversion(1, Farad, 10.0, Decifarad)
        assertScientificConversion(1, Farad, 0.1, Decafarad)
        assertScientificConversion(1, Farad, 0.01, Hectofarad)
        assertScientificConversion(1, Farad, 0.001, Kilofarad)
        assertScientificConversion(1, Farad, 0.000001, Megafarad)
        assertScientificConversion(1, Farad, 1e-9, Gigafarad)
    }

    @Test
    fun capacitanceFromChargeAndVoltageTest() {
        assertEquals(1(Abfarad), 2(Abcoulomb) / 2(Abvolt))
        assertEquals(1(Farad), 2(Coulomb) / 2(Volt))
        assertEquals(1(Abfarad), 2(Abcoulomb) / 2(Abvolt))
    }

    @Test
    fun capacitanceFromConductanceAndFrequencyTest() {
        assertEquals(1(Abfarad), 2(Absiemens) / 2(Hertz))
        assertEquals(1(Farad), 2(Siemens) / 2(Hertz))
        assertEquals(1(Abfarad), 2(Absiemens) / 2(Hertz))
    }

    @Test
    fun capacitanceFromTimeAndResistanceTest() {
        assertEquals(1(Abfarad), 2(Second) / 2(Abohm))
        assertEquals(1(Farad), 2(Second) / 2(Ohm))
        assertEquals(1(Abfarad), 2(Second) / 2(Abohm))
    }
}
