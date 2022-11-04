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

import com.splendo.kaluga.scientific.converter.amountOfSubstance.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class CatalysticActivityUnitTest {

    @Test
    fun catalysticActivityUnitTestConversionTest() {
        assertScientificConversion(1, Katal, 1e+9, Nanokatal)
        assertScientificConversion(1, Katal, 1e+6, Microkatal)
        assertScientificConversion(1, Katal, 1000.0, Millikatal)
        assertScientificConversion(1, Katal, 100.0, Centikatal)
        assertScientificConversion(1, Katal, 10.0, Decikatal)
        assertScientificConversion(1, Katal, 0.1, Decakatal)
        assertScientificConversion(1, Katal, 0.01, Hectokatal)
        assertScientificConversion(1, Katal, 0.001, Kilokatal)
        assertScientificConversion(1, Katal, 0.000001, Megakatal)
        assertScientificConversion(1, Katal, 0.000000001, Gigakatal)
    }

    @Test
    fun catalysticActivityFromAmountOfSubstanceAndTimeTest() {
        assertEquals(1(Katal), 2(Mole) / 2(Second))
    }
}
