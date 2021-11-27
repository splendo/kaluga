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

import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.converter.energy.times
import com.splendo.kaluga.scientific.converter.time.times
import kotlin.test.Test
import kotlin.test.assertEquals

class ActionUnitTest {

    @Test
    fun actionConversionTest() {
        assertScientificConversion(1.0, Joule x Second, 7.716E-8,WattHour x Hour, 11)
    }

    @Test
    fun convertFromEnergyAndTimeTest() {
        assertEquals(4(Joule x Second), 2(Joule) * 2(Second))
        assertEquals(4(Joule x Second), 2(Second) * 2(Joule))
        assertEquals(4(WattHour x Second), 2(WattHour) * 2(Second))
        assertEquals(4(WattHour x Second), 2(Second) * 2(WattHour))
        assertEquals(4(BritishThermalUnit x Second), 2(BritishThermalUnit) * 2(Second))
        assertEquals(4(BritishThermalUnit x Second), 2(Second) * 2(BritishThermalUnit))
    }
}