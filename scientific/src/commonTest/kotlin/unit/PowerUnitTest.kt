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

class PowerUnitTest {

    @Test
    fun powerConversionTest() {
        assertScientificConversion(1, Watt, 1e+9, Nanowatt)
        assertScientificConversion(1, Watt, 1e+6, Microwatt)
        assertScientificConversion(1, Watt, 1000.0, Milliwatt)
        assertScientificConversion(1, Watt, 100.0, Centiwatt)
        assertScientificConversion(1, Watt, 10.0, Deciwatt)
        assertScientificConversion(1, Watt, 0.1, Decawatt)
        assertScientificConversion(1, Watt, 0.01, Hectowatt)
        assertScientificConversion(1, Watt, 0.001, Kilowatt)
        assertScientificConversion(1, Watt, 1e-6, Megawatt)
        assertScientificConversion(1, Watt, 1e-9, Gigawatt)
        assertScientificConversion(1, Watt, 10000000.0, ErgPerSecond)

        assertScientificConversion(1, Watt, 0.00135962, MetricHorsepower, 8)

        assertScientificConversion(1, Watt, 0.74, FootPoundForcePerSecond, 2)
        assertScientificConversion(1, Watt, 44.25, FootPoundForcePerMinute, 2)
        assertScientificConversion(1, Watt, 0.0013, Horsepower, 4)
        assertScientificConversion(1, Watt, 0.00095, BritishThermalUnitPerSecond, 5)
        assertScientificConversion(1, Watt, 0.06, BritishThermalUnitPerMinute, 2)
        assertScientificConversion(1, Watt, 3.41, BritishThermalUnitPerHour, 2)
    }
}