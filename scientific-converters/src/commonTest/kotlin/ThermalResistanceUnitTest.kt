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

import com.splendo.kaluga.scientific.converter.temperature.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Fahrenheit
import com.splendo.kaluga.scientific.unit.Horsepower
import com.splendo.kaluga.scientific.unit.Kelvin
import com.splendo.kaluga.scientific.unit.Rankine
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test

class ThermalResistanceUnitTest {

    @Test
    fun thermalResistanceFromTemperatureAndPowerTest() {
        assertEqualScientificValue(1(Kelvin per Watt), 2(Kelvin) / 2(Watt))
        assertEqualScientificValue(1(Fahrenheit per Watt), 2(Fahrenheit) / 2(Watt), round = 32)
        assertEqualScientificValue(1(Rankine per Watt), 2(Rankine) / 2(Watt), round = 32)
        assertEqualScientificValue(1(Kelvin per Horsepower), 2(Kelvin) / 2(Horsepower), round = 20)
        assertEqualScientificValue(1(Fahrenheit per Horsepower), 2(Fahrenheit) / 2(Horsepower), round = 32)
        assertEqualScientificValue(1(Rankine per Horsepower), 2(Rankine) / 2(Horsepower), round = 32)
    }
}
