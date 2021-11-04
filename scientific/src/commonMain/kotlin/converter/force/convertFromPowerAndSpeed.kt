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

package com.splendo.kaluga.scientific.converter.force

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.Force
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Power
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Speed
import com.splendo.kaluga.scientific.byDividing
import kotlin.jvm.JvmName

@JvmName("forceFromPowerAndSpeedDefault")
fun <
    ForceUnit : Force,
    SpeedUnit : Speed,
    PowerUnit : Power
> ForceUnit.force(
    power: ScientificValue<MeasurementType.Power, PowerUnit>,
    speed: ScientificValue<MeasurementType.Speed, SpeedUnit>
) = force(power, speed, ::DefaultScientificValue)

@JvmName("forceFromPowerAndSpeed")
fun <
    ForceUnit : Force,
    SpeedUnit : Speed,
    PowerUnit : Power,
    Value : ScientificValue<MeasurementType.Force, ForceUnit>
> ForceUnit.force(
    power: ScientificValue<MeasurementType.Power, PowerUnit>,
    speed: ScientificValue<MeasurementType.Speed, SpeedUnit>,
    factory: (Decimal, ForceUnit) -> Value
) = byDividing(power, speed, factory)
