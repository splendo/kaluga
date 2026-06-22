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

package com.splendo.kaluga.scientific.converter

import com.splendo.kaluga.base.decimal.times
import com.splendo.kaluga.base.decimal.toDecimal
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.ionizingRadiationAbsorbedDose.asSpecificEnergy
import com.splendo.kaluga.scientific.converter.ionizingRadiationEquivalentDose.asSpecificEnergy
import com.splendo.kaluga.scientific.converter.kinematicViscosity.div
import com.splendo.kaluga.scientific.converter.molality.times
import com.splendo.kaluga.scientific.converter.molarEnergy.div
import com.splendo.kaluga.scientific.converter.molarEnergy.times
import com.splendo.kaluga.scientific.converter.power.div
import com.splendo.kaluga.scientific.converter.specificHeatCapacity.times
import com.splendo.kaluga.scientific.converter.temperature.deltaValue
import com.splendo.kaluga.scientific.converter.temperature.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Celsius
import com.splendo.kaluga.scientific.unit.Decimole
import com.splendo.kaluga.scientific.unit.Decirad
import com.splendo.kaluga.scientific.unit.DeciroentgenEquivalentMan
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.Fahrenheit
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.Gray
import com.splendo.kaluga.scientific.unit.Horsepower
import com.splendo.kaluga.scientific.unit.HorsepowerHour
import com.splendo.kaluga.scientific.unit.ImperialStandardGravityAcceleration
import com.splendo.kaluga.scientific.unit.ImperialTon
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilocalorie
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Minute
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.Rad
import com.splendo.kaluga.scientific.unit.RoentgenEquivalentMan
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Sievert
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.UsTon
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.test.Test

class SpecificEnergyUnitTest {

    @Test
    fun specificEnergyFromEnergyAndWeightTest() {
        assertEqualScientificValue(1(Joule per Kilogram), 2(Joule) / 2(Kilogram))
        assertEqualScientificValue(1(WattHour per Kilogram), 2(WattHour) / 2(Kilogram))
        assertEqualScientificValue(1(WattHour per Pound), 2(WattHour) / 2(Pound), round = 31)
        assertEqualScientificValue(1(WattHour per ImperialTon), 2(WattHour) / 2(ImperialTon), round = 30)
        assertEqualScientificValue(1(WattHour per UsTon), 2(WattHour) / 2(UsTon), round = 31)
        assertEqualScientificValue(1(HorsepowerHour per Pound), 2(HorsepowerHour) / 2(Pound), round = 32)
        assertEqualScientificValue(1(HorsepowerHour per ImperialTon), 2(HorsepowerHour) / 2(ImperialTon), round = 32)
        assertEqualScientificValue(1(HorsepowerHour per UsTon), 2(HorsepowerHour) / 2(UsTon), round = 32)
        assertEqualScientificValue(1(Joule per Kilogram), 2(Joule) / 2(Kilogram).convert(Pound), round = 30)
    }

    @Test
    fun specificEnergyFromAbsorbedDoseTest() {
        assertEqualScientificValue(200(Erg per Gram), 2(Rad).asSpecificEnergy())
        assertEqualScientificValue(200(Erg per Gram), 20(Decirad).asSpecificEnergy())
        assertEqualScientificValue(2(Joule per Kilogram), 2(Gray).asSpecificEnergy())
    }

    @Test
    fun specificEnergyFromEquivalentDoseTest() {
        assertEqualScientificValue(200(Erg per Gram), 2(RoentgenEquivalentMan).asSpecificEnergy())
        assertEqualScientificValue(200(Erg per Gram), 20(DeciroentgenEquivalentMan).asSpecificEnergy())
        assertEqualScientificValue(2(Joule per Kilogram), 2(Sievert).asSpecificEnergy())
    }

    @Test
    fun specificEnergyFromKinematicViscosityAndTimeTest() {
        assertEqualScientificValue(1(Joule per Kilogram), 2(SquareMeter per Second) / 2(Second))
        assertEqualScientificValue(1(FootPoundForce per Pound), (2.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(SquareFoot per Second) / 2(Second), round = 32)
    }

    @Test
    fun specificEnergyFromMolarEnergyAndMolalityTest() {
        assertEqualScientificValue(4(Joule per Kilogram), 2(Joule per Decimole) * 2(Decimole per Kilogram))
        assertEqualScientificValue(4(Joule per Kilogram), 2(Decimole per Kilogram) * 2(Joule per Decimole))
        assertEqualScientificValue(4(WattHour per Kilogram), 2(WattHour per Decimole) * 2(Decimole per Kilogram))
        assertEqualScientificValue(4(WattHour per Kilogram), 2(Decimole per Kilogram) * 2(WattHour per Decimole))
        assertEqualScientificValue(4(WattHour per Pound), 2(WattHour per Decimole) * 2(Decimole per Pound), round = 30)
        assertEqualScientificValue(4(WattHour per Pound), 2(Decimole per Pound) * 2(WattHour per Decimole), round = 30)
        assertEqualScientificValue(4(WattHour per ImperialTon), 2(WattHour per Decimole) * 2(Decimole per ImperialTon), round = 30)
        assertEqualScientificValue(4(WattHour per ImperialTon), 2(Decimole per ImperialTon) * 2(WattHour per Decimole), round = 30)
        assertEqualScientificValue(4(WattHour per UsTon), 2(WattHour per Decimole) * 2(Decimole per UsTon), round = 30)
        assertEqualScientificValue(4(WattHour per UsTon), 2(Decimole per UsTon) * 2(WattHour per Decimole), round = 30)
        assertEqualScientificValue(4(HorsepowerHour per Pound), 2(HorsepowerHour per Decimole) * 2(Decimole per Pound), round = 30)
        assertEqualScientificValue(4(HorsepowerHour per Pound), 2(Decimole per Pound) * 2(HorsepowerHour per Decimole), round = 30)
        assertEqualScientificValue(
            4(HorsepowerHour per ImperialTon),
            2(HorsepowerHour per Decimole) * 2(Decimole per ImperialTon),
            round = 30,
        )
        assertEqualScientificValue(
            4(HorsepowerHour per ImperialTon),
            2(Decimole per ImperialTon) * 2(HorsepowerHour per Decimole),
            round = 30,
        )
        assertEqualScientificValue(4(HorsepowerHour per UsTon), 2(HorsepowerHour per Decimole) * 2(Decimole per UsTon), round = 30)
        assertEqualScientificValue(4(HorsepowerHour per UsTon), 2(Decimole per UsTon) * 2(HorsepowerHour per Decimole), round = 30)
        assertEqualScientificValue(
            4(Joule per Kilogram),
            2(Joule per Decimole) * 2(Decimole per Kilogram).convert(Decimole per Pound),
        )
        assertEqualScientificValue(
            4(Joule per Kilogram),
            2(Decimole per Kilogram).convert(Decimole per Pound) * 2(Joule per Decimole),
        )
    }

    @Test
    fun specificEnergyFromMolarEnergyAndMolarMassTest() {
        assertEqualScientificValue(1(Joule per Kilogram), 2(Joule per Decimole) / 2(Kilogram per Decimole))
        assertEqualScientificValue(1(WattHour per Kilogram), 2(WattHour per Decimole) / 2(Kilogram per Decimole))
        assertEqualScientificValue(1(WattHour per Pound), 2(WattHour per Decimole) / 2(Pound per Decimole), round = 31)
        assertEqualScientificValue(1(WattHour per ImperialTon), 2(WattHour per Decimole) / 2(ImperialTon per Decimole), round = 30)
        assertEqualScientificValue(1(WattHour per UsTon), 2(WattHour per Decimole) / 2(UsTon per Decimole), round = 31)
        assertEqualScientificValue(1(HorsepowerHour per Pound), 2(HorsepowerHour per Decimole) / 2(Pound per Decimole), round = 32)
        assertEqualScientificValue(
            1(HorsepowerHour per ImperialTon),
            2(HorsepowerHour per Decimole) / 2(ImperialTon per Decimole),
            round = 32,
        )
        assertEqualScientificValue(1(HorsepowerHour per UsTon), 2(HorsepowerHour per Decimole) / 2(UsTon per Decimole), round = 32)
        assertEqualScientificValue(
            1(Joule per Kilogram),
            2(Joule per Decimole) / 2(Kilogram per Decimole).convert(Pound per Decimole),
            round = 30,
        )
    }

    @Test
    fun specificEnergyFromPowerAndMassFlowRateTest() {
        assertEqualScientificValue(1(Kilocalorie per Kilogram), 2(Kilocalorie per Minute) / 2(Kilogram per Minute), round = 32)
        assertEqualScientificValue(1(Kilocalorie per Pound), 2(Kilocalorie per Minute) / 2(Pound per Minute), round = 32)
        assertEqualScientificValue(1(Kilocalorie per Pound.ukImperial), 2(Kilocalorie per Minute) / 2(Pound.ukImperial per Minute), round = 32)
        assertEqualScientificValue(1(Kilocalorie per Pound.usCustomary), 2(Kilocalorie per Minute) / 2(Pound.usCustomary per Minute), round = 32)
        assertEqualScientificValue(1(Joule per Kilogram), 2(Joule per Minute) / 2(Kilogram per Minute))
        assertEqualScientificValue(1(FootPoundForce per Pound), 2(FootPoundForce per Minute) / 2(Pound per Minute), round = 32)
        assertEqualScientificValue(1(FootPoundForce per Pound.ukImperial), 2(FootPoundForce per Minute) / 2(Pound.ukImperial per Minute), round = 32)
        assertEqualScientificValue(1(FootPoundForce per Pound.usCustomary), 2(FootPoundForce per Minute) / 2(Pound.usCustomary per Minute), round = 32)
        assertEqualScientificValue(1(Joule per Kilogram), 2(Watt) / 2(Kilogram per Second))
        assertEqualScientificValue(33000(FootPoundForce per Pound), 2(Horsepower) / 2(Pound per Minute), round = 26)
        assertEqualScientificValue(33000(FootPoundForce per Pound.ukImperial), 2(Horsepower) / 2(Pound.ukImperial per Minute), round = 26)
        assertEqualScientificValue(33000(FootPoundForce per Pound.usCustomary), 2(Horsepower) / 2(Pound.usCustomary per Minute), round = 26)
        assertEqualScientificValue(1(Joule per Kilogram), 2(Watt) / 2(Kilogram per Second).convert(Pound per Second), round = 30)
    }

    @Test
    fun specificEnergyFromSpecificHeatCapacityAndTemperatureTest() {
        assertEqualScientificValue(4(Joule per Kilogram), 2((Joule per Celsius) per Kilogram) * 2(Celsius))
        assertEqualScientificValue(4(Joule per Kilogram), 2(Celsius) * 2((Joule per Celsius) per Kilogram))
        assertEqualScientificValue(4(WattHour per Kilogram), 2((WattHour per Celsius) per Kilogram) * 2(Celsius))
        assertEqualScientificValue(4(WattHour per Kilogram), 2(Celsius) * 2((WattHour per Celsius) per Kilogram))
        assertEqualScientificValue(
            4(WattHour per Pound.ukImperial),
            2((WattHour per Celsius) per Pound) * 2(Celsius),
            round = 30,
        )
        assertEqualScientificValue(
            4(WattHour per Pound.ukImperial),
            2(Celsius) * 2((WattHour per Celsius) per Pound),
            round = 30,
        )
        assertEqualScientificValue(
            4(WattHour per Pound.usCustomary),
            2((WattHour per Fahrenheit) per Pound) * 2(Fahrenheit),
            round = 28,
        )
        assertEqualScientificValue(
            4(WattHour per Pound.usCustomary),
            2(Fahrenheit) * 2((WattHour per Fahrenheit) per Pound),
            round = 28,
        )
        assertEqualScientificValue(
            4(HorsepowerHour per Pound.ukImperial),
            2((HorsepowerHour per Celsius) per Pound) * 2(Celsius),
            round = 30,
        )
        assertEqualScientificValue(
            4(HorsepowerHour per Pound.ukImperial),
            2(Celsius) * 2((HorsepowerHour per Celsius) per Pound),
            round = 30,
        )
        assertEqualScientificValue(
            4(HorsepowerHour per Pound.usCustomary),
            2((HorsepowerHour per Fahrenheit) per Pound) * 2(Fahrenheit),
            round = 28,
        )
        assertEqualScientificValue(
            4(HorsepowerHour per Pound.usCustomary),
            2(Fahrenheit) * 2((HorsepowerHour per Fahrenheit) per Pound),
            round = 28,
        )
        assertEqualScientificValue(
            4(Joule per Kilogram),
            2((Joule per Celsius) per Kilogram) * Fahrenheit.deltaValue(2(Celsius)),
            round = 32,
        )
        assertEqualScientificValue(
            4(Joule per Kilogram),
            Fahrenheit.deltaValue(2(Celsius)) * 2((Joule per Celsius) per Kilogram),
            round = 32,
        )
    }
}
