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
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.converter.weight.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class YankUnitTest {

    @Test
    fun yankConversionTest() {
        assertScientificConversion(1.0, (Newton per Second), 3.6e8, Dyne per Hour)
    }

    @Test
    fun yankFromForceAndTimeTest() {
        assertEquals(1(Newton per Second), 2(Newton) / 2(Second))
        assertEquals(1(PoundForce per Second), 2(PoundForce) / 2(Second))
        assertEquals(1(ImperialTonForce per Second), 2(ImperialTonForce) / 2(Second))
        assertEquals(1(UsTonForce per Second), 2(UsTonForce) / 2(Second))
    }

    @Test
    fun yankFromMassAndJoltTest() {
        assertEquals(4(Newton per Second), 2(Kilogram) * 2((Meter per Second per Second) per Second))
        assertEqualScientificValue(4(PoundForce per Second), (2 * ImperialStandardGravityAcceleration.value)(Pound) * 2((Foot per Second per Second) per Second), 5)
        assertEqualScientificValue(8960(PoundForce.ukImperial per Second), (2 * ImperialStandardGravityAcceleration.value)(ImperialTon) * 2((Foot per Second per Second) per Second), 5)
        assertEqualScientificValue(8000(PoundForce.usCustomary per Second), (2 * ImperialStandardGravityAcceleration.value)(UsTon) * 2((Foot per Second per Second) per Second), 5)
    }
}
