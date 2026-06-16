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
import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.areaDensity.times
import com.splendo.kaluga.scientific.converter.density.times
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.converter.weight.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.test.Test

class LinearMassDensityUnitTest {

    @Test
    fun linearMassDensityFromAreaAndSpecificVolumeTest() {
        assertEqualScientificValue(1(Kilogram per Meter), 2(SquareMeter) / 2(CubicMeter per Kilogram))
        assertEqualScientificValue(1(Pound per Foot), 2(SquareFoot) / 2(CubicFoot per Pound), round = 30)
        assertEqualScientificValue(
            1(Pound.ukImperial per Foot),
            2(SquareFoot) / 2(CubicFoot.ukImperial per Pound.ukImperial),
            round = 30,
        )
        assertEqualScientificValue(
            1(Pound.usCustomary per Foot),
            2(SquareFoot) / 2(CubicFoot.usCustomary per Pound.usCustomary),
            round = 30,
        )
        assertEqualScientificValue(
            1(Kilogram per Meter),
            2(SquareMeter).convert(SquareFoot) / 2(CubicMeter per Kilogram),
            round = 32,
        )
    }

    @Test
    fun linearMassDensityFromAreaDensityAndLengthTest() {
        assertEqualScientificValue(4(Kilogram per Meter), 2(Kilogram per SquareMeter) * 2(Meter))
        assertEqualScientificValue(4(Kilogram per Meter), 2(Meter) * 2(Kilogram per SquareMeter))
        assertEqualScientificValue(4(Pound per Foot), 2(Pound per SquareFoot) * 2(Foot), round = 32)
        assertEqualScientificValue(4(Pound per Foot), 2(Foot) * 2(Pound per SquareFoot), round = 32)
        assertEqualScientificValue(4(Pound.ukImperial per Foot), 2(Pound.ukImperial per SquareFoot) * 2(Foot), round = 32)
        assertEqualScientificValue(4(Pound.ukImperial per Foot), 2(Foot) * 2(Pound.ukImperial per SquareFoot), round = 32)
        assertEqualScientificValue(4(Pound.usCustomary per Foot), 2(Pound.usCustomary per SquareFoot) * 2(Foot), round = 32)
        assertEqualScientificValue(4(Pound.usCustomary per Foot), 2(Foot) * 2(Pound.usCustomary per SquareFoot), round = 32)
        assertEqualScientificValue(4(Kilogram per Meter), 2(Kilogram per SquareMeter) * 2(Meter).convert(Foot), round = 32)
        assertEqualScientificValue(4(Kilogram per Meter), 2(Meter).convert(Foot) * 2(Kilogram per SquareMeter), round = 32)
    }

    @Test
    fun linearMassDensityFromDensityAndLengthTest() {
        assertEqualScientificValue(4(Kilogram per Meter), 2(Kilogram per CubicMeter) * 2(SquareMeter))
        assertEqualScientificValue(4(Kilogram per Meter), 2(SquareMeter) * 2(Kilogram per CubicMeter))
        assertEqualScientificValue(4(Pound per Foot), 2(Pound per CubicFoot) * 2(SquareFoot), round = 32)
        assertEqualScientificValue(4(Pound per Foot), 2(SquareFoot) * 2(Pound per CubicFoot), round = 32)
        assertEqualScientificValue(
            4(Pound.ukImperial per Foot),
            2(Pound.ukImperial per CubicFoot) * 2(SquareFoot),
            round = 32,
        )
        assertEqualScientificValue(
            4(Pound.ukImperial per Foot),
            2(SquareFoot) * 2(Pound.ukImperial per CubicFoot),
            round = 32,
        )
        assertEqualScientificValue(
            4(Pound.usCustomary per Foot),
            2(Pound.usCustomary per CubicFoot) * 2(SquareFoot),
            round = 32,
        )
        assertEqualScientificValue(
            4(Pound.usCustomary per Foot),
            2(SquareFoot) * 2(Pound.usCustomary per CubicFoot),
            round = 32,
        )
        assertEqualScientificValue(
            4(Kilogram per Meter),
            2(Kilogram per CubicMeter) * 2(SquareMeter).convert(SquareFoot),
            round = 32,
        )
        assertEqualScientificValue(
            4(Kilogram per Meter),
            2(SquareMeter).convert(SquareFoot) * 2(Kilogram per CubicMeter),
            round = 32,
        )
    }

    @Test
    fun linearMassDensityFromWeightAndAreaTest() {
        assertEqualScientificValue(1(Kilogram per Meter), 2(Kilogram) / 2(Meter))
        assertEqualScientificValue(1(Pound per Foot), 2(Pound) / 2(Foot), round = 32)
        assertEqualScientificValue(1(Pound.ukImperial per Foot), 2(Pound.ukImperial) / 2(Foot), round = 32)
        assertEqualScientificValue(1(Pound.usCustomary per Foot), 2(Pound.usCustomary) / 2(Foot), round = 32)
        assertEqualScientificValue(1(Kilogram per Meter), 2(Kilogram) / 2(Meter).convert(Foot), round = 32)
    }
}
