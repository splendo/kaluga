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

package com.splendo.kaluga.scientific.converter.jolt

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.acceleration.acceleration
import com.splendo.kaluga.scientific.unit.Acceleration
import com.splendo.kaluga.scientific.unit.ImperialAcceleration
import com.splendo.kaluga.scientific.unit.ImperialJolt
import com.splendo.kaluga.scientific.unit.Jolt
import com.splendo.kaluga.scientific.unit.MetricAcceleration
import com.splendo.kaluga.scientific.unit.MetricJolt
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("metricJoltTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Jolt, MetricJolt>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>): ScientificValue<MeasurementType.Acceleration, MetricAcceleration> =
    (unit.acceleration).acceleration(this, time)

@JvmName("imperialJoltTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Jolt, ImperialJolt>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>): ScientificValue<MeasurementType.Acceleration, ImperialAcceleration> =
    (unit.acceleration).acceleration(this, time)

@JvmName("speedTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Jolt, Jolt>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>): ScientificValue<MeasurementType.Acceleration, Acceleration> =
    (unit.acceleration).acceleration(this, time)
