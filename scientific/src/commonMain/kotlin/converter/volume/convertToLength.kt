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

package com.splendo.kaluga.scientific.converter.volume

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.length.height
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
import com.splendo.kaluga.scientific.unit.ImperialVolume
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.Kilometer
import com.splendo.kaluga.scientific.unit.Megameter
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricVolume
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
import com.splendo.kaluga.scientific.unit.UKImperialVolume
import com.splendo.kaluga.scientific.unit.USCustomaryVolume
import com.splendo.kaluga.scientific.unit.Volume
import com.splendo.kaluga.scientific.unit.Yard
import kotlin.jvm.JvmName

@JvmName("cubicMeterDivSquareMeter")
infix operator fun ScientificValue<PhysicalQuantity.Volume, CubicMeter>.div(area: ScientificValue<PhysicalQuantity.Area, SquareMeter>) = Meter.height(this, area)

@JvmName("cubicNanometerDivSquareNanometer")
infix operator fun ScientificValue<PhysicalQuantity.Volume, CubicNanometer>.div(area: ScientificValue<PhysicalQuantity.Area, SquareNanometer>) = Nanometer.height(this, area)

@JvmName("cubicMicrometerDivSquareMicrometer")
infix operator fun ScientificValue<PhysicalQuantity.Volume, CubicMicrometer>.div(area: ScientificValue<PhysicalQuantity.Area, SquareMicrometer>) = Micrometer.height(this, area)

@JvmName("cubicMillimeterDivSquareMillimeter")
infix operator fun ScientificValue<PhysicalQuantity.Volume, CubicMillimeter>.div(area: ScientificValue<PhysicalQuantity.Area, SquareMillimeter>) = Millimeter.height(this, area)

@JvmName("cubicCentimeterDivSquareCentimeter")
infix operator fun ScientificValue<PhysicalQuantity.Volume, CubicCentimeter>.div(area: ScientificValue<PhysicalQuantity.Area, SquareCentimeter>) = Centimeter.height(this, area)

@JvmName("cubicDecimeterDivSquareDecimeter")
infix operator fun ScientificValue<PhysicalQuantity.Volume, CubicDecimeter>.div(area: ScientificValue<PhysicalQuantity.Area, SquareDecimeter>) = Decimeter.height(this, area)

@JvmName("cubicDecameterDivSquareDecameter")
infix operator fun ScientificValue<PhysicalQuantity.Volume, CubicDecameter>.div(area: ScientificValue<PhysicalQuantity.Area, SquareDecameter>) = Decameter.height(this, area)

@JvmName("cubicHectometerDivSquareHectometer")
infix operator fun ScientificValue<PhysicalQuantity.Volume, CubicHectometer>.div(area: ScientificValue<PhysicalQuantity.Area, SquareHectometer>) = Hectometer.height(this, area)

@JvmName("cubicKilometerDivSquareKilometer")
infix operator fun ScientificValue<PhysicalQuantity.Volume, CubicKilometer>.div(area: ScientificValue<PhysicalQuantity.Area, SquareKilometer>) = Kilometer.height(this, area)

@JvmName("cubicMegameterDivSquareMegameter")
infix operator fun ScientificValue<PhysicalQuantity.Volume, CubicMegameter>.div(area: ScientificValue<PhysicalQuantity.Area, SquareMegameter>) = Megameter.height(this, area)

@JvmName("cubicGigameterDivSquareGigameter")
infix operator fun ScientificValue<PhysicalQuantity.Volume, CubicGigameter>.div(area: ScientificValue<PhysicalQuantity.Area, SquareGigameter>) = Gigameter.height(this, area)

@JvmName("metricVolumeDivMetricArea")
infix operator fun <VolumeUnit : MetricVolume, AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = Meter.height(this, area)

@JvmName("cubicInchDivSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.Volume, CubicInch>.div(area: ScientificValue<PhysicalQuantity.Area, SquareInch>) = Inch.height(this, area)

@JvmName("cubicFootDivSquareFoot")
infix operator fun ScientificValue<PhysicalQuantity.Volume, CubicFoot>.div(area: ScientificValue<PhysicalQuantity.Area, SquareFoot>) = Foot.height(this, area)

@JvmName("cubicYardDivSquareYard")
infix operator fun ScientificValue<PhysicalQuantity.Volume, CubicYard>.div(area: ScientificValue<PhysicalQuantity.Area, SquareYard>) = Yard.height(this, area)

@JvmName("cubicMileDivSquareMile")
infix operator fun ScientificValue<PhysicalQuantity.Volume, CubicMile>.div(area: ScientificValue<PhysicalQuantity.Area, SquareMile>) = Mile.height(this, area)

@JvmName("acreFootDivAcre")
infix operator fun ScientificValue<PhysicalQuantity.Volume, AcreFoot>.div(area: ScientificValue<PhysicalQuantity.Area, Acre>) = Foot.height(this, area)

@JvmName("acreInchDivAcre")
infix operator fun ScientificValue<PhysicalQuantity.Volume, AcreInch>.div(area: ScientificValue<PhysicalQuantity.Area, Acre>) = Inch.height(this, area)

@JvmName("imperialVolumeDivImperialArea")
infix operator fun <VolumeUnit : ImperialVolume, AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = Foot.height(this, area)

@JvmName("ukImperialVolumeDivImperialArea")
infix operator fun <VolumeUnit : UKImperialVolume, AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = Foot.height(this, area)

@JvmName("usCustomaryVolumeDivImperialArea")
infix operator fun <VolumeUnit : USCustomaryVolume, AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = Foot.height(this, area)

@JvmName("volumeDivArea")
infix operator fun <VolumeUnit : Volume, AreaUnit : Area> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    Meter.height(this, area)
