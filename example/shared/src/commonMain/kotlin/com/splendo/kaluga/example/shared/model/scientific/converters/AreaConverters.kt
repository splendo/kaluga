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
import com.splendo.kaluga.scientific.converter.area.div
import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.unit.*

val PhysicalQuantity.Area.converters get() = listOf<QuantityConverter<PhysicalQuantity.Area, *>>(
    QuantityConverterWithOperator(
        "Energy from Surface Tension",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.SurfaceTension,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is SquareCentimeter && rightUnit is MetricSurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is MetricArea && rightUnit is MetricSurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareInch && rightUnit is ImperialSurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareInch && rightUnit is UKImperialSurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareInch && rightUnit is USCustomarySurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is ImperialSurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is UKImperialSurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is USCustomarySurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Area && rightUnit is SurfaceTension -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Force from Pressure",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Pressure,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is SquareCentimeter && rightUnit is Barye -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareCentimeter && rightUnit is BaryeMultiple -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is OunceSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is KipSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is KipSquareFoot -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is USTonSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is USTonSquareFoot -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is ImperialTonSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is ImperialTonSquareFoot -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is ImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is ImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is ImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Area && rightUnit is Pressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Length from Length",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Length,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is SquareMeter && rightUnit is Meter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareNanometer && rightUnit is Nanometer -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareMicrometer && rightUnit is Micrometer -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareMillimeter && rightUnit is Millimeter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareCentimeter && rightUnit is Centimeter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareDecimeter && rightUnit is Decimeter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareDecameter && rightUnit is Decameter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareHectometer && rightUnit is Hectometer -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareKilometer && rightUnit is Kilometer -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareMegameter && rightUnit is Megameter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareGigameter && rightUnit is Gigameter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is MetricArea && rightUnit is MetricLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareInch && rightUnit is Inch -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareFoot && rightUnit is Foot -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareYard && rightUnit is Yard -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareMile && rightUnit is Mile -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is ImperialLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Area && rightUnit is Length -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Linear Mass Density from Density",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Density,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricArea && rightUnit is MetricDensity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is ImperialDensity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is ImperialDensity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is UKImperialDensity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is USCustomaryDensity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Area && rightUnit is Density -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Linear Mass Density from Specific Volume",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.SpecificVolume,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricArea && rightUnit is MetricSpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is ImperialSpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is UKImperialSpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is USCustomarySpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Area && rightUnit is SpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Luminous Energy from Luminous Exposure",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.LuminousExposure,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricArea && rightUnit is MetricLuminousExposure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is ImperialLuminousExposure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Area && rightUnit is LuminousExposure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Luminous Flux from Illuminance",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Illuminance,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Area && rightUnit is Illuminance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Luminous Intensity from Luminance",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Luminance,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Area && rightUnit is Luminance -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Magnetic Flux from Magnetic Induction",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.MagneticInduction,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is SquareCentimeter && rightUnit is Gauss -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Area && rightUnit is MagneticInduction -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Momentum from Dynamic Viscosity",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.DynamicViscosity,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricArea && rightUnit is MetricDynamicViscosity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is ImperialDynamicViscosity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is UKImperialDynamicViscosity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is USCustomaryDynamicViscosity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Area && rightUnit is DynamicViscosity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Specific Volume from Linear Mass Density",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.LinearMassDensity,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricArea && rightUnit is MetricLinearMassDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is ImperialLinearMassDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is UKImperialLinearMassDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is USCustomaryLinearMassDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Area && rightUnit is LinearMassDensity -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Volume from Length",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Length,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is SquareMeter && rightUnit is Meter -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareNanometer && rightUnit is Nanometer -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareMicrometer && rightUnit is Micrometer -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareMillimeter && rightUnit is Millimeter -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareCentimeter && rightUnit is Centimeter -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareDecimeter && rightUnit is Decimeter -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareDecameter && rightUnit is Decameter -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareHectometer && rightUnit is Hectometer -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareKilometer && rightUnit is Kilometer -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareMegameter && rightUnit is Megameter -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareGigameter && rightUnit is Gigameter -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is MetricArea && rightUnit is MetricLength -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareInch && rightUnit is Inch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareFoot && rightUnit is Foot -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareYard && rightUnit is Yard -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is SquareMile && rightUnit is Mile -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Acre && rightUnit is Inch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Acre && rightUnit is Foot -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is ImperialLength -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Area && rightUnit is Length -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Volumetric Flow from Volumetric Flux",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.VolumetricFlux,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricArea && rightUnit is MetricVolumetricFlux -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is ImperialVolumetricFlux -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is UKImperialVolumetricFlux -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is USCustomaryVolumetricFlux -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Area && rightUnit is VolumetricFlux -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Weight from Area Density",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.AreaDensity,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricArea && rightUnit is MetricAreaDensity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is ImperialAreaDensity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is UKImperialAreaDensity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialArea && rightUnit is USCustomaryAreaDensity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Area && rightUnit is AreaDensity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
)
