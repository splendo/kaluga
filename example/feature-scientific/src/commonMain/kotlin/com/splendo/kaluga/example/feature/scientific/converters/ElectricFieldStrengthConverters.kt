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

package com.splendo.kaluga.example.feature.scientific.converters

import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.converter.electricFieldStrength.times
import com.splendo.kaluga.scientific.unit.ElectricCharge
import com.splendo.kaluga.scientific.unit.ElectricFieldStrength
import com.splendo.kaluga.scientific.unit.ElectricalConductivity
import com.splendo.kaluga.scientific.unit.ImperialElectricFieldStrength
import com.splendo.kaluga.scientific.unit.ImperialPermittivity
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.Permittivity

val PhysicalQuantity.ElectricFieldStrength.converters get() = listOf<QuantityConverter<PhysicalQuantity.ElectricFieldStrength, *>>(
    QuantityConverterWithOperator(
        "Electric Current Density from Electrical Conductivity",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.ElectricalConductivity,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ElectricFieldStrength && rightUnit is ElectricalConductivity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Force from Electric Charge",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.ElectricCharge,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ImperialElectricFieldStrength && rightUnit is ElectricCharge -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ElectricFieldStrength && rightUnit is ElectricCharge -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Surface Charge Density from Permittivity",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Permittivity,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ImperialElectricFieldStrength && rightUnit is ImperialPermittivity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ElectricFieldStrength && rightUnit is Permittivity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Voltage from Length",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Length,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ElectricFieldStrength && rightUnit is Length -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
)
