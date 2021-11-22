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

import com.splendo.kaluga.scientific.converter.voltage.div
import com.splendo.kaluga.scientific.converter.voltage.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class VoltageUnitTest {

    @Test
    fun voltConversionTest() {
        assertEquals(1e+9, Volt.convert(1.0, Nanovolt))
        assertEquals(1e+8, Volt.convert(1.0, Abvolt))
        assertEquals(1e+6, Volt.convert(1.0, Microvolt))
        assertEquals(1_000.0, Volt.convert(1.0, Millivolt))
        assertEquals(100.0, Volt.convert(1.0, Centivolt))
        assertEquals(10.0, Volt.convert(1.0, Decivolt))
        assertEquals(0.1, Volt.convert(1.0, Decavolt))
        assertEquals(0.01, Volt.convert(1.0, Hectovolt))
        assertEquals(0.001, Volt.convert(1.0, Kilovolt))
        assertEquals(1e-6, Volt.convert(1.0, Megavolt))
        assertEquals(1e-9, Volt.convert(1.0, Gigavolt))
    }

    @Test
    fun voltConvertWattConversionTest() {
        // convertToElectricCharge
        assertEquals(1(Abcoulomb), 1(Abvolt) * 1(Abfarad))
        assertEquals(1(Coulomb), 1(Volt) * 1(Farad))

        // convertToElectricCurrent
        assertEquals(1(Abampere), 1(Abvolt) * 1(Absiemens))
        assertEquals(1(Ampere), 1(Volt) * 1(Siemens))
        assertEquals(1(Abampere), 1(Abvolt) / 1(Abohm))
        assertEquals(1(Ampere), 1(Volt) / 1(Ohm))

        // convertToElectricResistance
        assertEquals(1(Abohm), 1(Abvolt) / 1(Abampere))
        assertEquals(1(Abohm), 1(Abvolt) / 1(Biot))
        assertEquals(1(Ohm), 1(Volt) / 1(Ampere))

        // convertToEnergy
        assertEquals(1(Erg), 1(Abvolt) * 1(Abcoulomb))
        assertEquals(1(Joule), 1(Volt) * 1(Coulomb))

        // convertToMagneticFlux
        assertEquals(1(Maxwell), 1(Abvolt) * 1(Second))
        assertEquals(1(Weber), 1(Volt) * 1(Second))

        // convertToPower
        assertEquals(1(ErgPerSecond), 1(Abvolt) * 1(Abampere))
        assertEquals(1(ErgPerSecond), 1(Abvolt) * 1(Biot))
        assertEquals(1(Watt), 1(Volt) * 1(Ampere))
    }
}