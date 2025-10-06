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
import com.splendo.kaluga.scientific.converter.acceleration.div
import com.splendo.kaluga.scientific.converter.yank.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Acceleration
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.GUnit
import com.splendo.kaluga.scientific.unit.ImperialStandardGravityAcceleration
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.test.Test

class JoltUnitTest {

    @Test
    fun joltFromAccelerationDivTimeTest() {
        assertEqualScientificValue(
            1.0(Meter per Second per Second per Second),
            (2(Meter per Second per Second) / 2(Second)),
        )
        assertEqualScientificValue(
            1.0(Foot per Second per Second per Second),
            (2(Foot per Second per Second) / 2(Second)),
        )
        assertEqualScientificValue(
            1.0(GUnit per Second),
            (2(GUnit) / 2(Second)),
        )
        assertEqualScientificValue(
            1.0(GUnit.metric per Second),
            (2(GUnit.metric) / 2(Second)),
        )
        assertEqualScientificValue(
            1.0(GUnit.imperial per Second),
            (2(GUnit.imperial) / 2(Second)),
        )
        assertEqualScientificValue(
            1.0(Meter per Second per Second per Second),
            (2((Meter per Second per Second) as Acceleration) / 2(Second)),
        )
    }

    @Test
    fun joltFromYankAndMassTest() {
        assertEqualScientificValue(
            1.0(Meter per Second per Second per Second),
            2(Newton per Second) / 2(Kilogram),
        )
        assertEqualScientificValue(
            ImperialStandardGravityAcceleration / 1(Second),
            2(PoundForce per Second) / 2(Pound),
            round = 32,
        )
        assertEqualScientificValue(
            ImperialStandardGravityAcceleration / 1(Second),
            2(PoundForce per Second) / 2(Pound.ukImperial),
            round = 32,
        )
        assertEqualScientificValue(
            ImperialStandardGravityAcceleration / 1(Second),
            2(PoundForce per Second) / 2(Pound.usCustomary),
            round = 32,
        )
        assertEqualScientificValue(
            ImperialStandardGravityAcceleration / 1(Second),
            2(PoundForce.ukImperial per Second) / 2(Pound),
            round = 32,
        )
        assertEqualScientificValue(
            ImperialStandardGravityAcceleration / 1(Second),
            2(PoundForce.ukImperial per Second) / 2(Pound.ukImperial),
            round = 32,
        )
        assertEqualScientificValue(
            ImperialStandardGravityAcceleration / 1(Second),
            2(PoundForce.usCustomary per Second) / 2(Pound),
            round = 32,
        )
        assertEqualScientificValue(
            ImperialStandardGravityAcceleration / 1(Second),
            2(PoundForce.usCustomary per Second) / 2(Pound.usCustomary),
            round = 32,
        )
        assertEqualScientificValue(
            1.0(Meter per Second per Second per Second),
            2(Newton per Second) / 2(Kilogram).convert(Pound),
            round = 30,
        )
    }
}
