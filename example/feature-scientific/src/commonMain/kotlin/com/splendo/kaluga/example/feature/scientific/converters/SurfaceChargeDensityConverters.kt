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
import com.splendo.kaluga.scientific.converter.surfaceChargeDensity.div
import com.splendo.kaluga.scientific.converter.surfaceChargeDensity.times
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.ElectricChargeDensity
import com.splendo.kaluga.scientific.unit.ElectricCurrentDensity
import com.splendo.kaluga.scientific.unit.ElectricFieldStrength
import com.splendo.kaluga.scientific.unit.Frequency
import com.splendo.kaluga.scientific.unit.ImperialElectricChargeDensity
import com.splendo.kaluga.scientific.unit.ImperialElectricFieldStrength
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.ImperialPermittivity
import com.splendo.kaluga.scientific.unit.ImperialSpeed
import com.splendo.kaluga.scientific.unit.ImperialSurfaceChargeDensity
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.MetricSurfaceChargeDensity
import com.splendo.kaluga.scientific.unit.Permittivity
import com.splendo.kaluga.scientific.unit.Speed
import com.splendo.kaluga.scientific.unit.SurfaceChargeDensity
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.UKImperialElectricChargeDensity
import com.splendo.kaluga.scientific.unit.USCustomaryElectricChargeDensity

val PhysicalQuantity.SurfaceChargeDensity.converters get() = listOf<QuantityConverter<PhysicalQuantity.SurfaceChargeDensity, *>>(
    QuantityConverterWithOperator(
        "Electric Charge from Area",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Area,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is SurfaceChargeDensity && rightUnit is Area -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Electric Charge Density from Length",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Length,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ImperialSurfaceChargeDensity && rightUnit is ImperialLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is SurfaceChargeDensity && rightUnit is Length -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Electric Current Density from Time",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Time,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricSurfaceChargeDensity && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialSurfaceChargeDensity && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is SurfaceChargeDensity && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Electric Current Density from Frequency",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Frequency,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricSurfaceChargeDensity && rightUnit is Frequency -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialSurfaceChargeDensity && rightUnit is Frequency -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is SurfaceChargeDensity && rightUnit is Frequency -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Electric Field Strength from Permittivity",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Permittivity,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ImperialSurfaceChargeDensity && rightUnit is ImperialPermittivity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is SurfaceChargeDensity && rightUnit is Permittivity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Length from Electric Charge Density",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.ElectricChargeDensity,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ImperialSurfaceChargeDensity && rightUnit is ImperialElectricChargeDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialSurfaceChargeDensity && rightUnit is UKImperialElectricChargeDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialSurfaceChargeDensity && rightUnit is USCustomaryElectricChargeDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is SurfaceChargeDensity && rightUnit is ElectricChargeDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Magnetic Field Strength from Speed",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Speed,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ImperialSurfaceChargeDensity && rightUnit is ImperialSpeed -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is SurfaceChargeDensity && rightUnit is Speed -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Permittivity from Electric Field Strength",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.ElectricFieldStrength,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ImperialSurfaceChargeDensity && rightUnit is ImperialElectricFieldStrength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is SurfaceChargeDensity && rightUnit is ElectricFieldStrength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Time from Electric Current Density",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.ElectricCurrentDensity,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is SurfaceChargeDensity && rightUnit is ElectricCurrentDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
)
