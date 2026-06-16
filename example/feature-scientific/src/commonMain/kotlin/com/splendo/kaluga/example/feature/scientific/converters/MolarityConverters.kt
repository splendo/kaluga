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

package com.splendo.kaluga.example.feature.scientific.converters

import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.converter.molarity.div
import com.splendo.kaluga.scientific.converter.molarity.molarVolume
import com.splendo.kaluga.scientific.converter.molarity.times
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialMolality
import com.splendo.kaluga.scientific.unit.ImperialMolarMass
import com.splendo.kaluga.scientific.unit.ImperialMolarity
import com.splendo.kaluga.scientific.unit.ImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricMolality
import com.splendo.kaluga.scientific.unit.MetricMolarMass
import com.splendo.kaluga.scientific.unit.MetricMolarity
import com.splendo.kaluga.scientific.unit.MetricSpecificVolume
import com.splendo.kaluga.scientific.unit.Molality
import com.splendo.kaluga.scientific.unit.MolarMass
import com.splendo.kaluga.scientific.unit.Molarity
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.UKImperialMolality
import com.splendo.kaluga.scientific.unit.UKImperialMolarMass
import com.splendo.kaluga.scientific.unit.UKImperialMolarity
import com.splendo.kaluga.scientific.unit.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.USCustomaryMolality
import com.splendo.kaluga.scientific.unit.USCustomaryMolarMass
import com.splendo.kaluga.scientific.unit.USCustomaryMolarity
import com.splendo.kaluga.scientific.unit.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.unit.Volume

val PhysicalQuantity.Molarity.converters get() = listOf<QuantityConverter<PhysicalQuantity.Molarity, *>>(
    QuantityConverterWithOperator(
        "Amount of Substance from Volume",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Volume,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Molarity && rightUnit is Volume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Density from Molality",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Molality,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricMolarity && rightUnit is MetricMolality -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialMolarity && rightUnit is ImperialMolality -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialMolarity && rightUnit is UKImperialMolality -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialMolarity && rightUnit is USCustomaryMolality -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialMolarity && rightUnit is ImperialMolality -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialMolarity && rightUnit is UKImperialMolality -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryMolarity && rightUnit is ImperialMolality -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryMolarity && rightUnit is USCustomaryMolality -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Molarity && rightUnit is Molality -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Density from Molar Mass",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.MolarMass,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricMolarity && rightUnit is MetricMolarMass -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialMolarity && rightUnit is ImperialMolarMass -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialMolarity && rightUnit is UKImperialMolarMass -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialMolarity && rightUnit is USCustomaryMolarMass -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialMolarity && rightUnit is ImperialMolarMass -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialMolarity && rightUnit is UKImperialMolarMass -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryMolarity && rightUnit is ImperialMolarMass -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryMolarity && rightUnit is USCustomaryMolarMass -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Molarity && rightUnit is MolarMass -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Molality from Density",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Density,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Molarity && rightUnit is MetricDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Molarity && rightUnit is ImperialDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Molarity && rightUnit is UKImperialDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Molarity && rightUnit is USCustomaryDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Molarity && rightUnit is Density -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Molality from Specific Volume",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.SpecificVolume,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Molarity && rightUnit is MetricSpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Molarity && rightUnit is ImperialSpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Molarity && rightUnit is UKImperialSpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Molarity && rightUnit is USCustomarySpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Molarity && rightUnit is SpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    SingleQuantityConverter("Molar Volume") { value, unit ->
        when (unit) {
            is MetricMolarity -> DefaultScientificValue(value, unit).molarVolume()
            is ImperialMolarity -> DefaultScientificValue(value, unit).molarVolume()
            is UKImperialMolarity -> DefaultScientificValue(value, unit).molarVolume()
            is USCustomaryMolarity -> DefaultScientificValue(value, unit).molarVolume()
            is Molarity -> DefaultScientificValue(value, unit).molarVolume()
            else -> throw RuntimeException("Unexpected unit: $unit")
        }
    },
)
