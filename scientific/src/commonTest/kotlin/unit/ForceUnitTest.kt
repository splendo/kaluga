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
import com.splendo.kaluga.scientific.converter.weight.times
import com.splendo.kaluga.scientific.converter.yank.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class ForceUnitTest {

    @Test
    fun electricChargeConversionTest() {
        assertEquals(1e+9, Newton.convert(1, Nanonewton))
        assertEquals(1e+6, Newton.convert(1, Micronewton))
        assertEquals(1000.0, Newton.convert(1, Millinewton))
        assertEquals(100.0, Newton.convert(1, Centinewton))
        assertEquals(10.0, Newton.convert(1, Decinewton))
        assertEquals(0.1, Newton.convert(1, Decanewton))
        assertEquals(0.01, Newton.convert(1, Hectonewton))
        assertEquals(0.001, Newton.convert(1, Kilonewton))
        assertEquals(1e-6, Newton.convert(1, Meganewton))
        assertEquals(1e-9, Newton.convert(1, Giganewton))

        assertEquals(100000.0, Newton.convert(1, Dyne))
        assertEquals(0.101972, Newton.convert(1, KilogramForce, 6))
        assertEquals(7.23301, Newton.convert(1, Poundal, 5))
        assertEquals(0.000225, Newton.convert(1, Kip, 6))
        // assertEquals(0.000102, Newton.convert(1, UsTonForce, 6)) FIXME

        assertEquals(1e+9, Dyne.convert(1, Nanodyne))
        assertEquals(1e+6, Dyne.convert(1, Microdyne))
        assertEquals(1000.0, Dyne.convert(1, Millidyne))
        assertEquals(100.0, Dyne.convert(1, Centidyne))
        assertEquals(10.0, Dyne.convert(1, Decidyne))
        assertEquals(0.1, Dyne.convert(1, Decadyne))
        assertEquals(0.01, Dyne.convert(1, Hectodyne))
        assertEquals(0.001, Dyne.convert(1, Kilodyne))
        assertEquals(1e-6, Dyne.convert(1, Megadyne))
        assertEquals(1e-9, Dyne.convert(1, Gigadyne))

        assertEquals(1.01972e-6, Dyne.convert(1, KilogramForce, 11))
        assertEquals(7.23301e-5, Dyne.convert(1, Poundal, 10))
        assertEquals(2.248089431e-9, Dyne.convert(1, Kip, 18))
        // assertEquals(0.000000001, Dyne.convert(1, UsTonForce)) FIXME

        assertEquals(1000.0, KilogramForce.convert(1, GramForce))
        assertEquals(1000000.0, KilogramForce.convert(1, MilligramForce))
        assertEquals(0.001, KilogramForce.convert(1, TonneForce))

        assertEquals(70.9316, KilogramForce.convert(1, Poundal, 4))
        assertEquals(0.0022, KilogramForce.convert(1, Kip, 4))
        // assertEquals(1000.0, KilogramForce.convert(1, UsTonForce)) FIXME

        assertEquals(0.031081, Poundal.convert(1, PoundForce, 6))
        assertEquals(0.497295, Poundal.convert(1, OunceForce, 6))
        // assertEquals(217.39130435, Poundal.convert(1, GrainForce,8)) FIXME

        // assertEquals(0.000031080949294433, Poundal.convert(1, Kip)) FIXME
        // assertEquals(1e+9, Poundal.convert(1, ImperialTonForce)) FIXME

        assertEquals(0.5, Kip.convert(1, UsTonForce, 1))
        assertEquals(0.4464, Kip.convert(1, ImperialTonForce, 4))
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
        assertEquals(1.0(PoundForce), 2(Pound x (Foot per Second)) / 2(Second))
    }

    @Test
    fun forceFromPowerAndSpeedTest() {
        assertEquals(1.0(Newton), 2(Watt) / 2(Meter per Second))
        assertEquals(1.0(PoundForce), 2(FootPoundForcePerSecond) / 2(Foot per Second))
    }

    @Test
    fun forceFromPressureAndAreaTest() {
        // 2(Pascal) * 2(SquareMeter) FIXME
    }

    @Test
    fun forceFromSurfaceTensionAndLengthTest() {
        // 2(Newton per Meter) * 2(Meter) FIXME
    }

    @Test
    fun forceFromYankAndTimeTest() {
        assertEqualScientificValue(4.0(Newton), 2(Newton per Second) * 2(Second))
        assertEqualScientificValue(4.0(PoundForce), 2(PoundForce per Second) * 2(Second))
    }
}