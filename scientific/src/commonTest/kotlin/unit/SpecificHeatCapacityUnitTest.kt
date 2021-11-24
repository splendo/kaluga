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

class SpecificHeatCapacityUnitTest {

    @Test
    fun specificHeatCapacityConversionTest() {
        assertEquals(0.00028, (Joule per Celsius per Kilogram).convert(1.0, WattHour per Kelvin per Kilogram, 5))
        assertEquals(0.0013, (WattHour per Celsius per ImperialTon).convert(1.0, HorsepowerHour per Kelvin per ImperialTon, 4))
        assertEquals(3.8016e+8, (HorsepowerHour per Fahrenheit per UsTon).convert(1.0, InchOunceForce per Rankine per UsTon))
    }
}