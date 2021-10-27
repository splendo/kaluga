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

package com.splendo.kaluga.scientific.electricCharge

import com.splendo.kaluga.scientific.Abampere
import com.splendo.kaluga.scientific.Abcoulomb
import com.splendo.kaluga.scientific.Ampere
import com.splendo.kaluga.scientific.ElectricCharge
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.electricCurrent.current
import kotlin.jvm.JvmName

@JvmName("abcoulombDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.ElectricCharge, Abcoulomb>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Abampere.current(this, time)
@JvmName("chargeDivTime")
infix operator fun <ChargeUnit : ElectricCharge, TimeUnit : Time> ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Ampere.current(this, time)
