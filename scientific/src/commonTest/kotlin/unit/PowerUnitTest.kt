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

package com.splendo.kaluga.scientific.unit

import kotlin.test.Test

class PowerUnitTest {

    @Test
    fun powerConversionTest() {
        assertScientificConversion("1", Watt, "1e+9", Nanowatt)
        assertScientificConversion("1", Watt, "1e+6", Microwatt)
        assertScientificConversion("1", Watt, "1000.0", Milliwatt)
        assertScientificConversion("1", Watt, "100.0", Centiwatt)
        assertScientificConversion("1", Watt, "10.0", Deciwatt)
        assertScientificConversion("1", Watt, "0.1", Decawatt)
        assertScientificConversion("1", Watt, "0.01", Hectowatt)
        assertScientificConversion("1", Watt, "0.001", Kilowatt)
        assertScientificConversion("1", Watt, "1e-6", Megawatt)
        assertScientificConversion("1", Watt, "1e-9", Gigawatt)
        assertScientificConversion("1", Watt, "10000000.0", Erg per Second)
        assertScientificConversion("1", Watt, "0.239006", Calorie per Second, 6)
        assertScientificConversion("1", Watt, "14.340344", Calorie per Minute, 6)
        assertScientificConversion("1", Watt, "0.238846", Calorie.IT per Second, 6)
        assertScientificConversion("1", Watt, "14.330754", Calorie.IT per Minute, 6)
        assertScientificConversion("1", Watt, "239.005736", Millicalorie per Second, 6)
        assertScientificConversion("1", Watt, "14340.344168", Millicalorie per Minute, 6)
        assertScientificConversion("1", Watt, "238.845897", Millicalorie.IT per Second, 6)
        assertScientificConversion("1", Watt, "14330.753798", Millicalorie.IT per Minute, 6)
        assertScientificConversion("1", Watt, "0.000239006", Kilocalorie per Second, 9)
        assertScientificConversion("1", Watt, "0.014340344", Kilocalorie per Minute, 9)
        assertScientificConversion("1", Watt, "0.000238846", Kilocalorie.IT per Second, 9)
        assertScientificConversion("1", Watt, "0.014330754", Kilocalorie.IT per Minute, 9)
        assertScientificConversion("1", Watt, "0.000000239006", Megacalorie per Second, 12)
        assertScientificConversion("1", Watt, "0.000014340344", Megacalorie per Minute, 12)
        assertScientificConversion("1", Watt, "0.000000238846", Megacalorie.IT per Second, 12)
        assertScientificConversion("1", Watt, "0.000014330754", Megacalorie.IT per Minute, 12)

        assertScientificConversion("1", Watt, "0.00135962", MetricHorsepower, 8)

        assertScientificConversion("1", Watt, "0.74", FootPoundForce per Second, 2)
        assertScientificConversion("1", Watt, "44.25", FootPoundForce per Minute, 2)
        assertScientificConversion("1", Watt, "0.0013", Horsepower, 4)
        assertScientificConversion("1", Watt, "0.00095", BritishThermalUnit per Second, 5)
        assertScientificConversion("1", Watt, "0.06", BritishThermalUnit per Minute, 2)
        assertScientificConversion("1", Watt, "3.41", BritishThermalUnit per Hour, 2)

        assertScientificConversion("1.0", Watt, "10.0", Deciwatt.metric)
        assertScientificConversion("1.0", Watt, "10.0", Deciwatt.imperial)
    }
}
