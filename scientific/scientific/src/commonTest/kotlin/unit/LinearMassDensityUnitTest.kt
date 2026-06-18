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

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.base.decimal.div
import kotlin.test.Test

class LinearMassDensityUnitTest {

    @Test
    fun linearMassDensityConversionTest() {
        val expected = Kilogram.convert(Decimal.ONE, Pound) / Meter.convert(Decimal.ONE, Foot)
        assertScientificConversion(Decimal.ONE, (Kilogram per Meter), expected, (Pound per Foot), round = 30)
        assertScientificConversion(
            Decimal.ONE,
            (Kilogram per Meter),
            expected,
            (Pound.ukImperial per Foot),
            round = 30,
        )
        assertScientificConversion(
            Decimal.ONE,
            (Kilogram per Meter),
            expected,
            (Pound.usCustomary per Foot),
            round = 30,
        )
    }
}
