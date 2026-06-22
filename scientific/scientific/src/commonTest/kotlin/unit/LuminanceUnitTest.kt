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
import com.splendo.kaluga.base.decimal.pow
import com.splendo.kaluga.base.decimal.times
import com.splendo.kaluga.base.decimal.toDecimal
import kotlin.test.Test

class LuminanceUnitTest {

    @Test
    fun luminanceConversionTest() {
        assertScientificConversion("1", Nit, "1e+9", Nanonit)
        assertScientificConversion("1", Nit, "1e+6", Micronit)
        assertScientificConversion("1", Nit, "1000.0", Millinit)
        assertScientificConversion("1", Nit, "100.0", Centinit)
        assertScientificConversion("1", Nit, "10.0", Decinit)
        assertScientificConversion("1", Nit, "0.1", Decanit)
        assertScientificConversion("1", Nit, "0.01", Hectonit)
        assertScientificConversion("1", Nit, "0.001", Kilonit)
        assertScientificConversion("1", Nit, "1e-6", Meganit)
        assertScientificConversion("1", Nit, "1e-9", Giganit)

        assertScientificConversion(Decimal.ONE, Nit, Decimal.PI, Apostilb, round = 32)
        assertScientificConversion(Decimal.ONE, Nit, Decimal.PI / 10000.toDecimal(), Lambert, round = 32)
        assertScientificConversion(Decimal.ONE, Nit, Decimal.THOUSAND * Decimal.PI, Skot, round = 29)
        assertScientificConversion(Decimal.ONE, Nit, 10000000.toDecimal() * Decimal.PI, Bril, round = 32)
        assertScientificConversion(Decimal.ONE, Nit, Decimal.PI / Meter.convert(Decimal.ONE, Foot).pow(2), FootLambert, round = 32)
    }
}
