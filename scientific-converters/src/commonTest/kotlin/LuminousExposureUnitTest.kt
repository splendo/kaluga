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
import com.splendo.kaluga.scientific.converter.illuminance.times
import com.splendo.kaluga.scientific.converter.luminousEnergy.div
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.FootCandle
import com.splendo.kaluga.scientific.unit.Illuminance
import com.splendo.kaluga.scientific.unit.Lumen
import com.splendo.kaluga.scientific.unit.Lux
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test

class LuminousExposureUnitTest {

    @Test
    fun luminousExposureFromIlluminanceAndTimeTest() {
        assertEqualScientificValue(4(Lux x Second), 2(Lux) * 2(Second))
        assertEqualScientificValue(4(Lux x Second), 2(Second) * 2(Lux))
        assertEqualScientificValue(4(FootCandle x Second), 2(FootCandle) * 2(Second), round = 32)
        assertEqualScientificValue(4(FootCandle x Second), 2(Second) * 2(FootCandle), round = 32)
        assertEqualScientificValue(4(Lux x Second), 2(Lux as Illuminance) * 2(Second))
        assertEqualScientificValue(4(Lux x Second), 2(Second) * 2(Lux as Illuminance))
    }

    @Test
    fun luminousExposureFromLuminousEnergyAndAreaTest() {
        assertEqualScientificValue(1(Lux x Second), 2(Lumen x Second) / 2(SquareMeter))
        assertEqualScientificValue(1(FootCandle x Second), 2(Lumen x Second) / 2(SquareFoot), round = 32)
        assertEqualScientificValue(1(Lux x Second), 2(Lumen x Second) / 2(SquareMeter).convert(SquareFoot as Area), round = 32)
    }
}
