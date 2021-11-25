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

import com.splendo.kaluga.scientific.converter.temperature.deltaValueInKelvin
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class TemperatureUnitTest {

    @Test
    fun temperatureConversionTest() {
        assertScientificConversion(1.0, Celsius, 274.15, Kelvin, 2)
        assertScientificConversion(1.0, Celsius, 33.8, Fahrenheit, 2)
        assertScientificConversion(1.0, Celsius, 493.47, Rankine, 2)
        assertScientificConversion(1.0, Kelvin, -457.87, Fahrenheit, 2)
        assertScientificConversion(1.0, Kelvin, 1.8, Rankine, 2)

        assertScientificConversion(1.0, Kelvin, -272.15, Celsius, 2)
        assertScientificConversion(1.0, Fahrenheit, -17.22, Celsius, 2)
        assertScientificConversion(1.0, Rankine, -272.59, Celsius, 2)
        assertScientificConversion(1.0, Fahrenheit, 255.93, Kelvin, 2)
        assertScientificConversion(1.0, Rankine, 0.56, Kelvin, 2)
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

    @Test
    fun deltaToKelvinTest() {
        // FIXME find out expected values
        // assertEquals(273.15(Kelvin), 1(Celsius).deltaValueInKelvin())
        // assertEquals(273.15(Kelvin), 1(Fahrenheit).deltaValueInKelvin())
        // assertEquals(0.555556(Kelvin), 1(Rankine).deltaValueInKelvin())
    }

    @Test
    fun temperatureFromEnergyAndHeatCapacityTest() {
        // TODO
    }

    @Test
    fun temperatureFromSpecificEnergyAndSpecificHeatCapacityTest() {
        // TODO
    }

    @Test
    fun temperatureFromThermalResistanceAndPowerTest() {
        // TODO
    }

    @Test
    fun kelvinToDelta() {
        // TODO
    }
}