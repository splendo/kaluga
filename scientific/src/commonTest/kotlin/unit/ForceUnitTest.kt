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

import com.splendo.kaluga.base.utils.round
import com.splendo.kaluga.scientific.assertEqualScientificValue
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.momentum.div
import com.splendo.kaluga.scientific.converter.power.div
import com.splendo.kaluga.scientific.converter.pressure.times
import com.splendo.kaluga.scientific.converter.surfaceTension.times
import com.splendo.kaluga.scientific.converter.weight.times
import com.splendo.kaluga.scientific.converter.yank.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class ForceUnitTest {

    @Test
    fun forceConversionTest() {
        assertScientificConversion(1, Newton, 1e+9, Nanonewton)
        assertScientificConversion(1, Newton, 1e+6, Micronewton)
        assertScientificConversion(1, Newton, 1000.0, Millinewton)
        assertScientificConversion(1, Newton, 100.0, Centinewton)
        assertScientificConversion(1, Newton, 10.0, Decinewton)
        assertScientificConversion(1, Newton, 0.1, Decanewton)
        assertScientificConversion(1, Newton, 0.01, Hectonewton)
        assertScientificConversion(1, Newton, 0.001, Kilonewton)
        assertScientificConversion(1, Newton, 1e-6, Meganewton)
        assertScientificConversion(1, Newton, 1e-9, Giganewton)

        assertScientificConversion(1, Newton, 100000.0, Dyne)
        assertScientificConversion(1, Newton, 0.101972, KilogramForce, 6)
        assertScientificConversion(1, Newton, 7.23301, Poundal, 5)
        assertScientificConversion(1, Newton, 0.224809, PoundForce, 6)

        assertScientificConversion(1, Dyne, 1e+9, Nanodyne)
        assertScientificConversion(1, Dyne, 1e+6, Microdyne)
        assertScientificConversion(1, Dyne, 1000.0, Millidyne)
        assertScientificConversion(1, Dyne, 100.0, Centidyne)
        assertScientificConversion(1, Dyne, 10.0, Decidyne)
        assertScientificConversion(1, Dyne, 0.1, Decadyne)
        assertScientificConversion(1, Dyne, 0.01, Hectodyne)
        assertScientificConversion(1, Dyne, 0.001, Kilodyne)
        assertScientificConversion(1, Dyne, 1e-6, Megadyne)
        assertScientificConversion(1, Dyne, 1e-9, Gigadyne)

        assertScientificConversion(1, KilogramForce, 1000.0, GramForce)
        assertScientificConversion(1, KilogramForce, 1000000.0, MilligramForce)
        assertScientificConversion(1, KilogramForce, 0.001, TonneForce)

        assertScientificConversion(1, PoundForce, 16, OunceForce)
        assertScientificConversion(1, PoundForce, 7000, GrainForce)
        assertScientificConversion(1, PoundForce, 0.001, Kip)
        assertScientificConversion(1, PoundForce, 0.00044643, ImperialTonForce, 8)
        assertScientificConversion(1, PoundForce, 1.0, PoundForce.ukImperial)
        assertScientificConversion(1, PoundForce, 0.0005, UsTonForce)
        assertScientificConversion(1, PoundForce, 1.0, PoundForce.usCustomary)
    }

    @Test
    fun forceFromMassAndAccelerationTest() {
        // TODO check if expected are accurate
        assertEquals(4.0(Newton), (2(Kilogram) * 2(Meter per Second per Second)))
        assertEquals(0.124323801(PoundForce).decimalValue, (2(Pound) * 2(Foot per Second per Second)).decimalValue.round(9))
        assertEquals(0.124323801(UsTonForce).decimalValue, (2(UsTon) * 2(Foot per Second per Second)).decimalValue.round(9))
        assertEquals(0.124323801(ImperialTonForce).decimalValue, (2(ImperialTon) * 2(Foot per Second per Second)).decimalValue.round(9))
    }

    @Test
    fun forceFromEnergyAndLengthTest() {
        assertEquals(1.0(Newton), 2(Joule) / 2(Meter))
        assertEquals(1.0(PoundForce), 2(FootPoundForce) / 2(Foot))
    }

    @Test
    fun forceFromMomentumAndTimeTest() {
        assertEquals(1.0(Newton), 2(Kilogram x (Meter per Second)) / 2(Second))
        assertEqualScientificValue(1(PoundForce), (2 * ImperialStandardGravityAcceleration.value)(Pound x (Foot per Second)) / 2(Second), 10)
    }

    @Test
    fun forceFromPowerAndSpeedTest() {
        assertEquals(1.0(Newton), 2(Watt) / 2(Meter per Second))
        assertEquals(1.0(PoundForce), 2(FootPoundForcePerSecond) / 2(Foot per Second))
    }

    @Test
    fun forceFromPressureAndAreaTest() {
        assertEquals(4(Newton), 2(Pascal) * 2(SquareMeter))
    }

    @Test
    fun forceFromSurfaceTensionAndLengthTest() {
        assertEqualScientificValue(4(Newton), 2(Newton per Meter) * 2(Meter))
    }

    @Test
    fun forceFromYankAndTimeTest() {
        assertEqualScientificValue(4.0(Newton), 2(Newton per Second) * 2(Second))
        assertEqualScientificValue(4.0(PoundForce), 2(PoundForce per Second) * 2(Second))
    }
}