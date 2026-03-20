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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import kotlin.test.Test
import com.splendo.kaluga.test.base.IgnoreJs

@IgnoreJs
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
        val wattInCaloriePerSecond = Joule.convert(Decimal.ONE, Calorie)
        val wattInCaloriePerMinute = wattInCaloriePerSecond * 60.toDecimal()
        val wattInCalorieITPerSecond = Joule.convert(Decimal.ONE, Calorie.IT)
        val wattInCalorieITPerMinute = wattInCalorieITPerSecond * 60.toDecimal()

        assertScientificConversion(Decimal.ONE, Watt, wattInCaloriePerSecond, Calorie per Second, round = 32)
        assertScientificConversion(Decimal.ONE, Watt, wattInCaloriePerMinute, Calorie per Minute, round = 32)
        assertScientificConversion(Decimal.ONE, Watt, wattInCalorieITPerSecond, Calorie.IT per Second, round = 32)
        assertScientificConversion(Decimal.ONE, Watt, wattInCalorieITPerMinute, Calorie.IT per Minute, round = 32)
        assertScientificConversion(Decimal.ONE, Watt, Decimal.THOUSAND * wattInCaloriePerSecond, Millicalorie per Second, round = 32)
        assertScientificConversion(Decimal.ONE, Watt, Decimal.THOUSAND * wattInCaloriePerMinute, Millicalorie per Minute, round = 32)
        assertScientificConversion(Decimal.ONE, Watt, Decimal.THOUSAND * wattInCalorieITPerSecond, Millicalorie.IT per Second, round = 32)
        assertScientificConversion(Decimal.ONE, Watt, Decimal.THOUSAND * wattInCalorieITPerMinute, Millicalorie.IT per Minute, round = 32)
        assertScientificConversion(Decimal.ONE, Watt, wattInCaloriePerSecond / Decimal.THOUSAND, Kilocalorie per Second, round = 32)
        assertScientificConversion(Decimal.ONE, Watt, wattInCaloriePerMinute / Decimal.THOUSAND, Kilocalorie per Minute, round = 32)
        assertScientificConversion(Decimal.ONE, Watt, wattInCalorieITPerSecond / Decimal.THOUSAND, Kilocalorie.IT per Second, round = 32)
        assertScientificConversion(Decimal.ONE, Watt, wattInCalorieITPerMinute / Decimal.THOUSAND, Kilocalorie.IT per Minute, round = 32)
        assertScientificConversion(Decimal.ONE, Watt, wattInCaloriePerSecond / 1000000.toDecimal(), Megacalorie per Second, round = 32)
        assertScientificConversion(Decimal.ONE, Watt, wattInCaloriePerMinute / 1000000.toDecimal(), Megacalorie per Minute, round = 32)
        assertScientificConversion(Decimal.ONE, Watt, wattInCalorieITPerSecond / 1000000.toDecimal(), Megacalorie.IT per Second, round = 32)
        assertScientificConversion(Decimal.ONE, Watt, wattInCalorieITPerMinute / 1000000.toDecimal(), Megacalorie.IT per Minute, round = 32)

        assertScientificConversion(Decimal.ONE, Watt, Decimal.ONE / (75.toDecimal() * MetricStandardGravityAcceleration.decimalValue), MetricHorsepower, round = 32)

        val jouleInFootPoundForce = Joule.convert(Decimal.ONE, FootPoundForce)
        assertScientificConversion(Decimal.ONE, Watt, jouleInFootPoundForce, FootPoundForce per Second, round = 30)
        assertScientificConversion(Decimal.ONE, Watt, jouleInFootPoundForce * 60.toDecimal(), FootPoundForce per Minute, round = 30)
        assertScientificConversion(Decimal.ONE, Watt, jouleInFootPoundForce / 550.toDecimal(), Horsepower, round = 31)
        val jouleInBTU = Joule.convert(Decimal.ONE, BritishThermalUnit)
        assertScientificConversion(Decimal.ONE, Watt, jouleInBTU, BritishThermalUnit per Second, round = 32)
        assertScientificConversion(Decimal.ONE, Watt, jouleInBTU * 60.toDecimal(), BritishThermalUnit per Minute, round = 32)
        assertScientificConversion(Decimal.ONE, Watt, jouleInBTU * 3600.toDecimal(), BritishThermalUnit per Hour, round = 32)

        assertScientificConversion("1.0", Watt, "10.0", Deciwatt.metric)
        assertScientificConversion("1.0", Watt, "10.0", Deciwatt.imperial)
    }
}
