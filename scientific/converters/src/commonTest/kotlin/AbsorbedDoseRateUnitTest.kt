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

import com.splendo.kaluga.scientific.converter.absorbedDoseRate.times
import com.splendo.kaluga.scientific.converter.ionizingRadiationAbsorbedDose.div
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Gray
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.per
import kotlin.test.Test

class AbsorbedDoseRateUnitTest {

    @Test
    fun absorbedDoseRateFromAbsorbedDoseAndTimeTest() {
        assertEqualScientificValue(1(Gray per Second), 2(Gray) / 2(Second))
    }

    @Test
    fun absorbedDoseFromAbsorbedDoseRateAndTimeTest() {
        assertEqualScientificValue(4(Gray), 2(Gray per Second) * 2(Second))
        assertEqualScientificValue(4(Gray), 2(Second) * 2(Gray per Second))
    }

    @Test
    fun timeFromAbsorbedDoseAndAbsorbedDoseRateTest() {
        assertEqualScientificValue(2(Second), 4(Gray) / 2(Gray per Second))
    }
}
