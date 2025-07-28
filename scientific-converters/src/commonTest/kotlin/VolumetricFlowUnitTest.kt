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

import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.massFlowRate.div
import com.splendo.kaluga.scientific.converter.power.div
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.converter.volumetricFlux.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.CubicCentimeter
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicInch
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Decibarye
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.Horsepower
import com.splendo.kaluga.scientific.unit.ImperialGallon
import com.splendo.kaluga.scientific.unit.ImperialTonSquareInch
import com.splendo.kaluga.scientific.unit.InchOunceForce
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.KiloPoundSquareInch
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.KipSquareInch
import com.splendo.kaluga.scientific.unit.Minute
import com.splendo.kaluga.scientific.unit.OunceSquareInch
import com.splendo.kaluga.scientific.unit.Pascal
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundSquareFoot
import com.splendo.kaluga.scientific.unit.PoundSquareInch
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.USTonSquareInch
import com.splendo.kaluga.scientific.unit.UsLiquidGallon
import com.splendo.kaluga.scientific.unit.Volume
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.test.Test

class VolumetricFlowUnitTest {

    @Test
    fun volumetricFlowFromVolumeAndTimeTest() {
        assertEqualScientificValue(1(CubicMeter per Second), 2(CubicMeter) / 2(Second))
        assertEqualScientificValue(1(CubicFoot per Second), 2(CubicFoot) / 2(Second))
        assertEqualScientificValue(1(ImperialGallon per Second), 2(ImperialGallon) / 2(Second))
        assertEqualScientificValue(1(UsLiquidGallon per Second), 2(UsLiquidGallon) / 2(Second))
        assertEqualScientificValue(
            1(CubicMeter per Second),
            2(CubicMeter).convert(CubicFoot as Volume) / 2(Second),
            round = 32,
        )
    }

    @Test
    fun volumetricFlowFromVolumetricFluxAndAreaTest() {
        assertEqualScientificValue(
            4(CubicMeter per Second),
            2((CubicMeter per Second) per SquareMeter) * 2(SquareMeter),
        )
        assertEqualScientificValue(
            4(CubicMeter per Second),
            2(SquareMeter) * 2((CubicMeter per Second) per SquareMeter),
        )
        assertEqualScientificValue(
            4(CubicFoot per Second),
            2((CubicFoot per Second) per SquareFoot) * 2(SquareFoot),
            round = 32,
        )
        assertEqualScientificValue(
            4(CubicFoot per Second),
            2(SquareFoot) * 2((CubicFoot per Second) per SquareFoot),
            round = 32,
        )
        assertEqualScientificValue(
            4(ImperialGallon per Second),
            2((ImperialGallon per Second) per SquareFoot) * 2(SquareFoot),
            round = 32,
        )
        assertEqualScientificValue(
            4(ImperialGallon per Second),
            2(SquareFoot) * 2((ImperialGallon per Second) per SquareFoot),
            round = 32,
        )
        assertEqualScientificValue(
            4(UsLiquidGallon per Second),
            2((UsLiquidGallon per Second) per SquareFoot) * 2(SquareFoot),
            round = 32,
        )
        assertEqualScientificValue(
            4(UsLiquidGallon per Second),
            2(SquareFoot) * 2((UsLiquidGallon per Second) per SquareFoot),
            round = 32,
        )
        assertEqualScientificValue(
            4(CubicMeter per Second),
            2((CubicMeter per Second) per SquareMeter) * 2(SquareMeter).convert(SquareFoot),
            round = 32,
        )
        assertEqualScientificValue(
            4(CubicMeter per Second),
            2(SquareMeter).convert(SquareFoot) * 2((CubicMeter per Second) per SquareMeter),
            round = 32,
        )
    }

    @Test
    fun densityFromMassFlowRateAndDensityFlowTest() {
        assertEqualScientificValue(1(CubicMeter per Minute), 2(Kilogram per Minute) / 2(Kilogram per CubicMeter), round = 32)
        assertEqualScientificValue(1(CubicFoot per Minute), 2(Pound per Minute) / 2(Pound per CubicFoot), round = 32)
        assertEqualScientificValue(
            1(CubicFoot.ukImperial per Minute),
            2(Pound per Minute) / 2(Pound.ukImperial per CubicFoot),
            round = 32,
        )
        assertEqualScientificValue(
            1(CubicFoot.usCustomary per Minute),
            2(Pound per Minute) / 2(Pound.usCustomary per CubicFoot),
            round = 32,
        )
        assertEqualScientificValue(
            1(CubicFoot per Minute),
            2(Pound.ukImperial per Minute) / 2(Pound per CubicFoot),
            round = 32,
        )
        assertEqualScientificValue(
            1(CubicFoot.ukImperial per Minute),
            2(Pound.ukImperial per Minute) / 2(Pound.ukImperial per CubicFoot),
            round = 32,
        )
        assertEqualScientificValue(
            1(CubicFoot per Minute),
            2(Pound.usCustomary per Minute) / 2(Pound per CubicFoot),
            round = 32,
        )
        assertEqualScientificValue(
            1(CubicFoot.usCustomary per Minute),
            2(Pound.usCustomary per Minute) / 2(Pound.usCustomary per CubicFoot),
            round = 32,
        )
        assertEqualScientificValue(
            1(CubicMeter per Second),
            2(Kilogram per Second) / 2(Kilogram per CubicMeter).convert(Pound per CubicFoot),
            round = 30,
        )
    }

    @Test
    fun volumetricFlowFromPowerAndPressureTest() {
        assertEqualScientificValue(1(CubicCentimeter per Minute), 2(Erg per Minute) / 2(Barye), round = 32)
        assertEqualScientificValue(1(CubicCentimeter per Minute), 2(Erg per Minute) / 20(Decibarye), round = 32)
        assertEqualScientificValue(1(CubicInch per Minute), 2(InchPoundForce per Minute) / 2(PoundSquareInch), round = 32)
        assertEqualScientificValue(1(CubicInch per Minute), 2(InchOunceForce per Minute) / 2(OunceSquareInch), round = 32)
        assertEqualScientificValue("0.001".toDecimal()(CubicInch per Minute), 2(InchPoundForce per Minute) / 2(KiloPoundSquareInch), round = 32)
        assertEqualScientificValue("0.001".toDecimal()(CubicInch.usCustomary per Minute), 2(InchPoundForce per Minute) / 2(KipSquareInch), round = 32)
        assertEqualScientificValue("5.0e-4".toDecimal()(CubicInch.usCustomary per Minute), 2(InchPoundForce per Minute) / 2(USTonSquareInch), round = 32)
        assertEqualScientificValue(
            ("5.0e-4".toDecimal() / "1.12".toDecimal())(CubicInch.ukImperial per Minute),
            2(InchPoundForce per Minute) / 2(ImperialTonSquareInch),
            round = 32,
        )
        assertEqualScientificValue(1(CubicFoot per Minute), 2(FootPoundForce per Minute) / 2(PoundSquareFoot), round = 32)
        assertEqualScientificValue(1(CubicFoot.ukImperial per Minute), 2(FootPoundForce per Minute) / 2(PoundSquareFoot.ukImperial), round = 32)
        assertEqualScientificValue(1(CubicFoot.usCustomary per Minute), 2(FootPoundForce per Minute) / 2(PoundSquareFoot.usCustomary), round = 32)
        assertEqualScientificValue(6600(CubicInch per Second), 2(Horsepower) / 2(PoundSquareInch), round = 31)
        assertEqualScientificValue(105600(CubicInch per Second), 2(Horsepower) / 2(OunceSquareInch), round = 30)
        assertEqualScientificValue("6.6".toDecimal()(CubicInch per Second), 2(Horsepower) / 2(KiloPoundSquareInch))
        assertEqualScientificValue("6.6".toDecimal()(CubicInch.usCustomary per Second), 2(Horsepower) / 2(KipSquareInch))
        assertEqualScientificValue("3.3".toDecimal()(CubicInch.usCustomary per Second), 2(Horsepower) / 2(USTonSquareInch))
        assertEqualScientificValue(("3.3".toDecimal() / "1.12".toDecimal())(CubicInch.ukImperial per Second), 2(Horsepower) / 2(ImperialTonSquareInch), round = 16)
        assertEqualScientificValue(550(CubicFoot per Second), 2(Horsepower) / 2(PoundSquareFoot), round = 31)
        assertEqualScientificValue(550(CubicFoot.ukImperial per Second), 2(Horsepower) / 2(PoundSquareFoot.ukImperial), round = 31)
        assertEqualScientificValue(550(CubicFoot.usCustomary per Second), 2(Horsepower) / 2(PoundSquareFoot.usCustomary), round = 31)
        assertEqualScientificValue(1(CubicMeter per Second), 2(Watt) / 2(Pascal))
    }
}
