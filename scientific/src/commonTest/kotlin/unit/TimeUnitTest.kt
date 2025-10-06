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

class TimeUnitTest {

    @Test
    fun secondConversionTest() {
        assertScientificConversion("1.0", Second, "1e+9", Nanosecond)
        assertScientificConversion("1.0", Second, "1e+6", Microsecond)
        assertScientificConversion("1.0", Second, "1000.0", Millisecond)
        assertScientificConversion("1.0", Second, "100.0", Centisecond)
        assertScientificConversion("1.0", Second, "10.0", Decisecond)
        assertScientificConversion(Decimal.ONE, Second, Decimal.ONE / 60.toDecimal(), Minute, round = 32)
        assertScientificConversion(Decimal.ONE, Second, Decimal.ONE / 3600.toDecimal(), Hour, round = 32)
    }
}
