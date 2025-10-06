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
import com.splendo.kaluga.scientific.converter.area.div
import com.splendo.kaluga.scientific.converter.areaDensity.div
import com.splendo.kaluga.scientific.converter.areaDensity.times
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.converter.length.height
import com.splendo.kaluga.scientific.converter.linearMassDensity.div
import com.splendo.kaluga.scientific.converter.specificVolume.times
import com.splendo.kaluga.scientific.converter.speed.times
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.converter.weight.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Acre
import com.splendo.kaluga.scientific.unit.AcreFoot
import com.splendo.kaluga.scientific.unit.AcreInch
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.CubicCentimeter
import com.splendo.kaluga.scientific.unit.CubicDecameter
import com.splendo.kaluga.scientific.unit.CubicDecimeter
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicGigameter
import com.splendo.kaluga.scientific.unit.CubicHectometer
import com.splendo.kaluga.scientific.unit.CubicInch
import com.splendo.kaluga.scientific.unit.CubicKilometer
import com.splendo.kaluga.scientific.unit.CubicMegameter
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.CubicMicrometer
import com.splendo.kaluga.scientific.unit.CubicMile
import com.splendo.kaluga.scientific.unit.CubicMillimeter
import com.splendo.kaluga.scientific.unit.CubicNanometer
import com.splendo.kaluga.scientific.unit.CubicYard
import com.splendo.kaluga.scientific.unit.Decameter
import com.splendo.kaluga.scientific.unit.Decidyne
import com.splendo.kaluga.scientific.unit.Decierg
import com.splendo.kaluga.scientific.unit.Decimeter
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.FootPoundal
import com.splendo.kaluga.scientific.unit.Gigameter
import com.splendo.kaluga.scientific.unit.GrainForce
import com.splendo.kaluga.scientific.unit.Hectometer
import com.splendo.kaluga.scientific.unit.ImperialTonForce
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.InchOunceForce
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Kilometer
import com.splendo.kaluga.scientific.unit.Megameter
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Micrometer
import com.splendo.kaluga.scientific.unit.Mile
import com.splendo.kaluga.scientific.unit.Millimeter
import com.splendo.kaluga.scientific.unit.Nanometer
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Poundal
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.SquareDecameter
import com.splendo.kaluga.scientific.unit.SquareDecimeter
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareGigameter
import com.splendo.kaluga.scientific.unit.SquareHectometer
import com.splendo.kaluga.scientific.unit.SquareInch
import com.splendo.kaluga.scientific.unit.SquareKilometer
import com.splendo.kaluga.scientific.unit.SquareMegameter
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.SquareMicrometer
import com.splendo.kaluga.scientific.unit.SquareMile
import com.splendo.kaluga.scientific.unit.SquareMillimeter
import com.splendo.kaluga.scientific.unit.SquareNanometer
import com.splendo.kaluga.scientific.unit.SquareYard
import com.splendo.kaluga.scientific.unit.UsTonForce
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.Yard
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.test.Test

class LengthUnitTest {

    @Test
    fun widthFromAreaAndLengthTest() {
        assertEqualScientificValue(1(Meter), 2(SquareMeter) / 2(Meter))
        assertEqualScientificValue(1(Nanometer), 2(SquareNanometer) / 2(Nanometer))
        assertEqualScientificValue(1(Micrometer), 2(SquareMicrometer) / 2(Micrometer))
        assertEqualScientificValue(1(Millimeter), 2(SquareMillimeter) / 2(Millimeter))
        assertEqualScientificValue(1(Centimeter), 2(SquareCentimeter) / 2(Centimeter))
        assertEqualScientificValue(1(Decimeter), 2(SquareDecimeter) / 2(Decimeter))
        assertEqualScientificValue(1(Decameter), 2(SquareDecameter) / 2(Decameter))
        assertEqualScientificValue(1(Hectometer), 2(SquareHectometer) / 2(Hectometer))
        assertEqualScientificValue(1(Kilometer), 2(SquareKilometer) / 2(Kilometer))
        assertEqualScientificValue(1(Megameter), 2(SquareMegameter) / 2(Megameter))
        assertEqualScientificValue(1(Gigameter), 2(SquareGigameter) / 2(Gigameter))
        assertEqualScientificValue(
            1(Meter),
            2(SquareMeter).convert(SquareCentimeter) / 2(Meter).convert(Hectometer),
        )

        assertEqualScientificValue(1(Inch), 2(SquareInch) / 2(Inch), round = 32)
        assertEqualScientificValue(1(Foot), 2(SquareFoot) / 2(Foot))
        assertEqualScientificValue(1(Yard), 2(SquareYard) / 2(Yard))
        assertEqualScientificValue(1(Mile), 2(SquareMile) / 2(Mile))
        assertEqualScientificValue(1(Foot), 2(SquareFoot).convert(SquareInch) / 2(Foot).convert(Yard))
        assertEqualScientificValue(1(Meter), 2(SquareMeter).convert(SquareCentimeter) / 2(Meter).convert(Yard), round = 32)
    }

    @Test
    fun lengthFromAreaDensityAndDensityTest() {
        assertEqualScientificValue(1(Meter), 2(Kilogram per SquareMeter) / 2(Kilogram per CubicMeter))
        assertEqualScientificValue(1(Foot), 2(Pound per SquareFoot) / 2(Pound per CubicFoot), round = 32)
        assertEqualScientificValue(1(Foot), 2(Pound.ukImperial per SquareFoot) / 2(Pound per CubicFoot), round = 32)
        assertEqualScientificValue(1(Foot), 2(Pound.usCustomary per SquareFoot) / 2(Pound per CubicFoot), round = 32)
        assertEqualScientificValue(1(Foot), 2(Pound per SquareFoot) / 2(Pound.ukImperial per CubicFoot), round = 32)
        assertEqualScientificValue(
            1(Foot),
            2(Pound.ukImperial per SquareFoot) / 2(Pound.ukImperial per CubicFoot),
            round = 32,
        )
        assertEqualScientificValue(1(Foot), 2(Pound per SquareFoot) / 2(Pound.usCustomary per CubicFoot), round = 32)
        assertEqualScientificValue(
            1(Foot),
            2(Pound.usCustomary per SquareFoot) / 2(Pound.usCustomary per CubicFoot),
            round = 32,
        )
        assertEqualScientificValue(
            1(Meter),
            2(Kilogram per SquareMeter) / 2(Kilogram per CubicMeter).convert(Pound per CubicFoot),
            round = 30,
        )
    }

    @Test
    fun lengthFromAreaDensityAndSpecificVolumeTest() {
        assertEqualScientificValue(4(Meter), 2(CubicMeter per Kilogram) * 2(Kilogram per SquareMeter))
        assertEqualScientificValue(4(Foot), 2(CubicFoot per Pound) * 2(Pound per SquareFoot), round = 30)
        assertEqualScientificValue(4(Foot), 2(CubicFoot per Pound) * 2(Pound.ukImperial per SquareFoot), round = 30)
        assertEqualScientificValue(4(Foot), 2(CubicFoot per Pound) * 2(Pound.usCustomary per SquareFoot), round = 30)
        assertEqualScientificValue(4(Foot), 2(CubicFoot per Pound.ukImperial) * 2(Pound per SquareFoot), round = 30)
        assertEqualScientificValue(
            4(Foot),
            2(CubicFoot per Pound.ukImperial) * 2(Pound.ukImperial per SquareFoot),
            round = 30,
        )
        assertEqualScientificValue(4(Foot), 2(CubicFoot per Pound.usCustomary) * 2(Pound per SquareFoot), round = 30)
        assertEqualScientificValue(
            4(Foot),
            2(CubicFoot per Pound.usCustomary) * 2(Pound.usCustomary per SquareFoot),
            round = 30,
        )
        assertEqualScientificValue(
            4(Meter),
            2(CubicMeter per Kilogram) * 2(Kilogram per SquareMeter).convert(Pound per SquareFoot),
            round = 30,
        )

        assertEqualScientificValue(4(Meter), 2(Kilogram per SquareMeter) * 2(CubicMeter per Kilogram))
        assertEqualScientificValue(4(Foot), 2(Pound per SquareFoot) * 2(CubicFoot per Pound), round = 30)
        assertEqualScientificValue(4(Foot), 2(Pound.ukImperial per SquareFoot) * 2(CubicFoot per Pound), round = 30)
        assertEqualScientificValue(4(Foot), 2(Pound.usCustomary per SquareFoot) * 2(CubicFoot per Pound), round = 30)
        assertEqualScientificValue(4(Foot), 2(Pound per SquareFoot) * 2(CubicFoot per Pound.ukImperial), round = 30)
        assertEqualScientificValue(
            4(Foot),
            2(Pound.ukImperial per SquareFoot) * 2(CubicFoot per Pound.ukImperial),
            round = 30,
        )
        assertEqualScientificValue(4(Foot), 2(Pound per SquareFoot) * 2(CubicFoot per Pound.usCustomary), round = 30)
        assertEqualScientificValue(
            4(Foot),
            2(Pound.usCustomary per SquareFoot) * 2(CubicFoot per Pound.usCustomary),
            round = 30,
        )
        assertEqualScientificValue(
            4(Meter),
            2(Kilogram per SquareMeter).convert(Pound per SquareFoot) * 2(CubicMeter per Kilogram),
            round = 30,
        )
    }

    @Test
    fun lengthFromEnergyAndForceTest() {
        assertEqualScientificValue(1(Meter), 2(Joule) / 2(Newton))
        assertEqualScientificValue(1(Centimeter), 2(Erg) / 2(Dyne))
        assertEqualScientificValue(1(Centimeter), 20(Decierg) / 2(Dyne))
        assertEqualScientificValue(1(Centimeter), 2(Erg) / 20(Decidyne))
        assertEqualScientificValue(1(Centimeter), 20(Decierg) / 20(Decidyne))
        assertEqualScientificValue(1(Foot), 2(FootPoundal) / 2(Poundal))
        assertEqualScientificValue(1(Foot), 2(FootPoundForce) / 2(PoundForce))
        assertEqualScientificValue(1(Inch), 2(InchPoundForce) / 2(PoundForce), round = 32)
        assertEqualScientificValue(1(Inch), 2(InchOunceForce) / 2(OunceForce), round = 32)
        assertEqualScientificValue(1(Foot), 2(FootPoundForce) / 2(PoundForce).convert(GrainForce), round = 32)
        assertEqualScientificValue(1(Foot), 2(FootPoundForce) / 2(PoundForce).convert(ImperialTonForce), round = 32)
        assertEqualScientificValue(1(Foot), 2(FootPoundForce) / 2(PoundForce).convert(UsTonForce), round = 32)
        assertEqualScientificValue(1(Foot), 2(FootPoundForce).convert(WattHour) / 2(PoundForce), round = 32)
        assertEqualScientificValue(
            1(Foot),
            2(FootPoundForce).convert(WattHour) / 2(PoundForce).convert(ImperialTonForce),
            round = 32,
        )
        assertEqualScientificValue(
            1(Foot),
            2(FootPoundForce).convert(WattHour) / 2(PoundForce).convert(UsTonForce),
            round = 32,
        )
    }

    @Test
    fun lengthFromForceAndSurfaceTensionTest() {
        assertEqualScientificValue(1(Meter), 2(Newton) / 2(Newton per Meter))
        assertEqualScientificValue(1(Foot), 2(PoundForce) / 2(PoundForce per Foot), round = 32)
        assertEqualScientificValue(1(Foot), 2(PoundForce.ukImperial) / 2(PoundForce per Foot), round = 32)
        assertEqualScientificValue(1(Foot), 2(PoundForce.usCustomary) / 2(PoundForce per Foot), round = 32)
        assertEqualScientificValue(1(Foot), 2(PoundForce) / 2(PoundForce.ukImperial per Foot), round = 32)
        assertEqualScientificValue(
            1(Foot),
            2(PoundForce.ukImperial) / 2(PoundForce.ukImperial per Foot),
            round = 32,
        )
        assertEqualScientificValue(1(Foot), 2(PoundForce) / 2(PoundForce.usCustomary per Foot), round = 32)
        assertEqualScientificValue(1(Foot), 2(PoundForce.usCustomary) / 2(PoundForce per Foot), round = 32)
        assertEqualScientificValue(
            1(Meter),
            2(Newton) / 2(Newton per Meter).convert(PoundForce per Foot),
            round = 30,
        )
    }

    @Test
    fun lengthFromLinearMassDensityAndDensityTest() {
        assertEqualScientificValue(1(Meter), 2(Kilogram per Meter) / 2(Kilogram per SquareMeter))
        assertEqualScientificValue(1(Foot), 2(Pound per Foot) / 2(Pound per SquareFoot))
        assertEqualScientificValue(1(Foot), 2(Pound per Foot) / 2(Pound.ukImperial per SquareFoot))
        assertEqualScientificValue(1(Foot), 2(Pound per Foot) / 2(Pound.usCustomary per SquareFoot))
        assertEqualScientificValue(1(Foot), 2(Pound.ukImperial per Foot) / 2(Pound per SquareFoot))
        assertEqualScientificValue(1(Foot), 2(Pound.ukImperial per Foot) / 2(Pound.ukImperial per SquareFoot))
        assertEqualScientificValue(1(Foot), 2(Pound.usCustomary per Foot) / 2(Pound per SquareFoot))
        assertEqualScientificValue(1(Foot), 2(Pound.usCustomary per Foot) / 2(Pound.usCustomary per SquareFoot))
        assertEqualScientificValue(
            1(Meter),
            2(Kilogram per Meter) / 2(Kilogram per SquareMeter).convert(Pound per SquareFoot),
            round = 30,
        )
    }

    @Test
    fun distanceFromSpeedAndTimeTest() {
        assertEqualScientificValue(4(Meter), 2(Meter per Second) * 2(Second))
        assertEqualScientificValue(4(Meter), 2(Second) * 2(Meter per Second))
        assertEqualScientificValue(4(Foot), 2(Foot per Second) * 2(Second))
        assertEqualScientificValue(4(Foot), 2(Second) * 2(Foot per Second))
    }

    @Test
    fun heightFromVolumeLengthAndWidthTest() {
        assertEqualScientificValue(2(Meter), 8(CubicMeter) / 4(SquareMeter))
        assertEqualScientificValue(2(Nanometer), 8(CubicNanometer) / 4(SquareNanometer))
        assertEqualScientificValue(2(Micrometer), 8(CubicMicrometer) / 4(SquareMicrometer))
        assertEqualScientificValue(2(Millimeter), 8(CubicMillimeter) / 4(SquareMillimeter))
        assertEqualScientificValue(2(Centimeter), 8(CubicCentimeter) / 4(SquareCentimeter))
        assertEqualScientificValue(2(Decimeter), 8(CubicDecimeter) / 4(SquareDecimeter))
        assertEqualScientificValue(2(Decameter), 8(CubicDecameter) / 4(SquareDecameter))
        assertEqualScientificValue(2(Hectometer), 8(CubicHectometer) / 4(SquareHectometer))
        assertEqualScientificValue(2(Kilometer), 8(CubicKilometer) / 4(SquareKilometer))
        assertEqualScientificValue(2(Megameter), 8(CubicMegameter) / 4(SquareMegameter))
        assertEqualScientificValue(2(Gigameter), 8(CubicGigameter) / 4(SquareGigameter))
        assertEqualScientificValue(
            2(Meter),
            8(CubicMeter).convert(CubicDecameter) / 4(SquareMeter).convert(SquareDecimeter),
        )
        assertEqualScientificValue(2(Meter), Meter.height(8(CubicMeter), 2(Meter), 2(Meter)))

        assertEqualScientificValue(2(Inch), 8(CubicInch) / 4(SquareInch), round = 32)
        assertEqualScientificValue(2(Foot), 8(CubicFoot) / 4(SquareFoot))
        assertEqualScientificValue(2(Yard), 8(CubicYard) / 4(SquareYard))
        assertEqualScientificValue(2(Mile), 8(CubicMile) / 4(SquareMile))
        assertEqualScientificValue(2(Inch), 8(AcreInch) / 4(Acre), round = 32)
        assertEqualScientificValue(2(Foot), 8(AcreFoot) / 4(Acre), round = 32)
        assertEqualScientificValue(2(Foot), 8(CubicFoot).convert(CubicInch) / 4(SquareFoot).convert(SquareYard), round = 32)
        assertEqualScientificValue(2(Foot), 8(CubicFoot.ukImperial) / 4(SquareFoot), round = 32)
        assertEqualScientificValue(2(Foot), 8(CubicFoot.usCustomary) / 4(SquareFoot), round = 32)
        assertEqualScientificValue(
            2(Meter),
            8(CubicMeter).convert(CubicDecameter) / 4(SquareMeter).convert(SquareInch),
            round = 32,
        )
    }

    @Test
    fun lengthFromWeightAndLinearMassDensityTest() {
        assertEqualScientificValue(1(Meter), 2(Kilogram) / 2(Kilogram per Meter))
        assertEqualScientificValue(1(Foot), 2(Pound) / 2(Pound per Foot), round = 32)
        assertEqualScientificValue(1(Foot), 2(Pound.ukImperial) / 2(Pound per Foot), round = 32)
        assertEqualScientificValue(1(Foot), 2(Pound.usCustomary) / 2(Pound per Foot), round = 32)
        assertEqualScientificValue(1(Foot), 2(Pound) / 2(Pound.ukImperial per Foot), round = 32)
        assertEqualScientificValue(1(Foot), 2(Pound.ukImperial) / 2(Pound.ukImperial per Foot), round = 32)
        assertEqualScientificValue(1(Foot), 2(Pound) / 2(Pound.usCustomary per Foot), round = 32)
        assertEqualScientificValue(1(Foot), 2(Pound.usCustomary) / 2(Pound.usCustomary per Foot), round = 32)
        assertEqualScientificValue(
            1(Meter),
            2(Kilogram) / 2(Kilogram per Meter).convert(Pound per Foot),
            round = 30,
        )
    }
}
