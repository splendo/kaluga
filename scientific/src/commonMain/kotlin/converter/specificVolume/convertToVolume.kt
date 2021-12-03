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

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.volume.volume
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.MetricSpecificVolume
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialSpecificVolume
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomarySpecificVolume
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import kotlin.jvm.JvmName

@JvmName("metricSpecificVolumeTimesMetricWeight")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<PhysicalQuantity.SpecificVolume, MetricSpecificVolume>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = unit.volume.volume(this, weight)

@JvmName("imperialSpecificVolumeTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = unit.volume.volume(this, weight)

@JvmName("imperialSpecificVolumeTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = unit.volume.volume(this, weight)

@JvmName("imperialSpecificVolumeTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.SpecificVolume, ImperialSpecificVolume>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = unit.volume.volume(this, weight)

@JvmName("ukImperialSpecificVolumeTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = unit.volume.volume(this, weight)

@JvmName("ukImperialSpecificVolumeTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.SpecificVolume, UKImperialSpecificVolume>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = unit.volume.volume(this, weight)

@JvmName("usCustomarySpecificVolumeTimesUKImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = unit.volume.volume(this, weight)

@JvmName("usCustomarySpecificVolumeTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.SpecificVolume, USCustomarySpecificVolume>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = unit.volume.volume(this, weight)

@JvmName("specificVolumeTimesWeight")
infix operator fun <SpecificVolumeUnit : SpecificVolume, WeightUnit : Weight> ScientificValue<PhysicalQuantity.SpecificVolume, SpecificVolumeUnit>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = CubicMeter.volume(this, weight)
