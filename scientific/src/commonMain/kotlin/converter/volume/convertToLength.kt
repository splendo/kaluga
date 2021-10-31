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

package com.splendo.kaluga.scientific.converter.volume

import com.splendo.kaluga.scientific.Acre
import com.splendo.kaluga.scientific.AcreFoot
import com.splendo.kaluga.scientific.Area
import com.splendo.kaluga.scientific.Centimeter
import com.splendo.kaluga.scientific.CubicCentimeter
import com.splendo.kaluga.scientific.CubicDecameter
import com.splendo.kaluga.scientific.CubicDecimeter
import com.splendo.kaluga.scientific.CubicFoot
import com.splendo.kaluga.scientific.CubicGigameter
import com.splendo.kaluga.scientific.CubicHectometer
import com.splendo.kaluga.scientific.CubicInch
import com.splendo.kaluga.scientific.CubicKilometer
import com.splendo.kaluga.scientific.CubicMegameter
import com.splendo.kaluga.scientific.CubicMeter
import com.splendo.kaluga.scientific.CubicMicrometer
import com.splendo.kaluga.scientific.CubicMile
import com.splendo.kaluga.scientific.CubicMillimeter
import com.splendo.kaluga.scientific.CubicNanometer
import com.splendo.kaluga.scientific.CubicYard
import com.splendo.kaluga.scientific.Decameter
import com.splendo.kaluga.scientific.Decimeter
import com.splendo.kaluga.scientific.Foot
import com.splendo.kaluga.scientific.Gigameter
import com.splendo.kaluga.scientific.Hectometer
import com.splendo.kaluga.scientific.ImperialArea
import com.splendo.kaluga.scientific.ImperialVolume
import com.splendo.kaluga.scientific.Inch
import com.splendo.kaluga.scientific.Kilometer
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Megameter
import com.splendo.kaluga.scientific.Meter
import com.splendo.kaluga.scientific.MetricArea
import com.splendo.kaluga.scientific.MetricVolume
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
import com.splendo.kaluga.scientific.UKImperialVolume
import com.splendo.kaluga.scientific.USCustomaryVolume
import com.splendo.kaluga.scientific.Volume
import com.splendo.kaluga.scientific.Yard
import com.splendo.kaluga.scientific.converter.length.height
import kotlin.jvm.JvmName

@JvmName("cubicMeterDivSquareMeter")
infix operator fun ScientificValue<MeasurementType.Volume, CubicMeter>.div(area: ScientificValue<MeasurementType.Area, SquareMeter>) = Meter.height(this, area)
@JvmName("cubicNanometerDivSquareNanometer")
infix operator fun ScientificValue<MeasurementType.Volume, CubicNanometer>.div(area: ScientificValue<MeasurementType.Area, SquareNanometer>) = Nanometer.height(this, area)
@JvmName("cubicMicrometerDivSquareMicrometer")
infix operator fun ScientificValue<MeasurementType.Volume, CubicMicrometer>.div(area: ScientificValue<MeasurementType.Area, SquareMicrometer>) = Micrometer.height(this, area)
@JvmName("cubicMillimeterDivSquareMillimeter")
infix operator fun ScientificValue<MeasurementType.Volume, CubicMillimeter>.div(area: ScientificValue<MeasurementType.Area, SquareMillimeter>) = Millimeter.height(this, area)
@JvmName("cubicCentimeterDivSquareCentimeter")
infix operator fun ScientificValue<MeasurementType.Volume, CubicCentimeter>.div(area: ScientificValue<MeasurementType.Area, SquareCentimeter>) = Centimeter.height(this, area)
@JvmName("cubicDecimeterDivSquareDecimeter")
infix operator fun ScientificValue<MeasurementType.Volume, CubicDecimeter>.div(area: ScientificValue<MeasurementType.Area, SquareDecimeter>) = Decimeter.height(this, area)
@JvmName("cubicDecameterDivSquareDecameter")
infix operator fun ScientificValue<MeasurementType.Volume, CubicDecameter>.div(area: ScientificValue<MeasurementType.Area, SquareDecameter>) = Decameter.height(this, area)
@JvmName("cubicHectometerDivSquareHectometer")
infix operator fun ScientificValue<MeasurementType.Volume, CubicHectometer>.div(area: ScientificValue<MeasurementType.Area, SquareHectometer>) = Hectometer.height(this, area)
@JvmName("cubicKilometerDivSquareKilometer")
infix operator fun ScientificValue<MeasurementType.Volume, CubicKilometer>.div(area: ScientificValue<MeasurementType.Area, SquareKilometer>) = Kilometer.height(this, area)
@JvmName("cubicMegameterDivSquareMegameter")
infix operator fun ScientificValue<MeasurementType.Volume, CubicMegameter>.div(area: ScientificValue<MeasurementType.Area, SquareMegameter>) = Megameter.height(this, area)
@JvmName("cubicGigameterDivSquareGigameter")
infix operator fun ScientificValue<MeasurementType.Volume, CubicGigameter>.div(area: ScientificValue<MeasurementType.Area, SquareGigameter>) = Gigameter.height(this, area)
@JvmName("metricVolumeDivMetricArea")
infix operator fun <VolumeUnit : MetricVolume, AreaUnit : MetricArea> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Meter.height(this, area)
@JvmName("cubicInchDivSquareInch")
infix operator fun ScientificValue<MeasurementType.Volume, CubicInch>.div(area: ScientificValue<MeasurementType.Area, SquareInch>) = Inch.height( this, area)
@JvmName("cubicFootDivSquareFoot")
infix operator fun ScientificValue<MeasurementType.Volume, CubicFoot>.div(area: ScientificValue<MeasurementType.Area, SquareFoot>) = Foot.height(this, area)
@JvmName("cubicYardDivSquareYard")
infix operator fun ScientificValue<MeasurementType.Volume, CubicYard>.div(area: ScientificValue<MeasurementType.Area, SquareYard>) = Yard.height(this, area)
@JvmName("cubicMileDivSquareMile")
infix operator fun ScientificValue<MeasurementType.Volume, CubicMile>.div(area: ScientificValue<MeasurementType.Area, SquareMile>) = Mile.height(this, area)
@JvmName("acreFootDivAcre")
infix operator fun ScientificValue<MeasurementType.Volume, AcreFoot>.div(area: ScientificValue<MeasurementType.Area, Acre>) = Foot.height(this, area)
@JvmName("imperialVolumeDivImperialArea")
infix operator fun <VolumeUnit : ImperialVolume, AreaUnit : ImperialArea> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Foot.height(this, area)
@JvmName("ukImperialVolumeDivImperialArea")
infix operator fun <VolumeUnit : UKImperialVolume, AreaUnit : ImperialArea> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Foot.height(this, area)
@JvmName("usCustomaryVolumeDivImperialArea")
infix operator fun <VolumeUnit : USCustomaryVolume, AreaUnit : ImperialArea> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Foot.height(this, area)
@JvmName("volumeDivArea")
infix operator fun <VolumeUnit : Volume, AreaUnit : Area> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Meter.height(this, area)
