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
import com.splendo.kaluga.scientific.converter.voltage.div
import com.splendo.kaluga.scientific.converter.voltage.times
import com.splendo.kaluga.scientific.unit.Abampere
import com.splendo.kaluga.scientific.unit.Abcoulomb
import com.splendo.kaluga.scientific.unit.Abfarad
import com.splendo.kaluga.scientific.unit.Abohm
import com.splendo.kaluga.scientific.unit.Absiemens
import com.splendo.kaluga.scientific.unit.Abvolt
import com.splendo.kaluga.scientific.unit.Biot
import com.splendo.kaluga.scientific.unit.ElectricCapacitance
import com.splendo.kaluga.scientific.unit.ElectricCharge
import com.splendo.kaluga.scientific.unit.ElectricConductance
import com.splendo.kaluga.scientific.unit.ElectricCurrent
import com.splendo.kaluga.scientific.unit.ElectricResistance
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.Voltage

val PhysicalQuantity.Voltage.converters get() = listOf<QuantityConverter<PhysicalQuantity.Voltage, *>>(
    QuantityConverterWithOperator("Electric Charge from Electric Capacitance", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.ElectricCapacitance) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abvolt && rightUnit is Abfarad -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Voltage && rightUnit is ElectricCapacitance -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Electric Current from Electric Conductance", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.ElectricConductance) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abvolt && rightUnit is Absiemens -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Voltage && rightUnit is ElectricConductance -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Electric Current from Electric Resistance", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.ElectricResistance) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abvolt && rightUnit is Abohm -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Voltage && rightUnit is ElectricResistance -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Electric Resistance from Electric Current", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.ElectricCurrent) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abvolt && rightUnit is Abampere -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Abvolt && rightUnit is Biot -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Voltage && rightUnit is ElectricCurrent -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Energy from Electric Charge", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.ElectricCharge) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abvolt && rightUnit is Abcoulomb -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Voltage && rightUnit is ElectricCharge -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Magnetic Flux from Time", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.Time) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abvolt && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Voltage && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Power from Electric Current", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.ElectricCurrent) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abvolt && rightUnit is Abampere -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Abvolt && rightUnit is Biot -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Voltage && rightUnit is ElectricCurrent -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    }
)
