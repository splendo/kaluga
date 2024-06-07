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

package com.splendo.kaluga.scientific.converter.action

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.energy.energy
import com.splendo.kaluga.scientific.unit.Action
import com.splendo.kaluga.scientific.unit.ImperialAction
import com.splendo.kaluga.scientific.unit.MetricAction
import com.splendo.kaluga.scientific.unit.MetricAndImperialAction
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("metricAndImperialActionDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Action, MetricAndImperialAction>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit.energy).energy(this, time)

@JvmName("metricActionDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Action, MetricAction>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit.energy).energy(this, time)

@JvmName("imperialActionDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Action, ImperialAction>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit.energy).energy(this, time)

@JvmName("actionDivTime")
infix operator fun <ActionUnit : Action, TimeUnit : Time> ScientificValue<PhysicalQuantity.Action, ActionUnit>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit.energy).energy(this, time)
