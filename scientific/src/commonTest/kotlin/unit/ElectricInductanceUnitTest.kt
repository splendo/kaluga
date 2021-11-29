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

import com.splendo.kaluga.scientific.converter.electricResistance.div
import com.splendo.kaluga.scientific.converter.electricResistance.times
import com.splendo.kaluga.scientific.converter.magneticFlux.div
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class ElectricInductanceUnitTest {

    @Test
    fun electricInductanceConversionTest() {
        assertScientificConversion(1, Henry, 1e+9, Nanohenry)
        assertScientificConversion(1, Henry, 1e+6, Microhenry)
        assertScientificConversion(1, Henry, 1000.0, Millihenry)
        assertScientificConversion(1, Henry, 100.0, Centihenry)
        assertScientificConversion(1, Henry, 10.0, Decihenry)
        assertScientificConversion(1, Henry, 0.1, Decahenry)
        assertScientificConversion(1, Henry, 0.01, Hectohenry)
        assertScientificConversion(1, Henry, 0.001, Kilohenry)
        assertScientificConversion(1, Henry, 1e-6, Megahenry)
        assertScientificConversion(1, Henry, 1e-9, Gigahenry)
        assertScientificConversion(1, Henry, 1000000000.0, Abhenry)
    }

    @Test
    fun inductanceFromResistanceAndFrequencyTest() {
        assertEquals(1(Abhenry), 2(Abohm) / 2(Hertz))
        assertEquals(1(Henry), 2(Ohm) / 2(Hertz))
    }

    @Test
    fun inductanceFromResistanceAndTimeTest() {
        assertEquals(4(Abhenry), 2(Abohm) * 2(Second))
        assertEquals(4(Abhenry), 2(Second) * 2(Abohm))
        assertEquals(4(Henry), 2(Ohm) * 2(Second))
        assertEquals(4(Henry), 2(Second) * 2(Ohm))
        assertEquals(4(Abhenry), 2(Abohm) * 2(Second))
        assertEquals(4(Abhenry), 2(Second) * 2(Abohm))
    }

    @Test
    fun inductanceFromFluxAndCurrentTest() {
        assertEquals(1(Abhenry), 2(Maxwell) / 2(Abampere))
        assertEquals(1(Abhenry), 2(Maxwell) / 2(Biot))
        assertEquals(1(Henry), 2(Weber) / 2(Ampere))
        assertEquals(1(Henry), 2(Weber) / 2(Ampere))
    }
}
