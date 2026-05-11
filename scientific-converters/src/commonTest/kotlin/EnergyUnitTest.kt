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
import com.splendo.kaluga.scientific.converter.action.div
import com.splendo.kaluga.scientific.converter.amountOfSubstance.times
import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.electricCharge.times
import com.splendo.kaluga.scientific.converter.electricCurrent.times
import com.splendo.kaluga.scientific.converter.force.times
import com.splendo.kaluga.scientific.converter.heatCapacity.times
import com.splendo.kaluga.scientific.converter.ionizingRadiationAbsorbedDose.times
import com.splendo.kaluga.scientific.converter.ionizingRadiationEquivalentDose.times
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.converter.magneticFlux.times
import com.splendo.kaluga.scientific.converter.molarEnergy.times
import com.splendo.kaluga.scientific.converter.power.times
import com.splendo.kaluga.scientific.converter.pressure.times
import com.splendo.kaluga.scientific.converter.specificEnergy.times
import com.splendo.kaluga.scientific.converter.surfaceTension.times
import com.splendo.kaluga.scientific.converter.temperature.times
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.converter.voltage.times
import com.splendo.kaluga.scientific.converter.volume.times
import com.splendo.kaluga.scientific.converter.weight.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Abampere
import com.splendo.kaluga.scientific.unit.Abcoulomb
import com.splendo.kaluga.scientific.unit.Abvolt
import com.splendo.kaluga.scientific.unit.Action
import com.splendo.kaluga.scientific.unit.Ampere
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.Biot
import com.splendo.kaluga.scientific.unit.BritishThermalUnit
import com.splendo.kaluga.scientific.unit.Calorie
import com.splendo.kaluga.scientific.unit.Celsius
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Centiwatt
import com.splendo.kaluga.scientific.unit.CentiwattHour
import com.splendo.kaluga.scientific.unit.Coulomb
import com.splendo.kaluga.scientific.unit.CubicCentimeter
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicInch
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Decawatt
import com.splendo.kaluga.scientific.unit.DecawattHour
import com.splendo.kaluga.scientific.unit.Decibarye
import com.splendo.kaluga.scientific.unit.Decirad
import com.splendo.kaluga.scientific.unit.DeciroentgenEquivalentMan
import com.splendo.kaluga.scientific.unit.Deciwatt
import com.splendo.kaluga.scientific.unit.DeciwattHour
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.Fahrenheit
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.Gigawatt
import com.splendo.kaluga.scientific.unit.GigawattHour
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.Gray
import com.splendo.kaluga.scientific.unit.HeatCapacity
import com.splendo.kaluga.scientific.unit.Hectowatt
import com.splendo.kaluga.scientific.unit.HectowattHour
import com.splendo.kaluga.scientific.unit.Horsepower
import com.splendo.kaluga.scientific.unit.HorsepowerHour
import com.splendo.kaluga.scientific.unit.Hour
import com.splendo.kaluga.scientific.unit.ImperialMetricAndImperialEnergyWrapper
import com.splendo.kaluga.scientific.unit.ImperialTonSquareInch
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.InchOunceForce
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.KiloPoundSquareInch
import com.splendo.kaluga.scientific.unit.Kilocalorie
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Kilowatt
import com.splendo.kaluga.scientific.unit.KilowattHour
import com.splendo.kaluga.scientific.unit.KipSquareInch
import com.splendo.kaluga.scientific.unit.Maxwell
import com.splendo.kaluga.scientific.unit.Megacalorie
import com.splendo.kaluga.scientific.unit.Megawatt
import com.splendo.kaluga.scientific.unit.MegawattHour
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Microwatt
import com.splendo.kaluga.scientific.unit.MicrowattHour
import com.splendo.kaluga.scientific.unit.Millicalorie
import com.splendo.kaluga.scientific.unit.Milliwatt
import com.splendo.kaluga.scientific.unit.MilliwattHour
import com.splendo.kaluga.scientific.unit.Minute
import com.splendo.kaluga.scientific.unit.MolarEnergy
import com.splendo.kaluga.scientific.unit.Mole
import com.splendo.kaluga.scientific.unit.Nanowatt
import com.splendo.kaluga.scientific.unit.NanowattHour
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.OunceSquareInch
import com.splendo.kaluga.scientific.unit.Pascal
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.PoundSquareFoot
import com.splendo.kaluga.scientific.unit.PoundSquareInch
import com.splendo.kaluga.scientific.unit.Rad
import com.splendo.kaluga.scientific.unit.RoentgenEquivalentMan
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Sievert
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareInch
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.USTonSquareInch
import com.splendo.kaluga.scientific.unit.Volt
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.Weber
import com.splendo.kaluga.scientific.unit.imperial
import com.splendo.kaluga.scientific.unit.metric
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test
import com.splendo.kaluga.test.base.IgnoreJs

@IgnoreJs
class EnergyUnitTest {

    @Test
    fun energyFromAmountOfSubstanceTimesEnergyTest() {
        assertEqualScientificValue(4(WattHour), 2(Mole) * 2(WattHour per Mole))
        assertEqualScientificValue(4(WattHour), 2(WattHour per Mole) * 2(Mole))
        assertEqualScientificValue(4(Joule), 2(Mole) * 2(Joule per Mole))
        assertEqualScientificValue(4(Joule), 2(Joule per Mole) * 2(Mole))
        assertEqualScientificValue(4(WattHour.imperial), 2(Mole) * 2(WattHour.imperial per Mole))
        assertEqualScientificValue(4(WattHour.imperial), 2(WattHour.imperial per Mole) * 2(Mole))
        assertEqualScientificValue(4(Joule), 2(Mole) * 2((Joule per Mole) as MolarEnergy))
        assertEqualScientificValue(4(Joule), 2((Joule per Mole) as MolarEnergy) * 2(Mole))
    }

    @Test
    fun energyFromActionAndTimeTest() {
        assertEqualScientificValue(1(Joule), 2(Joule x Second) / 2(Second))
        assertEqualScientificValue(1(WattHour), 2(WattHour x Second) / 2(Second))
        assertEqualScientificValue(1(FootPoundForce), 2(FootPoundForce x Second) / 2(Second))
        assertEqualScientificValue(1(Joule), 2((Joule x Second) as Action) / 2(Second))
    }

    @Test
    fun energyFromChargeAndVoltageTest() {
        assertEqualScientificValue(4(Erg), 2(Abcoulomb) * 2(Abvolt))
        assertEqualScientificValue(4(Erg), 2(Abvolt) * 2(Abcoulomb))
        assertEqualScientificValue(4(Joule), 2(Coulomb) * 2(Volt))
        assertEqualScientificValue(4(Joule), 2(Volt) * 2(Coulomb))
    }

    @Test
    fun energyFromForceAndDistanceTest() {
        assertEqualScientificValue(4(Joule), 2(Newton) * 2(Meter))
        assertEqualScientificValue(4(Joule), 2(Meter) * 2(Newton))
        assertEqualScientificValue(4(FootPoundForce), 2(PoundForce) * 2(Foot))
        assertEqualScientificValue(4(FootPoundForce), 2(Foot) * 2(PoundForce))
        assertEqualScientificValue(4(FootPoundForce), 2(PoundForce.ukImperial) * 2(Foot))
        assertEqualScientificValue(4(FootPoundForce), 2(Foot) * 2(PoundForce.ukImperial))
        assertEqualScientificValue(4(FootPoundForce), 2(PoundForce.usCustomary) * 2(Foot))
        assertEqualScientificValue(4(FootPoundForce), 2(Foot) * 2(PoundForce.usCustomary))
        assertEqualScientificValue(4(Joule), 2(Newton) * 2(Meter).convert(Foot), round = 32)
        assertEqualScientificValue(4(Joule), 2(Meter).convert(Foot) * 2(Newton), round = 32)
    }

    @Test
    fun energyFromHeatCapacityAndTemperatureTest() {
        assertEqualScientificValue(4(Joule), 2(Joule per Celsius) * 2(Celsius))
        assertEqualScientificValue(4(Joule), 2(Celsius) * 2(Joule per Celsius))
        assertEqualScientificValue(4(Calorie), 2(Calorie per Celsius) * 2(Celsius))
        assertEqualScientificValue(4(Calorie), 2(Celsius) * 2(Calorie per Celsius))
        assertEqualScientificValue(
            4(ImperialMetricAndImperialEnergyWrapper(Calorie)),
            2(Calorie per Fahrenheit) * 2(Fahrenheit),
            round = 32,
        )
        assertEqualScientificValue(
            4(ImperialMetricAndImperialEnergyWrapper(Calorie)),
            2(Fahrenheit) * 2(Calorie per Fahrenheit),
            round = 32,
        )
        assertEqualScientificValue(4(FootPoundForce), 2(FootPoundForce per Celsius) * 2(Celsius))
        assertEqualScientificValue(4(FootPoundForce), 2(Celsius) * 2(FootPoundForce per Celsius))
        assertEqualScientificValue(
            4(FootPoundForce),
            2(FootPoundForce per Fahrenheit) * 2(Fahrenheit),
            round = 32,
        )
        assertEqualScientificValue(
            4(FootPoundForce),
            2(Fahrenheit) * 2(FootPoundForce per Fahrenheit),
            round = 32,
        )
        assertEqualScientificValue(4(Joule), 2((Joule per Celsius) as HeatCapacity) * 2(Celsius))
        assertEqualScientificValue(4(Joule), 2(Celsius) * 2((Joule per Celsius) as HeatCapacity))
    }

    @Test
    fun energyFromAbsorbedDoseAndWeightTest() {
        assertEqualScientificValue(4(Joule), 2(Gray) * 2(Kilogram))
        assertEqualScientificValue(4(Joule), 2(Kilogram) * 2(Gray))
        assertEqualScientificValue(400(Erg), 2(Rad) * 2(Gram))
        assertEqualScientificValue(400(Erg), 2(Gram) * 2(Rad))
        assertEqualScientificValue(400(Erg), 20(Decirad) * 2(Gram))
        assertEqualScientificValue(400(Erg), 2(Gram) * 20(Decirad))
        assertEqualScientificValue(
            4(Joule).convert(FootPoundForce),
            2(Gray) * 2(Kilogram).convert(Pound),
            round = 30,
        )
        assertEqualScientificValue(
            4(Joule).convert(FootPoundForce),
            2(Kilogram).convert(Pound) * 2(Gray),
            round = 30,
        )
    }

    @Test
    fun energyFromEquivalentDoseAndWeightTest() {
        assertEqualScientificValue(4(Joule), 2(Sievert) * 2(Kilogram))
        assertEqualScientificValue(4(Joule), 2(Kilogram) * 2(Sievert))
        assertEqualScientificValue(400(Erg), 2(RoentgenEquivalentMan) * 2(Gram))
        assertEqualScientificValue(400(Erg), 2(Gram) * 2(RoentgenEquivalentMan))
        assertEqualScientificValue(400(Erg), 20(DeciroentgenEquivalentMan) * 2(Gram))
        assertEqualScientificValue(400(Erg), 2(Gram) * 20(DeciroentgenEquivalentMan))
        assertEqualScientificValue(
            4(Joule).convert(FootPoundForce),
            2(Sievert) * 2(Kilogram).convert(Pound),
            round = 30,
        )
        assertEqualScientificValue(
            4(Joule).convert(FootPoundForce),
            2(Kilogram).convert(Pound) * 2(Sievert),
            round = 30,
        )
    }

    @Test
    fun energyFromFluxAndCurrentTest() {
        assertEqualScientificValue(4(Erg), 2(Maxwell) * 2(Abampere))
        assertEqualScientificValue(4(Erg), 2(Abampere) * 2(Maxwell))
        assertEqualScientificValue(4(Erg), 2(Maxwell) * 2(Biot))
        assertEqualScientificValue(4(Erg), 2(Biot) * 2(Maxwell))
        assertEqualScientificValue(4(Joule), 2(Weber) * 2(Ampere))
        assertEqualScientificValue(4(Joule), 2(Ampere) * 2(Weber))
    }

    @Test
    fun energyFromMolarEnergyAndAmountOfSubstanceTest() {
        assertEqualScientificValue(4(Joule), 2(Joule per Mole) * 2(Mole))
        assertEqualScientificValue(4(Joule), 2(Mole) * 2(Joule per Mole))
        assertEqualScientificValue(4(Calorie), 2(Calorie per Mole) * 2(Mole))
        assertEqualScientificValue(4(Calorie), 2(Mole) * 2(Calorie per Mole))
        assertEqualScientificValue(4(FootPoundForce), 2(FootPoundForce per Mole) * 2(Mole))
        assertEqualScientificValue(4(FootPoundForce), 2(Mole) * 2(FootPoundForce per Mole))
        assertEqualScientificValue(4(Joule), 2((Joule per Mole) as MolarEnergy) * 2(Mole))
        assertEqualScientificValue(4(Joule), 2(Mole) * 2((Joule per Mole) as MolarEnergy))
    }

    @Test
    fun energyFromPowerAndTimeTest() {
        assertEqualScientificValue(4(Joule), 2(Watt) * 2(Second))
        assertEqualScientificValue(4(Joule), 2(Second) * 2(Watt))

        assertEqualScientificValue(4(WattHour), 2(Watt) * 2(Hour))
        assertEqualScientificValue(4(WattHour), 2(Hour) * 2(Watt))
        assertEqualScientificValue(4(NanowattHour), 2(Nanowatt) * 2(Hour))
        assertEqualScientificValue(4(NanowattHour), 2(Hour) * 2(Nanowatt))
        assertEqualScientificValue(4(MicrowattHour), 2(Microwatt) * 2(Hour))
        assertEqualScientificValue(4(MicrowattHour), 2(Hour) * 2(Microwatt))
        assertEqualScientificValue(4(MilliwattHour), 2(Milliwatt) * 2(Hour))
        assertEqualScientificValue(4(MilliwattHour), 2(Hour) * 2(Milliwatt))
        assertEqualScientificValue(4(CentiwattHour), 2(Centiwatt) * 2(Hour))
        assertEqualScientificValue(4(CentiwattHour), 2(Hour) * 2(Centiwatt))
        assertEqualScientificValue(4(DeciwattHour), 2(Deciwatt) * 2(Hour))
        assertEqualScientificValue(4(DeciwattHour), 2(Hour) * 2(Deciwatt))
        assertEqualScientificValue(4(DecawattHour), 2(Decawatt) * 2(Hour))
        assertEqualScientificValue(4(DecawattHour), 2(Hour) * 2(Decawatt))
        assertEqualScientificValue(4(HectowattHour), 2(Hectowatt) * 2(Hour))
        assertEqualScientificValue(4(HectowattHour), 2(Hour) * 2(Hectowatt))
        assertEqualScientificValue(4(KilowattHour), 2(Kilowatt) * 2(Hour))
        assertEqualScientificValue(4(KilowattHour), 2(Hour) * 2(Kilowatt))
        assertEqualScientificValue(4(MegawattHour), 2(Megawatt) * 2(Hour))
        assertEqualScientificValue(4(MegawattHour), 2(Hour) * 2(Megawatt))
        assertEqualScientificValue(4(GigawattHour), 2(Gigawatt) * 2(Hour))
        assertEqualScientificValue(4(GigawattHour), 2(Hour) * 2(Gigawatt))

        assertEqualScientificValue(4(Calorie), 2(Calorie per Hour) * 2(Hour), round = 32)
        assertEqualScientificValue(4(Calorie), 2(Hour) * 2(Calorie per Hour), round = 32)
        assertEqualScientificValue(4(Calorie.IT), 2(Calorie.IT per Hour) * 2(Hour), round = 32)
        assertEqualScientificValue(4(Calorie.IT), 2(Hour) * 2(Calorie.IT per Hour), round = 32)
        assertEqualScientificValue(4(Millicalorie), 2(Millicalorie per Hour) * 2(Hour), round = 32)
        assertEqualScientificValue(4(Millicalorie), 2(Hour) * 2(Millicalorie per Hour), round = 32)
        assertEqualScientificValue(4(Millicalorie.IT), 2(Millicalorie.IT per Hour) * 2(Hour), round = 32)
        assertEqualScientificValue(4(Millicalorie.IT), 2(Hour) * 2(Millicalorie.IT per Hour), round = 32)
        assertEqualScientificValue(4(Kilocalorie), 2(Kilocalorie per Hour) * 2(Hour), round = 32)
        assertEqualScientificValue(4(Kilocalorie), 2(Hour) * 2(Kilocalorie per Hour), round = 32)
        assertEqualScientificValue(4(Kilocalorie.IT), 2(Kilocalorie.IT per Hour) * 2(Hour), round = 32)
        assertEqualScientificValue(4(Kilocalorie.IT), 2(Hour) * 2(Kilocalorie.IT per Hour), round = 32)
        assertEqualScientificValue(4(Megacalorie), 2(Megacalorie per Hour) * 2(Hour), round = 32)
        assertEqualScientificValue(4(Megacalorie), 2(Hour) * 2(Megacalorie per Hour), round = 32)
        assertEqualScientificValue(4(Megacalorie.IT), 2(Megacalorie.IT per Hour) * 2(Hour), round = 32)
        assertEqualScientificValue(4(Megacalorie.IT), 2(Hour) * 2(Megacalorie.IT per Hour), round = 32)

        assertEqualScientificValue(4(WattHour).convert(Joule), 2(Watt.metric) * 2(Hour))
        assertEqualScientificValue(4(WattHour).convert(Joule), 2(Hour) * 2(Watt.metric))
        assertEqualScientificValue(4(WattHour).convert(FootPoundForce), 2(Watt.imperial) * 2(Hour))
        assertEqualScientificValue(4(WattHour).convert(FootPoundForce), 2(Hour) * 2(Watt.imperial))

        assertEqualScientificValue(4(Erg), 2(Erg per Second) * 2(Second))
        assertEqualScientificValue(4(Erg), 2(Second) * 2(Erg per Second))

        assertEqualScientificValue(4(HorsepowerHour), 2(Horsepower) * 2(Hour))
        assertEqualScientificValue(4(HorsepowerHour), 2(Hour) * 2(Horsepower))
        assertEqualScientificValue(4(FootPoundForce), 2(FootPoundForce per Second) * 2(Second))
        assertEqualScientificValue(4(FootPoundForce), 2(Second) * 2(FootPoundForce per Second))
        assertEqualScientificValue(4(InchPoundForce), 2(InchPoundForce per Second) * 2(Second), round = 32)
        assertEqualScientificValue(4(InchPoundForce), 2(Second) * 2(InchPoundForce per Second), round = 32)
        assertEqualScientificValue(4(InchPoundForce), 2(InchPoundForce per Minute) * 2(Minute), round = 32)
        assertEqualScientificValue(4(InchPoundForce), 2(Minute) * 2(InchPoundForce per Minute), round = 32)
        assertEqualScientificValue(
            4(BritishThermalUnit),
            2(BritishThermalUnit per Second) * 2(Second),
            round = 32,
        )
        assertEqualScientificValue(
            4(BritishThermalUnit),
            2(Second) * 2(BritishThermalUnit per Second),
            round = 32,
        )
        assertEqualScientificValue(
            4(BritishThermalUnit),
            2(BritishThermalUnit per Minute) * 2(Minute),
            round = 32,
        )
        assertEqualScientificValue(
            4(BritishThermalUnit),
            2(Minute) * 2(BritishThermalUnit per Minute),
            round = 32,
        )
        assertEqualScientificValue(4(BritishThermalUnit), 2(BritishThermalUnit per Hour) * 2(Hour), round = 32)
        assertEqualScientificValue(4(BritishThermalUnit), 2(Hour) * 2(BritishThermalUnit per Hour), round = 32)
    }

    @Test
    fun energyFromPressureAndVolumeTest() {
        assertEqualScientificValue(4(Erg), 2(Barye) * 2(CubicCentimeter))
        assertEqualScientificValue(4(Erg), 2(CubicCentimeter) * 2(Barye))
        assertEqualScientificValue(4(Erg), 20(Decibarye) * 2(CubicCentimeter))
        assertEqualScientificValue(4(Erg), 2(CubicCentimeter) * 20(Decibarye))
        assertEqualScientificValue(4(Joule), 2(Pascal) * 2(CubicMeter))
        assertEqualScientificValue(4(Joule), 2(CubicMeter) * 2(Pascal))
        assertEqualScientificValue(4(InchPoundForce), 2(PoundSquareInch) * 2(CubicInch), round = 32)
        assertEqualScientificValue(4(InchPoundForce), 2(CubicInch) * 2(PoundSquareInch), round = 32)
        assertEqualScientificValue(4(FootPoundForce), 2(PoundSquareFoot) * 2(CubicFoot), round = 32)
        assertEqualScientificValue(4(FootPoundForce), 2(CubicFoot) * 2(PoundSquareFoot), round = 32)
        assertEqualScientificValue(4(InchOunceForce), 2(OunceSquareInch) * 2(CubicInch), round = 32)
        assertEqualScientificValue(4(InchOunceForce), 2(CubicInch) * 2(OunceSquareInch), round = 32)
        assertEqualScientificValue(4000(InchPoundForce), 2(KiloPoundSquareInch) * 2(CubicInch), round = 31)
        assertEqualScientificValue(4000(InchPoundForce), 2(CubicInch) * 2(KiloPoundSquareInch), round = 31)
        assertEqualScientificValue(4000(InchPoundForce), 2(KipSquareInch) * 2(CubicInch), round = 31)
        assertEqualScientificValue(4000(InchPoundForce), 2(CubicInch) * 2(KipSquareInch), round = 31)
        assertEqualScientificValue(
            4(InchPoundForce),
            2(PoundSquareInch).convert(USTonSquareInch) * 2(CubicInch),
            round = 32,
        )
        assertEqualScientificValue(
            4(InchPoundForce),
            2(CubicInch) * 2(PoundSquareInch).convert(USTonSquareInch),
            round = 32,
        )
        assertEqualScientificValue(
            4(InchPoundForce),
            2(PoundSquareInch).convert(ImperialTonSquareInch) * 2(CubicInch),
            round = 32,
        )
        assertEqualScientificValue(
            4(InchPoundForce),
            2(CubicInch) * 2(PoundSquareInch).convert(ImperialTonSquareInch),
            round = 32,
        )
        assertEqualScientificValue(4(FootPoundForce), 2(PoundSquareFoot) * 2(CubicFoot.ukImperial), round = 32)
        assertEqualScientificValue(4(FootPoundForce), 2(CubicFoot.ukImperial) * 2(PoundSquareFoot), round = 32)
        assertEqualScientificValue(4(FootPoundForce), 2(PoundSquareFoot) * 2(CubicFoot.usCustomary), round = 32)
        assertEqualScientificValue(4(FootPoundForce), 2(CubicFoot.usCustomary) * 2(PoundSquareFoot), round = 32)
        assertEqualScientificValue(4(FootPoundForce), 2(PoundSquareFoot.ukImperial) * 2(CubicFoot), round = 32)
        assertEqualScientificValue(4(FootPoundForce), 2(CubicFoot) * 2(PoundSquareFoot.ukImperial), round = 32)
        assertEqualScientificValue(4(FootPoundForce), 2(PoundSquareFoot.ukImperial) * 2(CubicFoot.ukImperial), round = 32)
        assertEqualScientificValue(4(FootPoundForce), 2(CubicFoot.ukImperial) * 2(PoundSquareFoot.ukImperial), round = 32)
        assertEqualScientificValue(4(FootPoundForce), 2(PoundSquareFoot.usCustomary) * 2(CubicFoot), round = 32)
        assertEqualScientificValue(4(FootPoundForce), 2(CubicFoot) * 2(PoundSquareFoot.usCustomary), round = 32)
        assertEqualScientificValue(4(FootPoundForce), 2(PoundSquareFoot.usCustomary) * 2(CubicFoot.usCustomary), round = 32)
        assertEqualScientificValue(4(FootPoundForce), 2(CubicFoot.usCustomary) * 2(PoundSquareFoot.usCustomary), round = 32)
        assertEqualScientificValue(4(Joule), 2(Pascal) * 2(CubicMeter).convert(CubicFoot), round = 32)
        assertEqualScientificValue(4(Joule), 2(CubicMeter).convert(CubicFoot) * 2(Pascal), round = 32)
    }

    @Test
    fun energyFromSpecificEnergyAndWeightTest() {
        assertEqualScientificValue(4(Joule), 2(Joule per Kilogram) * 2(Kilogram))
        assertEqualScientificValue(4(Joule), 2(Kilogram) * 2(Joule per Kilogram))
        assertEqualScientificValue(4(WattHour.metric), 2(WattHour per Kilogram) * 2(Kilogram))
        assertEqualScientificValue(4(WattHour.metric), 2(Kilogram) * 2(WattHour per Kilogram))
        assertEqualScientificValue(4(WattHour.imperial), 2(WattHour per Pound) * 2(Pound), round = 30)
        assertEqualScientificValue(4(WattHour.imperial), 2(Pound) * 2(WattHour per Pound), round = 30)
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(WattHour per Pound) * 2(Pound.ukImperial),
            round = 30,
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(Pound.ukImperial) * 2(WattHour per Pound),
            round = 30,
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(WattHour per Pound) * 2(Pound.usCustomary),
            round = 30,
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(Pound.usCustomary) * 2(WattHour per Pound),
            round = 30,
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(WattHour per Pound.ukImperial) * 2(Pound),
            round = 30,
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(Pound) * 2(WattHour per Pound.ukImperial),
            round = 30,
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(WattHour per Pound.ukImperial) * 2(Pound.ukImperial),
            round = 30,
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(Pound.ukImperial) * 2(WattHour per Pound.ukImperial),
            round = 30,
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(WattHour per Pound.usCustomary) * 2(Pound),
            round = 30,
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(Pound) * 2(WattHour per Pound.usCustomary),
            round = 30,
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(WattHour per Pound.usCustomary) * 2(Pound.usCustomary),
            round = 30,
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(Pound.usCustomary) * 2(WattHour per Pound.usCustomary),
            round = 30,
        )
        assertEqualScientificValue(4(FootPoundForce), 2(FootPoundForce per Pound) * 2(Pound), round = 30)
        assertEqualScientificValue(4(FootPoundForce), 2(Pound) * 2(FootPoundForce per Pound), round = 30)
        assertEqualScientificValue(4(Joule), 2(Joule per Kilogram) * 2(Kilogram).convert(Pound), round = 30)
        assertEqualScientificValue(4(Joule), 2(Kilogram).convert(Pound) * 2(Joule per Kilogram), round = 30)
    }

    @Test
    fun energyFromSurfaceTensionAndAreaTest() {
        assertEqualScientificValue(4(Erg), 2(Dyne per Centimeter) * 2(SquareCentimeter))
        assertEqualScientificValue(4(Erg), 2(SquareCentimeter) * 2(Dyne per Centimeter))
        assertEqualScientificValue(4(Joule), 2(Newton per Meter) * 2(SquareMeter))
        assertEqualScientificValue(4(Joule), 2(SquareMeter) * 2(Newton per Meter))
        assertEqualScientificValue(4(FootPoundForce), 2(PoundForce per Foot) * 2(SquareFoot), round = 32)
        assertEqualScientificValue(4(FootPoundForce), 2(SquareFoot) * 2(PoundForce per Foot), round = 32)
        assertEqualScientificValue(4(FootPoundForce), 2(PoundForce.ukImperial per Foot) * 2(SquareFoot), round = 32)
        assertEqualScientificValue(4(FootPoundForce), 2(SquareFoot) * 2(PoundForce.ukImperial per Foot), round = 32)
        assertEqualScientificValue(4(FootPoundForce), 2(PoundForce.usCustomary per Foot) * 2(SquareFoot), round = 32)
        assertEqualScientificValue(4(FootPoundForce), 2(SquareFoot) * 2(PoundForce.usCustomary per Foot), round = 32)
        assertEqualScientificValue(4(InchPoundForce), 2(PoundForce per Inch) * 2(SquareInch), round = 32)
        assertEqualScientificValue(4(InchPoundForce), 2(SquareInch) * 2(PoundForce per Inch), round = 32)
        assertEqualScientificValue(4(InchPoundForce), 2(PoundForce.ukImperial per Inch) * 2(SquareInch), round = 32)
        assertEqualScientificValue(4(InchPoundForce), 2(SquareInch) * 2(PoundForce.ukImperial per Inch), round = 32)
        assertEqualScientificValue(4(InchPoundForce), 2(PoundForce.usCustomary per Inch) * 2(SquareInch), round = 32)
        assertEqualScientificValue(4(InchPoundForce), 2(SquareInch) * 2(PoundForce.usCustomary per Inch), round = 32)
        assertEqualScientificValue(4(Joule), 2(Newton per Meter) * 2(SquareMeter).convert(SquareFoot), round = 32)
        assertEqualScientificValue(4(Joule), 2(SquareMeter).convert(SquareFoot) * 2(Newton per Meter), round = 32)
    }
}
