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

import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.assertEqualScientificValue
import com.splendo.kaluga.scientific.converter.dynamicViscosity.times
import com.splendo.kaluga.scientific.converter.force.times
import com.splendo.kaluga.scientific.converter.weight.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class MomentumUnitTest {

    @Test
    fun metricMomentumConversionTest() {
        assertScientificConversion(1.0, (Kilogram x (Meter per Second)), 3600, Gram x (Kilometer per Hour))
    }

    @Test
    fun momentumFromDynamicViscosityAndAreaTest() {
        assertEquals(4(Kilogram x (Meter per Second)), 2(Pascal x Second) * 2(SquareMeter))
        assertEquals((4 * ImperialStandardGravityAcceleration.value)(Pound x (Foot per Second)), 2(PoundSquareFoot x Second) * 2(SquareFoot))
        assertEqualScientificValue((8960.toDecimal().times(ImperialStandardGravityAcceleration.decimalValue, 5))(Pound.ukImperial x (Foot per Second)), 2(ImperialTonSquareFoot x Second) * 2(SquareFoot), 5)
        assertEqualScientificValue((8000.toDecimal().times(ImperialStandardGravityAcceleration.decimalValue, 5))(Pound.usCustomary x (Foot per Second)), 2(USTonSquareFoot x Second) * 2(SquareFoot), 5)
    }

    @Test
    fun momentumFromForceAndTimeTest() {
        assertEquals(4(Kilogram x (Meter per Second)), 2(Newton) * 2(Second))
        assertEquals((4 * ImperialStandardGravityAcceleration.value)(Pound x (Foot per Second)), 2(PoundForce) * 2(Second))
        assertEquals((4 * ImperialStandardGravityAcceleration.value)(ImperialTon x (Foot per Second)), 2(ImperialTonForce) * 2(Second))
        assertEquals((4 * ImperialStandardGravityAcceleration.value)(UsTon x (Foot per Second)), 2(UsTonForce) * 2(Second))
    }

    @Test
    fun momentumFromMassAndSpeedTest() {
        assertEquals(4(Kilogram x (Meter per Second)), 2(Kilogram) * 2(Meter per Second))
        assertEquals(4(Pound x (Foot per Second)), 2(Pound) * 2(Foot per Second))
        assertEquals(4(ImperialTon x (Foot per Second)), 2(ImperialTon) * 2(Foot per Second))
        assertEquals(4(UsTon x (Foot per Second)), 2(UsTon) * 2(Foot per Second))
    }
}
