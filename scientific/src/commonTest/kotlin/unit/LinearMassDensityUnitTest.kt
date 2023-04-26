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

import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.area.div
import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.areaDensity.times
import com.splendo.kaluga.scientific.converter.density.times
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.converter.weight.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class LinearMassDensityUnitTest {

    @Test
    fun linearMassDensityConversionTest() {
        assertScientificConversion(1, (Kilogram per Meter), 0.671969, (Pound per Foot), 6)
        assertScientificConversion(
            1,
            (Kilogram per Meter),
            0.671969,
            (Pound.ukImperial per Foot),
            6,
        )
        assertScientificConversion(
            1,
            (Kilogram per Meter),
            0.671969,
            (Pound.usCustomary per Foot),
            6,
        )
    }

    @Test
    fun linearMassDensityFromAreaAndSpecificVolumeTest() {
        assertEquals(1(Kilogram per Meter), 2(SquareMeter) / 2(CubicMeter per Kilogram))
        assertEquals(1(Pound per Foot), 2(SquareFoot) / 2(CubicFoot per Pound))
        assertEquals(
            1(Pound.ukImperial per Foot),
            2(SquareFoot) / 2(CubicFoot.ukImperial per Pound.ukImperial),
        )
        assertEquals(
            1(Pound.usCustomary per Foot),
            2(SquareFoot) / 2(CubicFoot.usCustomary per Pound.usCustomary),
        )
        assertEquals(
            1(Kilogram per Meter),
            2(SquareMeter).convert(SquareFoot) / 2(CubicMeter per Kilogram),
        )
    }

    @Test
    fun linearMassDensityFromAreaDensityAndLengthTest() {
        assertEquals(4(Kilogram per Meter), 2(Kilogram per SquareMeter) * 2(Meter))
        assertEquals(4(Kilogram per Meter), 2(Meter) * 2(Kilogram per SquareMeter))
        assertEquals(4(Pound per Foot), 2(Pound per SquareFoot) * 2(Foot))
        assertEquals(4(Pound per Foot), 2(Foot) * 2(Pound per SquareFoot))
        assertEquals(4(Pound.ukImperial per Foot), 2(Pound.ukImperial per SquareFoot) * 2(Foot))
        assertEquals(4(Pound.ukImperial per Foot), 2(Foot) * 2(Pound.ukImperial per SquareFoot))
        assertEquals(4(Pound.usCustomary per Foot), 2(Pound.usCustomary per SquareFoot) * 2(Foot))
        assertEquals(4(Pound.usCustomary per Foot), 2(Foot) * 2(Pound.usCustomary per SquareFoot))
        assertEquals(4(Kilogram per Meter), 2(Kilogram per SquareMeter) * 2(Meter).convert(Foot))
        assertEquals(4(Kilogram per Meter), 2(Meter).convert(Foot) * 2(Kilogram per SquareMeter))
    }

    @Test
    fun linearMassDensityFromDensityAndLengthTest() {
        assertEquals(4(Kilogram per Meter), 2(Kilogram per CubicMeter) * 2(SquareMeter))
        assertEquals(4(Kilogram per Meter), 2(SquareMeter) * 2(Kilogram per CubicMeter))
        assertEquals(4(Pound per Foot), 2(Pound per CubicFoot) * 2(SquareFoot))
        assertEquals(4(Pound per Foot), 2(SquareFoot) * 2(Pound per CubicFoot))
        assertEquals(
            4(Pound.ukImperial per Foot),
            2(Pound.ukImperial per CubicFoot) * 2(SquareFoot),
        )
        assertEquals(
            4(Pound.ukImperial per Foot),
            2(SquareFoot) * 2(Pound.ukImperial per CubicFoot),
        )
        assertEquals(
            4(Pound.usCustomary per Foot),
            2(Pound.usCustomary per CubicFoot) * 2(SquareFoot),
        )
        assertEquals(
            4(Pound.usCustomary per Foot),
            2(SquareFoot) * 2(Pound.usCustomary per CubicFoot),
        )
        assertEquals(
            4(Kilogram per Meter),
            2(Kilogram per CubicMeter) * 2(SquareMeter).convert(SquareFoot),
        )
        assertEquals(
            4(Kilogram per Meter),
            2(SquareMeter).convert(SquareFoot) * 2(Kilogram per CubicMeter),
        )
    }

    @Test
    fun linearMassDensityFromWeightAndAreaTest() {
        assertEquals(1(Kilogram per Meter), 2(Kilogram) / 2(Meter))
        assertEquals(1(Pound per Foot), 2(Pound) / 2(Foot))
        assertEquals(1(Pound.ukImperial per Foot), 2(Pound.ukImperial) / 2(Foot))
        assertEquals(1(Pound.usCustomary per Foot), 2(Pound.usCustomary) / 2(Foot))
        assertEquals(1(Kilogram per Meter), 2(Kilogram) / 2(Meter).convert(Foot))
    }
}
