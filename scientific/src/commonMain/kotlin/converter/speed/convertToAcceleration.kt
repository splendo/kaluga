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

package com.splendo.kaluga.scientific.converter.speed

import com.splendo.kaluga.scientific.ImperialSpeed
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Meter
import com.splendo.kaluga.scientific.MetricSpeed
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Second
import com.splendo.kaluga.scientific.Speed
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.converter.acceleration.acceleration
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("metricSpeedDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Speed, MetricSpeed>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).acceleration(this, time)
@JvmName("imperialSpeedDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Speed, ImperialSpeed>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).acceleration(this, time)
@JvmName("speedDivTime")
infix operator fun <SpeedUnit : Speed, TimeUnit : Time> ScientificValue<MeasurementType.Speed, SpeedUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Meter per Second per time.unit).acceleration(this, time)
