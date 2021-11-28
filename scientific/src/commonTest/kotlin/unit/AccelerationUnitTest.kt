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
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.converter.jolt.times
import com.splendo.kaluga.scientific.converter.speed.div
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class AccelerationUnitTest {

    @Test
    fun accelerationConversionTest() {
        assertScientificConversion(
            1,
            (Meter per Second per Second),
            11.811024,
            Foot per Millisecond per Hour,
            6
        )
    }

    @Test
    fun accelerationFromForceAndMassTest() {
        assertEquals(1(Meter per Second per Second), 2(Newton) / 2(Kilogram))
        assertEquals(1(Centimeter per Second per Second), 2(Dyne) / 2(Gram))
        assertEquals(1(Centimeter per Second per Second), 20(Decidyne) / 2(Gram))
        assertEquals(ImperialStandardGravityAcceleration, (2(PoundForce) / 2(Pound)))
        assertEquals(1(Foot per Second per Second), (2(Poundal) / 2(Pound)))
        assertEquals(1(Foot per Second per Second), (2(Poundal) / 2(Pound.ukImperial)))
        assertEquals(1(Foot per Second per Second), (2(Poundal) / 2(Pound.usCustomary)))
        assertEquals(1(Foot per Second per Second), (2(Poundal.ukImperial) / 2(Pound)))
        assertEquals(1(Foot per Second per Second), (2(Poundal.ukImperial) / 2(Pound.ukImperial)))
        assertEquals(1(Foot per Second per Second), (2(Poundal.usCustomary) / 2(Pound)))
        assertEquals(1(Foot per Second per Second), (2(Poundal.usCustomary) / 2(Pound.usCustomary)))
        assertEquals(MetricStandardGravityAcceleration, 2(PoundForce) / 2(Pound).convert(Kilogram))
    }

    @Test
    fun accelerationFromJoltAndTimeTest() {
        assertEquals(
            4(Meter per Second per Second),
            2(Meter per Second per Second per Second) * 2(Second)
        )
        assertEquals(
            4(Meter per Second per Second),
            2(Second) * 2(Meter per Second per Second per Second)
        )
        assertEquals(
            4(Foot per Second per Second),
            2(Foot per Second per Second per Second) * 2(Second)
        )
        assertEquals(
            4(Foot per Second per Second),
            2(Second) * 2(Foot per Second per Second per Second)
        )
        assertEqualScientificValue(
            4(Meter per Second per Second),
            2((Meter per Second per Second per Second) as Jolt) * 2(Second)
        )
        assertEqualScientificValue(
            4(Meter per Second per Second),
            2(Second) * 2((Meter per Second per Second per Second) as Jolt)
        )
    }

    @Test
    fun accelerationFromSpeedAndTime() {
        assertEquals(1(Meter per Second per Second), 2(Meter per Second) / 2(Second))
        assertEquals(1(Foot per Second per Second), 2(Foot per Second) / 2(Second))
        assertEquals(1(Meter per Second per Second), 2((Meter per Second) as Speed) / 2(Second))
    }
}