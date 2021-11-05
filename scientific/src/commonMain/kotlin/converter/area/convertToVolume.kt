/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands
 
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

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.volume.volume
import com.splendo.kaluga.scientific.unit.Acre
import com.splendo.kaluga.scientific.unit.AcreFoot
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
infix operator fun ScientificValue<MeasurementType.Area, SquareMeter>.times(height: ScientificValue<MeasurementType.Length, Meter>) = CubicMeter.volume(this, height)
@JvmName("squareNanometerTimesNanometer")
infix operator fun ScientificValue<MeasurementType.Area, SquareNanometer>.times(height: ScientificValue<MeasurementType.Length, Nanometer>) = CubicNanometer.volume(this, height)
@JvmName("squareMicrometerTimesMicrometer")
infix operator fun ScientificValue<MeasurementType.Area, SquareMicrometer>.times(height: ScientificValue<MeasurementType.Length, Micrometer>) = CubicMillimeter.volume(this, height)
@JvmName("squareMillimeterTimesMillimeter")
infix operator fun ScientificValue<MeasurementType.Area, SquareMillimeter>.times(height: ScientificValue<MeasurementType.Length, Millimeter>) = CubicMillimeter.volume(this, height)
@JvmName("squareCentimeterTimesCentimeter")
infix operator fun ScientificValue<MeasurementType.Area, SquareCentimeter>.times(height: ScientificValue<MeasurementType.Length, Centimeter>) = CubicCentimeter.volume(this, height)
@JvmName("squareDecimeterTimesDecimeter")
infix operator fun ScientificValue<MeasurementType.Area, SquareDecimeter>.times(height: ScientificValue<MeasurementType.Length, Decimeter>) = CubicDecimeter.volume(this, height)
@JvmName("squareDecameterTimesDecameter")
infix operator fun ScientificValue<MeasurementType.Area, SquareDecameter>.times(height: ScientificValue<MeasurementType.Length, Decameter>) = CubicDecameter.volume(this, height)
@JvmName("squareHectometerTimesHectometer")
infix operator fun ScientificValue<MeasurementType.Area, SquareHectometer>.times(height: ScientificValue<MeasurementType.Length, Hectometer>) = CubicHectometer.volume(this, height)
@JvmName("squareKilometerTimesKilometer")
infix operator fun ScientificValue<MeasurementType.Area, SquareKilometer>.times(height: ScientificValue<MeasurementType.Length, Kilometer>) = CubicKilometer.volume(this, height)
@JvmName("squareMegameterTimesMegameter")
infix operator fun ScientificValue<MeasurementType.Area, SquareMegameter>.times(height: ScientificValue<MeasurementType.Length, Megameter>) = CubicMegameter.volume(this, height)
@JvmName("squareGigameterTimesGigameter")
infix operator fun ScientificValue<MeasurementType.Area, SquareGigameter>.times(height: ScientificValue<MeasurementType.Length, Gigameter>) = CubicGigameter.volume(this, height)
@JvmName("metricAreaTimesMetricLength")
infix operator fun <AreaUnit : MetricArea, HeightUnit : MetricLength> ScientificValue<MeasurementType.Area, AreaUnit>.times(height: ScientificValue<MeasurementType.Length, HeightUnit>) = CubicMeter.volume(this, height)
@JvmName("squareInchTimesInch")
infix operator fun ScientificValue<MeasurementType.Area, SquareInch>.times(height: ScientificValue<MeasurementType.Length, Inch>) = CubicInch.volume(this, height)
@JvmName("squareFootTimesFoot")
infix operator fun ScientificValue<MeasurementType.Area, SquareFoot>.times(height: ScientificValue<MeasurementType.Length, Foot>) = CubicFoot.volume(this, height)
@JvmName("squareYardTimesYard")
infix operator fun ScientificValue<MeasurementType.Area, SquareYard>.times(height: ScientificValue<MeasurementType.Length, Yard>) = CubicYard.volume(this, height)
@JvmName("squareMileTimesMile")
infix operator fun ScientificValue<MeasurementType.Area, SquareMile>.times(height: ScientificValue<MeasurementType.Length, Mile>) = CubicMile.volume(this, height)
@JvmName("acreTimesFoot")
infix operator fun ScientificValue<MeasurementType.Area, Acre>.times(height: ScientificValue<MeasurementType.Length, Foot>) = AcreFoot.volume(this, height)
@JvmName("imperialAreaTimesImperialLength")
infix operator fun <AreaUnit : ImperialArea, HeightUnit : ImperialLength> ScientificValue<MeasurementType.Area, AreaUnit>.times(height: ScientificValue<MeasurementType.Length, HeightUnit>) = CubicFoot.volume(this, height)
@JvmName("areaTimesLength")
infix operator fun <AreaUnit : Area, HeightUnit : Length> ScientificValue<MeasurementType.Area, AreaUnit>.times(height: ScientificValue<MeasurementType.Length, HeightUnit>) = CubicMeter.volume(this, height)
