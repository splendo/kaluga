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
    round: Int? = null,
    bidirectional: Boolean = true,
) = assertScientificConversion(left.toDecimal(), leftUnit, expectedRight.toDecimal(), rightUnit, round, bidirectional)

fun <Quantity : PhysicalQuantity> assertScientificConversion(
    left: Decimal,
    leftUnit: ScientificUnit<Quantity>,
    expectedRight: Decimal,
    rightUnit: ScientificUnit<Quantity>,
    round: Int? = null,
    bidirectional: Boolean = true,
) {
    val roundedExpectedRight = round?.let { expectedRight.round(it) } ?: expectedRight
    val actualRight = leftUnit.convert(left, rightUnit)
    val roundedActualRight = round?.let { actualRight.round(it) } ?: actualRight
    assertEquals(roundedExpectedRight, roundedActualRight)
    if (bidirectional) {
        val roundedLeft = round?.let { left.round(it) } ?: left
        val actualLeftReverse = rightUnit.convert(actualRight, leftUnit)
        val roundedActualLeftReverse = round?.let { actualLeftReverse.round(it) } ?: actualLeftReverse
        assertEquals(roundedLeft, roundedActualLeftReverse)
    }
}
