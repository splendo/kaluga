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

import com.splendo.kaluga.scientific.converter.amountOfSubstance.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Katal
import com.splendo.kaluga.scientific.unit.Mole
import com.splendo.kaluga.scientific.unit.Second
import kotlin.test.Test
import kotlin.test.assertEquals

class CatalysticActivityUnitTest {

    @Test
    fun catalysticActivityFromAmountOfSubstanceAndTimeTest() {
        assertEquals(1(Katal), 2(Mole) / 2(Second))
    }
}
