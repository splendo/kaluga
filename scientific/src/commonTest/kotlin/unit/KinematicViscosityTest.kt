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

import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.area.div
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class KinematicViscosityTest {

    @Test
    fun kinematicViscosityFromAreaAndTimeTest() {
        assertEquals(1(SquareCentimeter per Second), (2(Centimeter) * 1(Centimeter)) / 2(Second))
        assertEquals(1(SquareMeter per Second), (2(SquareMeter)) / 2(Second))
        assertEquals(1(SquareInch per Second), (2(Inch) * 1(Inch)) / 2(Second))
        assertEquals(1(SquareFoot per Second), (2(SquareFoot)) / 2(Second))

        assertEquals(1(SquareMeter per Second), (2(Meter).convert(Foot) * 1(Meter)) / 2(Second))
        assertEquals(1(SquareFoot per Second), (2(Foot).convert(Inch) * 1(Foot)) / 2(Second))
    }
}
