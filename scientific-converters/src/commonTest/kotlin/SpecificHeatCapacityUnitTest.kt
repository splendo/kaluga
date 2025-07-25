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
import com.splendo.kaluga.scientific.converter.heatCapacity.div
import com.splendo.kaluga.scientific.converter.specificEnergy.div
import com.splendo.kaluga.scientific.converter.temperature.deltaValue
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.BritishThermalUnit
import com.splendo.kaluga.scientific.unit.Celsius
import com.splendo.kaluga.scientific.unit.Fahrenheit
import com.splendo.kaluga.scientific.unit.HorsepowerHour
import com.splendo.kaluga.scientific.unit.ImperialTon
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kelvin
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.UsTon
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.test.Test

class SpecificHeatCapacityUnitTest {

    @Test
    fun testInvertedConstructors() {
        assertEqualScientificValue(2(Joule per Kelvin per Kilogram), 2(Joule per Kilogram per Kelvin))
        assertEqualScientificValue(2(WattHour per Kelvin per Kilogram), 2(WattHour per Kilogram per Kelvin))
        assertEqualScientificValue(2(WattHour per Kelvin per Pound), 2(WattHour per Pound per Kelvin))
        assertEqualScientificValue(2(WattHour per Kelvin per Pound.ukImperial), 2(WattHour per Pound per Kelvin))
        assertEqualScientificValue(2(WattHour per Kelvin per Pound), 2(WattHour per Pound.ukImperial per Kelvin))
        assertEqualScientificValue(2(BritishThermalUnit per Kelvin per Pound), 2(BritishThermalUnit per Pound per Kelvin))
        assertEqualScientificValue(2(BritishThermalUnit per Kelvin per Pound.ukImperial), 2(BritishThermalUnit per Pound per Kelvin))
        assertEqualScientificValue(2(BritishThermalUnit per Kelvin per Pound), 2(BritishThermalUnit per Pound.ukImperial per Kelvin))
        assertEqualScientificValue(2(WattHour per Fahrenheit per Pound), 2(WattHour per Pound per Fahrenheit))
        assertEqualScientificValue(2(WattHour per Fahrenheit per Pound.usCustomary), 2(WattHour per Pound per Fahrenheit))
        assertEqualScientificValue(2(WattHour per Fahrenheit per Pound), 2(WattHour per Pound.usCustomary per Fahrenheit))
    }

    @Test
    fun specificHeatCapacityFromHeatCapacityAndWeightTest() {
        assertEqualScientificValue(1(Joule per Celsius per Kilogram), 2(Joule per Celsius) / 2(Kilogram))
        assertEqualScientificValue(1(WattHour per Celsius per Kilogram), 2(WattHour per Celsius) / 2(Kilogram))
        assertEqualScientificValue(1(WattHour per Celsius per Pound), 2(WattHour per Celsius) / 2(Pound))
        assertEqualScientificValue(1(WattHour per Celsius per Pound), 2(WattHour per Celsius) / 2(Pound.ukImperial))
        assertEqualScientificValue(1(WattHour per Fahrenheit per Pound), 2(WattHour per Fahrenheit) / 2(Pound))
        assertEqualScientificValue(1(WattHour per Fahrenheit per Pound), 2(WattHour per Fahrenheit) / 2(Pound.usCustomary))
        assertEqualScientificValue(1(HorsepowerHour per Celsius per Pound), 2(HorsepowerHour per Celsius) / 2(Pound))
        assertEqualScientificValue(1(HorsepowerHour per Celsius per Pound), 2(HorsepowerHour per Celsius) / 2(Pound.ukImperial))
        assertEqualScientificValue(1(HorsepowerHour per Fahrenheit per Pound), 2(HorsepowerHour per Fahrenheit) / 2(Pound))
        assertEqualScientificValue(1(HorsepowerHour per Fahrenheit per Pound), 2(HorsepowerHour per Fahrenheit) / 2(Pound.usCustomary))
        assertEqualScientificValue(1(Joule per Kelvin per Kilogram), 2(Joule per Celsius) / 2(Kilogram).convert(Pound), 9)
    }

    @Test
    fun specificHeatCapacityFromSpecificEnergyAndTemperatureTest() {
        assertEqualScientificValue(1(Joule per Celsius per Kilogram), 2(Joule per Kilogram) / 2(Celsius))
        assertEqualScientificValue(1(WattHour per Celsius per Kilogram), 2(WattHour per Kilogram) / 2(Celsius))
        assertEqualScientificValue(1(WattHour per Celsius per Pound), 2(WattHour per Pound) / 2(Celsius))
        assertEqualScientificValue(1(WattHour per Fahrenheit per Pound), 2(WattHour per Pound) / 2(Fahrenheit))
        assertEqualScientificValue(1(WattHour per Celsius per ImperialTon), 2(WattHour per ImperialTon) / 2(Celsius))
        assertEqualScientificValue(1(WattHour per Fahrenheit per UsTon), 2(WattHour per UsTon) / 2(Fahrenheit))
        assertEqualScientificValue(1(Joule per Kelvin per Kilogram), 2(Joule per Kilogram) / Fahrenheit.deltaValue(2(Kelvin)))
    }
}
