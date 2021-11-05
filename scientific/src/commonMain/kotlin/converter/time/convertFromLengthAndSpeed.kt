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

package com.splendo.kaluga.scientific.converter.time

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.Speed
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("timeFromDistanceAndSpeedDefault")
fun <
    LengthUnit : Length,
    TimeUnit : Time,
    SpeedUnit : Speed
    > TimeUnit.time(
    distance: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
    speed: ScientificValue<PhysicalQuantity.Speed, SpeedUnit>
) = time(distance, speed, ::DefaultScientificValue)

@JvmName("timeFromDistanceAndSpeed")
fun <
    LengthUnit : Length,
    TimeUnit : Time,
    SpeedUnit : Speed,
    Value : ScientificValue<PhysicalQuantity.Time, TimeUnit>
    > TimeUnit.time(
    distance: ScientificValue<PhysicalQuantity.Length, LengthUnit>,
    speed: ScientificValue<PhysicalQuantity.Speed, SpeedUnit>,
    factory: (Decimal, TimeUnit) -> Value
) = byDividing(distance, speed, factory)
