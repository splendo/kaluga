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
import com.splendo.kaluga.scientific.converter.electricCurrent.div
import com.splendo.kaluga.scientific.converter.electricCurrent.times
import com.splendo.kaluga.scientific.converter.electricResistance.times
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.magneticFlux.div
import com.splendo.kaluga.scientific.converter.power.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Abampere
import com.splendo.kaluga.scientific.unit.Abcoulomb
import com.splendo.kaluga.scientific.unit.Abfarad
import com.splendo.kaluga.scientific.unit.Abohm
import com.splendo.kaluga.scientific.unit.Absiemens
import com.splendo.kaluga.scientific.unit.Abvolt
import com.splendo.kaluga.scientific.unit.Ampere
import com.splendo.kaluga.scientific.unit.Biot
import com.splendo.kaluga.scientific.unit.Coulomb
import com.splendo.kaluga.scientific.unit.Decierg
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.Farad
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

class VoltageUnitTest {

    @Test
    fun voltageFromElectricChargeAndCapacitanceTest() {
        assertEquals(1(Abvolt), 2(Abcoulomb) / 2(Abfarad))
        assertEquals(1(Volt), 2(Coulomb) / 2(Farad))
    }

    @Test
    fun voltageFromElectricCurrentAndConductanceTest() {
        assertEquals(1(Abvolt), 2(Abampere) / 2(Absiemens))
        assertEquals(1(Abvolt), 2(Biot) / 2(Absiemens))
        assertEquals(1(Volt), 2(Ampere) / 2(Siemens))
    }

    @Test
    fun voltageFromElectricCurrentAndResistanceTest() {
        assertEquals(4(Abvolt), 2(Abampere) * 2(Abohm))
        assertEquals(4(Abvolt), 2(Abohm) * 2(Abampere))
        assertEquals(4(Abvolt), 2(Biot) * 2(Abohm))
        assertEquals(4(Abvolt), 2(Abohm) * 2(Biot))
        assertEquals(4(Volt), 2(Ampere) * 2(Ohm))
        assertEquals(4(Volt), 2(Ohm) * 2(Ampere))
    }

    @Test
    fun voltageFromEnergyAndElectricChargeTest() {
        assertEquals(1(Abvolt), 2(Erg) / 2(Abcoulomb))
        assertEquals(1(Abvolt), 20(Decierg) / 2(Abcoulomb))
        assertEquals(1(Volt), 2(Joule) / 2(Coulomb))
    }

    @Test
    fun voltageFromMagneticFluxAndTimeTest() {
        assertEquals(1(Abvolt), 2(Maxwell) / 2(Second))
        assertEquals(1(Volt), 2(Weber) / 2(Second))
    }

    @Test
    fun voltageFromPowerAndElectricCurrentTest() {
        assertEquals(1(Abvolt), 2(Erg per Second) / 2(Abampere))
        assertEquals(1(Abvolt), 2(Erg per Second) / 2(Biot))
        assertEquals(1(Volt), 2(Watt) / 2(Ampere))
    }
}
