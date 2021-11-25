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

import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.converter.volumetricFlux.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class VolumetricFlowUnitTest {

    @Test
    fun volumetricFlowConversionTest() {
        // TODO
        CubicMeter per Second
    }

    @Test
    fun volumetricFlowFromVolumeAndTimeTest() {
        assertEquals(1(CubicMeter per Second), 2(CubicMeter) / 2(Second))
        assertEquals(1(CubicFoot per Second), 2(CubicFoot) / 2(Second))
        assertEquals(1(ImperialGallon per Second), 2(ImperialGallon) / 2(Second))
        assertEquals(1(UsLiquidGallon per Second), 2(UsLiquidGallon) / 2(Second))
    }

    @Test
    fun volumetricFlowFromVolumetricFluxAndAreaTest() {
        assertEquals(4(CubicMeter per Second), 2((CubicMeter per Second) per SquareMeter) * 2(SquareMeter))
        assertEquals(4(CubicFoot per Second), 2((CubicFoot per Second) per SquareFoot) * 2(SquareFoot))
    }
}
