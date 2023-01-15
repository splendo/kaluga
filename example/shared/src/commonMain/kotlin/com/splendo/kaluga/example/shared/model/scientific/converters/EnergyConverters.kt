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
import com.splendo.kaluga.scientific.unit.*

val PhysicalQuantity.Energy.converters get() = listOf<QuantityConverter<PhysicalQuantity.Energy, *>>(
    QuantityConverterWithOperator("Action from Time", QuantityConverter.WithOperator.Type.Multiplication, PhysicalQuantity.Time) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndImperialEnergy && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricEnergy && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Amount of Substance from Molar Energy", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.MolarEnergy) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Energy && rightUnit is MolarEnergy -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Area from Surface Tension", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.SurfaceTension) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is MetricSurfaceTension -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ErgMultiple && rightUnit is MetricSurfaceTension -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is MetricSurfaceTension -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is InchPoundForce && rightUnit is ImperialSurfaceTension -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is InchPoundForce && rightUnit is UKImperialSurfaceTension -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is InchPoundForce && rightUnit is USCustomarySurfaceTension -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is InchOunceForce && rightUnit is ImperialSurfaceTension -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is InchOunceForce && rightUnit is UKImperialSurfaceTension -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is InchOunceForce && rightUnit is USCustomarySurfaceTension -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is ImperialSurfaceTension -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is UKImperialSurfaceTension -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is USCustomarySurfaceTension -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricEnergy && rightUnit is MetricSurfaceTension -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is ImperialSurfaceTension -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is UKImperialSurfaceTension -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is USCustomarySurfaceTension -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is SurfaceTension -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Electric Charge from Voltage", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Voltage) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is Abvolt -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ErgMultiple && rightUnit is Abvolt -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is Voltage -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Electric Current from Magnetic Flux", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.MagneticFlux) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is Maxwell -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ErgMultiple && rightUnit is Maxwell -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is MagneticFlux -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Force from Length", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Length) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is Centimeter -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ErgMultiple && rightUnit is Centimeter -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is MetricLength -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is FootPoundal && rightUnit is ImperialLength -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is InchOunceForce && rightUnit is ImperialLength -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is ImperialLength -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is ImperialLength -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is Length -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Heat Capacity from Temperature", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Temperature) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndImperialEnergy && rightUnit is MetricAndUKImperialTemperature -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is USCustomaryTemperature -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricEnergy && rightUnit is MetricAndUKImperialTemperature -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is MetricAndUKImperialTemperature -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is USCustomaryTemperature -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is Temperature -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Ionizing Radiation Absorbed Dose from Weight", QuantityConverter.WithOperator.Type.Custom("absorbed by"), PhysicalQuantity.Weight) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is Gram -> DefaultScientificValue(leftValue, leftUnit) absorbedBy DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ErgMultiple && rightUnit is Gram -> DefaultScientificValue(leftValue, leftUnit) absorbedBy DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is Weight -> DefaultScientificValue(leftValue, leftUnit) absorbedBy DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Ionizing Radiation Equivalent Dose from Weight", QuantityConverter.WithOperator.Type.Custom("equivalent dose by"), PhysicalQuantity.Weight) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is Gram -> DefaultScientificValue(leftValue, leftUnit) equivalentDoseBy DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ErgMultiple && rightUnit is Gram -> DefaultScientificValue(leftValue, leftUnit) equivalentDoseBy DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is Weight -> DefaultScientificValue(leftValue, leftUnit) equivalentDoseBy DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Length from Force", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Force) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is Dyne -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Erg && rightUnit is DyneMultiple -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ErgMultiple && rightUnit is Dyne -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ErgMultiple && rightUnit is DyneMultiple -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is FootPoundal && rightUnit is Poundal -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is FootPoundForce && rightUnit is PoundForce -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is InchPoundForce && rightUnit is PoundForce -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is InchOunceForce && rightUnit is OunceForce -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is ImperialForce -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is UKImperialForce -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is USCustomaryForce -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is ImperialForce -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is UKImperialForce -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is USCustomaryForce -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is Force -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Magnetic Flux from Electric Current", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.ElectricCurrent) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is Abampere -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ErgMultiple && rightUnit is Abampere -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Erg && rightUnit is Biot -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ErgMultiple && rightUnit is Biot -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is ElectricCurrent -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Molar Energy from Amount of Substance", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.AmountOfSubstance) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndImperialEnergy && rightUnit is AmountOfSubstance -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricEnergy && rightUnit is AmountOfSubstance -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is AmountOfSubstance -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is AmountOfSubstance -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Power from Time", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Time) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is WattHour && rightUnit is Hour -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is WattHourMultiple && rightUnit is Hour -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Joule && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is JouleMultiple && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Erg && rightUnit is Second -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ErgMultiple && rightUnit is Second -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is FootPoundal && rightUnit is Second -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is FootPoundal && rightUnit is Minute -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is FootPoundForce && rightUnit is Second -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is FootPoundForce && rightUnit is Minute -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is InchPoundForce && rightUnit is Second -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is InchPoundForce && rightUnit is Minute -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is InchOunceForce && rightUnit is Second -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is InchOunceForce && rightUnit is Minute -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is BritishThermalUnit && rightUnit is Hour -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is BritishThermalUnit && rightUnit is Minute -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is BritishThermalUnit && rightUnit is Second -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricEnergy && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is Time -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Pressure from Volume", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Volume) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is CubicCentimeter -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ErgMultiple && rightUnit is CubicCentimeter -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is FootPoundal && rightUnit is CubicFoot -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is FootPoundForce && rightUnit is CubicFoot -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is ImperialVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is UKImperialVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is USCustomaryVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is ImperialVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is UKImperialVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is USCustomaryVolume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is Volume -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Specific Energy from Weight", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Weight) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndImperialEnergy && rightUnit is MetricWeight -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is ImperialWeight -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is UKImperialWeight -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is USCustomaryWeight -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricEnergy && rightUnit is MetricWeight -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is ImperialWeight -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is UKImperialWeight -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is USCustomaryWeight -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is Weight -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Surface Tension from Area", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Area) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is SquareCentimeter -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ErgMultiple && rightUnit is SquareCentimeter -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is FootPoundal && rightUnit is SquareFoot -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is InchPoundForce && rightUnit is SquareInch -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is InchOunceForce && rightUnit is SquareInch -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is ImperialArea -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is ImperialArea -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is Area -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Temperature from Heat Capacity", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.HeatCapacity) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndImperialEnergy && rightUnit is MetricAndUKImperialHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is MetricHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is UKImperialHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is USCustomaryHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricEnergy && rightUnit is MetricHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is MetricAndUKImperialHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is UKImperialHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is USCustomaryHeatCapacity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is HeatCapacity -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Time from Power", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Power) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is WattHour && rightUnit is Watt -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is WattHour && rightUnit is WattMultiple -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is WattHourMultiple && rightUnit is Watt -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is WattHourMultiple && rightUnit is Watt -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is HorsepowerHour && rightUnit is Horsepower -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is FootPoundForce && rightUnit is FootPoundForcePerMinute -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is InchPoundForce && rightUnit is InchPoundForcePerMinute -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is BritishThermalUnit && rightUnit is BritishThermalUnitPerHour -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is BritishThermalUnit && rightUnit is BritishThermalUnitPerMinute -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is Power -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Voltage from Electric Charge", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.ElectricCharge) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is Abcoulomb -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ErgMultiple && rightUnit is Abcoulomb -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is ElectricCharge -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Volume from Pressure", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.Pressure) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is Barye -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Erg && rightUnit is BaryeMultiple -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ErgMultiple && rightUnit is Barye -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ErgMultiple && rightUnit is BaryeMultiple -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is FootPoundal && rightUnit is PoundSquareFoot -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is InchPoundForce && rightUnit is PoundSquareInch -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is InchOunceForce && rightUnit is PoundSquareInch -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is ImperialPressure -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is UKImperialPressure -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is USCustomaryPressure -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is ImperialPressure -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is UKImperialPressure -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is USCustomaryPressure -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is Pressure -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Weight from Ionizing Radiation Absorbed Dose", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.IonizingRadiationAbsorbedDose) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is Rad -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Erg && rightUnit is RadMultiple -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ErgMultiple && rightUnit is Rad -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ErgMultiple && rightUnit is RadMultiple -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is IonizingRadiationAbsorbedDose -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Weight from Ionizing Radiation Equivalent Dose", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.IonizingRadiationEquivalentDose) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Erg && rightUnit is RoentgenEquivalentMan -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Erg && rightUnit is RoentgenEquivalentManMultiple -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ErgMultiple && rightUnit is RoentgenEquivalentMan -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ErgMultiple && rightUnit is RoentgenEquivalentManMultiple -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is IonizingRadiationEquivalentDose -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator("Weight from Specific Energy", QuantityConverter.WithOperator.Type.Division, PhysicalQuantity.SpecificEnergy) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricAndImperialEnergy && rightUnit is MetricSpecificEnergy -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is ImperialSpecificEnergy -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is UKImperialSpecificEnergy -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricAndImperialEnergy && rightUnit is USCustomarySpecificEnergy -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricEnergy && rightUnit is MetricSpecificEnergy -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is ImperialSpecificEnergy -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is UKImperialSpecificEnergy -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialEnergy && rightUnit is USCustomarySpecificEnergy -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Energy && rightUnit is SpecificEnergy -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    }
)
