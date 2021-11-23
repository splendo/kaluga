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

class ThermalResistanceUnitTest {

    @Test
    fun metricAndUKImperialThermalResistanceConversionTest() {
        assertEquals(1.0, (Celsius per Watt).convert(1.0, Kelvin per Watt))
        assertEquals(1_000.0, (Celsius per Watt).convert(1.0, Celsius per Kilowatt))
    }

    @Test
    fun metricThermalResistanceConversionTest() {
        assertEquals(735.5, (Celsius per Watt).convert(1.0, Celsius per MetricHorsepower, 1))
        assertEquals(735.5, (Kelvin per Watt).convert(1.0, Kelvin per MetricHorsepower, 1))
    }

    @Test
    fun ukImperialThermalResistanceConversionTest() {
        assertEquals(1460373.06, (Celsius per Watt).convert(1.0, Celsius per Horsepower, 2))
        assertEquals(1.36, (Celsius per Watt).convert(1.0, Celsius per FootPoundForcePerSecond, 2))
        assertEquals(1055.06, (Celsius per Watt).convert(1.0, Celsius per BritishThermalUnitPerSecond, 2))
    }

    @Test
    fun usCustomaryThermalResistanceUnitsConversionTest() {
        assertEquals(1077115.89, (Rankine per FootPoundForcePerSecond).convert(1.0, Rankine per Horsepower, 2))
        assertEquals(778.17, (Rankine per FootPoundForcePerSecond).convert(1.0, Rankine per BritishThermalUnitPerSecond, 2))
        assertEquals(1.0, (Rankine per FootPoundForcePerSecond).convert(1.0, Fahrenheit per FootPoundForcePerSecond, 2))

        assertEquals(1077115.89, (Fahrenheit per FootPoundForcePerSecond).convert(1.0, Fahrenheit per Horsepower, 2))
        assertEquals(778.17, (Fahrenheit per FootPoundForcePerSecond).convert(1.0, Fahrenheit per BritishThermalUnitPerSecond, 2))
        assertEquals(1.0, (Fahrenheit per FootPoundForcePerSecond).convert(1.0, Rankine per FootPoundForcePerSecond, 2))
    }
}
