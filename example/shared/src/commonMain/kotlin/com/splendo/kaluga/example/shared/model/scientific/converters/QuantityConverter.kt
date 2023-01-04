/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.shared.model.scientific.converters

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.unit.ScientificUnit
import kotlin.reflect.KClass

data class QuantityConverter<Left: PhysicalQuantity, Right: PhysicalQuantity, Result: PhysicalQuantity>(
    val leftQuantity: KClass<Left>,
    val rightQuantity: KClass<Right>,
    val resultQuantity: KClass<Result>,
    val name: String,
    val type: Type,
    val rightUnits: Set<ScientificUnit<Right>>,
    val converter: (left: Pair<Decimal, ScientificUnit<Left>>, right: Pair<Decimal, ScientificUnit<Right>>) -> ScientificValue<Result, *>
) {
    sealed class Type {

        abstract val operatorSymbol: String

        object Multiplication : Type() {
            override val operatorSymbol: String = "*"
        }
        object Division : Type() {
            override val operatorSymbol: String = "/"
        }
        data class Custom(override val operatorSymbol: String) : Type()
    }

    fun convert(left: Decimal, leftUnit: ScientificUnit<*>, right: Decimal, rightUnit: ScientificUnit<*>): ScientificValue<*, *>? {
        return if (leftQuantity.isInstance(leftUnit.quantity) && rightQuantity.isInstance(rightUnit.quantity)) {
            converter(left to leftUnit as ScientificUnit<Left>, right to rightUnit as ScientificUnit<Right>)
        } else {
            null
        }
    }
}

inline fun <reified Left : PhysicalQuantity, reified Right : PhysicalQuantity, reified Result : PhysicalQuantity> QuantityConverter(
    name: String,
    type: QuantityConverter.Type,
    rightUnits: Set<ScientificUnit<Right>>,
    noinline converter: (left: Pair<Decimal, ScientificUnit<Left>>, right: Pair<Decimal, ScientificUnit<Right>>) -> ScientificValue<Result, *>
) = QuantityConverter(Left::class, Right::class, Result::class, name, type, rightUnits, converter)
