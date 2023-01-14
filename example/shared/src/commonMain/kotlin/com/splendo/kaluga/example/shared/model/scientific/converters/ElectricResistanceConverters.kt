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
import com.splendo.kaluga.scientific.converter.electricResistance.div
import com.splendo.kaluga.scientific.converter.electricResistance.times
import com.splendo.kaluga.scientific.unit.Abampere
import com.splendo.kaluga.scientific.unit.Abcoulomb
import com.splendo.kaluga.scientific.unit.Abohm
import com.splendo.kaluga.scientific.unit.Biot
import com.splendo.kaluga.scientific.unit.ElectricCapacitance
import com.splendo.kaluga.scientific.unit.ElectricCharge
import com.splendo.kaluga.scientific.unit.ElectricCurrent
import com.splendo.kaluga.scientific.unit.ElectricInductance
import com.splendo.kaluga.scientific.unit.ElectricResistance
import com.splendo.kaluga.scientific.unit.Frequency
import com.splendo.kaluga.scientific.unit.Time

val PhysicalQuantity.ElectricResistance.converters get() = listOf<QuantityConverter<PhysicalQuantity.ElectricResistance, *>>(
    QuantityConverterWithOperator("Electric Inductance from Frequency", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Frequency) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abohm && rightUnit is Frequency -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ElectricResistance && rightUnit is Frequency -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Electric Inductance from Time", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.Time) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abohm && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ElectricResistance && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Frequency from Electric Inductance", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.ElectricInductance) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ElectricResistance && rightUnit is ElectricInductance -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Magnetic Flux from Electric Charge", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.ElectricCharge) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abohm && rightUnit is Abcoulomb -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ElectricResistance && rightUnit is ElectricCharge -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Time from Electric Capacitance", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.ElectricCapacitance) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ElectricResistance && rightUnit is ElectricCapacitance -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Voltage from Electric Current", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.ElectricCurrent) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abohm && rightUnit is Abampere -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Abohm && rightUnit is Biot -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ElectricResistance && rightUnit is ElectricCurrent -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    }
)
