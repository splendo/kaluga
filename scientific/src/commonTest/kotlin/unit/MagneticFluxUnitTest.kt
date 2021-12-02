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

import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.electricCharge.times
import com.splendo.kaluga.scientific.converter.electricCurrent.times
import com.splendo.kaluga.scientific.converter.electricInductance.times
import com.splendo.kaluga.scientific.converter.electricResistance.times
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.magneticInduction.times
import com.splendo.kaluga.scientific.converter.time.times
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
        assertEquals(4(Maxwell), 2(Abhenry) * 2(Abampere))
        assertEquals(4(Maxwell), 2(Abampere) * 2(Abhenry))
        assertEquals(4(Maxwell), 2(Abhenry) * 2(Biot))
        assertEquals(4(Maxwell), 2(Biot) * 2(Abhenry))
        assertEquals(4(Weber), 2(Henry) * 2(Ampere))
        assertEquals(4(Weber), 2(Ampere) * 2(Henry))
    }

    @Test
    fun fluxFromResistanceAndChargeTest() {
        assertEquals(4(Maxwell), 2(Abohm) * 2(Abcoulomb))
        assertEquals(4(Maxwell), 2(Abcoulomb) * 2(Abohm))
        assertEquals(4(Weber), 2(Ohm) * 2(Coulomb))
        assertEquals(4(Weber), 2(Coulomb) * 2(Ohm))
    }

    @Test
    fun fluxFromEnergyAndCurrentTest() {
        assertEquals(1(Maxwell), 2(Erg) / 2(Abampere))
        assertEquals(1(Maxwell), 20(Decierg) / 2(Abampere))
        assertEquals(1(Maxwell), 2(Erg) / 2(Biot))
        assertEquals(1(Maxwell), 20(Decierg) / 2(Biot))
        assertEquals(1(Weber), 2(Joule) / 2(Ampere))
    }

    @Test
    fun fluxFromInductionAndAreaTest() {
        assertEquals(4(Maxwell), 2(Gauss) * 2(SquareCentimeter))
        assertEquals(4(Maxwell), 2(SquareCentimeter) * 2(Gauss))
        assertEquals(4(Weber), 2(Tesla) * 2(SquareMeter))
        assertEquals(4(Weber), 2(SquareMeter) * 2(Tesla))
    }

    @Test
    fun fluxFromVoltageAndTimeTest() {
        assertEquals(4(Maxwell), 2(Abvolt) * 2(Second))
        assertEquals(4(Maxwell), 2(Second) * 2(Abvolt))
        assertEquals(4(Weber), 2(Volt) * 2(Second))
        assertEquals(4(Weber), 2(Second) * 2(Volt))
    }
}
