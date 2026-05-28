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
class ActionUnitTest {

    @Test
    fun actionConversionTest() {
        assertScientificConversion(Decimal.ONE, Joule x Second, Decimal.ONE / 12960000.toDecimal(), WattHour x Hour, round = 32)
    }
}
