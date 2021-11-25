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

import com.splendo.kaluga.scientific.converter.electricInductance.times
import com.splendo.kaluga.scientific.converter.electricResistance.times
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.magneticInduction.times
import com.splendo.kaluga.scientific.converter.voltage.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class MagneticFluxUnitTest {

    @Test
    fun luminusIntensityConversionTest() {
        assertScientificConversion(1, Weber, 1e+9, Nanoweber)
        assertScientificConversion(1, Weber, 1e+6, Microweber)
        assertScientificConversion(1, Weber, 1000.0, Milliweber)
        assertScientificConversion(1, Weber, 100.0, Centiweber)
        assertScientificConversion(1, Weber, 10.0, Deciweber)
        assertScientificConversion(1, Weber, 0.1, Decaweber)
        assertScientificConversion(1, Weber, 0.01, Hectoweber)
        assertScientificConversion(1, Weber, 0.001, Kiloweber)
        assertScientificConversion(1, Weber, 1e-6, Megaweber)
        assertScientificConversion(1, Weber, 1e-9, Gigaweber)
        assertScientificConversion(1, Weber, 100000000.0, Maxwell)
    }

    @Test
    fun fluxFromInductanceAndCurrentTest() {
        assertEquals(4(Weber), 2(Henry) * 2(Ampere))
    }

    @Test
    fun fluxFromResistanceAndChargeTest() {
        assertEquals(4(Weber), 2(Ohm) * 2(Coulomb))
    }

    @Test
    fun fluxFromEnergyAndCurrentTest() {
        assertEquals(1(Weber), 2(Joule) / 2(Ampere))
        // TODO figure out expects for WattHour and HorsepowerHour
        //assertEquals(1(Weber), 2(WattHour) / 2(Ampere))
        //assertEquals(1(Weber), 2(HorsepowerHour) / 2(Ampere))
    }

    @Test
    fun fluxFromInductionAndAreaTest() {
        assertEquals(4(Weber), 2(Tesla) * 2(SquareMeter))
        // TODO check if correct, from google [1 Tesla in Weber/Square Foot is Equal to	0.09290304] so I did that * 4
        assertEquals(0.37161216(Weber), 2(Tesla) * 2(SquareFoot))
    }

    @Test
    fun fluxFromVoltageAndTimeTest() {
        assertEquals(4(Weber), 2(Volt) * 2(Second))
    }
}