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

import com.splendo.kaluga.base.decimal.toDecimal
import com.splendo.kaluga.scientific.converter.decimal.div
import com.splendo.kaluga.scientific.converter.electricConductance.div
import com.splendo.kaluga.scientific.converter.electricResistance.div
import com.splendo.kaluga.scientific.converter.time.frequency
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.BeatsPerMinute
import com.splendo.kaluga.scientific.unit.Farad
import com.splendo.kaluga.scientific.unit.Henry
import com.splendo.kaluga.scientific.unit.Hertz
import com.splendo.kaluga.scientific.unit.Minute
import com.splendo.kaluga.scientific.unit.Ohm
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Siemens
import kotlin.test.Test

class FrequencyUnitTest {

    @Test
    fun frequencyFromConductanceAndCapacityTest() {
        assertEqualScientificValue(1(Hertz), 2(Siemens) / 2(Farad))
    }

    @Test
    fun frequencyFromResistanceAndInductanceTest() {
        assertEqualScientificValue(1(Hertz), 2(Ohm) / 2(Henry))
    }

    @Test
    fun frequencyFromInvertedTimeTest() {
        assertEqualScientificValue(1(Hertz), 2 / 2(Second))
        assertEqualScientificValue(1(Hertz), 2.toDecimal() / 2(Second))
        assertEqualScientificValue(1(BeatsPerMinute), 2 / 2(Minute), round = 32)
        assertEqualScientificValue(1(BeatsPerMinute), 2.toDecimal() / 2(Minute), round = 32)
        assertEqualScientificValue(0.5(Hertz), 2(Second).frequency())
        assertEqualScientificValue(0.5(BeatsPerMinute), 2(Minute).frequency(), round = 32)
    }
}
