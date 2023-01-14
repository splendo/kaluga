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

import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.converter.surfaceTension.times
import com.splendo.kaluga.scientific.unit.*

val PhysicalQuantity.SurfaceTension.converters get() = listOf<QuantityConverter<PhysicalQuantity.SurfaceTension, *>>(
    QuantityConverterWithOperator("Energy from Area", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.Area) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricSurfaceTension && rightUnit is SquareCentimeter -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricSurfaceTension && rightUnit is MetricArea -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSurfaceTension && rightUnit is SquareInch -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialSurfaceTension && rightUnit is SquareInch -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomarySurfaceTension && rightUnit is SquareInch -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSurfaceTension && rightUnit is ImperialArea -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialSurfaceTension && rightUnit is ImperialArea -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomarySurfaceTension && rightUnit is ImperialArea -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SurfaceTension && rightUnit is Area -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Force from Length", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.Length) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricSurfaceTension && rightUnit is MetricLength -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSurfaceTension && rightUnit is ImperialLength -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialSurfaceTension && rightUnit is ImperialLength -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomarySurfaceTension && rightUnit is ImperialLength -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SurfaceTension && rightUnit is Length -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    }
)
