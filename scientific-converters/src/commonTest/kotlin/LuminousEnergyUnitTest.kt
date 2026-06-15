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
import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.luminousExposure.times
import com.splendo.kaluga.scientific.converter.luminousFlux.times
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.FootCandle
import com.splendo.kaluga.scientific.unit.Lumen
import com.splendo.kaluga.scientific.unit.Lux
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test
import com.splendo.kaluga.test.base.IgnoreJs

@IgnoreJs
class LuminousEnergyUnitTest {

    @Test
    fun luminousEnergyFromLuminousExposureAndAreaTest() {
        assertEqualScientificValue(4(Lumen x Second), 2(Lux x Second) * 2(SquareMeter))
        assertEqualScientificValue(4(Lumen x Second), 2(SquareMeter) * 2(Lux x Second))
        assertEqualScientificValue(4(Lumen x Second), 2(FootCandle x Second) * 2(SquareFoot), round = 32)
        assertEqualScientificValue(4(Lumen x Second), 2(SquareFoot) * 2(FootCandle x Second), round = 32)
        assertEqualScientificValue(4(Lumen x Second), 2(Lux x Second) * 2(SquareMeter).convert(SquareFoot), round = 32)
        assertEqualScientificValue(4(Lumen x Second), 2(SquareMeter).convert(SquareFoot) * 2(Lux x Second), round = 32)
    }

    @Test
    fun luminousEnergyFromLuminousFluxAndTimeTest() {
        assertEqualScientificValue(4(Lumen x Second), 2(Lumen) * 2(Second))
        assertEqualScientificValue(4(Lumen x Second), 2(Second) * 2(Lumen))
    }
}
