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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.Acceleration
import com.splendo.kaluga.scientific.unit.Jolt
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("joltFromAccelerationAndTimeDefault")
fun <
    AccelerationUnit : Acceleration,
    TimeUnit : Time,
    JoltUnit : Jolt
    > JoltUnit.jolt(
    acceleration: ScientificValue<MeasurementType.Acceleration, AccelerationUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = jolt(acceleration, time, ::DefaultScientificValue)

@JvmName("joltFromAccelerationAndTime")
fun <
    AccelerationUnit : Acceleration,
    TimeUnit : Time,
    JoltUnit : Jolt,
    Value : ScientificValue<MeasurementType.Jolt, JoltUnit>
    > JoltUnit.jolt(
    acceleration: ScientificValue<MeasurementType.Acceleration, AccelerationUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>,
    factory: (Decimal, JoltUnit) -> Value
) = byDividing(acceleration, time, factory)
