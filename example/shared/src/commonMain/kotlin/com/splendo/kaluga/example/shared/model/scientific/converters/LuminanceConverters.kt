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
import com.splendo.kaluga.scientific.converter.luminance.times
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.ImperialLuminance
import com.splendo.kaluga.scientific.unit.Lambert
import com.splendo.kaluga.scientific.unit.Luminance
import com.splendo.kaluga.scientific.unit.MetricLuminance
import com.splendo.kaluga.scientific.unit.SolidAngle
import com.splendo.kaluga.scientific.unit.Stilb

val PhysicalQuantity.Luminance.converters get() = listOf<QuantityConverter<PhysicalQuantity.Luminance, *, *>>(
    QuantityConverter("Illuminance from Solid Angle", QuantityConverter.Type.Multiplication, PhysicalQuantity.SolidAngle) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Stilb && rightUnit is SolidAngle -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Lambert && rightUnit is SolidAngle -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricLuminance && rightUnit is SolidAngle -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialLuminance && rightUnit is SolidAngle -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Luminance && rightUnit is SolidAngle -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Luminous Intensity from Area", QuantityConverter.Type.Multiplication, PhysicalQuantity.Area) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Luminance && rightUnit is Area -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    }
)
