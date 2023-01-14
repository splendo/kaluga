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
import com.splendo.kaluga.scientific.converter.density.div
import com.splendo.kaluga.scientific.converter.density.times
import com.splendo.kaluga.scientific.unit.*

val PhysicalQuantity.Density.converters get() = listOf<QuantityConverter<PhysicalQuantity.Density, *>>(
    QuantityConverterWithOperator("Area Density from Length", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.Length) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricDensity && rightUnit is MetricLength -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialDensity && rightUnit is ImperialLength -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialDensity && rightUnit is ImperialLength -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryDensity && rightUnit is ImperialLength -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Density && rightUnit is Length -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Linear Mass Density from Area", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.Area) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricDensity && rightUnit is MetricArea -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialDensity && rightUnit is ImperialArea -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialDensity && rightUnit is ImperialArea -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryDensity && rightUnit is ImperialArea -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Density && rightUnit is Area -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Molarity from Molality", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.Molality) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricDensity && rightUnit is MetricMolality -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialDensity && rightUnit is ImperialMolality -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialDensity && rightUnit is ImperialMolality -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryDensity && rightUnit is ImperialMolality -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Density && rightUnit is Molality -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Molarity from Molar Mass", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.MolarMass) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricDensity && rightUnit is MetricMolarMass -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialDensity && rightUnit is ImperialMolarMass -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialDensity && rightUnit is ImperialMolarMass -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryDensity && rightUnit is ImperialMolarMass -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Density && rightUnit is MolarMass -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Molar Mass from Molarity", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Molarity) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricDensity && rightUnit is MetricMolarity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialDensity && rightUnit is ImperialMolarity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialDensity && rightUnit is ImperialMolarity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryDensity && rightUnit is ImperialMolarity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Density && rightUnit is Molarity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Molarity from Molar Volume", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.MolarVolume) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricDensity && rightUnit is MetricMolarVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialDensity && rightUnit is ImperialMolarVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialDensity && rightUnit is ImperialMolarVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryDensity && rightUnit is ImperialMolarVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Density && rightUnit is MolarVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Weight from Volume", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.Volume) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricDensity && rightUnit is MetricVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialDensity && rightUnit is ImperialVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialDensity && rightUnit is UKImperialVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialDensity && rightUnit is USCustomaryVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialDensity && rightUnit is ImperialVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialDensity && rightUnit is UKImperialVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryDensity && rightUnit is ImperialVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryDensity && rightUnit is USCustomaryVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Density && rightUnit is Volume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
)
