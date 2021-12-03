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
import com.splendo.kaluga.scientific.converter.density.times
import com.splendo.kaluga.scientific.converter.length.div
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.converter.linearMassDensity.div
import com.splendo.kaluga.scientific.converter.weight.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class AreaDensityUnitTest {

    @Test
    fun areaDensityConversionTest() {
        assertScientificConversion(1, (Kilogram per SquareMeter), 0.204816, Pound per SquareFoot, 6)
    }

    @Test
    fun areaDensityFromDensityAndLengthTest() {
        assertEquals(4(Kilogram per SquareMeter), 2(Kilogram per CubicMeter) * 2(Meter))
        assertEquals(4(Kilogram per SquareMeter), 2(Meter) * 2(Kilogram per CubicMeter))
        assertEquals(4(Pound per SquareFoot), 2(Pound per CubicFoot) * 2(Foot))
        assertEquals(4(Pound per SquareFoot), 2(Foot) * 2(Pound per CubicFoot))
        assertEquals(
            4(Pound.ukImperial per SquareFoot),
            2(Pound.ukImperial per CubicFoot) * 2(Foot)
        )
        assertEquals(
            4(Pound.ukImperial per SquareFoot),
            2(Foot) * 2(Pound.ukImperial per CubicFoot)
        )
        assertEquals(
            4(Pound.usCustomary per SquareFoot),
            2(Pound.usCustomary per CubicFoot) * 2(Foot)
        )
        assertEquals(
            4(Pound.usCustomary per SquareFoot),
            2(Foot) * 2(Pound.usCustomary per CubicFoot)
        )
        assertEquals(
            4(Kilogram per SquareMeter),
            2(Kilogram per CubicMeter) * 2(Meter).convert(Foot)
        )
        assertEquals(
            4(Kilogram per SquareMeter),
            2(Meter).convert(Foot) * 2(Kilogram per CubicMeter)
        )
    }

    @Test
    fun areaDensityFromLengthAndSpecificVolumeTest() {
        assertEquals(1(Kilogram per SquareMeter), 2(Meter) / 2(CubicMeter per Kilogram))
        assertEquals(1(Pound per SquareFoot), 2(Foot) / 2(CubicFoot per Pound))
        assertEquals(
            1(Pound.ukImperial per SquareFoot),
            2(Foot) / 2(CubicFoot.ukImperial per Pound)
        )
        assertEquals(
            1(Pound.usCustomary per SquareFoot),
            2(Foot) / 2(CubicFoot.usCustomary per Pound)
        )
        assertEquals(
            1(Kilogram per SquareMeter),
            2(Meter).convert(Foot) / 2(CubicMeter per Kilogram)
        )
    }

    @Test
    fun areaDensityFromLinearMassDensityAndLengthTest() {
        assertEquals(1(Kilogram per SquareMeter), 2(Kilogram per Meter) / 2(Meter))
        assertEquals(1(Pound per SquareFoot), 2(Pound per Foot) / 2(Foot))
        assertEquals(1(Pound.ukImperial per SquareFoot), 2(Pound.ukImperial per Foot) / 2(Foot))
        assertEquals(1(Pound.usCustomary per SquareFoot), 2(Pound.usCustomary per Foot) / 2(Foot))
        assertEquals(1(Kilogram per SquareMeter), 2(Kilogram per Meter) / 2(Meter).convert(Foot))
    }

    @Test
    fun areaDensityFromWeightAndAreaTest() {
        assertEquals(1(Kilogram per SquareMeter), 2(Kilogram) / 2(SquareMeter))
        assertEquals(1(Pound per SquareFoot), 2(Pound) / 2(SquareFoot))
        assertEquals(1(Pound.ukImperial per SquareFoot), 2(Pound.ukImperial) / 2(SquareFoot))
        assertEquals(1(Pound.usCustomary per SquareFoot), 2(Pound.usCustomary) / 2(SquareFoot))
        assertEqualScientificValue(
            1(Kilogram per SquareMeter),
            2(Kilogram).convert(Pound) / 2(SquareMeter),
            5
        )
    }
}
