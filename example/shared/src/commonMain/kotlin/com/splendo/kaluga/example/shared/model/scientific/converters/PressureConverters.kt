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
import com.splendo.kaluga.scientific.converter.pressure.times
import com.splendo.kaluga.scientific.unit.*

val PhysicalQuantity.Pressure.converters get() = listOf<QuantityConverter<PhysicalQuantity.Pressure, *>>(
    QuantityConverterWithOperator(
        "Dynamic Viscosity from Time",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Time,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricPressure && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialPressure && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialPressure && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryPressure && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Pressure && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Energy from Volume",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Volume,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Barye && rightUnit is CubicCentimeter -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is BaryeMultiple && rightUnit is CubicCentimeter -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is PoundSquareFoot && rightUnit is CubicFoot -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is PoundSquareInch && rightUnit is CubicInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is OunceSquareInch && rightUnit is CubicInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is KiloPoundSquareInch && rightUnit is CubicInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is KipSquareInch && rightUnit is CubicInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USTonSquareInch && rightUnit is CubicInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialTonSquareInch && rightUnit is CubicInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialPressure && rightUnit is ImperialVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialPressure && rightUnit is UKImperialVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialPressure && rightUnit is USCustomaryVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialPressure && rightUnit is ImperialVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialPressure && rightUnit is UKImperialVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryPressure && rightUnit is ImperialVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryPressure && rightUnit is USCustomaryVolume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Pressure && rightUnit is Volume -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Force from Area",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Area,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Barye && rightUnit is SquareCentimeter -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is BaryeMultiple && rightUnit is SquareCentimeter -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is OunceSquareInch && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is KipSquareInch && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is KipSquareFoot && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USTonSquareInch && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USTonSquareFoot && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialTonSquareInch && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialTonSquareFoot && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialPressure && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialPressure && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryPressure && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Pressure && rightUnit is Area -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
)
