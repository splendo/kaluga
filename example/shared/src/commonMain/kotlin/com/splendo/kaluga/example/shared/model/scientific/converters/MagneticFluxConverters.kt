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
import com.splendo.kaluga.scientific.converter.magneticFlux.div
import com.splendo.kaluga.scientific.converter.magneticFlux.times
import com.splendo.kaluga.scientific.unit.Abampere
import com.splendo.kaluga.scientific.unit.Abcoulomb
import com.splendo.kaluga.scientific.unit.Abhenry
import com.splendo.kaluga.scientific.unit.Abohm
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Biot
import com.splendo.kaluga.scientific.unit.ElectricCharge
import com.splendo.kaluga.scientific.unit.ElectricCurrent
import com.splendo.kaluga.scientific.unit.ElectricInductance
import com.splendo.kaluga.scientific.unit.ElectricResistance
import com.splendo.kaluga.scientific.unit.Gauss
import com.splendo.kaluga.scientific.unit.MagneticFlux
import com.splendo.kaluga.scientific.unit.MagneticInduction
import com.splendo.kaluga.scientific.unit.Maxwell
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.Voltage

val PhysicalQuantity.MagneticFlux.converters get() = listOf<QuantityConverter<PhysicalQuantity.MagneticFlux, *>>(
    QuantityConverterWithOperator(
        "Area from Magnetic Induction",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.MagneticInduction,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Maxwell && rightUnit is Gauss -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is MagneticFlux && rightUnit is MagneticInduction -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Electric Charge from Electric Resistance",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.ElectricResistance,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Maxwell && rightUnit is Abohm -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is MagneticFlux && rightUnit is ElectricResistance -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Electric Current from Electric Inductance",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.ElectricInductance,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Maxwell && rightUnit is Abhenry -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is MagneticFlux && rightUnit is ElectricInductance -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Electric Inductance from Electric Current",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.ElectricCurrent,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Maxwell && rightUnit is Abampere -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Maxwell && rightUnit is Biot -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is MagneticFlux && rightUnit is ElectricCurrent -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Electric Resistance from Electric Charge",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.ElectricCharge,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Maxwell && rightUnit is Abcoulomb -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is MagneticFlux && rightUnit is ElectricCharge -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Energy from Electric Current",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.ElectricCurrent,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Maxwell && rightUnit is Abampere -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Maxwell && rightUnit is Biot -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is MagneticFlux && rightUnit is ElectricCurrent -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Magnetic Induction from Area",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Area,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Maxwell && rightUnit is SquareCentimeter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is MagneticFlux && rightUnit is Area -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Time from Voltage",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Voltage,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MagneticFlux && rightUnit is Voltage -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Voltage from Time",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Time,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Maxwell && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is MagneticFlux && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
)
