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

package com.splendo.kaluga.scientific.converter.length

import com.splendo.kaluga.scientific.Acre
import com.splendo.kaluga.scientific.Area
import com.splendo.kaluga.scientific.Centimeter
import com.splendo.kaluga.scientific.Decameter
import com.splendo.kaluga.scientific.Decimeter
import com.splendo.kaluga.scientific.Foot
import com.splendo.kaluga.scientific.Gigameter
import com.splendo.kaluga.scientific.Hectometer
import com.splendo.kaluga.scientific.ImperialArea
import com.splendo.kaluga.scientific.ImperialLength
import com.splendo.kaluga.scientific.Inch
import com.splendo.kaluga.scientific.Kilometer
import com.splendo.kaluga.scientific.Length
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Megameter
import com.splendo.kaluga.scientific.Meter
import com.splendo.kaluga.scientific.MetricArea
import com.splendo.kaluga.scientific.MetricLength
import com.splendo.kaluga.scientific.Micrometer
import com.splendo.kaluga.scientific.Mile
import com.splendo.kaluga.scientific.Millimeter
import com.splendo.kaluga.scientific.Nanometer
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SquareCentimeter
import com.splendo.kaluga.scientific.SquareDecameter
import com.splendo.kaluga.scientific.SquareDecimeter
import com.splendo.kaluga.scientific.SquareFoot
import com.splendo.kaluga.scientific.SquareGigameter
import com.splendo.kaluga.scientific.SquareHectometer
import com.splendo.kaluga.scientific.SquareInch
import com.splendo.kaluga.scientific.SquareKilometer
import com.splendo.kaluga.scientific.SquareMegameter
import com.splendo.kaluga.scientific.SquareMeter
import com.splendo.kaluga.scientific.SquareMicrometer
import com.splendo.kaluga.scientific.SquareMile
import com.splendo.kaluga.scientific.SquareMillimeter
import com.splendo.kaluga.scientific.SquareNanometer
import com.splendo.kaluga.scientific.SquareYard
import com.splendo.kaluga.scientific.Yard
import com.splendo.kaluga.scientific.converter.area.times
import kotlin.jvm.JvmName

@JvmName("meterTimesSquareMeter")
infix operator fun ScientificValue<MeasurementType.Length, Meter>.times(area: ScientificValue<MeasurementType.Area, SquareMeter>) = area * this
@JvmName("nanometerTimesSquareNanometer")
infix operator fun ScientificValue<MeasurementType.Length, Nanometer>.times(area: ScientificValue<MeasurementType.Area, SquareNanometer>) = area * this
@JvmName("micrometerTimesSquareMicrometer")
infix operator fun ScientificValue<MeasurementType.Length, Micrometer>.times(area: ScientificValue<MeasurementType.Area, SquareMicrometer>) = area * this
@JvmName("millimeterTimesSquareMillimeter")
infix operator fun ScientificValue<MeasurementType.Length, Millimeter>.times(area: ScientificValue<MeasurementType.Area, SquareMillimeter>) = area * this
@JvmName("centimeterTimesSquareCentimeter")
infix operator fun ScientificValue<MeasurementType.Length, Centimeter>.times(area: ScientificValue<MeasurementType.Area, SquareCentimeter>) = area * this
@JvmName("decimeterTimesSquareDecimeter")
infix operator fun ScientificValue<MeasurementType.Length, Decimeter>.times(area: ScientificValue<MeasurementType.Area, SquareDecimeter>) = area * this
@JvmName("decameterTimesSquareDecameter")
infix operator fun ScientificValue<MeasurementType.Length, Decameter>.times(area: ScientificValue<MeasurementType.Area, SquareDecameter>) = area * this
@JvmName("hectometerTimesSquarehectometer")
infix operator fun ScientificValue<MeasurementType.Length, Hectometer>.times(area: ScientificValue<MeasurementType.Area, SquareHectometer>) = area * this
@JvmName("kilometerTimesSquareKilometer")
infix operator fun ScientificValue<MeasurementType.Length, Kilometer>.times(area: ScientificValue<MeasurementType.Area, SquareKilometer>) = area * this
@JvmName("kilometerTimesSquareMegameter")
infix operator fun ScientificValue<MeasurementType.Length, Megameter>.times(area: ScientificValue<MeasurementType.Area, SquareMegameter>) = area * this
@JvmName("gigameterTimesSquareGigameter")
infix operator fun ScientificValue<MeasurementType.Length, Gigameter>.times(area: ScientificValue<MeasurementType.Area, SquareGigameter>) = area * this
@JvmName("metricLengthTimesMetricArea")
infix operator fun <AreaUnit : MetricArea, HeightUnit : MetricLength> ScientificValue<MeasurementType.Length, HeightUnit>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = area * this
@JvmName("inchTimesSquareInch")
infix operator fun ScientificValue<MeasurementType.Length, Inch>.times(area: ScientificValue<MeasurementType.Area, SquareInch>) = area * this
@JvmName("footTimesSquareFoot")
infix operator fun ScientificValue<MeasurementType.Length, Foot>.times(area: ScientificValue<MeasurementType.Area, SquareFoot>) = area * this
@JvmName("yardTimesSquareYard")
infix operator fun ScientificValue<MeasurementType.Length, Yard>.times(area: ScientificValue<MeasurementType.Area, SquareYard>) = area * this
@JvmName("mileTimesSquareMile")
infix operator fun ScientificValue<MeasurementType.Length, Mile>.times(area: ScientificValue<MeasurementType.Area, SquareMile>) = area * this
@JvmName("footTimesAcre")
infix operator fun ScientificValue<MeasurementType.Length, Foot>.times(area: ScientificValue<MeasurementType.Area, Acre>) = area * this
@JvmName("imperialLengthTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea, HeightUnit : ImperialLength> ScientificValue<MeasurementType.Length, HeightUnit>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = area * this
@JvmName("lengthTimesArea")
infix operator fun <AreaUnit : Area, HeightUnit : Length> ScientificValue<MeasurementType.Length, HeightUnit>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = area * this
