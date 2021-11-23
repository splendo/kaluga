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
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class ElectricChargeUnitTest {

    @Test
    fun electricChargeConversionTest() {
        assertEquals(1e+9, Coulomb.convert(1, Nanocoulomb))
        assertEquals(1e+6, Coulomb.convert(1, Microcoulomb))
        assertEquals(1000.0, Coulomb.convert(1, Millicoulomb))
        assertEquals(100.0, Coulomb.convert(1, Centicoulomb))
        assertEquals(10.0, Coulomb.convert(1, Decicoulomb))
        assertEquals(0.1, Coulomb.convert(1, Decacoulomb))
        assertEquals(0.01, Coulomb.convert(1, Hectocoulomb))
        assertEquals(0.001, Coulomb.convert(1, Kilocoulomb))
        assertEquals(1e-6, Coulomb.convert(1, Megacoulomb))
        assertEquals(1e-9, Coulomb.convert(1, Gigacoulomb))
        assertEquals(0.1, Coulomb.convert(1, Abcoulomb))
    }

    @Test
    fun chargeFromCapacitanceAndVoltageTest() {
        assertEquals(4(Coulomb), 2(Farad) * 2(Volt))
    }

    @Test
    fun chargeFromCurrentAndTimeTest() {
        assertEquals(4(Coulomb), 2(Ampere) * 2(Second))
    }

    @Test
    fun chargeFromEnergyAndVoltageTest() {
        assertEquals(1(Coulomb), 2(Joule) / 2(Volt))
    }

    @Test
    fun chargeFromFluxAndResistanceTest() {
        assertEquals(1(Coulomb), 2(Weber) / 2(Ohm))
    }
}