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

package com.splendo.kaluga.scientific.area

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
import com.splendo.kaluga.scientific.length.width
import kotlin.jvm.JvmName

@JvmName("squareMeterDivMeter")
operator fun ScientificValue<MeasurementType.Area, SquareMeter>.div(length: ScientificValue<MeasurementType.Length, Meter>) = Meter.width(this, length)
@JvmName("squareNanometerDivNanometer")
operator fun ScientificValue<MeasurementType.Area, SquareNanometer>.div(length: ScientificValue<MeasurementType.Length, Nanometer>) = Nanometer.width(this, length)
@JvmName("squareMicrometerDivMicrometer")
operator fun ScientificValue<MeasurementType.Area, SquareMicrometer>.div(length: ScientificValue<MeasurementType.Length, Micrometer>) = Micrometer.width(this, length)
@JvmName("squareMillimeterDivMillimeter")
operator fun ScientificValue<MeasurementType.Area, SquareMillimeter>.div(length: ScientificValue<MeasurementType.Length, Millimeter>) = Millimeter.width(this, length)
@JvmName("squareCentimeterDivCentimeter")
operator fun ScientificValue<MeasurementType.Area, SquareCentimeter>.div(length: ScientificValue<MeasurementType.Length, Centimeter>) = Centimeter.width(this, length)
@JvmName("squareDecimeterDivDecimeter")
operator fun ScientificValue<MeasurementType.Area, SquareDecimeter>.div(length: ScientificValue<MeasurementType.Length, Decimeter>) = Decimeter.width(this, length)
@JvmName("squareDecameterDivDecameter")
operator fun ScientificValue<MeasurementType.Area, SquareDecameter>.div(length: ScientificValue<MeasurementType.Length, Decameter>) = Decameter.width(this, length)
@JvmName("squareHectometerDivHectometer")
operator fun ScientificValue<MeasurementType.Area, SquareHectometer>.div(length: ScientificValue<MeasurementType.Length, Hectometer>) = Hectometer.width(this, length)
@JvmName("squareKilometerDivKilometer")
operator fun ScientificValue<MeasurementType.Area, SquareKilometer>.div(length: ScientificValue<MeasurementType.Length, Kilometer>) = Kilometer.width(this, length)
@JvmName("squareMegameterDivMegameter")
operator fun ScientificValue<MeasurementType.Area, SquareMegameter>.div(length: ScientificValue<MeasurementType.Length, Megameter>) = Megameter.width(this, length)
@JvmName("squareGigameterDivGigameter")
operator fun ScientificValue<MeasurementType.Area, SquareGigameter>.div(length: ScientificValue<MeasurementType.Length, Gigameter>) = Gigameter.width(this, length)
@JvmName("metricAreaDivMetricLength")
operator fun <AreaUnit : MetricArea, LengthUnit : MetricLength> ScientificValue<MeasurementType.Area, AreaUnit>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = Meter.width(this, length)
@JvmName("squareInchDivInch")
operator fun ScientificValue<MeasurementType.Area, SquareInch>.div(length: ScientificValue<MeasurementType.Length, Inch>) = Inch.width(this, length)
@JvmName("squareFootDivFoot")
operator fun ScientificValue<MeasurementType.Area, SquareFoot>.div(length: ScientificValue<MeasurementType.Length, Foot>) = Foot.width(this, length)
@JvmName("squareYardDivYard")
operator fun ScientificValue<MeasurementType.Area, SquareYard>.div(length: ScientificValue<MeasurementType.Length, Yard>) = Yard.width(this, length)
@JvmName("squareMileDivMile")
operator fun ScientificValue<MeasurementType.Area, SquareMile>.div(length: ScientificValue<MeasurementType.Length, Mile>) = Mile.width(this, length)
@JvmName("imperialAreaDivImperialLength")
operator fun <AreaUnit : ImperialArea, LengthUnit : ImperialLength> ScientificValue<MeasurementType.Area, AreaUnit>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = Foot.width(this, length)
@JvmName("areaDivLength")
operator fun <AreaUnit : Area, LengthUnit : Length> ScientificValue<MeasurementType.Area, AreaUnit>.div(length: ScientificValue<MeasurementType.Length, LengthUnit>) = Meter.width(this, length)
