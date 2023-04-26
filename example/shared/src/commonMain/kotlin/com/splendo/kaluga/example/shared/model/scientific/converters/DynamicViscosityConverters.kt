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
import com.splendo.kaluga.scientific.converter.dynamicViscosity.div
import com.splendo.kaluga.scientific.converter.dynamicViscosity.times
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.DynamicViscosity
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialDynamicViscosity
import com.splendo.kaluga.scientific.unit.ImperialKinematicViscosity
import com.splendo.kaluga.scientific.unit.KinematicViscosity
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricDynamicViscosity
import com.splendo.kaluga.scientific.unit.MetricKinematicViscosity
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.UKImperialDynamicViscosity
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.USCustomaryDynamicViscosity

val PhysicalQuantity.DynamicViscosity.converters get() = listOf<QuantityConverter<PhysicalQuantity.DynamicViscosity, *>>(
    QuantityConverterWithOperator(
        "Density from Kinematic Viscosity",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.KinematicViscosity,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricDynamicViscosity && rightUnit is MetricKinematicViscosity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialDynamicViscosity && rightUnit is ImperialKinematicViscosity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialDynamicViscosity && rightUnit is ImperialKinematicViscosity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryDynamicViscosity && rightUnit is ImperialKinematicViscosity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is DynamicViscosity && rightUnit is KinematicViscosity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Kinematic Viscosity from Density",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Density,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricDynamicViscosity && rightUnit is MetricDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialDynamicViscosity && rightUnit is ImperialDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialDynamicViscosity && rightUnit is UKImperialDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialDynamicViscosity && rightUnit is USCustomaryDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialDynamicViscosity && rightUnit is ImperialDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialDynamicViscosity && rightUnit is UKImperialDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryDynamicViscosity && rightUnit is ImperialDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryDynamicViscosity && rightUnit is USCustomaryDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is DynamicViscosity && rightUnit is Density -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Momentum from Area",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Area,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricDynamicViscosity && rightUnit is MetricArea -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialDynamicViscosity && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialDynamicViscosity && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryDynamicViscosity && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is DynamicViscosity && rightUnit is Area -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Pressure from Time",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Time,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricDynamicViscosity && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialDynamicViscosity && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialDynamicViscosity && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryDynamicViscosity && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is DynamicViscosity && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Time from Pressure",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Pressure,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is DynamicViscosity && rightUnit is Pressure -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
)
