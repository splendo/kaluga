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

import com.splendo.kaluga.scientific.converter.angle.div
import com.splendo.kaluga.scientific.converter.angularAcceleration.times
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Centiradian
import com.splendo.kaluga.scientific.unit.Millisecond
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test
import kotlin.test.assertEquals

class AngularVelocityUnitTest {

    @Test
    fun angularVelocityFromAngleDivTimeTest() {
        assertEquals(1(Centiradian per Millisecond), 1(Centiradian) / 1(Millisecond))
    }

    @Test
    fun angularVelocityFromAngularAccelerationTimesTimeTest() {
        assertEquals(
            4(Centiradian per Millisecond),
            2(Centiradian per Millisecond per Millisecond) * 2(Millisecond),
        )
        assertEquals(
            4(Centiradian per Millisecond),
            2(Millisecond) * 2(Centiradian per Millisecond per Millisecond),
        )
    }
}
