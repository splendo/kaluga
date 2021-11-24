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

import kotlin.test.Test
import kotlin.test.assertEquals

class PowerUnitTest {

    @Test
    fun powerConversionTest() {
        assertEquals(1e+9, Watt.convert(1, Nanowatt))
        assertEquals(1e+6, Watt.convert(1, Microwatt))
        assertEquals(1000.0, Watt.convert(1, Milliwatt))
        assertEquals(100.0, Watt.convert(1, Centiwatt))
        assertEquals(10.0, Watt.convert(1, Deciwatt))
        assertEquals(0.1, Watt.convert(1, Decawatt))
        assertEquals(0.01, Watt.convert(1, Hectowatt))
        assertEquals(0.001, Watt.convert(1, Kilowatt))
        assertEquals(1e-6, Watt.convert(1, Megawatt))
        assertEquals(1e-9, Watt.convert(1, Gigawatt))
        assertEquals(10000000.0, Watt.convert(1, ErgPerSecond))

        assertEquals(0.1, Watt.convert(1, FootPoundForcePerSecond))
        assertEquals(0.1, Watt.convert(1, FootPoundForcePerMinute))
        assertEquals(0.1, Watt.convert(1, Horsepower))
        assertEquals(0.1, Watt.convert(1, BritishThermalUnitPerSecond))
        assertEquals(0.1, Watt.convert(1, BritishThermalUnitPerMinute))
        assertEquals(0.1, Watt.convert(1, BritishThermalUnitPerHour))
    }
}