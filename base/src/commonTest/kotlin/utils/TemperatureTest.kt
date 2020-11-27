/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package utils

import com.splendo.kaluga.base.utils.Locale
import com.splendo.kaluga.base.utils.TemperatureMeasurement
import com.splendo.kaluga.base.utils.TemperatureUnit
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.asserter

private fun messagePrefix(message: String?) = if (message == null) "" else "$message. "

private fun assertEquals(expected: Double, actual: Double?, epsilon: Double, message: String? = null) {
    asserter.assertNotNull(null, actual)
    asserter.assertTrue(
        { messagePrefix(message) + "Expected <$expected>, actual <$actual>, should differ no more than <$epsilon>." },
        abs(expected - actual!!) <= epsilon
    )
}

class TemperatureTest {

    @Test
    fun testTemperatureUnitSymbol() {
        val unitC = TemperatureUnit.CELSIUS
        assertEquals("°C", unitC.symbol)

        val unitF = TemperatureUnit.FAHRENHEIT
        assertEquals("°F", unitF.symbol)
    }

    @Test
    fun testTemperatureConversion() {
        val unitC = TemperatureMeasurement(36.6, TemperatureUnit.CELSIUS)
        val valueF = unitC.convert(TemperatureUnit.FAHRENHEIT)
        assertEquals(97.88, valueF, epsilon = 0.01)

        val unitF = TemperatureMeasurement(99.0, TemperatureUnit.FAHRENHEIT)
        val valueC = unitF.convert(TemperatureUnit.CELSIUS)
        assertEquals(37.22, valueC, epsilon = 0.01)
    }

    @Test
    fun testFormatter() {
        val unitC = TemperatureMeasurement(36.6, TemperatureUnit.CELSIUS)
        val stringC = unitC.format(inUnit = TemperatureUnit.CELSIUS)
        assertEquals("36.6°C", stringC)

        val usLocale = Locale.createLocale("en", "US")
        val stringF = unitC.format(usLocale)
        assertEquals("97.9°F", stringF)
    }
}
