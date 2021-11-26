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

import com.splendo.kaluga.scientific.converter.heatCapacity.div
import com.splendo.kaluga.scientific.converter.specificEnergy.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class SpecificHeatCapacityUnitTest {

    @Test
    fun specificHeatCapacityConversionTest() {
        assertScientificConversion(1.0, (Joule per Kelvin per Kilogram), 0.00023885, BritishThermalUnit per Fahrenheit per Pound, 8)
    }

    @Test
    fun specificHeatCapacityFromHeatCapacityAndWeightTest() {
        assertEquals(1(Joule per Celsius per Kilogram), 2(Joule per Celsius) / 2(Kilogram))
        assertEquals(1(WattHour per Celsius per Kilogram), 2(WattHour per Celsius) / 2(Kilogram))
        assertEquals(1(WattHour per Celsius per Pound), 2(WattHour per Celsius) / 2(Pound))
        assertEquals(1(WattHour per Fahrenheit per Pound), 2(WattHour per Fahrenheit) / 2(Pound))
        assertEquals(1(HorsepowerHour per Celsius per Pound), 2(HorsepowerHour per Celsius) / 2(Pound))
        assertEquals(1(HorsepowerHour per Fahrenheit per Pound), 2(HorsepowerHour per Fahrenheit) / 2(Pound))
    }

    @Test
    fun specificHeatCapacityFromSpecificEnergyAndTemperatureTest() {
        assertEquals(1(Joule per Celsius per Kilogram), 2(Joule per Kilogram) / 2(Celsius))
        assertEquals(1(WattHour per Celsius per Kilogram), 2(WattHour per Kilogram) / 2(Celsius))
        assertEquals(1(WattHour per Celsius per Pound), 2(WattHour per Pound) / 2(Celsius))
        assertEquals(1(WattHour per Fahrenheit per Pound), 2(WattHour per Pound) / 2(Fahrenheit))
        assertEquals(1(WattHour per Celsius per ImperialTon),2(WattHour per ImperialTon) / 2(Celsius))
        assertEquals(1(WattHour per Fahrenheit per UsTon), 2(WattHour per UsTon) / 2(Fahrenheit))
    }
}
