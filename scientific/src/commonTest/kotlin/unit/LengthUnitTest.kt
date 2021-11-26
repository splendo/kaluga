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

import com.splendo.kaluga.scientific.assertEqualScientificValue
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
import kotlin.test.Test
import kotlin.test.assertEquals

class LengthUnitTest {

    @Test
    fun meterConversionTest() {
        assertScientificConversion(1.0, Meter, 1e+9, Nanometer)
        assertScientificConversion(1.0, Meter, 1e+6, Micrometer)
        assertScientificConversion(1.0, Meter, 1_000.0, Millimeter)
        assertScientificConversion(1.0, Meter, 100.0, Centimeter)
        assertScientificConversion(1.0, Meter, 10.0, Decimeter)
        assertScientificConversion(1.0, Meter, 0.1, Decameter)
        assertScientificConversion(1.0, Meter, 0.01, Hectometer)
        assertScientificConversion(1.0, Meter, 0.001, Kilometer)
        assertScientificConversion(1.0, Meter, 1e-6, Megameter)
        assertScientificConversion(1.0, Meter, 1e-9, Gigameter)
    }

    @Test
    fun feetConversionTest() {
        assertScientificConversion(1.0, Foot, 0.3048, Meter)
        assertScientificConversion(1.0, Foot, 12.0, Inch)
        assertScientificConversion(1.0, Foot, 0.33333333, Yard, 8)
        assertScientificConversion(1.0, Foot, 0.00018939, Mile, 8)
    }

    @Test
    fun widthFromAreaAndLengthTest() {
        assertEquals(1(Meter), 2(SquareMeter) / 2(Meter))
        assertEquals(1(Nanometer), 2(SquareNanometer) / 2(Nanometer))
        assertEquals(1(Micrometer), 2(SquareMicrometer) / 2(Micrometer))
        assertEquals(1(Millimeter), 2(SquareMillimeter) / 2(Millimeter))
        assertEquals(1(Centimeter), 2(SquareCentimeter) / 2(Centimeter))
        assertEquals(1(Decimeter), 2(SquareDecimeter) / 2(Decimeter))
        assertEquals(1(Decameter), 2(SquareDecameter) / 2(Decameter))
        assertEquals(1(Hectometer), 2(SquareHectometer) / 2(Hectometer))
        assertEquals(1(Kilometer), 2(SquareKilometer) / 2(Kilometer))
        assertEquals(1(Megameter), 2(SquareMegameter) / 2(Megameter))
        assertEquals(1(Gigameter), 2(SquareGigameter) / 2(Gigameter))
        assertEquals(1(Meter), 2(SquareMeter).convert(SquareCentimeter) / 2(Meter).convert(Hectometer))

        assertEquals(1(Inch), 2(SquareInch) / 2(Inch))
        assertEquals(1(Foot), 2(SquareFoot) / 2(Foot))
        assertEquals(1(Yard), 2(SquareYard) / 2(Yard))
        assertEquals(1(Mile), 2(SquareMile) / 2(Mile))
        assertEquals(1(Foot), 2(SquareFoot).convert(SquareInch) / 2(Foot).convert(Yard))
        assertEquals(1(Meter), 2(SquareMeter).convert(SquareCentimeter) / 2(Meter).convert(Yard))
    }

    @Test
    fun lengthFromAreaDensityAndDensityTest() {
        assertEquals(1(Meter), 2(Kilogram per SquareMeter) / 2(Kilogram per CubicMeter))
        assertEquals(1(Foot), 2(Pound per SquareFoot) / 2(Pound per CubicFoot))
        assertEquals(1(Foot), 2(Pound.ukImperial per SquareFoot) / 2(Pound per CubicFoot))
        assertEquals(1(Foot), 2(Pound.usCustomary per SquareFoot) / 2(Pound per CubicFoot))
        assertEquals(1(Foot), 2(Pound per SquareFoot) / 2(Pound.ukImperial per CubicFoot))
        assertEquals(1(Foot), 2(Pound.ukImperial per SquareFoot) / 2(Pound.ukImperial per CubicFoot))
        assertEquals(1(Foot), 2(Pound per SquareFoot) / 2(Pound.usCustomary per CubicFoot))
        assertEquals(1(Foot), 2(Pound.usCustomary per SquareFoot) / 2(Pound.usCustomary per CubicFoot))
        assertEquals(1(Meter), 2(Kilogram per SquareMeter) / 2(Kilogram per CubicMeter).convert(Pound per CubicFoot))
    }

    @Test
    fun lengthFromAreaDensityAndSpecificVolumeTest() {
        assertEquals(4(Meter), 2(CubicMeter per Kilogram) * 2(Kilogram per SquareMeter))
        assertEquals(4(Foot), 2(CubicFoot per Pound) * 2(Pound per SquareFoot))
        assertEquals(4(Foot), 2(CubicFoot per Pound) * 2(Pound.ukImperial per SquareFoot))
        assertEquals(4(Foot), 2(CubicFoot per Pound) * 2(Pound.usCustomary per SquareFoot))
        assertEquals(4(Foot), 2(CubicFoot per Pound.ukImperial) * 2(Pound per SquareFoot))
        assertEquals(4(Foot), 2(CubicFoot per Pound.ukImperial) * 2(Pound.ukImperial per SquareFoot))
        assertEquals(4(Foot), 2(CubicFoot per Pound.usCustomary) * 2(Pound per SquareFoot))
        assertEquals(4(Foot), 2(CubicFoot per Pound.usCustomary) * 2(Pound.usCustomary per SquareFoot))
        assertEquals(4(Meter), 2(CubicMeter per Kilogram) * 2(Kilogram per SquareMeter).convert(Pound per SquareFoot))

        assertEquals(4(Meter), 2(Kilogram per SquareMeter) * 2(CubicMeter per Kilogram))
        assertEquals(4(Foot), 2(Pound per SquareFoot) * 2(CubicFoot per Pound))
        assertEquals(4(Foot), 2(Pound.ukImperial per SquareFoot) * 2(CubicFoot per Pound))
        assertEquals(4(Foot), 2(Pound.usCustomary per SquareFoot) * 2(CubicFoot per Pound))
        assertEquals(4(Foot), 2(Pound per SquareFoot) * 2(CubicFoot per Pound.ukImperial))
        assertEquals(4(Foot), 2(Pound.ukImperial per SquareFoot) * 2(CubicFoot per Pound.ukImperial))
        assertEquals(4(Foot), 2(Pound per SquareFoot) * 2(CubicFoot per Pound.usCustomary))
        assertEquals(4(Foot), 2(Pound.usCustomary per SquareFoot) * 2(CubicFoot per Pound.usCustomary))
        assertEquals(4(Meter), 2(Kilogram per SquareMeter).convert(Pound per SquareFoot) * 2(CubicMeter per Kilogram))
    }

    @Test
    fun lengthFromEnergyAndForceTest() {
        assertEquals(1(Meter), 2(Joule) / 2(Newton))
        assertEquals(1(Centimeter), 2(Erg) / 2(Dyne))
        assertEquals(1(Centimeter), 20(Decierg) / 2(Dyne))
        assertEquals(1(Centimeter), 2(Erg) / 20(Decidyne))
        assertEquals(1(Centimeter), 20(Decierg) / 20(Decidyne))
        assertEquals(1(Foot), 2(FootPoundal) / 2(Poundal))
        assertEquals(1(Foot), 2(FootPoundForce) / 2(PoundForce))
        assertEquals(1(Inch), 2(InchPoundForce) / 2(PoundForce))
        assertEquals(1(Inch), 2(InchOunceForce) / 2(OunceForce))
        assertEquals(1(Foot), 2(FootPoundForce) / 2(PoundForce).convert(GrainForce))
        assertEquals(1(Foot), 2(FootPoundForce) / 2(PoundForce).convert(ImperialTonForce))
        assertEquals(1(Foot), 2(FootPoundForce) / 2(PoundForce).convert(UsTonForce))
        assertEquals(1(Foot), 2(FootPoundForce).convert(WattHour) / 2(PoundForce))
        assertEquals(1(Foot), 2(FootPoundForce).convert(WattHour) / 2(PoundForce).convert(ImperialTonForce))
        assertEquals(1(Foot), 2(FootPoundForce).convert(WattHour) / 2(PoundForce).convert(UsTonForce))
    }

    @Test
    fun lengthFromForceAndSurfaceTensionTest() {
        assertEqualScientificValue(1(Meter), 2(Newton) / 2(Newton per Meter))
        assertEqualScientificValue(1(Foot), 2(PoundForce) / 2(PoundForce per Foot))
        assertEqualScientificValue(1(Foot), 2(PoundForce.ukImperial) / 2(PoundForce per Foot))
        assertEqualScientificValue(1(Foot), 2(PoundForce.usCustomary) / 2(PoundForce per Foot))
        assertEqualScientificValue(1(Foot), 2(PoundForce) / 2(PoundForce.ukImperial per Foot))
        assertEqualScientificValue(1(Foot), 2(PoundForce.ukImperial) / 2(PoundForce.ukImperial per Foot))
        assertEqualScientificValue(1(Foot), 2(PoundForce) / 2(PoundForce.usCustomary per Foot))
        assertEqualScientificValue(1(Foot), 2(PoundForce.usCustomary) / 2(PoundForce per Foot))
        assertEqualScientificValue(1(Meter), 2(Newton) / 2(Newton per Meter).convert(PoundForce per Foot))
    }

    @Test
    fun lengthFromLinearMassDensityAndDensityTest() {
        assertEquals(1(Meter), 2(Kilogram per Meter) / 2(Kilogram per SquareMeter))
        assertEquals(1(Foot), 2(Pound per Foot) / 2(Pound per SquareFoot))
        assertEquals(1(Foot), 2(Pound per Foot) / 2(Pound.ukImperial per SquareFoot))
        assertEquals(1(Foot), 2(Pound per Foot) / 2(Pound.usCustomary per SquareFoot))
        assertEquals(1(Foot), 2(Pound.ukImperial per Foot) / 2(Pound per SquareFoot))
        assertEquals(1(Foot), 2(Pound.ukImperial per Foot) / 2(Pound.ukImperial per SquareFoot))
        assertEquals(1(Foot), 2(Pound.usCustomary per Foot) / 2(Pound per SquareFoot))
        assertEquals(1(Foot), 2(Pound.usCustomary per Foot) / 2(Pound.usCustomary per SquareFoot))
        assertEquals(1(Meter), 2(Kilogram per Meter) / 2(Kilogram per SquareMeter).convert(Pound per SquareFoot))
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
        assertEquals(2(Meter), 8(CubicMeter) / 4(SquareMeter))
        assertEquals(2(Nanometer), 8(CubicNanometer) / 4(SquareNanometer))
        assertEquals(2(Micrometer), 8(CubicMicrometer) / 4(SquareMicrometer))
        assertEquals(2(Millimeter), 8(CubicMillimeter) / 4(SquareMillimeter))
        assertEquals(2(Centimeter), 8(CubicCentimeter) / 4(SquareCentimeter))
        assertEquals(2(Decimeter), 8(CubicDecimeter) / 4(SquareDecimeter))
        assertEquals(2(Decameter), 8(CubicDecameter) / 4(SquareDecameter))
        assertEquals(2(Hectometer), 8(CubicHectometer) / 4(SquareHectometer))
        assertEquals(2(Kilometer), 8(CubicKilometer) / 4(SquareKilometer))
        assertEquals(2(Megameter), 8(CubicMegameter) / 4(SquareMegameter))
        assertEquals(2(Gigameter), 8(CubicGigameter) / 4(SquareGigameter))
        assertEquals(2(Meter), 8(CubicMeter).convert(CubicDecameter) / 4(SquareMeter).convert(SquareDecimeter))
        assertEquals(2(Meter), Meter.height(8(CubicMeter), 2(Meter), 2(Meter)))

        assertEquals(2(Inch), 8(CubicInch) / 4(SquareInch))
        assertEquals(2(Foot), 8(CubicFoot) / 4(SquareFoot))
        assertEquals(2(Yard), 8(CubicYard) / 4(SquareYard))
        assertEquals(2(Mile), 8(CubicMile) / 4(SquareMile))
        assertEquals(2(Inch), 8(AcreInch) / 4(Acre))
        assertEquals(2(Foot), 8(AcreFoot) / 4(Acre))
        assertEquals(2(Foot), 8(CubicFoot).convert(CubicInch) / 4(SquareFoot).convert(SquareYard))
        assertEquals(2(Foot), 8(CubicFoot.ukImperial) / 4(SquareFoot))
        assertEquals(2(Foot), 8(CubicFoot.usCustomary) / 4(SquareFoot))
        assertEquals(2(Meter), 8(CubicMeter).convert(CubicDecameter) / 4(SquareMeter).convert(SquareInch))
    }

    @Test
    fun lengthFromWeightAndLinearMassDensityTest() {
        assertEqualScientificValue(1(Meter), 2(Kilogram) / 2(Kilogram per Meter))
        assertEqualScientificValue(1(Foot), 2(Pound) / 2(Pound per Foot))
        assertEqualScientificValue(1(Foot), 2(Pound.ukImperial) / 2(Pound per Foot))
        assertEqualScientificValue(1(Foot), 2(Pound.usCustomary) / 2(Pound per Foot))
        assertEqualScientificValue(1(Foot), 2(Pound) / 2(Pound.ukImperial per Foot))
        assertEqualScientificValue(1(Foot), 2(Pound.ukImperial) / 2(Pound.ukImperial per Foot))
        assertEqualScientificValue(1(Foot), 2(Pound) / 2(Pound.usCustomary per Foot))
        assertEqualScientificValue(1(Foot), 2(Pound.ukImperial) / 2(Pound.usCustomary per Foot))
        assertEqualScientificValue(1(Meter), 2(Kilogram) / 2(Kilogram per Meter).convert(Pound per Foot))
    }
}