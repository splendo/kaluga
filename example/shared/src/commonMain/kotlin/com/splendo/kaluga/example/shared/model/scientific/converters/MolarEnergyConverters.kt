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
import com.splendo.kaluga.scientific.converter.molarEnergy.div
import com.splendo.kaluga.scientific.converter.molarEnergy.times
import com.splendo.kaluga.scientific.unit.*

val PhysicalQuantity.MolarEnergy.converters get() = listOf<QuantityConverter<PhysicalQuantity.MolarEnergy, *, *>>(
    QuantityConverter("Energy from Amount of Substance", QuantityConverter.Type.Multiplication, PhysicalQuantity.AmountOfSubstance) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndImperialMolarEnergy && rightUnit is AmountOfSubstance -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricMolarEnergy && rightUnit is AmountOfSubstance -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolarEnergy && rightUnit is AmountOfSubstance -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarEnergy && rightUnit is AmountOfSubstance -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Molar Mass from Specific Energy", QuantityConverter.Type.Division, PhysicalQuantity.SpecificEnergy) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MolarEnergy && rightUnit is MetricSpecificEnergy -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarEnergy && rightUnit is ImperialSpecificEnergy -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarEnergy && rightUnit is UKImperialSpecificEnergy -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarEnergy && rightUnit is USCustomarySpecificEnergy -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarEnergy && rightUnit is SpecificEnergy -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Specific Energy from Molar Mass", QuantityConverter.Type.Division, PhysicalQuantity.MolarMass) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndImperialMolarEnergy && rightUnit is MetricMolarMass -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialMolarEnergy && rightUnit is ImperialMolarMass -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialMolarEnergy && rightUnit is UKImperialMolarMass -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialMolarEnergy && rightUnit is USCustomaryMolarMass -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricMolarEnergy && rightUnit is MetricMolarMass -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolarEnergy && rightUnit is ImperialMolarMass -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolarEnergy && rightUnit is UKImperialMolarMass -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolarEnergy && rightUnit is USCustomaryMolarMass -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarEnergy && rightUnit is MolarMass -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Specific Energy from Molality", QuantityConverter.Type.Multiplication, PhysicalQuantity.Molality) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndImperialMolarEnergy && rightUnit is MetricMolality -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialMolarEnergy && rightUnit is ImperialMolality -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialMolarEnergy && rightUnit is UKImperialMolality -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialMolarEnergy && rightUnit is USCustomaryMolality -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricMolarEnergy && rightUnit is MetricMolality -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolarEnergy && rightUnit is ImperialMolality -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolarEnergy && rightUnit is UKImperialMolality -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialMolarEnergy && rightUnit is USCustomaryMolality -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MolarEnergy && rightUnit is Molality -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    }
)
