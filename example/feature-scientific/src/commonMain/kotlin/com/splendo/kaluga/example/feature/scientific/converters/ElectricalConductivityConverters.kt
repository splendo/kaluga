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
import com.splendo.kaluga.scientific.converter.electricalConductivity.resistivity
import com.splendo.kaluga.scientific.converter.electricalConductivity.times
import com.splendo.kaluga.scientific.unit.ElectricFieldStrength
import com.splendo.kaluga.scientific.unit.ElectricalConductivity
import com.splendo.kaluga.scientific.unit.ImperialElectricFieldStrength
import com.splendo.kaluga.scientific.unit.ImperialElectricalConductivity
import com.splendo.kaluga.scientific.unit.Length

val PhysicalQuantity.ElectricalConductivity.converters get() = listOf<QuantityConverter<PhysicalQuantity.ElectricalConductivity, *>>(
    QuantityConverterWithOperator(
        "Electric Conductance from Length",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Length,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ElectricalConductivity && rightUnit is Length -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Electric Current Density from Electric Field Strength",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.ElectricFieldStrength,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ImperialElectricalConductivity && rightUnit is ImperialElectricFieldStrength -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ElectricalConductivity && rightUnit is ElectricFieldStrength -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    SingleQuantityConverter("Resistivity") { value, unit ->
        when (unit) {
            is ElectricalConductivity -> DefaultScientificValue(value, unit).resistivity()
            else -> throw RuntimeException("Unexpected unit: $unit")
        }
    },
)
