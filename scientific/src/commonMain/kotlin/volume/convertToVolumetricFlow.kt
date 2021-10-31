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

package com.splendo.kaluga.scientific.volume

import com.splendo.kaluga.scientific.CubicMeter
import com.splendo.kaluga.scientific.ImperialVolume
import com.splendo.kaluga.scientific.Volume
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricVolume
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Second
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.UKImperialVolume
import com.splendo.kaluga.scientific.USCustomaryVolume
import com.splendo.kaluga.scientific.per
import com.splendo.kaluga.scientific.volumetricFlow.volumetricFlow
import kotlin.jvm.JvmName

@JvmName("metricVolumeDivTime")
infix operator fun <VolumeUnit : MetricVolume, TimeUnit : Time> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).volumetricFlow(this, time)
@JvmName("imperialVolumeDivTime")
infix operator fun <VolumeUnit : ImperialVolume, TimeUnit : Time> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).volumetricFlow(this, time)
@JvmName("ukImperialVolumeDivTime")
infix operator fun <VolumeUnit : UKImperialVolume, TimeUnit : Time> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).volumetricFlow(this, time)
@JvmName("usCustomaryVolumeDivTime")
infix operator fun <VolumeUnit : USCustomaryVolume, TimeUnit : Time> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).volumetricFlow(this, time)
@JvmName("volumeDivTime")
infix operator fun <VolumeUnit : Volume, TimeUnit : Time> ScientificValue<MeasurementType.Volume, VolumeUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (CubicMeter per Second).volumetricFlow(this, time)
