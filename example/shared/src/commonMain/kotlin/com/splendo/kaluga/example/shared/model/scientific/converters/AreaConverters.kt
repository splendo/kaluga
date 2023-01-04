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

val PhysicalQuantity.Area.converters get() = listOf<QuantityConverter<PhysicalQuantity.Area, *, *>>(
    QuantityConverter("Energy from Surface Tension", QuantityConverter.Type.Multiplication, SurfaceTensionUnits) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is SquareCentimeter && rightUnit is MetricSurfaceTension -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricArea && rightUnit is MetricSurfaceTension -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SquareInch && rightUnit is ImperialSurfaceTension -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SquareInch && rightUnit is UKImperialSurfaceTension -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SquareInch && rightUnit is USCustomarySurfaceTension -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialArea && rightUnit is ImperialSurfaceTension -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialArea && rightUnit is UKImperialSurfaceTension -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialArea && rightUnit is USCustomarySurfaceTension -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Area && rightUnit is SurfaceTension -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
    QuantityConverter("Force from Pressure", QuantityConverter.Type.Multiplication, PressureUnits) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is SquareCentimeter && rightUnit is Barye -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialArea && rightUnit is OunceSquareInch -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialArea && rightUnit is KipSquareInch -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialArea && rightUnit is KipSquareFoot -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialArea && rightUnit is USTonSquareInch -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialArea && rightUnit is USTonSquareFoot -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialArea && rightUnit is ImperialTonSquareInch -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialArea && rightUnit is ImperialTonSquareFoot -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialArea && rightUnit is ImperialPressure -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialArea && rightUnit is ImperialPressure -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialArea && rightUnit is ImperialPressure -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Area && rightUnit is Pressure -> DefaultScientificValue(leftValue, leftUnit) * DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
    QuantityConverter("Length from Length", QuantityConverter.Type.Division, LengthUnits) { (leftValue, leftUnit), (rightValue, rightUnit) ->
        when {
            leftUnit is SquareMeter && rightUnit is Meter -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SquareNanometer && rightUnit is Nanometer -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SquareMicrometer && rightUnit is Micrometer -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SquareMillimeter && rightUnit is Millimeter -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SquareCentimeter && rightUnit is Centimeter -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SquareDecimeter && rightUnit is Decimeter -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SquareDecameter && rightUnit is Decameter -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SquareHectometer && rightUnit is Hectometer -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SquareKilometer && rightUnit is Kilometer -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SquareMegameter && rightUnit is Megameter -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SquareGigameter && rightUnit is Gigameter -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is MetricArea && rightUnit is MetricLength -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SquareInch && rightUnit is Inch -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SquareFoot && rightUnit is Foot -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SquareYard && rightUnit is Yard -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is SquareMile && rightUnit is Mile -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is ImperialArea && rightUnit is ImperialLength -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            leftUnit is Area && rightUnit is Length -> DefaultScientificValue(leftValue, leftUnit) / DefaultScientificValue(rightValue, rightUnit)
            else -> throw RuntimeException("Unexpected units: $leftUnit $rightUnit")
        }
    },
)