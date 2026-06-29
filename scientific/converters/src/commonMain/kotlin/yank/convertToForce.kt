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

package com.splendo.kaluga.scientific.converter.yank

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.force.force
import com.splendo.kaluga.scientific.unit.ImperialYank
import com.splendo.kaluga.scientific.unit.MetricYank
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.UKImperialYank
import com.splendo.kaluga.scientific.unit.USCustomaryYank
import com.splendo.kaluga.scientific.unit.Yank
import kotlin.jvm.JvmName

@JvmName("metricYankTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Yank, MetricYank>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit.force).force(this, time)

@JvmName("imperialYankTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Yank, ImperialYank>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit.force).force(this, time)

@JvmName("ukImperialYankTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Yank, UKImperialYank>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit.force).force(this, time)

@JvmName("usCustomaryYankTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Yank, USCustomaryYank>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit.force).force(this, time)

@JvmName("yankTimesTime")
infix operator fun <YankUnit : Yank, TimeUnit : Time> ScientificValue<PhysicalQuantity.Yank, YankUnit>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit.force).force(this, time)
