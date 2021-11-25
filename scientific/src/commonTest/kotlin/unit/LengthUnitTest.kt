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