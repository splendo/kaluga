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
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.acceleration.div
import com.splendo.kaluga.scientific.converter.yank.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class JoltUnitTest {

    @Test
    fun joltTest() {
        assertScientificConversion(
            1,
            (Meter per Second per Second per Second),
            11.8110236,
            Foot per Minute per Millisecond per Minute,
            7,
        )
    }

    @Test
    fun joltFromAccelerationDivTimeTest() {
        assertEquals(
            1.0(Meter per Second per Second per Second),
            (2(Meter per Second per Second) / 2(Second)),
        )
        assertEquals(
            1.0(Foot per Second per Second per Second),
            (2(Foot per Second per Second) / 2(Second)),
        )
        assertEquals(
            1.0(GUnit per Second),
            (2(GUnit) / 2(Second)),
        )
        assertEquals(
            1.0(GUnit.metric per Second),
            (2(GUnit.metric) / 2(Second)),
        )
        assertEquals(
            1.0(GUnit.imperial per Second),
            (2(GUnit.imperial) / 2(Second)),
        )
        assertEquals(
            1.0(Meter per Second per Second per Second),
            (2((Meter per Second per Second) as Acceleration) / 2(Second)),
        )
    }

    @Test
    fun joltFromYankAndMassTest() {
        assertEquals(
            1.0(Meter per Second per Second per Second),
            2(Newton per Second) / 2(Kilogram),
        )
        assertEquals(
            ImperialStandardGravityAcceleration / 1(Second),
            2(PoundForce per Second) / 2(Pound),
        )
        assertEquals(
            ImperialStandardGravityAcceleration / 1(Second),
            2(PoundForce per Second) / 2(Pound.ukImperial),
        )
        assertEquals(
            ImperialStandardGravityAcceleration / 1(Second),
            2(PoundForce per Second) / 2(Pound.usCustomary),
        )
        assertEquals(
            ImperialStandardGravityAcceleration / 1(Second),
            2(PoundForce.ukImperial per Second) / 2(Pound),
        )
        assertEquals(
            ImperialStandardGravityAcceleration / 1(Second),
            2(PoundForce.ukImperial per Second) / 2(Pound.ukImperial),
        )
        assertEquals(
            ImperialStandardGravityAcceleration / 1(Second),
            2(PoundForce.usCustomary per Second) / 2(Pound),
        )
        assertEquals(
            ImperialStandardGravityAcceleration / 1(Second),
            2(PoundForce.usCustomary per Second) / 2(Pound.usCustomary),
        )
        assertEqualScientificValue(
            1.0(Meter per Second per Second per Second),
            2(Newton per Second) / 2(Kilogram).convert(Pound),
            9,
        )
    }
}
