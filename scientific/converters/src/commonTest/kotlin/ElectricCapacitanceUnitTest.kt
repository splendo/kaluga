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

package com.splendo.kaluga.scientific.converter

import com.splendo.kaluga.scientific.converter.electricCharge.div
import com.splendo.kaluga.scientific.converter.electricConductance.div
import com.splendo.kaluga.scientific.converter.time.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Abcoulomb
import com.splendo.kaluga.scientific.unit.Abfarad
import com.splendo.kaluga.scientific.unit.Abohm
import com.splendo.kaluga.scientific.unit.Absiemens
import com.splendo.kaluga.scientific.unit.Abvolt
import com.splendo.kaluga.scientific.unit.Coulomb
import com.splendo.kaluga.scientific.unit.Farad
import com.splendo.kaluga.scientific.unit.Hertz
import com.splendo.kaluga.scientific.unit.Ohm
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Siemens
import com.splendo.kaluga.scientific.unit.Volt
import kotlin.test.Test
import kotlin.test.assertEquals

class ElectricCapacitanceUnitTest {

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
