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
import kotlin.test.Test
import kotlin.test.assertEquals

class AreaUnitTest {

    @Test
    fun areaConversionTest() {
        assertScientificConversion(1, SquareMeter, 1e+18, SquareNanometer)
        assertScientificConversion(1, SquareMeter, 1e+12, SquareMicrometer)
        assertScientificConversion(1, SquareMeter, 1000000.0, SquareMillimeter)
        assertScientificConversion(1, SquareMeter, 10000.0, SquareCentimeter)
        assertScientificConversion(1, SquareMeter, 100.0, SquareDecimeter)
        assertScientificConversion(1, SquareMeter, 0.01, SquareDecameter)
        assertScientificConversion(1, SquareMeter, 0.0001, SquareHectometer)
        assertScientificConversion(1, SquareMeter, 1e-6, SquareKilometer)
        assertScientificConversion(1, SquareMeter, 0.0001, Hectare)

        assertScientificConversion(1, SquareMeter, 1550.0, SquareInch, 0)
        assertScientificConversion(1, SquareMeter, 10.7639, SquareFoot, 4)
        assertScientificConversion(1, SquareMeter, 1.19599, SquareYard, 5)
        assertScientificConversion(1, SquareMeter, 3.86102e-7, SquareMile, 12)
        assertScientificConversion(1, SquareMeter, 0.000247105, Acre, 9)
    }

    @Test
    fun areaFromEnergyAndSurfaceTensionTest() {
        assertEquals(1(SquareMeter), 2(Joule) / 2(Newton per Meter))
        assertEquals(1(SquareCentimeter), 2(Erg) / 2(Dyne per Centimeter))
        assertEquals(1(SquareCentimeter), 20(Decierg) / 2(Dyne per Centimeter))
        assertEquals(1(SquareMeter), 2(Joule).convert(WattHour) / 2(Newton per Meter))
        assertEquals(1(SquareInch), 2(InchPoundForce) / 2(PoundForce per Inch))
        assertEquals(1(SquareInch), 2(InchPoundForce) / 2(PoundForce.ukImperial per Inch))
        assertEquals(1(SquareInch), 2(InchPoundForce) / 2(PoundForce.usCustomary per Inch))
        assertEquals(1(SquareInch), 2(InchOunceForce) / 2(OunceForce per Inch))
        assertEquals(1(SquareInch), 2(InchOunceForce) / 2(OunceForce.ukImperial per Inch))
        assertEquals(1(SquareInch), 2(InchOunceForce) / 2(OunceForce.usCustomary per Inch))
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
        assertEquals(1(SquareFoot), 2(FootPoundForce) / 2(PoundForce per Foot))
        assertEquals(1(SquareFoot), 2(FootPoundForce) / 2(PoundForce.ukImperial per Foot))
        assertEquals(1(SquareFoot), 2(FootPoundForce) / 2(PoundForce.usCustomary per Foot))
        assertEquals(1(SquareMeter), 2(Joule).convert(FootPoundForce) / 2(Newton per Meter))
    }

    @Test
    fun areaFromForceAndPressureTest() {
        assertEquals(1(SquareMeter), 2(Newton) / 2(Pascal))
        assertEquals(1(SquareCentimeter), 2(Dyne) / 2(Barye))
        assertEquals(1(SquareCentimeter), 2(Dyne) / 20(Decibarye))
        assertEquals(1(SquareCentimeter), 20(Decidyne) / 2(Barye))
        assertEquals(1(SquareCentimeter), 20(Decidyne) / 20(Decibarye))
        assertEquals(1(SquareInch), 2(PoundForce) / 2(PoundSquareInch))
        assertEquals(1(SquareFoot), 2(PoundForce) / 2(PoundSquareFoot))
        assertEquals(1(SquareInch), 2(PoundForce) / 0.002(KiloPoundSquareInch))
        assertEquals(1(SquareInch), 2(OunceForce) / 2(OunceSquareInch))
        assertEquals(1(SquareInch), 2(Kip) / 2(KipSquareInch))
        assertEquals(1(SquareFoot), 2(Kip) / 2(KipSquareFoot))
        assertEquals(1(SquareInch), 2(UsTonForce) / 2(USTonSquareInch))
        assertEquals(1(SquareFoot), 2(UsTonForce) / 2(USTonSquareFoot))
        assertEquals(1(SquareInch), 2(ImperialTonForce) / 2(ImperialTonSquareInch))
        assertEquals(1(SquareFoot), 2(ImperialTonForce) / 2(ImperialTonSquareFoot))
        assertEquals(1(SquareFoot), 2(PoundForce).convert(GrainForce) / 2(PoundSquareFoot))
        assertEquals(
            1(SquareFoot),
            2(PoundForce).convert(GrainForce) / 2(PoundSquareFoot.ukImperial),
        )
        assertEquals(
            1(SquareFoot),
            2(PoundForce).convert(GrainForce) / 2(PoundSquareFoot.usCustomary),
        )
        assertEquals(
            1(SquareFoot),
            2(PoundForce).convert(GrainForce.ukImperial) / 2(PoundSquareFoot),
        )
        assertEquals(
            1(SquareFoot),
            2(PoundForce).convert(GrainForce.ukImperial) / 2(PoundSquareFoot.ukImperial),
        )
        assertEquals(
            1(SquareFoot),
            2(PoundForce).convert(GrainForce.usCustomary) / 2(PoundSquareFoot),
        )
        assertEquals(
            1(SquareFoot),
            2(PoundForce).convert(GrainForce.usCustomary) / 2(PoundSquareFoot.usCustomary),
        )
        assertEquals(1(SquareMeter), 2(Newton).convert(PoundForce) / 2(Pascal))
    }

    @Test
    fun areaFromLengthAndWidthTest() {
        assertEquals(4(SquareMeter), 2(Meter) * 2(Meter))
        assertEquals(4(SquareNanometer), 2(Nanometer) * 2(Nanometer))
        assertEquals(4(SquareMicrometer), 2(Micrometer) * 2(Micrometer))
        assertEquals(4(SquareMillimeter), 2(Millimeter) * 2(Millimeter))
        assertEquals(4(SquareCentimeter), 2(Centimeter) * 2(Centimeter))
        assertEquals(4(SquareDecimeter), 2(Decimeter) * 2(Decimeter))
        assertEquals(4(SquareDecameter), 2(Decameter) * 2(Decameter))
        assertEquals(4(SquareHectometer), 2(Hectometer) * 2(Hectometer))
        assertEquals(4(SquareKilometer), 2(Kilometer) * 2(Kilometer))
        assertEquals(4(SquareMegameter), 2(Megameter) * 2(Megameter))
        assertEquals(4(SquareGigameter), 2(Gigameter) * 2(Gigameter))
        assertEquals(4(SquareMeter), 20(Decimeter) * 0.2(Decameter))

        assertEquals(4(SquareInch), 2(Inch) * 2(Inch))
        assertEquals(4(SquareFoot), 2(Foot) * 2(Foot))
        assertEquals(4(SquareYard), 2(Yard) * 2(Yard))
        assertEquals(4(SquareMile), 2(Mile) * 2(Mile))
        assertEquals(4(SquareFoot), 2(Foot).convert(Inch) * 2(Foot))
        assertEquals(4(SquareMeter), 2(Meter).convert(Foot) * 2(Meter))
    }

    @Test
    fun areaFromLinearMassDensityAndDensityTest() {
        assertEquals(1(SquareMeter), 2(Kilogram per Meter) / 2(Kilogram per CubicMeter))
        assertEquals(1(SquareFoot), 2(Pound per Foot) / 2(Pound per CubicFoot))
        assertEquals(1(SquareFoot), 2(Pound per Foot) / 2(Pound.ukImperial per CubicFoot))
        assertEquals(1(SquareFoot), 2(Pound per Foot) / 2(Pound.usCustomary per CubicFoot))
        assertEquals(1(SquareFoot), 2(Pound.ukImperial per Foot) / 2(Pound per CubicFoot))
        assertEquals(
            1(SquareFoot),
            2(Pound.ukImperial per Foot) / 2(Pound.ukImperial per CubicFoot),
        )
        assertEquals(1(SquareFoot), 2(Pound.usCustomary per Foot) / 2(Pound per CubicFoot))
        assertEquals(1(SquareFoot), 2(Pound.usCustomary per Foot) / 2(Pound per CubicFoot))
        assertEquals(
            1(SquareMeter),
            2(Kilogram per Meter) / 2(Kilogram per CubicMeter).convert(Pound per CubicFoot),
        )
    }

    @Test
    fun areaFromLinearMassDensityAndSpecificVolumeTest() {
        assertEquals(4(SquareMeter), 2(Kilogram per Meter) * 2(CubicMeter per Kilogram))
        assertEquals(4(SquareMeter), 2(CubicMeter per Kilogram) * 2(Kilogram per Meter))
        assertEquals(4(SquareFoot), 2(Pound per Foot) * 2(CubicFoot per Pound))
        assertEquals(4(SquareFoot), 2(CubicFoot per Pound) * 2(Pound per Foot))
        assertEquals(4(SquareFoot), 2(Pound per Foot) * 2(CubicFoot per Pound.ukImperial))
        assertEquals(4(SquareFoot), 2(CubicFoot per Pound.ukImperial) * 2(Pound per Foot))
        assertEquals(4(SquareFoot), 2(Pound per Foot) * 2(CubicFoot per Pound.usCustomary))
        assertEquals(4(SquareFoot), 2(CubicFoot per Pound.usCustomary) * 2(Pound per Foot))
        assertEquals(4(SquareFoot), 2(Pound.ukImperial per Foot) * 2(CubicFoot per Pound))
        assertEquals(4(SquareFoot), 2(CubicFoot per Pound) * 2(Pound.ukImperial per Foot))
        assertEquals(
            4(SquareFoot),
            2(Pound.ukImperial per Foot) * 2(CubicFoot per Pound.ukImperial),
        )
        assertEquals(
            4(SquareFoot),
            2(CubicFoot per Pound.ukImperial) * 2(Pound.ukImperial per Foot),
        )
        assertEquals(4(SquareFoot), 2(Pound.usCustomary per Foot) * 2(CubicFoot per Pound))
        assertEquals(4(SquareFoot), 2(CubicFoot per Pound) * 2(Pound.usCustomary per Foot))
        assertEquals(
            4(SquareFoot),
            2(Pound.usCustomary per Foot) * 2(CubicFoot per Pound.usCustomary),
        )
        assertEquals(
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
        assertEquals(1(SquareMeter), 2(Lumen x Second) / 2(Lux x Second))
        assertEquals(1(SquareFoot), 2(Lumen x Second) / 2(FootCandle x Second))
        assertEquals(
            1(SquareMeter),
            2(Lumen x Second) / 2(Lux x Second).convert((FootCandle x Second) as LuminousExposure),
        )
    }

    @Test
    fun areaFromFluxAndIlluminanceTest() {
        assertEquals(1(SquareCentimeter), 2(Lumen) / 2(Phot))
        assertEquals(1(SquareCentimeter), 2(Lumen) / 20(Deciphot))
        assertEquals(1(SquareMeter), 2(Lumen) / 2(Lux))
        assertEquals(1(SquareFoot), 2(Lumen) / 2(FootCandle))
    }

    @Test
    fun areaFromLuminousIntensityAndLuminanceTest() {
        assertEquals(1(SquareMeter), 2(Candela) / 2(Nit))
        assertEquals(1(SquareCentimeter), 2(Candela) / 2(Stilb))
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
        assertEquals(1(SquareCentimeter), 2(Maxwell) / 2(Gauss))
        assertEquals(1(SquareMeter), 2(Weber) / 2(Tesla))
    }

    @Test
    fun areaFromMomentumAndDynamicViscosityTest() {
        assertEquals(1(SquareMeter), 2(Kilogram x (Meter per Second)) / 2(Pascal x Second))
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
        assertEquals(
            1(SquareMeter),
            2(Kilogram x (Meter per Second)) / 2(Pascal x Second).convert(PoundSquareFoot x Second),
        )
    }

    @Test
    fun areaFromVolumeAndHeightTest() {
        assertEquals(1(SquareMeter), 2(CubicMeter) / 2(Meter))
        assertEquals(1(SquareNanometer), 2(CubicNanometer) / 2(Nanometer))
        assertEquals(1(SquareMicrometer), 2(CubicMicrometer) / 2(Micrometer))
        assertEquals(1(SquareMillimeter), 2(CubicMillimeter) / 2(Millimeter))
        assertEquals(1(SquareCentimeter), 2(CubicCentimeter) / 2(Centimeter))
        assertEquals(1(SquareDecimeter), 2(CubicDecimeter) / 2(Decimeter))
        assertEquals(1(SquareDecameter), 2(CubicDecameter) / 2(Decameter))
        assertEquals(1(SquareHectometer), 2(CubicHectometer) / 2(Hectometer))
        assertEquals(1(SquareKilometer), 2(CubicKilometer) / 2(Kilometer))
        assertEquals(1(SquareMegameter), 2(CubicMegameter) / 2(Megameter))
        assertEquals(1(SquareGigameter), 2(CubicGigameter) / 2(Gigameter))
        assertEquals(1(SquareMeter), 2(CubicMeter).convert(CubicDecameter) / 20(Decimeter))

        assertEquals(1(SquareInch), 2(CubicInch) / 2(Inch))
        assertEquals(1(SquareFoot), 2(CubicFoot) / 2(Foot))
        assertEquals(1(SquareYard), 2(CubicYard) / 2(Yard))
        assertEquals(1(SquareMile), 2(CubicMile) / 2(Mile))
        assertEquals(1(Acre), 2(AcreInch) / 2(Inch))
        assertEquals(1(Acre), 2(AcreFoot) / 2(Foot))
        assertEquals(1(SquareFoot), 2(CubicFoot).convert(CubicInch) / 2(Foot))
        assertEquals(1(SquareFoot), 2(CubicFoot.ukImperial) / 2(Foot))
        assertEquals(1(SquareFoot), 2(CubicFoot.usCustomary) / 2(Foot))
        assertEquals(1(SquareMeter), 2(CubicMeter) / 2(Meter).convert(Foot))
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
