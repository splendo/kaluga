/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.converter.area

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.length.width
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.Decameter
import com.splendo.kaluga.scientific.unit.Decimeter
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.Gigameter
import com.splendo.kaluga.scientific.unit.Hectometer
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.Kilometer
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.Megameter
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.Micrometer
import com.splendo.kaluga.scientific.unit.Mile
import com.splendo.kaluga.scientific.unit.Millimeter
import com.splendo.kaluga.scientific.unit.Nanometer
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
import com.splendo.kaluga.scientific.unit.Yard
import kotlin.jvm.JvmName

@JvmName("squareMeterDivMeter")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareMeter>.div(length: ScientificValue<PhysicalQuantity.Length, Meter>) = Meter.width(this, length)

@JvmName("squareNanometerDivNanometer")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareNanometer>.div(length: ScientificValue<PhysicalQuantity.Length, Nanometer>) = Nanometer.width(this, length)

@JvmName("squareMicrometerDivMicrometer")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareMicrometer>.div(length: ScientificValue<PhysicalQuantity.Length, Micrometer>) = Micrometer.width(this, length)

@JvmName("squareMillimeterDivMillimeter")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareMillimeter>.div(length: ScientificValue<PhysicalQuantity.Length, Millimeter>) = Millimeter.width(this, length)

@JvmName("squareCentimeterDivCentimeter")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareCentimeter>.div(length: ScientificValue<PhysicalQuantity.Length, Centimeter>) = Centimeter.width(this, length)

@JvmName("squareDecimeterDivDecimeter")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareDecimeter>.div(length: ScientificValue<PhysicalQuantity.Length, Decimeter>) = Decimeter.width(this, length)

@JvmName("squareDecameterDivDecameter")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareDecameter>.div(length: ScientificValue<PhysicalQuantity.Length, Decameter>) = Decameter.width(this, length)

@JvmName("squareHectometerDivHectometer")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareHectometer>.div(length: ScientificValue<PhysicalQuantity.Length, Hectometer>) = Hectometer.width(this, length)

@JvmName("squareKilometerDivKilometer")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareKilometer>.div(length: ScientificValue<PhysicalQuantity.Length, Kilometer>) = Kilometer.width(this, length)

@JvmName("squareMegameterDivMegameter")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareMegameter>.div(length: ScientificValue<PhysicalQuantity.Length, Megameter>) = Megameter.width(this, length)

@JvmName("squareGigameterDivGigameter")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareGigameter>.div(length: ScientificValue<PhysicalQuantity.Length, Gigameter>) = Gigameter.width(this, length)

@JvmName("metricAreaDivMetricLength")
infix operator fun <AreaUnit : MetricArea, LengthUnit : MetricLength> ScientificValue<PhysicalQuantity.Area, AreaUnit>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = Meter.width(this, length)

@JvmName("squareInchDivInch")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareInch>.div(length: ScientificValue<PhysicalQuantity.Length, Inch>) = Inch.width(this, length)

@JvmName("squareFootDivFoot")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareFoot>.div(length: ScientificValue<PhysicalQuantity.Length, Foot>) = Foot.width(this, length)

@JvmName("squareYardDivYard")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareYard>.div(length: ScientificValue<PhysicalQuantity.Length, Yard>) = Yard.width(this, length)

@JvmName("squareMileDivMile")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareMile>.div(length: ScientificValue<PhysicalQuantity.Length, Mile>) = Mile.width(this, length)

@JvmName("imperialAreaDivImperialLength")
infix operator fun <AreaUnit : ImperialArea, LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Area, AreaUnit>.div(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
) = Foot.width(this, length)

@JvmName("areaDivLength")
infix operator fun <AreaUnit : Area, LengthUnit : Length> ScientificValue<PhysicalQuantity.Area, AreaUnit>.div(length: ScientificValue<PhysicalQuantity.Length, LengthUnit>) =
    Meter.width(this, length)
