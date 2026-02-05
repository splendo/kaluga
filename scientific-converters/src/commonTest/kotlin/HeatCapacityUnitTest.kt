/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.converter

import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.specificHeatCapacity.times
import com.splendo.kaluga.scientific.converter.weight.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Calorie
import com.splendo.kaluga.scientific.unit.Celsius
import com.splendo.kaluga.scientific.unit.Fahrenheit
import com.splendo.kaluga.scientific.unit.HorsepowerHour
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kelvin
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.Rankine
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.test.Test

class HeatCapacityUnitTest {

    @Test
    fun heatCapacityFromEnergyAndTemperature() {
        assertEqualScientificValue(1(Joule per Celsius), 2(Joule) / 2(Celsius))
        assertEqualScientificValue(1(Calorie per Kelvin), 2(Calorie) / 2(Kelvin))
        assertEqualScientificValue(1(HorsepowerHour per Celsius), 2(HorsepowerHour) / 2(Celsius))
        assertEqualScientificValue(1(Calorie per Fahrenheit), 2(Calorie) / 2(Fahrenheit), round = 22)
        assertEqualScientificValue(1(HorsepowerHour per Fahrenheit), 2(HorsepowerHour) / 2(Fahrenheit), round = 31)
        assertEqualScientificValue(1(Joule per Kelvin), 2(Joule) / 2(Kelvin).convert(Rankine), round = 32)
    }

    @Test
    fun heatCapacityFromWeightAndSpecificHeatCapacityTest() {
        assertEqualScientificValue(4(Joule per Celsius), 2((Joule per Kilogram) per Celsius) * 2(Kilogram))
        assertEqualScientificValue(4(Joule per Celsius), 2(Kilogram) * 2((Joule per Kilogram) per Celsius))
        assertEqualScientificValue(
            4(HorsepowerHour per Celsius),
            2((HorsepowerHour per Pound) per Celsius) * 2(Pound),
            round = 30,
        )
        assertEqualScientificValue(
            4(HorsepowerHour per Celsius),
            2(Pound) * 2((HorsepowerHour per Pound) per Celsius),
            round = 30,
        )
        assertEqualScientificValue(
            4(HorsepowerHour per Celsius),
            2((HorsepowerHour per Pound) per Celsius) * 2(Pound.ukImperial),
            round = 30,
        )
        assertEqualScientificValue(
            4(HorsepowerHour per Celsius),
            2(Pound.ukImperial) * 2((HorsepowerHour per Pound) per Celsius),
            round = 30,
        )
        assertEqualScientificValue(
            4(HorsepowerHour per Fahrenheit),
            2((HorsepowerHour per Pound) per Fahrenheit) * 2(Pound),
            round = 28,
        )
        assertEqualScientificValue(
            4(HorsepowerHour per Fahrenheit),
            2(Pound) * 2((HorsepowerHour per Pound) per Fahrenheit),
            round = 28,
        )
        assertEqualScientificValue(
            4(HorsepowerHour per Fahrenheit),
            2((HorsepowerHour per Pound) per Fahrenheit) * 2(Pound.usCustomary),
            round = 28,
        )
        assertEqualScientificValue(
            4(HorsepowerHour per Fahrenheit),
            2(Pound.usCustomary) * 2((HorsepowerHour per Pound) per Fahrenheit),
            round = 28,
        )
        assertEqualScientificValue(
            4(Joule per Kelvin),
            2((Joule per Kilogram) per Celsius) * 2(Kilogram).convert(Pound),
            round = 30,
        )
        assertEqualScientificValue(
            4(Joule per Kelvin),
            2(Kilogram).convert(Pound) * 2((Joule per Kilogram) per Celsius),
            round = 30,
        )
    }
}
