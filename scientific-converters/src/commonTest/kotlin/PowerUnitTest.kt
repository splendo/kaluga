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
import com.splendo.kaluga.scientific.unit.*
import kotlin.test.Test
import kotlin.test.assertEquals

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
        assertEqualScientificValue(1(Calorie per Hour), 2(Calorie) / 2(Hour))
        assertEqualScientificValue(1(Calorie.IT per Hour), 2(Calorie.IT) / 2(Hour))
        assertEqualScientificValue(1(Millicalorie per Hour), 2(Millicalorie) / 2(Hour))
        assertEqualScientificValue(1(Millicalorie.IT per Hour), 2(Millicalorie.IT) / 2(Hour))
        assertEqualScientificValue(1(Kilocalorie per Hour), 2(Kilocalorie) / 2(Hour))
        assertEqualScientificValue(1(Kilocalorie.IT per Hour), 2(Kilocalorie.IT) / 2(Hour))
        assertEqualScientificValue(1(Megacalorie per Hour), 2(Megacalorie) / 2(Hour))
        assertEqualScientificValue(1(Megacalorie.IT per Hour), 2(Megacalorie.IT) / 2(Hour))

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
            8,
        )
        assertEqualScientificValue(
            1(FootPoundal per Minute),
            2(FootPoundal) / 2(Minute),
            8,
        )
        assertEqualScientificValue(1(FootPoundForce per Second), 2(FootPoundForce) / 2(Second), 8)
        assertEqualScientificValue(1(FootPoundForce per Minute), 2(FootPoundForce) / 2(Minute), 8)
        assertEqualScientificValue(1(InchPoundForce per Second), 2(InchPoundForce) / 2(Second), 8)
        assertEqualScientificValue(1(InchPoundForce per Minute), 2(InchPoundForce) / 2(Minute), 8)
        assertEqualScientificValue(
            1(BritishThermalUnit per Second),
            2(BritishThermalUnit) / 2(Second),
            8,
        )
        assertEqualScientificValue(
            1(BritishThermalUnit per Minute),
            2(BritishThermalUnit) / 2(Minute),
            8,
        )
        assertEqualScientificValue(1(BritishThermalUnit per Hour), 2(BritishThermalUnit) / 2(Hour), 8)
        assertEqualScientificValue(1(Horsepower), 2(HorsepowerHour) / 2(Hour))

        assertEqualScientificValue(1(Watt.imperial), 2(WattHour.imperial) / 2(Hour))
        assertEqualScientificValue(1(Watt), 2(Joule).convert(WattHour as Energy) / 2(Second))
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
        assertEqualScientificValue(4(Watt), 2(Newton).convert(PoundForce) * 2(Meter per Second))
        assertEqualScientificValue(4(Watt), 2(Meter per Second) * 2(Newton).convert(PoundForce))
    }

    @Test
    fun powerFromMassFlowRateAndSpecificEnergy() {
        assertEqualScientificValue(4(Joule per Minute), 2(Kilogram per Minute) * 2(Joule per Kilogram))
        assertEqualScientificValue(4(Joule per Minute), 2(Joule per Kilogram) * 2(Kilogram per Minute))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(Pound per Minute) * 2(FootPoundForce per Pound))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(FootPoundForce per Pound) * 2(Pound per Minute))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(Pound per Minute) * 2(FootPoundForce per Pound.ukImperial))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(FootPoundForce per Pound.ukImperial) * 2(Pound per Minute))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(Pound per Minute) * 2(FootPoundForce per Pound.usCustomary))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(FootPoundForce per Pound.usCustomary) * 2(Pound per Minute))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(Pound.ukImperial per Minute) * 2(FootPoundForce per Pound))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(FootPoundForce per Pound) * 2(Pound.ukImperial per Minute))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(Pound.ukImperial per Minute) * 2(FootPoundForce per Pound.ukImperial))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(FootPoundForce per Pound.ukImperial) * 2(Pound.ukImperial per Minute))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(Pound.usCustomary per Minute) * 2(FootPoundForce per Pound))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(FootPoundForce per Pound) * 2(Pound.usCustomary per Minute))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(Pound.usCustomary per Minute) * 2(FootPoundForce per Pound.usCustomary))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(FootPoundForce per Pound.usCustomary) * 2(Pound.usCustomary per Minute))
        assertEqualScientificValue(4(Watt), 2(Kilogram per Second) * 2(Joule per Kilogram).convert(FootPoundForce per Pound), 8)
        assertEqualScientificValue(4(Watt), 2(Joule per Kilogram).convert(FootPoundForce per Pound) * 2(Kilogram per Second), 8)
    }

    @Test
    fun powerFromPressureAndVolumetricFlowTest() {
        assertEqualScientificValue(4(Erg per Minute), 2(Barye) * 2(CubicCentimeter per Minute))
        assertEqualScientificValue(4(Erg per Minute), 2(CubicCentimeter per Minute) * 2(Barye))
        assertEqualScientificValue(4(Erg per Minute), 20(Decibarye) * 2(CubicCentimeter per Minute))
        assertEqualScientificValue(4(Erg per Minute), 2(CubicCentimeter per Minute) * 20(Decibarye))
        assertEqualScientificValue(4(InchPoundForce per Minute), 2(PoundSquareInch) * 2(CubicInch per Minute))
        assertEqualScientificValue(4(InchPoundForce per Minute), 2(CubicInch per Minute) * 2(PoundSquareInch))
        assertEqualScientificValue(4(InchPoundForce per Minute), 2(PoundSquareInch) * 2(CubicInch.ukImperial per Minute))
        assertEqualScientificValue(4(InchPoundForce per Minute), 2(CubicInch.ukImperial per Minute) * 2(PoundSquareInch))
        assertEqualScientificValue(4(InchPoundForce per Minute), 2(PoundSquareInch) * 2(CubicInch.usCustomary per Minute))
        assertEqualScientificValue(4(InchPoundForce per Minute), 2(CubicInch.usCustomary per Minute) * 2(PoundSquareInch))
        assertEqualScientificValue(4(InchOunceForce per Minute), 2(OunceSquareInch) * 2(CubicInch per Minute))
        assertEqualScientificValue(4(InchOunceForce per Minute), 2(CubicInch per Minute) * 2(OunceSquareInch))
        assertEqualScientificValue(4(InchOunceForce per Minute), 2(OunceSquareInch) * 2(CubicInch.ukImperial per Minute))
        assertEqualScientificValue(4(InchOunceForce per Minute), 2(CubicInch.ukImperial per Minute) * 2(OunceSquareInch))
        assertEqualScientificValue(4(InchOunceForce per Minute), 2(OunceSquareInch) * 2(CubicInch.usCustomary per Minute))
        assertEqualScientificValue(4(InchOunceForce per Minute), 2(CubicInch.usCustomary per Minute) * 2(OunceSquareInch))
        assertEqualScientificValue(4000(InchPoundForce per Minute), 2(KiloPoundSquareInch) * 2(CubicInch per Minute))
        assertEqualScientificValue(4000(InchPoundForce per Minute), 2(CubicInch per Minute) * 2(KiloPoundSquareInch))
        assertEqualScientificValue(4000(InchPoundForce per Minute), 2(KiloPoundSquareInch) * 2(CubicInch.ukImperial per Minute))
        assertEqualScientificValue(4000(InchPoundForce per Minute), 2(CubicInch.ukImperial per Minute) * 2(KiloPoundSquareInch))
        assertEqualScientificValue(4000(InchPoundForce per Minute), 2(KiloPoundSquareInch) * 2(CubicInch.usCustomary per Minute))
        assertEqualScientificValue(4000(InchPoundForce per Minute), 2(CubicInch.usCustomary per Minute) * 2(KiloPoundSquareInch))
        assertEqualScientificValue(4000(InchPoundForce per Minute), 2(KipSquareInch) * 2(CubicInch.usCustomary per Minute))
        assertEqualScientificValue(4000(InchPoundForce per Minute), 2(CubicInch.usCustomary per Minute) * 2(KipSquareInch))
        assertEqualScientificValue(8000(InchPoundForce per Minute), 2(USTonSquareInch) * 2(CubicInch.usCustomary per Minute))
        assertEqualScientificValue(8000(InchPoundForce per Minute), 2(CubicInch.usCustomary per Minute) * 2(USTonSquareInch))
        assertEqualScientificValue(8960(InchPoundForce per Minute), 2(ImperialTonSquareInch) * 2(CubicInch.ukImperial per Minute))
        assertEqualScientificValue(8960(InchPoundForce per Minute), 2(CubicInch.ukImperial per Minute) * 2(ImperialTonSquareInch))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(PoundSquareFoot) * 2(CubicFoot per Minute))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(CubicFoot per Minute) * 2(PoundSquareFoot))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(PoundSquareFoot) * 2(CubicFoot.ukImperial per Minute))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(CubicFoot.ukImperial per Minute) * 2(PoundSquareFoot))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(PoundSquareFoot) * 2(CubicFoot.usCustomary per Minute))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(CubicFoot.usCustomary per Minute) * 2(PoundSquareFoot))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(PoundSquareFoot.ukImperial) * 2(CubicFoot per Minute))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(CubicFoot per Minute) * 2(PoundSquareFoot.ukImperial))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(PoundSquareFoot.ukImperial) * 2(CubicFoot.ukImperial per Minute))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(CubicFoot.ukImperial per Minute) * 2(PoundSquareFoot.ukImperial))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(PoundSquareFoot.usCustomary) * 2(CubicFoot per Minute))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(CubicFoot per Minute) * 2(PoundSquareFoot.usCustomary))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(PoundSquareFoot.usCustomary) * 2(CubicFoot.usCustomary per Minute))
        assertEqualScientificValue(4(FootPoundForce per Minute), 2(CubicFoot.usCustomary per Minute) * 2(PoundSquareFoot.usCustomary))
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
        assertEqualScientificValue(1(Watt), Fahrenheit.deltaValue(2(Celsius)) / 2(Celsius per Watt))
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
