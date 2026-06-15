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

import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.illuminance.times
import com.splendo.kaluga.scientific.converter.luminousEnergy.div
import com.splendo.kaluga.scientific.converter.luminousIntensity.times
import com.splendo.kaluga.scientific.converter.solidAngle.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Candela
import com.splendo.kaluga.scientific.unit.FootCandle
import com.splendo.kaluga.scientific.unit.Lumen
import com.splendo.kaluga.scientific.unit.Lux
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.Steradian
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test
import com.splendo.kaluga.test.base.IgnoreJs

@IgnoreJs
class LuminusFluxUnitTest {

    @Test
    fun luminousFluxFromIlluminanceAndAreaTest() {
        assertEqualScientificValue(4(Lumen), 2(Lux) * 2(SquareMeter))
        assertEqualScientificValue(4(Lumen), 2(SquareMeter) * 2(Lux))
        assertEqualScientificValue(4(Lumen), 2(FootCandle) * 2(SquareFoot), round = 32)
        assertEqualScientificValue(4(Lumen), 2(SquareFoot) * 2(FootCandle), round = 32)
    }

    @Test
    fun luminousFluxFromLuminousEnergyAndTimeTest() {
        assertEqualScientificValue(1(Lumen), 2(Lumen x Second) / 2(Second))
    }

    @Test
    fun luminousFluxFromIntensityAndSolidAngleTest() {
        assertEqualScientificValue(4(Lumen), 2(Candela) * 2(Steradian))
        assertEqualScientificValue(4(Lumen), 2(Steradian) * 2(Candela))
    }
}
