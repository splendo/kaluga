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
import com.splendo.kaluga.scientific.converter.specificEnergy.div
import com.splendo.kaluga.scientific.converter.specificEnergy.times
import com.splendo.kaluga.scientific.unit.*

val PhysicalQuantity.SpecificEnergy.converters get() = listOf<QuantityConverter<PhysicalQuantity.SpecificEnergy, *>>(
    QuantityConverterWithOperator("Energy from Weight", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.Weight) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricSpecificEnergy && rightUnit is MetricWeight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificEnergy && rightUnit is ImperialWeight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificEnergy && rightUnit is UKImperialWeight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificEnergy && rightUnit is USCustomaryWeight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialSpecificEnergy && rightUnit is ImperialWeight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialSpecificEnergy && rightUnit is UKImperialWeight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomarySpecificEnergy && rightUnit is ImperialWeight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomarySpecificEnergy && rightUnit is USCustomaryWeight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SpecificEnergy && rightUnit is Weight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Molality from Molar Energy", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.MolarEnergy) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricSpecificEnergy && rightUnit is MolarEnergy -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificEnergy && rightUnit is MolarEnergy -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialSpecificEnergy && rightUnit is MolarEnergy -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomarySpecificEnergy && rightUnit is MolarEnergy -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SpecificEnergy && rightUnit is MolarEnergy -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Molar Energy from Molar Mass", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.MolarMass) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricSpecificEnergy && rightUnit is MolarMass -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificEnergy && rightUnit is MolarMass -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialSpecificEnergy && rightUnit is MolarMass -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomarySpecificEnergy && rightUnit is MolarMass -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SpecificEnergy && rightUnit is MolarMass -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Molar Energy from Molality", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Molality) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricSpecificEnergy && rightUnit is Molality -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificEnergy && rightUnit is Molality -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialSpecificEnergy && rightUnit is Molality -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomarySpecificEnergy && rightUnit is Molality -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SpecificEnergy && rightUnit is Molality -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Specific Heat Capacity from Temperature", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Temperature) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricSpecificEnergy && rightUnit is MetricAndUKImperialTemperature -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificEnergy && rightUnit is MetricAndUKImperialTemperature -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificEnergy && rightUnit is USCustomaryTemperature -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialSpecificEnergy && rightUnit is MetricAndUKImperialTemperature -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomarySpecificEnergy && rightUnit is USCustomaryTemperature -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SpecificEnergy && rightUnit is Temperature -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Temperature from Specific Heat Capacity", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.SpecificHeatCapacity) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricSpecificEnergy && rightUnit is MetricSpecificHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificEnergy && rightUnit is UKImperialSpecificHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificEnergy && rightUnit is USCustomarySpecificHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialSpecificEnergy && rightUnit is UKImperialSpecificHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomarySpecificEnergy && rightUnit is USCustomarySpecificHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SpecificEnergy && rightUnit is SpecificHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    }
)
