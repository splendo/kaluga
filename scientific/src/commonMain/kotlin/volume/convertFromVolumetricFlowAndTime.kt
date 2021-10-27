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

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.Volume
import com.splendo.kaluga.scientific.VolumetricFlow
import com.splendo.kaluga.scientific.byMultiplying
import kotlin.jvm.JvmName

@JvmName("distanceFromSpeedAndTime")
fun <
    VolumeUnit : Volume,
    TimeUnit : Time,
    VolumetricFlowUnit : VolumetricFlow
    > VolumeUnit.volume(
    volumetricFlow: ScientificValue<MeasurementType.VolumetricFlow, VolumetricFlowUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = byMultiplying(volumetricFlow, time)