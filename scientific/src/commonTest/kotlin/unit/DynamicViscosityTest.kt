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

import com.splendo.kaluga.scientific.converter.momentum.div
import com.splendo.kaluga.scientific.converter.pressure.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class DynamicViscosityTest {

    @Test
    fun dynamicViscosityConversionTest() {
        // assertEquals(1.0, (Bar x Second).convert(2.9,PoundSquareFoot x Second,4)) FIXME
    }

    @Test
    fun dynamicViscosityFromMomentumAndAreaTest(){
        assertEquals(1(Pascal x Second), 2(Kilogram x (Meter per Second)) / 2(SquareMeter))
        // FIXME does that yield PoundSquareInch? PoundSquareFoot does not work, result is also not 1
        // assertEquals(1(PoundSquareInch x Second), 2(Pound x (Foot per Second)) / 2(SquareFoot))
    }

    @Test
    fun dynamicViscosityFromPressureAndTimeTest(){
        assertEquals(4(Pascal x Second), 2(Pascal) * 2(Second))
        assertEquals(4(PoundSquareFoot x Second), 2(PoundSquareFoot) * 2(Second))
        assertEquals(4(KipSquareFoot x Second), 2(KipSquareFoot) * 2(Second))
        assertEquals(4(ImperialTonSquareFoot x Second), 2(ImperialTonSquareFoot) * 2(Second))
    }
}