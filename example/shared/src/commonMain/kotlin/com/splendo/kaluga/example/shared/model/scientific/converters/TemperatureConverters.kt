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
import com.splendo.kaluga.scientific.converter.temperature.div
import com.splendo.kaluga.scientific.converter.temperature.times
import com.splendo.kaluga.scientific.unit.*

val PhysicalQuantity.Temperature.converters get() = listOf<QuantityConverter<PhysicalQuantity.Temperature, *>>(
    QuantityConverterWithOperator("Energy from Heat Capacity", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.HeatCapacity) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndUKImperialTemperature && rightUnit is MetricAndUKImperialHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndUKImperialTemperature && rightUnit is MetricHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndUKImperialTemperature && rightUnit is UKImperialHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryTemperature && rightUnit is USCustomaryHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Temperature && rightUnit is HeatCapacity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Power from Thermal Resistance", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.ThermalResistance) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndUKImperialTemperature && rightUnit is MetricAndUKImperialThermalResistance -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndUKImperialTemperature && rightUnit is MetricThermalResistance -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndUKImperialTemperature && rightUnit is UKImperialThermalResistance -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryTemperature && rightUnit is USCustomaryThermalResistance -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Temperature && rightUnit is ThermalResistance -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Specific Energy from Specific Heat Capacity", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.SpecificHeatCapacity) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndUKImperialTemperature && rightUnit is MetricSpecificHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndUKImperialTemperature && rightUnit is UKImperialSpecificHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryTemperature && rightUnit is USCustomarySpecificHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Temperature && rightUnit is SpecificHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Thermal Resistance from Power", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Power) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndUKImperialTemperature && rightUnit is MetricAndImperialPower -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndUKImperialTemperature && rightUnit is MetricPower -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndUKImperialTemperature && rightUnit is ImperialPower -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryTemperature && rightUnit is ImperialPower -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Temperature && rightUnit is Power -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    }
)
