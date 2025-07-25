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
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.converter.kinematicViscosity.times
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.converter.linearMassDensity.div
import com.splendo.kaluga.scientific.converter.linearMassDensity.times
import com.splendo.kaluga.scientific.converter.luminousEnergy.div
import com.splendo.kaluga.scientific.converter.luminousFlux.div
import com.splendo.kaluga.scientific.converter.luminousIntensity.div
import com.splendo.kaluga.scientific.converter.magneticFlux.div
import com.splendo.kaluga.scientific.converter.momentum.div
import com.splendo.kaluga.scientific.converter.specificVolume.times
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.converter.volumetricFlow.div
import com.splendo.kaluga.scientific.converter.weight.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Acre
import com.splendo.kaluga.scientific.unit.AcreFoot
import com.splendo.kaluga.scientific.unit.AcreInch
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.Candela
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
import com.splendo.kaluga.scientific.unit.Decidyne
import com.splendo.kaluga.scientific.unit.Decierg
import com.splendo.kaluga.scientific.unit.Decimeter
import com.splendo.kaluga.scientific.unit.Deciphot
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.FootCandle
import com.splendo.kaluga.scientific.unit.FootLambert
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.Gauss
import com.splendo.kaluga.scientific.unit.Gigameter
import com.splendo.kaluga.scientific.unit.GrainForce
import com.splendo.kaluga.scientific.unit.Hectometer
import com.splendo.kaluga.scientific.unit.ImperialStandardGravityAcceleration
import com.splendo.kaluga.scientific.unit.ImperialTonForce
import com.splendo.kaluga.scientific.unit.ImperialTonSquareFoot
import com.splendo.kaluga.scientific.unit.ImperialTonSquareInch
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.InchOunceForce
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.KiloPoundSquareInch
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Kilometer
import com.splendo.kaluga.scientific.unit.Kip
import com.splendo.kaluga.scientific.unit.KipSquareFoot
import com.splendo.kaluga.scientific.unit.KipSquareInch
import com.splendo.kaluga.scientific.unit.Lambert
import com.splendo.kaluga.scientific.unit.Lumen
import com.splendo.kaluga.scientific.unit.Luminance
import com.splendo.kaluga.scientific.unit.LuminousExposure
import com.splendo.kaluga.scientific.unit.Lux
import com.splendo.kaluga.scientific.unit.Maxwell
import com.splendo.kaluga.scientific.unit.Megameter
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Micrometer
import com.splendo.kaluga.scientific.unit.Mile
import com.splendo.kaluga.scientific.unit.Millimeter
import com.splendo.kaluga.scientific.unit.Nanometer
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.Nit
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.OunceSquareInch
import com.splendo.kaluga.scientific.unit.Pascal
import com.splendo.kaluga.scientific.unit.Phot
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.PoundSquareFoot
import com.splendo.kaluga.scientific.unit.PoundSquareInch
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
import com.splendo.kaluga.scientific.unit.Stilb
import com.splendo.kaluga.scientific.unit.Tesla
import com.splendo.kaluga.scientific.unit.USTonSquareFoot
import com.splendo.kaluga.scientific.unit.USTonSquareInch
import com.splendo.kaluga.scientific.unit.UsTonForce
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.Weber
import com.splendo.kaluga.scientific.unit.Yard
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test

class AreaUnitTest {

    @Test
    fun areaFromEnergyAndSurfaceTensionTest() {
        assertEqualScientificValue(1(SquareMeter), 2(Joule) / 2(Newton per Meter))
        assertEqualScientificValue(1(SquareCentimeter), 2(Erg) / 2(Dyne per Centimeter))
        assertEqualScientificValue(1(SquareCentimeter), 20(Decierg) / 2(Dyne per Centimeter))
        assertEqualScientificValue(1(SquareMeter), 2(Joule).convert(WattHour) / 2(Newton per Meter))
        assertEqualScientificValue(1(SquareInch), 2(InchPoundForce) / 2(PoundForce per Inch))
        assertEqualScientificValue(1(SquareInch), 2(InchPoundForce) / 2(PoundForce.ukImperial per Inch))
        assertEqualScientificValue(1(SquareInch), 2(InchPoundForce) / 2(PoundForce.usCustomary per Inch))
        assertEqualScientificValue(1(SquareInch), 2(InchOunceForce) / 2(OunceForce per Inch))
        assertEqualScientificValue(1(SquareInch), 2(InchOunceForce) / 2(OunceForce.ukImperial per Inch))
        assertEqualScientificValue(1(SquareInch), 2(InchOunceForce) / 2(OunceForce.usCustomary per Inch))
        assertEqualScientificValue(
            1(SquareFoot),
            2(FootPoundForce).convert(WattHour) / 2(PoundForce per Foot),
            5,
        )
        assertEqualScientificValue(
            1(SquareFoot),
            2(FootPoundForce).convert(WattHour) / 2(PoundForce.ukImperial per Foot),
            5,
        )
        assertEqualScientificValue(
            1(SquareFoot),
            2(FootPoundForce).convert(WattHour) / 2(PoundForce.usCustomary per Foot),
            5,
        )
        assertEqualScientificValue(1(SquareFoot), 2(FootPoundForce) / 2(PoundForce per Foot))
        assertEqualScientificValue(1(SquareFoot), 2(FootPoundForce) / 2(PoundForce.ukImperial per Foot))
        assertEqualScientificValue(1(SquareFoot), 2(FootPoundForce) / 2(PoundForce.usCustomary per Foot))
        assertEqualScientificValue(1(SquareMeter), 2(Joule).convert(FootPoundForce) / 2(Newton per Meter))
    }

    @Test
    fun areaFromForceAndPressureTest() {
        assertEqualScientificValue(1(SquareMeter), 2(Newton) / 2(Pascal))
        assertEqualScientificValue(1(SquareCentimeter), 2(Dyne) / 2(Barye))
        assertEqualScientificValue(1(SquareCentimeter), 2(Dyne) / 20(Decibarye))
        assertEqualScientificValue(1(SquareCentimeter), 20(Decidyne) / 2(Barye))
        assertEqualScientificValue(1(SquareCentimeter), 20(Decidyne) / 20(Decibarye))
        assertEqualScientificValue(1(SquareInch), 2(PoundForce) / 2(PoundSquareInch))
        assertEqualScientificValue(1(SquareFoot), 2(PoundForce) / 2(PoundSquareFoot))
        assertEqualScientificValue(1(SquareInch), 2(PoundForce) / 0.002(KiloPoundSquareInch))
        assertEqualScientificValue(1(SquareInch), 2(OunceForce) / 2(OunceSquareInch))
        assertEqualScientificValue(1(SquareInch), 2(Kip) / 2(KipSquareInch))
        assertEqualScientificValue(1(SquareFoot), 2(Kip) / 2(KipSquareFoot))
        assertEqualScientificValue(1(SquareInch), 2(UsTonForce) / 2(USTonSquareInch))
        assertEqualScientificValue(1(SquareFoot), 2(UsTonForce) / 2(USTonSquareFoot))
        assertEqualScientificValue(1(SquareInch), 2(ImperialTonForce) / 2(ImperialTonSquareInch))
        assertEqualScientificValue(1(SquareFoot), 2(ImperialTonForce) / 2(ImperialTonSquareFoot))
        assertEqualScientificValue(1(SquareFoot), 2(PoundForce).convert(GrainForce) / 2(PoundSquareFoot))
        assertEqualScientificValue(
            1(SquareFoot),
            2(PoundForce).convert(GrainForce) / 2(PoundSquareFoot.ukImperial),
        )
        assertEqualScientificValue(
            1(SquareFoot),
            2(PoundForce).convert(GrainForce) / 2(PoundSquareFoot.usCustomary),
        )
        assertEqualScientificValue(
            1(SquareFoot),
            2(PoundForce).convert(GrainForce.ukImperial) / 2(PoundSquareFoot),
        )
        assertEqualScientificValue(
            1(SquareFoot),
            2(PoundForce).convert(GrainForce.ukImperial) / 2(PoundSquareFoot.ukImperial),
        )
        assertEqualScientificValue(
            1(SquareFoot),
            2(PoundForce).convert(GrainForce.usCustomary) / 2(PoundSquareFoot),
        )
        assertEqualScientificValue(
            1(SquareFoot),
            2(PoundForce).convert(GrainForce.usCustomary) / 2(PoundSquareFoot.usCustomary),
        )
        assertEqualScientificValue(1(SquareMeter), 2(Newton).convert(PoundForce) / 2(Pascal))
    }

    @Test
    fun areaFromLengthAndWidthTest() {
        assertEqualScientificValue(4(SquareMeter), 2(Meter) * 2(Meter))
        assertEqualScientificValue(4(SquareNanometer), 2(Nanometer) * 2(Nanometer))
        assertEqualScientificValue(4(SquareMicrometer), 2(Micrometer) * 2(Micrometer))
        assertEqualScientificValue(4(SquareMillimeter), 2(Millimeter) * 2(Millimeter))
        assertEqualScientificValue(4(SquareCentimeter), 2(Centimeter) * 2(Centimeter))
        assertEqualScientificValue(4(SquareDecimeter), 2(Decimeter) * 2(Decimeter))
        assertEqualScientificValue(4(SquareDecameter), 2(Decameter) * 2(Decameter))
        assertEqualScientificValue(4(SquareHectometer), 2(Hectometer) * 2(Hectometer))
        assertEqualScientificValue(4(SquareKilometer), 2(Kilometer) * 2(Kilometer))
        assertEqualScientificValue(4(SquareMegameter), 2(Megameter) * 2(Megameter))
        assertEqualScientificValue(4(SquareGigameter), 2(Gigameter) * 2(Gigameter))
        assertEqualScientificValue(4(SquareMeter), 20(Decimeter) * 0.2(Decameter))

        assertEqualScientificValue(4(SquareInch), 2(Inch) * 2(Inch))
        assertEqualScientificValue(4(SquareFoot), 2(Foot) * 2(Foot))
        assertEqualScientificValue(4(SquareYard), 2(Yard) * 2(Yard))
        assertEqualScientificValue(4(SquareMile), 2(Mile) * 2(Mile))
        assertEqualScientificValue(4(SquareFoot), 2(Foot).convert(Inch) * 2(Foot))
        assertEqualScientificValue(4(SquareMeter), 2(Meter).convert(Foot) * 2(Meter))
    }

    @Test
    fun areaFromLinearMassDensityAndDensityTest() {
        assertEqualScientificValue(1(SquareMeter), 2(Kilogram per Meter) / 2(Kilogram per CubicMeter))
        assertEqualScientificValue(1(SquareFoot), 2(Pound per Foot) / 2(Pound per CubicFoot))
        assertEqualScientificValue(1(SquareFoot), 2(Pound per Foot) / 2(Pound.ukImperial per CubicFoot))
        assertEqualScientificValue(1(SquareFoot), 2(Pound per Foot) / 2(Pound.usCustomary per CubicFoot))
        assertEqualScientificValue(1(SquareFoot), 2(Pound.ukImperial per Foot) / 2(Pound per CubicFoot))
        assertEqualScientificValue(
            1(SquareFoot),
            2(Pound.ukImperial per Foot) / 2(Pound.ukImperial per CubicFoot),
        )
        assertEqualScientificValue(1(SquareFoot), 2(Pound.usCustomary per Foot) / 2(Pound per CubicFoot))
        assertEqualScientificValue(1(SquareFoot), 2(Pound.usCustomary per Foot) / 2(Pound per CubicFoot))
        assertEqualScientificValue(
            1(SquareMeter),
            2(Kilogram per Meter) / 2(Kilogram per CubicMeter).convert(Pound per CubicFoot),
        )
    }

    @Test
    fun areaFromLinearMassDensityAndSpecificVolumeTest() {
        assertEqualScientificValue(4(SquareMeter), 2(Kilogram per Meter) * 2(CubicMeter per Kilogram))
        assertEqualScientificValue(4(SquareMeter), 2(CubicMeter per Kilogram) * 2(Kilogram per Meter))
        assertEqualScientificValue(4(SquareFoot), 2(Pound per Foot) * 2(CubicFoot per Pound))
        assertEqualScientificValue(4(SquareFoot), 2(CubicFoot per Pound) * 2(Pound per Foot))
        assertEqualScientificValue(4(SquareFoot), 2(Pound per Foot) * 2(CubicFoot per Pound.ukImperial))
        assertEqualScientificValue(4(SquareFoot), 2(CubicFoot per Pound.ukImperial) * 2(Pound per Foot))
        assertEqualScientificValue(4(SquareFoot), 2(Pound per Foot) * 2(CubicFoot per Pound.usCustomary))
        assertEqualScientificValue(4(SquareFoot), 2(CubicFoot per Pound.usCustomary) * 2(Pound per Foot))
        assertEqualScientificValue(4(SquareFoot), 2(Pound.ukImperial per Foot) * 2(CubicFoot per Pound))
        assertEqualScientificValue(4(SquareFoot), 2(CubicFoot per Pound) * 2(Pound.ukImperial per Foot))
        assertEqualScientificValue(
            4(SquareFoot),
            2(Pound.ukImperial per Foot) * 2(CubicFoot per Pound.ukImperial),
        )
        assertEqualScientificValue(
            4(SquareFoot),
            2(CubicFoot per Pound.ukImperial) * 2(Pound.ukImperial per Foot),
        )
        assertEqualScientificValue(4(SquareFoot), 2(Pound.usCustomary per Foot) * 2(CubicFoot per Pound))
        assertEqualScientificValue(4(SquareFoot), 2(CubicFoot per Pound) * 2(Pound.usCustomary per Foot))
        assertEqualScientificValue(
            4(SquareFoot),
            2(Pound.usCustomary per Foot) * 2(CubicFoot per Pound.usCustomary),
        )
        assertEqualScientificValue(
            4(SquareFoot),
            2(CubicFoot per Pound.usCustomary) * 2(Pound.usCustomary per Foot),
        )
        assertEqualScientificValue(
            4(SquareMeter),
            2(Kilogram per Meter) * 2(CubicMeter per Kilogram).convert(CubicFoot per Pound),
            5,
        )
        assertEqualScientificValue(
            4(SquareMeter),
            2(CubicMeter per Kilogram).convert(CubicFoot per Pound) * 2(Kilogram per Meter),
            5,
        )
    }

    @Test
    fun areaFromLuminousEnergyAndExposureTest() {
        assertEqualScientificValue(1(SquareMeter), 2(Lumen x Second) / 2(Lux x Second))
        assertEqualScientificValue(1(SquareFoot), 2(Lumen x Second) / 2(FootCandle x Second))
        assertEqualScientificValue(
            1(SquareMeter),
            2(Lumen x Second) / 2(Lux x Second).convert((FootCandle x Second) as LuminousExposure),
        )
    }

    @Test
    fun areaFromFluxAndIlluminanceTest() {
        assertEqualScientificValue(1(SquareCentimeter), 2(Lumen) / 2(Phot))
        assertEqualScientificValue(1(SquareCentimeter), 2(Lumen) / 20(Deciphot))
        assertEqualScientificValue(1(SquareMeter), 2(Lumen) / 2(Lux))
        assertEqualScientificValue(1(SquareFoot), 2(Lumen) / 2(FootCandle))
    }

    @Test
    fun areaFromLuminousIntensityAndLuminanceTest() {
        assertEqualScientificValue(1(SquareMeter), 2(Candela) / 2(Nit))
        assertEqualScientificValue(1(SquareCentimeter), 2(Candela) / 2(Stilb))
        assertEqualScientificValue(3.14159(SquareCentimeter), 2(Candela) / 2(Lambert), 5)
        assertEqualScientificValue(3.14159(SquareFoot), 2(Candela) / 2(FootLambert), 5)
        assertEqualScientificValue(
            1(SquareMeter),
            2(Candela) / 2(Nit).convert(FootLambert as Luminance),
            5,
        )
    }

    @Test
    fun areaFromMagneticFluxAndInductionTest() {
        assertEqualScientificValue(1(SquareCentimeter), 2(Maxwell) / 2(Gauss))
        assertEqualScientificValue(1(SquareMeter), 2(Weber) / 2(Tesla))
    }

    @Test
    fun areaFromMomentumAndDynamicViscosityTest() {
        assertEqualScientificValue(1(SquareMeter), 2(Kilogram x (Meter per Second)) / 2(Pascal x Second))
        assertEqualScientificValue(
            1(SquareFoot),
            (2 * ImperialStandardGravityAcceleration.value)(Pound x (Foot per Second)) / 2(
                PoundSquareFoot x Second,
            ),
            5,
        )
        assertEqualScientificValue(
            1(SquareFoot),
            (2 * ImperialStandardGravityAcceleration.value)(Pound x (Foot per Second)) / 2(
                PoundSquareFoot.ukImperial x Second,
            ),
            5,
        )
        assertEqualScientificValue(
            1(SquareFoot),
            (2 * ImperialStandardGravityAcceleration.value)(Pound x (Foot per Second)) / 2(
                PoundSquareFoot.usCustomary x Second,
            ),
            5,
        )
        assertEqualScientificValue(
            1(SquareFoot),
            (2 * ImperialStandardGravityAcceleration.value)(Pound.ukImperial x (Foot per Second)) / 2(
                PoundSquareFoot x Second,
            ),
            5,
        )
        assertEqualScientificValue(
            1(SquareFoot),
            (2 * ImperialStandardGravityAcceleration.value)(Pound.ukImperial x (Foot per Second)) / 2(
                PoundSquareFoot.ukImperial x Second,
            ),
            5,
        )
        assertEqualScientificValue(
            1(SquareFoot),
            (2 * ImperialStandardGravityAcceleration.value)(Pound.usCustomary x (Foot per Second)) / 2(
                PoundSquareFoot x Second,
            ),
            5,
        )
        assertEqualScientificValue(
            1(SquareFoot),
            (2 * ImperialStandardGravityAcceleration.value)(Pound.usCustomary x (Foot per Second)) / 2(
                PoundSquareFoot.usCustomary x Second,
            ),
            5,
        )
        assertEqualScientificValue(
            1(SquareMeter),
            2(Kilogram x (Meter per Second)) / 2(Pascal x Second).convert(PoundSquareFoot x Second),
        )
    }

    @Test
    fun areaFromVolumeAndHeightTest() {
        assertEqualScientificValue(1(SquareMeter), 2(CubicMeter) / 2(Meter))
        assertEqualScientificValue(1(SquareNanometer), 2(CubicNanometer) / 2(Nanometer))
        assertEqualScientificValue(1(SquareMicrometer), 2(CubicMicrometer) / 2(Micrometer))
        assertEqualScientificValue(1(SquareMillimeter), 2(CubicMillimeter) / 2(Millimeter))
        assertEqualScientificValue(1(SquareCentimeter), 2(CubicCentimeter) / 2(Centimeter))
        assertEqualScientificValue(1(SquareDecimeter), 2(CubicDecimeter) / 2(Decimeter))
        assertEqualScientificValue(1(SquareDecameter), 2(CubicDecameter) / 2(Decameter))
        assertEqualScientificValue(1(SquareHectometer), 2(CubicHectometer) / 2(Hectometer))
        assertEqualScientificValue(1(SquareKilometer), 2(CubicKilometer) / 2(Kilometer))
        assertEqualScientificValue(1(SquareMegameter), 2(CubicMegameter) / 2(Megameter))
        assertEqualScientificValue(1(SquareGigameter), 2(CubicGigameter) / 2(Gigameter))
        assertEqualScientificValue(1(SquareMeter), 2(CubicMeter).convert(CubicDecameter) / 20(Decimeter))

        assertEqualScientificValue(1(SquareInch), 2(CubicInch) / 2(Inch))
        assertEqualScientificValue(1(SquareFoot), 2(CubicFoot) / 2(Foot))
        assertEqualScientificValue(1(SquareYard), 2(CubicYard) / 2(Yard))
        assertEqualScientificValue(1(SquareMile), 2(CubicMile) / 2(Mile))
        assertEqualScientificValue(1(Acre), 2(AcreInch) / 2(Inch))
        assertEqualScientificValue(1(Acre), 2(AcreFoot) / 2(Foot))
        assertEqualScientificValue(1(SquareFoot), 2(CubicFoot).convert(CubicInch) / 2(Foot))
        assertEqualScientificValue(1(SquareFoot), 2(CubicFoot.ukImperial) / 2(Foot))
        assertEqualScientificValue(1(SquareFoot), 2(CubicFoot.usCustomary) / 2(Foot))
        assertEqualScientificValue(1(SquareMeter), 2(CubicMeter) / 2(Meter).convert(Foot))
    }

    @Test
    fun areaFromVolumetricFlowAndVolumetricFluxTest() {
        assertEqualScientificValue(
            1(SquareMeter),
            2(CubicMeter per Second) / 2((CubicMeter per Second) per SquareMeter),
        )
        assertEqualScientificValue(
            1(SquareFoot),
            2(CubicFoot per Second) / 2((CubicFoot per Second) per SquareFoot),
        )
        assertEqualScientificValue(
            1(SquareFoot),
            2(CubicFoot per Second) / 2((CubicFoot.ukImperial per Second) per SquareFoot),
        )
        assertEqualScientificValue(
            1(SquareFoot),
            2(CubicFoot per Second) / 2((CubicFoot.usCustomary per Second) per SquareFoot),
        )
        assertEqualScientificValue(
            1(SquareFoot),
            2(CubicFoot.ukImperial per Second) / 2((CubicFoot per Second) per SquareFoot),
        )
        assertEqualScientificValue(
            1(SquareFoot),
            2(CubicFoot.ukImperial per Second) / 2((CubicFoot.ukImperial per Second) per SquareFoot),
        )
        assertEqualScientificValue(
            1(SquareFoot),
            2(CubicFoot.usCustomary per Second) / 2((CubicFoot per Second) per SquareFoot),
        )
        assertEqualScientificValue(
            1(SquareFoot),
            2(CubicFoot.usCustomary per Second) / 2((CubicFoot.usCustomary per Second) per SquareFoot),
        )
        assertEqualScientificValue(
            1(SquareMeter),
            2(CubicMeter per Second).convert(CubicFoot per Second) / 2((CubicMeter per Second) per SquareMeter),
        )
    }

    @Test
    fun areaFromWeightAndAreaDensityTest() {
        assertEqualScientificValue(1(SquareMeter), 2(Kilogram) / 2(Kilogram per (SquareMeter)))
        assertEqualScientificValue(1(SquareFoot), 2(Pound) / 2(Pound per (SquareFoot)))
        assertEqualScientificValue(1(SquareFoot), 2(Pound) / 2(Pound.ukImperial per (SquareFoot)))
        assertEqualScientificValue(1(SquareFoot), 2(Pound) / 2(Pound.usCustomary per (SquareFoot)))
        assertEqualScientificValue(1(SquareFoot), 2(Pound.ukImperial) / 2(Pound per (SquareFoot)))
        assertEqualScientificValue(
            1(SquareFoot),
            2(Pound.ukImperial) / 2(Pound.ukImperial per (SquareFoot)),
        )
        assertEqualScientificValue(1(SquareFoot), 2(Pound.usCustomary) / 2(Pound per (SquareFoot)))
        assertEqualScientificValue(
            1(SquareFoot),
            2(Pound.usCustomary) / 2(Pound.usCustomary per (SquareFoot)),
        )
        assertEqualScientificValue(
            1(SquareMeter),
            2(Kilogram).convert(Pound) / 2(Kilogram per (SquareMeter)),
            5,
        )
    }

    @Test
    fun areaFromMetricKinematicViscosityAndTimeTest() {
        assertEqualScientificValue(4(SquareMeter), (2(SquareMeter per Second) * 2(Second)))
    }

    @Test
    fun areaFromImperialKinematicViscosityAndTimeTest() {
        assertEqualScientificValue(4(SquareFoot), (2(SquareFoot per Second) * 2(Second)))
    }
}
