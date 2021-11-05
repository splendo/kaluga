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

package com.splendo.kaluga.scientific.converter.weight

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.MassFlowRate
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.Weight
import kotlin.jvm.JvmName

@JvmName("weightFromMassFlowRateAndTimeDefault")
fun <
    WeightUnit : Weight,
    TimeUnit : Time,
    MassFlowRateUnit : MassFlowRate
> WeightUnit.mass(
    massFlowRate: ScientificValue<MeasurementType.MassFlowRate, MassFlowRateUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = mass(massFlowRate, time, ::DefaultScientificValue)

@JvmName("weightFromMassFlowRateAndTime")
fun <
    WeightUnit : Weight,
    TimeUnit : Time,
    MassFlowRateUnit : MassFlowRate,
    Value : ScientificValue<MeasurementType.Weight, WeightUnit>
> WeightUnit.mass(
    massFlowRate: ScientificValue<MeasurementType.MassFlowRate, MassFlowRateUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>,
    factory: (Decimal, WeightUnit) -> Value
) = byMultiplying(massFlowRate, time, factory)
