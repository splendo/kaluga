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
import com.splendo.kaluga.scientific.converter.specificWeight.times
import com.splendo.kaluga.scientific.unit.ImperialSpecificWeight
import com.splendo.kaluga.scientific.unit.ImperialVolume
import com.splendo.kaluga.scientific.unit.MetricSpecificWeight
import com.splendo.kaluga.scientific.unit.MetricVolume
import com.splendo.kaluga.scientific.unit.SpecificWeight
import com.splendo.kaluga.scientific.unit.UKImperialSpecificWeight
import com.splendo.kaluga.scientific.unit.UKImperialVolume
import com.splendo.kaluga.scientific.unit.USCustomarySpecificWeight
import com.splendo.kaluga.scientific.unit.USCustomaryVolume
import com.splendo.kaluga.scientific.unit.Volume
import kotlin.jvm.JvmName

@JvmName("metricVolumeTimesMetricSpecificWeight")
infix operator fun <VolumeUnit : MetricVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.times(
    specificWeight: ScientificValue<PhysicalQuantity.SpecificWeight, MetricSpecificWeight>,
) = specificWeight * this

@JvmName("imperialVolumeTimesImperialSpecificWeight")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.times(
    specificWeight: ScientificValue<PhysicalQuantity.SpecificWeight, ImperialSpecificWeight>,
) = specificWeight * this

@JvmName("imperialVolumeTimesUKImperialSpecificWeight")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.times(
    specificWeight: ScientificValue<PhysicalQuantity.SpecificWeight, UKImperialSpecificWeight>,
) = specificWeight * this

@JvmName("imperialVolumeTimesUSCustomarySpecificWeight")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.times(
    specificWeight: ScientificValue<PhysicalQuantity.SpecificWeight, USCustomarySpecificWeight>,
) = specificWeight * this

@JvmName("ukImperialVolumeTimesImperialSpecificWeight")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.times(
    specificWeight: ScientificValue<PhysicalQuantity.SpecificWeight, ImperialSpecificWeight>,
) = specificWeight * this

@JvmName("ukImperialVolumeTimesUKImperialSpecificWeight")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.times(
    specificWeight: ScientificValue<PhysicalQuantity.SpecificWeight, UKImperialSpecificWeight>,
) = specificWeight * this

@JvmName("usCustomaryVolumeTimesImperialSpecificWeight")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.times(
    specificWeight: ScientificValue<PhysicalQuantity.SpecificWeight, ImperialSpecificWeight>,
) = specificWeight * this

@JvmName("usCustomaryVolumeTimesUSCustomarySpecificWeight")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.times(
    specificWeight: ScientificValue<PhysicalQuantity.SpecificWeight, USCustomarySpecificWeight>,
) = specificWeight * this

@JvmName("volumeTimesSpecificWeight")
infix operator fun <SpecificWeightUnit : SpecificWeight, VolumeUnit : Volume> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.times(
    specificWeight: ScientificValue<PhysicalQuantity.SpecificWeight, SpecificWeightUnit>,
) = specificWeight * this
