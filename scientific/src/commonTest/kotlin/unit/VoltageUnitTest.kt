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
        assertEquals(4(Abcoulomb), 2(Abvolt) * 2(Abfarad))
        assertEquals(4(Coulomb), 2(Volt) * 2(Farad))

        // convertToElectricCurrent
        assertEquals(4(Abampere), 2(Abvolt) * 2(Absiemens))
        assertEquals(4(Ampere), 2(Volt) * 2(Siemens))
        assertEquals(1(Abampere), 2(Abvolt) / 2(Abohm))
        assertEquals(1(Ampere), 2(Volt) / 2(Ohm))

        // convertToElectricResistance
        assertEquals(1(Abohm), 2(Abvolt) / 2(Abampere))
        assertEquals(1(Abohm), 2(Abvolt) / 2(Biot))
        assertEquals(1(Ohm), 1(Volt) / 1(Ampere))

        // convertToEnergy
        assertEquals(4(Erg), 2(Abvolt) * 2(Abcoulomb))
        assertEquals(4(Joule), 2(Volt) * 2(Coulomb))

        // convertToMagneticFlux
        assertEquals(4(Maxwell), 2(Abvolt) * 2(Second))
        assertEquals(4(Weber), 2(Volt) * 2(Second))

        // convertToPower
        assertEquals(4(ErgPerSecond), 2(Abvolt) * 2(Abampere))
        assertEquals(4(ErgPerSecond), 2(Abvolt) * 2(Biot))
        assertEquals(4(Watt), 2(Volt) * 2(Ampere))
    }
}