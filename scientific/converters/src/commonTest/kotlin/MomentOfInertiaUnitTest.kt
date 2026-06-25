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
import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.momentOfInertia.div
import com.splendo.kaluga.scientific.converter.weight.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test

class MomentOfInertiaUnitTest {

    @Test
    fun momentOfInertiaFromWeightAndAreaTest() {
        assertEqualScientificValue(4(Kilogram x SquareMeter), (2(Kilogram) * 2(SquareMeter)).convert(Kilogram x SquareMeter))
        assertEqualScientificValue(4(Kilogram x SquareMeter), (2(SquareMeter) * 2(Kilogram)).convert(Kilogram x SquareMeter))
    }

    @Test
    fun weightFromMomentOfInertiaAndAreaTest() {
        assertEqualScientificValue(2(Kilogram), (4(Kilogram x SquareMeter) / 2(SquareMeter)).convert(Kilogram))
    }

    @Test
    fun areaFromMomentOfInertiaAndWeightTest() {
        assertEqualScientificValue(2(SquareMeter), (4(Kilogram x SquareMeter) / 2(Kilogram)).convert(SquareMeter))
    }
}
