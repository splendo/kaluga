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

import com.splendo.kaluga.scientific.converter.energy.times
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class ActionUnitTest {

    @Test
    fun actionUnitConversionTest() {
        assertScientificConversion(
            3600,
            Joule x Second,
            1,
            WattHour x Second
        )
        assertScientificConversion(
            1.35582,
            Joule x Second,
            1,
            FootPoundForce x Second,
            5
        )
        // FIXME not sure if expected is correct, yields something strange
        /*assertScientificConversion(
            0.000376616,
            WattHour x Second,
            1,
            FootPoundForce x Second
        )*/
    }

    @Test
    fun actionFromEnergyAndTimeTest() {
        assertEquals(4(Joule x Second), 2(Joule) * 2(Second))
        assertEquals(4(Joule x Second), 2(Second) * 2(Joule))
        assertEquals(4(Calorie x Second), 2(Calorie) * 2(Second))
        assertEquals(4(Calorie x Second), 2(Second) * 2(Calorie))
        assertEquals(4(FootPoundForce x Second), 2(FootPoundForce) * 2(Second))
        assertEquals(4(FootPoundForce x Second), 2(Second) * 2(FootPoundForce))
    }
}