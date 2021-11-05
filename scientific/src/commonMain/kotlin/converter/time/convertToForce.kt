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

package com.splendo.kaluga.scientific.converter.time

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.yank.times
import com.splendo.kaluga.scientific.unit.ImperialYank
import com.splendo.kaluga.scientific.unit.MetricYank
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.UKImperialYank
import com.splendo.kaluga.scientific.unit.USCustomaryYank
import com.splendo.kaluga.scientific.unit.Yank
import kotlin.jvm.JvmName

@JvmName("timeTimesMetricYank")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(yank: ScientificValue<MeasurementType.Yank, MetricYank>) =
    yank * this

@JvmName("timeTimesImperialYank")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(yank: ScientificValue<MeasurementType.Yank, ImperialYank>) =
    yank * this

@JvmName("timeTimesUKImperialYank")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(yank: ScientificValue<MeasurementType.Yank, UKImperialYank>) =
    yank * this

@JvmName("timeTimesUSCustomaryYank")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(yank: ScientificValue<MeasurementType.Yank, USCustomaryYank>) =
    yank * this

@JvmName("timeTimesYank")
infix operator fun <YankUnit : Yank, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(
    yank: ScientificValue<MeasurementType.Yank, YankUnit>
) = yank * this
