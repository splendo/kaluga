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

import com.splendo.kaluga.scientific.converter.electricInductance.div
import com.splendo.kaluga.scientific.converter.electricInductance.times
import com.splendo.kaluga.scientific.converter.magneticFlux.div
import com.splendo.kaluga.scientific.converter.time.div
import com.splendo.kaluga.scientific.converter.voltage.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class ElectricResistanceUnitTest {

    @Test
    fun electricResistanceUnitTestConversionTest() {
        assertEquals(1e+9, Ohm.convert(1, Nanoohm))
        assertEquals(1e+6, Ohm.convert(1, Microohm))
        assertEquals(1000.0, Ohm.convert(1, Milliohm))
        assertEquals(100.0, Ohm.convert(1, Centiohm))
        assertEquals(10.0, Ohm.convert(1, Deciohm))
        assertEquals(0.1, Ohm.convert(1, Decaohm))
        assertEquals(0.01, Ohm.convert(1, HectoOhm))
        assertEquals(0.001, Ohm.convert(1, Kiloohm))
        assertEquals(1e-6, Ohm.convert(1, Megaohm))
        assertEquals(1e-9, Ohm.convert(1, Gigaohm))
        assertEquals(1000000000.0, Ohm.convert(1, Abohm))
    }

    @Test
    fun electricResistanceFromInvertedConductanceTest() {
        // TODO
    }

    @Test
    fun resistanceFromInductanceAndFrequencyTest() {
        assertEquals(4(Ohm), 2(Henry) * 2(Hertz))
    }

    @Test
    fun resistanceFromInductanceAndTimeTest() {
        assertEquals(1(Ohm), 2(Henry) / 2(Second))
    }

    @Test
    fun resistanceFromFluxAndChargeTest() {
        assertEquals(1(Ohm), 2(Weber) / 2(Coulomb))
    }

    @Test
    fun resistanceFromTimeAndCapacitanceTest() {
        assertEquals(1(Ohm), 2(Second) / 2(Farad))
    }

    @Test
    fun resistanceFromVoltageAndCurrentTest() {
        assertEquals(1(Ohm), 2(Volt) / 2(Ampere))
    }
}