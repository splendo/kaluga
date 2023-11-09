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

package com.splendo.kaluga.scientific.converter.length

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.speed.speed
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricLengthDivTime")
infix operator fun <LengthUnit : MetricLength, TimeUnit : Time> ScientificValue<PhysicalQuantity.Length, LengthUnit>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit per time.unit).speed(this, time)

@JvmName("imperialLengthDivTime")
infix operator fun <LengthUnit : ImperialLength, TimeUnit : Time> ScientificValue<PhysicalQuantity.Length, LengthUnit>.div(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = (unit per time.unit).speed(this, time)

@JvmName("lengthDivTime")
infix operator fun <LengthUnit : Length, TimeUnit : Time> ScientificValue<PhysicalQuantity.Length, LengthUnit>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (Meter per Second).speed(this, time)
