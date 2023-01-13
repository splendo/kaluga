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
import com.splendo.kaluga.scientific.converter.specificVolume.div
import com.splendo.kaluga.scientific.converter.specificVolume.times
import com.splendo.kaluga.scientific.unit.*

val PhysicalQuantity.SpecificVolume.converters get() = listOf<QuantityConverter<PhysicalQuantity.SpecificVolume, *, *>>(
    QuantityConverter("Area from Linear Mass Density", QuantityConverter.Type.Multiplication, PhysicalQuantity.LinearMassDensity) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricSpecificVolume && rightUnit is MetricLinearMassDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificVolume && rightUnit is ImperialLinearMassDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificVolume && rightUnit is UKImperialLinearMassDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificVolume && rightUnit is USCustomaryLinearMassDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialSpecificVolume && rightUnit is ImperialLinearMassDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialSpecificVolume && rightUnit is UKImperialLinearMassDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomarySpecificVolume && rightUnit is ImperialLinearMassDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomarySpecificVolume && rightUnit is USCustomaryLinearMassDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SpecificVolume && rightUnit is LinearMassDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Length from Area Density", QuantityConverter.Type.Multiplication, PhysicalQuantity.AreaDensity) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricSpecificVolume && rightUnit is MetricAreaDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificVolume && rightUnit is ImperialAreaDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificVolume && rightUnit is UKImperialAreaDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificVolume && rightUnit is USCustomaryAreaDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialSpecificVolume && rightUnit is ImperialAreaDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialSpecificVolume && rightUnit is UKImperialAreaDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomarySpecificVolume && rightUnit is ImperialAreaDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomarySpecificVolume && rightUnit is USCustomaryAreaDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SpecificVolume && rightUnit is AreaDensity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Molality from Molar Volume", QuantityConverter.Type.Division, PhysicalQuantity.MolarVolume) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricSpecificVolume && rightUnit is MolarVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificVolume && rightUnit is MolarVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialSpecificVolume && rightUnit is MolarVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomarySpecificVolume && rightUnit is MolarVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SpecificVolume && rightUnit is MolarVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Molality from Molarity", QuantityConverter.Type.Multiplication, PhysicalQuantity.Molarity) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricSpecificVolume && rightUnit is Molarity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificVolume && rightUnit is Molarity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialSpecificVolume && rightUnit is Molarity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomarySpecificVolume && rightUnit is Molarity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SpecificVolume && rightUnit is Molarity -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Molar Volume from Molar Mass", QuantityConverter.Type.Multiplication, PhysicalQuantity.MolarMass) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricSpecificVolume && rightUnit is MolarMass -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificVolume && rightUnit is MolarMass -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialSpecificVolume && rightUnit is MolarMass -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomarySpecificVolume && rightUnit is MolarMass -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SpecificVolume && rightUnit is MolarMass -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Molar Volume from Molality", QuantityConverter.Type.Division, PhysicalQuantity.Molality) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricSpecificVolume && rightUnit is Molality -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificVolume && rightUnit is Molality -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialSpecificVolume && rightUnit is Molality -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomarySpecificVolume && rightUnit is Molality -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SpecificVolume && rightUnit is Molality -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Volume from Weight", QuantityConverter.Type.Multiplication, PhysicalQuantity.Weight) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricSpecificVolume && rightUnit is MetricWeight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificVolume && rightUnit is ImperialWeight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificVolume && rightUnit is UKImperialWeight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialSpecificVolume && rightUnit is USCustomaryWeight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialSpecificVolume && rightUnit is ImperialWeight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialSpecificVolume && rightUnit is UKImperialWeight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomarySpecificVolume && rightUnit is ImperialWeight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomarySpecificVolume && rightUnit is USCustomaryWeight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SpecificVolume && rightUnit is Weight -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    }
)
