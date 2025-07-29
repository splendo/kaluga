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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.pow
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import kotlin.test.Test

class SolidAngleUnitTest {

    @Test
    fun solidAngleConversionTest() {
        assertScientificConversion("1", Steradian, "1e+9", Nanosteradian)
        assertScientificConversion("1", Steradian, "1e+6", Microsteradian)
        assertScientificConversion("1", Steradian, "1000.0", Millisteradian)
        assertScientificConversion("1", Steradian, "100.0", Centisteradian)
        assertScientificConversion("1", Steradian, "10.0", Decisteradian)

        assertScientificConversion(Decimal.ONE, Steradian, Decimal.ONE / (Decimal.PI * 4.toDecimal()), Spat, round = 32)
        assertScientificConversion(Decimal.ONE, Steradian, Radian.convert(Decimal.ONE, Degree).pow(2), SquareDegree, round = 29)
    }
}
