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
import com.splendo.kaluga.scientific.converter.areaDensity.div
import com.splendo.kaluga.scientific.converter.areaDensity.times
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.AreaDensity
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialAreaDensity
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.ImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricAreaDensity
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.MetricSpecificVolume
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialAreaDensity
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.USCustomaryAreaDensity
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.USCustomarySpecificVolume

val PhysicalQuantity.AreaDensity.converters get() = listOf<QuantityConverter<PhysicalQuantity.AreaDensity, *>>(
    QuantityConverterWithOperator(
        "Density from Length",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Length,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAreaDensity && rightUnit is MetricLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialAreaDensity && rightUnit is ImperialLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialAreaDensity && rightUnit is ImperialLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryAreaDensity && rightUnit is ImperialLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is AreaDensity && rightUnit is Length -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Length from Specific Volume",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.SpecificVolume,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAreaDensity && rightUnit is MetricSpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialAreaDensity && rightUnit is ImperialSpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialAreaDensity && rightUnit is UKImperialSpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialAreaDensity && rightUnit is USCustomarySpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialAreaDensity && rightUnit is ImperialSpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialAreaDensity && rightUnit is UKImperialSpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryAreaDensity && rightUnit is ImperialSpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryAreaDensity && rightUnit is USCustomarySpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is AreaDensity && rightUnit is SpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Length from Density",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Density,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAreaDensity && rightUnit is MetricDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialAreaDensity && rightUnit is ImperialDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialAreaDensity && rightUnit is UKImperialDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialAreaDensity && rightUnit is USCustomaryDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialAreaDensity && rightUnit is ImperialDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialAreaDensity && rightUnit is UKImperialDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryAreaDensity && rightUnit is ImperialDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryAreaDensity && rightUnit is USCustomaryDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is AreaDensity && rightUnit is Density -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Linear Mass Density from Length",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Length,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAreaDensity && rightUnit is MetricLength -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialAreaDensity && rightUnit is ImperialLength -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialAreaDensity && rightUnit is ImperialLength -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryAreaDensity && rightUnit is ImperialLength -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is AreaDensity && rightUnit is Length -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Weight from Area",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Area,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAreaDensity && rightUnit is MetricArea -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialAreaDensity && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialAreaDensity && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryAreaDensity && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is AreaDensity && rightUnit is Area -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
)
