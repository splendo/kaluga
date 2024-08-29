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

sealed class QuantityConverter<From : PhysicalQuantity, Result : PhysicalQuantity> {
    abstract val name: String

    data class Single<From : PhysicalQuantity, Result : PhysicalQuantity>(
        val fromQuantity: KClass<From>,
        val resultQuantity: KClass<Result>,
        override val name: String,
        val converter: (Decimal, ScientificUnit<From>) -> ScientificValue<Result, *>,
    ) : QuantityConverter<From, Result>() {

        @Suppress("UNCHECKED_CAST")
        fun convert(value: Decimal, unit: ScientificUnit<*>): ScientificValue<*, *>? = if (fromQuantity.isInstance(unit.quantity)) {
            converter(value, unit as ScientificUnit<From>)
        } else {
            null
        }
    }

    data class WithOperator<Left : PhysicalQuantity, Right : PhysicalQuantity, Result : PhysicalQuantity>(
        val leftQuantity: KClass<Left>,
        val rightQuantity: Right,
        val resultQuantity: KClass<Result>,
        override val name: String,
        val type: Type,
        val converter: (left: Pair<Decimal, ScientificUnit<Left>>, right: Pair<Decimal, ScientificUnit<Right>>) -> ScientificValue<Result, *>,
    ) : QuantityConverter<Left, Result>() {
        sealed class Type {

            abstract val operatorSymbol: String

            data object Multiplication : Type() {
                override val operatorSymbol: String = "*"
            }

            data object Division : Type() {
                override val operatorSymbol: String = "/"
            }

            data class Custom(override val operatorSymbol: String) : Type()
        }

        @Suppress("UNCHECKED_CAST")
        fun convert(left: Decimal, leftUnit: ScientificUnit<*>, right: Decimal, rightUnit: ScientificUnit<*>): ScientificValue<*, *>? =
            if (leftQuantity.isInstance(leftUnit.quantity) &&
                rightQuantity::class.isInstance(
                    rightUnit.quantity,
                )
            ) {
                converter(
                    left to leftUnit as ScientificUnit<Left>,
                    right to rightUnit as ScientificUnit<Right>,
                )
            } else {
                null
            }
    }
}

inline fun <reified From : PhysicalQuantity, reified Result : PhysicalQuantity> SingleQuantityConverter(
    name: String,
    noinline converter: (value: Decimal, unit: ScientificUnit<From>) -> ScientificValue<Result, *>,
) = QuantityConverter.Single(From::class, Result::class, name, converter)

inline fun <reified Left : PhysicalQuantity, reified Right : PhysicalQuantity, reified Result : PhysicalQuantity> QuantityConverterWithOperator(
    name: String,
    type: QuantityConverter.WithOperator.Type,
    right: Right,
    noinline converter: (left: Pair<Decimal, ScientificUnit<Left>>, right: Pair<Decimal, ScientificUnit<Right>>) -> ScientificValue<Result, *>,
) = QuantityConverter.WithOperator(Left::class, right, Result::class, name, type, converter)
