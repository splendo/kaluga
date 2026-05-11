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

import com.splendo.kaluga.base.utils.toDecimal
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
import com.splendo.kaluga.scientific.converter.kinematicViscosity.div
import com.splendo.kaluga.scientific.converter.length.div
import com.splendo.kaluga.scientific.converter.luminousEnergy.div
import com.splendo.kaluga.scientific.converter.luminousExposure.div
import com.splendo.kaluga.scientific.converter.magneticFlux.div
import com.splendo.kaluga.scientific.converter.speed.div
import com.splendo.kaluga.scientific.converter.weight.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.BeatsPerMinute
import com.splendo.kaluga.scientific.unit.BritishThermalUnit
import com.splendo.kaluga.scientific.unit.Decaampere
import com.splendo.kaluga.scientific.unit.Decakatal
import com.splendo.kaluga.scientific.unit.Decalumen
import com.splendo.kaluga.scientific.unit.Decanewton
import com.splendo.kaluga.scientific.unit.Decaohm
import com.splendo.kaluga.scientific.unit.Decaphot
import com.splendo.kaluga.scientific.unit.Decavolt
import com.splendo.kaluga.scientific.unit.Decawatt
import com.splendo.kaluga.scientific.unit.Decibecquerel
import com.splendo.kaluga.scientific.unit.Decicoulomb
import com.splendo.kaluga.scientific.unit.Decifarad
import com.splendo.kaluga.scientific.unit.Decihenry
import com.splendo.kaluga.scientific.unit.Decihertz
import com.splendo.kaluga.scientific.unit.Decijoule
import com.splendo.kaluga.scientific.unit.Decilumen
import com.splendo.kaluga.scientific.unit.Decimole
import com.splendo.kaluga.scientific.unit.Decinewton
import com.splendo.kaluga.scientific.unit.Deciphot
import com.splendo.kaluga.scientific.unit.Deciradian
import com.splendo.kaluga.scientific.unit.DeciwattHour
import com.splendo.kaluga.scientific.unit.Deciweber
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.Horsepower
import com.splendo.kaluga.scientific.unit.HorsepowerHour
import com.splendo.kaluga.scientific.unit.Hour
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Kilometer
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Minute
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test
import com.splendo.kaluga.test.base.IgnoreJs

@IgnoreJs
class TimeUnitTest {

    @Test
    fun timeFromAccelerationAndJoltTest() {
        assertEqualScientificValue(1(Hour), 2(Meter per Second per Second) / 2(Meter per Second per Second per Hour), round = 29)
    }

    @Test
    fun timeFromActionAndEnergyTest() {
        assertEqualScientificValue(1(Hour), 2(Decijoule x Hour) / 2(Decijoule))
    }

    @Test
    fun timeFromAmountOfSubstanceAndCatalysticActivityTest() {
        assertEqualScientificValue(1(Second), 20(Decimole) / "0.2".toDecimal()(Decakatal))
    }

    @Test
    fun timeFromAngleAndAngularVelocityTest() {
        assertEqualScientificValue(1(Hour), 2(Deciradian) / 2(Deciradian per Hour), round = 29)
    }

    @Test
    fun timeFromAngularAccelerationAndAngularVelocityTest() {
        assertEqualScientificValue(1(Hour), 2(Deciradian per Second) / 2(Deciradian per Second per Hour), round = 29)
    }

    @Test
    fun timeFromChargeAndCurrentTest() {
        assertEqualScientificValue(1(Second), 20(Decicoulomb) / "0.2".toDecimal()(Decaampere))
    }

    @Test
    fun timeFromDynamicViscosityAndPressureTest() {
        assertEqualScientificValue(1(Hour), 2(Barye x Hour) / 2(Barye))
    }

    @Test
    fun timeFromElectricCapacitanceAndResistanceTest() {
        assertEqualScientificValue(4(Second), 20(Decifarad) * "0.2".toDecimal()(Decaohm))
        assertEqualScientificValue(4(Second), "0.2".toDecimal()(Decaohm) * 20(Decifarad))
    }

    @Test
    fun timeFromElectricInductanceAndResistanceTest() {
        assertEqualScientificValue(1(Second), 20(Decihenry) / "0.2".toDecimal()(Decaohm))
    }

    @Test
    fun timeFromEnergyAndPowerTest() {
        assertEqualScientificValue(1(Second), 2(Joule) / 2(Watt))
        assertEqualScientificValue(1(Hour), 2(WattHour) / 2(Watt))
        assertEqualScientificValue(1(Hour), 20(DeciwattHour) / 2(Watt))
        assertEqualScientificValue(1(Hour), 2(WattHour) / "0.2".toDecimal()(Decawatt))
        assertEqualScientificValue(1(Hour), 20(DeciwattHour) / "0.2".toDecimal()(Decawatt))
        assertEqualScientificValue(1(Hour), 2(HorsepowerHour) / 2(Horsepower))
        assertEqualScientificValue(1(Minute), 2(FootPoundForce) / 2(FootPoundForce per Minute), round = 32)
        assertEqualScientificValue(1(Minute), 2(InchPoundForce) / 2(InchPoundForce per Minute), round = 32)
        assertEqualScientificValue(1(Minute), 2(BritishThermalUnit) / 2(BritishThermalUnit per Minute), round = 32)
        assertEqualScientificValue(1(Hour), 2(BritishThermalUnit) / 2(BritishThermalUnit per Hour), round = 32)
        assertEqualScientificValue(1(Second), 2(Joule) / 2(Watt).convert(BritishThermalUnit per Second), round = 32)
    }

    @Test
    fun timeFromForceAndYankTest() {
        assertEqualScientificValue(1(Hour), 20(Decinewton) / "0.2".toDecimal()(Decanewton per Hour), round = 29)
    }

    @Test
    fun timeFromFrequencyTest() {
        assertEqualScientificValue(0.5(Minute), 2(BeatsPerMinute).time(), round = 32)
        assertEqualScientificValue(2(Second), 5(Decihertz).time())
    }

    @Test
    fun timeFromKinematicViscosityAndSpecificEnergyTest() {
        assertEqualScientificValue(1(Minute), 7200(SquareMeter per Minute) / 2(Joule per Kilogram))
    }

    @Test
    fun timeFromLengthAndSpeedTest() {
        assertEqualScientificValue(1(Hour), 2(Kilometer) / 2(Kilometer per Hour), round = 29)
    }

    @Test
    fun timeFromLuminousEnergyAndFluxTest() {
        assertEqualScientificValue(1(Hour), 20(Decilumen x Hour) / "0.2".toDecimal()(Decalumen))
    }

    @Test
    fun timeFromLuminousExposureAndIlluminanceTest() {
        assertEqualScientificValue(1(Hour), 20(Deciphot x Hour) / "0.2".toDecimal()(Decaphot))
    }

    @Test
    fun timeFromMagneticFluxAndVoltageTest() {
        assertEqualScientificValue(1(Second), 20(Deciweber) / "0.2".toDecimal()(Decavolt))
    }

    @Test
    fun timeFromInvertedRadioactivityTest() {
        assertEqualScientificValue(2(Second), 1 / 5(Decibecquerel))
        assertEqualScientificValue(2(Second), 1.toDecimal() / 5(Decibecquerel))
    }

    @Test
    fun timeFromSpeedAndAccelerationTest() {
        assertEqualScientificValue(1(Hour), 2(Kilometer per Second) / 2(Kilometer per Second per Hour), round = 29)
    }

    @Test
    fun timeFromWeightAndMassFlowRateTest() {
        assertEqualScientificValue(1(Hour), 2(Gram) / 2(Gram per Hour), round = 29)
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
