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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.Acceleration
import com.splendo.kaluga.scientific.unit.Speed
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("speedFromAccelerationAndTimeDefault")
fun <
    AccelerationUnit : Acceleration,
    TimeUnit : Time,
    SpeedUnit : Speed
    > SpeedUnit.speed(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, AccelerationUnit>,
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>
) = speed(acceleration, time, ::DefaultScientificValue)

@JvmName("speedFromAccelerationAndTime")
fun <
    AccelerationUnit : Acceleration,
    TimeUnit : Time,
    SpeedUnit : Speed,
    Value : ScientificValue<PhysicalQuantity.Speed, SpeedUnit>
    > SpeedUnit.speed(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, AccelerationUnit>,
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
    factory: (Decimal, SpeedUnit) -> Value
) = byMultiplying(acceleration, time, factory)
