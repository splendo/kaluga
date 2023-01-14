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
import com.splendo.kaluga.scientific.converter.luminousFlux.div
import com.splendo.kaluga.scientific.converter.luminousFlux.times
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Illuminance
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialIlluminance
import com.splendo.kaluga.scientific.unit.LuminousFlux
import com.splendo.kaluga.scientific.unit.LuminousIntensity
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.Phot
import com.splendo.kaluga.scientific.unit.PhotMultiple
import com.splendo.kaluga.scientific.unit.SolidAngle
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.Time

val PhysicalQuantity.LuminousFlux.converters get() = listOf<QuantityConverter<PhysicalQuantity.LuminousFlux, *>>(
    QuantityConverterWithOperator("Area from Illuminance", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Illuminance) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is LuminousFlux && rightUnit is Phot -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is LuminousFlux && rightUnit is PhotMultiple -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is LuminousFlux && rightUnit is ImperialIlluminance -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is LuminousFlux && rightUnit is Illuminance -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Illuminance from Area", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Area) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is LuminousFlux && rightUnit is SquareCentimeter -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is LuminousFlux && rightUnit is MetricArea -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is LuminousFlux && rightUnit is ImperialArea -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is LuminousFlux && rightUnit is Area -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Luminous Energy from Time", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.Time) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is LuminousFlux && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Luminous Intensity from Solid Angle", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.SolidAngle) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is LuminousFlux && rightUnit is SolidAngle -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Solid Angle from Luminous Intensity", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.LuminousIntensity) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is LuminousFlux && rightUnit is LuminousIntensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    }
)
