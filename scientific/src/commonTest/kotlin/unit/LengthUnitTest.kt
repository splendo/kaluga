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
import com.splendo.kaluga.base.utils.toDecimal
import kotlin.test.Test
import com.splendo.kaluga.test.base.IgnoreJs

@IgnoreJs
class LengthUnitTest {

    @Test
    fun meterConversionTest() {
        assertScientificConversion("1.0", Meter, "1e+9", Nanometer)
        assertScientificConversion("1.0", Meter, "1e+6", Micrometer)
        assertScientificConversion("1.0", Meter, "1000.0", Millimeter)
        assertScientificConversion("1.0", Meter, "100.0", Centimeter)
        assertScientificConversion("1.0", Meter, "10.0", Decimeter)
        assertScientificConversion("1.0", Meter, "0.1", Decameter)
        assertScientificConversion("1.0", Meter, "0.01", Hectometer)
        assertScientificConversion("1.0", Meter, "0.001", Kilometer)
        assertScientificConversion("1.0", Meter, "1e-6", Megameter)
        assertScientificConversion("1.0", Meter, "1e-9", Gigameter)
    }

    @Test
    fun feetConversionTest() {
        assertScientificConversion("1.0", Foot, "0.3048", Meter)
        assertScientificConversion("1.0", Foot, "12", Inch)
        assertScientificConversion(Decimal.ONE, Foot, Decimal.ONE / 3.toDecimal(), Yard, round = 32)
        assertScientificConversion(Decimal.ONE, Foot, Decimal.ONE / (5280.toDecimal()), Mile, round = 32)
    }
}
