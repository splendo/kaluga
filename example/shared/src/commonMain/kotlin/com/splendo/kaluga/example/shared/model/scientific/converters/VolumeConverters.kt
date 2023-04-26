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
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.converter.volume.times
import com.splendo.kaluga.scientific.unit.*

val PhysicalQuantity.Volume.converters get() = listOf<QuantityConverter<PhysicalQuantity.Volume, *>>(
    QuantityConverterWithOperator(
        "Amount of Substance from Molarity",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Molarity,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Volume && rightUnit is Molarity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Amount of Substance from Molar Volume",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.MolarVolume,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is Volume && rightUnit is MolarVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Area from Length",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Length,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is CubicMeter && rightUnit is Meter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicNanometer && rightUnit is Nanometer -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicMicrometer && rightUnit is Micrometer -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicMillimeter && rightUnit is Millimeter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicCentimeter && rightUnit is Centimeter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicDecimeter && rightUnit is Decimeter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicDecameter && rightUnit is Decameter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicHectometer && rightUnit is Hectometer -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicKilometer && rightUnit is Kilometer -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicMegameter && rightUnit is Megameter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicGigameter && rightUnit is Gigameter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is MetricVolume && rightUnit is MetricLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicInch && rightUnit is Inch -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicFoot && rightUnit is Foot -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicYard && rightUnit is Yard -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicMile && rightUnit is Mile -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is AcreInch && rightUnit is Inch -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is AcreFoot && rightUnit is Foot -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialVolume && rightUnit is ImperialLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialVolume && rightUnit is ImperialLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryVolume && rightUnit is ImperialLength -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Volume && rightUnit is Length -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Energy from Pressure",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Pressure,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is CubicCentimeter && rightUnit is Barye -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicCentimeter && rightUnit is BaryeMultiple -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicFoot && rightUnit is PoundSquareFoot -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicInch && rightUnit is PoundSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicInch && rightUnit is OunceSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicInch && rightUnit is KiloPoundSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicInch && rightUnit is KipSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicInch && rightUnit is USTonSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicInch && rightUnit is ImperialTonSquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialVolume && rightUnit is ImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialVolume && rightUnit is UKImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialVolume && rightUnit is USCustomaryPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialVolume && rightUnit is ImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialVolume && rightUnit is UKImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryVolume && rightUnit is ImperialPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryVolume && rightUnit is USCustomaryPressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Volume && rightUnit is Pressure -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Length from Area",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Area,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is CubicMeter && rightUnit is SquareMeter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicNanometer && rightUnit is SquareNanometer -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicMicrometer && rightUnit is SquareMicrometer -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicMillimeter && rightUnit is SquareMillimeter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicCentimeter && rightUnit is SquareCentimeter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicDecimeter && rightUnit is SquareDecimeter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicDecameter && rightUnit is SquareDecameter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicHectometer && rightUnit is SquareHectometer -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicKilometer && rightUnit is SquareKilometer -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicMegameter && rightUnit is SquareMegameter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicGigameter && rightUnit is SquareGigameter -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is MetricVolume && rightUnit is MetricArea -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicInch && rightUnit is SquareInch -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicFoot && rightUnit is SquareFoot -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicYard && rightUnit is SquareYard -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is CubicMile && rightUnit is SquareMile -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is AcreInch && rightUnit is Acre -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is AcreFoot && rightUnit is Acre -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialVolume && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialVolume && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryVolume && rightUnit is ImperialArea -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Volume && rightUnit is Area -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Molar Volume from Amount of Substance",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.AmountOfSubstance,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricVolume && rightUnit is AmountOfSubstance -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialVolume && rightUnit is AmountOfSubstance -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialVolume && rightUnit is AmountOfSubstance -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryVolume && rightUnit is AmountOfSubstance -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Volume && rightUnit is AmountOfSubstance -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Specific Volume from Weight",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Weight,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricVolume && rightUnit is MetricWeight -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialVolume && rightUnit is ImperialWeight -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialVolume && rightUnit is UKImperialWeight -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialVolume && rightUnit is USCustomaryWeight -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialVolume && rightUnit is ImperialWeight -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialVolume && rightUnit is UKImperialWeight -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryVolume && rightUnit is ImperialWeight -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryVolume && rightUnit is USCustomaryWeight -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Volume && rightUnit is Weight -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Volumetric Flow from Time",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.Time,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricVolume && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialVolume && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialVolume && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryVolume && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Volume && rightUnit is Time -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Weight from Density",
        QuantityConverter.WithOperator.Type.Multiplication,
        PhysicalQuantity.Density,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricVolume && rightUnit is MetricDensity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialVolume && rightUnit is ImperialDensity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialVolume && rightUnit is UKImperialDensity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialVolume && rightUnit is USCustomaryDensity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialVolume && rightUnit is ImperialDensity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialVolume && rightUnit is UKImperialDensity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryVolume && rightUnit is ImperialDensity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryVolume && rightUnit is USCustomaryDensity -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Volume && rightUnit is Density -> {
                DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
    QuantityConverterWithOperator(
        "Weight from Specific Volume",
        QuantityConverter.WithOperator.Type.Division,
        PhysicalQuantity.SpecificVolume,
    ) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is MetricVolume && rightUnit is MetricSpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialVolume && rightUnit is ImperialSpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialVolume && rightUnit is UKImperialSpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is ImperialVolume && rightUnit is USCustomarySpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialVolume && rightUnit is ImperialSpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is UKImperialVolume && rightUnit is UKImperialSpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryVolume && rightUnit is ImperialSpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is USCustomaryVolume && rightUnit is USCustomarySpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            leftUnit is Volume && rightUnit is SpecificVolume -> {
                DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            }
            else -> throw RuntimeException("Unexpected units: $leftUnit, $rightUnit")
        }
    },
)
