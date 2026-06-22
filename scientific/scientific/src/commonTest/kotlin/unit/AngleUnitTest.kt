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
import com.splendo.kaluga.base.decimal.times
import com.splendo.kaluga.base.decimal.toDecimal
import kotlin.test.Test

class AngleUnitTest {

    @Test
    fun angleConversionTest() {
        assertScientificConversion("1", Radian, "1000000000.0", Nanoradian)
        assertScientificConversion("1", Radian, "1000000.0", Microradian)
        assertScientificConversion("1", Radian, "1000.0", Milliradian)
        assertScientificConversion("1", Radian, "100.0", Centiradian)
        assertScientificConversion("1", Radian, "10.0", Deciradian)

        val expectedTurn = Decimal.ONE / (Decimal.PI * 2.toDecimal())
        assertScientificConversion(Decimal.ONE, Radian, expectedTurn, Turn, round = 32)
        assertScientificConversion(Decimal.ONE, Radian, Decimal.THOUSAND * Decimal.THOUSAND * expectedTurn, Microturn, round = 32)
        assertScientificConversion(Decimal.ONE, Radian, Decimal.THOUSAND * expectedTurn, Milliturn, round = 32)
        assertScientificConversion(Decimal.ONE, Radian, Decimal.HUNDRED * expectedTurn, Centiturn, round = 32)
        assertScientificConversion(Decimal.ONE, Radian, Decimal.TEN * expectedTurn, Deciturn, round = 32)
        assertScientificConversion(Decimal.ONE, Radian, expectedTurn * 360.toDecimal(), Degree, round = 32)
        assertScientificConversion(Decimal.ONE, Radian, expectedTurn * 400.toDecimal(), Gradian, round = 32)
        assertScientificConversion(Decimal.ONE, Radian, expectedTurn * 21600.toDecimal(), ArcMinute, round = 32)
        assertScientificConversion(Decimal.ONE, Radian, expectedTurn * 1296000.toDecimal(), ArcSecond, round = 32)
    }
}
