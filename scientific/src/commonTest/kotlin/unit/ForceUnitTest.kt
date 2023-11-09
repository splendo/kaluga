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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.scientific.assertEqualScientificValue
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.acceleration.times
import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.converter.momentum.div
import com.splendo.kaluga.scientific.converter.power.div
import com.splendo.kaluga.scientific.converter.pressure.times
import com.splendo.kaluga.scientific.converter.surfaceTension.times
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.converter.weight.times
import com.splendo.kaluga.scientific.converter.yank.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.times
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
        assertEquals(4.0(Newton), 2(Kilogram) * 2(Meter per Second per Second))
        assertEquals(4.0(Newton), 2(Meter per Second per Second) * 2(Kilogram))

        assertEquals(4.0(KilogramForce), 2(Kilogram) * 2(GUnit))
        assertEquals(4.0(KilogramForce), 2(GUnit) * 2(Kilogram))
        assertEquals(4.0(TonneForce), 2(Tonne) * 2(GUnit))
        assertEquals(4.0(TonneForce), 2(GUnit) * 2(Tonne))
        assertEquals(4.0(GramForce), 2(Gram) * 2(GUnit))
        assertEquals(4.0(GramForce), 2(GUnit) * 2(Gram))
        assertEquals(4.0(MilligramForce), 2(Milligram) * 2(GUnit))
        assertEquals(4.0(MilligramForce), 2(GUnit) * 2(Milligram))
        assertEquals((4.0 * MetricStandardGravityAcceleration.value)(Newton), 2(Kilogram) * 2(GUnit.metric))
        assertEquals((4.0 * MetricStandardGravityAcceleration.value)(Newton), 2(GUnit.metric) * 2(Kilogram))

        assertEqualScientificValue(
            4(PoundForce),
            2(Pound) * (2 * ImperialStandardGravityAcceleration),
            9,
        )
        assertEqualScientificValue(
            4(PoundForce),
            2(Pound) * 2(GUnit),
            9,
        )
        assertEqualScientificValue(
            4(PoundForce),
            2(Pound) * 2(GUnit.imperial),
            9,
        )
        assertEqualScientificValue(
            4(PoundForce),
            (2 * ImperialStandardGravityAcceleration) * 2(Pound),
            9,
        )
        assertEqualScientificValue(
            4(PoundForce),
            2(GUnit) * 2(Pound),
            9,
        )
        assertEqualScientificValue(
            4(OunceForce),
            2(Ounce) * (2 * ImperialStandardGravityAcceleration),
            9,
        )
        assertEqualScientificValue(
            4(OunceForce),
            2(Ounce) * 2(GUnit),
            9,
        )
        assertEqualScientificValue(
            4(OunceForce),
            (2 * ImperialStandardGravityAcceleration) * 2(Ounce),
            9,
        )
        assertEqualScientificValue(
            4(OunceForce),
            2(GUnit) * 2(Ounce),
            9,
        )
        assertEqualScientificValue(
            4(GrainForce),
            2(Grain) * (2 * ImperialStandardGravityAcceleration),
            9,
        )
        assertEqualScientificValue(
            4(GrainForce),
            2(Grain) * 2(GUnit),
            9,
        )
        assertEqualScientificValue(
            4(GrainForce),
            (2 * ImperialStandardGravityAcceleration) * 2(Grain),
            9,
        )
        assertEqualScientificValue(
            4(GrainForce),
            2(GUnit) * 2(Grain),
            9,
        )
        assertEqualScientificValue(
            4(UsTonForce),
            2(UsTon) * (2 * ImperialStandardGravityAcceleration),
            9,
        )
        assertEqualScientificValue(
            4(UsTonForce),
            2(UsTon) * 2(GUnit),
            9,
        )
        assertEqualScientificValue(
            4(UsTonForce),
            (2 * ImperialStandardGravityAcceleration) * 2(UsTon),
            9,
        )
        assertEqualScientificValue(
            4(UsTonForce),
            2(GUnit) * 2(UsTon),
            9,
        )
        assertEqualScientificValue(
            4(ImperialTonForce),
            2(ImperialTon) * (2 * ImperialStandardGravityAcceleration),
            9,
        )
        assertEqualScientificValue(
            4(ImperialTonForce),
            2(ImperialTon) * 2(GUnit),
            9,
        )
        assertEqualScientificValue(
            4(ImperialTonForce),
            (2 * ImperialStandardGravityAcceleration) * 2(ImperialTon),
            9,
        )
        assertEqualScientificValue(
            4(ImperialTonForce),
            2(GUnit) * 2(ImperialTon),
            9,
        )
        assertEqualScientificValue(
            4(PoundForce),
            2(Pound as ImperialWeight) * (2 * ImperialStandardGravityAcceleration),
            9,
        )
        assertEqualScientificValue(
            4(PoundForce),
            2(Pound as ImperialWeight) * 2(GUnit),
            9,
        )
        assertEqualScientificValue(
            4(PoundForce),
            (2 * ImperialStandardGravityAcceleration) * 2(Pound as ImperialWeight),
            9,
        )
        assertEqualScientificValue(
            4(PoundForce),
            2(GUnit) * 2(Pound as ImperialWeight),
            9,
        )
        assertEqualScientificValue(
            4(PoundForce.ukImperial),
            2(Pound.ukImperial) * (2 * ImperialStandardGravityAcceleration),
            9,
        )
        assertEqualScientificValue(
            4(PoundForce.ukImperial),
            2(Pound.ukImperial) * 2(GUnit),
            9,
        )
        assertEqualScientificValue(
            4(PoundForce.ukImperial),
            (2 * ImperialStandardGravityAcceleration) * 2(Pound.ukImperial),
            9,
        )
        assertEqualScientificValue(
            4(PoundForce.ukImperial),
            2(GUnit) * 2(Pound.ukImperial),
            9,
        )
        assertEqualScientificValue(
            4(PoundForce.usCustomary),
            2(Pound.usCustomary) * (2 * ImperialStandardGravityAcceleration),
            9,
        )
        assertEqualScientificValue(
            4(PoundForce.usCustomary),
            2(Pound.usCustomary) * 2(GUnit),
            9,
        )
        assertEqualScientificValue(
            4(PoundForce.usCustomary),
            (2 * ImperialStandardGravityAcceleration) * 2(Pound.usCustomary),
            9,
        )
        assertEqualScientificValue(
            4(PoundForce.usCustomary),
            2(GUnit) * 2(Pound.usCustomary),
            9,
        )
        assertEquals(
            4.0(Newton),
            2(Kilogram) * 2(Meter per Second per Second).convert(Foot per Second per Second),
        )
        assertEquals(
            4.0(Newton),
            2(Meter per Second per Second).convert(Foot per Second per Second) * 2(Kilogram),
        )
    }

    @Test
    fun forceFromEnergyAndLengthTest() {
        assertEquals(1.0(Dyne), 2(Erg) / 2(Centimeter))
        assertEquals(1.0(Dyne), 20(Decierg) / 2(Centimeter))
        assertEquals(1.0(Newton), 2(Joule) / 2(Meter))
        assertEquals(1.0(Newton), 2(Joule).convert(WattHour) / 2(Meter))

        assertEquals(1.0(Poundal), 2(FootPoundal) / 2(Foot))
        assertEquals(1.0(OunceForce), 2(InchOunceForce) / 2(Inch))
        assertEquals(1.0(PoundForce), 2(FootPoundForce) / 2(Foot))
        assertEqualScientificValue(
            1.0(Newton).convert(PoundForce),
            2(Joule).convert(WattHour) / 2(Meter).convert(Foot),
            8,
        )
        assertEqualScientificValue(1.0(Newton), 2(Joule) / 2(Meter).convert(Foot), 8)
    }

    @Test
    fun forceFromMomentumAndTimeTest() {
        assertEquals(1.0(Newton), 2(Kilogram x (Meter per Second)) / 2(Second))
        assertEqualScientificValue(
            1(PoundForce),
            (2 * ImperialStandardGravityAcceleration.value)(Pound x (Foot per Second)) / 2(Second),
            10,
        )
        assertEqualScientificValue(
            1(PoundForce.ukImperial),
            (2 * ImperialStandardGravityAcceleration.value)(Pound.ukImperial x (Foot per Second)) / 2(
                Second,
            ),
            10,
        )
        assertEqualScientificValue(
            1(PoundForce.usCustomary),
            (2 * ImperialStandardGravityAcceleration.value)(Pound.usCustomary x (Foot per Second)) / 2(
                Second,
            ),
            10,
        )
        assertEquals(1.0(Newton), 2(Kilogram x (Meter per Second)).convert((Pound x (Foot per Second)) as Momentum) / 2(Second))
    }

    @Test
    fun forceFromPowerAndSpeedTest() {
        assertEquals(1.0(Dyne), 2(ErgPerSecond) / 2(Centimeter per Second))
        assertEquals(1.0(Newton), 2(Watt.metric) / 2(Meter per Second))
        assertEquals(1.0(PoundForce), 2(FootPoundForcePerSecond) / 2(Foot per Second))
        assertEquals(1.0(PoundForce), 2(FootPoundForcePerSecond).convert(Watt) / 2(Foot per Second))
        assertEquals(1.0(Newton), 2(Watt.metric) / 2(Meter per Second).convert(Foot per Second))
    }

    @Test
    fun forceFromPressureAndAreaTest() {
        assertEquals(4(Dyne), 2(Barye) * 2(SquareCentimeter))
        assertEquals(4(Dyne), 2(SquareCentimeter) * 2(Barye))
        assertEquals(4(Dyne), 20(Decibarye) * 2(SquareCentimeter))
        assertEquals(4(Dyne), 2(SquareCentimeter) * 20(Decibarye))
        assertEquals(4(Newton), 2(Pascal) * 2(SquareMeter))
        assertEquals(4(Newton), 2(SquareMeter) * 2(Pascal))
        assertEquals(4(PoundForce), 2(PoundSquareFoot) * 2(SquareFoot))
        assertEquals(4(PoundForce), 2(SquareFoot) * 2(PoundSquareFoot))
        assertEquals(4(OunceForce), 2(OunceSquareInch) * 2(SquareInch))
        assertEquals(4(OunceForce), 2(SquareInch) * 2(OunceSquareInch))
        assertEquals(4(Kip), 2(KipSquareInch) * 2(SquareInch))
        assertEquals(4(Kip), 2(SquareInch) * 2(KipSquareInch))
        assertEquals(4(Kip), 2(KipSquareFoot) * 2(SquareFoot))
        assertEquals(4(Kip), 2(SquareFoot) * 2(KipSquareFoot))
        assertEquals(4(UsTonForce), 2(USTonSquareInch) * 2(SquareInch))
        assertEquals(4(UsTonForce), 2(SquareInch) * 2(USTonSquareInch))
        assertEquals(4(UsTonForce), 2(USTonSquareFoot) * 2(SquareFoot))
        assertEquals(4(UsTonForce), 2(SquareFoot) * 2(USTonSquareFoot))
        assertEquals(4(ImperialTonForce), 2(ImperialTonSquareInch) * 2(SquareInch))
        assertEquals(4(ImperialTonForce), 2(SquareInch) * 2(ImperialTonSquareInch))
        assertEquals(4(ImperialTonForce), 2(ImperialTonSquareFoot) * 2(SquareFoot))
        assertEquals(4(ImperialTonForce), 2(SquareFoot) * 2(ImperialTonSquareFoot))
        assertEquals(4(PoundForce), 2(PoundSquareFoot).convert(InchOfMercury) * 2(SquareFoot))
        assertEquals(4(PoundForce), 2(SquareFoot) * 2(PoundSquareFoot).convert(InchOfMercury))
        assertEquals(4(PoundForce.ukImperial), 2(PoundSquareFoot.ukImperial) * 2(SquareFoot))
        assertEquals(4(PoundForce.ukImperial), 2(SquareFoot) * 2(PoundSquareFoot.ukImperial))
        assertEquals(4(PoundForce.usCustomary), 2(PoundSquareFoot.usCustomary) * 2(SquareFoot))
        assertEquals(4(PoundForce.usCustomary), 2(SquareFoot) * 2(PoundSquareFoot.usCustomary))
        assertEquals(4(Newton), 2(Pascal) * 2(SquareMeter).convert(SquareFoot))
        assertEquals(4(Newton), 2(SquareMeter).convert(SquareFoot) * 2(Pascal))
    }

    @Test
    fun forceFromSurfaceTensionAndLengthTest() {
        assertEqualScientificValue(4(Newton), 2(Newton per Meter) * 2(Meter))
        assertEqualScientificValue(4(Newton), 2(Meter) * 2(Newton per Meter))
        assertEqualScientificValue(4(PoundForce), 2(PoundForce per Foot) * 2(Foot))
        assertEqualScientificValue(4(PoundForce), 2(Foot) * 2(PoundForce per Foot))
        assertEqualScientificValue(4(ImperialTonForce), 2(ImperialTonForce per Foot) * 2(Foot))
        assertEqualScientificValue(4(ImperialTonForce), 2(Foot) * 2(ImperialTonForce per Foot))
        assertEqualScientificValue(4(UsTonForce), 2(UsTonForce per Foot) * 2(Foot))
        assertEqualScientificValue(4(UsTonForce), 2(Foot) * 2(UsTonForce per Foot))
        assertEqualScientificValue(4(Newton), 2(Newton per Meter) * 2(Meter).convert(Foot))
        assertEqualScientificValue(4(Newton), 2(Meter).convert(Foot) * 2(Newton per Meter))
    }

    @Test
    fun forceFromYankAndTimeTest() {
        assertEqualScientificValue(4.0(Newton), 2(Newton per Second) * 2(Second))
        assertEqualScientificValue(4.0(Newton), 2(Second) * 2(Newton per Second))
        assertEqualScientificValue(4.0(PoundForce), 2(PoundForce per Second) * 2(Second))
        assertEqualScientificValue(4.0(PoundForce), 2(Second) * 2(PoundForce per Second))
        assertEqualScientificValue(
            4.0(ImperialTonForce),
            2(ImperialTonForce per Second) * 2(Second),
        )
        assertEqualScientificValue(
            4.0(ImperialTonForce),
            2(Second) * 2(ImperialTonForce per Second),
        )
        assertEqualScientificValue(4.0(UsTonForce), 2(UsTonForce per Second) * 2(Second))
        assertEqualScientificValue(4.0(UsTonForce), 2(Second) * 2(UsTonForce per Second))
        assertEqualScientificValue(4.0(Newton), 2((Newton per Second) as Yank) * 2(Second))
        assertEqualScientificValue(4.0(Newton), 2(Second) * 2((Newton per Second) as Yank))
    }
}
