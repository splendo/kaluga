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

import com.splendo.kaluga.scientific.assertEqualScientificValue
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.power.times
import com.splendo.kaluga.scientific.converter.specificEnergy.div
import com.splendo.kaluga.scientific.converter.thermalResistance.times
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
    fun temperatureFromEnergyAndHeatCapacityTest() {
        assertEqualScientificValue(1(Celsius), 2(WattHour) / 2(WattHour per Celsius))
        assertEqualScientificValue(1(Celsius), 2(WattHour) / 2(WattHour.metric per Celsius))
        assertEqualScientificValue(1(Celsius), 2(WattHour) / 2(WattHour.imperial per Celsius))
        assertEqualScientificValue(1(Fahrenheit), 2(WattHour) / 2(WattHour per Fahrenheit))
        assertEqualScientificValue(1(Celsius), 2(WattHour.imperial) / 2(WattHour per Celsius))
        assertEqualScientificValue(
            1(Celsius),
            2(BritishThermalUnit) / 2(BritishThermalUnit per Celsius),
        )
        assertEqualScientificValue(
            1(Fahrenheit),
            2(BritishThermalUnit) / 2(BritishThermalUnit per Fahrenheit),
        )
        assertEqualScientificValue(
            1(Kelvin),
            2(Joule).convert(BritishThermalUnit) / 2(Joule per Kelvin),
        )
    }

    @Test
    fun temperatureFromSpecificEnergyAndSpecificHeatCapacityTest() {
        assertEqualScientificValue(
            1(Celsius),
            2(Joule per Kilogram) / 2(Joule per Celsius per Kilogram),
        )
        assertEqualScientificValue(
            1(Celsius),
            2(WattHour per Pound) / 2(WattHour per Celsius per Pound),
        )
        assertEqualScientificValue(
            1(Fahrenheit),
            2(WattHour per Pound) / 2(WattHour per Fahrenheit per Pound),
        )
        assertEqualScientificValue(
            1(Celsius),
            2(WattHour per ImperialTon) / 2(WattHour per Celsius per ImperialTon),
        )
        assertEqualScientificValue(
            1(Fahrenheit),
            2(WattHour per UsTon) / 2(WattHour per Fahrenheit per UsTon),
        )
        assertEqualScientificValue(
            1(Kelvin),
            2(Joule per Kilogram).convert(WattHour per Pound) / 2(Joule per Celsius per Kilogram),
        )
    }

    @Test
    fun temperatureFromThermalResistanceAndPowerTest() {
        assertEqualScientificValue(4(Celsius), 2(Celsius per Watt) * 2(Watt))
        assertEqualScientificValue(4(Celsius), 2(Watt) * 2(Celsius per Watt))
        assertEqualScientificValue(4(Celsius), 2(Celsius per Watt.metric) * 2(Watt))
        assertEqualScientificValue(4(Celsius), 2(Watt) * 2(Celsius per Watt.metric))
        assertEqualScientificValue(4(Celsius), 2(Celsius per ErgPerSecond) * 2(ErgPerSecond))
        assertEqualScientificValue(4(Celsius), 2(ErgPerSecond) * 2(Celsius per ErgPerSecond))
        assertEqualScientificValue(4(Celsius), 2(Celsius per Watt.imperial) * 2(Watt))
        assertEqualScientificValue(4(Celsius), 2(Watt) * 2(Celsius per Watt.imperial))
        assertEqualScientificValue(
            4(Celsius),
            2(Celsius per BritishThermalUnitPerSecond) * 2(BritishThermalUnitPerSecond),
        )
        assertEqualScientificValue(
            4(Celsius),
            2(BritishThermalUnitPerSecond) * 2(Celsius per BritishThermalUnitPerSecond),
        )
        assertEqualScientificValue(4(Fahrenheit), 2(Fahrenheit per Watt) * 2(Watt))
        assertEqualScientificValue(4(Fahrenheit), 2(Watt) * 2(Fahrenheit per Watt))
        assertEqualScientificValue(
            4(Fahrenheit),
            2(Fahrenheit per BritishThermalUnitPerSecond) * 2(BritishThermalUnitPerSecond),
        )
        assertEqualScientificValue(
            4(Fahrenheit),
            2(BritishThermalUnitPerSecond) * 2(Fahrenheit per BritishThermalUnitPerSecond),
        )
        assertEqualScientificValue(
            4(Kelvin),
            2(Celsius per Watt).convert(Fahrenheit per Watt) * 2(Watt.metric),
        )
        assertEqualScientificValue(
            4(Kelvin),
            2(Watt.metric) * 2(Celsius per Watt).convert(Fahrenheit per Watt),
        )
    }
}
