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
import com.splendo.kaluga.scientific.converter.power.div
import com.splendo.kaluga.scientific.converter.power.times
import com.splendo.kaluga.scientific.unit.Abampere
import com.splendo.kaluga.scientific.unit.Abvolt
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.BaryeMultiple
import com.splendo.kaluga.scientific.unit.Biot
import com.splendo.kaluga.scientific.unit.Centiwatt
import com.splendo.kaluga.scientific.unit.Decawatt
import com.splendo.kaluga.scientific.unit.Deciwatt
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.DyneMultiple
import com.splendo.kaluga.scientific.unit.ElectricCurrent
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.Gigawatt
import com.splendo.kaluga.scientific.unit.Hectowatt
import com.splendo.kaluga.scientific.unit.Horsepower
import com.splendo.kaluga.scientific.unit.Hour
import com.splendo.kaluga.scientific.unit.ImperialCombinedPower
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.ImperialPower
import com.splendo.kaluga.scientific.unit.ImperialPressure
import com.splendo.kaluga.scientific.unit.ImperialSpeed
import com.splendo.kaluga.scientific.unit.ImperialTonSquareInch
import com.splendo.kaluga.scientific.unit.ImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.KiloPoundSquareInch
import com.splendo.kaluga.scientific.unit.Kilowatt
import com.splendo.kaluga.scientific.unit.KipSquareInch
import com.splendo.kaluga.scientific.unit.Megawatt
import com.splendo.kaluga.scientific.unit.MetricAndImperialCombinedPower
import com.splendo.kaluga.scientific.unit.MetricAndImperialPower
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialThermalResistance
import com.splendo.kaluga.scientific.unit.MetricCombinedPower
import com.splendo.kaluga.scientific.unit.MetricForce
import com.splendo.kaluga.scientific.unit.MetricPower
import com.splendo.kaluga.scientific.unit.MetricSpeed
import com.splendo.kaluga.scientific.unit.MetricThermalResistance
import com.splendo.kaluga.scientific.unit.MetricVolumetricFlow
import com.splendo.kaluga.scientific.unit.Microwatt
import com.splendo.kaluga.scientific.unit.Milliwatt
import com.splendo.kaluga.scientific.unit.Nanowatt
import com.splendo.kaluga.scientific.unit.OunceSquareInch
import com.splendo.kaluga.scientific.unit.PoundSquareInch
import com.splendo.kaluga.scientific.unit.Power
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.Speed
import com.splendo.kaluga.scientific.unit.ThermalResistance
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.UKImperialPressure
import com.splendo.kaluga.scientific.unit.UKImperialThermalResistance
import com.splendo.kaluga.scientific.unit.UKImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import com.splendo.kaluga.scientific.unit.USCustomaryPressure
import com.splendo.kaluga.scientific.unit.USCustomaryThermalResistance
import com.splendo.kaluga.scientific.unit.USCustomaryVolumetricFlow
import com.splendo.kaluga.scientific.unit.USTonSquareInch
import com.splendo.kaluga.scientific.unit.Voltage
import com.splendo.kaluga.scientific.unit.VolumetricFlow
import com.splendo.kaluga.scientific.unit.Watt

val PhysicalQuantity.Power.converters get() = listOf<QuantityConverter<PhysicalQuantity.Power, *>>(
    QuantityConverterWithOperator(
        "Electric Current from Voltage",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Voltage,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricCombinedPower && rightUnit is Abvolt -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Power && rightUnit is Voltage -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Energy from Time",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Time,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Watt && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Nanowatt && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Microwatt && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Milliwatt && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Centiwatt && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Deciwatt && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Decawatt && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Hectowatt && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Kilowatt && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Megawatt && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Gigawatt && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialCombinedPower && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricCombinedPower && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricPower && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialCombinedPower && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Horsepower && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialPower && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Power && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Force from Speed",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Speed,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricCombinedPower && rightUnit is MetricSpeed -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricPower && rightUnit is MetricSpeed -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialCombinedPower && rightUnit is MetricSpeed -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialPower && rightUnit is ImperialSpeed -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialPower && rightUnit is ImperialSpeed -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Power && rightUnit is Speed -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Pressure from Volumetric Flow",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.VolumetricFlow,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricCombinedPower && rightUnit is MetricVolumetricFlow -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialCombinedPower && rightUnit is ImperialVolumetricFlow -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialCombinedPower && rightUnit is UKImperialVolumetricFlow -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialCombinedPower && rightUnit is USCustomaryVolumetricFlow -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialPower && rightUnit is ImperialVolumetricFlow -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialPower && rightUnit is UKImperialVolumetricFlow -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialPower && rightUnit is USCustomaryVolumetricFlow -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Power && rightUnit is VolumetricFlow -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Speed from Force",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Force,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricCombinedPower && rightUnit is Dyne -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricCombinedPower && rightUnit is DyneMultiple -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricPower && rightUnit is MetricForce -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialCombinedPower && rightUnit is ImperialForce -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialPower && rightUnit is ImperialForce -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialPower && rightUnit is UKImperialForce -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialPower && rightUnit is USCustomaryForce -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialPower && rightUnit is ImperialForce -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialPower && rightUnit is UKImperialForce -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialPower && rightUnit is USCustomaryForce -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Power && rightUnit is Force -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Temperature from Thermal Resistance",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.ThermalResistance,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Power && rightUnit is MetricAndUKImperialThermalResistance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialPower && rightUnit is MetricThermalResistance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricPower && rightUnit is MetricThermalResistance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialPower && rightUnit is UKImperialThermalResistance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialPower && rightUnit is UKImperialThermalResistance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialPower && rightUnit is USCustomaryThermalResistance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialPower && rightUnit is USCustomaryThermalResistance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Power && rightUnit is ThermalResistance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Voltage from Electric Current",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.ElectricCurrent,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricCombinedPower && rightUnit is Abampere -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricCombinedPower && rightUnit is Biot -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Power && rightUnit is ElectricCurrent -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Volumetric Flow from Pressure",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Pressure,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricCombinedPower && rightUnit is Barye -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricCombinedPower && rightUnit is BaryeMultiple -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialCombinedPower && rightUnit is PoundSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialCombinedPower && rightUnit is OunceSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialCombinedPower && rightUnit is KiloPoundSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialCombinedPower && rightUnit is KipSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialCombinedPower && rightUnit is USTonSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialCombinedPower && rightUnit is ImperialTonSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialCombinedPower && rightUnit is ImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialCombinedPower && rightUnit is UKImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialCombinedPower && rightUnit is USCustomaryPressure -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialPower && rightUnit is PoundSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialPower && rightUnit is OunceSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialPower && rightUnit is KiloPoundSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialPower && rightUnit is KipSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialPower && rightUnit is USTonSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialPower && rightUnit is ImperialTonSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialPower && rightUnit is ImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialPower && rightUnit is UKImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialPower && rightUnit is USCustomaryPressure -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Power && rightUnit is Pressure -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
)
