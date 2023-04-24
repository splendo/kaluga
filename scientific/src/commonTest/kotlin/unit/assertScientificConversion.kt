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

import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlin.test.assertEquals

fun <Quantity : PhysicalQuantity> assertScientificConversion(
    left: Number,
    leftUnit: ScientificUnit<Quantity>,
    expectedRight: Number,
    rightUnit: ScientificUnit<Quantity>,
    bidirectional: Boolean = true,
) {
    assertEquals(expectedRight.toDouble(), leftUnit.convert(left, rightUnit))
    if (bidirectional) {
        assertEquals(left.toDouble(), rightUnit.convert(expectedRight, leftUnit))
    }
}

fun <Quantity : PhysicalQuantity> assertScientificConversion(
    left: Number,
    leftUnit: ScientificUnit<Quantity>,
    expectedRight: Number,
    rightUnit: ScientificUnit<Quantity>,
    round: Int,
    bidirectional: Boolean = true,
) {
    assertEquals(expectedRight.toDouble(), leftUnit.convert(left, rightUnit, round))
    if (bidirectional) {
        assertEquals(
            left.toDouble(),
            rightUnit.convert(leftUnit.convert(left, rightUnit), leftUnit, round),
        )
    }
}
