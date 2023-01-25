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
import com.splendo.kaluga.scientific.converter.ionizingRadiationAbsorbedDose.asSpecificEnergy
import com.splendo.kaluga.scientific.converter.ionizingRadiationEquivalentDose.asSpecificEnergy
import com.splendo.kaluga.scientific.converter.kinematicViscosity.div
import com.splendo.kaluga.scientific.converter.molality.times
import com.splendo.kaluga.scientific.converter.molarEnergy.div
import com.splendo.kaluga.scientific.converter.molarEnergy.times
import com.splendo.kaluga.scientific.converter.specificHeatCapacity.times
import com.splendo.kaluga.scientific.converter.temperature.deltaValue
import com.splendo.kaluga.scientific.converter.temperature.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class SpecificEnergyUnitTest {

    @Test
    fun specificEnergyConversionTest() {
        assertScientificConversion(1.0, (Joule per Gram), 0.27778, WattHour per Kilogram, 5)
        assertScientificConversion(1.0, (WattHour per Pound), 2240, WattHour per ImperialTon)
        assertScientificConversion(1.0, (WattHour per Pound), 2000, WattHour per UsTon)
        assertScientificConversion(
            1.0,
            (BritishThermalUnit per Pound),
            2240,
            BritishThermalUnit per ImperialTon
        )
        assertScientificConversion(
            1.0,
            (BritishThermalUnit per Pound),
            2000,
            BritishThermalUnit per UsTon
        )
    }

    @Test
    fun specificEnergyFromEnergyAndWeightTest() {
        assertEquals(1(Joule per Kilogram), 2(Joule) / 2(Kilogram))
        assertEquals(1(WattHour per Kilogram), 2(WattHour) / 2(Kilogram))
        assertEquals(1(WattHour per Pound), 2(WattHour) / 2(Pound))
        assertEquals(1(WattHour per ImperialTon), 2(WattHour) / 2(ImperialTon))
        assertEquals(1(WattHour per UsTon), 2(WattHour) / 2(UsTon))
        assertEquals(1(HorsepowerHour per Pound), 2(HorsepowerHour) / 2(Pound))
        assertEquals(1(HorsepowerHour per ImperialTon), 2(HorsepowerHour) / 2(ImperialTon))
        assertEquals(1(HorsepowerHour per UsTon), 2(HorsepowerHour) / 2(UsTon))
        assertEqualScientificValue(1(Joule per Kilogram), 2(Joule) / 2(Kilogram).convert(Pound), 8)
    }

    @Test
    fun specificEnergyFromAbsorbedDoseTest() {
        assertEquals(200(Erg per Gram), 2(Rad).asSpecificEnergy())
        assertEquals(200(Erg per Gram), 20(Decirad).asSpecificEnergy())
        assertEquals(2(Joule per Kilogram), 2(Gray).asSpecificEnergy())
    }

    @Test
    fun specificEnergyFromEquivalentDoseTest() {
        assertEquals(200(Erg per Gram), 2(RoentgenEquivalentMan).asSpecificEnergy())
        assertEquals(200(Erg per Gram), 20(DeciroentgenEquivalentMan).asSpecificEnergy())
        assertEquals(2(Joule per Kilogram), 2(Sievert).asSpecificEnergy())
    }

    @Test
    fun specificEnergyFromKinematicViscosityAndTimeTest() {
        assertEqualScientificValue(1(Joule per Kilogram), 2(SquareMeter per Second) / 2(Second))
        assertEqualScientificValue(1(FootPoundForce per Pound), (2 * ImperialStandardGravityAcceleration.value)(SquareFoot per Second) / 2(Second), 5)
    }

    @Test
    fun specificEnergyFromMolarEnergyAndMolalityTest() {
        assertEquals(4(Joule per Kilogram), 2(Joule per Decimole) * 2(Decimole per Kilogram))
        assertEquals(4(Joule per Kilogram), 2(Decimole per Kilogram) * 2(Joule per Decimole))
        assertEquals(4(WattHour per Kilogram), 2(WattHour per Decimole) * 2(Decimole per Kilogram))
        assertEquals(4(WattHour per Kilogram), 2(Decimole per Kilogram) * 2(WattHour per Decimole))
        assertEquals(4(WattHour per Pound), 2(WattHour per Decimole) * 2(Decimole per Pound))
        assertEquals(4(WattHour per Pound), 2(Decimole per Pound) * 2(WattHour per Decimole))
        assertEquals(4(WattHour per ImperialTon), 2(WattHour per Decimole) * 2(Decimole per ImperialTon))
        assertEquals(4(WattHour per ImperialTon), 2(Decimole per ImperialTon) * 2(WattHour per Decimole))
        assertEquals(4(WattHour per UsTon), 2(WattHour per Decimole) * 2(Decimole per UsTon))
        assertEquals(4(WattHour per UsTon), 2(Decimole per UsTon) * 2(WattHour per Decimole))
        assertEquals(4(HorsepowerHour per Pound), 2(HorsepowerHour per Decimole) * 2(Decimole per Pound))
        assertEquals(4(HorsepowerHour per Pound), 2(Decimole per Pound) * 2(HorsepowerHour per Decimole))
        assertEquals(
            4(HorsepowerHour per ImperialTon),
            2(HorsepowerHour per Decimole) * 2(Decimole per ImperialTon)
        )
        assertEquals(
            4(HorsepowerHour per ImperialTon),
            2(Decimole per ImperialTon) * 2(HorsepowerHour per Decimole)
        )
        assertEquals(4(HorsepowerHour per UsTon), 2(HorsepowerHour per Decimole) * 2(Decimole per UsTon))
        assertEquals(4(HorsepowerHour per UsTon), 2(Decimole per UsTon) * 2(HorsepowerHour per Decimole))
        assertEquals(
            4(Joule per Kilogram),
            2(Joule per Decimole) * 2(Decimole per Kilogram).convert(Decimole per Pound)
        )
        assertEquals(
            4(Joule per Kilogram),
            2(Decimole per Kilogram).convert(Decimole per Pound) * 2(Joule per Decimole)
        )
    }

    @Test
    fun specificEnergyFromMolarEnergyAndMolarMassTest() {
        assertEquals(1(Joule per Kilogram), 2(Joule per Decimole) / 2(Kilogram per Decimole))
        assertEquals(1(WattHour per Kilogram), 2(WattHour per Decimole) / 2(Kilogram per Decimole))
        assertEquals(1(WattHour per Pound), 2(WattHour per Decimole) / 2(Pound per Decimole))
        assertEquals(1(WattHour per ImperialTon), 2(WattHour per Decimole) / 2(ImperialTon per Decimole))
        assertEquals(1(WattHour per UsTon), 2(WattHour per Decimole) / 2(UsTon per Decimole))
        assertEquals(1(HorsepowerHour per Pound), 2(HorsepowerHour per Decimole) / 2(Pound per Decimole))
        assertEquals(
            1(HorsepowerHour per ImperialTon),
            2(HorsepowerHour per Decimole) / 2(ImperialTon per Decimole)
        )
        assertEquals(1(HorsepowerHour per UsTon), 2(HorsepowerHour per Decimole) / 2(UsTon per Decimole))
        assertEqualScientificValue(
            1(Joule per Kilogram),
            2(Joule per Decimole) / 2(Kilogram per Decimole).convert(Pound per Decimole),
            8
        )
    }

    @Test
    fun specificEnergyFromSpecificHeatCapacityAndTemperatureTest() {
        assertEquals(4(Joule per Kilogram), 2((Joule per Celsius) per Kilogram) * 2(Celsius))
        assertEquals(4(Joule per Kilogram), 2(Celsius) * 2((Joule per Celsius) per Kilogram))
        assertEquals(4(WattHour per Kilogram), 2((WattHour per Celsius) per Kilogram) * 2(Celsius))
        assertEquals(4(WattHour per Kilogram), 2(Celsius) * 2((WattHour per Celsius) per Kilogram))
        assertEquals(
            4(WattHour per Pound.ukImperial),
            2((WattHour per Celsius) per Pound) * 2(Celsius)
        )
        assertEquals(
            4(WattHour per Pound.ukImperial),
            2(Celsius) * 2((WattHour per Celsius) per Pound)
        )
        assertEquals(
            4(WattHour per Pound.usCustomary),
            2((WattHour per Fahrenheit) per Pound) * 2(Fahrenheit)
        )
        assertEquals(
            4(WattHour per Pound.usCustomary),
            2(Fahrenheit) * 2((WattHour per Fahrenheit) per Pound)
        )
        assertEquals(
            4(HorsepowerHour per Pound.ukImperial),
            2((HorsepowerHour per Celsius) per Pound) * 2(Celsius)
        )
        assertEquals(
            4(HorsepowerHour per Pound.ukImperial),
            2(Celsius) * 2((HorsepowerHour per Celsius) per Pound)
        )
        assertEquals(
            4(HorsepowerHour per Pound.usCustomary),
            2((HorsepowerHour per Fahrenheit) per Pound) * 2(Fahrenheit)
        )
        assertEquals(
            4(HorsepowerHour per Pound.usCustomary),
            2(Fahrenheit) * 2((HorsepowerHour per Fahrenheit) per Pound)
        )
        assertEquals(
            4(Joule per Kilogram),
            2((Joule per Celsius) per Kilogram) * Fahrenheit.deltaValue(2(Celsius))
        )
        assertEquals(
            4(Joule per Kilogram),
            Fahrenheit.deltaValue(2(Celsius)) * 2((Joule per Celsius) per Kilogram)
        )
    }
}
