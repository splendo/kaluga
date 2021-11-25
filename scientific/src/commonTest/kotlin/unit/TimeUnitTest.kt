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
import com.splendo.kaluga.scientific.converter.amountOfSubstance.div
import com.splendo.kaluga.scientific.converter.angle.div
import com.splendo.kaluga.scientific.converter.angularVelocity.div
import com.splendo.kaluga.scientific.converter.speed.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class TimeUnitTest {

    @Test
    fun secondConversionTest() {
        assertEquals(1e+9, Second.convert(1.0, Nanosecond))
        assertEquals(1e+6, Second.convert(1.0, Microsecond))
        assertEquals(1_000.0, Second.convert(1.0, Millisecond))
        assertEquals(100.0, Second.convert(1.0, Centisecond))
        assertEquals(10.0, Second.convert(1.0, Decisecond))
        assertEquals(0.017, Second.convert(1.0, Minute, 3))
        assertEquals(0.00028, Second.convert(1.0, Hour, 5))
    }

    @Test
    fun timeFromAccelerationAndJoltTest(){
        // 2(Meter per Second per Second) / 2((Meter per Second per Second) per Second) FIXME
    }

    @Test
    fun timeFromSpeedDivAccelerationTest() {
        assertEqualScientificValue(1(Second), 2(Meter per Second) / 2(Meter per Second per Second))
    }

    @Test
    fun timeFromAmountOfSubstanceDivCatalysticActivityTest() {
        assertEquals(1(Second), 2(Mole) / 2(Katal))
    }

    @Test
    fun timeFromAngleDivAngularVelocityTest() {
        assertEquals(1(Second), 2(Radian) / 2(Radian per Second))
    }

    @Test
    fun timeFromAngularVelocityDivAngularAccelerationTest() {
        assertEquals(1(Second), 2(Radian per Second) / 2(Radian per Second per Second))
    }

    // TODO rest

}