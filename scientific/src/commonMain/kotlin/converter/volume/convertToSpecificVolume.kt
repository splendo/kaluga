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

import com.splendo.kaluga.scientific.CubicMeter
import com.splendo.kaluga.scientific.ImperialVolume
import com.splendo.kaluga.scientific.ImperialWeight
import com.splendo.kaluga.scientific.Kilogram
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricVolume
import com.splendo.kaluga.scientific.MetricWeight
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialVolume
import com.splendo.kaluga.scientific.UKImperialWeight
import com.splendo.kaluga.scientific.USCustomaryVolume
import com.splendo.kaluga.scientific.USCustomaryWeight
import com.splendo.kaluga.scientific.Volume
import com.splendo.kaluga.scientific.Weight
import com.splendo.kaluga.scientific.converter.specificVolume.specificVolume
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("metricVolumeDivMetricWeight")
infix operator fun <VolumeUnit : MetricVolume, WeightUnit : MetricWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificVolume(this, weight)
@JvmName("imperialVolumeDivImperialWeight")
infix operator fun <VolumeUnit : ImperialVolume, WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificVolume(this, weight)
@JvmName("imperialVolumeDivUKImperialWeight")
infix operator fun <VolumeUnit : ImperialVolume, WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificVolume(this, weight)
@JvmName("imperialVolumeDivUSCustomaryWeight")
infix operator fun <VolumeUnit : ImperialVolume, WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificVolume(this, weight)
@JvmName("ukImperialVolumeDivImperialWeight")
infix operator fun <VolumeUnit : UKImperialVolume, WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificVolume(this, weight)
@JvmName("ukImperialVolumeDivUKImperialWeight")
infix operator fun <VolumeUnit : UKImperialVolume, WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificVolume(this, weight)
@JvmName("usCustomaryVolumeDivImperialWeight")
infix operator fun <VolumeUnit : USCustomaryVolume, WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificVolume(this, weight)
@JvmName("usCustomaryVolumeDivUSCustomaryWeight")
infix operator fun <VolumeUnit : USCustomaryVolume, WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit per weight.unit).specificVolume(this, weight)
@JvmName("volumeDivWeight")
infix operator fun <VolumeUnit : Volume, WeightUnit : Weight> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (CubicMeter per Kilogram).specificVolume(this, weight)
