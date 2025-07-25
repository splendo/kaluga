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
        )
        assertEqualScientificValue(
            4(CubicFoot per Second),
            2(SquareFoot) * 2((CubicFoot per Second) per SquareFoot),
        )
        assertEqualScientificValue(
            4(ImperialGallon per Second),
            2((ImperialGallon per Second) per SquareFoot) * 2(SquareFoot),
        )
        assertEqualScientificValue(
            4(ImperialGallon per Second),
            2(SquareFoot) * 2((ImperialGallon per Second) per SquareFoot),
        )
        assertEqualScientificValue(
            4(UsLiquidGallon per Second),
            2((UsLiquidGallon per Second) per SquareFoot) * 2(SquareFoot),
        )
        assertEqualScientificValue(
            4(UsLiquidGallon per Second),
            2(SquareFoot) * 2((UsLiquidGallon per Second) per SquareFoot),
        )
        assertEqualScientificValue(
            4(CubicMeter per Second),
            2((CubicMeter per Second) per SquareMeter) * 2(SquareMeter).convert(SquareFoot),
        )
        assertEqualScientificValue(
            4(CubicMeter per Second),
            2(SquareMeter).convert(SquareFoot) * 2((CubicMeter per Second) per SquareMeter),
        )
    }

    @Test
    fun densityFromMassFlowRateAndDensityFlowTest() {
        assertEqualScientificValue(1(CubicMeter per Minute), 2(Kilogram per Minute) / 2(Kilogram per CubicMeter))
        assertEqualScientificValue(1(CubicFoot per Minute), 2(Pound per Minute) / 2(Pound per CubicFoot))
        assertEqualScientificValue(
            1(CubicFoot.ukImperial per Minute),
            2(Pound per Minute) / 2(Pound.ukImperial per CubicFoot),
        )
        assertEqualScientificValue(
            1(CubicFoot.usCustomary per Minute),
            2(Pound per Minute) / 2(Pound.usCustomary per CubicFoot),
        )
        assertEqualScientificValue(
            1(CubicFoot per Minute),
            2(Pound.ukImperial per Minute) / 2(Pound per CubicFoot),
        )
        assertEqualScientificValue(
            1(CubicFoot.ukImperial per Minute),
            2(Pound.ukImperial per Minute) / 2(Pound.ukImperial per CubicFoot),
        )
        assertEqualScientificValue(
            1(CubicFoot per Minute),
            2(Pound.usCustomary per Minute) / 2(Pound per CubicFoot),
        )
        assertEqualScientificValue(
            1(CubicFoot.usCustomary per Minute),
            2(Pound.usCustomary per Minute) / 2(Pound.usCustomary per CubicFoot),
        )
        assertEqualScientificValue(
            1(CubicMeter per Second),
            2(Kilogram per Second) / 2(Kilogram per CubicMeter).convert(Pound per CubicFoot),
        )
    }

    @Test
    fun volumetricFlowFromPowerAndPressureTest() {
        assertEqualScientificValue(1(CubicCentimeter per Minute), 2(Erg per Minute) / 2(Barye))
        assertEqualScientificValue(1(CubicCentimeter per Minute), 2(Erg per Minute) / 20(Decibarye))
        assertEqualScientificValue(1(CubicInch per Minute), 2(InchPoundForce per Minute) / 2(PoundSquareInch))
        assertEqualScientificValue(1(CubicInch per Minute), 2(InchOunceForce per Minute) / 2(OunceSquareInch))
        assertEqualScientificValue(0.001(CubicInch per Minute), 2(InchPoundForce per Minute) / 2(KiloPoundSquareInch))
        assertEqualScientificValue(0.001(CubicInch.usCustomary per Minute), 2(InchPoundForce per Minute) / 2(KipSquareInch))
        assertEqualScientificValue(5.0e-4(CubicInch.usCustomary per Minute), 2(InchPoundForce per Minute) / 2(USTonSquareInch))
        assertEqualScientificValue(4.46429e-4(CubicInch.ukImperial per Minute), 2(InchPoundForce per Minute) / 2(ImperialTonSquareInch), 5)
        assertEqualScientificValue(1(CubicFoot per Minute), 2(FootPoundForce per Minute) / 2(PoundSquareFoot))
        assertEqualScientificValue(1(CubicFoot.ukImperial per Minute), 2(FootPoundForce per Minute) / 2(PoundSquareFoot.ukImperial))
        assertEqualScientificValue(1(CubicFoot.usCustomary per Minute), 2(FootPoundForce per Minute) / 2(PoundSquareFoot.usCustomary))
        assertEqualScientificValue(6600(CubicInch per Second), 2(Horsepower) / 2(PoundSquareInch))
        assertEqualScientificValue(105600(CubicInch per Second), 2(Horsepower) / 2(OunceSquareInch))
        assertEqualScientificValue(6.6(CubicInch per Second), 2(Horsepower) / 2(KiloPoundSquareInch))
        assertEqualScientificValue(6.6(CubicInch.usCustomary per Second), 2(Horsepower) / 2(KipSquareInch))
        assertEqualScientificValue(3.3(CubicInch.usCustomary per Second), 2(Horsepower) / 2(USTonSquareInch))
        assertEqualScientificValue(2.946429(CubicInch.ukImperial per Second), 2(Horsepower) / 2(ImperialTonSquareInch), 6)
        assertEqualScientificValue(550(CubicFoot per Second), 2(Horsepower) / 2(PoundSquareFoot))
        assertEqualScientificValue(550(CubicFoot.ukImperial per Second), 2(Horsepower) / 2(PoundSquareFoot.ukImperial))
        assertEqualScientificValue(550(CubicFoot.usCustomary per Second), 2(Horsepower) / 2(PoundSquareFoot.usCustomary))
        assertEqualScientificValue(1(CubicMeter per Second), 2(Watt) / 2(Pascal))
    }
}
