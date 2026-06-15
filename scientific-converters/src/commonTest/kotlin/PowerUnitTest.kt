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

import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.electricCurrent.times
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.force.times
import com.splendo.kaluga.scientific.converter.massFlowRate.times
import com.splendo.kaluga.scientific.converter.pressure.times
import com.splendo.kaluga.scientific.converter.specificEnergy.times
import com.splendo.kaluga.scientific.converter.speed.times
import com.splendo.kaluga.scientific.converter.temperature.deltaValue
import com.splendo.kaluga.scientific.converter.temperature.div
import com.splendo.kaluga.scientific.converter.voltage.times
import com.splendo.kaluga.scientific.converter.volumetricFlow.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Abampere
import com.splendo.kaluga.scientific.unit.Abvolt
import com.splendo.kaluga.scientific.unit.Ampere
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.Biot
import com.splendo.kaluga.scientific.unit.BritishThermalUnit
import com.splendo.kaluga.scientific.unit.Calorie
import com.splendo.kaluga.scientific.unit.Celsius
import com.splendo.kaluga.scientific.unit.Centijoule
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Centiwatt
import com.splendo.kaluga.scientific.unit.CentiwattHour
import com.splendo.kaluga.scientific.unit.CubicCentimeter
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicInch
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Decajoule
import com.splendo.kaluga.scientific.unit.Decawatt
import com.splendo.kaluga.scientific.unit.DecawattHour
import com.splendo.kaluga.scientific.unit.Decibarye
import com.splendo.kaluga.scientific.unit.Decidyne
import com.splendo.kaluga.scientific.unit.Decierg
import com.splendo.kaluga.scientific.unit.Decijoule
import com.splendo.kaluga.scientific.unit.Deciwatt
import com.splendo.kaluga.scientific.unit.DeciwattHour
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.Fahrenheit
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.FootPoundal
import com.splendo.kaluga.scientific.unit.Gigajoule
import com.splendo.kaluga.scientific.unit.Gigawatt
import com.splendo.kaluga.scientific.unit.GigawattHour
import com.splendo.kaluga.scientific.unit.Hectojoule
import com.splendo.kaluga.scientific.unit.Hectowatt
import com.splendo.kaluga.scientific.unit.HectowattHour
import com.splendo.kaluga.scientific.unit.Horsepower
import com.splendo.kaluga.scientific.unit.HorsepowerHour
import com.splendo.kaluga.scientific.unit.Hour
import com.splendo.kaluga.scientific.unit.ImperialTonSquareInch
import com.splendo.kaluga.scientific.unit.InchOunceForce
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kelvin
import com.splendo.kaluga.scientific.unit.KiloPoundSquareInch
import com.splendo.kaluga.scientific.unit.Kilocalorie
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Kilojoule
import com.splendo.kaluga.scientific.unit.Kilowatt
import com.splendo.kaluga.scientific.unit.KilowattHour
import com.splendo.kaluga.scientific.unit.KipSquareInch
import com.splendo.kaluga.scientific.unit.Megacalorie
import com.splendo.kaluga.scientific.unit.Megajoule
import com.splendo.kaluga.scientific.unit.Megawatt
import com.splendo.kaluga.scientific.unit.MegawattHour
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Microjoule
import com.splendo.kaluga.scientific.unit.Microwatt
import com.splendo.kaluga.scientific.unit.MicrowattHour
import com.splendo.kaluga.scientific.unit.Millicalorie
import com.splendo.kaluga.scientific.unit.Millijoule
import com.splendo.kaluga.scientific.unit.Milliwatt
import com.splendo.kaluga.scientific.unit.MilliwattHour
import com.splendo.kaluga.scientific.unit.Minute
import com.splendo.kaluga.scientific.unit.Nanojoule
import com.splendo.kaluga.scientific.unit.Nanowatt
import com.splendo.kaluga.scientific.unit.NanowattHour
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.OunceSquareInch
import com.splendo.kaluga.scientific.unit.Pascal
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.PoundSquareFoot
import com.splendo.kaluga.scientific.unit.PoundSquareInch
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.USTonSquareInch
import com.splendo.kaluga.scientific.unit.Volt
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.imperial
import com.splendo.kaluga.scientific.unit.metric
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.test.Test

class PowerUnitTest {

    @Test
    fun powerFromEnergyAndTimeTest() {
        assertEqualScientificValue(1(Watt), 2(WattHour) / 2(Hour))
        assertEqualScientificValue(1(Nanowatt), 2(NanowattHour) / 2(Hour))
        assertEqualScientificValue(1(Microwatt), 2(MicrowattHour) / 2(Hour))
        assertEqualScientificValue(1(Milliwatt), 2(MilliwattHour) / 2(Hour))
        assertEqualScientificValue(1(Centiwatt), 2(CentiwattHour) / 2(Hour))
        assertEqualScientificValue(1(Deciwatt), 2(DeciwattHour) / 2(Hour))
        assertEqualScientificValue(1(Decawatt), 2(DecawattHour) / 2(Hour))
        assertEqualScientificValue(1(Hectowatt), 2(HectowattHour) / 2(Hour))
        assertEqualScientificValue(1(Kilowatt), 2(KilowattHour) / 2(Hour))
        assertEqualScientificValue(1(Megawatt), 2(MegawattHour) / 2(Hour))
        assertEqualScientificValue(1(Gigawatt), 2(GigawattHour) / 2(Hour))
        assertEqualScientificValue(1(Calorie per Hour), 2(Calorie) / 2(Hour), round = 32)
        assertEqualScientificValue(1(Calorie.IT per Hour), 2(Calorie.IT) / 2(Hour), round = 32)
        assertEqualScientificValue(1(Millicalorie per Hour), 2(Millicalorie) / 2(Hour), round = 32)
        assertEqualScientificValue(1(Millicalorie.IT per Hour), 2(Millicalorie.IT) / 2(Hour), round = 32)
        assertEqualScientificValue(1(Kilocalorie per Hour), 2(Kilocalorie) / 2(Hour), round = 32)
        assertEqualScientificValue(1(Kilocalorie.IT per Hour), 2(Kilocalorie.IT) / 2(Hour), round = 32)
        assertEqualScientificValue(1(Megacalorie per Hour), 2(Megacalorie) / 2(Hour), round = 32)
        assertEqualScientificValue(1(Megacalorie.IT per Hour), 2(Megacalorie.IT) / 2(Hour), round = 32)

        assertEqualScientificValue(1(Watt.metric), 2(Joule) / 2(Second))
        assertEqualScientificValue(1(Nanowatt.metric), 2(Nanojoule) / 2(Second))
        assertEqualScientificValue(1(Microwatt.metric), 2(Microjoule) / 2(Second))
        assertEqualScientificValue(1(Milliwatt.metric), 2(Millijoule) / 2(Second))
        assertEqualScientificValue(1(Centiwatt.metric), 2(Centijoule) / 2(Second))
        assertEqualScientificValue(1(Deciwatt.metric), 2(Decijoule) / 2(Second))
        assertEqualScientificValue(1(Decawatt.metric), 2(Decajoule) / 2(Second))
        assertEqualScientificValue(1(Hectowatt.metric), 2(Hectojoule) / 2(Second))
        assertEqualScientificValue(1(Kilowatt.metric), 2(Kilojoule) / 2(Second))
        assertEqualScientificValue(1(Megawatt.metric), 2(Megajoule) / 2(Second))
        assertEqualScientificValue(1(Gigawatt.metric), 2(Gigajoule) / 2(Second))

        assertEqualScientificValue(1(Erg per Second), 2(Erg) / 2(Second))
        assertEqualScientificValue(10(Decierg per Second), 20(Decierg) / 2(Second))

        assertEqualScientificValue(
            1(FootPoundal per Second),
            2(FootPoundal) / 2(Second),
            round = 32,
        )
        assertEqualScientificValue(
            1(FootPoundal per Minute),
            2(FootPoundal) / 2(Minute),
            round = 32,
        )
        assertEqualScientificValue(1(FootPoundForce per Second), 2(FootPoundForce) / 2(Second))
        assertEqualScientificValue(1(FootPoundForce per Minute), 2(FootPoundForce) / 2(Minute), round = 32)
        assertEqualScientificValue(1(InchPoundForce per Second), 2(InchPoundForce) / 2(Second), round = 32)
        assertEqualScientificValue(1(InchPoundForce per Minute), 2(InchPoundForce) / 2(Minute), round = 32)
        assertEqualScientificValue(
            1(BritishThermalUnit per Second),
            2(BritishThermalUnit) / 2(Second),
            round = 32,
        )
        assertEqualScientificValue(
            1(BritishThermalUnit per Minute),
            2(BritishThermalUnit) / 2(Minute),
            round = 32,
        )
        assertEqualScientificValue(1(BritishThermalUnit per Hour), 2(BritishThermalUnit) / 2(Hour), round = 32)
        assertEqualScientificValue(1(Horsepower), 2(HorsepowerHour) / 2(Hour), round = 32)

        assertEqualScientificValue(1(Watt.imperial), 2(WattHour.imperial) / 2(Hour), round = 32)
        assertEqualScientificValue(1(Watt), 2(Joule).convert(WattHour as Energy) / 2(Second), round = 32)
    }

    @Test
    fun powerFromForceAndSpeedTest() {
        assertEqualScientificValue(4(Erg per Second), 2(Dyne) * 2(Centimeter per Second))
        assertEqualScientificValue(4(Erg per Second), 2(Centimeter per Second) * 2(Dyne))
        assertEqualScientificValue(4(Erg per Second), 20(Decidyne) * 2(Centimeter per Second))
        assertEqualScientificValue(4(Erg per Second), 2(Centimeter per Second) * 20(Decidyne))
        assertEqualScientificValue(4(Watt), 2(Newton) * 2(Meter per Second))
        assertEqualScientificValue(4(Watt), 2(Meter per Second) * 2(Newton))
        assertEqualScientificValue(4(FootPoundForce per Second), 2(PoundForce) * 2(Foot per Second))
        assertEqualScientificValue(4(FootPoundForce per Second), 2(Foot per Second) * 2(PoundForce))
        assertEqualScientificValue(4(FootPoundForce per Second), 2(PoundForce.ukImperial) * 2(Foot per Second))
        assertEqualScientificValue(4(FootPoundForce per Second), 2(Foot per Second) * 2(PoundForce.ukImperial))
        assertEqualScientificValue(4(FootPoundForce per Second), 2(PoundForce.usCustomary) * 2(Foot per Second))
        assertEqualScientificValue(4(FootPoundForce per Second), 2(Foot per Second) * 2(PoundForce.usCustomary))
        assertEqualScientificValue(4(Watt), 2(Newton).convert(PoundForce) * 2(Meter per Second), round = 30)
        assertEqualScientificValue(4(Watt), 2(Meter per Second) * 2(Newton).convert(PoundForce), round = 30)
    }

    @Test
    fun powerFromMassFlowRateAndSpecificEnergy() {
        assertEqualScientificValue(4(Joule per Minute), 2(Kilogram per Minute) * 2(Joule per Kilogram), round = 32)
        assertEqualScientificValue(4(Joule per Minute), 2(Joule per Kilogram) * 2(Kilogram per Minute), round = 32)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(Pound per Minute) * 2(FootPoundForce per Pound), round = 30)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(FootPoundForce per Pound) * 2(Pound per Minute), round = 30)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(Pound per Minute) * 2(FootPoundForce per Pound.ukImperial), round = 30)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(FootPoundForce per Pound.ukImperial) * 2(Pound per Minute), round = 30)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(Pound per Minute) * 2(FootPoundForce per Pound.usCustomary), round = 30)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(FootPoundForce per Pound.usCustomary) * 2(Pound per Minute), round = 30)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(Pound.ukImperial per Minute) * 2(FootPoundForce per Pound), round = 30)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(FootPoundForce per Pound) * 2(Pound.ukImperial per Minute), round = 30)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(Pound.ukImperial per Minute) * 2(FootPoundForce per Pound.ukImperial), round = 30)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(FootPoundForce per Pound.ukImperial) * 2(Pound.ukImperial per Minute), round = 30)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(Pound.usCustomary per Minute) * 2(FootPoundForce per Pound), round = 30)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(FootPoundForce per Pound) * 2(Pound.usCustomary per Minute), round = 30)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(Pound.usCustomary per Minute) * 2(FootPoundForce per Pound.usCustomary), round = 30)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(FootPoundForce per Pound.usCustomary) * 2(Pound.usCustomary per Minute), round = 30)
        assertEqualScientificValue(4(Watt), 2(Kilogram per Second) * 2(Joule per Kilogram).convert(FootPoundForce per Pound), round = 30)
        assertEqualScientificValue(4(Watt), 2(Joule per Kilogram).convert(FootPoundForce per Pound) * 2(Kilogram per Second), round = 30)
    }

    @Test
    fun powerFromPressureAndVolumetricFlowTest() {
        assertEqualScientificValue(4(Erg per Minute), 2(Barye) * 2(CubicCentimeter per Minute), round = 32)
        assertEqualScientificValue(4(Erg per Minute), 2(CubicCentimeter per Minute) * 2(Barye), round = 32)
        assertEqualScientificValue(4(Erg per Minute), 20(Decibarye) * 2(CubicCentimeter per Minute), round = 32)
        assertEqualScientificValue(4(Erg per Minute), 2(CubicCentimeter per Minute) * 20(Decibarye), round = 32)
        assertEqualScientificValue(4(InchPoundForce per Minute), 2(PoundSquareInch) * 2(CubicInch per Minute), round = 32)
        assertEqualScientificValue(4(InchPoundForce per Minute), 2(CubicInch per Minute) * 2(PoundSquareInch), round = 32)
        assertEqualScientificValue(4(InchPoundForce per Minute), 2(PoundSquareInch) * 2(CubicInch.ukImperial per Minute), round = 32)
        assertEqualScientificValue(4(InchPoundForce per Minute), 2(CubicInch.ukImperial per Minute) * 2(PoundSquareInch), round = 32)
        assertEqualScientificValue(4(InchPoundForce per Minute), 2(PoundSquareInch) * 2(CubicInch.usCustomary per Minute), round = 32)
        assertEqualScientificValue(4(InchPoundForce per Minute), 2(CubicInch.usCustomary per Minute) * 2(PoundSquareInch), round = 32)
        assertEqualScientificValue(4(InchOunceForce per Minute), 2(OunceSquareInch) * 2(CubicInch per Minute), round = 32)
        assertEqualScientificValue(4(InchOunceForce per Minute), 2(CubicInch per Minute) * 2(OunceSquareInch), round = 32)
        assertEqualScientificValue(4(InchOunceForce per Minute), 2(OunceSquareInch) * 2(CubicInch.ukImperial per Minute), round = 32)
        assertEqualScientificValue(4(InchOunceForce per Minute), 2(CubicInch.ukImperial per Minute) * 2(OunceSquareInch), round = 32)
        assertEqualScientificValue(4(InchOunceForce per Minute), 2(OunceSquareInch) * 2(CubicInch.usCustomary per Minute), round = 32)
        assertEqualScientificValue(4(InchOunceForce per Minute), 2(CubicInch.usCustomary per Minute) * 2(OunceSquareInch), round = 32)
        assertEqualScientificValue(4000(InchPoundForce per Minute), 2(KiloPoundSquareInch) * 2(CubicInch per Minute), round = 29)
        assertEqualScientificValue(4000(InchPoundForce per Minute), 2(CubicInch per Minute) * 2(KiloPoundSquareInch), round = 29)
        assertEqualScientificValue(4000(InchPoundForce per Minute), 2(KiloPoundSquareInch) * 2(CubicInch.ukImperial per Minute), round = 29)
        assertEqualScientificValue(4000(InchPoundForce per Minute), 2(CubicInch.ukImperial per Minute) * 2(KiloPoundSquareInch), round = 29)
        assertEqualScientificValue(4000(InchPoundForce per Minute), 2(KiloPoundSquareInch) * 2(CubicInch.usCustomary per Minute), round = 29)
        assertEqualScientificValue(4000(InchPoundForce per Minute), 2(CubicInch.usCustomary per Minute) * 2(KiloPoundSquareInch), round = 29)
        assertEqualScientificValue(4000(InchPoundForce per Minute), 2(KipSquareInch) * 2(CubicInch.usCustomary per Minute), round = 29)
        assertEqualScientificValue(4000(InchPoundForce per Minute), 2(CubicInch.usCustomary per Minute) * 2(KipSquareInch), round = 29)
        assertEqualScientificValue(8000(InchPoundForce per Minute), 2(USTonSquareInch) * 2(CubicInch.usCustomary per Minute), round = 29)
        assertEqualScientificValue(8000(InchPoundForce per Minute), 2(CubicInch.usCustomary per Minute) * 2(USTonSquareInch), round = 29)
        assertEqualScientificValue(8960(InchPoundForce per Minute), 2(ImperialTonSquareInch) * 2(CubicInch.ukImperial per Minute), round = 29)
        assertEqualScientificValue(8960(InchPoundForce per Minute), 2(CubicInch.ukImperial per Minute) * 2(ImperialTonSquareInch), round = 29)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(PoundSquareFoot) * 2(CubicFoot per Minute), round = 32)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(CubicFoot per Minute) * 2(PoundSquareFoot), round = 32)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(PoundSquareFoot) * 2(CubicFoot.ukImperial per Minute), round = 32)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(CubicFoot.ukImperial per Minute) * 2(PoundSquareFoot), round = 32)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(PoundSquareFoot) * 2(CubicFoot.usCustomary per Minute), round = 32)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(CubicFoot.usCustomary per Minute) * 2(PoundSquareFoot), round = 32)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(PoundSquareFoot.ukImperial) * 2(CubicFoot per Minute), round = 32)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(CubicFoot per Minute) * 2(PoundSquareFoot.ukImperial), round = 32)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(PoundSquareFoot.ukImperial) * 2(CubicFoot.ukImperial per Minute), round = 32)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(CubicFoot.ukImperial per Minute) * 2(PoundSquareFoot.ukImperial), round = 32)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(PoundSquareFoot.usCustomary) * 2(CubicFoot per Minute), round = 32)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(CubicFoot per Minute) * 2(PoundSquareFoot.usCustomary), round = 32)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(PoundSquareFoot.usCustomary) * 2(CubicFoot.usCustomary per Minute), round = 32)
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(CubicFoot.usCustomary per Minute) * 2(PoundSquareFoot.usCustomary), round = 32)
        assertEqualScientificValue(4(Watt), 2(Pascal) * 2(CubicMeter per Second))
        assertEqualScientificValue(4(Watt), 2(CubicMeter per Second) * 2(Pascal))
    }

    @Test
    fun powerFromTemperatureAndThermalResistanceDefaultTest() {
        assertEqualScientificValue(1(Watt), 2(Kelvin) / 2(Kelvin per Watt))
        assertEqualScientificValue(1(Watt), 2(Celsius) / 2(Celsius per Watt))
        assertEqualScientificValue(1(Watt.metric), 2(Celsius) / 2(Celsius per Watt.metric))
        assertEqualScientificValue(1(Watt.imperial), 2(Celsius) / 2(Celsius per Watt.imperial))
        assertEqualScientificValue(1(Watt.imperial), 2(Fahrenheit) / 2(Fahrenheit per Watt))
        assertEqualScientificValue(1(Watt), Fahrenheit.deltaValue(2(Celsius)) / 2(Celsius per Watt), round = 32)
    }

    @Test
    fun powerFromVoltageAndCurrentTest() {
        assertEqualScientificValue(4(Erg per Second), 2(Abvolt) * 2(Abampere))
        assertEqualScientificValue(4(Erg per Second), 2(Abampere) * 2(Abvolt))
        assertEqualScientificValue(4(Erg per Second), 2(Abvolt) * 2(Biot))
        assertEqualScientificValue(4(Erg per Second), 2(Biot) * 2(Abvolt))
        assertEqualScientificValue(4(Watt), 2(Volt) * 2(Ampere))
        assertEqualScientificValue(4(Watt), 2(Ampere) * 2(Volt))
    }
}
