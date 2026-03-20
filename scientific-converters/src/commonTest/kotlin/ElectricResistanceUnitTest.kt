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

import com.splendo.kaluga.scientific.converter.electricConductance.resistance
import com.splendo.kaluga.scientific.converter.electricInductance.div
import com.splendo.kaluga.scientific.converter.electricInductance.times
import com.splendo.kaluga.scientific.converter.frequency.times
import com.splendo.kaluga.scientific.converter.magneticFlux.div
import com.splendo.kaluga.scientific.converter.time.div
import com.splendo.kaluga.scientific.converter.voltage.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Abampere
import com.splendo.kaluga.scientific.unit.Abcoulomb
import com.splendo.kaluga.scientific.unit.Abfarad
import com.splendo.kaluga.scientific.unit.Abhenry
import com.splendo.kaluga.scientific.unit.Abohm
import com.splendo.kaluga.scientific.unit.Absiemens
import com.splendo.kaluga.scientific.unit.Abvolt
import com.splendo.kaluga.scientific.unit.Ampere
import com.splendo.kaluga.scientific.unit.Biot
import com.splendo.kaluga.scientific.unit.Coulomb
import com.splendo.kaluga.scientific.unit.Farad
import com.splendo.kaluga.scientific.unit.Henry
import com.splendo.kaluga.scientific.unit.Hertz
import com.splendo.kaluga.scientific.unit.Maxwell
import com.splendo.kaluga.scientific.unit.Ohm
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Siemens
import com.splendo.kaluga.scientific.unit.Volt
import com.splendo.kaluga.scientific.unit.Weber
import kotlin.test.Test
import kotlin.test.assertEquals
import com.splendo.kaluga.test.base.IgnoreJs

@IgnoreJs
class ElectricResistanceUnitTest {

    @Test
    fun electricResistanceFromInvertedConductanceTest() {
        assertEquals(2(Abohm), 0.5(Absiemens).resistance())
        assertEquals(2(Ohm), 0.5(Siemens).resistance())
    }

    @Test
    fun resistanceFromInductanceAndFrequencyTest() {
        assertEquals(4(Abohm), 2(Abhenry) * 2(Hertz))
        assertEquals(4(Abohm), 2(Hertz) * 2(Abhenry))
        assertEquals(4(Ohm), 2(Henry) * 2(Hertz))
        assertEquals(4(Ohm), 2(Hertz) * 2(Henry))
    }

    @Test
    fun resistanceFromInductanceAndTimeTest() {
        assertEquals(1(Abohm), 2(Abhenry) / 2(Second))
        assertEquals(1(Ohm), 2(Henry) / 2(Second))
        assertEquals(1(Abohm), 2(Abhenry) / 2(Second))
    }

    @Test
    fun resistanceFromFluxAndChargeTest() {
        assertEquals(1(Abohm), 2(Maxwell) / 2(Abcoulomb))
        assertEquals(1(Ohm), 2(Weber) / 2(Coulomb))
    }

    @Test
    fun resistanceFromTimeAndCapacitanceTest() {
        assertEquals(1(Abohm), 2(Second) / 2(Abfarad))
        assertEquals(1(Ohm), 2(Second) / 2(Farad))
    }

    @Test
    fun resistanceFromVoltageAndCurrentTest() {
        assertEquals(1(Abohm), 2(Abvolt) / 2(Abampere))
        assertEquals(1(Abohm), 2(Abvolt) / 2(Biot))
        assertEquals(1(Ohm), 2(Volt) / 2(Ampere))
    }
}
