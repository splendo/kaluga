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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.scientific.assertEqualScientificValue
import com.splendo.kaluga.scientific.converter.angularVelocity.times
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test

class AngleUnitTest {

    @Test
    fun angleConversionTest() {
        assertScientificConversion(1, Radian, 1000000000.0, Nanoradian)
        assertScientificConversion(1, Radian, 1000000.0, Microradian)
        assertScientificConversion(1, Radian, 1000.0, Milliradian)
        assertScientificConversion(1, Radian, 100.0, Centiradian)
        assertScientificConversion(1, Radian, 10.0, Deciradian)

        assertScientificConversion(1, Radian, 0.159155, Turn, 6)
        assertScientificConversion(1, Radian, 159154.9431, Microturn, 4)
        assertScientificConversion(1, Radian, 159.1549, Milliturn, 4)
        assertScientificConversion(1, Radian, 15.9155, Centiturn, 4)
        assertScientificConversion(1, Radian, 1.5915, Deciturn, 4)
        assertScientificConversion(1, Radian, 57.2958, Degree, 4)
        assertScientificConversion(1, Radian, 63.662, Gradian, 3)
        assertScientificConversion(1, Radian, 3437.75, ArcMinute, 2)
        assertScientificConversion(1, Radian, 206265.0, ArcSecond, 0)
    }

    @Test
    fun angleFromAngularVelocityAndTimeTest() {
        assertEqualScientificValue(4(Deciradian), 2(Deciradian per Minute) * 2(Minute))
        assertEqualScientificValue(4(Deciradian), 2(Minute) * 2(Deciradian per Minute))
    }
}
