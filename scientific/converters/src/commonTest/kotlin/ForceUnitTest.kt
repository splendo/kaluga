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

import com.splendo.kaluga.base.decimal.times
import com.splendo.kaluga.base.decimal.toDecimal
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
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Decibarye
import com.splendo.kaluga.scientific.unit.Decierg
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.FootPoundal
import com.splendo.kaluga.scientific.unit.GUnit
import com.splendo.kaluga.scientific.unit.Grain
import com.splendo.kaluga.scientific.unit.GrainForce
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.GramForce
import com.splendo.kaluga.scientific.unit.ImperialStandardGravityAcceleration
import com.splendo.kaluga.scientific.unit.ImperialTon
import com.splendo.kaluga.scientific.unit.ImperialTonForce
import com.splendo.kaluga.scientific.unit.ImperialTonSquareFoot
import com.splendo.kaluga.scientific.unit.ImperialTonSquareInch
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.InchOfMercury
import com.splendo.kaluga.scientific.unit.InchOunceForce
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.KilogramForce
import com.splendo.kaluga.scientific.unit.Kip
import com.splendo.kaluga.scientific.unit.KipSquareFoot
import com.splendo.kaluga.scientific.unit.KipSquareInch
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricStandardGravityAcceleration
import com.splendo.kaluga.scientific.unit.Milligram
import com.splendo.kaluga.scientific.unit.MilligramForce
import com.splendo.kaluga.scientific.unit.Momentum
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.Ounce
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.OunceSquareInch
import com.splendo.kaluga.scientific.unit.Pascal
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.PoundSquareFoot
import com.splendo.kaluga.scientific.unit.Poundal
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareInch
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.Tonne
import com.splendo.kaluga.scientific.unit.TonneForce
import com.splendo.kaluga.scientific.unit.USTonSquareFoot
import com.splendo.kaluga.scientific.unit.USTonSquareInch
import com.splendo.kaluga.scientific.unit.UsTon
import com.splendo.kaluga.scientific.unit.UsTonForce
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.Yank
import com.splendo.kaluga.scientific.unit.metric
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test

class ForceUnitTest {

    @Test
    fun forceFromMassAndAccelerationTest() {
        assertEqualScientificValue(4.0(Newton), 2(Kilogram) * 2(Meter per Second per Second))
        assertEqualScientificValue(4.0(Newton), 2(Meter per Second per Second) * 2(Kilogram))

        assertEqualScientificValue(4.0(KilogramForce), 2(Kilogram) * 2(GUnit))
        assertEqualScientificValue(4.0(KilogramForce), 2(GUnit) * 2(Kilogram))
        assertEqualScientificValue(4.0(TonneForce), 2(Tonne) * 2(GUnit))
        assertEqualScientificValue(4.0(TonneForce), 2(GUnit) * 2(Tonne))
        assertEqualScientificValue(4.0(GramForce), 2(Gram) * 2(GUnit))
        assertEqualScientificValue(4.0(GramForce), 2(GUnit) * 2(Gram))
        assertEqualScientificValue(4.0(MilligramForce), 2(Milligram) * 2(GUnit))
        assertEqualScientificValue(4.0(MilligramForce), 2(GUnit) * 2(Milligram))
        assertEqualScientificValue((4.0.toDecimal() * MetricStandardGravityAcceleration.decimalValue)(Newton), 2(Kilogram) * 2(GUnit.metric))
        assertEqualScientificValue((4.0.toDecimal() * MetricStandardGravityAcceleration.decimalValue)(Newton), 2(GUnit.metric) * 2(Kilogram))

        assertEqualScientificValue(
            4(PoundForce),
            2(Pound) * (2 * ImperialStandardGravityAcceleration),
            round = 32,
        )
        assertEqualScientificValue(
            4(PoundForce),
            2(Pound) * 2(GUnit),
            round = 32,
        )
        assertEqualScientificValue(
            4(PoundForce),
            2(Pound) * 2(GUnit.imperial),
            round = 32,
        )
        assertEqualScientificValue(
            4(PoundForce),
            (2 * ImperialStandardGravityAcceleration) * 2(Pound),
            round = 32,
        )
        assertEqualScientificValue(
            4(PoundForce),
            2(GUnit) * 2(Pound),
            round = 32,
        )
        assertEqualScientificValue(
            4(OunceForce),
            2(Ounce) * (2 * ImperialStandardGravityAcceleration),
            round = 32,
        )
        assertEqualScientificValue(
            4(OunceForce),
            2(Ounce) * 2(GUnit),
            round = 32,
        )
        assertEqualScientificValue(
            4(OunceForce),
            (2 * ImperialStandardGravityAcceleration) * 2(Ounce),
            round = 32,
        )
        assertEqualScientificValue(
            4(OunceForce),
            2(GUnit) * 2(Ounce),
            round = 32,
        )
        assertEqualScientificValue(
            4(GrainForce),
            2(Grain) * (2 * ImperialStandardGravityAcceleration),
            round = 32,
        )
        assertEqualScientificValue(
            4(GrainForce),
            2(Grain) * 2(GUnit),
            round = 32,
        )
        assertEqualScientificValue(
            4(GrainForce),
            (2 * ImperialStandardGravityAcceleration) * 2(Grain),
            round = 32,
        )
        assertEqualScientificValue(
            4(GrainForce),
            2(GUnit) * 2(Grain),
            round = 32,
        )
        assertEqualScientificValue(
            4(UsTonForce),
            2(UsTon) * (2 * ImperialStandardGravityAcceleration),
            round = 32,
        )
        assertEqualScientificValue(
            4(UsTonForce),
            2(UsTon) * 2(GUnit),
            round = 32,
        )
        assertEqualScientificValue(
            4(UsTonForce),
            (2 * ImperialStandardGravityAcceleration) * 2(UsTon),
            round = 32,
        )
        assertEqualScientificValue(
            4(UsTonForce),
            2(GUnit) * 2(UsTon),
            round = 32,
        )
        assertEqualScientificValue(
            4(ImperialTonForce),
            2(ImperialTon) * (2 * ImperialStandardGravityAcceleration),
            round = 32,
        )
        assertEqualScientificValue(
            4(ImperialTonForce),
            2(ImperialTon) * 2(GUnit),
            round = 32,
        )
        assertEqualScientificValue(
            4(ImperialTonForce),
            (2 * ImperialStandardGravityAcceleration) * 2(ImperialTon),
            round = 32,
        )
        assertEqualScientificValue(
            4(ImperialTonForce),
            2(GUnit) * 2(ImperialTon),
            round = 32,
        )
        assertEqualScientificValue(
            4(PoundForce),
            2(Pound as ImperialWeight) * (2 * ImperialStandardGravityAcceleration),
            round = 32,
        )
        assertEqualScientificValue(
            4(PoundForce),
            2(Pound as ImperialWeight) * 2(GUnit),
            round = 32,
        )
        assertEqualScientificValue(
            4(PoundForce),
            (2 * ImperialStandardGravityAcceleration) * 2(Pound as ImperialWeight),
            round = 32,
        )
        assertEqualScientificValue(
            4(PoundForce),
            2(GUnit) * 2(Pound as ImperialWeight),
            round = 32,
        )
        assertEqualScientificValue(
            4(PoundForce.ukImperial),
            2(Pound.ukImperial) * (2 * ImperialStandardGravityAcceleration),
            round = 32,
        )
        assertEqualScientificValue(
            4(PoundForce.ukImperial),
            2(Pound.ukImperial) * 2(GUnit),
            round = 32,
        )
        assertEqualScientificValue(
            4(PoundForce.ukImperial),
            (2 * ImperialStandardGravityAcceleration) * 2(Pound.ukImperial),
            round = 32,
        )
        assertEqualScientificValue(
            4(PoundForce.ukImperial),
            2(GUnit) * 2(Pound.ukImperial),
            round = 32,
        )
        assertEqualScientificValue(
            4(PoundForce.usCustomary),
            2(Pound.usCustomary) * (2 * ImperialStandardGravityAcceleration),
            round = 32,
        )
        assertEqualScientificValue(
            4(PoundForce.usCustomary),
            2(Pound.usCustomary) * 2(GUnit),
            round = 32,
        )
        assertEqualScientificValue(
            4(PoundForce.usCustomary),
            (2 * ImperialStandardGravityAcceleration) * 2(Pound.usCustomary),
            round = 32,
        )
        assertEqualScientificValue(
            4(PoundForce.usCustomary),
            2(GUnit) * 2(Pound.usCustomary),
            round = 32,
        )
        assertEqualScientificValue(
            4.0(Newton),
            2(Kilogram) * 2(Meter per Second per Second).convert(Foot per Second per Second),
            round = 32,
        )
        assertEqualScientificValue(
            4.0(Newton),
            2(Meter per Second per Second).convert(Foot per Second per Second) * 2(Kilogram),
            round = 32,
        )
    }

    @Test
    fun forceFromEnergyAndLengthTest() {
        assertEqualScientificValue(1.0(Dyne), 2(Erg) / 2(Centimeter))
        assertEqualScientificValue(1.0(Dyne), 20(Decierg) / 2(Centimeter))
        assertEqualScientificValue(1.0(Newton), 2(Joule) / 2(Meter))
        assertEqualScientificValue(1.0(Newton), 2(Joule).convert(WattHour) / 2(Meter), round = 32)

        assertEqualScientificValue(1.0(Poundal), 2(FootPoundal) / 2(Foot), round = 32)
        assertEqualScientificValue(1.0(OunceForce), 2(InchOunceForce) / 2(Inch), round = 32)
        assertEqualScientificValue(1.0(PoundForce), 2(FootPoundForce) / 2(Foot))
        assertEqualScientificValue(
            1.0(Newton).convert(PoundForce),
            2(Joule).convert(WattHour) / 2(Meter).convert(Foot),
            round = 30,
        )
        assertEqualScientificValue(1.0(Newton), 2(Joule) / 2(Meter).convert(Foot), round = 32)
    }

    @Test
    fun forceFromMomentumAndTimeTest() {
        assertEqualScientificValue(1.0(Newton), 2(Kilogram x (Meter per Second)) / 2(Second))
        assertEqualScientificValue(
            1(PoundForce),
            (2.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Pound x (Foot per Second)) / 2(Second),
            round = 32,
        )
        assertEqualScientificValue(
            1(PoundForce.ukImperial),
            (2.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Pound.ukImperial x (Foot per Second)) / 2(
                Second,
            ),
            round = 32,
        )
        assertEqualScientificValue(
            1(PoundForce.usCustomary),
            (2.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Pound.usCustomary x (Foot per Second)) / 2(
                Second,
            ),
            round = 32,
        )
        assertEqualScientificValue(1.0(Newton), 2(Kilogram x (Meter per Second)).convert((Pound x (Foot per Second)) as Momentum) / 2(Second), round = 30)
    }

    @Test
    fun forceFromPowerAndSpeedTest() {
        assertEqualScientificValue(1.0(Dyne), 2(Erg per Second) / 2(Centimeter per Second))
        assertEqualScientificValue(1.0(Newton), 2(Watt.metric) / 2(Meter per Second))
        assertEqualScientificValue(1.0(PoundForce), 2(FootPoundForce per Second) / 2(Foot per Second))
        assertEqualScientificValue(1.0(PoundForce), 2(FootPoundForce per Second).convert(Watt) / 2(Foot per Second))
        assertEqualScientificValue(1.0(Newton), 2(Watt.metric) / 2(Meter per Second).convert(Foot per Second), round = 32)
    }

    @Test
    fun forceFromPressureAndAreaTest() {
        assertEqualScientificValue(4(Dyne), 2(Barye) * 2(SquareCentimeter))
        assertEqualScientificValue(4(Dyne), 2(SquareCentimeter) * 2(Barye))
        assertEqualScientificValue(4(Dyne), 20(Decibarye) * 2(SquareCentimeter))
        assertEqualScientificValue(4(Dyne), 2(SquareCentimeter) * 20(Decibarye))
        assertEqualScientificValue(4(Newton), 2(Pascal) * 2(SquareMeter))
        assertEqualScientificValue(4(Newton), 2(SquareMeter) * 2(Pascal))
        assertEqualScientificValue(4(PoundForce), 2(PoundSquareFoot) * 2(SquareFoot), round = 32)
        assertEqualScientificValue(4(PoundForce), 2(SquareFoot) * 2(PoundSquareFoot), round = 32)
        assertEqualScientificValue(4(OunceForce), 2(OunceSquareInch) * 2(SquareInch), round = 32)
        assertEqualScientificValue(4(OunceForce), 2(SquareInch) * 2(OunceSquareInch), round = 32)
        assertEqualScientificValue(4(Kip), 2(KipSquareInch) * 2(SquareInch), round = 32)
        assertEqualScientificValue(4(Kip), 2(SquareInch) * 2(KipSquareInch), round = 32)
        assertEqualScientificValue(4(Kip), 2(KipSquareFoot) * 2(SquareFoot), round = 32)
        assertEqualScientificValue(4(Kip), 2(SquareFoot) * 2(KipSquareFoot), round = 32)
        assertEqualScientificValue(4(UsTonForce), 2(USTonSquareInch) * 2(SquareInch), round = 32)
        assertEqualScientificValue(4(UsTonForce), 2(SquareInch) * 2(USTonSquareInch), round = 32)
        assertEqualScientificValue(4(UsTonForce), 2(USTonSquareFoot) * 2(SquareFoot), round = 32)
        assertEqualScientificValue(4(UsTonForce), 2(SquareFoot) * 2(USTonSquareFoot), round = 32)
        assertEqualScientificValue(4(ImperialTonForce), 2(ImperialTonSquareInch) * 2(SquareInch), round = 32)
        assertEqualScientificValue(4(ImperialTonForce), 2(SquareInch) * 2(ImperialTonSquareInch), round = 32)
        assertEqualScientificValue(4(ImperialTonForce), 2(ImperialTonSquareFoot) * 2(SquareFoot), round = 32)
        assertEqualScientificValue(4(ImperialTonForce), 2(SquareFoot) * 2(ImperialTonSquareFoot), round = 32)
        assertEqualScientificValue(4(PoundForce), 2(PoundSquareFoot).convert(InchOfMercury) * 2(SquareFoot), round = 32)
        assertEqualScientificValue(4(PoundForce), 2(SquareFoot) * 2(PoundSquareFoot).convert(InchOfMercury), round = 32)
        assertEqualScientificValue(4(PoundForce.ukImperial), 2(PoundSquareFoot.ukImperial) * 2(SquareFoot), round = 32)
        assertEqualScientificValue(4(PoundForce.ukImperial), 2(SquareFoot) * 2(PoundSquareFoot.ukImperial), round = 32)
        assertEqualScientificValue(4(PoundForce.usCustomary), 2(PoundSquareFoot.usCustomary) * 2(SquareFoot), round = 32)
        assertEqualScientificValue(4(PoundForce.usCustomary), 2(SquareFoot) * 2(PoundSquareFoot.usCustomary), round = 32)
        assertEqualScientificValue(4(Newton), 2(Pascal) * 2(SquareMeter).convert(SquareFoot), round = 32)
        assertEqualScientificValue(4(Newton), 2(SquareMeter).convert(SquareFoot) * 2(Pascal), round = 32)
    }

    @Test
    fun forceFromSurfaceTensionAndLengthTest() {
        assertEqualScientificValue(4(Newton), 2(Newton per Meter) * 2(Meter))
        assertEqualScientificValue(4(Newton), 2(Meter) * 2(Newton per Meter))
        assertEqualScientificValue(4(PoundForce), 2(PoundForce per Foot) * 2(Foot), round = 32)
        assertEqualScientificValue(4(PoundForce), 2(Foot) * 2(PoundForce per Foot), round = 32)
        assertEqualScientificValue(4(ImperialTonForce), 2(ImperialTonForce per Foot) * 2(Foot), round = 32)
        assertEqualScientificValue(4(ImperialTonForce), 2(Foot) * 2(ImperialTonForce per Foot), round = 32)
        assertEqualScientificValue(4(UsTonForce), 2(UsTonForce per Foot) * 2(Foot), round = 32)
        assertEqualScientificValue(4(UsTonForce), 2(Foot) * 2(UsTonForce per Foot), round = 32)
        assertEqualScientificValue(4(Newton), 2(Newton per Meter) * 2(Meter).convert(Foot), round = 32)
        assertEqualScientificValue(4(Newton), 2(Meter).convert(Foot) * 2(Newton per Meter), round = 32)
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
