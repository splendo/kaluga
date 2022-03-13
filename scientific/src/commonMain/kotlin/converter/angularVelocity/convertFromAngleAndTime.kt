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

package com.splendo.kaluga.scientific.converter.angularVelocity

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.Angle
import com.splendo.kaluga.scientific.unit.AngularVelocity
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("angularVelocityFromAngleAndTimeDefault")
fun <
    AngleUnit : Angle,
    TimeUnit : Time
    > AngularVelocity.velocity(
    angle: ScientificValue<PhysicalQuantity.Angle, AngleUnit>,
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>
) = velocity(angle, time, ::DefaultScientificValue)

@JvmName("angularVelocityFromAngleAndTime")
fun <
    AngleUnit : Angle,
    TimeUnit : Time,
    Value : ScientificValue<PhysicalQuantity.AngularVelocity, AngularVelocity>
    > AngularVelocity.velocity(
    angle: ScientificValue<PhysicalQuantity.Angle, AngleUnit>,
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
    factory: (Decimal, AngularVelocity) -> Value
) = byDividing(angle, time, factory)
