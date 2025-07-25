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
import com.splendo.kaluga.scientific.converter.volumetricFlow.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialGallon
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.UsLiquidGallon
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test

class VolumetricFluxUnitTest {

    @Test
    fun volumetricFluxFromVolumetricFlowAndAreaTest() {
        assertEqualScientificValue(1(CubicMeter per Second per SquareMeter), 2(CubicMeter per Second) / 2(SquareMeter))
        assertEqualScientificValue(1(CubicFoot per Second per SquareFoot), 2(CubicFoot per Second) / 2(SquareFoot))
        assertEqualScientificValue(1(ImperialGallon per Second per SquareFoot), 2(ImperialGallon per Second) / 2(SquareFoot))
        assertEqualScientificValue(1(UsLiquidGallon per Second per SquareFoot), 2(UsLiquidGallon per Second) / 2(SquareFoot))
        assertEqualScientificValue(1(CubicMeter per Second per SquareMeter), 2(CubicMeter per Second) / 2(SquareMeter).convert(SquareFoot))
    }
}
