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

package com.splendo.kaluga.scientific.converter.electricCharge

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.ElectricCharge
import com.splendo.kaluga.scientific.unit.ElectricCurrent
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("chargeFromCurrentAndTimeDefault")
fun <
    CurrentUnit : ElectricCurrent,
    TimeUnit : Time,
    ChargeUnit : ElectricCharge,
    > ChargeUnit.charge(
    current: ScientificValue<PhysicalQuantity.ElectricCurrent, CurrentUnit>,
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = charge(current, time, ::DefaultScientificValue)

@JvmName("chargeFromCurrentAndTime")
fun <
    CurrentUnit : ElectricCurrent,
    TimeUnit : Time,
    ChargeUnit : ElectricCharge,
    Value : ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>,
    > ChargeUnit.charge(
    current: ScientificValue<PhysicalQuantity.ElectricCurrent, CurrentUnit>,
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
    factory: (Decimal, ChargeUnit) -> Value,
) = byMultiplying(current, time, factory)
