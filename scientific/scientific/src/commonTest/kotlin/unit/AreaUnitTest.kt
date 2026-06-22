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
import com.splendo.kaluga.base.decimal.pow
import com.splendo.kaluga.base.decimal.times
import com.splendo.kaluga.base.decimal.toDecimal
import kotlin.test.Test

class AreaUnitTest {

    @Test
    fun areaConversionTest() {
        assertScientificConversion("1", SquareMeter, "1e+18", SquareNanometer)
        assertScientificConversion("1", SquareMeter, "1e+12", SquareMicrometer)
        assertScientificConversion("1", SquareMeter, "1000000.0", SquareMillimeter)
        assertScientificConversion("1", SquareMeter, "10000.0", SquareCentimeter)
        assertScientificConversion("1", SquareMeter, "100.0", SquareDecimeter)
        assertScientificConversion("1", SquareMeter, "0.01", SquareDecameter)
        assertScientificConversion("1", SquareMeter, "0.0001", SquareHectometer)
        assertScientificConversion("1", SquareMeter, "1e-6", SquareKilometer)
        assertScientificConversion("1", SquareMeter, "0.0001", Hectare)

        assertScientificConversion(Decimal.ONE, SquareMeter, Meter.convert(Decimal.ONE, Inch).pow(2), SquareInch, round = 32)
        assertScientificConversion(Decimal.ONE, SquareMeter, Meter.convert(Decimal.ONE, Foot).pow(2), SquareFoot, round = 32)
        assertScientificConversion(Decimal.ONE, SquareMeter, Meter.convert(Decimal.ONE, Yard).pow(2), SquareYard, round = 32)
        assertScientificConversion(Decimal.ONE, SquareMeter, Meter.convert(Decimal.ONE, Mile).pow(2), SquareMile, round = 32)
        assertScientificConversion(Decimal.ONE, SquareMeter, Meter.convert(Decimal.ONE, Mile).pow(2) * 640.toDecimal(), Acre, round = 32)
    }
}
