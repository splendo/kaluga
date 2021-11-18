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

import kotlin.test.Test
import kotlin.test.assertEquals

class AngularAccelerationUnitTest {

    @Test
    fun angularAccelerationTest() {
        assertEquals(0.000001, (Radian per Second per Second).convert(1, Radian per Millisecond per Millisecond))
        assertEquals(0.0002777777777778, (Radian per Minute per Minute).convert(1, Radian per Second per Second))
        assertEquals(0.0002777777777778, (Radian per Hour).convert(1, Radian per Minute))
        assertEquals(0.159155, (Radian per Second per Second).convert(1,Turn per Second per Second, 6))
        assertEquals(57.2958, (Radian per Second per Second).convert(1, Degree per Second per Second, 4))
        assertEquals(63.662, (Radian per Second per Second).convert(1, Gradian per Second per Second, 3))
        assertEquals(3437.75, (Radian per Second per Second).convert(1, ArcMinute per Second per Second, 2))
    }
}