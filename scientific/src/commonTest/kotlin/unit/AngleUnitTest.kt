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

class AngleUnitTest {

    @Test
    fun angleConversionTest() {
        assertEquals(1000000000.0, Radian.convert(1, Nanoradian))
        assertEquals(1000000.0, Radian.convert(1, Microradian))
        assertEquals(1000.0, Radian.convert(1, Milliradian))
        assertEquals(100.0, Radian.convert(1, Centiradian))
        assertEquals(10.0, Radian.convert(1, Deciradian))

        assertEquals(0.159155, Radian.convert(1, Turn, 6))
        assertEquals(159154.9431, Radian.convert(1, Microturn, 4))
        assertEquals(159.1549, Radian.convert(1, Milliturn, 4))
        assertEquals(15.9155, Radian.convert(1, Centiturn, 4))
        assertEquals(1.5915, Radian.convert(1, Deciturn, 4))
        assertEquals(57.2958, Radian.convert(1, Degree, 4))
        assertEquals(63.662, Radian.convert(1, Gradian, 3))
        assertEquals(3437.75, Radian.convert(1, ArcMinute, 2))
        assertEquals(206265.0, Radian.convert(1, ArcSecond, 0))
    }
}