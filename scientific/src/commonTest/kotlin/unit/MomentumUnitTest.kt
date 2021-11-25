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

import com.splendo.kaluga.scientific.assertEqualScientificValue
import com.splendo.kaluga.scientific.converter.dynamicViscosity.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class MomentumUnitTest {

    @Test
    fun metricMomentumConversionTest() {
        assertEquals(3.6, (Kilogram x (Meter per Second)).convert(1.0, Kilogram x (Kilometer per Hour)))
        assertEquals(3.6, (Dalton x (Meter per Second)).convert(1.0, Dalton x (Kilometer per Hour)))

        assertEquals(0.68, (Pound x (Foot per Second)).convert(1.0, Pound x (Mile per Hour), 2))

        assertEquals(0.68, (ImperialTon x (Foot per Second)).convert(1.0, ImperialTon x (Mile per Hour), 2))
        assertEquals(0.68, (UsTon x (Foot per Second)).convert(1.0, UsTon x (Mile per Hour), 2))
    }

    @Test
    fun momentumFromDynamicViscosityAndAreaTest() {
        // FIXME figure out expected values
        // assertEquals(4(Kilogram x (Meter per Second)), 2(Bar x Second) * 2(SquareMeter))
        // assertEquals(4(Pound x (Foot per Second)), 2(PoundSquareFoot x Second) * 2(SquareFoot))
        // assertEqualScientificValue(4(ImperialTon x (Foot per Second)), 2(ImperialTonSquareFoot x Second) * 2(SquareFoot))
        // assertEqualScientificValue(4(UsTon x (Foot per Second)), 2(USTonSquareFoot x Second) * 2(SquareFoot))
    }
}