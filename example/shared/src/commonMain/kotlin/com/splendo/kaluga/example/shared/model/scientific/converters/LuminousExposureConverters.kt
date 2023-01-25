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
import com.splendo.kaluga.scientific.converter.luminousExposure.div
import com.splendo.kaluga.scientific.converter.luminousExposure.times
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Illuminance
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialIlluminance
import com.splendo.kaluga.scientific.unit.ImperialLuminousExposure
import com.splendo.kaluga.scientific.unit.LuminousExposure
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricIlluminance
import com.splendo.kaluga.scientific.unit.MetricLuminousExposure
import com.splendo.kaluga.scientific.unit.Time

val PhysicalQuantity.LuminousExposure.converters get() = listOf<QuantityConverter<PhysicalQuantity.LuminousExposure, *>>(
    QuantityConverterWithOperator("Illuminance from Time", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Time) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricLuminousExposure && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialLuminousExposure && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is LuminousExposure && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Luminous Energy from Area", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.Area) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricLuminousExposure && rightUnit is MetricArea -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialLuminousExposure && rightUnit is ImperialArea -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is LuminousExposure && rightUnit is Area -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Time from Illuminance", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Illuminance) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricLuminousExposure && rightUnit is MetricIlluminance -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialLuminousExposure && rightUnit is ImperialIlluminance -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is LuminousExposure && rightUnit is Illuminance -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    }
)
