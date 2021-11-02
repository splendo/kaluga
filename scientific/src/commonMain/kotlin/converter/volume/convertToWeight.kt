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

import com.splendo.kaluga.scientific.Density
import com.splendo.kaluga.scientific.ImperialDensity
import com.splendo.kaluga.scientific.ImperialSpecificVolume
import com.splendo.kaluga.scientific.ImperialVolume
import com.splendo.kaluga.scientific.Kilogram
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricDensity
import com.splendo.kaluga.scientific.MetricSpecificVolume
import com.splendo.kaluga.scientific.MetricVolume
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SpecificVolume
import com.splendo.kaluga.scientific.UKImperialDensity
import com.splendo.kaluga.scientific.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.UKImperialVolume
import com.splendo.kaluga.scientific.USCustomaryDensity
import com.splendo.kaluga.scientific.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.USCustomaryVolume
import com.splendo.kaluga.scientific.Volume
import com.splendo.kaluga.scientific.converter.density.times
import com.splendo.kaluga.scientific.converter.weight.weight
import kotlin.jvm.JvmName

@JvmName("metricVolumeTimesMetricDensity")
infix operator fun <VolumeUnit : MetricVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(density: ScientificValue<MeasurementType.Density, MetricDensity>) = density * this
@JvmName("imperialVolumeTimesImperialDensity")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(density: ScientificValue<MeasurementType.Density, ImperialDensity>) = density * this
@JvmName("imperialVolumeTimesUKImperialDensity")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) = density * this
@JvmName("imperialVolumeTimesUSCustomaryDensity")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) = density * this
@JvmName("ukImperialVolumeTimesUKImperialDensity")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(density: ScientificValue<MeasurementType.Density, UKImperialDensity>) = density * this
@JvmName("usCustomaryVolumeTimesUSCustomaryDensity")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(density: ScientificValue<MeasurementType.Density, USCustomaryDensity>) = density * this
@JvmName("volumeTimesDensity")
infix operator fun <DensityUnit : Density, VolumeUnit : Volume> ScientificValue<MeasurementType.Volume, VolumeUnit>.times(density: ScientificValue<MeasurementType.Density, DensityUnit>) = density * this

@JvmName("metricVolumeDivMetricSpecificVolume")
infix operator fun <VolumeUnit : MetricVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, MetricSpecificVolume>) = specificVolume.unit.per.weight(this, specificVolume)
@JvmName("imperialVolumeDivImperialSpecificVolume")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>) = specificVolume.unit.per.weight(this, specificVolume)
@JvmName("ukImperialVolumeDivImperialSpecificVolume")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>) = specificVolume.unit.per.weight(this, specificVolume)
@JvmName("ukImperialVolumeDivUKImperialSpecificVolume")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, UKImperialSpecificVolume>) = specificVolume.unit.per.weight(this, specificVolume)
@JvmName("usCustomaryVolumeDivImperialSpecificVolume")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, ImperialSpecificVolume>) = specificVolume.unit.per.weight(this, specificVolume)
@JvmName("usCustomaryVolumeDivUSCustomarySpecificVolume")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, USCustomarySpecificVolume>) = specificVolume.unit.per.weight(this, specificVolume)
@JvmName("volumeDivSpecificVolume")
infix operator fun <VolumeUnit : Volume, SpecificVolumeUnit : SpecificVolume> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(specificVolume: ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>) = Kilogram.weight(this, specificVolume)
