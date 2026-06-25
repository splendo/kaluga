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
import com.splendo.kaluga.scientific.converter.magneticFieldStrength.div
import com.splendo.kaluga.scientific.converter.magneticFieldStrength.times
import com.splendo.kaluga.scientific.unit.ElectricCurrentDensity
import com.splendo.kaluga.scientific.unit.ImperialElectricCurrentDensity
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.ImperialMagneticFieldStrength
import com.splendo.kaluga.scientific.unit.ImperialSpeed
import com.splendo.kaluga.scientific.unit.ImperialSurfaceChargeDensity
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.MagneticFieldStrength
import com.splendo.kaluga.scientific.unit.Permeability
import com.splendo.kaluga.scientific.unit.Speed
import com.splendo.kaluga.scientific.unit.SurfaceChargeDensity

val PhysicalQuantity.MagneticFieldStrength.converters get() = listOf<QuantityConverter<PhysicalQuantity.MagneticFieldStrength, *>>(
    QuantityConverterWithOperator(
        "Electric Current from Length",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Length,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MagneticFieldStrength && rightUnit is Length -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Electric Current Density from Length",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Length,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ImperialMagneticFieldStrength && rightUnit is ImperialLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MagneticFieldStrength && rightUnit is Length -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Length from Electric Current Density",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.ElectricCurrentDensity,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ImperialMagneticFieldStrength && rightUnit is ImperialElectricCurrentDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MagneticFieldStrength && rightUnit is ElectricCurrentDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Magnetic Induction from Permeability",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Permeability,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MagneticFieldStrength && rightUnit is Permeability -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Surface Charge Density from Speed",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Speed,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ImperialMagneticFieldStrength && rightUnit is ImperialSpeed -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MagneticFieldStrength && rightUnit is Speed -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Speed from Surface Charge Density",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.SurfaceChargeDensity,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is ImperialMagneticFieldStrength && rightUnit is ImperialSurfaceChargeDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MagneticFieldStrength && rightUnit is SurfaceChargeDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
)
