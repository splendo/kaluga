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
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.converter.weight.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.ImperialStandardGravityAcceleration
import com.splendo.kaluga.scientific.unit.ImperialTonForce
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.UsTonForce
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.test.Test
import com.splendo.kaluga.test.base.IgnoreJs

@IgnoreJs
class YankUnitTest {

    @Test
    fun yankFromForceAndTimeTest() {
        assertEqualScientificValue(1(Newton per Second), 2(Newton) / 2(Second))
        assertEqualScientificValue(1(PoundForce per Second), 2(PoundForce) / 2(Second))
        assertEqualScientificValue(1(ImperialTonForce per Second), 2(ImperialTonForce) / 2(Second))
        assertEqualScientificValue(1(UsTonForce per Second), 2(UsTonForce) / 2(Second))
        assertEqualScientificValue(1(Newton per Second), 2(Newton).convert(PoundForce as Force) / 2(Second), round = 30)
    }

    @Test
    fun yankFromMassAndJoltTest() {
        assertEqualScientificValue(
            4(Newton per Second),
            2(Kilogram) * 2((Meter per Second per Second) per Second),
        )
        assertEqualScientificValue(
            4(PoundForce per Second),
            2(Pound) * (ImperialStandardGravityAcceleration / 0.5(Second)),
            round = 32,
        )
        assertEqualScientificValue(
            4(PoundForce.ukImperial per Second),
            2(Pound.ukImperial) * (ImperialStandardGravityAcceleration / 0.5(Second)),
            round = 32,
        )
        assertEqualScientificValue(
            4(PoundForce.usCustomary per Second),
            2(Pound.usCustomary) * (ImperialStandardGravityAcceleration / 0.5(Second)),
            round = 32,
        )
        assertEqualScientificValue(
            4(Newton per Second),
            2(Kilogram).convert(Pound) * 2((Meter per Second per Second) per Second),
            round = 28,
        )
    }
}
