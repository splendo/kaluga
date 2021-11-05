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

package com.splendo.kaluga.scientific.converter.acceleration

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.speed.speed
import com.splendo.kaluga.scientific.unit.Acceleration
import com.splendo.kaluga.scientific.unit.ImperialAcceleration
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricAcceleration
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricAccelerationTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Acceleration, MetricAcceleration>.times(
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = unit.speed.speed(this, time)

@JvmName("imperialAccelerationTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Acceleration, ImperialAcceleration>.times(
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = unit.speed.speed(this, time)

@JvmName("speedTimesTime")
infix operator fun <AccelerationUnit : Acceleration, TimeUnit : Time> ScientificValue<MeasurementType.Acceleration, AccelerationUnit>.times(
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = (Meter per Second).speed(this, time)
