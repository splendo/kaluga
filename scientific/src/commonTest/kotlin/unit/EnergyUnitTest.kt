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
        assertScientificConversion(1.0, Electronvolt, 1e-6, Megaelectronvolt, 7, bidirectional = false)
        assertScientificConversion(1.0, Megaelectronvolt, 1e6, Electronvolt, 7, bidirectional = false)
        assertScientificConversion(1.0, Electronvolt, 1e-9, Gigaelectronvolt, 10, bidirectional = false)
        assertScientificConversion(1.0, Gigaelectronvolt, 1e9, Electronvolt, 10, bidirectional = false)
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
        assertEqualScientificValue(4(WattHour.metric), 2(WattHour per Kilogram) * 2(Kilogram))
        assertEqualScientificValue(4(FootPoundForce), 2(FootPoundForce per Pound) * 2(Pound))
    }

    @Test
    fun energyFromSurfaceTensionAndAreaTest() {
        assertEquals(4(Joule), 2(Newton per Meter) * 2(SquareMeter))
        assertEquals(4(FootPoundForce), 2(PoundForce per Foot) * 2(SquareFoot))
    }
}
