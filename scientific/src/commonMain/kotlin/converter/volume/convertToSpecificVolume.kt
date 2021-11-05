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
import com.splendo.kaluga.scientific.converter.specificVolume.specificVolume
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialVolume
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricVolume
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.UKImperialVolume
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryVolume
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Volume
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricVolumeDivMetricWeight")
infix operator fun <VolumeUnit : MetricVolume, WeightUnit : MetricWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (unit per weight.unit).specificVolume(this, weight)

@JvmName("imperialVolumeDivImperialWeight")
infix operator fun <VolumeUnit : ImperialVolume, WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (unit per weight.unit).specificVolume(this, weight)

@JvmName("imperialVolumeDivUKImperialWeight")
infix operator fun <VolumeUnit : ImperialVolume, WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (unit per weight.unit).specificVolume(this, weight)

@JvmName("imperialVolumeDivUSCustomaryWeight")
infix operator fun <VolumeUnit : ImperialVolume, WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (unit per weight.unit).specificVolume(this, weight)

@JvmName("ukImperialVolumeDivImperialWeight")
infix operator fun <VolumeUnit : UKImperialVolume, WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (unit per weight.unit).specificVolume(this, weight)

@JvmName("ukImperialVolumeDivUKImperialWeight")
infix operator fun <VolumeUnit : UKImperialVolume, WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (unit per weight.unit).specificVolume(this, weight)

@JvmName("usCustomaryVolumeDivImperialWeight")
infix operator fun <VolumeUnit : USCustomaryVolume, WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (unit per weight.unit).specificVolume(this, weight)

@JvmName("usCustomaryVolumeDivUSCustomaryWeight")
infix operator fun <VolumeUnit : USCustomaryVolume, WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (unit per weight.unit).specificVolume(this, weight)

@JvmName("volumeDivWeight")
infix operator fun <VolumeUnit : Volume, WeightUnit : Weight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (CubicMeter per Kilogram).specificVolume(this, weight)
