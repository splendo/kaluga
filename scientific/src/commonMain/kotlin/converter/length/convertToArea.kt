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

import com.splendo.kaluga.scientific.Centimeter
import com.splendo.kaluga.scientific.Decameter
import com.splendo.kaluga.scientific.Decimeter
import com.splendo.kaluga.scientific.Foot
import com.splendo.kaluga.scientific.Gigameter
import com.splendo.kaluga.scientific.Hectometer
import com.splendo.kaluga.scientific.ImperialLength
import com.splendo.kaluga.scientific.Inch
import com.splendo.kaluga.scientific.Kilometer
import com.splendo.kaluga.scientific.Length
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Megameter
import com.splendo.kaluga.scientific.Meter
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
import com.splendo.kaluga.scientific.converter.area.area
import kotlin.jvm.JvmName

@JvmName("meterTimesMeter")
infix operator fun ScientificValue<MeasurementType.Length, Meter>.times(width: ScientificValue<MeasurementType.Length, Meter>) = SquareMeter.area(this, width)
@JvmName("nanometerTimesNanometer")
infix operator fun ScientificValue<MeasurementType.Length, Nanometer>.times(width: ScientificValue<MeasurementType.Length, Nanometer>) = SquareNanometer.area(this, width)
@JvmName("micrometerTimesMicrometer")
infix operator fun ScientificValue<MeasurementType.Length, Micrometer>.times(width: ScientificValue<MeasurementType.Length, Micrometer>) = SquareMicrometer.area(this, width)
@JvmName("millimeterTimesMillimeter")
infix operator fun ScientificValue<MeasurementType.Length, Millimeter>.times(width: ScientificValue<MeasurementType.Length, Millimeter>) = SquareMillimeter.area(this, width)
@JvmName("centimeterTimesCentieter")
infix operator fun ScientificValue<MeasurementType.Length, Centimeter>.times(width: ScientificValue<MeasurementType.Length, Centimeter>) = SquareCentimeter.area(this, width)
@JvmName("decimeterTimesDecimeter")
infix operator fun ScientificValue<MeasurementType.Length, Decimeter>.times(width: ScientificValue<MeasurementType.Length, Decimeter>) = SquareDecimeter.area(this, width)
@JvmName("decameterTimesDecameter")
infix operator fun ScientificValue<MeasurementType.Length, Decameter>.times(width: ScientificValue<MeasurementType.Length, Decameter>) = SquareDecameter.area(this, width)
@JvmName("hectometerTimesHectometer")
infix operator fun ScientificValue<MeasurementType.Length, Hectometer>.times(width: ScientificValue<MeasurementType.Length, Hectometer>) = SquareHectometer.area(this, width)
@JvmName("kilometerTimesKilometer")
infix operator fun ScientificValue<MeasurementType.Length, Kilometer>.times(width: ScientificValue<MeasurementType.Length, Kilometer>) = SquareKilometer.area(this, width)
@JvmName("megameterTimesMegameter")
infix operator fun ScientificValue<MeasurementType.Length, Megameter>.times(width: ScientificValue<MeasurementType.Length, Megameter>) = SquareMegameter.area(this, width)
@JvmName("gigameterTimesGigameter")
infix operator fun ScientificValue<MeasurementType.Length, Gigameter>.times(width: ScientificValue<MeasurementType.Length, Gigameter>) = SquareGigameter.area(this, width)
@JvmName("metricLengthTimesMetricLength")
infix operator fun <LengthUnit : MetricLength, WidthUnit : MetricLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(width: ScientificValue<MeasurementType.Length, WidthUnit>) = SquareMeter.area(this, width)
@JvmName("inchTimesInch")
infix operator fun ScientificValue<MeasurementType.Length, Inch>.times(width: ScientificValue<MeasurementType.Length, Inch>) = SquareInch.area(this, width)
@JvmName("footTimesFoot")
infix operator fun ScientificValue<MeasurementType.Length, Foot>.times(width: ScientificValue<MeasurementType.Length, Foot>) = SquareFoot.area(this, width)
@JvmName("yardTimesYard")
infix operator fun ScientificValue<MeasurementType.Length, Yard>.times(width: ScientificValue<MeasurementType.Length, Yard>) = SquareYard.area(this, width)
@JvmName("mileTimesMile")
infix operator fun ScientificValue<MeasurementType.Length, Mile>.times(width: ScientificValue<MeasurementType.Length, Mile>) = SquareMile.area(this, width)
@JvmName("imperialLengthTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength, WidthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(width: ScientificValue<MeasurementType.Length, WidthUnit>) = SquareFoot.area(this, width)
@JvmName("lengthTimesLength")
infix operator fun <LengthUnit : Length, WidthUnit : Length> ScientificValue<MeasurementType.Length, LengthUnit>.times(width: ScientificValue<MeasurementType.Length, WidthUnit>) = SquareMeter.area(this, width)
