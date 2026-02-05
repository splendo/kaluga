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

import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.amountOfSubstance.div
import com.splendo.kaluga.scientific.converter.amountOfSubstance.times
import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.converter.molarVolume.times
import com.splendo.kaluga.scientific.converter.specificVolume.times
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.converter.volumetricFlow.times
import com.splendo.kaluga.scientific.converter.weight.div
import com.splendo.kaluga.scientific.converter.weight.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Acre
import com.splendo.kaluga.scientific.unit.AcreFoot
import com.splendo.kaluga.scientific.unit.AcreInch
import com.splendo.kaluga.scientific.unit.Barye
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
import com.splendo.kaluga.scientific.unit.Decibarye
import com.splendo.kaluga.scientific.unit.Decierg
import com.splendo.kaluga.scientific.unit.Decimeter
import com.splendo.kaluga.scientific.unit.Decimole
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.FootPoundal
import com.splendo.kaluga.scientific.unit.Gigameter
import com.splendo.kaluga.scientific.unit.Hectometer
import com.splendo.kaluga.scientific.unit.Hour
import com.splendo.kaluga.scientific.unit.ImperialGallon
import com.splendo.kaluga.scientific.unit.ImperialStandardGravityAcceleration
import com.splendo.kaluga.scientific.unit.ImperialTon
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
import com.splendo.kaluga.scientific.unit.MolarVolume
import com.splendo.kaluga.scientific.unit.Molarity
import com.splendo.kaluga.scientific.unit.Nanometer
import com.splendo.kaluga.scientific.unit.Pascal
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundSquareFoot
import com.splendo.kaluga.scientific.unit.PoundSquareInch
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
import com.splendo.kaluga.scientific.unit.UsLiquidGallon
import com.splendo.kaluga.scientific.unit.UsTon
import com.splendo.kaluga.scientific.unit.VolumetricFlow
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.Yard
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.test.Test

class VolumeUnitTest {

    @Test
    fun volumeFromAmountOfSubstanceDivMolarityTest() {
        assertEqualScientificValue(1(CubicMeter), 2(Decimole) / 2(Decimole per CubicMeter))
        assertEqualScientificValue(1(CubicFoot), 2(Decimole) / 2(Decimole per CubicFoot), round = 32)
        assertEqualScientificValue(1(ImperialGallon), 2(Decimole) / 2(Decimole per ImperialGallon), round = 32)
        assertEqualScientificValue(1(UsLiquidGallon), 2(Decimole) / 2(Decimole per UsLiquidGallon), round = 32)
        assertEqualScientificValue(
            1(CubicMeter),
            2(Decimole) / 2(Decimole per CubicMeter).convert((Decimole per CubicFoot) as Molarity),
        )
    }

    @Test
    fun volumeFromAreaAndLengthTest() {
        assertEqualScientificValue(4(CubicMeter), 2(Meter) * 2(SquareMeter))
        assertEqualScientificValue(4(CubicMeter), 2(SquareMeter) * 2(Meter))
        assertEqualScientificValue(4(CubicNanometer), 2(Nanometer) * 2(SquareNanometer))
        assertEqualScientificValue(4(CubicNanometer), 2(SquareNanometer) * 2(Nanometer))
        assertEqualScientificValue(4(CubicMicrometer), 2(Micrometer) * 2(SquareMicrometer))
        assertEqualScientificValue(4(CubicMicrometer), 2(SquareMicrometer) * 2(Micrometer))
        assertEqualScientificValue(4(CubicMillimeter), 2(Millimeter) * 2(SquareMillimeter))
        assertEqualScientificValue(4(CubicMillimeter), 2(SquareMillimeter) * 2(Millimeter))
        assertEqualScientificValue(4(CubicCentimeter), 2(Centimeter) * 2(SquareCentimeter))
        assertEqualScientificValue(4(CubicCentimeter), 2(SquareCentimeter) * 2(Centimeter))
        assertEqualScientificValue(4(CubicDecimeter), 2(Decimeter) * 2(SquareDecimeter))
        assertEqualScientificValue(4(CubicDecimeter), 2(SquareDecimeter) * 2(Decimeter))
        assertEqualScientificValue(4(CubicDecameter), 2(Decameter) * 2(SquareDecameter))
        assertEqualScientificValue(4(CubicDecameter), 2(SquareDecameter) * 2(Decameter))
        assertEqualScientificValue(4(CubicHectometer), 2(Hectometer) * 2(SquareHectometer))
        assertEqualScientificValue(4(CubicHectometer), 2(SquareHectometer) * 2(Hectometer))
        assertEqualScientificValue(4(CubicKilometer), 2(Kilometer) * 2(SquareKilometer))
        assertEqualScientificValue(4(CubicKilometer), 2(SquareKilometer) * 2(Kilometer))
        assertEqualScientificValue(4(CubicMegameter), 2(Megameter) * 2(SquareMegameter))
        assertEqualScientificValue(4(CubicMegameter), 2(SquareMegameter) * 2(Megameter))
        assertEqualScientificValue(4(CubicGigameter), 2(Gigameter) * 2(SquareGigameter))
        assertEqualScientificValue(4(CubicGigameter), 2(SquareGigameter) * 2(Gigameter))
        assertEqualScientificValue(4(CubicMeter), 200(Centimeter) * 2(SquareMeter))
        assertEqualScientificValue(4(CubicMeter), 2(SquareMeter) * 200(Centimeter))

        assertEqualScientificValue(4(CubicInch), 2(Inch) * 2(SquareInch), round = 32)
        assertEqualScientificValue(4(CubicInch), 2(SquareInch) * 2(Inch), round = 32)
        assertEqualScientificValue(4(CubicFoot), 2(Foot) * 2(SquareFoot))
        assertEqualScientificValue(4(CubicFoot), 2(SquareFoot) * 2(Foot))
        assertEqualScientificValue(4(CubicYard), 2(Yard) * 2(SquareYard))
        assertEqualScientificValue(4(CubicYard), 2(SquareYard) * 2(Yard))
        assertEqualScientificValue(4(CubicMile), 2(Mile) * 2(SquareMile))
        assertEqualScientificValue(4(CubicMile), 2(SquareMile) * 2(Mile))

        assertEqualScientificValue(4(AcreInch), 2(Inch) * 2(Acre), round = 32)
        assertEqualScientificValue(4(AcreInch), 2(Acre) * 2(Inch), round = 32)
        assertEqualScientificValue(4(AcreFoot), 2(Foot) * 2(Acre), round = 32)
        assertEqualScientificValue(4(AcreFoot), 2(Acre) * 2(Foot), round = 32)
        assertEqualScientificValue(4(CubicFoot), 24(Inch) * 2(SquareFoot), round = 32)
        assertEqualScientificValue(4(CubicFoot), 2(SquareFoot) * 24(Inch), round = 32)

        assertEqualScientificValue(4(CubicMeter), 2(Meter).convert(Foot) * 2(SquareMeter), round = 32)
        assertEqualScientificValue(4(CubicMeter), 2(SquareMeter) * 2(Meter).convert(Foot), round = 32)
    }

    @Test
    fun volumeFromEnergyAndPressureTest() {
        assertEqualScientificValue(1(CubicCentimeter), 2(Erg) / 2(Barye))
        assertEqualScientificValue(1(CubicCentimeter), 20(Decierg) / 2(Barye))
        assertEqualScientificValue(1(CubicCentimeter), 2(Erg) / 20(Decibarye))
        assertEqualScientificValue(1(CubicCentimeter), 20(Decierg) / 20(Decibarye))
        assertEqualScientificValue(1(CubicMeter), 2(Joule) / 2(Pascal))

        assertEqualScientificValue(
            1(CubicFoot),
            (2.toDecimal() * ImperialStandardGravityAcceleration.decimalValue)(FootPoundal) / 2(PoundSquareFoot),
            round = 32,
        )
        assertEqualScientificValue(1(CubicInch), 2(InchPoundForce) / 2(PoundSquareInch), round = 32)
        assertEqualScientificValue(1(CubicInch), 32(InchOunceForce) / 2(PoundSquareInch), round = 32)
        assertEqualScientificValue(1(CubicFoot), 2(FootPoundForce) / 2(PoundSquareFoot), round = 32)
        assertEqualScientificValue(
            1(CubicFoot),
            2(FootPoundForce).convert(WattHour) / 2(PoundSquareFoot),
            round = 32,
        )
        assertEqualScientificValue(1(CubicFoot.ukImperial), 2(FootPoundForce) / 2(PoundSquareFoot.ukImperial), round = 32)
        assertEqualScientificValue(
            1(CubicFoot.ukImperial),
            2(FootPoundForce).convert(WattHour) / 2(PoundSquareFoot.ukImperial),
            round = 32,
        )
        assertEqualScientificValue(1(CubicFoot.usCustomary), 2(FootPoundForce) / 2(PoundSquareFoot.usCustomary), round = 32)
        assertEqualScientificValue(
            1(CubicFoot.usCustomary),
            2(FootPoundForce).convert(WattHour) / 2(PoundSquareFoot.usCustomary),
            round = 32,
        )
        assertEqualScientificValue(1(CubicMeter), 2(Joule) / 2(Pascal).convert(PoundSquareFoot), round = 30)
    }

    @Test
    fun volumeFromMolarVolumeAndAmountOfSubstanceTest() {
        assertEqualScientificValue(4(CubicMeter), 2(Decimole) * 2(CubicMeter per Decimole))
        assertEqualScientificValue(4(CubicMeter), 2(CubicMeter per Decimole) * 2(Decimole))
        assertEqualScientificValue(4(CubicFoot), 2(Decimole) * 2(CubicFoot per Decimole))
        assertEqualScientificValue(4(CubicFoot), 2(CubicFoot per Decimole) * 2(Decimole))
        assertEqualScientificValue(4(ImperialGallon), 2(Decimole) * 2(ImperialGallon per Decimole))
        assertEqualScientificValue(4(ImperialGallon), 2(ImperialGallon per Decimole) * 2(Decimole))
        assertEqualScientificValue(4(UsLiquidGallon), 2(Decimole) * 2(UsLiquidGallon per Decimole))
        assertEqualScientificValue(4(UsLiquidGallon), 2(UsLiquidGallon per Decimole) * 2(Decimole))
        assertEqualScientificValue(
            4(CubicMeter),
            2(Decimole) * 2(CubicMeter per Decimole).convert((CubicFoot per Decimole) as MolarVolume),
            round = 32,
        )
        assertEqualScientificValue(
            4(CubicMeter),
            2(CubicMeter per Decimole).convert((CubicFoot per Decimole) as MolarVolume) * 2(Decimole),
            round = 32,
        )
    }

    @Test
    fun volumeFromSpecificVolumeAndWeightTest() {
        assertEqualScientificValue(4(CubicMeter), 2(Kilogram) * 2(CubicMeter per Kilogram))
        assertEqualScientificValue(4(CubicMeter), 2(CubicMeter per Kilogram) * 2(Kilogram))
        assertEqualScientificValue(4(CubicFoot), 2(Pound) * 2(CubicFoot per Pound), round = 30)
        assertEqualScientificValue(4(CubicFoot), 2(CubicFoot per Pound) * 2(Pound), round = 30)
        assertEqualScientificValue(4(CubicFoot), 2(Pound.ukImperial) * 2(CubicFoot per Pound), round = 30)
        assertEqualScientificValue(4(CubicFoot), 2(CubicFoot per Pound) * 2(Pound.ukImperial), round = 30)
        assertEqualScientificValue(4(CubicFoot), 2(Pound.usCustomary) * 2(CubicFoot per Pound), round = 30)
        assertEqualScientificValue(4(CubicFoot), 2(CubicFoot per Pound) * 2(Pound.usCustomary), round = 30)
        assertEqualScientificValue(4(ImperialGallon), 2(Pound) * 2(ImperialGallon per Pound), round = 30)
        assertEqualScientificValue(4(ImperialGallon), 2(ImperialGallon per Pound) * 2(Pound), round = 30)
        assertEqualScientificValue(
            4(ImperialGallon),
            2(ImperialTon) * 2(ImperialGallon per ImperialTon),
            round = 30,
        )
        assertEqualScientificValue(
            4(ImperialGallon),
            2(ImperialGallon per ImperialTon) * 2(ImperialTon),
            round = 30,
        )
        assertEqualScientificValue(4(UsLiquidGallon), 2(Pound) * 2(UsLiquidGallon per Pound), round = 30)
        assertEqualScientificValue(4(UsLiquidGallon), 2(UsLiquidGallon per Pound) * 2(Pound), round = 30)
        assertEqualScientificValue(4(UsLiquidGallon), 2(UsTon) * 2(UsLiquidGallon per UsTon), round = 30)
        assertEqualScientificValue(4(UsLiquidGallon), 2(UsLiquidGallon per UsTon) * 2(UsTon), round = 30)
        assertEqualScientificValue(
            4(CubicMeter),
            2(Kilogram).convert(Pound) * 2(CubicMeter per Kilogram),
            round = 30,
        )
        assertEqualScientificValue(
            4(CubicMeter),
            2(CubicMeter per Kilogram) * 2(Kilogram).convert(Pound),
            round = 30,
        )
    }

    @Test
    fun volumeFromVolumetricFlowAndTimeTest() {
        assertEqualScientificValue(4(CubicMeter), 2(CubicMeter per Hour) * 2(Hour), round = 32)
        assertEqualScientificValue(4(CubicMeter), 2(Hour) * 2(CubicMeter per Hour), round = 32)
        assertEqualScientificValue(4(CubicFoot), 2(CubicFoot per Hour) * 2(Hour), round = 32)
        assertEqualScientificValue(4(CubicFoot), 2(Hour) * 2(CubicFoot per Hour), round = 32)
        assertEqualScientificValue(4(ImperialGallon), 2(ImperialGallon per Hour) * 2(Hour), round = 32)
        assertEqualScientificValue(4(ImperialGallon), 2(Hour) * 2(ImperialGallon per Hour), round = 32)
        assertEqualScientificValue(4(UsLiquidGallon), 2(UsLiquidGallon per Hour) * 2(Hour), round = 32)
        assertEqualScientificValue(4(UsLiquidGallon), 2(Hour) * 2(UsLiquidGallon per Hour), round = 32)
        assertEqualScientificValue(
            4(CubicMeter),
            2((CubicMeter per Hour) as VolumetricFlow) * 2(Hour),
            round = 32,
        )
        assertEqualScientificValue(
            4(CubicMeter),
            2(Hour) * 2((CubicMeter per Hour) as VolumetricFlow),
            round = 32,
        )
    }

    @Test
    fun volumeFromWeightAndDensityTest() {
        assertEqualScientificValue(1(CubicMeter), 2(Kilogram) / 2(Kilogram per CubicMeter))
        assertEqualScientificValue(1(CubicFoot), 2(Pound) / 2(Pound per CubicFoot), round = 32)
        assertEqualScientificValue(1(CubicFoot), 2(Pound.ukImperial) / 2(Pound per CubicFoot), round = 32)
        assertEqualScientificValue(1(CubicFoot), 2(Pound.usCustomary) / 2(Pound per CubicFoot), round = 32)
        assertEqualScientificValue(1(ImperialGallon), 2(Pound) / 2(Pound per ImperialGallon), round = 32)
        assertEqualScientificValue(
            1(ImperialGallon),
            2(ImperialTon) / 2(ImperialTon per ImperialGallon),
            round = 32,
        )
        assertEqualScientificValue(1(UsLiquidGallon), 2(Pound) / 2(Pound per UsLiquidGallon), round = 32)
        assertEqualScientificValue(1(UsLiquidGallon), 2(UsTon) / 2(UsTon per UsLiquidGallon), round = 32)
        assertEqualScientificValue(
            1(CubicMeter),
            2(Kilogram).convert(Pound) / 2(Kilogram per CubicMeter),
            round = 30,
        )
    }
}
