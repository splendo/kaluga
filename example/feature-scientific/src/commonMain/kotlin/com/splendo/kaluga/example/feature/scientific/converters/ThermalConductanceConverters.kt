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
import com.splendo.kaluga.scientific.converter.thermalConductance.div
import com.splendo.kaluga.scientific.converter.thermalConductance.thermalResistance
import com.splendo.kaluga.scientific.converter.thermalConductance.times
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialThermalConductance
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.MetricThermalConductance
import com.splendo.kaluga.scientific.unit.Temperature
import com.splendo.kaluga.scientific.unit.ThermalConductance
import com.splendo.kaluga.scientific.unit.ThermalConductivity
import com.splendo.kaluga.scientific.unit.UKImperialThermalConductance
import com.splendo.kaluga.scientific.unit.USCustomaryThermalConductance

val PhysicalQuantity.ThermalConductance.converters get() = listOf<QuantityConverter<PhysicalQuantity.ThermalConductance, *>>(
    QuantityConverterWithOperator(
        "Length from Thermal Conductivity",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.ThermalConductivity,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ThermalConductance && rightUnit is ThermalConductivity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Power from Temperature",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Temperature,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ThermalConductance && rightUnit is Temperature -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Thermal Conductivity from Length",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Length,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndUKImperialThermalConductance && rightUnit is MetricLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndUKImperialThermalConductance && rightUnit is ImperialLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricThermalConductance && rightUnit is MetricLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is UKImperialThermalConductance && rightUnit is ImperialLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is USCustomaryThermalConductance && rightUnit is ImperialLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ThermalConductance && rightUnit is Length -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    SingleQuantityConverter("Thermal Resistance") { value, unit ->
        when (unit) {
            is ThermalConductance -> DefaultScientificValue(value, unit).thermalResistance()
            else -> throw RuntimeException("Unexpected unit: $unit")
        }
    },
)
