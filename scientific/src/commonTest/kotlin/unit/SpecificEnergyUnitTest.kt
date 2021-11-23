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

class SpecificEnergyUnitTest {

    @Test
    fun metricSpecificEnergyConversionTest() {
        assertEquals(1.0e+7, (Joule per Kilogram).convert(1.0, Erg per Kilogram))
    }

    @Test
    fun metricAndImperialSpecificEnergyConversionTest() {
        assertEquals(860.42, (WattHour per Pound).convert(1.0, Calorie per Pound, 2))
    }

    @Test
    fun ukImperialSpecificEnergyConversionTest() {
        assertEquals(1.0007, (BritishThermalUnit per ImperialTon).convert(1.0, BritishThermalUnit.Thermal per ImperialTon, 4))
    }

    @Test
    fun usCustomarySpecificEnergyConversionTest() {
        assertEquals(1_980_000.0, (HorsepowerHour per UsTon).convert(1.0, FootPoundForce per UsTon))
    }
}