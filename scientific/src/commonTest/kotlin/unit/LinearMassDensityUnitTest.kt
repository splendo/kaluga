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

import com.splendo.kaluga.scientific.converter.area.div
import com.splendo.kaluga.scientific.converter.areaDensity.times
import com.splendo.kaluga.scientific.converter.density.times
import com.splendo.kaluga.scientific.converter.weight.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class LinearMassDensityUnitTest {

    @Test
    fun linearMassDensityConversionTest() {
        // assertEquals(6.022e+23, (Gram per Meter).convert(1, Dalton per Meter, 0)) FIXME yields diff
        // assertEquals(2.20462, (Kilogram per Meter).convert(1, Pound per Meter,5)) FIXME yields diff
    }

    @Test
    fun linearMassDensityFromAreaAndSpecificVolumeTest() {
        assertEquals(1(Kilogram per Meter), 2(SquareMeter) / 2(CubicMeter per Kilogram))
        assertEquals(1(Pound per Foot), 2(SquareFoot) / 2(CubicFoot per Pound))
    }

    @Test
    fun linearMassDensityFromAreaDensityAndLengthTest() {
        assertEquals(4(Kilogram per Meter), 2(Kilogram per SquareMeter) * 2(Meter))
        assertEquals(4(Pound per Foot), 2(Pound per SquareFoot) * 2(Foot))
    }

    @Test
    fun linearMassDensityFromDensityAndLengthTest() {
        assertEquals(4(Kilogram per Meter), 2(Kilogram per CubicMeter) * 2(SquareMeter))
        assertEquals(4(Pound per Foot), 2(Pound per CubicFoot) * 2(SquareFoot))
    }

    @Test
    fun linearMassDensityFromWeightAndAreaTest() {
        assertEquals(1(Kilogram per Meter), 2(Kilogram) / 2(Meter))
        assertEquals(1(Pound per Foot), 2(Pound) / 2(Foot))
    }
}