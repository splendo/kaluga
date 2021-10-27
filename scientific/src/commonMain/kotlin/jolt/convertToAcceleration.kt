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

package com.splendo.kaluga.scientific.jolt

import com.splendo.kaluga.scientific.Acceleration
import com.splendo.kaluga.scientific.ImperialAcceleration
import com.splendo.kaluga.scientific.ImperialJolt
import com.splendo.kaluga.scientific.Jolt
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricAcceleration
import com.splendo.kaluga.scientific.MetricJolt
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.acceleration.acceleration
import kotlin.jvm.JvmName

@JvmName("metricJoltTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Jolt, MetricJolt>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) : ScientificValue<MeasurementType.Acceleration, MetricAcceleration> = (unit.acceleration).acceleration(this, time)
@JvmName("imperialJoltTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Jolt, ImperialJolt>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) : ScientificValue<MeasurementType.Acceleration, ImperialAcceleration> = (unit.acceleration).acceleration(this, time)
@JvmName("speedTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Jolt, Jolt>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) : ScientificValue<MeasurementType.Acceleration, Acceleration> = (unit.acceleration).acceleration(this, time)
