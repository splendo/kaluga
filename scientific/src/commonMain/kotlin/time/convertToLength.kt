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

package com.splendo.kaluga.scientific.time

import com.splendo.kaluga.scientific.ImperialLength
import com.splendo.kaluga.scientific.ImperialSpeed
import com.splendo.kaluga.scientific.Length
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricLength
import com.splendo.kaluga.scientific.MetricSpeed
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Speed
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.speed.times
import kotlin.jvm.JvmName

@JvmName("timeTimesMetricSpeed")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(speed: ScientificValue<MeasurementType.Speed, MetricSpeed>) : ScientificValue<MeasurementType.Length, MetricLength> = speed * this
@JvmName("timeTimesImperialSpeed")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) : ScientificValue<MeasurementType.Length, ImperialLength> = speed * this
@JvmName("timeTimesSpeed")
infix operator fun <SpeedUnit : Speed, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(speed: ScientificValue<MeasurementType.Speed, SpeedUnit>) : ScientificValue<MeasurementType.Length, Length> = speed * this
