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
import com.splendo.kaluga.scientific.converter.electricCurrent.div
import com.splendo.kaluga.scientific.converter.electricCurrent.times
import com.splendo.kaluga.scientific.unit.*

val PhysicalQuantity.ElectricCurrent.converters get() = listOf<QuantityConverter<PhysicalQuantity.ElectricCurrent, *, *>>(
    QuantityConverter("Electric Charge from Time", QuantityConverter.Type.Multiplication, TimeUnits) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abampere && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Biot && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ElectricCurrent && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Electric Charge from Time", QuantityConverter.Type.Division, VoltageUnits) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abampere && rightUnit is Abvolt -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Biot && rightUnit is Abvolt -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ElectricCurrent && rightUnit is Voltage -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Energy from Magnetic Flux", QuantityConverter.Type.Multiplication, MagneticFluxUnits) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abampere && rightUnit is Maxwell -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Biot && rightUnit is Maxwell -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ElectricCurrent && rightUnit is MagneticFlux -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Magnetic Flux from Electric Inductance", QuantityConverter.Type.Multiplication, ElectricInductanceUnits) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abampere && rightUnit is Abhenry -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Biot && rightUnit is Abhenry -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ElectricCurrent && rightUnit is ElectricInductance -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Power from Voltage", QuantityConverter.Type.Multiplication, VoltageUnits) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abampere && rightUnit is Abvolt -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Biot && rightUnit is Abvolt -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ElectricCurrent && rightUnit is Voltage -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Voltage from Electric Conductance", QuantityConverter.Type.Division, ElectricConductanceUnits) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abampere && rightUnit is Absiemens -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Biot && rightUnit is Absiemens -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ElectricCurrent && rightUnit is ElectricConductance -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverter("Voltage from Electric Resistance", QuantityConverter.Type.Multiplication, ElectricResistanceUnits) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Abampere && rightUnit is Abohm -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Biot && rightUnit is Abohm -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ElectricCurrent && rightUnit is ElectricResistance -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
)
