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

import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.ionizingRadiationAbsorbedDose.asSpecificEnergy
import com.splendo.kaluga.scientific.converter.ionizingRadiationEquivalentDose.asSpecificEnergy
import com.splendo.kaluga.scientific.converter.molarEnergy.div
import com.splendo.kaluga.scientific.converter.molarEnergy.times
import com.splendo.kaluga.scientific.converter.specificHeatCapacity.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class SpecificEnergyUnitTest {

    @Test
    fun specificEnergyConversionTest() {
        assertEquals(0.28, (Joule per Milligram).convert(1.0, WattHour per Gram, 2))
        assertEquals(860.42, (WattHour per Pound).convert(1.0, Calorie per Pound, 2))
        assertEquals(
            2851.67,
            (HorsepowerHour per UsTon).convert(1.0, BritishThermalUnit.Thermal per ImperialTon, 2)
        )
    }

    @Test
    fun specificEnergyFromEnergyAndWeightTest() {
        assertEquals(1(Joule per Kilogram), 2(Joule) / 2(Kilogram))
        assertEquals(1(WattHour per Pound), 2(WattHour) / 2(Pound))
        assertEquals(1(WattHour per ImperialTon), 2(WattHour) / 2(ImperialTon))
        assertEquals(1(WattHour per UsTon), 2(WattHour) / 2(UsTon))
        assertEquals(1(HorsepowerHour per Pound), 2(HorsepowerHour) / 2(Pound))
        assertEquals(1(HorsepowerHour per ImperialTon), 2(HorsepowerHour) / 2(ImperialTon))
        assertEquals(1(HorsepowerHour per UsTon), 2(HorsepowerHour) / 2(UsTon))
    }

    @Test
    fun specificEnergyFromAbsorbedDoseTest() {
        assertEquals(2(Joule per Kilogram), 2(Gray).asSpecificEnergy())
    }

    @Test
    fun specificEnergyFromEquivalentDoseTest() {
        assertEquals(2(Joule per Kilogram), 2(Sievert).asSpecificEnergy())
    }

    @Test
    fun specificEnergyFromMolarEnergyAndMolalityTest() {
        assertEquals(4(Joule per Kilogram), 2(Joule per Mole) * 2(Mole per Kilogram))
        assertEquals(4(WattHour per Kilogram), 2(WattHour per Mole) * 2(Mole per Kilogram))
        assertEquals(4(WattHour per Pound), 2(WattHour per Mole) * 2(Mole per Pound))
        assertEquals(4(WattHour per ImperialTon), 2(WattHour per Mole) * 2(Mole per ImperialTon))
        assertEquals(4(WattHour per UsTon), 2(WattHour per Mole) * 2(Mole per UsTon))
        assertEquals(4(HorsepowerHour per Pound), 2(HorsepowerHour per Mole) * 2(Mole per Pound))
        assertEquals(4(HorsepowerHour per ImperialTon), 2(HorsepowerHour per Mole) * 2(Mole per ImperialTon))
        assertEquals(4(HorsepowerHour per UsTon), 2(HorsepowerHour per Mole) * 2(Mole per UsTon))
    }

    @Test
    fun specificEnergyFromMolarEnergyAndMolarMassTest() {
        assertEquals(1(Joule per Kilogram), 2(Joule per Mole) / 2(Kilogram per Mole))
        assertEquals(1(WattHour per Kilogram), 2(WattHour per Mole) / 2(Kilogram per Mole))
        assertEquals(1(WattHour per Pound), 2(WattHour per Mole) / 2(Pound per Mole))
        assertEquals(1(WattHour per ImperialTon), 2(WattHour per Mole) / 2(ImperialTon per Mole))
        assertEquals(1(WattHour per UsTon), 2(WattHour per Mole) / 2(UsTon per Mole))
        assertEquals(1(HorsepowerHour per Pound), 2(HorsepowerHour per Mole) / 2(Pound per Mole))
        assertEquals(1(HorsepowerHour per ImperialTon), 2(HorsepowerHour per Mole) / 2(ImperialTon per Mole))
        assertEquals(1(HorsepowerHour per UsTon), 2(HorsepowerHour per Mole) / 2(UsTon per Mole))
    }

    @Test
    fun specificEnergyFromSpecificHeatCapacityAndTemperatureTest() {
        assertEquals(4(Joule per Kilogram), 2((Joule per Celsius) per Kilogram) * 2(Celsius))
        assertEquals(4(WattHour per Kilogram), 2((WattHour per Celsius) per Kilogram) * 2(Celsius))
        // assertEquals(4(WattHour per Pound), 2((WattHour per Celsius) per Pound) * 2(Celsius)) FIXME
        // assertEquals(1(WattHour per ImperialTon), 2((WattHour per Celsius) per ImperialTon) * 2(Celsius)) FIXME find expect value
        // assertEquals(1(WattHour per UsTon),2((WattHour per Fahrenheit) per UsTon) * 2(Fahrenheit)) FIXME find expect value
        // assertEquals(1(HorsepowerHour per Pound), 2((HorsepowerHour per Fahrenheit) per Pound) * 2(Fahrenheit)) FIXME
        // assertEquals(1(HorsepowerHour per ImperialTon), 2((HorsepowerHour per Celsius) per ImperialTon) * 2(Celsius)) FIXME find expect value
        // assertEquals(1(HorsepowerHour per UsTon), 2((HorsepowerHour per Fahrenheit) per UsTon) * 2(Fahrenheit)) FIXME find expect value
    }
}
