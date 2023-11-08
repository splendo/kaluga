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

package com.splendo.kaluga.scientific

import com.splendo.kaluga.base.utils.round
import com.splendo.kaluga.base.utils.toDouble
import kotlin.test.assertEquals

fun <Quantity : PhysicalQuantity> assertEqualScientificValue(expected: ScientificValue<Quantity, *>, actual: ScientificValue<Quantity, *>, round: Int? = null) {
    assertEquals(expected.unit, actual.unit)
    val actualValue = round?.let { actual.decimalValue.round(it) } ?: actual.decimalValue
    val expectedValue = round?.let { expected.decimalValue.round(it) } ?: expected.decimalValue
    assertEquals(
        expectedValue.toDouble(),
        actualValue.toDouble(),
    )
}
