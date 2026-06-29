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

package com.splendo.kaluga.scientific.converter.electricCurrent

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.electricCharge.charge
import com.splendo.kaluga.scientific.unit.Abampere
import com.splendo.kaluga.scientific.unit.Abcoulomb
import com.splendo.kaluga.scientific.unit.Biot
import com.splendo.kaluga.scientific.unit.Coulomb
import com.splendo.kaluga.scientific.unit.ElectricCurrent
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("abampereTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.ElectricCurrent, Abampere>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    Abcoulomb.charge(this, time)

@JvmName("biotTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.ElectricCurrent, Biot>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    Abcoulomb.charge(this, time)

@JvmName("currentTimesTime")
infix operator fun <CurrentUnit : ElectricCurrent, TimeUnit : Time> ScientificValue<PhysicalQuantity.ElectricCurrent, CurrentUnit>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = Coulomb.charge(this, time)
