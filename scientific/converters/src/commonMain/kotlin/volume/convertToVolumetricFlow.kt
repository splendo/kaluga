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
import com.splendo.kaluga.scientific.converter.volumetricFlow.volumetricFlow
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialVolume
import com.splendo.kaluga.scientific.unit.MetricVolume
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.UKImperialVolume
import com.splendo.kaluga.scientific.unit.USCustomaryVolume
import com.splendo.kaluga.scientific.unit.Volume
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricVolumeDivTime")
infix operator fun <VolumeUnit : MetricVolume, TimeUnit : Time> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit per time.unit).volumetricFlow(this, time)

@JvmName("imperialVolumeDivTime")
infix operator fun <VolumeUnit : ImperialVolume, TimeUnit : Time> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = (unit per time.unit).volumetricFlow(this, time)

@JvmName("ukImperialVolumeDivTime")
infix operator fun <VolumeUnit : UKImperialVolume, TimeUnit : Time> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = (unit per time.unit).volumetricFlow(this, time)

@JvmName("usCustomaryVolumeDivTime")
infix operator fun <VolumeUnit : USCustomaryVolume, TimeUnit : Time> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = (unit per time.unit).volumetricFlow(this, time)

@JvmName("volumeDivTime")
infix operator fun <VolumeUnit : Volume, TimeUnit : Time> ScientificValue<PhysicalQuantity.Volume, VolumeUnit>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (CubicMeter per Second).volumetricFlow(this, time)
