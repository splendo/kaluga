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

import com.splendo.kaluga.scientific.assertEqualScientificValue
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
import kotlin.test.Test
import kotlin.test.assertEquals

class EnergyUnitTest {

    @Test
    fun jouleConversionTest() {
        assertScientificConversion(1.0, Joule, 1e+9, Nanojoule)
        assertScientificConversion(1.0, Joule, 1e+6, Microjoule)
        assertScientificConversion(1.0, Joule, 1_000.0, Millijoule)
        assertScientificConversion(1.0, Joule, 100.0, Centijoule)
        assertScientificConversion(1.0, Joule, 10.0, Decijoule)
        assertScientificConversion(1.0, Joule, 0.1, Decajoule)
        assertScientificConversion(1.0, Joule, 0.01, Hectojoule)
        assertScientificConversion(1.0, Joule, 0.001, Kilojoule)
        assertScientificConversion(1.0, Joule, 1e-6, Megajoule)
        assertScientificConversion(1.0, Joule, 1e-9, Gigajoule)

        assertScientificConversion(1.0, Joule, 0.00027778, WattHour, 8)
        assertScientificConversion(1.0, Joule, 1.0e+7, Erg)
        assertScientificConversion(1.0, Joule, 6.2415090744607621E18, Electronvolt, 2)
        assertScientificConversion(1.0, Joule, 0.239006, Calorie, 6)
        assertScientificConversion(1.0, Joule, 0.238846, Calorie.IT, 6)
        assertScientificConversion(1.0, Joule, 23.73, FootPoundal, 2)
        assertScientificConversion(1.0, Joule, 0.74, FootPoundForce, 2)
        assertScientificConversion(1.0, Joule, 8.85, InchPoundForce, 2)
        assertScientificConversion(1.0, Joule, 0.0000004, HorsepowerHour, 7)
        assertScientificConversion(1.0, Joule, 0.00094782, BritishThermalUnit, 8)
        assertScientificConversion(1.0, Joule, 0.00094845, BritishThermalUnit.Thermal, 8)
    }

    @Test
    fun ergConversionTest() {
        assertScientificConversion(1.0, Erg, 1e+9, Nanoerg)
        assertScientificConversion(1.0, Erg, 1e+6, Microerg)
        assertScientificConversion(1.0, Erg, 1_000.0, Millierg)
        assertScientificConversion(1.0, Erg, 100.0, Centierg)
        assertScientificConversion(1.0, Erg, 10.0, Decierg)
        assertScientificConversion(1.0, Erg, 0.1, Decaerg)
        assertScientificConversion(1.0, Erg, 0.01, Hectoerg)
        assertScientificConversion(1.0, Erg, 0.001, Kiloerg)
        assertScientificConversion(1.0, Erg, 1e-6, Megaerg)
        assertScientificConversion(1.0, Erg, 1e-9, Gigaerg)
    }

    @Test
    fun electronvoltConversionTest() {
        assertScientificConversion(1.0, Electronvolt, 1e+9, Nanoelectronvolt)
        assertScientificConversion(1.0, Electronvolt, 1e+6, Microelectronvolt)
        assertScientificConversion(1.0, Electronvolt, 1_000.0, Millielectronvolt)
        assertScientificConversion(1.0, Electronvolt, 100.0, Centielectronvolt)
        assertScientificConversion(1.0, Electronvolt, 10.0, Decielectronvolt)
        assertScientificConversion(1.0, Electronvolt, 0.1, Decaelectronvolt)
        assertScientificConversion(1.0, Electronvolt, 0.01, Hectoelectronvolt, 3)
        assertScientificConversion(1.0, Electronvolt, 0.001, Kiloelectronvolt, 4)
        assertScientificConversion(
            1.0,
            Electronvolt,
            1e-6,
            Megaelectronvolt,
            7,
            bidirectional = false
        )
        assertScientificConversion(
            1.0,
            Megaelectronvolt,
            1e6,
            Electronvolt,
            7,
            bidirectional = false
        )
        assertScientificConversion(
            1.0,
            Electronvolt,
            1e-9,
            Gigaelectronvolt,
            10,
            bidirectional = false
        )
        assertScientificConversion(
            1.0,
            Gigaelectronvolt,
            1e9,
            Electronvolt,
            10,
            bidirectional = false
        )
    }

    @Test
    fun wattHourConversionTest() {
        assertScientificConversion(1.0, WattHour, 1e+9, NanowattHour)
        assertScientificConversion(1.0, WattHour, 1e+6, MicrowattHour)
        assertScientificConversion(1.0, WattHour, 1_000.0, MilliwattHour)
        assertScientificConversion(1.0, WattHour, 100.0, CentiwattHour)
        assertScientificConversion(1.0, WattHour, 10.0, DeciwattHour)
        assertScientificConversion(1.0, WattHour, 0.1, DecawattHour)
        assertScientificConversion(1.0, WattHour, 0.01, HectowattHour)
        assertScientificConversion(1.0, WattHour, 0.001, KilowattHour)
        assertScientificConversion(1.0, WattHour, 1e-6, MegawattHour)
        assertScientificConversion(1.0, WattHour, 1e-9, GigawattHour)
        assertScientificConversion(1.0, WattHour, 1.0, WattHour.metric)
        assertScientificConversion(1.0, WattHour, 1.0, WattHour.imperial)
    }

    @Test
    fun calorieConversionTest() {
        assertScientificConversion(1.0, Calorie, 1_000.0, Millicalorie)
        assertScientificConversion(1.0, Calorie, 0.001, Kilocalorie)
        assertScientificConversion(1.0, Calorie, 1e-6, Megacalorie)

        assertScientificConversion(1.0, Calorie.IT, 1_000.0, Millicalorie.IT)
        assertScientificConversion(1.0, Calorie.IT, 0.001, Kilocalorie.IT)
        assertScientificConversion(1.0, Calorie.IT, 1e-6, Megacalorie.IT)
    }

    @Test
    fun imperialEnergyConversionTest() {
        assertScientificConversion(1.0, FootPoundal, 0.03, FootPoundForce, 2)
        assertScientificConversion(1.0, InchPoundForce, 16.0, InchOunceForce, 2)
        assertScientificConversion(1.0, HorsepowerHour, 1.0, HorsepowerHour)
        assertScientificConversion(1.0, BritishThermalUnit, 1.0, BritishThermalUnit.Thermal, 2)
    }

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
        assertEquals(4(Erg), 2(Abcoulomb) * 2(Abvolt))
        assertEquals(4(Erg), 2(Abvolt) * 2(Abcoulomb))
        assertEquals(4(Joule), 2(Coulomb) * 2(Volt))
        assertEquals(4(Joule), 2(Volt) * 2(Coulomb))
    }

    @Test
    fun energyFromForceAndDistanceTest() {
        assertEquals(4(Joule), 2(Newton) * 2(Meter))
        assertEquals(4(Joule), 2(Meter) * 2(Newton))
        assertEquals(4(FootPoundForce), 2(PoundForce) * 2(Foot))
        assertEquals(4(FootPoundForce), 2(Foot) * 2(PoundForce))
        assertEquals(4(FootPoundForce), 2(PoundForce.ukImperial) * 2(Foot))
        assertEquals(4(FootPoundForce), 2(Foot) * 2(PoundForce.ukImperial))
        assertEquals(4(FootPoundForce), 2(PoundForce.usCustomary) * 2(Foot))
        assertEquals(4(FootPoundForce), 2(Foot) * 2(PoundForce.usCustomary))
        assertEquals(4(Joule), 2(Newton) * 2(Meter).convert(Foot))
        assertEquals(4(Joule), 2(Meter).convert(Foot) * 2(Newton))
    }

    @Test
    fun energyFromHeatCapacityAndTemperatureTest() {
        assertEqualScientificValue(4(Joule), 2(Joule per Celsius) * 2(Celsius))
        assertEqualScientificValue(4(Joule), 2(Celsius) * 2(Joule per Celsius))
        assertEqualScientificValue(4(Calorie), 2(Calorie per Celsius) * 2(Celsius))
        assertEqualScientificValue(4(Calorie), 2(Celsius) * 2(Calorie per Celsius))
        assertEqualScientificValue(
            4(ImperialMetricAndImperialEnergyWrapper(Calorie)),
            2(Calorie per Fahrenheit) * 2(Fahrenheit)
        )
        assertEqualScientificValue(
            4(ImperialMetricAndImperialEnergyWrapper(Calorie)),
            2(Fahrenheit) * 2(Calorie per Fahrenheit)
        )
        assertEqualScientificValue(4(FootPoundForce), 2(FootPoundForce per Celsius) * 2(Celsius))
        assertEqualScientificValue(4(FootPoundForce), 2(Celsius) * 2(FootPoundForce per Celsius))
        assertEqualScientificValue(
            4(FootPoundForce),
            2(FootPoundForce per Fahrenheit) * 2(Fahrenheit)
        )
        assertEqualScientificValue(
            4(FootPoundForce),
            2(Fahrenheit) * 2(FootPoundForce per Fahrenheit)
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
            8
        )
        assertEqualScientificValue(
            4(Joule).convert(FootPoundForce),
            2(Kilogram).convert(Pound) * 2(Gray),
            8
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
            8
        )
        assertEqualScientificValue(
            4(Joule).convert(FootPoundForce),
            2(Kilogram).convert(Pound) * 2(Sievert),
            8
        )
    }

    @Test
    fun energyFromFluxAndCurrentTest() {
        assertEquals(4(Erg), 2(Maxwell) * 2(Abampere))
        assertEquals(4(Erg), 2(Abampere) * 2(Maxwell))
        assertEquals(4(Erg), 2(Maxwell) * 2(Biot))
        assertEquals(4(Erg), 2(Biot) * 2(Maxwell))
        assertEquals(4(Joule), 2(Weber) * 2(Ampere))
        assertEquals(4(Joule), 2(Ampere) * 2(Weber))
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
        assertEquals(4(Joule), 2(Watt) * 2(Second))
        assertEquals(4(Joule), 2(Second) * 2(Watt))

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

        assertEqualScientificValue(4(WattHour.metric), 2(Watt.metric) * 2(Hour))
        assertEqualScientificValue(4(WattHour.metric), 2(Hour) * 2(Watt.metric))
        assertEqualScientificValue(4(NanowattHour.metric), 2(Nanowatt.metric) * 2(Hour))
        assertEqualScientificValue(4(NanowattHour.metric), 2(Hour) * 2(Nanowatt.metric))
        assertEqualScientificValue(4(MicrowattHour.metric), 2(Microwatt.metric) * 2(Hour))
        assertEqualScientificValue(4(MicrowattHour.metric), 2(Hour) * 2(Microwatt.metric))
        assertEqualScientificValue(4(MilliwattHour.metric), 2(Milliwatt.metric) * 2(Hour))
        assertEqualScientificValue(4(MilliwattHour.metric), 2(Hour) * 2(Milliwatt.metric))
        assertEqualScientificValue(4(CentiwattHour.metric), 2(Centiwatt.metric) * 2(Hour))
        assertEqualScientificValue(4(CentiwattHour.metric), 2(Hour) * 2(Centiwatt.metric))
        assertEqualScientificValue(4(DeciwattHour.metric), 2(Deciwatt.metric) * 2(Hour))
        assertEqualScientificValue(4(DeciwattHour.metric), 2(Hour) * 2(Deciwatt.metric))
        assertEqualScientificValue(4(DecawattHour.metric), 2(Decawatt.metric) * 2(Hour))
        assertEqualScientificValue(4(DecawattHour.metric), 2(Hour) * 2(Decawatt.metric))
        assertEqualScientificValue(4(HectowattHour.metric), 2(Hectowatt.metric) * 2(Hour))
        assertEqualScientificValue(4(HectowattHour.metric), 2(Hour) * 2(Hectowatt.metric))
        assertEqualScientificValue(4(KilowattHour.metric), 2(Kilowatt.metric) * 2(Hour))
        assertEqualScientificValue(4(KilowattHour.metric), 2(Hour) * 2(Kilowatt.metric))
        assertEqualScientificValue(4(MegawattHour.metric), 2(Megawatt.metric) * 2(Hour))
        assertEqualScientificValue(4(MegawattHour.metric), 2(Hour) * 2(Megawatt.metric))
        assertEqualScientificValue(4(GigawattHour.metric), 2(Gigawatt.metric) * 2(Hour))
        assertEqualScientificValue(4(GigawattHour.metric), 2(Hour) * 2(Gigawatt.metric))

        assertEquals(4(Joule).convert(FootPoundForce), 2(Watt.imperial) * 2(Second))
        assertEquals(4(Joule).convert(FootPoundForce), 2(Second) * 2(Watt.imperial))
        assertEqualScientificValue(4(WattHour.imperial), 2(Watt.imperial) * 2(Hour))
        assertEqualScientificValue(4(WattHour.imperial), 2(Hour) * 2(Watt.imperial))
        assertEqualScientificValue(4(NanowattHour.imperial), 2(Nanowatt.imperial) * 2(Hour))
        assertEqualScientificValue(4(NanowattHour.imperial), 2(Hour) * 2(Nanowatt.imperial))
        assertEqualScientificValue(4(MicrowattHour.imperial), 2(Microwatt.imperial) * 2(Hour))
        assertEqualScientificValue(4(MicrowattHour.imperial), 2(Hour) * 2(Microwatt.imperial))
        assertEqualScientificValue(4(MilliwattHour.imperial), 2(Milliwatt.imperial) * 2(Hour))
        assertEqualScientificValue(4(MilliwattHour.imperial), 2(Hour) * 2(Milliwatt.imperial))
        assertEqualScientificValue(4(CentiwattHour.imperial), 2(Centiwatt.imperial) * 2(Hour))
        assertEqualScientificValue(4(CentiwattHour.imperial), 2(Hour) * 2(Centiwatt.imperial))
        assertEqualScientificValue(4(DeciwattHour.imperial), 2(Deciwatt.imperial) * 2(Hour))
        assertEqualScientificValue(4(DeciwattHour.imperial), 2(Hour) * 2(Deciwatt.imperial))
        assertEqualScientificValue(4(DecawattHour.imperial), 2(Decawatt.imperial) * 2(Hour))
        assertEqualScientificValue(4(DecawattHour.imperial), 2(Hour) * 2(Decawatt.imperial))
        assertEqualScientificValue(4(HectowattHour.imperial), 2(Hectowatt.imperial) * 2(Hour))
        assertEqualScientificValue(4(HectowattHour.imperial), 2(Hour) * 2(Hectowatt.imperial))
        assertEqualScientificValue(4(KilowattHour.imperial), 2(Kilowatt.imperial) * 2(Hour))
        assertEqualScientificValue(4(KilowattHour.imperial), 2(Hour) * 2(Kilowatt.imperial))
        assertEqualScientificValue(4(MegawattHour.imperial), 2(Megawatt.imperial) * 2(Hour))
        assertEqualScientificValue(4(MegawattHour.imperial), 2(Hour) * 2(Megawatt.imperial))
        assertEqualScientificValue(4(GigawattHour.imperial), 2(Gigawatt.imperial) * 2(Hour))
        assertEqualScientificValue(4(GigawattHour.imperial), 2(Hour) * 2(Gigawatt.imperial))

        assertEqualScientificValue(4(Erg), 2(ErgPerSecond) * 2(Second))
        assertEqualScientificValue(4(Erg), 2(Second) * 2(ErgPerSecond))

        assertEqualScientificValue(4(HorsepowerHour), 2(Horsepower) * 2(Hour))
        assertEqualScientificValue(4(HorsepowerHour), 2(Hour) * 2(Horsepower))
        assertEqualScientificValue(4(FootPoundForce), 2(FootPoundForcePerSecond) * 2(Second))
        assertEqualScientificValue(4(FootPoundForce), 2(Second) * 2(FootPoundForcePerSecond))
        assertEqualScientificValue(
            4(BritishThermalUnit),
            2(BritishThermalUnitPerSecond) * 2(Second)
        )
        assertEqualScientificValue(
            4(BritishThermalUnit),
            2(Second) * 2(BritishThermalUnitPerSecond)
        )
        assertEqualScientificValue(
            4(BritishThermalUnit),
            2(BritishThermalUnitPerMinute) * 2(Minute)
        )
        assertEqualScientificValue(
            4(BritishThermalUnit),
            2(Minute) * 2(BritishThermalUnitPerMinute)
        )
        assertEqualScientificValue(4(BritishThermalUnit), 2(BritishThermalUnitPerHour) * 2(Hour))
        assertEqualScientificValue(4(BritishThermalUnit), 2(Hour) * 2(BritishThermalUnitPerHour))
    }

    @Test
    fun energyFromPressureAndVolumeTest() {
        assertEquals(4(Erg), 2(Barye) * 2(CubicCentimeter))
        assertEquals(4(Erg), 2(CubicCentimeter) * 2(Barye))
        assertEquals(4(Erg), 20(Decibarye) * 2(CubicCentimeter))
        assertEquals(4(Erg), 2(CubicCentimeter) * 20(Decibarye))
        assertEquals(4(Joule), 2(Pascal) * 2(CubicMeter))
        assertEquals(4(Joule), 2(CubicMeter) * 2(Pascal))
        assertEquals(4(InchPoundForce), 2(PoundSquareInch) * 2(CubicInch))
        assertEquals(4(InchPoundForce), 2(CubicInch) * 2(PoundSquareInch))
        assertEquals(4(FootPoundForce), 2(PoundSquareFoot) * 2(CubicFoot))
        assertEquals(4(FootPoundForce), 2(CubicFoot) * 2(PoundSquareFoot))
        assertEquals(4(InchOunceForce), 2(OunceSquareInch) * 2(CubicInch))
        assertEquals(4(InchOunceForce), 2(CubicInch) * 2(OunceSquareInch))
        assertEquals(4000(InchPoundForce), 2(KiloPoundSquareInch) * 2(CubicInch))
        assertEquals(4000(InchPoundForce), 2(CubicInch) * 2(KiloPoundSquareInch))
        assertEquals(4000(InchPoundForce), 2(KipSquareInch) * 2(CubicInch))
        assertEquals(4000(InchPoundForce), 2(CubicInch) * 2(KipSquareInch))
        assertEqualScientificValue(
            4(InchPoundForce),
            2(PoundSquareInch).convert(USTonSquareInch) * 2(CubicInch),
            5
        )
        assertEqualScientificValue(
            4(InchPoundForce),
            2(CubicInch) * 2(PoundSquareInch).convert(USTonSquareInch),
            5
        )
        assertEqualScientificValue(
            4(InchPoundForce),
            2(PoundSquareInch).convert(ImperialTonSquareInch) * 2(CubicInch),
            5
        )
        assertEqualScientificValue(
            4(InchPoundForce),
            2(CubicInch) * 2(PoundSquareInch).convert(ImperialTonSquareInch),
            5
        )
        assertEquals(4(FootPoundForce), 2(PoundSquareFoot) * 2(CubicFoot.ukImperial))
        assertEquals(4(FootPoundForce), 2(CubicFoot.ukImperial) * 2(PoundSquareFoot))
        assertEquals(4(FootPoundForce), 2(PoundSquareFoot) * 2(CubicFoot.usCustomary))
        assertEquals(4(FootPoundForce), 2(CubicFoot.usCustomary) * 2(PoundSquareFoot))
        assertEquals(4(FootPoundForce), 2(PoundSquareFoot.ukImperial) * 2(CubicFoot))
        assertEquals(4(FootPoundForce), 2(CubicFoot) * 2(PoundSquareFoot.ukImperial))
        assertEquals(4(FootPoundForce), 2(PoundSquareFoot.ukImperial) * 2(CubicFoot.ukImperial))
        assertEquals(4(FootPoundForce), 2(CubicFoot.ukImperial) * 2(PoundSquareFoot.ukImperial))
        assertEquals(4(FootPoundForce), 2(PoundSquareFoot.usCustomary) * 2(CubicFoot))
        assertEquals(4(FootPoundForce), 2(CubicFoot) * 2(PoundSquareFoot.usCustomary))
        assertEquals(4(FootPoundForce), 2(PoundSquareFoot.usCustomary) * 2(CubicFoot.usCustomary))
        assertEquals(4(FootPoundForce), 2(CubicFoot.usCustomary) * 2(PoundSquareFoot.usCustomary))
        assertEqualScientificValue(4(Joule), 2(Pascal) * 2(CubicMeter).convert(CubicFoot), 5)
        assertEqualScientificValue(4(Joule), 2(CubicMeter).convert(CubicFoot) * 2(Pascal), 5)
    }

    @Test
    fun energyFromSpecificEnergyAndWeightTest() {
        assertEqualScientificValue(4(Joule), 2(Joule per Kilogram) * 2(Kilogram))
        assertEqualScientificValue(4(Joule), 2(Kilogram) * 2(Joule per Kilogram))
        assertEqualScientificValue(4(WattHour.metric), 2(WattHour per Kilogram) * 2(Kilogram))
        assertEqualScientificValue(4(WattHour.metric), 2(Kilogram) * 2(WattHour per Kilogram))
        assertEqualScientificValue(4(WattHour.imperial), 2(WattHour per Pound) * 2(Pound))
        assertEqualScientificValue(4(WattHour.imperial), 2(Pound) * 2(WattHour per Pound))
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(WattHour per Pound) * 2(Pound.ukImperial)
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(Pound.ukImperial) * 2(WattHour per Pound)
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(WattHour per Pound) * 2(Pound.usCustomary)
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(Pound.usCustomary) * 2(WattHour per Pound)
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(WattHour per Pound.ukImperial) * 2(Pound)
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(Pound) * 2(WattHour per Pound.ukImperial)
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(WattHour per Pound.ukImperial) * 2(Pound.ukImperial)
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(Pound.ukImperial) * 2(WattHour per Pound.ukImperial)
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(WattHour per Pound.usCustomary) * 2(Pound)
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(Pound) * 2(WattHour per Pound.usCustomary)
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(WattHour per Pound.usCustomary) * 2(Pound.usCustomary)
        )
        assertEqualScientificValue(
            4(WattHour.imperial),
            2(Pound.usCustomary) * 2(WattHour per Pound.usCustomary)
        )
        assertEqualScientificValue(4(FootPoundForce), 2(FootPoundForce per Pound) * 2(Pound))
        assertEqualScientificValue(4(FootPoundForce), 2(Pound) * 2(FootPoundForce per Pound))
        assertEqualScientificValue(4(Joule), 2(Joule per Kilogram) * 2(Kilogram).convert(Pound), 5)
        assertEqualScientificValue(4(Joule), 2(Kilogram).convert(Pound) * 2(Joule per Kilogram), 5)
    }

    @Test
    fun energyFromSurfaceTensionAndAreaTest() {
        assertEquals(4(Erg), 2(Dyne per Centimeter) * 2(SquareCentimeter))
        assertEquals(4(Erg), 2(SquareCentimeter) * 2(Dyne per Centimeter))
        assertEquals(4(Joule), 2(Newton per Meter) * 2(SquareMeter))
        assertEquals(4(Joule), 2(SquareMeter) * 2(Newton per Meter))
        assertEquals(4(FootPoundForce), 2(PoundForce per Foot) * 2(SquareFoot))
        assertEquals(4(FootPoundForce), 2(SquareFoot) * 2(PoundForce per Foot))
        assertEquals(4(FootPoundForce), 2(PoundForce.ukImperial per Foot) * 2(SquareFoot))
        assertEquals(4(FootPoundForce), 2(SquareFoot) * 2(PoundForce.ukImperial per Foot))
        assertEquals(4(FootPoundForce), 2(PoundForce.usCustomary per Foot) * 2(SquareFoot))
        assertEquals(4(FootPoundForce), 2(SquareFoot) * 2(PoundForce.usCustomary per Foot))
        assertEquals(4(InchPoundForce), 2(PoundForce per Inch) * 2(SquareInch))
        assertEquals(4(InchPoundForce), 2(SquareInch) * 2(PoundForce per Inch))
        assertEquals(4(InchPoundForce), 2(PoundForce.ukImperial per Inch) * 2(SquareInch))
        assertEquals(4(InchPoundForce), 2(SquareInch) * 2(PoundForce.ukImperial per Inch))
        assertEquals(4(InchPoundForce), 2(PoundForce.usCustomary per Inch) * 2(SquareInch))
        assertEquals(4(InchPoundForce), 2(SquareInch) * 2(PoundForce.usCustomary per Inch))
        assertEquals(4(Joule), 2(Newton per Meter) * 2(SquareMeter).convert(SquareFoot))
        assertEquals(4(Joule), 2(SquareMeter).convert(SquareFoot) * 2(Newton per Meter))
    }
}
