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

import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.luminousExposure.times
import com.splendo.kaluga.scientific.converter.luminousFlux.times
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class LuminousEnergyUnitTest {

    @Test
    fun luminousEnergyConversionTest() {
        assertScientificConversion(1.0, Lumen x Second, 0.027778, Centilumen x Hour, 6)
    }

    @Test
    fun luminousEnergyFromLuminousExposureAndAreaTest() {
        assertEquals(4(Lumen x Second), 2(Lux x Second) * 2(SquareMeter))
        assertEquals(4(Lumen x Second), 2(SquareMeter) * 2(Lux x Second))
        assertEquals(4(Lumen x Second), 2(FootCandle x Second) * 2(SquareFoot))
        assertEquals(4(Lumen x Second), 2(SquareFoot) * 2(FootCandle x Second))
    }

    @Test
    fun luminousEnergyFromLuminousFluxAndTimeTest() {
        assertEquals(4(Lumen x Second), 2(Lumen) * 2(Second))
        assertEquals(4(Lumen x Second), 2(Second) * 2(Lumen))
    }
}
