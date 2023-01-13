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
import com.splendo.kaluga.scientific.converter.luminousEnergy.div
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialLuminousExposure
import com.splendo.kaluga.scientific.unit.LuminousEnergy
import com.splendo.kaluga.scientific.unit.LuminousExposure
import com.splendo.kaluga.scientific.unit.LuminousFlux
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricLuminousExposure
import com.splendo.kaluga.scientific.unit.Time

val PhysicalQuantity.LuminousEnergy.converters get() = listOf<QuantityConverter<PhysicalQuantity.LuminousEnergy, *, *>>(
    QuantityConverter("Energy from Luminous Exposure", QuantityConverter.Type.Division, PhysicalQuantity.LuminousExposure) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is LuminousEnergy && rightUnit is MetricLuminousExposure -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is LuminousEnergy && rightUnit is ImperialLuminousExposure -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is LuminousEnergy && rightUnit is LuminousExposure -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Luminous Exposure from Area", QuantityConverter.Type.Division, PhysicalQuantity.Area) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is LuminousEnergy && rightUnit is MetricArea -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is LuminousEnergy && rightUnit is ImperialArea -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is LuminousEnergy && rightUnit is Area -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Luminous Flux from Time", QuantityConverter.Type.Division, PhysicalQuantity.Time) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is LuminousEnergy && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Time from Luminous Flux", QuantityConverter.Type.Division, PhysicalQuantity.LuminousFlux) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is LuminousEnergy && rightUnit is LuminousFlux -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    }
)
