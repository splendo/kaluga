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

package com.splendo.kaluga.scientific.converter.specificWeight

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.force.mass
import com.splendo.kaluga.scientific.unit.SpecificWeight
import com.splendo.kaluga.scientific.unit.ImperialSpecificWeight
import com.splendo.kaluga.scientific.unit.ImperialVolume
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.MetricSpecificWeight
import com.splendo.kaluga.scientific.unit.MetricVolume
import com.splendo.kaluga.scientific.unit.UKImperialSpecificWeight
import com.splendo.kaluga.scientific.unit.UKImperialVolume
import com.splendo.kaluga.scientific.unit.USCustomarySpecificWeight
import com.splendo.kaluga.scientific.unit.USCustomaryVolume
import com.splendo.kaluga.scientific.unit.Volume
import kotlin.jvm.JvmName

@JvmName("metricSpecificWeightTimesMetricVolume")
infix operator fun <VolumeUnit : MetricVolume> ScientificValue<PhysicalQuantity.SpecificWeight, MetricSpecificWeight>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = unit.force.mass(this, volume)

@JvmName("imperialSpecificWeightTimesImperialVolume")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.SpecificWeight, ImperialSpecificWeight>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = unit.force.mass(this, volume)

@JvmName("imperialSpecificWeightTimesUKImperialVolume")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<PhysicalQuantity.SpecificWeight, ImperialSpecificWeight>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = unit.force.mass(this, volume)

@JvmName("imperialSpecificWeightTimesUSCustomaryVolume")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<PhysicalQuantity.SpecificWeight, ImperialSpecificWeight>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = unit.force.mass(this, volume)

@JvmName("ukImperialSpecificWeightTimesImperialVolume")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.SpecificWeight, UKImperialSpecificWeight>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = unit.force.mass(this, volume)

@JvmName("ukImperialSpecificWeightTimesUKImperialVolume")
infix operator fun <VolumeUnit : UKImperialVolume> ScientificValue<PhysicalQuantity.SpecificWeight, UKImperialSpecificWeight>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = unit.force.mass(this, volume)

@JvmName("usCustomarySpecificWeightTimesUKImperialVolume")
infix operator fun <VolumeUnit : ImperialVolume> ScientificValue<PhysicalQuantity.SpecificWeight, USCustomarySpecificWeight>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = unit.force.mass(this, volume)

@JvmName("usCustomarySpecificWeightTimesUSCustomaryVolume")
infix operator fun <VolumeUnit : USCustomaryVolume> ScientificValue<PhysicalQuantity.SpecificWeight, USCustomarySpecificWeight>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = unit.force.mass(this, volume)

@JvmName("specificWeightTimesVolume")
infix operator fun <SpecificWeightUnit : SpecificWeight, VolumeUnit : Volume> ScientificValue<PhysicalQuantity.SpecificWeight, SpecificWeightUnit>.times(
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = Newton.mass(this, volume)
