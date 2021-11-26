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

import com.splendo.kaluga.scientific.converter.angle.div
import com.splendo.kaluga.scientific.converter.angularAcceleration.times
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class AngularVelocityUnitTest {

    @Test
    fun angularVelocityTest() {
        assertScientificConversion(1, (Radian per Second), 0.1, Centiradian per Millisecond)
    }

    @Test
    fun angularVelocityFromAngleDivTimeTest() {
        assertEquals(1(Radian per Second), 1(Radian) / 1(Second))
    }

    @Test
    fun angularVelocityFromAngularAccelerationTimesTimeTest() {
        assertEquals(4(Radian per Second), 2(Radian per Second per Second) * 2(Second))
        assertEquals(4(Radian per Second), 2(Second) * 2(Radian per Second per Second))
    }
}