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

import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.assertEqualScientificValue
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.acceleration.div
import com.splendo.kaluga.scientific.converter.action.div
import com.splendo.kaluga.scientific.converter.amountOfSubstance.div
import com.splendo.kaluga.scientific.converter.angle.div
import com.splendo.kaluga.scientific.converter.angularVelocity.div
import com.splendo.kaluga.scientific.converter.area.div
import com.splendo.kaluga.scientific.converter.decimal.div
import com.splendo.kaluga.scientific.converter.dynamicViscosity.div
import com.splendo.kaluga.scientific.converter.electricCapacitance.times
import com.splendo.kaluga.scientific.converter.electricCharge.div
import com.splendo.kaluga.scientific.converter.electricInductance.div
import com.splendo.kaluga.scientific.converter.electricResistance.times
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.converter.frequency.time
import com.splendo.kaluga.scientific.converter.length.div
import com.splendo.kaluga.scientific.converter.luminousEnergy.div
import com.splendo.kaluga.scientific.converter.luminousExposure.div
import com.splendo.kaluga.scientific.converter.magneticFlux.div
import com.splendo.kaluga.scientific.converter.speed.div
import com.splendo.kaluga.scientific.converter.weight.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test

class TimeUnitTest {

    @Test
    fun secondConversionTest() {
        assertScientificConversion(1.0, Second, 1e+9, Nanosecond)
        assertScientificConversion(1.0, Second, 1e+6, Microsecond)
        assertScientificConversion(1.0, Second, 1_000.0, Millisecond)
        assertScientificConversion(1.0, Second, 100.0, Centisecond)
        assertScientificConversion(1.0, Second, 10.0, Decisecond)
        assertScientificConversion(1.0, Second, 0.017, Minute, 3)
        assertScientificConversion(1.0, Second, 0.00028, Hour, 5)
    }

    @Test
    fun timeFromAccelerationAndJoltTest() {
        assertEqualScientificValue(1(Hour), 2(Meter per Second per Second) / 2(Meter per Second per Second per Hour))
    }

    @Test
    fun timeFromActionAndEnergyTest() {
        assertEqualScientificValue(1(Hour), 2(Decijoule x Hour) / 2(Decijoule))
    }

    @Test
    fun timeFromAmountOfSubstanceAndCatalysticActivityTest() {
        assertEqualScientificValue(1(Second), 20(Decimole) / 0.2(Decakatal))
    }

    @Test
    fun timeFromAngleAndAngularVelocityTest() {
        assertEqualScientificValue(1(Hour), 2(Deciradian) / 2(Deciradian per Hour))
    }

    @Test
    fun timeFromAngularAccelerationAndAngularVelocityTest() {
        assertEqualScientificValue(1(Hour), 2(Deciradian per Second) / 2(Deciradian per Second per Hour))
    }

    @Test
    fun timeFromChargeAndCurrentTest() {
        assertEqualScientificValue(1(Second), 20(Decicoulomb) / 0.2(Decaampere))
    }

    @Test
    fun timeFromDynamicViscosityAndPressureTest() {
        assertEqualScientificValue(1(Hour), 2(Barye x Hour) / 2(Barye))
    }

    @Test
    fun timeFromElectricCapacitanceAndResistanceTest() {
        assertEqualScientificValue(4(Second), 20(Decifarad) * 0.2(Decaohm))
        assertEqualScientificValue(4(Second), 0.2(Decaohm) * 20(Decifarad))
    }

    @Test
    fun timeFromElectricInductanceAndResistanceTest() {
        assertEqualScientificValue(1(Second), 20(Decihenry) / 0.2(Decaohm))
    }

    @Test
    fun timeFromEnergyAndPowerTest() {
        assertEqualScientificValue(1(Second), 2(Joule) / 2(Watt))
        assertEqualScientificValue(1(Hour), 2(WattHour) / 2(Watt))
        assertEqualScientificValue(1(Hour), 20(DeciwattHour) / 2(Watt))
        assertEqualScientificValue(1(Hour), 2(WattHour) / 0.2(Decawatt))
        assertEqualScientificValue(1(Hour), 20(DeciwattHour) / 0.2(Decawatt))
        assertEqualScientificValue(1(Hour), 2(HorsepowerHour) / 2(Horsepower))
        assertEqualScientificValue(1(Minute), 2(FootPoundForce) / 2(FootPoundForcePerMinute))
        assertEqualScientificValue(1(Minute), 2(InchPoundForce) / 2(InchPoundForcePerMinute))
        assertEqualScientificValue(1(Minute), 2(BritishThermalUnit) / 2(BritishThermalUnitPerMinute))
        assertEqualScientificValue(1(Hour), 2(BritishThermalUnit) / 2(BritishThermalUnitPerHour))
        assertEqualScientificValue(1(Second), 2(Joule) / 2(Watt).convert(BritishThermalUnitPerSecond))
    }

    @Test
    fun timeFromForceAndYankTest() {
        assertEqualScientificValue(1(Hour), 20(Decinewton) / 0.2(Decanewton per Hour))
    }

    @Test
    fun timeFromFrequencyTest() {
        assertEqualScientificValue(0.5(Minute), 2(BeatsPerMinute).time())
        assertEqualScientificValue(2(Second), 5(Decihertz).time())
    }

    @Test
    fun timeFromLengthAndSpeedTest() {
        assertEqualScientificValue(1(Hour), 2(Kilometer) / 2(Kilometer per Hour))
    }

    @Test
    fun timeFromLuminousEnergyAndFluxTest() {
        assertEqualScientificValue(1(Hour), 20(Decilumen x Hour) / 0.2(Decalumen))
    }

    @Test
    fun timeFromLuminousExposureAndIlluminanceTest() {
        assertEqualScientificValue(1(Hour), 20(Deciphot x Hour) / 0.2(Decaphot))
    }

    @Test
    fun timeFromMagneticFluxAndVoltageTest() {
        assertEqualScientificValue(1(Second), 20(Deciweber) / 0.2(Decavolt))
    }

    @Test
    fun timeFromInvertedRadioactivityTest() {
        assertEqualScientificValue(2(Second), 1 / 5(Decibecquerel))
        assertEqualScientificValue(2(Second), 1.toDecimal() / 5(Decibecquerel))
    }

    @Test
    fun timeFromSpeedAndAccelerationTest() {
        assertEqualScientificValue(1(Hour), 2(Kilometer per Second) / 2(Kilometer per Second per Hour))
    }

    @Test
    fun timeFromWeightAndMassFlowRateTest() {
        assertEqualScientificValue(1(Hour), 2(Gram) / 2(Gram per Hour))
    }

    @Test
    fun timeFromMetricAreaDividedByMetricKinematicViscosityTest() {
        assertEqualScientificValue(1(Second), (2(SquareMeter) / 2(SquareMeter per Second)))
    }

    @Test
    fun timeFromImperialAreaByImperialKinematicViscosityTest() {
        assertEqualScientificValue(1(Second), (2(SquareFoot) / 2(SquareFoot per Second)))
    }
}
