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
import com.splendo.kaluga.scientific.converter.area.div
import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.unit.Acre
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.AreaDensity
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.BaryeMultiple
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Decameter
import com.splendo.kaluga.scientific.unit.Decimeter
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.DynamicViscosity
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Gauss
import com.splendo.kaluga.scientific.unit.Gigameter
import com.splendo.kaluga.scientific.unit.Hectometer
import com.splendo.kaluga.scientific.unit.Illuminance
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialAreaDensity
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialDynamicViscosity
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.ImperialLinearMassDensity
import com.splendo.kaluga.scientific.unit.ImperialLuminousExposure
import com.splendo.kaluga.scientific.unit.ImperialPressure
import com.splendo.kaluga.scientific.unit.ImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.ImperialSurfaceTension
import com.splendo.kaluga.scientific.unit.ImperialTonSquareFoot
import com.splendo.kaluga.scientific.unit.ImperialTonSquareInch
import com.splendo.kaluga.scientific.unit.ImperialVolumetricFlux
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.Kilometer
import com.splendo.kaluga.scientific.unit.KipSquareFoot
import com.splendo.kaluga.scientific.unit.KipSquareInch
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.LinearMassDensity
import com.splendo.kaluga.scientific.unit.Luminance
import com.splendo.kaluga.scientific.unit.LuminousExposure
import com.splendo.kaluga.scientific.unit.MagneticInduction
import com.splendo.kaluga.scientific.unit.Megameter
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricAreaDensity
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricDynamicViscosity
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.MetricLinearMassDensity
import com.splendo.kaluga.scientific.unit.MetricLuminousExposure
import com.splendo.kaluga.scientific.unit.MetricSpecificVolume
import com.splendo.kaluga.scientific.unit.MetricSurfaceTension
import com.splendo.kaluga.scientific.unit.MetricVolumetricFlux
import com.splendo.kaluga.scientific.unit.Micrometer
import com.splendo.kaluga.scientific.unit.Mile
import com.splendo.kaluga.scientific.unit.Millimeter
import com.splendo.kaluga.scientific.unit.Nanometer
import com.splendo.kaluga.scientific.unit.OunceSquareInch
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.SquareDecameter
import com.splendo.kaluga.scientific.unit.SquareDecimeter
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareGigameter
import com.splendo.kaluga.scientific.unit.SquareHectometer
import com.splendo.kaluga.scientific.unit.SquareInch
import com.splendo.kaluga.scientific.unit.SquareKilometer
import com.splendo.kaluga.scientific.unit.SquareMegameter
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.SquareMicrometer
import com.splendo.kaluga.scientific.unit.SquareMile
import com.splendo.kaluga.scientific.unit.SquareMillimeter
import com.splendo.kaluga.scientific.unit.SquareNanometer
import com.splendo.kaluga.scientific.unit.SquareYard
import com.splendo.kaluga.scientific.unit.SurfaceTension
import com.splendo.kaluga.scientific.unit.UKImperialAreaDensity
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.UKImperialDynamicViscosity
import com.splendo.kaluga.scientific.unit.UKImperialLinearMassDensity
import com.splendo.kaluga.scientific.unit.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialSurfaceTension
import com.splendo.kaluga.scientific.unit.UKImperialVolumetricFlux
import com.splendo.kaluga.scientific.unit.USCustomaryAreaDensity
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.USCustomaryDynamicViscosity
import com.splendo.kaluga.scientific.unit.USCustomaryLinearMassDensity
import com.splendo.kaluga.scientific.unit.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.unit.USCustomarySurfaceTension
import com.splendo.kaluga.scientific.unit.USCustomaryVolumetricFlux
import com.splendo.kaluga.scientific.unit.USTonSquareFoot
import com.splendo.kaluga.scientific.unit.USTonSquareInch
import com.splendo.kaluga.scientific.unit.VolumetricFlux
import com.splendo.kaluga.scientific.unit.Yard

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
