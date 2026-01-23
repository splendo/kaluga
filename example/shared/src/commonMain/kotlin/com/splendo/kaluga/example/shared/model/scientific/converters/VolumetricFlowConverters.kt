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
import com.splendo.kaluga.scientific.converter.volumetricFlow.div
import com.splendo.kaluga.scientific.converter.volumetricFlow.times
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.BaryeMultiple
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialPressure
import com.splendo.kaluga.scientific.unit.ImperialTonSquareInch
import com.splendo.kaluga.scientific.unit.ImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.ImperialVolumetricFlux
import com.splendo.kaluga.scientific.unit.KiloPoundSquareInch
import com.splendo.kaluga.scientific.unit.KipSquareInch
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricVolumetricFlow
import com.splendo.kaluga.scientific.unit.MetricVolumetricFlux
import com.splendo.kaluga.scientific.unit.OunceSquareInch
import com.splendo.kaluga.scientific.unit.PoundSquareInch
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.UKImperialPressure
import com.splendo.kaluga.scientific.unit.UKImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.UKImperialVolumetricFlux
import com.splendo.kaluga.scientific.unit.USCustomaryPressure
import com.splendo.kaluga.scientific.unit.USCustomaryVolumetricFlow
import com.splendo.kaluga.scientific.unit.USCustomaryVolumetricFlux
import com.splendo.kaluga.scientific.unit.USTonSquareInch
import com.splendo.kaluga.scientific.unit.VolumetricFlow
import com.splendo.kaluga.scientific.unit.VolumetricFlux

val PhysicalQuantity.VolumetricFlow.converters get() = listOf<QuantityConverter<PhysicalQuantity.VolumetricFlow, *>>(
    QuantityConverterWithOperator(
        "Area from Volumetric Flux",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.VolumetricFlux,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricVolumetricFlow && rightUnit is MetricVolumetricFlux -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialVolumetricFlow && rightUnit is ImperialVolumetricFlux -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialVolumetricFlow && rightUnit is UKImperialVolumetricFlux -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialVolumetricFlow && rightUnit is USCustomaryVolumetricFlux -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialVolumetricFlow && rightUnit is ImperialVolumetricFlux -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialVolumetricFlow && rightUnit is UKImperialVolumetricFlux -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryVolumetricFlow && rightUnit is ImperialVolumetricFlux -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryVolumetricFlow && rightUnit is USCustomaryVolumetricFlux -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is VolumetricFlow && rightUnit is VolumetricFlux -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Power from Pressure",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Pressure,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricVolumetricFlow && rightUnit is Barye -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricVolumetricFlow && rightUnit is BaryeMultiple -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialVolumetricFlow && rightUnit is PoundSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialVolumetricFlow && rightUnit is PoundSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryVolumetricFlow && rightUnit is PoundSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialVolumetricFlow && rightUnit is OunceSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialVolumetricFlow && rightUnit is OunceSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryVolumetricFlow && rightUnit is OunceSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialVolumetricFlow && rightUnit is KiloPoundSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialVolumetricFlow && rightUnit is KiloPoundSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryVolumetricFlow && rightUnit is KiloPoundSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryVolumetricFlow && rightUnit is KipSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryVolumetricFlow && rightUnit is USTonSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialVolumetricFlow && rightUnit is ImperialTonSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialVolumetricFlow && rightUnit is ImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialVolumetricFlow && rightUnit is ImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryVolumetricFlow && rightUnit is ImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialVolumetricFlow && rightUnit is UKImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialVolumetricFlow && rightUnit is UKImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialVolumetricFlow && rightUnit is USCustomaryPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryVolumetricFlow && rightUnit is USCustomaryPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is VolumetricFlow && rightUnit is Pressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Volume from Time",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Time,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricVolumetricFlow && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialVolumetricFlow && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialVolumetricFlow && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryVolumetricFlow && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is VolumetricFlow && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Volumetric Flux from Area",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Area,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricVolumetricFlow && rightUnit is MetricArea -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialVolumetricFlow && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialVolumetricFlow && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryVolumetricFlow && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is VolumetricFlow && rightUnit is Area -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
)
