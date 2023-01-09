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
import com.splendo.kaluga.scientific.converter.electricInductance.div
import com.splendo.kaluga.scientific.converter.electricInductance.times
import com.splendo.kaluga.scientific.unit.*

val PhysicalQuantity.ElectricInductance.converters get() = listOf<QuantityConverter<PhysicalQuantity.ElectricInductance, *, *>>(
    QuantityConverter("Electric Resistance from Frequency", QuantityConverter.Type.Multiplication, FrequencyUnits) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abhenry && rightUnit is Frequency -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ElectricInductance && rightUnit is Frequency -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Electric Resistance from Time", QuantityConverter.Type.Division, TimeUnits) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abhenry && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ElectricInductance && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Magnetic Flux from Electric Current", QuantityConverter.Type.Multiplication, ElectricCurrentUnits) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abhenry && rightUnit is Abampere -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Abhenry && rightUnit is Biot -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ElectricInductance && rightUnit is ElectricCurrent -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Time from Electric Resistance", QuantityConverter.Type.Division, ElectricResistanceUnits) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ElectricInductance && rightUnit is ElectricResistance -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
)
