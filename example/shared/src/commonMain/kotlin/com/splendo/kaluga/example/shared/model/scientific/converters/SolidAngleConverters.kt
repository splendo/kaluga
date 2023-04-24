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
import com.splendo.kaluga.scientific.converter.solidAngle.times
import com.splendo.kaluga.scientific.unit.*

val PhysicalQuantity.SolidAngle.converters get() = listOf<QuantityConverter<PhysicalQuantity.SolidAngle, *>>(
    QuantityConverterWithOperator(
        "Illuminance from Luminance",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Luminance,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is SolidAngle && rightUnit is Luminance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SolidAngle && rightUnit is Lambert -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SolidAngle && rightUnit is MetricLuminance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SolidAngle && rightUnit is ImperialLuminance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SolidAngle && rightUnit is Luminance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Luminous Flux from Luminous Intensity",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.LuminousIntensity,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is SolidAngle && rightUnit is LuminousIntensity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
)
