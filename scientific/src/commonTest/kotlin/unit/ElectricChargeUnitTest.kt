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

import com.splendo.kaluga.scientific.converter.electricCapacitance.times
import com.splendo.kaluga.scientific.converter.electricCurrent.times
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.magneticFlux.div
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.converter.voltage.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class ElectricChargeUnitTest {

    @Test
    fun electricChargeConversionTest() {
        assertScientificConversion(1, Coulomb, 1e+9, Nanocoulomb)
        assertScientificConversion(1, Coulomb, 1e+6, Microcoulomb)
        assertScientificConversion(1, Coulomb, 1000.0, Millicoulomb)
        assertScientificConversion(1, Coulomb, 100.0, Centicoulomb)
        assertScientificConversion(1, Coulomb, 10.0, Decicoulomb)
        assertScientificConversion(1, Coulomb, 0.1, Decacoulomb)
        assertScientificConversion(1, Coulomb, 0.01, Hectocoulomb)
        assertScientificConversion(1, Coulomb, 0.001, Kilocoulomb)
        assertScientificConversion(1, Coulomb, 1e-6, Megacoulomb)
        assertScientificConversion(1, Coulomb, 1e-9, Gigacoulomb)
        assertScientificConversion(1, Coulomb, 0.1, Abcoulomb)
    }

    @Test
    fun chargeFromCapacitanceAndVoltageTest() {
        assertEquals(4(Abcoulomb), 2(Abfarad) * 2(Abvolt))
        assertEquals(4(Coulomb), 2(Farad) * 2(Volt))
        assertEquals(4(Coulomb), 2(Volt) * 2(Farad))
        assertEquals(4(Abcoulomb), 2(Abfarad) * 2(Abvolt))
        assertEquals(4(Abcoulomb), 2(Abvolt) * 2(Abfarad))
    }

    @Test
    fun chargeFromCurrentAndTimeTest() {
        assertEquals(4(Abcoulomb), 2(Abampere) * 2(Second))
        assertEquals(4(Abcoulomb), 2(Biot) * 2(Second))
        assertEquals(4(Coulomb), 2(Ampere) * 2(Second))
        assertEquals(4(Coulomb), 2(Second) * 2(Ampere))
        assertEquals(4(Abcoulomb), 2(Abampere) * 2(Second))
        assertEquals(4(Abcoulomb), 2(Second) * 2(Abampere))
    }

    @Test
    fun chargeFromEnergyAndVoltageTest() {
        assertEquals(1(Abcoulomb), 2(Erg) / 2(Abvolt))
        assertEquals(1(Abcoulomb), 20(Decierg) / 2(Abvolt))
        assertEquals(1(Coulomb), 2(Joule) / 2(Volt))
    }

    @Test
    fun chargeFromFluxAndResistanceTest() {
        assertEquals(1(Abcoulomb), 2(Maxwell) / 2(Abohm))
        assertEquals(1(Coulomb), 2(Weber) / 2(Ohm))
    }
}
