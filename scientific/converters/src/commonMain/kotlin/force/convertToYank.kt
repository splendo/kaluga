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

package com.splendo.kaluga.scientific.converter.force

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.yank.yank
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.MetricForce
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricForceDivTime")
infix operator fun <ForceUnit : MetricForce, TimeUnit : Time> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit per time.unit).yank(this, time)

@JvmName("imperialForceDivTime")
infix operator fun <ForceUnit : ImperialForce, TimeUnit : Time> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit per time.unit).yank(this, time)

@JvmName("ukImperialForceDivTime")
infix operator fun <ForceUnit : UKImperialForce, TimeUnit : Time> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit per time.unit).yank(this, time)

@JvmName("usCustomaryForceDivTime")
infix operator fun <ForceUnit : USCustomaryForce, TimeUnit : Time> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit per time.unit).yank(this, time)

@JvmName("forceDivTime")
infix operator fun <ForceUnit : Force, TimeUnit : Time> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (Newton per time.unit).yank(this, time)
