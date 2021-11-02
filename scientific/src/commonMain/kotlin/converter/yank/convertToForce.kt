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

package com.splendo.kaluga.scientific.converter.yank

import com.splendo.kaluga.scientific.ImperialYank
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricYank
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.UKImperialYank
import com.splendo.kaluga.scientific.USCustomaryYank
import com.splendo.kaluga.scientific.Yank
import com.splendo.kaluga.scientific.converter.force.force
import kotlin.jvm.JvmName

@JvmName("metricYankTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Yank, MetricYank>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit.force).force(this, time)
@JvmName("imperialYankTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Yank, ImperialYank>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit.force).force(this, time)
@JvmName("ukImperialYankTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Yank, UKImperialYank>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit.force).force(this, time)
@JvmName("usCustomaryYankTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Yank, USCustomaryYank>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit.force).force(this, time)
@JvmName("yankTimesTime")
infix operator fun <YankUnit : Yank, TimeUnit : Time> ScientificValue<MeasurementType.Yank, YankUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit.force).force(this, time)
