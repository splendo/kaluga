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
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.Power
import com.splendo.kaluga.scientific.unit.Speed
import kotlin.jvm.JvmName

@JvmName("speedFromPowerAndForceDefault")
fun <
    ForceUnit : Force,
    SpeedUnit : Speed,
    PowerUnit : Power,
    > SpeedUnit.speed(
    power: ScientificValue<PhysicalQuantity.Power, PowerUnit>,
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
) = speed(power, force, ::DefaultScientificValue)

@JvmName("speedFromPowerAndForce")
fun <
    ForceUnit : Force,
    SpeedUnit : Speed,
    PowerUnit : Power,
    Value : ScientificValue<PhysicalQuantity.Speed, SpeedUnit>,
    > SpeedUnit.speed(
    power: ScientificValue<PhysicalQuantity.Power, PowerUnit>,
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
    factory: (Decimal, SpeedUnit) -> Value,
) = byDividing(power, force, factory)
