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

package com.splendo.kaluga.scientific.weight

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
import com.splendo.kaluga.scientific.density.density
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("metricWeightDivMetricVolume")
infix operator fun <WeightUnit : MetricWeight, VolumeUnit : MetricVolume> ScientificValue<MeasurementType.Weight, WeightUnit>.div(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = (unit per volume.unit).density(this, volume)
@JvmName("imperialWeightDivImperialVolume")
infix operator fun <WeightUnit : ImperialWeight, VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Weight, WeightUnit>.div(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = (unit per volume.unit).density(this, volume)
@JvmName("imperialWeightDivUKImperialVolume")
infix operator fun <WeightUnit : ImperialWeight, VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.Weight, WeightUnit>.div(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = (unit per volume.unit).density(this, volume)
@JvmName("imperialWeightDivUSCustomaryVolume")
infix operator fun <WeightUnit : ImperialWeight, VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.Weight, WeightUnit>.div(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = (unit per volume.unit).density(this, volume)
@JvmName("ukImperialWeightDivImperialVolume")
infix operator fun <WeightUnit : UKImperialWeight, VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Weight, WeightUnit>.div(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = (unit per volume.unit).density(this, volume)
@JvmName("ukImperialWeightDivUKImperialVolume")
infix operator fun <WeightUnit : UKImperialWeight, VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.Weight, WeightUnit>.div(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = (unit per volume.unit).density(this, volume)
@JvmName("usCustomaryWeightDivImperialVolume")
infix operator fun <WeightUnit : USCustomaryWeight, VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Weight, WeightUnit>.div(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = (unit per volume.unit).density(this, volume)
@JvmName("usCustomaryWeightDivUSCustomaryVolume")
infix operator fun <WeightUnit : USCustomaryWeight, VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.Weight, WeightUnit>.div(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = (unit per volume.unit).density(this, volume)
@JvmName("weightDivVolume")
infix operator fun <WeightUnit : Weight, VolumeUnit : Volume> ScientificValue<MeasurementType.Weight, WeightUnit>.div(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = (Kilogram per CubicMeter).density(this, volume)
