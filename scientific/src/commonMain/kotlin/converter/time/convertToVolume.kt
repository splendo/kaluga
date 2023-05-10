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

package com.splendo.kaluga.scientific.converter.time

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.volumetricFlow.times
import com.splendo.kaluga.scientific.unit.ImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.MetricVolumetricFlow
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.UKImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.USCustomaryVolumetricFlow
import com.splendo.kaluga.scientific.unit.VolumetricFlow
import kotlin.jvm.JvmName

@JvmName("timeTimesMetricVolumetricFlow")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, MetricVolumetricFlow>,
) = volumetricFlow * this

@JvmName("timeTimesImperialVolumetricFlow")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>,
) = volumetricFlow * this

@JvmName("timeTimesUKImperialVolumetricFlow")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>,
) = volumetricFlow * this

@JvmName("timeTimesUSCustomaryVolumetricFlow")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>,
) = volumetricFlow * this

@JvmName("timeTimesVolumetricFlow")
infix operator fun <VolumetricFlowUnit : VolumetricFlow, TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(
    volumetricFlow: ScientificValue<PhysicalQuantity.VolumetricFlow, VolumetricFlowUnit>,
) = volumetricFlow * this
