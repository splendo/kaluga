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
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.ElectricCharge
import com.splendo.kaluga.scientific.unit.ElectricCurrent
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("timeFromChargeAndCurrentDefault")
fun <
    CurrentUnit : ElectricCurrent,
    TimeUnit : Time,
    ChargeUnit : ElectricCharge
> TimeUnit.duration(
    charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>,
    current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>
) = duration(charge, current, ::DefaultScientificValue)

@JvmName("timeFromChargeAndCurrent")
fun <
    CurrentUnit : ElectricCurrent,
    TimeUnit : Time,
    ChargeUnit : ElectricCharge,
    Value : ScientificValue<MeasurementType.Time, TimeUnit>
> TimeUnit.duration(
    charge: ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>,
    current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>,
    factory: (Decimal, TimeUnit) -> Value
) = byDividing(charge, current, factory)
