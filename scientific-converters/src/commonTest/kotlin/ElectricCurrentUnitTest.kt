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
import com.splendo.kaluga.scientific.converter.electricConductance.times
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.magneticFlux.div
import com.splendo.kaluga.scientific.converter.power.div
import com.splendo.kaluga.scientific.converter.voltage.div
import com.splendo.kaluga.scientific.converter.voltage.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Abampere
import com.splendo.kaluga.scientific.unit.Abcoulomb
import com.splendo.kaluga.scientific.unit.Abhenry
import com.splendo.kaluga.scientific.unit.Abohm
import com.splendo.kaluga.scientific.unit.Absiemens
import com.splendo.kaluga.scientific.unit.Abvolt
import com.splendo.kaluga.scientific.unit.Ampere
import com.splendo.kaluga.scientific.unit.Coulomb
import com.splendo.kaluga.scientific.unit.Decierg
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.Henry
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Maxwell
import com.splendo.kaluga.scientific.unit.Ohm
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Siemens
import com.splendo.kaluga.scientific.unit.Volt
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.Weber
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test
import kotlin.test.assertEquals

class ElectricCurrentUnitTest {

    @Test
    fun currentFromChargeAndTimeTest() {
        assertEquals(1(Abampere), 2(Abcoulomb) / 2(Second))
        assertEquals(1(Ampere), 2(Coulomb) / 2(Second))
    }

    @Test
    fun currentFromEnergyAndFluxTest() {
        assertEquals(1(Abampere), 2(Erg) / 2(Maxwell))
        assertEquals(1(Abampere), 20(Decierg) / 2(Maxwell))
        assertEquals(1(Ampere), 2(Joule) / 2(Weber))
    }

    @Test
    fun currentFromFluxAndInductanceTest() {
        assertEquals(1(Abampere), 2(Maxwell) / 2(Abhenry))
        assertEquals(1(Ampere), 2(Weber) / 2(Henry))
    }

    @Test
    fun currentFromPowerAndVoltageTest() {
        assertEquals(1(Abampere), 2(Erg per Second) / 2(Abvolt))
        assertEquals(1(Ampere), 2(Watt) / 2(Volt))
    }

    @Test
    fun currentFromVoltageAndConductanceTest() {
        assertEquals(4(Ampere), 2(Siemens) * 2(Volt))
        assertEquals(4(Ampere), 2(Volt) * 2(Siemens))
        assertEquals(4(Abampere), 2(Absiemens) * 2(Abvolt))
        assertEquals(4(Abampere), 2(Abvolt) * 2(Absiemens))
    }

    @Test
    fun currentFromVoltageAndResistanceTest() {
        assertEquals(1(Abampere), 2(Abvolt) / 2(Abohm))
        assertEquals(1(Ampere), 2(Volt) / 2(Ohm))
    }
}
