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
import com.splendo.kaluga.scientific.converter.linearMassDensity.div
import com.splendo.kaluga.scientific.converter.linearMassDensity.times
import com.splendo.kaluga.scientific.unit.*

val PhysicalQuantity.LinearMassDensity.converters get() = listOf<QuantityConverter<PhysicalQuantity.LinearMassDensity, *, *>>(
    QuantityConverter("Area from Density", QuantityConverter.Type.Division, PhysicalQuantity.Density) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricLinearMassDensity && rightUnit is MetricDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialLinearMassDensity && rightUnit is ImperialDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialLinearMassDensity && rightUnit is UKImperialDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialLinearMassDensity && rightUnit is USCustomaryDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialLinearMassDensity && rightUnit is ImperialDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialLinearMassDensity && rightUnit is UKImperialDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryLinearMassDensity && rightUnit is ImperialDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryLinearMassDensity && rightUnit is USCustomaryDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is LinearMassDensity && rightUnit is Density -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Area from Specific Volume", QuantityConverter.Type.Multiplication, PhysicalQuantity.SpecificVolume) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricLinearMassDensity && rightUnit is MetricSpecificVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialLinearMassDensity && rightUnit is ImperialSpecificVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialLinearMassDensity && rightUnit is UKImperialSpecificVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialLinearMassDensity && rightUnit is USCustomarySpecificVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialLinearMassDensity && rightUnit is ImperialSpecificVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialLinearMassDensity && rightUnit is UKImperialSpecificVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryLinearMassDensity && rightUnit is ImperialSpecificVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryLinearMassDensity && rightUnit is USCustomarySpecificVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is LinearMassDensity && rightUnit is SpecificVolume -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Area Density from Length", QuantityConverter.Type.Division, PhysicalQuantity.Length) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricLinearMassDensity && rightUnit is MetricLength -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialLinearMassDensity && rightUnit is ImperialLength -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialLinearMassDensity && rightUnit is ImperialLength -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryLinearMassDensity && rightUnit is ImperialLength -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is LinearMassDensity && rightUnit is Length -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Density from Area", QuantityConverter.Type.Division, PhysicalQuantity.Area) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricLinearMassDensity && rightUnit is MetricArea -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialLinearMassDensity && rightUnit is ImperialArea -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialLinearMassDensity && rightUnit is ImperialArea -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryLinearMassDensity && rightUnit is ImperialArea -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is LinearMassDensity && rightUnit is Area -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Length from Area Density", QuantityConverter.Type.Division, PhysicalQuantity.AreaDensity) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricLinearMassDensity && rightUnit is MetricAreaDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialLinearMassDensity && rightUnit is ImperialAreaDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialLinearMassDensity && rightUnit is UKImperialAreaDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialLinearMassDensity && rightUnit is USCustomaryAreaDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialLinearMassDensity && rightUnit is ImperialAreaDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialLinearMassDensity && rightUnit is UKImperialAreaDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryLinearMassDensity && rightUnit is ImperialAreaDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryLinearMassDensity && rightUnit is USCustomaryAreaDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is LinearMassDensity && rightUnit is AreaDensity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Weight from Length", QuantityConverter.Type.Multiplication, PhysicalQuantity.Length) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricLinearMassDensity && rightUnit is MetricLength -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialLinearMassDensity && rightUnit is ImperialLength -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is UKImperialLinearMassDensity && rightUnit is ImperialLength -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is USCustomaryLinearMassDensity && rightUnit is ImperialLength -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is LinearMassDensity && rightUnit is Length -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    }
)
