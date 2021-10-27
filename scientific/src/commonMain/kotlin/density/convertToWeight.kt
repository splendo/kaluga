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

package com.splendo.kaluga.scientific.density

import com.splendo.kaluga.scientific.Density
import com.splendo.kaluga.scientific.ImperialDensity
import com.splendo.kaluga.scientific.ImperialVolume
import com.splendo.kaluga.scientific.Kilogram
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricDensity
import com.splendo.kaluga.scientific.MetricVolume
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialDensity
import com.splendo.kaluga.scientific.UKImperialVolume
import com.splendo.kaluga.scientific.USCustomaryDensity
import com.splendo.kaluga.scientific.USCustomaryVolume
import com.splendo.kaluga.scientific.Volume
import com.splendo.kaluga.scientific.weight.mass
import kotlin.jvm.JvmName

@JvmName("metricDensityTimesMetricVolume")
infix operator fun <VolumeUnit : MetricVolume> ScientificValue<MeasurementType.Density, MetricDensity>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = unit.weight.mass(this, volume)
@JvmName("imperialDensityTimesImperialVolume")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Density, ImperialDensity>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = unit.weight.mass(this, volume)
@JvmName("ukImperialDensityTimesUKImperialVolume")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.Density, UKImperialDensity>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = unit.weight.mass(this, volume)
@JvmName("usCustomaryDensityTimesUSCustomaryVolume")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.Density, USCustomaryDensity>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = unit.weight.mass(this, volume)
@JvmName("densityTimesVolume")
infix operator fun <DensityUnit : Density, VolumeUnit : Volume> ScientificValue<MeasurementType.Density, DensityUnit>.times(volume: ScientificValue<MeasurementType.Volume, VolumeUnit>) = Kilogram.mass(this, volume)
