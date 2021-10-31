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

package com.splendo.kaluga.scientific.converter.electricCurrent

import com.splendo.kaluga.scientific.Abampere
import com.splendo.kaluga.scientific.Abcoulomb
import com.splendo.kaluga.scientific.Biot
import com.splendo.kaluga.scientific.Coulomb
import com.splendo.kaluga.scientific.ElectricCurrent
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.converter.electricCharge.charge
import kotlin.jvm.JvmName

@JvmName("abampereTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.ElectricCurrent, Abampere>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Abcoulomb.charge(this, time)
@JvmName("biotTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.ElectricCurrent, Biot>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Abcoulomb.charge(this, time)
@JvmName("currentTimesTime")
infix operator fun <CurrentUnit : ElectricCurrent, TimeUnit : Time> ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Coulomb.charge(this, time)
