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

import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.specificHeatCapacity.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class HeatCapacityUnitTest {

    @Test
    fun heatCapacityConversionTest() {
        assertEquals(0.737562, (Joule per Kelvin).convert(1, FootPoundForce per Celsius, 6))
        assertEquals(0.409757, (Joule per Kelvin).convert(1, FootPoundForce per Fahrenheit, 6))
    }

    @Test
    fun heatCapacityFromEnergyAndTemperature() {
        assertEquals(1(Joule per Celsius), 2(Joule) / 2(Celsius))
        assertEquals(1(HorsepowerHour per Fahrenheit), 2(HorsepowerHour) / 2(Fahrenheit))
        assertEquals(1(Calorie per Kelvin), 2(Calorie) / 2(Kelvin))
    }

    @Test
    fun heatCapacityFromWeightAndSpecificHeatCapacityTest() {
        assertEquals(4(Joule per Celsius), 2((Joule per Kilogram) per Celsius) * 2(Kilogram))
        assertEquals(1(HorsepowerHour per Fahrenheit), 2((HorsepowerHour per Pound) per Fahrenheit) * 2(Pound))
        // TODO find a way for Calorie ?
    }
}