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
import com.splendo.kaluga.base.utils.round
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlin.test.assertEquals

fun <Quantity : PhysicalQuantity> assertScientificConversion(
    left: String,
    leftUnit: ScientificUnit<Quantity>,
    expectedRight: String,
    rightUnit: ScientificUnit<Quantity>,
    bidirectional: Boolean = true,
) = assertScientificConversion(left.toDecimal(), leftUnit, expectedRight.toDecimal(), rightUnit, bidirectional)

fun <Quantity : PhysicalQuantity> assertScientificConversion(
    left: Decimal,
    leftUnit: ScientificUnit<Quantity>,
    expectedRight: Decimal,
    rightUnit: ScientificUnit<Quantity>,
    bidirectional: Boolean = true,
) {
    assertEquals(expectedRight.round(20), leftUnit.convert(left, rightUnit).round(20))
    if (bidirectional) {
        assertEquals(left.round(20), rightUnit.convert(expectedRight, leftUnit).round(20))
    }
}

fun <Quantity : PhysicalQuantity> assertScientificConversion(
    left: String,
    leftUnit: ScientificUnit<Quantity>,
    expectedRight: String,
    rightUnit: ScientificUnit<Quantity>,
    round: Int,
    bidirectional: Boolean = true,
) = assertScientificConversion(left.toDecimal(), leftUnit, expectedRight.toDecimal(), rightUnit, round, bidirectional)

fun <Quantity : PhysicalQuantity> assertScientificConversion(
    left: Decimal,
    leftUnit: ScientificUnit<Quantity>,
    expectedRight: Decimal,
    rightUnit: ScientificUnit<Quantity>,
    round: Int,
    bidirectional: Boolean = true,
) {
    assertEquals(expectedRight.round(round), leftUnit.convert(left, rightUnit, round))
    if (bidirectional) {
        assertEquals(
            left.round(round),
            rightUnit.convert(leftUnit.convert(left, rightUnit), leftUnit, round),
        )
    }
}
