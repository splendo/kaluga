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
import com.splendo.kaluga.scientific.converter.pressure.times
import com.splendo.kaluga.scientific.unit.Barye
import com.splendo.kaluga.scientific.unit.CubicCentimeter
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicInch
import com.splendo.kaluga.scientific.unit.ImperialPressure
import com.splendo.kaluga.scientific.unit.ImperialTonSquareInch
import com.splendo.kaluga.scientific.unit.ImperialVolume
import com.splendo.kaluga.scientific.unit.KiloPoundSquareInch
import com.splendo.kaluga.scientific.unit.KipSquareInch
import com.splendo.kaluga.scientific.unit.MeasurementSystem
import com.splendo.kaluga.scientific.unit.MetricMultipleUnit
import com.splendo.kaluga.scientific.unit.MetricPressure
import com.splendo.kaluga.scientific.unit.MetricVolume
import com.splendo.kaluga.scientific.unit.OunceSquareInch
import com.splendo.kaluga.scientific.unit.PoundSquareFoot
import com.splendo.kaluga.scientific.unit.PoundSquareInch
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.UKImperialPressure
import com.splendo.kaluga.scientific.unit.UKImperialVolume
import com.splendo.kaluga.scientific.unit.USCustomaryPressure
import com.splendo.kaluga.scientific.unit.USCustomaryVolume
import com.splendo.kaluga.scientific.unit.USTonSquareInch
import com.splendo.kaluga.scientific.unit.Volume
import kotlin.jvm.JvmName

@JvmName("cubicCentimeterTimesBarye")
infix operator fun ScientificValue<MeasurementType.Volume, CubicCentimeter>.times(pressure: ScientificValue<MeasurementType.Pressure, Barye>) =
    pressure * this

@JvmName("cubicCentimeterTimesBaryeMultiple")
infix operator fun <BaryeUnit> ScientificValue<MeasurementType.Volume, CubicCentimeter>.times(
    pressure: ScientificValue<MeasurementType.Pressure, BaryeUnit>
) where BaryeUnit : Pressure, BaryeUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Pressure, Barye> =
    pressure * this

@JvmName("metricVolumeTimesMetricPressure")
infix operator fun <PressureUnit : MetricPressure, VolumeUnit : MetricVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = pressure * this

@JvmName("cubicFootTimesPoundSquareFoot")
infix operator fun ScientificValue<MeasurementType.Volume, CubicFoot>.times(pressure: ScientificValue<MeasurementType.Pressure, PoundSquareFoot>) =
    pressure * this

@JvmName("cubicInchTimesPoundSquareInch")
infix operator fun ScientificValue<MeasurementType.Volume, CubicInch>.times(pressure: ScientificValue<MeasurementType.Pressure, PoundSquareInch>) =
    pressure * this

@JvmName("cubicInchTimesOunceSquareInch")
infix operator fun ScientificValue<MeasurementType.Volume, CubicInch>.times(pressure: ScientificValue<MeasurementType.Pressure, OunceSquareInch>) =
    pressure * this

@JvmName("cubicInchTimesKilopoundSquareInch")
infix operator fun ScientificValue<MeasurementType.Volume, CubicInch>.times(pressure: ScientificValue<MeasurementType.Pressure, KiloPoundSquareInch>) =
    pressure * this

@JvmName("cubicInchTimesKipSquareInch")
infix operator fun ScientificValue<MeasurementType.Volume, CubicInch>.times(pressure: ScientificValue<MeasurementType.Pressure, KipSquareInch>) =
    pressure * this

@JvmName("cubicInchTimesUSTonSquareInch")
infix operator fun ScientificValue<MeasurementType.Volume, CubicInch>.times(pressure: ScientificValue<MeasurementType.Pressure, USTonSquareInch>) =
    pressure * this

@JvmName("cubicInchTimesImperialTonSquareInch")
infix operator fun ScientificValue<MeasurementType.Volume, CubicInch>.times(pressure: ScientificValue<MeasurementType.Pressure, ImperialTonSquareInch>) =
    pressure * this

@JvmName("imperialVolumeTimesImperialPressure")
infix operator fun <PressureUnit : ImperialPressure, VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = pressure * this

@JvmName("imperialVolumeTimesUKImperialPressure")
infix operator fun <PressureUnit : UKImperialPressure, VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = pressure * this

@JvmName("imperialVolumeTimesUSCustomaryPressure")
infix operator fun <PressureUnit : USCustomaryPressure, VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = pressure * this

@JvmName("ukImperialVolumeTimesImperialPressure")
infix operator fun <PressureUnit : ImperialPressure, VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = pressure * this

@JvmName("ukImperialVolumeTimesUKImperialPressure")
infix operator fun <PressureUnit : UKImperialPressure, VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = pressure * this

@JvmName("ukImperialVolumeTimesUSCustomaryPressure")
infix operator fun <PressureUnit : USCustomaryPressure, VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = pressure * this

@JvmName("usCustomaryVolumeTimesImperialPressure")
infix operator fun <PressureUnit : ImperialPressure, VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = pressure * this

@JvmName("usCustomaryVolumeTimesUKImperialPressure")
infix operator fun <PressureUnit : UKImperialPressure, VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = pressure * this

@JvmName("usCustomaryVolumeTimesUSCustomaryPressure")
infix operator fun <PressureUnit : USCustomaryPressure, VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = pressure * this

@JvmName("volumeTimesPressure")
infix operator fun <PressureUnit : Pressure, VolumeUnit : Volume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(
    pressure: ScientificValue<MeasurementType.Pressure, PressureUnit>
) = pressure * this
