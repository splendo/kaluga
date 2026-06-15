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

package com.splendo.kaluga.scientific.converter.pressure

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.DynamicViscosity
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("pressureFromDynamicViscosityAndTimeDefault")
fun <
    DynamicViscosityUnit : DynamicViscosity,
    TimeUnit : Time,
    PressureUnit : Pressure,
    > PressureUnit.pressure(
    dynamicViscosity: ScientificValue<PhysicalQuantity.DynamicViscosity, DynamicViscosityUnit>,
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = pressure(dynamicViscosity, time, ::DefaultScientificValue)

@JvmName("pressureFromDynamicViscosityAndTime")
fun <
    DynamicViscosityUnit : DynamicViscosity,
    TimeUnit : Time,
    PressureUnit : Pressure,
    Value : ScientificValue<PhysicalQuantity.Pressure, PressureUnit>,
    > PressureUnit.pressure(
    dynamicViscosity: ScientificValue<PhysicalQuantity.DynamicViscosity, DynamicViscosityUnit>,
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
    factory: (Decimal, PressureUnit) -> Value,
) = byDividing(dynamicViscosity, time, factory)
