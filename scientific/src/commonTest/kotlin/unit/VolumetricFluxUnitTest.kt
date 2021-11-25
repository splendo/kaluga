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

import com.splendo.kaluga.scientific.converter.volumetricFlow.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class VolumetricFluxUnitTest {

    @Test
    fun volumetricFluxConversionTest() {
        assertScientificConversion(1.0, (CubicMeter per Second per SquareMeter), 196.85, CubicFoot per Minute per SquareFoot, 2)
    }

    @Test
    fun volumetricFluxFromVolumetricFlowAndAreaTest() {
        assertEquals(1(CubicMeter per Second per SquareMeter), 2(CubicMeter per Second) / 2(SquareMeter))
        assertEquals(1(CubicFoot per Second per SquareFoot), 2(CubicFoot per Second) / 2(SquareFoot))
    }
}
