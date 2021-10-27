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

package com.splendo.kaluga.scientific.volume

import com.splendo.kaluga.scientific.Acre
import com.splendo.kaluga.scientific.AcreFoot
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
import com.splendo.kaluga.scientific.ImperialLength
import com.splendo.kaluga.scientific.ImperialVolume
import com.splendo.kaluga.scientific.Inch
import com.splendo.kaluga.scientific.Kilometer
import com.splendo.kaluga.scientific.Length
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Megameter
import com.splendo.kaluga.scientific.Meter
import com.splendo.kaluga.scientific.MetricLength
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
import com.splendo.kaluga.scientific.area.area
import kotlin.jvm.JvmName

@JvmName("cubicMeterDivMeter")
operator fun ScientificValue<MeasurementType.Volume, CubicMeter>.div(height: ScientificValue<MeasurementType.Length, Meter>) = SquareMeter.area(this, height)
@JvmName("cubicNanometerDivNanometer")
operator fun ScientificValue<MeasurementType.Volume, CubicNanometer>.div(height: ScientificValue<MeasurementType.Length, Nanometer>) = SquareNanometer.area(this, height)
@JvmName("cubicMicrometerDivMicrometer")
operator fun ScientificValue<MeasurementType.Volume, CubicMicrometer>.div(height: ScientificValue<MeasurementType.Length, Micrometer>) = SquareMicrometer.area(this, height)
@JvmName("cubicMillimeterDivMillimeter")
operator fun ScientificValue<MeasurementType.Volume, CubicMillimeter>.div(height: ScientificValue<MeasurementType.Length, Millimeter>) = SquareMillimeter.area(this, height)
@JvmName("cubicCentimeterDivCentimeter")
operator fun ScientificValue<MeasurementType.Volume, CubicCentimeter>.div(height: ScientificValue<MeasurementType.Length, Centimeter>) = SquareCentimeter.area(this, height)
@JvmName("cubicDecimeterDivDecimeter")
operator fun ScientificValue<MeasurementType.Volume, CubicDecimeter>.div(height: ScientificValue<MeasurementType.Length, Decimeter>) = SquareDecimeter.area(this, height)
@JvmName("cubicDecameterDivDecameter")
operator fun ScientificValue<MeasurementType.Volume, CubicDecameter>.div(height: ScientificValue<MeasurementType.Length, Decameter>) = SquareDecameter.area(this, height)
@JvmName("cubicHectometerDivHectometer")
operator fun ScientificValue<MeasurementType.Volume, CubicHectometer>.div(height: ScientificValue<MeasurementType.Length, Hectometer>) = SquareHectometer.area(this, height)
@JvmName("cubicKilometerDivKilometer")
operator fun ScientificValue<MeasurementType.Volume, CubicKilometer>.div(height: ScientificValue<MeasurementType.Length, Kilometer>) = SquareKilometer.area(this, height)
@JvmName("cubicMegameterDivMegameter")
operator fun ScientificValue<MeasurementType.Volume, CubicMegameter>.div(height: ScientificValue<MeasurementType.Length, Megameter>) = SquareMegameter.area(this, height)
@JvmName("cubicGigameterDivGigameter")
operator fun ScientificValue<MeasurementType.Volume, CubicGigameter>.div(height: ScientificValue<MeasurementType.Length, Gigameter>) = SquareGigameter.area(this, height)
@JvmName("metricVolumeDivMetricLength")
operator fun <VolumeUnit : MetricVolume, HeightUnit : MetricLength> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(height: ScientificValue<MeasurementType.Length, HeightUnit>) = SquareMeter.area(this, height)
@JvmName("cubicInchDivInch")
operator fun ScientificValue<MeasurementType.Volume, CubicInch>.div(height: ScientificValue<MeasurementType.Length, Inch>) = SquareInch.area(this, height)
@JvmName("cubicFootDivFoot")
operator fun ScientificValue<MeasurementType.Volume, CubicFoot>.div(height: ScientificValue<MeasurementType.Length, Foot>) = SquareFoot.area(this, height)
@JvmName("cubicYardDivYard")
operator fun ScientificValue<MeasurementType.Volume, CubicYard>.div(height: ScientificValue<MeasurementType.Length, Yard>) = SquareYard.area(this, height)
@JvmName("cubicMileDivMile")
operator fun ScientificValue<MeasurementType.Volume, CubicMile>.div(height: ScientificValue<MeasurementType.Length, Mile>) = SquareMile.area(this, height)
@JvmName("acreFootDivFoot")
operator fun ScientificValue<MeasurementType.Volume, AcreFoot>.div(height: ScientificValue<MeasurementType.Length, Foot>) = Acre.area(this, height)
@JvmName("imperialVolumeDivImperialLength")
operator fun <VolumeUnit : ImperialVolume, HeightUnit : ImperialLength> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(height: ScientificValue<MeasurementType.Length, HeightUnit>) = SquareFoot.area(this, height)
@JvmName("ukImperialVolumeDivImperialLength")
operator fun <VolumeUnit : UKImperialVolume, HeightUnit : ImperialLength> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(height: ScientificValue<MeasurementType.Length, HeightUnit>) = SquareFoot.area(this, height)
@JvmName("usCustomaryVolumeDivImperialLength")
operator fun <VolumeUnit : USCustomaryVolume, HeightUnit : ImperialLength> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(height: ScientificValue<MeasurementType.Length, HeightUnit>) = SquareFoot.area(this, height)
@JvmName("volumeDivLength")
operator fun <VolumeUnit : Volume, HeightUnit : Length> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(height: ScientificValue<MeasurementType.Length, HeightUnit>) = SquareMeter.area(this, height)
