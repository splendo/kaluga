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
import com.splendo.kaluga.scientific.converter.action.div
import com.splendo.kaluga.scientific.converter.amountOfSubstance.times
import com.splendo.kaluga.scientific.converter.electricCharge.times
import com.splendo.kaluga.scientific.converter.force.times
import com.splendo.kaluga.scientific.converter.heatCapacity.times
import com.splendo.kaluga.scientific.converter.ionizingRadiationAbsorbedDose.times
import com.splendo.kaluga.scientific.converter.ionizingRadiationEquivalentDose.times
import com.splendo.kaluga.scientific.converter.magneticFlux.times
import com.splendo.kaluga.scientific.converter.molarEnergy.times
import com.splendo.kaluga.scientific.converter.power.times
import com.splendo.kaluga.scientific.converter.pressure.times
import com.splendo.kaluga.scientific.converter.specificEnergy.times
import com.splendo.kaluga.scientific.converter.surfaceTension.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class EnergyUnitTest {

    @Test
    fun wattHourConversionTest() {
        assertEquals(1e+9, WattHour.convert(1.0, NanowattHour))
        assertEquals(1e+6, WattHour.convert(1.0, MicrowattHour))
        assertEquals(1_000.0, WattHour.convert(1.0, MilliwattHour))
        assertEquals(100.0, WattHour.convert(1.0, CentiwattHour))
        assertEquals(10.0, WattHour.convert(1.0, DeciwattHour))
        assertEquals(0.1, WattHour.convert(1.0, DecawattHour))
        assertEquals(0.01, WattHour.convert(1.0, HectowattHour))
        assertEquals(0.001, WattHour.convert(1.0, KilowattHour))
        assertEquals(1e-6, WattHour.convert(1.0, MegawattHour))
        assertEquals(1e-9, WattHour.convert(1.0, GigawattHour))
    }

    @Test
    fun calorieConversionTest() {
        assertEquals(1_000.0, Calorie.convert(1.0, Millicalorie))
        assertEquals(0.001, Calorie.convert(1.0, Kilocalorie))
        assertEquals(1e-6, Calorie.convert(1.0, Megacalorie))

        assertEquals(1_000.0, Calorie.IT.convert(1.0, Millicalorie.IT))
        assertEquals(0.001, Calorie.IT.convert(1.0, Kilocalorie.IT))
        assertEquals(1e-6, Calorie.IT.convert(1.0, Megacalorie.IT))
    }

    @Test
    fun jouleConversionTest() {
        assertEquals(1e+9, Joule.convert(1.0, Nanojoule))
        assertEquals(1e+6, Joule.convert(1.0, Microjoule))
        assertEquals(1_000.0, Joule.convert(1.0, Millijoule))
        assertEquals(100.0, Joule.convert(1.0, Centijoule))
        assertEquals(10.0, Joule.convert(1.0, Decijoule))
        assertEquals(0.1, Joule.convert(1.0, Decajoule))
        assertEquals(0.01, Joule.convert(1.0, Hectojoule))
        assertEquals(0.001, Joule.convert(1.0, Kilojoule))
        assertEquals(1e-6, Joule.convert(1.0, Megajoule))
        assertEquals(1e-9, Joule.convert(1.0, Gigajoule))
    }

    @Test
    fun ergConversionTest() {
        assertEquals(1e+9, Erg.convert(1.0, Nanoerg))
        assertEquals(1e+6, Erg.convert(1.0, Microerg))
        assertEquals(1_000.0, Erg.convert(1.0, Millierg))
        assertEquals(100.0, Erg.convert(1.0, Centierg))
        assertEquals(10.0, Erg.convert(1.0, Decierg))
        assertEquals(0.1, Erg.convert(1.0, Decaerg))
        assertEquals(0.01, Erg.convert(1.0, Hectoerg))
        assertEquals(0.001, Erg.convert(1.0, Kiloerg))
        assertEquals(1e-6, Erg.convert(1.0, Megaerg))
        assertEquals(1e-9, Erg.convert(1.0, Gigaerg))
    }

    @Test
    fun electronvoltConversionTest() {
        assertEquals(1e+9, Electronvolt.convert(1.0, Nanoelectronvolt))
        assertEquals(1e+6, Electronvolt.convert(1.0, Microelectronvolt))
        assertEquals(1_000.0, Electronvolt.convert(1.0, Millielectronvolt))
        assertEquals(100.0, Electronvolt.convert(1.0, Centielectronvolt))
        assertEquals(10.0, Electronvolt.convert(1.0, Decielectronvolt))
        assertEquals(0.1, Electronvolt.convert(1.0, Decaelectronvolt))
        assertEquals(0.01, Electronvolt.convert(1.0, Hectoelectronvolt))
        assertEquals(0.001, Electronvolt.convert(1.0, Kiloelectronvolt))
        assertEquals(1e-6, Electronvolt.convert(1.0, Megaelectronvolt))
        assertEquals(1e-9, Electronvolt.convert(1.0, Gigaelectronvolt))
    }

    @Test
    fun imperialEnergyConversionTest() {
        assertEquals(0.03, FootPoundal.convert(1.0, FootPoundForce, 2))
        assertEquals(16.0, InchPoundForce.convert(1.0, InchOunceForce, 2))
        assertEquals(1.0, HorsepowerHour.convert(1.0, HorsepowerHour))
        assertEquals(1.0, BritishThermalUnit.convert(1.0, BritishThermalUnit.Thermal, 2))
    }

    @Test
    fun wattHourToOtherEnergyConversionTest() {
        assertEquals(860.42, WattHour.convert(1.0, Calorie, 2))
        assertEquals(859.85, WattHour.convert(1.0, Calorie.IT, 2))
        assertEquals(3600.0, WattHour.convert(1.0, Joule))
        assertEquals(3.6e+10, WattHour.convert(1.0, Erg))
        assertEquals(2.2469432668058747E22, WattHour.convert(1.0, Electronvolt, 2))
        assertEquals(85429.3, WattHour.convert(1.0, FootPoundal, 2))
        assertEquals(2655.22, WattHour.convert(1.0, FootPoundForce, 2))
        assertEquals(31862.68, WattHour.convert(1.0, InchPoundForce, 2))
        assertEquals(0.0013, WattHour.convert(1.0, HorsepowerHour, 4))
        assertEquals(3.4121, WattHour.convert(1.0, BritishThermalUnit, 4))
        assertEquals(3.4144, WattHour.convert(1.0, BritishThermalUnit.Thermal, 4))
    }

    @Test
    fun calorieOtherEnergyConversionTest() {
        assertEquals(4.18, Calorie.convert(1.0, Joule, 2))
        assertEquals(4.184e+7, Calorie.convert(1.0, Erg))
        assertEquals(2.6114473967543833E19, Calorie.convert(1.0, Electronvolt, 2))
        assertEquals(99.29, Calorie.convert(1.0, FootPoundal, 2))
        assertEquals(3.09, Calorie.convert(1.0, FootPoundForce, 2))
        assertEquals(37.03, Calorie.convert(1.0, InchPoundForce, 2))
        assertEquals(0.0000016, Calorie.convert(1.0, HorsepowerHour, 7))
        assertEquals(0.0039657, Calorie.convert(1.0, BritishThermalUnit, 7))
        assertEquals(0.0039683, Calorie.convert(1.0, BritishThermalUnit.Thermal, 7))
    }

    @Test
    fun calorieITOtherEnergyConversionTest() {
        assertEquals(4.19, Calorie.IT.convert(1.0, Joule, 2))
        assertEquals(4.1868e+7, Calorie.IT.convert(1.0, Erg))
        assertEquals(2.613195019295232E19, Calorie.IT.convert(1.0, Electronvolt, 2))
        assertEquals(99.35, Calorie.IT.convert(1.0, FootPoundal, 2))
        assertEquals(3.09, Calorie.IT.convert(1.0, FootPoundForce, 2))
        assertEquals(37.06, Calorie.IT.convert(1.0, InchPoundForce, 2))
        assertEquals(0.0000016, Calorie.IT.convert(1.0, HorsepowerHour, 7))
        assertEquals(0.003968, Calorie.IT.convert(1.0, BritishThermalUnit, 6))
        assertEquals(0.003971, Calorie.IT.convert(1.0, BritishThermalUnit.Thermal, 6))
    }

    @Test
    fun jouleToOtherEnergyConversionTest() {
        assertEquals(1.0e+7, Joule.convert(1.0, Erg))
        assertEquals(6.2415090744607621E18, Joule.convert(1.0, Electronvolt, 2))
        assertEquals(23.73, Joule.convert(1.0, FootPoundal, 2))
        assertEquals(0.74, Joule.convert(1.0, FootPoundForce, 2))
        assertEquals(8.85, Joule.convert(1.0, InchPoundForce, 2))
        assertEquals(0.0000004, Joule.convert(1.0, HorsepowerHour, 7))
        assertEquals(0.00094782, Joule.convert(1.0, BritishThermalUnit, 8))
        assertEquals(0.00094845, Joule.convert(1.0, BritishThermalUnit.Thermal, 8))
    }

    @Test
    fun energyFromAmountOfSubstanceTimesEnergyTest() {
        assertEqualScientificValue(4(Joule), 2(Mole) * 2(Joule per Mole))
    }

    @Test
    fun energyFromActionAndTimeTest() {
        assertEqualScientificValue(1(Joule), 2(Joule x Second) / 2(Second))
        assertEqualScientificValue(1(WattHour), 2(WattHour x Second) / 2(Second))
        assertEqualScientificValue(1(FootPoundForce), 2(FootPoundForce x Second) / 2(Second))
    }

    @Test
    fun energyFromChargeAndVoltageTest() {
        assertEquals(4(Joule), 2(Coulomb) * 2(Volt))
    }

    @Test
    fun energyFromForceAndDistanceTest() {
        assertEquals(4(Joule), 2(Newton) * 2(Meter))
        assertEquals(4(FootPoundForce), 2(PoundForce) * 2(Foot))
    }

    @Test
    fun energyFromHeatCapacityAndTemperatureTest() {
        // TODO check if 2,3 are valid
        assertEqualScientificValue(4(Joule), 2(Joule per Celsius) * 2(Celsius))
        assertEqualScientificValue(4(Calorie), 2(Calorie per Celsius) * 2(Celsius))
        assertEqualScientificValue(4(Calorie), 2(Calorie per Kelvin) * 2(Celsius))
        assertEqualScientificValue(4(FootPoundForce), 2(FootPoundForce per Kelvin) * 2(Kelvin))
        assertEqualScientificValue(4(FootPoundForce), 2(FootPoundForce per Fahrenheit) * 2(Fahrenheit))
    }

    @Test
    fun energyFromAbsorbedDoseAndWeightTest() {
        // FIXME could not find actual valid values, values returned seem wrong
        // assertEqualScientificValue(4(Joule), 2(Rad) * 2(Kilogram))
        // assertEqualScientificValue(4(WattHour), 2(Rad) * 2(Pound))
        // assertEqualScientificValue(4(FootPoundForce), 2(Rad) * 2(Kilogram))
    }

    @Test
    fun energyFromEquivalentDoseAndWeightTest() {
        assertEquals(4(Joule), 2(Sievert) * 2(Kilogram))
        // assertEqualScientificValue(4(WattHour), 2(Sievert) * 2(Pound)) FIXME could not find an expected value
        // assertEqualScientificValue(4(FootPoundForce), 2(Sievert) * 2(Pound)) FIXME could not find an expected value
    }

    @Test
    fun energyFromFluxAndCurrentTest() {
        assertEquals(4(Joule), 2(Weber) * 2(Ampere))
    }

    @Test
    fun energyFromMolarEnergyAndAmountOfSubstanceTest() {
        assertEqualScientificValue(4(Joule), 2(Joule per Mole) * 2(Mole))
        assertEqualScientificValue(4(WattHour), 2(WattHour per Mole) * 2(Mole))
        assertEqualScientificValue(4(FootPoundForce), 2(FootPoundForce per Mole) * 2(Mole))
    }

    @Test
    fun energyFromPowerAndTimeTest() {
        assertEquals(4(Joule), 2(Watt) * 2(Second))
        assertEqualScientificValue(4(WattHour), 2(Watt) * 2(Hour))
        // assertEqualScientificValue(1(WattHour), 2(Watt) * 2(Second)) FIXME yields Joule
        assertEqualScientificValue(4(FootPoundForce), 2(FootPoundForcePerSecond) * 2(Second))
        assertEqualScientificValue(4(FootPoundForce), 2(FootPoundForcePerMinute) * 2(Minute))
    }

    @Test
    fun energyFromPressureAndVolumeTest() {
        assertEquals(400000(Joule), 2(Bar) * 2(CubicMeter))
        assertEquals(4(FootPoundForce), 2(PoundSquareFoot) * 2(CubicFoot))
    }

    @Test
    fun energyFromSpecificEnergyAndWeightTest() {
        assertEqualScientificValue(4(Joule), 2(Joule per Kilogram) * 2(Kilogram))
        // assertEqualScientificValue(4(WattHour), 2(WattHour per Kilogram) * 2(Kilogram)) FIXME Expected WattHour found MetricMetricAndImperialEnergyWrapper
        assertEqualScientificValue(4(FootPoundForce), 2(FootPoundForce per Pound) * 2(Pound))
    }

    @Test
    fun energyFromSurfaceTensionAndAreaTest() {
        assertEquals(4(Joule), 2(Newton per Meter) * 2(SquareMeter))
        assertEquals(1(FootPoundForce), 2(PoundForce per Foot) * 2(SquareFoot))
    }
}
