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

package com.splendo.kaluga.scientific.length

import com.splendo.kaluga.scientific.ImperialLength
import com.splendo.kaluga.scientific.Length
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Meter
import com.splendo.kaluga.scientific.MetricLength
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Second
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.per
import com.splendo.kaluga.scientific.speed.speed
import kotlin.jvm.JvmName

@JvmName("metricLengthDivTime")
infix operator fun <LengthUnit : MetricLength, TimeUnit : Time> ScientificValue<MeasurementType.Length, LengthUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).speed(this, time)
@JvmName("imperialLengthDivTime")
infix operator fun <LengthUnit : ImperialLength, TimeUnit : Time> ScientificValue<MeasurementType.Length, LengthUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).speed(this, time)
@JvmName("lengthDivTime")
infix operator fun <LengthUnit : Length, TimeUnit : Time> ScientificValue<MeasurementType.Length, LengthUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Meter per Second).speed(this, time)
