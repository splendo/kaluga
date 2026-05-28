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
import com.splendo.kaluga.scientific.converter.energy.times
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.BritishThermalUnit
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test
import com.splendo.kaluga.test.base.IgnoreJs

@IgnoreJs
class ActionUnitTest {

    @Test
    fun convertFromEnergyAndTimeTest() {
        assertEqualScientificValue(4(Joule x Second), 2(Joule) * 2(Second))
        assertEqualScientificValue(4(Joule x Second), 2(Second) * 2(Joule))
        assertEqualScientificValue(4(WattHour x Second), 2(WattHour) * 2(Second))
        assertEqualScientificValue(4(WattHour x Second), 2(Second) * 2(WattHour))
        assertEqualScientificValue(4(BritishThermalUnit x Second), 2(BritishThermalUnit) * 2(Second), round = 32)
        assertEqualScientificValue(4(BritishThermalUnit x Second), 2(Second) * 2(BritishThermalUnit), round = 32)
        assertEqualScientificValue(4(Joule x Second), 2(Joule).convert(WattHour as Energy) * 2(Second), round = 32)
        assertEqualScientificValue(4(Joule x Second), 2(Second) * 2(Joule).convert(WattHour as Energy), round = 32)
    }
}
