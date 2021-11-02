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

package com.splendo.kaluga.scientific.converter.specificVolume

import com.splendo.kaluga.scientific.CubicMeter
import com.splendo.kaluga.scientific.ImperialSpecificVolume
import com.splendo.kaluga.scientific.ImperialWeight
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricSpecificVolume
import com.splendo.kaluga.scientific.MetricWeight
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SpecificVolume
import com.splendo.kaluga.scientific.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.UKImperialWeight
import com.splendo.kaluga.scientific.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.USCustomaryWeight
import com.splendo.kaluga.scientific.Weight
import com.splendo.kaluga.scientific.converter.volume.volume
import kotlin.jvm.JvmName

@JvmName("metricSpecificVolumeTimesMetricWeight")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = unit.volume.volume(this, weight)
@JvmName("imperialSpecificVolumeTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = unit.volume.volume(this, weight)
@JvmName("ukImperialSpecificVolumeTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = unit.volume.volume(this, weight)
@JvmName("ukImperialSpecificVolumeTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = unit.volume.volume(this, weight)
@JvmName("usCustomarySpecificVolumeTimesUKImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = unit.volume.volume(this, weight)
@JvmName("usCustomarySpecificVolumeTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = unit.volume.volume(this, weight)
@JvmName("specificVolumeTimesWeight")
infix operator fun <SpecificVolumeUnit : SpecificVolume, WeightUnit : Weight> ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = CubicMeter.volume(this, weight)
