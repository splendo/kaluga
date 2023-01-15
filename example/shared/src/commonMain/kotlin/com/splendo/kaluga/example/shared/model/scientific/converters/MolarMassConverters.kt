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
import com.splendo.kaluga.scientific.converter.molarMass.div
import com.splendo.kaluga.scientific.converter.molarMass.molality
import com.splendo.kaluga.scientific.converter.molarMass.times
import com.splendo.kaluga.scientific.unit.*

val PhysicalQuantity.MolarMass.converters get() = listOf<QuantityConverter<PhysicalQuantity.MolarMass, *>>(
    QuantityConverterWithOperator("Density from Molarity", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.Molarity) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricMolarMass && rightUnit is MetricMolarity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolarMass && rightUnit is ImperialMolarity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolarMass && rightUnit is UKImperialMolarity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolarMass && rightUnit is USCustomaryMolarity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialMolarMass && rightUnit is ImperialMolarity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialMolarMass && rightUnit is UKImperialMolarity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryMolarMass && rightUnit is ImperialMolarity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryMolarMass && rightUnit is USCustomaryMolarity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarMass && rightUnit is Molarity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Density from Molar Volume", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.MolarVolume) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricMolarMass && rightUnit is MetricMolarVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolarMass && rightUnit is ImperialMolarVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolarMass && rightUnit is UKImperialMolarVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolarMass && rightUnit is USCustomaryMolarVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialMolarMass && rightUnit is ImperialMolarVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialMolarMass && rightUnit is UKImperialMolarVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryMolarMass && rightUnit is ImperialMolarVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryMolarMass && rightUnit is USCustomaryMolarVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarMass && rightUnit is MolarVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    SingleQuantityConverter("Molality") { value, unit ->
        when (unit) {
            is MetricMolarMass -> DefaultScientificValue(value, unit).molality()
            is ImperialMolarMass -> DefaultScientificValue(value, unit).molality()
            is UKImperialMolarMass -> DefaultScientificValue(value, unit).molality()
            is USCustomaryMolarMass -> DefaultScientificValue(value, unit).molality()
            is MolarMass -> DefaultScientificValue(value, unit).molality()
            else -> throw RuntimeException("Unexpected unit: $unit")
        }
    },
    QuantityConverterWithOperator("Molar Energy from Specific Energy", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.SpecificEnergy) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MolarMass && rightUnit is MetricSpecificEnergy -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarMass && rightUnit is ImperialSpecificEnergy -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarMass && rightUnit is UKImperialSpecificEnergy -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarMass && rightUnit is USCustomarySpecificEnergy -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarMass && rightUnit is SpecificEnergy -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Molar Volume from Density", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Density) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MolarMass && rightUnit is MetricDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarMass && rightUnit is ImperialDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarMass && rightUnit is UKImperialDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarMass && rightUnit is USCustomaryDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarMass && rightUnit is Density -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Molar Volume from Specific Volume", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.SpecificVolume) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MolarMass && rightUnit is MetricSpecificVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarMass && rightUnit is ImperialSpecificVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarMass && rightUnit is UKImperialSpecificVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarMass && rightUnit is USCustomarySpecificVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarMass && rightUnit is SpecificVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Weight from Amount of Substance", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.AmountOfSubstance) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricMolarMass && rightUnit is AmountOfSubstance -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolarMass && rightUnit is AmountOfSubstance -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialMolarMass && rightUnit is AmountOfSubstance -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryMolarMass && rightUnit is AmountOfSubstance -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarMass && rightUnit is AmountOfSubstance -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    }
)
