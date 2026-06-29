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

package com.splendo.kaluga.scientific.converter.speed

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.length.distance
import com.splendo.kaluga.scientific.unit.ImperialSpeed
import com.splendo.kaluga.scientific.unit.MetricSpeed
import com.splendo.kaluga.scientific.unit.Speed
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("metricSpeedTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Speed, MetricSpeed>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    unit.distance.distance(this, time)

@JvmName("imperialSpeedTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    unit.distance.distance(this, time)

@JvmName("speedTimesTime")
infix operator fun <SpeedUnit : Speed, TimeUnit : Time> ScientificValue<PhysicalQuantity.Speed, SpeedUnit>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    unit.distance.distance(this, time)
