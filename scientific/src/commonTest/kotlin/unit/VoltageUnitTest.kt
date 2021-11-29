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
import com.splendo.kaluga.scientific.converter.electricCurrent.div
import com.splendo.kaluga.scientific.converter.electricCurrent.times
import com.splendo.kaluga.scientific.converter.electricResistance.times
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.magneticFlux.div
import com.splendo.kaluga.scientific.converter.power.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class VoltageUnitTest {

    @Test
    fun voltConversionTest() {
        assertScientificConversion(1.0, Volt, 1e+9, Nanovolt)
        assertScientificConversion(1.0, Volt, 1e+8, Abvolt)
        assertScientificConversion(1.0, Volt, 1e+6, Microvolt)
        assertScientificConversion(1.0, Volt, 1_000.0, Millivolt)
        assertScientificConversion(1.0, Volt, 100.0, Centivolt)
        assertScientificConversion(1.0, Volt, 10.0, Decivolt)
        assertScientificConversion(1.0, Volt, 0.1, Decavolt)
        assertScientificConversion(1.0, Volt, 0.01, Hectovolt)
        assertScientificConversion(1.0, Volt, 0.001, Kilovolt)
        assertScientificConversion(1.0, Volt, 1e-6, Megavolt)
        assertScientificConversion(1.0, Volt, 1e-9, Gigavolt)
    }

    @Test
    fun voltageFromElectricChargeAndCapacitance() {
        assertEquals(1(Abvolt), 2(Abcoulomb) / 2(Abfarad))
        assertEquals(1(Volt), 2(Coulomb) / 2(Farad))
    }

    @Test
    fun voltageFromElectricCurrentAndConductance() {
        assertEquals(1(Abvolt), 2(Abampere) / 2(Absiemens))
        assertEquals(1(Abvolt), 2(Biot) / 2(Absiemens))
        assertEquals(1(Volt), 2(Ampere) / 2(Siemens))
    }

    @Test
    fun voltageFromElectricCurrentAndResistance() {
        assertEquals(4(Abvolt), 2(Abampere) * 2(Abohm))
        assertEquals(4(Abvolt), 2(Abohm) * 2(Abampere))
        assertEquals(4(Abvolt), 2(Biot) * 2(Abohm))
        assertEquals(4(Abvolt), 2(Abohm) * 2(Biot))
        assertEquals(4(Volt), 2(Ampere) * 2(Abohm))
        assertEquals(4(Volt), 2(Abohm) * 2(Ampere))
    }

    @Test
    fun voltageFromEnergyAndElectricCharge() {
        assertEquals(1(Abvolt), 2(Erg) / 2(Abcoulomb))
        assertEquals(1(Abvolt), 20(Decierg) / 2(Abcoulomb))
        assertEquals(1(Volt), 2(Joule) / 2(Coulomb))
    }

    @Test
    fun voltageFromMagneticFluxAndTime() {
        assertEquals(1(Abvolt), 2(Maxwell) / 2(Second))
        assertEquals(1(Volt), 2(Weber) / 2(Second))
    }

    @Test
    fun voltageFromPowerAndElectricCurrent() {
        assertEquals(1(Abvolt), 2(ErgPerSecond) / 2(Abampere))
        assertEquals(1(Abvolt), 2(ErgPerSecond) / 2(Biot))
        assertEquals(1(Volt), 2(Watt) / 2(Ampere))
    }
}