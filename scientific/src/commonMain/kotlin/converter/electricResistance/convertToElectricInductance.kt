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

package com.splendo.kaluga.scientific.converter.electricResistance

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.electricInductance.inductance
import com.splendo.kaluga.scientific.unit.Abhenry
import com.splendo.kaluga.scientific.unit.Abohm
import com.splendo.kaluga.scientific.unit.ElectricResistance
import com.splendo.kaluga.scientific.unit.Frequency
import com.splendo.kaluga.scientific.unit.Henry
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("abohmDivFrequency")
infix operator fun <FrequencyUnit : Frequency> ScientificValue<PhysicalQuantity.ElectricResistance, Abohm>.div(
    frequency: ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>,
) = Abhenry.inductance(this, frequency)

@JvmName("resistanceDivFrequency")
infix operator fun <ResistanceUnit : ElectricResistance, FrequencyUnit : Frequency> ScientificValue<PhysicalQuantity.ElectricResistance, ResistanceUnit>.div(
    frequency: ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>,
) = Henry.inductance(this, frequency)

@JvmName("abohmTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.ElectricResistance, Abohm>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    Abhenry.inductance(this, time)

@JvmName("resistanceTimesTime")
infix operator fun <ResistanceUnit : ElectricResistance, TimeUnit : Time> ScientificValue<PhysicalQuantity.ElectricResistance, ResistanceUnit>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = Henry.inductance(this, time)
