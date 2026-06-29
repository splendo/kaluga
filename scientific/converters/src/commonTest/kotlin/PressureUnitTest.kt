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
import com.splendo.kaluga.scientific.converter.dynamicViscosity.div
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.converter.power.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.CubicCentimeter
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicInch
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Decidyne
import com.splendo.kaluga.scientific.unit.Decierg
import com.splendo.kaluga.scientific.unit.DynamicViscosity
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.FootPoundal
import com.splendo.kaluga.scientific.unit.GrainForce
import com.splendo.kaluga.scientific.unit.Horsepower
import com.splendo.kaluga.scientific.unit.ImperialStandardGravityAcceleration
import com.splendo.kaluga.scientific.unit.ImperialTonForce
import com.splendo.kaluga.scientific.unit.ImperialTonSquareFoot
import com.splendo.kaluga.scientific.unit.ImperialTonSquareInch
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kip
import com.splendo.kaluga.scientific.unit.KipSquareFoot
import com.splendo.kaluga.scientific.unit.KipSquareInch
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.OunceSquareInch
import com.splendo.kaluga.scientific.unit.Pascal
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.PoundSquareFoot
import com.splendo.kaluga.scientific.unit.PoundSquareInch
import com.splendo.kaluga.scientific.unit.Poundal
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareInch
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.SquareYard
import com.splendo.kaluga.scientific.unit.USTonSquareFoot
import com.splendo.kaluga.scientific.unit.USTonSquareInch
import com.splendo.kaluga.scientific.unit.UsTonForce
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test

class PressureUnitTest {

    @Test
    fun pressureFromDynamicViscosityAndTimeTest() {
        assertEqualScientificValue(1(Pascal), 2(Pascal x Second) / 2(Second))
        assertEqualScientificValue(1(PoundSquareFoot), 2(PoundSquareFoot x Second) / 2(Second), round = 32)
        assertEqualScientificValue(1(USTonSquareFoot), 2(USTonSquareFoot x Second) / 2(Second), round = 32)
        assertEqualScientificValue(
            1(ImperialTonSquareFoot),
            2(ImperialTonSquareFoot x Second) / 2(Second),
            round = 32,
        )
        assertEqualScientificValue(
            1(Pascal),
            2((Pascal x Second) as DynamicViscosity) / 2(Second),
        )
    }

    @Test
    fun pressureFromEnergyAndVolumeTest() {
        assertEqualScientificValue(1(Barye), 2(Erg) / 2(CubicCentimeter))
        assertEqualScientificValue(1(Barye), 20(Decierg) / 2(CubicCentimeter))
        assertEqualScientificValue(1(Pascal), 2(Joule) / 2(CubicMeter))
        assertEqualScientificValue(
            1(PoundSquareFoot),
            (2.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(FootPoundal) / 2(CubicFoot),
            round = 32,
        )
        assertEqualScientificValue(1(PoundSquareFoot), 2(FootPoundForce) / 2(CubicFoot), round = 32)
        assertEqualScientificValue(1(PoundSquareInch), 2(InchPoundForce) / 2(CubicInch), round = 32)
        assertEqualScientificValue(
            1(PoundSquareInch),
            2(InchPoundForce).convert(WattHour) / 2(CubicInch),
            round = 32,
        )
        assertEqualScientificValue(
            1(PoundSquareInch.ukImperial),
            2(InchPoundForce) / 2(CubicInch.ukImperial),
            round = 32,
        )
        assertEqualScientificValue(
            1(PoundSquareInch.usCustomary),
            2(InchPoundForce) / 2(CubicInch.usCustomary),
            round = 32,
        )
        assertEqualScientificValue(
            1(PoundSquareInch.ukImperial),
            2(InchPoundForce).convert(WattHour) / 2(CubicInch.ukImperial),
            round = 32,
        )
        assertEqualScientificValue(
            1(PoundSquareInch.usCustomary),
            2(InchPoundForce).convert(WattHour) / 2(CubicInch.usCustomary),
            round = 32,
        )
        assertEqualScientificValue(1(Pascal), 2(Joule) / 2(CubicMeter).convert(CubicFoot), round = 32)
    }

    @Test
    fun pressureFromForceAndAreaTest() {
        assertEqualScientificValue(1(Barye), 2(Dyne) / 2(SquareCentimeter))
        assertEqualScientificValue(1(Barye), 20(Decidyne) / 2(SquareCentimeter))
        assertEqualScientificValue(1(Pascal), 2(Newton) / 2(SquareMeter))
        assertEqualScientificValue(
            1(PoundSquareFoot),
            (2.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Poundal) / 2(SquareFoot),
            round = 32,
        )
        assertEqualScientificValue(
            1(PoundSquareInch),
            (2.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(Poundal) / 2(SquareInch),
            round = 32,
        )
        assertEqualScientificValue(1(PoundSquareFoot), 2(PoundForce) / 2(SquareFoot), round = 32)
        assertEqualScientificValue(1(PoundSquareInch), 2(PoundForce) / 2(SquareInch), round = 32)
        assertEqualScientificValue(1(OunceSquareInch), 2(OunceForce) / 2(SquareInch), round = 31)
        assertEqualScientificValue(
            1(OunceSquareInch),
            2(OunceForce).convert(GrainForce) / 2(SquareInch),
            round = 31,
        )
        assertEqualScientificValue(1(KipSquareFoot), 2(Kip) / 2(SquareFoot), round = 32)
        assertEqualScientificValue(1(KipSquareInch), 2(Kip) / 2(SquareInch), round = 29)
        assertEqualScientificValue(1(USTonSquareFoot), 2(UsTonForce) / 2(SquareFoot), round = 32)
        assertEqualScientificValue(1(USTonSquareInch), 2(UsTonForce) / 2(SquareInch), round = 29)
        assertEqualScientificValue(1(ImperialTonSquareFoot), 2(ImperialTonForce) / 2(SquareFoot), round = 32)
        assertEqualScientificValue(1(ImperialTonSquareInch), 2(ImperialTonForce) / 2(SquareInch), round = 26)
        assertEqualScientificValue(
            1(PoundSquareInch),
            2(PoundForce) / 2(SquareInch).convert(SquareYard),
            round = 26,
        )
        assertEqualScientificValue(
            1(PoundSquareInch.ukImperial),
            2(PoundForce.ukImperial) / 2(SquareInch),
            round = 26,
        )
        assertEqualScientificValue(
            1(PoundSquareInch.usCustomary),
            2(PoundForce.usCustomary) / 2(SquareInch),
            round = 26,
        )
        assertEqualScientificValue(1(Pascal), 2(Newton) / 2(SquareMeter).convert(SquareFoot), round = 32)
    }

    @Test
    fun pressureFromPowerAndVolumetricFlowTest() {
        assertEqualScientificValue(1(Barye), 2(Erg per Second) / 2(CubicCentimeter per Second))
        assertEqualScientificValue(1(PoundSquareInch), 2(InchPoundForce per Second) / 2(CubicInch per Second), round = 32)
        assertEqualScientificValue(1(PoundSquareInch.ukImperial), 2(InchPoundForce per Second) / 2(CubicInch.ukImperial per Second), round = 32)
        assertEqualScientificValue(1(PoundSquareInch.usCustomary), 2(InchPoundForce per Second) / 2(CubicInch.usCustomary per Second), round = 32)
        assertEqualScientificValue(6600(PoundSquareInch), 2(Horsepower) / 2(CubicInch per Second), round = 24)
        assertEqualScientificValue(6600(PoundSquareInch.ukImperial), 2(Horsepower) / 2(CubicInch.ukImperial per Second), round = 24)
        assertEqualScientificValue(6600(PoundSquareInch.usCustomary), 2(Horsepower) / 2(CubicInch.usCustomary per Second), round = 24)
        assertEqualScientificValue(1(Pascal), 2(Watt) / 2(CubicMeter per Second))
    }
}
