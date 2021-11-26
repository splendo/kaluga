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
import com.splendo.kaluga.scientific.converter.electricConductance.times
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.magneticFlux.div
import com.splendo.kaluga.scientific.converter.power.div
import com.splendo.kaluga.scientific.converter.voltage.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class ElectricCurrentUnitTest {

    @Test
    fun electricCurrentConversionTest() {
        assertScientificConversion(1, Ampere, 1e+9, Nanoampere)
        assertScientificConversion(1, Ampere, 1e+6, Microampere)
        assertScientificConversion(1, Ampere, 1000.0, Milliampere)
        assertScientificConversion(1, Ampere, 100.0, Centiampere)
        assertScientificConversion(1, Ampere, 10.0, Deciampere)
        assertScientificConversion(1, Ampere, 0.1, Decaampere)
        assertScientificConversion(1, Ampere, 0.01, Hectoampere)
        assertScientificConversion(1, Ampere, 0.001, Kiloampere)
        assertScientificConversion(1, Ampere, 1e-6, Megaampere)
        assertScientificConversion(1, Ampere, 1e-9, Gigaampere)

        assertScientificConversion(1, Ampere, 0.1, Biot)
        assertScientificConversion(1, Ampere, 0.1, Abampere)
    }

    @Test
    fun currentFromChargeAndTimeTest() {
        assertEquals(1(Ampere), 1(Coulomb) / 1(Second))
    }

    @Test
    fun currentFromEnergyAndFluxTest() {
        assertEquals(1(Ampere), 1(Joule) / 1(Weber))
    }

    @Test
    fun currentFromFluxAndInductanceTest() {
        assertEquals(1(Ampere), 2(Weber) / 2(Henry))
    }

    @Test
    fun currentFromPowerAndVoltageTest() {
        assertEquals(1(Ampere), 2(Watt) / 2(Volt))
    }

    @Test
    fun currentFromVoltageAndConductanceTest() {
        assertEquals(4(Ampere), 2(Siemens) * 2(Volt))
    }

    @Test
    fun currentFromVoltageAndResistanceTest() {
        assertEquals(1(Ampere), 2(Volt) / 2(Ohm))
    }
}