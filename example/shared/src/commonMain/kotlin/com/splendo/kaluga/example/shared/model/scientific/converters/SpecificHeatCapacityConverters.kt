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
import com.splendo.kaluga.scientific.converter.specificHeatCapacity.times
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialTemperature
import com.splendo.kaluga.scientific.unit.MetricSpecificHeatCapacity
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.SpecificHeatCapacity
import com.splendo.kaluga.scientific.unit.Temperature
import com.splendo.kaluga.scientific.unit.UKImperialSpecificHeatCapacity
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomarySpecificHeatCapacity
import com.splendo.kaluga.scientific.unit.USCustomaryTemperature
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight

val PhysicalQuantity.SpecificHeatCapacity.converters get() = listOf<QuantityConverter<PhysicalQuantity.SpecificHeatCapacity, *>>(
    QuantityConverterWithOperator(
        "Heat Capacity from Weight",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Weight,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricSpecificHeatCapacity && rightUnit is MetricWeight -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialSpecificHeatCapacity && rightUnit is ImperialWeight -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialSpecificHeatCapacity && rightUnit is UKImperialWeight -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomarySpecificHeatCapacity && rightUnit is ImperialWeight -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomarySpecificHeatCapacity && rightUnit is USCustomaryWeight -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SpecificHeatCapacity && rightUnit is Weight -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Specific Energy from Temperature",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Temperature,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricSpecificHeatCapacity && rightUnit is MetricAndUKImperialTemperature -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialSpecificHeatCapacity && rightUnit is MetricAndUKImperialTemperature -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomarySpecificHeatCapacity && rightUnit is USCustomaryTemperature -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SpecificHeatCapacity && rightUnit is Temperature -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
)
