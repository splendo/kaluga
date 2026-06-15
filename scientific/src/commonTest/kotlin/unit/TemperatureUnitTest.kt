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
import com.splendo.kaluga.base.utils.minus
import com.splendo.kaluga.base.utils.plus
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import com.splendo.kaluga.test.base.IgnoreJs

@IgnoreJs
class TemperatureUnitTest {

    @Test
    fun temperatureConversionTest() {
        assertScientificConversion("1.0", Celsius, "274.15", Kelvin)
        assertScientificConversion("1.0", Celsius, "33.8", Fahrenheit, round = 32)
        assertScientificConversion("1.0", Celsius, "493.47", Rankine, round = 32)
        assertScientificConversion("1.0", Kelvin, "-457.87", Fahrenheit, round = 32)
        assertScientificConversion("1.0", Kelvin, "1.8", Rankine, round = 32)

        val kelvinInCelsius = "273.15".toDecimal()
        val rankineInKelvin = 5.toDecimal() / 9.toDecimal()
        val oneFahrenheitInCelcius = (-31).toDecimal() * rankineInKelvin
        assertScientificConversion(Decimal.ONE, Kelvin, Decimal.ONE - kelvinInCelsius, Celsius)
        assertScientificConversion(Decimal.ONE, Fahrenheit, oneFahrenheitInCelcius, Celsius, round = 30)
        assertScientificConversion(Decimal.ONE, Rankine, "-490.67".toDecimal() * rankineInKelvin, Celsius, round = 30)
        assertScientificConversion(Decimal.ONE, Fahrenheit, oneFahrenheitInCelcius + kelvinInCelsius, Kelvin, round = 30)
        assertScientificConversion(Decimal.ONE, Rankine, rankineInKelvin, Kelvin)
    }

    @Test
    fun testDeltaConversion() {
        assertEquals(1.0, Celsius.convertDelta(1, Kelvin))
        assertEquals(1.0, Kelvin.convertDelta(1, Celsius))
        assertEquals(1.8, Kelvin.convertDelta(1, Rankine, 2))
        assertEquals(1.8, Kelvin.convertDelta(1, Fahrenheit, 2))
        assertEquals(0.5556, Rankine.convertDelta(1, Kelvin, 4))
        assertEquals(0.5556, Fahrenheit.convertDelta(1, Kelvin, 4))
    }
}
