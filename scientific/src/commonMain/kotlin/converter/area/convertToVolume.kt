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
import com.splendo.kaluga.scientific.converter.volume.volume
import com.splendo.kaluga.scientific.unit.Acre
import com.splendo.kaluga.scientific.unit.AcreFoot
import com.splendo.kaluga.scientific.unit.AcreInch
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.CubicCentimeter
import com.splendo.kaluga.scientific.unit.CubicDecameter
import com.splendo.kaluga.scientific.unit.CubicDecimeter
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicGigameter
import com.splendo.kaluga.scientific.unit.CubicHectometer
import com.splendo.kaluga.scientific.unit.CubicInch
import com.splendo.kaluga.scientific.unit.CubicKilometer
import com.splendo.kaluga.scientific.unit.CubicMegameter
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.CubicMicrometer
import com.splendo.kaluga.scientific.unit.CubicMile
import com.splendo.kaluga.scientific.unit.CubicMillimeter
import com.splendo.kaluga.scientific.unit.CubicNanometer
import com.splendo.kaluga.scientific.unit.CubicYard
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

@JvmName("squareMeterTimesMeter")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareMeter>.times(height: ScientificValue<PhysicalQuantity.Length, Meter>) = CubicMeter.volume(this, height)

@JvmName("squareNanometerTimesNanometer")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareNanometer>.times(height: ScientificValue<PhysicalQuantity.Length, Nanometer>) = CubicNanometer.volume(this, height)

@JvmName("squareMicrometerTimesMicrometer")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareMicrometer>.times(height: ScientificValue<PhysicalQuantity.Length, Micrometer>) =
    CubicMicrometer.volume(this, height)

@JvmName("squareMillimeterTimesMillimeter")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareMillimeter>.times(height: ScientificValue<PhysicalQuantity.Length, Millimeter>) =
    CubicMillimeter.volume(this, height)

@JvmName("squareCentimeterTimesCentimeter")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareCentimeter>.times(height: ScientificValue<PhysicalQuantity.Length, Centimeter>) =
    CubicCentimeter.volume(this, height)

@JvmName("squareDecimeterTimesDecimeter")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareDecimeter>.times(height: ScientificValue<PhysicalQuantity.Length, Decimeter>) = CubicDecimeter.volume(this, height)

@JvmName("squareDecameterTimesDecameter")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareDecameter>.times(height: ScientificValue<PhysicalQuantity.Length, Decameter>) = CubicDecameter.volume(this, height)

@JvmName("squareHectometerTimesHectometer")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareHectometer>.times(height: ScientificValue<PhysicalQuantity.Length, Hectometer>) =
    CubicHectometer.volume(this, height)

@JvmName("squareKilometerTimesKilometer")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareKilometer>.times(height: ScientificValue<PhysicalQuantity.Length, Kilometer>) = CubicKilometer.volume(this, height)

@JvmName("squareMegameterTimesMegameter")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareMegameter>.times(height: ScientificValue<PhysicalQuantity.Length, Megameter>) = CubicMegameter.volume(this, height)

@JvmName("squareGigameterTimesGigameter")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareGigameter>.times(height: ScientificValue<PhysicalQuantity.Length, Gigameter>) = CubicGigameter.volume(this, height)

@JvmName("metricAreaTimesMetricLength")
infix operator fun <AreaUnit : MetricArea, HeightUnit : MetricLength> ScientificValue<PhysicalQuantity.Area, AreaUnit>.times(
    height: ScientificValue<PhysicalQuantity.Length, HeightUnit>,
) = CubicMeter.volume(this, height)

@JvmName("squareInchTimesInch")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareInch>.times(height: ScientificValue<PhysicalQuantity.Length, Inch>) = CubicInch.volume(this, height)

@JvmName("squareFootTimesFoot")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareFoot>.times(height: ScientificValue<PhysicalQuantity.Length, Foot>) = CubicFoot.volume(this, height)

@JvmName("squareYardTimesYard")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareYard>.times(height: ScientificValue<PhysicalQuantity.Length, Yard>) = CubicYard.volume(this, height)

@JvmName("squareMileTimesMile")
infix operator fun ScientificValue<PhysicalQuantity.Area, SquareMile>.times(height: ScientificValue<PhysicalQuantity.Length, Mile>) = CubicMile.volume(this, height)

@JvmName("acreTimesInch")
infix operator fun ScientificValue<PhysicalQuantity.Area, Acre>.times(height: ScientificValue<PhysicalQuantity.Length, Inch>) = AcreInch.volume(this, height)

@JvmName("acreTimesFoot")
infix operator fun ScientificValue<PhysicalQuantity.Area, Acre>.times(height: ScientificValue<PhysicalQuantity.Length, Foot>) = AcreFoot.volume(this, height)

@JvmName("imperialAreaTimesImperialLength")
infix operator fun <AreaUnit : ImperialArea, HeightUnit : ImperialLength> ScientificValue<PhysicalQuantity.Area, AreaUnit>.times(
    height: ScientificValue<PhysicalQuantity.Length, HeightUnit>,
) = CubicFoot.volume(this, height)

@JvmName("areaTimesLength")
infix operator fun <AreaUnit : Area, HeightUnit : Length> ScientificValue<PhysicalQuantity.Area, AreaUnit>.times(height: ScientificValue<PhysicalQuantity.Length, HeightUnit>) =
    CubicMeter.volume(this, height)
