/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.base.utils.toDouble
import kotlinx.serialization.Serializable

@Serializable
sealed class ScientificUnit {
    abstract val symbol: String

    abstract fun toSIUnit(value: Decimal): Decimal
    abstract fun fromSIUnit(value: Decimal): Decimal
}

fun ScientificUnit.convert(
    value: Double,
    to: ScientificUnit
): Double = convert(value.toDecimal(), to).toDouble()

fun ScientificUnit.convert(
    value: Decimal,
    to: ScientificUnit
): Decimal = to.fromSIUnit(toSIUnit(value))
