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

        assertScientificConversion("1", SquareMeter, "1550.0", SquareInch, 0)
        assertScientificConversion("1", SquareMeter, "10.7639", SquareFoot, 4)
        assertScientificConversion("1", SquareMeter, "1.19599", SquareYard, 5)
        assertScientificConversion("1", SquareMeter, "3.86102e-7", SquareMile, 12)
        assertScientificConversion("1", SquareMeter, "0.000247105", Acre, 9)
    }
}
