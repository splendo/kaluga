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
import com.splendo.kaluga.scientific.converter.molality.div
import com.splendo.kaluga.scientific.converter.molality.molarMass
import com.splendo.kaluga.scientific.converter.molality.times
import com.splendo.kaluga.scientific.unit.*

val PhysicalQuantity.Molality.converters get() = listOf<QuantityConverter<PhysicalQuantity.Molality, *>>(
    QuantityConverterWithOperator("Amount of Substance from Weight", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.Weight) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Molality && rightUnit is Weight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    SingleQuantityConverter("Molar Mass") { value, unit ->
        when (unit) {
            is MetricMolality -> DefaultScientificValue(value, unit).molarMass()
            is ImperialMolality -> DefaultScientificValue(value, unit).molarMass()
            is UKImperialMolality -> DefaultScientificValue(value, unit).molarMass()
            is USCustomaryMolality -> DefaultScientificValue(value, unit).molarMass()
            is Molality -> DefaultScientificValue(value, unit).molarMass()
            else -> throw RuntimeException("Unexpected unit: $unit")
        }
    },
    QuantityConverterWithOperator("Molarity from Density", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.Density) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Molality && rightUnit is MetricDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molality && rightUnit is ImperialDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molality && rightUnit is UKImperialDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molality && rightUnit is USCustomaryDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molality && rightUnit is Density -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Molarity from Specific Volume", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.SpecificVolume) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Molality && rightUnit is MetricSpecificVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molality && rightUnit is ImperialSpecificVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molality && rightUnit is UKImperialSpecificVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molality && rightUnit is USCustomarySpecificVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molality && rightUnit is SpecificVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Specific Energy from Molar Energy", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.MolarEnergy) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricMolality && rightUnit is MetricAndImperialMolarEnergy -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolality && rightUnit is MetricAndImperialMolarEnergy -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialMolality && rightUnit is MetricAndImperialMolarEnergy -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryMolality && rightUnit is MetricAndImperialMolarEnergy -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricMolality && rightUnit is MetricMolarEnergy -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolality && rightUnit is ImperialMolarEnergy -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialMolality && rightUnit is ImperialMolarEnergy -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryMolality && rightUnit is ImperialMolarEnergy -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molality && rightUnit is MolarEnergy -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Specific Volume from Molar Volume", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.MolarVolume) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricMolality && rightUnit is MetricMolarVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolality && rightUnit is ImperialMolarVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolality && rightUnit is UKImperialMolarVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolality && rightUnit is USCustomaryMolarVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialMolality && rightUnit is ImperialMolarVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialMolality && rightUnit is UKImperialMolarVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryMolality && rightUnit is ImperialMolarVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryMolality && rightUnit is USCustomaryMolarVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molality && rightUnit is MolarVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Specific Volume from Molarity", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Molarity) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricMolality && rightUnit is MetricMolarity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolality && rightUnit is ImperialMolarity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolality && rightUnit is UKImperialMolarity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolality && rightUnit is USCustomaryMolarity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialMolality && rightUnit is ImperialMolarity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialMolality && rightUnit is UKImperialMolarity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryMolality && rightUnit is ImperialMolarity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryMolality && rightUnit is USCustomaryMolarity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Molality && rightUnit is Molarity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    }
)
