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
import com.splendo.kaluga.scientific.converter.area.div
import com.splendo.kaluga.scientific.converter.areaDensity.div
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.converter.linearMassDensity.div
import com.splendo.kaluga.scientific.converter.specificVolume.times
import com.splendo.kaluga.scientific.converter.speed.times
import com.splendo.kaluga.scientific.converter.weight.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class LengthUnitTest {

    // ##### Same length unit table conversions #####

    @Test
    fun meterConversionTest() {
        assertEquals(1e+9, Meter.convert(1.0, Nanometer))
        assertEquals(1e+6, Meter.convert(1.0, Micrometer))
        assertEquals(1_000.0, Meter.convert(1.0, Millimeter))
        assertEquals(100.0, Meter.convert(1.0, Centimeter))
        assertEquals(10.0, Meter.convert(1.0, Decimeter))
        assertEquals(0.1, Meter.convert(1.0, Decameter))
        assertEquals(0.01, Meter.convert(1.0, Hectometer))
        assertEquals(0.001, Meter.convert(1.0, Kilometer))
        assertEquals(1e-6, Meter.convert(1.0, Megameter))
        assertEquals(1e-9, Meter.convert(1.0, Gigameter))
    }

    @Test
    fun feetConversionTest() {
        assertEquals(12.0, Foot.convert(1.0, Inch))
        assertEquals(0.33333333, Foot.convert(1.0, Yard, 8))
        assertEquals(0.00018939, Foot.convert(1.0, Mile, 8))
    }

    // ##### Mixed mass unit table conversions #####

    @Test
    fun meterToFeetConversionTest() {
        assertEquals(39.37007874, Meter.convert(1.0, Inch, 8))
        assertEquals(1.0936133, Meter.convert(1.0, Yard, 8))
        assertEquals(0.00062137, Meter.convert(1.0, Mile, 8))
    }

    @Test
    fun feetToMeterConversionTest() {
        assertEquals(304800000.0, Foot.convert(1.0, Nanometer))
        assertEquals(304800.0, Foot.convert(1.0, Micrometer))
        assertEquals(304.8, Foot.convert(1.0, Millimeter))
        assertEquals(30.48, Foot.convert(1.0, Centimeter))
        assertEquals(3.048, Foot.convert(1.0, Decimeter))
        assertEquals(0.03048, Foot.convert(1.0, Decameter))
        assertEquals(0.003048, Foot.convert(1.0, Hectometer))
        assertEquals(0.0003048, Foot.convert(1.0, Kilometer))
        assertEquals(0.0000003, Foot.convert(1.0, Megameter, 8))
        assertEquals(0.000000000305, Foot.convert(1.0, Gigameter, 12))
    }

    @Test
    fun widthFromAreaAndLengthTest() {
        assertEquals(1(Meter), 2(SquareMeter) / 2(Meter))
        assertEquals(1(Foot), 2(SquareFoot) / 2(Foot))
    }

    @Test
    fun lengthFromAreaDensityAndDensityTest() {
        assertEquals(1(Meter), 2(Kilogram per SquareMeter) / 2(Kilogram per CubicMeter))
        assertEquals(1(Foot), 2(Pound per SquareFoot) / 2(Pound per CubicFoot))
    }

    @Test
    fun lengthFromAreaDensityAndSpecificVolumeTest() {
        assertEquals(4(Meter), 2(CubicMeter per Kilogram) * 2(Kilogram per SquareMeter))
        assertEquals(4(Foot), 2(CubicFoot per Pound) * 2(Pound per SquareFoot))
    }

    @Test
    fun lengthFromEnergyAndForceTest() {
        assertEquals(1(Meter), 2(Joule) / 2(Newton))
        assertEquals(1(Foot), 2(FootPoundForce) / 2(PoundForce))
    }

    @Test
    fun lengthFromForceAndSurfaceTensionTest() {
        assertEqualScientificValue(1(Meter), 2(Newton) / 2(Newton per Meter))
        assertEqualScientificValue(1(Foot), 2(PoundForce) / 2(PoundForce per Foot))
    }

    @Test
    fun lengthFromLinearMassDensityAndDensityTest() {
        assertEquals(1(Meter), 2(Kilogram per Meter) / 2(Kilogram per SquareMeter))
        assertEquals(1(Foot), 2(Pound per Foot) / 2(Pound per SquareFoot))
    }

    @Test
    fun distanceFromSpeedAndTimeTest() {
        assertEqualScientificValue(4(Meter), 2(Meter per Second) * 2(Second))
        assertEqualScientificValue(4(Foot), 2(Foot per Second) * 2(Second))
    }

    @Test
    fun heightFromVolumeLengthAndWidthTest() {
        // FIXME errors in convert method (recursive)
    }

    @Test
    fun lengthFromWeightAndLinearMassDensityTest() {
        assertEqualScientificValue(1(Meter), 2(Kilogram) / 2(Kilogram per Meter))
        assertEqualScientificValue(1(Foot), 2(Pound) / 2(Pound per Foot))
    }
}