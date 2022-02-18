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

import com.splendo.kaluga.scientific.converter.temperature.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class ThermalResistanceUnitTest {

    @Test
    fun thermalResistanceConversionTest() {
        assertScientificConversion(1.0, (Celsius per Megawatt), 0.00074, Kelvin per MetricHorsepower, 5)
        assertScientificConversion(1.0, (Kelvin per MetricHorsepower), 0.000055, Fahrenheit per FootPoundForcePerMinute, 6)
        assertScientificConversion(1.0, (Celsius per Watt), 31.65, Rankine per BritishThermalUnitPerMinute, 2)
    }

    @Test
    fun thermalResistanceFromTemperatureAndPowerTest() {
        assertEquals(1(Kelvin per Watt), 2(Kelvin) / 2(Watt))
        assertEquals(1(Fahrenheit per Watt), 2(Fahrenheit) / 2(Watt))
        assertEquals(1(Rankine per Watt), 2(Rankine) / 2(Watt))
        assertEquals(1(Kelvin per Horsepower), 2(Kelvin) / 2(Horsepower))
        assertEquals(1(Fahrenheit per Horsepower), 2(Fahrenheit) / 2(Horsepower))
        assertEquals(1(Rankine per Horsepower), 2(Rankine) / 2(Horsepower))
    }
}
