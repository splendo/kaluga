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

package com.splendo.kaluga.scientific.converter.jolt

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.snap.snap
import com.splendo.kaluga.scientific.unit.ImperialJolt
import com.splendo.kaluga.scientific.unit.Jolt
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricAndImperialJolt
import com.splendo.kaluga.scientific.unit.MetricJolt
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricAndImperialJoltDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Jolt, MetricAndImperialJolt>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit per time.unit).snap(this, time)

@JvmName("metricJoltDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Jolt, MetricJolt>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit per time.unit).snap(this, time)

@JvmName("imperialJoltDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Jolt, ImperialJolt>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit per time.unit).snap(this, time)

@JvmName("joltDivTime")
infix operator fun <JoltUnit : Jolt, TimeUnit : Time> ScientificValue<PhysicalQuantity.Jolt, JoltUnit>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (Meter per Second per Second per Second per time.unit).snap(this, time)
