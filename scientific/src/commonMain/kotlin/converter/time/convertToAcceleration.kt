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
import com.splendo.kaluga.scientific.converter.jolt.times
import com.splendo.kaluga.scientific.unit.ImperialJolt
import com.splendo.kaluga.scientific.unit.Jolt
import com.splendo.kaluga.scientific.unit.MetricAndImperialJolt
import com.splendo.kaluga.scientific.unit.MetricJolt
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("timeTimesMetricAndImperialJolt")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(jolt: ScientificValue<PhysicalQuantity.Jolt, MetricAndImperialJolt>) = jolt * this

@JvmName("timeTimesMetricJolt")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(jolt: ScientificValue<PhysicalQuantity.Jolt, MetricJolt>) = jolt * this

@JvmName("timeTimesImperialJolt")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(jolt: ScientificValue<PhysicalQuantity.Jolt, ImperialJolt>) = jolt * this

@JvmName("timeTimesJolt")
infix operator fun <JoltUnit : Jolt, TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(jolt: ScientificValue<PhysicalQuantity.Jolt, JoltUnit>) = jolt * this
