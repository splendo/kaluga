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
import com.splendo.kaluga.scientific.converter.molarity.div
import com.splendo.kaluga.scientific.converter.molarity.times
import com.splendo.kaluga.scientific.unit.*

val PhysicalQuantity.Molarity.converters get() = listOf<QuantityConverter<PhysicalQuantity.Molarity, *>>(
    QuantityConverterWithOperator("Amount of Substance from Volume", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.Volume) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Molarity && rightUnit is Volume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Density from Molality", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Molality) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricMolarity && rightUnit is MetricMolality -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolarity && rightUnit is ImperialMolality -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolarity && rightUnit is UKImperialMolality -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolarity && rightUnit is USCustomaryMolality -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialMolarity && rightUnit is ImperialMolality -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialMolarity && rightUnit is UKImperialMolality -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryMolarity && rightUnit is ImperialMolality -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryMolarity && rightUnit is USCustomaryMolality -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molarity && rightUnit is Molality -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Density from Molar Mass", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.MolarMass) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricMolarity && rightUnit is MetricMolarMass -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolarity && rightUnit is ImperialMolarMass -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolarity && rightUnit is UKImperialMolarMass -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolarity && rightUnit is USCustomaryMolarMass -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialMolarity && rightUnit is ImperialMolarMass -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialMolarity && rightUnit is UKImperialMolarMass -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryMolarity && rightUnit is ImperialMolarMass -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryMolarity && rightUnit is USCustomaryMolarMass -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molarity && rightUnit is MolarMass -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Molality from Density", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Density) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Molarity && rightUnit is MetricDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molarity && rightUnit is ImperialDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molarity && rightUnit is UKImperialDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molarity && rightUnit is USCustomaryDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molarity && rightUnit is Density -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Molality from Specific Volume", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.SpecificVolume) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Molarity && rightUnit is MetricSpecificVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molarity && rightUnit is ImperialSpecificVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molarity && rightUnit is UKImperialSpecificVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molarity && rightUnit is USCustomarySpecificVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molarity && rightUnit is SpecificVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    }
)
