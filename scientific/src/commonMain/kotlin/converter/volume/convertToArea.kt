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

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.area.area
import com.splendo.kaluga.scientific.unit.Acre
import com.splendo.kaluga.scientific.unit.AcreFoot
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
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.ImperialVolume
import com.splendo.kaluga.scientific.unit.Inch
import com.splendo.kaluga.scientific.unit.Kilometer
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.Megameter
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricLength
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

@JvmName("cubicMeterDivMeter")
infix operator fun ScientificValue<MeasurementType.Volume, CubicMeter>.div(height: ScientificValue<MeasurementType.Length, Meter>) =
    SquareMeter.area(this, height)

@JvmName("cubicNanometerDivNanometer")
infix operator fun ScientificValue<MeasurementType.Volume, CubicNanometer>.div(height: ScientificValue<MeasurementType.Length, Nanometer>) =
    SquareNanometer.area(this, height)

@JvmName("cubicMicrometerDivMicrometer")
infix operator fun ScientificValue<MeasurementType.Volume, CubicMicrometer>.div(height: ScientificValue<MeasurementType.Length, Micrometer>) =
    SquareMicrometer.area(this, height)

@JvmName("cubicMillimeterDivMillimeter")
infix operator fun ScientificValue<MeasurementType.Volume, CubicMillimeter>.div(height: ScientificValue<MeasurementType.Length, Millimeter>) =
    SquareMillimeter.area(this, height)

@JvmName("cubicCentimeterDivCentimeter")
infix operator fun ScientificValue<MeasurementType.Volume, CubicCentimeter>.div(height: ScientificValue<MeasurementType.Length, Centimeter>) =
    SquareCentimeter.area(this, height)

@JvmName("cubicDecimeterDivDecimeter")
infix operator fun ScientificValue<MeasurementType.Volume, CubicDecimeter>.div(height: ScientificValue<MeasurementType.Length, Decimeter>) =
    SquareDecimeter.area(this, height)

@JvmName("cubicDecameterDivDecameter")
infix operator fun ScientificValue<MeasurementType.Volume, CubicDecameter>.div(height: ScientificValue<MeasurementType.Length, Decameter>) =
    SquareDecameter.area(this, height)

@JvmName("cubicHectometerDivHectometer")
infix operator fun ScientificValue<MeasurementType.Volume, CubicHectometer>.div(height: ScientificValue<MeasurementType.Length, Hectometer>) =
    SquareHectometer.area(this, height)

@JvmName("cubicKilometerDivKilometer")
infix operator fun ScientificValue<MeasurementType.Volume, CubicKilometer>.div(height: ScientificValue<MeasurementType.Length, Kilometer>) =
    SquareKilometer.area(this, height)

@JvmName("cubicMegameterDivMegameter")
infix operator fun ScientificValue<MeasurementType.Volume, CubicMegameter>.div(height: ScientificValue<MeasurementType.Length, Megameter>) =
    SquareMegameter.area(this, height)

@JvmName("cubicGigameterDivGigameter")
infix operator fun ScientificValue<MeasurementType.Volume, CubicGigameter>.div(height: ScientificValue<MeasurementType.Length, Gigameter>) =
    SquareGigameter.area(this, height)

@JvmName("metricVolumeDivMetricLength")
infix operator fun <VolumeUnit : MetricVolume, HeightUnit : MetricLength> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(
    height: ScientificValue<MeasurementType.Length, HeightUnit>
) = SquareMeter.area(this, height)

@JvmName("cubicInchDivInch")
infix operator fun ScientificValue<MeasurementType.Volume, CubicInch>.div(height: ScientificValue<MeasurementType.Length, Inch>) =
    SquareInch.area(this, height)

@JvmName("cubicFootDivFoot")
infix operator fun ScientificValue<MeasurementType.Volume, CubicFoot>.div(height: ScientificValue<MeasurementType.Length, Foot>) =
    SquareFoot.area(this, height)

@JvmName("cubicYardDivYard")
infix operator fun ScientificValue<MeasurementType.Volume, CubicYard>.div(height: ScientificValue<MeasurementType.Length, Yard>) =
    SquareYard.area(this, height)

@JvmName("cubicMileDivMile")
infix operator fun ScientificValue<MeasurementType.Volume, CubicMile>.div(height: ScientificValue<MeasurementType.Length, Mile>) =
    SquareMile.area(this, height)

@JvmName("acreFootDivFoot")
infix operator fun ScientificValue<MeasurementType.Volume, AcreFoot>.div(height: ScientificValue<MeasurementType.Length, Foot>) =
    Acre.area(this, height)

@JvmName("imperialVolumeDivImperialLength")
infix operator fun <VolumeUnit : ImperialVolume, HeightUnit : ImperialLength> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(
    height: ScientificValue<MeasurementType.Length, HeightUnit>
) = SquareFoot.area(this, height)

@JvmName("ukImperialVolumeDivImperialLength")
infix operator fun <VolumeUnit : UKImperialVolume, HeightUnit : ImperialLength> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(
    height: ScientificValue<MeasurementType.Length, HeightUnit>
) = SquareFoot.area(this, height)

@JvmName("usCustomaryVolumeDivImperialLength")
infix operator fun <VolumeUnit : USCustomaryVolume, HeightUnit : ImperialLength> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(
    height: ScientificValue<MeasurementType.Length, HeightUnit>
) = SquareFoot.area(this, height)

@JvmName("volumeDivLength")
infix operator fun <VolumeUnit : Volume, HeightUnit : Length> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(
    height: ScientificValue<MeasurementType.Length, HeightUnit>
) = SquareMeter.area(this, height)
