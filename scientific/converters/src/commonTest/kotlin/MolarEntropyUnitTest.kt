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

import com.splendo.kaluga.scientific.converter.amountOfSubstance.times
import com.splendo.kaluga.scientific.converter.heatCapacity.div
import com.splendo.kaluga.scientific.converter.molarEntropy.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.BritishThermalUnit
import com.splendo.kaluga.scientific.unit.Celsius
import com.splendo.kaluga.scientific.unit.Fahrenheit
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kelvin
import com.splendo.kaluga.scientific.unit.Mole
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test

class MolarEntropyUnitTest {

    @Test
    fun molarEntropyFromHeatCapacityAndAmountOfSubstanceTest() {
        assertEqualScientificValue(1(Joule per Kelvin per Mole), 2(Joule per Kelvin) / 2(Mole))
        assertEqualScientificValue(1(WattHour per Celsius per Mole), 2(WattHour per Celsius) / 2(Mole))
        assertEqualScientificValue(1(BritishThermalUnit per Fahrenheit per Mole), 2(BritishThermalUnit per Fahrenheit) / 2(Mole), 30)
    }

    @Test
    fun heatCapacityFromMolarEntropyAndAmountOfSubstanceTest() {
        assertEqualScientificValue(4(Joule per Kelvin), 2(Joule per Kelvin per Mole) * 2(Mole))
        assertEqualScientificValue(4(Joule per Kelvin), 2(Mole) * 2(Joule per Kelvin per Mole))
        assertEqualScientificValue(4(WattHour per Celsius), 2(WattHour per Celsius per Mole) * 2(Mole))
        assertEqualScientificValue(4(WattHour per Celsius), 2(Mole) * 2(WattHour per Celsius per Mole))
        assertEqualScientificValue(4(BritishThermalUnit per Fahrenheit), 2(BritishThermalUnit per Fahrenheit per Mole) * 2(Mole), 30)
        assertEqualScientificValue(4(BritishThermalUnit per Fahrenheit), 2(Mole) * 2(BritishThermalUnit per Fahrenheit per Mole), 30)
    }

    @Test
    fun amountOfSubstanceFromHeatCapacityAndMolarEntropyTest() {
        assertEqualScientificValue(2(Mole), 4(Joule per Kelvin) / 2(Joule per Kelvin per Mole))
        assertEqualScientificValue(2(Mole), 4(WattHour per Celsius) / 2(WattHour per Celsius per Mole))
        assertEqualScientificValue(2(Mole), 4(BritishThermalUnit per Fahrenheit) / 2(BritishThermalUnit per Fahrenheit per Mole), 30)
    }
}
