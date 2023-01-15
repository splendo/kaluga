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
import com.splendo.kaluga.scientific.converter.electricConductance.div
import com.splendo.kaluga.scientific.converter.electricConductance.resistance
import com.splendo.kaluga.scientific.converter.electricConductance.times
import com.splendo.kaluga.scientific.unit.Absiemens
import com.splendo.kaluga.scientific.unit.Abvolt
import com.splendo.kaluga.scientific.unit.ElectricCapacitance
import com.splendo.kaluga.scientific.unit.ElectricConductance
import com.splendo.kaluga.scientific.unit.Frequency
import com.splendo.kaluga.scientific.unit.Voltage

val PhysicalQuantity.ElectricConductance.converters get() = listOf<QuantityConverter<PhysicalQuantity.ElectricConductance, *>>(
    QuantityConverterWithOperator("Electric Capacitance from Frequency", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Frequency) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Absiemens && rightUnit is Frequency -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ElectricConductance && rightUnit is Frequency -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Electric Current from Voltage", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.Voltage) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Absiemens && rightUnit is Abvolt -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ElectricConductance && rightUnit is Voltage -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    SingleQuantityConverter("Electric Resistance") { value, unit ->
        when (unit) {
            is Absiemens -> DefaultScientificValue(value, unit).resistance()
            is ElectricConductance -> DefaultScientificValue(value, unit).resistance()
            else -> throw RuntimeException("Unexpected unit: $unit")
        }
    },
    QuantityConverterWithOperator("Frequency from Electric Capacitance", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.ElectricCapacitance) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ElectricConductance && rightUnit is ElectricCapacitance -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    }
)
