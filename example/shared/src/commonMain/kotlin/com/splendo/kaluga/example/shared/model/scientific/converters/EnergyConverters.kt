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
import com.splendo.kaluga.scientific.converter.energy.absorbedBy
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.energy.equivalentDoseBy
import com.splendo.kaluga.scientific.converter.energy.times
import com.splendo.kaluga.scientific.unit.Abampere
import com.splendo.kaluga.scientific.unit.Abcoulomb
import com.splendo.kaluga.scientific.unit.Abvolt
import com.splendo.kaluga.scientific.unit.AmountOfSubstance
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.BaryeMultiple
import com.splendo.kaluga.scientific.unit.Biot
import com.splendo.kaluga.scientific.unit.Centijoule
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.CentiwattHour
import com.splendo.kaluga.scientific.unit.CubicCentimeter
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.Decajoule
import com.splendo.kaluga.scientific.unit.DecawattHour
import com.splendo.kaluga.scientific.unit.Decijoule
import com.splendo.kaluga.scientific.unit.DeciwattHour
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.DyneMultiple
import com.splendo.kaluga.scientific.unit.ElectricCharge
import com.splendo.kaluga.scientific.unit.ElectricCurrent
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.ErgMultiple
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.FootPoundal
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.Gigajoule
import com.splendo.kaluga.scientific.unit.GigawattHour
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.HeatCapacity
import com.splendo.kaluga.scientific.unit.Hectojoule
import com.splendo.kaluga.scientific.unit.HectowattHour
import com.splendo.kaluga.scientific.unit.Horsepower
import com.splendo.kaluga.scientific.unit.HorsepowerHour
import com.splendo.kaluga.scientific.unit.Hour
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialCombinedPower
import com.splendo.kaluga.scientific.unit.ImperialEnergy
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.ImperialNamedEnergyUnit
import com.splendo.kaluga.scientific.unit.ImperialPressure
import com.splendo.kaluga.scientific.unit.ImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.ImperialSurfaceTension
import com.splendo.kaluga.scientific.unit.ImperialVolume
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.InchOunceForce
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.IonizingRadiationAbsorbedDose
import com.splendo.kaluga.scientific.unit.IonizingRadiationEquivalentDose
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilojoule
import com.splendo.kaluga.scientific.unit.KilowattHour
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.MagneticFlux
import com.splendo.kaluga.scientific.unit.Maxwell
import com.splendo.kaluga.scientific.unit.Megajoule
import com.splendo.kaluga.scientific.unit.MegawattHour
import com.splendo.kaluga.scientific.unit.MetricAndImperialCombinedPower
import com.splendo.kaluga.scientific.unit.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.unit.MetricAndImperialNamedEnergyUnit
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialHeatCapacity
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialTemperature
import com.splendo.kaluga.scientific.unit.MetricCombinedPower
import com.splendo.kaluga.scientific.unit.MetricEnergy
import com.splendo.kaluga.scientific.unit.MetricHeatCapacity
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.MetricNamedEnergyUnit
import com.splendo.kaluga.scientific.unit.MetricSpecificEnergy
import com.splendo.kaluga.scientific.unit.MetricSurfaceTension
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.Microjoule
import com.splendo.kaluga.scientific.unit.MicrowattHour
import com.splendo.kaluga.scientific.unit.Millijoule
import com.splendo.kaluga.scientific.unit.MilliwattHour
import com.splendo.kaluga.scientific.unit.MolarEnergy
import com.splendo.kaluga.scientific.unit.Nanojoule
import com.splendo.kaluga.scientific.unit.NanowattHour
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.PoundSquareFoot
import com.splendo.kaluga.scientific.unit.PoundSquareInch
import com.splendo.kaluga.scientific.unit.Poundal
import com.splendo.kaluga.scientific.unit.Power
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.Rad
import com.splendo.kaluga.scientific.unit.RadMultiple
import com.splendo.kaluga.scientific.unit.RoentgenEquivalentMan
import com.splendo.kaluga.scientific.unit.RoentgenEquivalentManMultiple
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareInch
import com.splendo.kaluga.scientific.unit.SurfaceTension
import com.splendo.kaluga.scientific.unit.Temperature
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.UKImperialHeatCapacity
import com.splendo.kaluga.scientific.unit.UKImperialPressure
import com.splendo.kaluga.scientific.unit.UKImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.UKImperialSurfaceTension
import com.splendo.kaluga.scientific.unit.UKImperialVolume
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import com.splendo.kaluga.scientific.unit.USCustomaryHeatCapacity
import com.splendo.kaluga.scientific.unit.USCustomaryPressure
import com.splendo.kaluga.scientific.unit.USCustomarySpecificEnergy
import com.splendo.kaluga.scientific.unit.USCustomarySurfaceTension
import com.splendo.kaluga.scientific.unit.USCustomaryTemperature
import com.splendo.kaluga.scientific.unit.USCustomaryVolume
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Voltage
import com.splendo.kaluga.scientific.unit.Volume
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.WattHourMultiple
import com.splendo.kaluga.scientific.unit.WattMultiple
import com.splendo.kaluga.scientific.unit.Weight

val PhysicalQuantity.Energy.converters get() = listOf<QuantityConverter<PhysicalQuantity.Energy, *>>(
    QuantityConverterWithOperator(
        "Action from Time",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Time,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndImperialEnergy && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricEnergy && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Amount of Substance from Molar Energy",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.MolarEnergy,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Energy && rightUnit is MolarEnergy -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Area from Surface Tension",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.SurfaceTension,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is MetricSurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ErgMultiple && rightUnit is MetricSurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is MetricSurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is InchPoundForce && rightUnit is ImperialSurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is InchPoundForce && rightUnit is UKImperialSurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is InchPoundForce && rightUnit is USCustomarySurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is InchOunceForce && rightUnit is ImperialSurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is InchOunceForce && rightUnit is UKImperialSurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is InchOunceForce && rightUnit is USCustomarySurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is ImperialSurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is UKImperialSurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is USCustomarySurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricEnergy && rightUnit is MetricSurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is ImperialSurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is UKImperialSurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is USCustomarySurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is SurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Electric Charge from Voltage",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Voltage,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is Abvolt -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ErgMultiple && rightUnit is Abvolt -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is Voltage -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Electric Current from Magnetic Flux",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.MagneticFlux,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is Maxwell -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ErgMultiple && rightUnit is Maxwell -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is MagneticFlux -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Force from Length",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Length,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is Centimeter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ErgMultiple && rightUnit is Centimeter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is MetricLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is FootPoundal && rightUnit is ImperialLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is InchOunceForce && rightUnit is ImperialLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is ImperialLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is ImperialLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is Length -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Heat Capacity from Temperature",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Temperature,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndImperialEnergy && rightUnit is MetricAndUKImperialTemperature -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is USCustomaryTemperature -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricEnergy && rightUnit is MetricAndUKImperialTemperature -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is MetricAndUKImperialTemperature -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is USCustomaryTemperature -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is Temperature -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Ionizing Radiation Absorbed Dose from Weight",
        QuantityConverter.WithOperator.Type.Custom("absorbed by"),
        PhysicalQuantity.Weight,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is Gram -> {
                DefaultScientificValue(leftValue, leftUnit) absorbedBy DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ErgMultiple && rightUnit is Gram -> {
                DefaultScientificValue(leftValue, leftUnit) absorbedBy DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is Weight -> {
                DefaultScientificValue(leftValue, leftUnit) absorbedBy DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Ionizing Radiation Equivalent Dose from Weight",
        QuantityConverter.WithOperator.Type.Custom("equivalent dose by"),
        PhysicalQuantity.Weight,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is Gram -> {
                DefaultScientificValue(leftValue, leftUnit) equivalentDoseBy DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ErgMultiple && rightUnit is Gram -> {
                DefaultScientificValue(leftValue, leftUnit) equivalentDoseBy DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is Weight -> {
                DefaultScientificValue(leftValue, leftUnit) equivalentDoseBy DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Length from Force",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Force,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is Dyne -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Erg && rightUnit is DyneMultiple -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ErgMultiple && rightUnit is Dyne -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ErgMultiple && rightUnit is DyneMultiple -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is FootPoundal && rightUnit is Poundal -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is FootPoundForce && rightUnit is PoundForce -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is InchPoundForce && rightUnit is PoundForce -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is InchOunceForce && rightUnit is OunceForce -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is ImperialForce -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is UKImperialForce -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is USCustomaryForce -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is ImperialForce -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is UKImperialForce -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is USCustomaryForce -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is Force -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Magnetic Flux from Electric Current",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.ElectricCurrent,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is Abampere -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ErgMultiple && rightUnit is Abampere -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Erg && rightUnit is Biot -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ErgMultiple && rightUnit is Biot -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is ElectricCurrent -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Molar Energy from Amount of Substance",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.AmountOfSubstance,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndImperialEnergy && rightUnit is AmountOfSubstance -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricEnergy && rightUnit is AmountOfSubstance -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is AmountOfSubstance -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is AmountOfSubstance -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Power from Time",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Time,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is WattHour && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is NanowattHour && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MicrowattHour && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MilliwattHour && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is CentiwattHour && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is DeciwattHour && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is DecawattHour && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is HectowattHour && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is KilowattHour && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MegawattHour && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is GigawattHour && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is WattHour && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Joule && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Nanojoule && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Microjoule && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Millijoule && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Centijoule && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Decijoule && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Decajoule && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Hectojoule && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Kilojoule && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Megajoule && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Gigajoule && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialNamedEnergyUnit && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricNamedEnergyUnit && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is HorsepowerHour && rightUnit is Hour -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialNamedEnergyUnit && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricEnergy && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Pressure from Volume",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Volume,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is CubicCentimeter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ErgMultiple && rightUnit is CubicCentimeter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is FootPoundal && rightUnit is CubicFoot -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is FootPoundForce && rightUnit is CubicFoot -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is ImperialVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is UKImperialVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is USCustomaryVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is ImperialVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is UKImperialVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is USCustomaryVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is Volume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Specific Energy from Weight",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Weight,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndImperialEnergy && rightUnit is MetricWeight -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is ImperialWeight -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is UKImperialWeight -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is USCustomaryWeight -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricEnergy && rightUnit is MetricWeight -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is ImperialWeight -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is UKImperialWeight -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is USCustomaryWeight -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is Weight -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Surface Tension from Area",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Area,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is SquareCentimeter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ErgMultiple && rightUnit is SquareCentimeter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is FootPoundal && rightUnit is SquareFoot -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is InchPoundForce && rightUnit is SquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is InchOunceForce && rightUnit is SquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is Area -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Temperature from Heat Capacity",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.HeatCapacity,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndImperialEnergy && rightUnit is MetricAndUKImperialHeatCapacity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is MetricHeatCapacity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is UKImperialHeatCapacity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is USCustomaryHeatCapacity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricEnergy && rightUnit is MetricHeatCapacity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is MetricAndUKImperialHeatCapacity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is UKImperialHeatCapacity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is USCustomaryHeatCapacity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is HeatCapacity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Time from Power",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Power,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is WattHour && rightUnit is Watt -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is WattHour && rightUnit is WattMultiple -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is WattHourMultiple && rightUnit is Watt -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is WattHourMultiple && rightUnit is Watt -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialNamedEnergyUnit && rightUnit is MetricAndImperialCombinedPower -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricNamedEnergyUnit && rightUnit is MetricCombinedPower -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is HorsepowerHour && rightUnit is Horsepower -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialNamedEnergyUnit && rightUnit is ImperialCombinedPower -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is Power -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Voltage from Electric Charge",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.ElectricCharge,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is Abcoulomb -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ErgMultiple && rightUnit is Abcoulomb -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is ElectricCharge -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Volume from Pressure",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Pressure,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is Barye -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Erg && rightUnit is BaryeMultiple -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ErgMultiple && rightUnit is Barye -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ErgMultiple && rightUnit is BaryeMultiple -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is FootPoundal && rightUnit is PoundSquareFoot -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is InchPoundForce && rightUnit is PoundSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is InchOunceForce && rightUnit is PoundSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is ImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is UKImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is USCustomaryPressure -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is ImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is UKImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is USCustomaryPressure -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is Pressure -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Weight from Ionizing Radiation Absorbed Dose",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.IonizingRadiationAbsorbedDose,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is Rad -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Erg && rightUnit is RadMultiple -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ErgMultiple && rightUnit is Rad -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ErgMultiple && rightUnit is RadMultiple -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is IonizingRadiationAbsorbedDose -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Weight from Ionizing Radiation Equivalent Dose",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.IonizingRadiationEquivalentDose,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is RoentgenEquivalentMan -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Erg && rightUnit is RoentgenEquivalentManMultiple -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ErgMultiple && rightUnit is RoentgenEquivalentMan -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ErgMultiple && rightUnit is RoentgenEquivalentManMultiple -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is IonizingRadiationEquivalentDose -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Weight from Specific Energy",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.SpecificEnergy,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndImperialEnergy && rightUnit is MetricSpecificEnergy -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is ImperialSpecificEnergy -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is UKImperialSpecificEnergy -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricAndImperialEnergy && rightUnit is USCustomarySpecificEnergy -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is MetricEnergy && rightUnit is MetricSpecificEnergy -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is ImperialSpecificEnergy -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is UKImperialSpecificEnergy -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is ImperialEnergy && rightUnit is USCustomarySpecificEnergy -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            leftUnit is Energy && rightUnit is SpecificEnergy -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }

            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
)
