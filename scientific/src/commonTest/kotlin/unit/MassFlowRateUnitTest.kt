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

import com.splendo.kaluga.scientific.converter.weight.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class MassFlowRateUnitTest {

    @Test
    fun massFlowRateConversionTest() {
        assertEquals(132.28, (Gram per Millisecond).convert(1.0, Pound per Minute, 2))
        assertEquals(1.12, (ImperialTon per Second).convert(1.0, UsTon per Second))
    }

    @Test
    fun massFlowRateFromWeightAndAreaTest() {
        assertEquals(1(Kilogram per Second), 2(Kilogram) / 2(Second))
        assertEquals(1(Pound per Second), 2(Pound) / 2(Second))
    }
}
