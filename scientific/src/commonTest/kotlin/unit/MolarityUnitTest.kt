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

import com.splendo.kaluga.scientific.converter.amountOfSubstance.div
import com.splendo.kaluga.scientific.invoke
import kotlin.test.Test
import kotlin.test.assertEquals

class MolarityUnitTest {

    @Test
    fun molarityConversionTest() {
        assertEquals(1.63871e-5, (Mole per CubicMeter).convert(1, Mole per CubicInch, 10))
    }

    @Test
    fun molarityFromAmountOfSubstanceDivMetricVolume(){
        assertEquals(1(Mole per CubicMeter),2(Mole) / 2(CubicMeter))
    }

    @Test
    fun molarityFromAmountOfSubstanceDivImperialVolume(){
        assertEquals(1(Mole per CubicInch),2(Mole) / 2(CubicInch))
    }
}