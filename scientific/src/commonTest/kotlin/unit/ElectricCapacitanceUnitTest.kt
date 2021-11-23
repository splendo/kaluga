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
        assertEquals(1000000000.0, Farad.convert(1, Nanofarad))
        assertEquals(1000000.0, Farad.convert(1, Microfarad))
        assertEquals(1000.0, Farad.convert(1, Millifarad))
        assertEquals(100.0, Farad.convert(1, Centifarad))
        assertEquals(10.0, Farad.convert(1, Decifarad))
        assertEquals(0.1, Farad.convert(1, Decafarad))
        assertEquals(0.01, Farad.convert(1, Hectofarad))
        assertEquals(0.001, Farad.convert(1, Kilofarad))
        assertEquals(0.000001, Farad.convert(1, Megafarad))
        assertEquals(1e-9, Farad.convert(1, Gigafarad))
    }

    @Test
    fun capacitanceFromChargeAndVoltageTest() {
        assertEquals(1(Farad), 2(Coulomb) / 2(Volt))
    }

    @Test
    fun capacitanceFromConductanceAndFrequencyTest() {
        assertEquals(1(Farad), 2(Siemens) / 2(Hertz))
    }

    @Test
    fun capacitanceFromTimeAndResistanceTest() {
        assertEquals(1(Farad), 2(Second) / 2(Ohm))
    }
}