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
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.converter.jolt.times
import com.splendo.kaluga.scientific.converter.speed.div
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Decidyne
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.GUnit
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.ImperialStandardGravityAcceleration
import com.splendo.kaluga.scientific.unit.Jolt
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricStandardGravityAcceleration
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Poundal
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Speed
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.test.Test

class AccelerationUnitTest {

    @Test
    fun accelerationFromForceAndMassTest() {
        assertEqualScientificValue(1(Meter per Second per Second), 2(Newton) / 2(Kilogram))
        assertEqualScientificValue(1(Centimeter per Second per Second), 2(Dyne) / 2(Gram))
        assertEqualScientificValue(1(Centimeter per Second per Second), 20(Decidyne) / 2(Gram))
        assertEqualScientificValue(ImperialStandardGravityAcceleration, (2(PoundForce) / 2(Pound)))
        assertEqualScientificValue(1(Foot per Second per Second), (2(Poundal) / 2(Pound)))
        assertEqualScientificValue(1(Foot per Second per Second), (2(Poundal) / 2(Pound.ukImperial)))
        assertEqualScientificValue(1(Foot per Second per Second), (2(Poundal) / 2(Pound.usCustomary)))
        assertEqualScientificValue(1(Foot per Second per Second), (2(Poundal.ukImperial) / 2(Pound)))
        assertEqualScientificValue(1(Foot per Second per Second), (2(Poundal.ukImperial) / 2(Pound.ukImperial)))
        assertEqualScientificValue(1(Foot per Second per Second), (2(Poundal.usCustomary) / 2(Pound)))
        assertEqualScientificValue(1(Foot per Second per Second), (2(Poundal.usCustomary) / 2(Pound.usCustomary)))
        assertEqualScientificValue(MetricStandardGravityAcceleration, 2(PoundForce) / 2(Pound).convert(Kilogram))
    }

    @Test
    fun accelerationFromJoltAndTimeTest() {
        assertEqualScientificValue(
            4(Meter per Second per Second),
            2(Meter per Second per Second per Second) * 2(Second),
        )
        assertEqualScientificValue(
            4(Meter per Second per Second),
            2(Second) * 2(Meter per Second per Second per Second),
        )
        assertEqualScientificValue(
            4(GUnit),
            2(GUnit per Second) * 2(Second),
        )
        assertEqualScientificValue(
            4(GUnit),
            2(Second) * 2(GUnit per Second),
        )
        assertEqualScientificValue(
            4(Foot per Second per Second),
            2(Foot per Second per Second per Second) * 2(Second),
        )
        assertEqualScientificValue(
            4(Foot per Second per Second),
            2(Second) * 2(Foot per Second per Second per Second),
        )
        assertEqualScientificValue(
            4(Meter per Second per Second),
            2((Meter per Second per Second per Second) as Jolt) * 2(Second),
        )
        assertEqualScientificValue(
            4(Meter per Second per Second),
            2(Second) * 2((Meter per Second per Second per Second) as Jolt),
        )
    }

    @Test
    fun accelerationFromSpeedAndTime() {
        assertEqualScientificValue(1(Meter per Second per Second), 2(Meter per Second) / 2(Second))
        assertEqualScientificValue(1(Foot per Second per Second), 2(Foot per Second) / 2(Second))
        assertEqualScientificValue(1(Meter per Second per Second), 2(Meter per Second).convert((Foot per Second) as Speed) / 2(Second), round = 32)
    }
}
