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

package com.splendo.kaluga.scientific.converter.density

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.weight.mass
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.ImperialVolume
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricDensity
import com.splendo.kaluga.scientific.unit.MetricVolume
import com.splendo.kaluga.scientific.unit.UKImperialDensity
import com.splendo.kaluga.scientific.unit.UKImperialVolume
import com.splendo.kaluga.scientific.unit.USCustomaryDensity
import com.splendo.kaluga.scientific.unit.USCustomaryVolume
import com.splendo.kaluga.scientific.unit.Volume
import kotlin.jvm.JvmName

@JvmName("metricDensityTimesMetricVolume")
infix operator fun <VolumeUnit : MetricVolume> ScientificValue<PhysicalQuantity.Density, MetricDensity>.times(volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>) =
    unit.weight.mass(this, volume)

@JvmName("imperialDensityTimesImperialVolume")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Density, ImperialDensity>.times(volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>) =
    unit.weight.mass(this, volume)

@JvmName("imperialDensityTimesUKImperialVolume")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<PhysicalQuantity.Density, ImperialDensity>.times(volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>) =
    unit.weight.mass(this, volume)

@JvmName("imperialDensityTimesUSCustomaryVolume")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<PhysicalQuantity.Density, ImperialDensity>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = unit.weight.mass(this, volume)

@JvmName("ukImperialDensityTimesImperialVolume")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Density, UKImperialDensity>.times(volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>) =
    unit.weight.mass(this, volume)

@JvmName("ukImperialDensityTimesUKImperialVolume")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<PhysicalQuantity.Density, UKImperialDensity>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = unit.weight.mass(this, volume)

@JvmName("usCustomaryDensityTimesUKImperialVolume")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = unit.weight.mass(this, volume)

@JvmName("usCustomaryDensityTimesUSCustomaryVolume")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<PhysicalQuantity.Density, USCustomaryDensity>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = unit.weight.mass(this, volume)

@JvmName("densityTimesVolume")
infix operator fun <DensityUnit : Density, VolumeUnit : Volume> ScientificValue<PhysicalQuantity.Density, DensityUnit>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = Kilogram.mass(this, volume)
