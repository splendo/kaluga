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

package com.splendo.kaluga.scientific.converter.electricInductance

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.electricResistance.resistance
import com.splendo.kaluga.scientific.unit.Abhenry
import com.splendo.kaluga.scientific.unit.Abohm
import com.splendo.kaluga.scientific.unit.ElectricInductance
import com.splendo.kaluga.scientific.unit.Frequency
import com.splendo.kaluga.scientific.unit.Ohm
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("abhenryTimesFrequency")
infix operator fun <FrequencyUnit : Frequency> ScientificValue<PhysicalQuantity.ElectricInductance, Abhenry>.times(
    frequency: ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>,
) = Abohm.resistance(this, frequency)

@JvmName("inductanceTimesFrequency")
infix operator fun <InductanceUnit : ElectricInductance, FrequencyUnit : Frequency> ScientificValue<PhysicalQuantity.ElectricInductance, InductanceUnit>.times(
    frequency: ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>,
) = Ohm.resistance(this, frequency)

@JvmName("abhenryDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.ElectricInductance, Abhenry>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    Abohm.resistance(this, time)

@JvmName("inductanceDivTime")
infix operator fun <InductanceUnit : ElectricInductance, TimeUnit : Time> ScientificValue<PhysicalQuantity.ElectricInductance, InductanceUnit>.div(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = Ohm.resistance(this, time)
