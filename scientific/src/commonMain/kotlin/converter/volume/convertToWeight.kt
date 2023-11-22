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
import com.splendo.kaluga.scientific.converter.density.times
import com.splendo.kaluga.scientific.converter.weight.weight
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.ImperialVolume
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricSpecificVolume
import com.splendo.kaluga.scientific.unit.MetricVolume
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialVolume
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.unit.USCustomaryVolume
import com.splendo.kaluga.scientific.unit.Volume
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.jvm.JvmName

@JvmName("metricVolumeTimesMetricDensity")
infix operator fun <VolumeUnit : MetricVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.times(density: ScientificValue<PhysicalQuantity.Density, MetricDensity>) =
    density * this

@JvmName("imperialVolumeTimesImperialDensity")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.times(density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>) =
    density * this

@JvmName("imperialVolumeTimesUKImperialDensity")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.times(
    density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>,
) = density * this

@JvmName("imperialVolumeTimesUSCustomaryDensity")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.times(
    density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>,
) = density * this

@JvmName("ukImperialVolumeTimesImperialDensity")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.times(
    density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>,
) = density * this

@JvmName("ukImperialVolumeTimesUKImperialDensity")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.times(
    density: ScientificValue<PhysicalQuantity.Density, UKImperialDensity>,
) = density * this

@JvmName("usCustomaryVolumeTimesImperialDensity")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.times(
    density: ScientificValue<PhysicalQuantity.Density, ImperialDensity>,
) = density * this

@JvmName("usCustomaryVolumeTimesUSCustomaryDensity")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.times(
    density: ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>,
) = density * this

@JvmName("volumeTimesDensity")
infix operator fun <DensityUnit : Density, VolumeUnit : Volume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.times(
    density: ScientificValue<PhysicalQuantity.Density, DensityUnit>,
) = density * this

@JvmName("metricVolumeDivMetricSpecificVolume")
infix operator fun <VolumeUnit : MetricVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, MetricSpecificVolume>,
) = specificVolume.unit.per.weight(this, specificVolume)

@JvmName("imperialVolumeDivImperialSpecificVolume")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>,
) = specificVolume.unit.per.weight(this, specificVolume)

@JvmName("imperialVolumeDivUKImperialSpecificVolume")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>,
) = specificVolume.unit.per.weight(this, specificVolume)

@JvmName("imperialVolumeDivUSCustomarySpecificVolume")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>,
) = specificVolume.unit.per.weight(this, specificVolume)

@JvmName("ukImperialVolumeDivImperialSpecificVolume")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>,
) = specificVolume.unit.per.ukImperial.weight(this, specificVolume)

@JvmName("ukImperialVolumeDivUKImperialSpecificVolume")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>,
) = specificVolume.unit.per.weight(this, specificVolume)

@JvmName("usCustomaryVolumeDivImperialSpecificVolume")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>,
) = specificVolume.unit.per.usCustomary.weight(this, specificVolume)

@JvmName("usCustomaryVolumeDivUSCustomarySpecificVolume")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>,
) = specificVolume.unit.per.weight(this, specificVolume)

@JvmName("volumeDivSpecificVolume")
infix operator fun <VolumeUnit : Volume, SpecificVolumeUnit : SpecificVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, SpecificVolumeUnit>,
) = Kilogram.weight(this, specificVolume)
