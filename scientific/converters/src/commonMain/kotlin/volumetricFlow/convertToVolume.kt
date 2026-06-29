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

package com.splendo.kaluga.scientific.converter.volumetricFlow

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.volume.volume
import com.splendo.kaluga.scientific.unit.ImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.MetricVolumetricFlow
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.UKImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.USCustomaryVolumetricFlow
import com.splendo.kaluga.scientific.unit.VolumetricFlow
import kotlin.jvm.JvmName

@JvmName("metricVolumetricFlowTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.VolumetricFlow, MetricVolumetricFlow>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    unit.volume.volume(this, time)

@JvmName("imperialVolumetricFlowTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    unit.volume.volume(this, time)

@JvmName("ukImperialVolumetricFlowTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    unit.volume.volume(this, time)

@JvmName("usCustomaryVolumetricFlowTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    unit.volume.volume(this, time)

@JvmName("volumetricFlowTimesTime")
infix operator fun <VolumetricFlowUnit : VolumetricFlow, TimeUnit : Time> ScientificValue<PhysicalQuantity.VolumetricFlow, VolumetricFlowUnit>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = unit.volume.volume(this, time)
