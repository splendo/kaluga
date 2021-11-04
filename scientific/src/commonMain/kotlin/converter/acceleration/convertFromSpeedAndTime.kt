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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.Acceleration
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Speed
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.byDividing
import kotlin.jvm.JvmName

@JvmName("accelerationFromSpeedAndTimeDefault")
fun <
    SpeedUnit : Speed,
    TimeUnit : Time,
    AccelerationUnit : Acceleration
> AccelerationUnit.acceleration(
    speed: ScientificValue<MeasurementType.Speed, SpeedUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = acceleration(speed, time, ::DefaultScientificValue)

@JvmName("accelerationFromSpeedAndTime")
fun <
    SpeedUnit : Speed,
    TimeUnit : Time,
    AccelerationUnit : Acceleration,
    Value : ScientificValue<MeasurementType.Acceleration, AccelerationUnit>
> AccelerationUnit.acceleration(
    speed: ScientificValue<MeasurementType.Speed, SpeedUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>,
    factory: (Decimal, AccelerationUnit) -> Value
) = byDividing(speed, time, factory)
