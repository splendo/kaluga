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
import com.splendo.kaluga.scientific.converter.time.div
import com.splendo.kaluga.scientific.converter.time.frequency
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.unit.*

val PhysicalQuantity.Time.converters get() = listOf<QuantityConverter<PhysicalQuantity.Time, *>>(
    QuantityConverterWithOperator(
        "Acceleration from Jolt",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Jolt,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Time && rightUnit is MetricJolt -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is ImperialJolt -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is Jolt -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Action from Energy",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Energy,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Time && rightUnit is MetricAndImperialEnergy -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is MetricEnergy -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is ImperialEnergy -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is Energy -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Amount of Substance from Catalystic Activity",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.CatalysticActivity,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Time && rightUnit is CatalysticActivity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Angle from Angular Velocity",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.AngularVelocity,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Time && rightUnit is AngularVelocity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Angular Velocity from Angular Acceleration",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.AngularAcceleration,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Time && rightUnit is AngularAcceleration -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Dynamic Viscosity from Pressure",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Pressure,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Time && rightUnit is MetricPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is ImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is UKImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is USCustomaryPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is Pressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Electric Capacitance from Electric Resistance",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.ElectricResistance,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Time && rightUnit is Abohm -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is ElectricResistance -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Electric Charge from Electric Current",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.ElectricCurrent,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Time && rightUnit is Abampere -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is Biot -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is ElectricCurrent -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Electric Inductance from Electric Resistance",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.ElectricResistance,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Time && rightUnit is Abohm -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is ElectricResistance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Electric Resistance from Electric Capacitance",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.ElectricCapacitance,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Time && rightUnit is Abfarad -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is ElectricCapacitance -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Energy from Power",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Power,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Hour && rightUnit is Watt -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Hour && rightUnit is Nanowatt -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Hour && rightUnit is Microwatt -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Hour && rightUnit is Milliwatt -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Hour && rightUnit is Centiwatt -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Hour && rightUnit is Deciwatt -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Hour && rightUnit is Decawatt -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Hour && rightUnit is Hectowatt -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Hour && rightUnit is Kilowatt -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Hour && rightUnit is Megawatt -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Hour && rightUnit is Gigawatt -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is ErgPerSecond -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is MetricPower -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is InchPoundForcePerSecond -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is InchPoundForcePerMinute -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Hour && rightUnit is Horsepower -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is BritishThermalUnitPerSecond -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is BritishThermalUnitPerMinute -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is BritishThermalUnitPerHour -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is ImperialPower -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is Power -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Force from Yank",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Yank,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Time && rightUnit is MetricYank -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is ImperialYank -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is UKImperialYank -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is USCustomaryYank -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is Yank -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    SingleQuantityConverter("Frequency") { value, unit ->
        when (unit) {
            is Minute -> DefaultScientificValue(value, unit).frequency()
            is Time -> DefaultScientificValue(value, unit).frequency()
            else -> throw RuntimeException("Unexpected unit: $unit")
        }
    },
    QuantityConverterWithOperator(
        "Kinematic Viscosity from Specific Energy",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.SpecificEnergy,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Time && rightUnit is MetricSpecificEnergy -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is ImperialSpecificEnergy -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is UKImperialSpecificEnergy -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is USCustomarySpecificEnergy -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is SpecificEnergy -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Length from Speed",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Speed,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Time && rightUnit is MetricSpeed -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is ImperialSpeed -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is Speed -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Luminous Energy from Luminous Flux",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.LuminousFlux,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Time && rightUnit is LuminousFlux -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Luminous Exposure from Illuminance",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Illuminance,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Time && rightUnit is MetricIlluminance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is ImperialIlluminance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is Illuminance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Magnetic Flux from Voltage",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Voltage,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Time && rightUnit is Abvolt -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is Voltage -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Momentum from Force",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Force,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Time && rightUnit is Dyne -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is DyneMultiple -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is TonneForce -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is GramForce -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is MilligramForce -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is MetricForce -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is Poundal -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is OunceForce -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is GrainForce -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is UsTonForce -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is ImperialTonForce -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is ImperialForce -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is UKImperialForce -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is USCustomaryForce -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is Force -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Speed from Acceleration",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Acceleration,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Time && rightUnit is MetricAcceleration -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is ImperialAcceleration -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is Acceleration -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Volume from Volumetric Flow",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.VolumetricFlow,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Time && rightUnit is MetricVolumetricFlow -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is ImperialVolumetricFlow -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is UKImperialVolumetricFlow -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is USCustomaryVolumetricFlow -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is VolumetricFlow -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Weight from Mass Flow Rate",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.MassFlowRate,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Time && rightUnit is MetricMassFlowRate -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is ImperialMassFlowRate -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is UKImperialMassFlowRate -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is USCustomaryMassFlowRate -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Time && rightUnit is MassFlowRate -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
)
