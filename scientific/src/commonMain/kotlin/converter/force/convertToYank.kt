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

package com.splendo.kaluga.scientific.converter.force

import com.splendo.kaluga.scientific.Force
import com.splendo.kaluga.scientific.ImperialForce
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricForce
import com.splendo.kaluga.scientific.Newton
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.UKImperialForce
import com.splendo.kaluga.scientific.USCustomaryForce
import com.splendo.kaluga.scientific.converter.yank.yank
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("metricForceDivTime")
infix operator fun <ForceUnit : MetricForce, TimeUnit : Time> ScientificValue<MeasurementType.Force, ForceUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).yank(this, time)
@JvmName("imperialForceDivTime")
infix operator fun <ForceUnit : ImperialForce, TimeUnit : Time> ScientificValue<MeasurementType.Force, ForceUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).yank(this, time)
@JvmName("ukImperialForceDivTime")
infix operator fun <ForceUnit : UKImperialForce, TimeUnit : Time> ScientificValue<MeasurementType.Force, ForceUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).yank(this, time)
@JvmName("usCustomaryForceDivTime")
infix operator fun <ForceUnit : USCustomaryForce, TimeUnit : Time> ScientificValue<MeasurementType.Force, ForceUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).yank(this, time)
@JvmName("forceDivTime")
infix operator fun <ForceUnit : Force, TimeUnit : Time> ScientificValue<MeasurementType.Force, ForceUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Newton per time.unit).yank(this, time)
