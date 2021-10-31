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

package com.splendo.kaluga.scientific.converter.electricInductance

import com.splendo.kaluga.scientific.Abhenry
import com.splendo.kaluga.scientific.Abohm
import com.splendo.kaluga.scientific.ElectricInductance
import com.splendo.kaluga.scientific.Frequency
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Ohm
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.converter.electricResistance.resistance
import kotlin.jvm.JvmName

@JvmName("abhenryTimesFrequency")
infix operator fun <FrequencyUnit : Frequency> ScientificValue<MeasurementType.ElectricInductance, Abhenry>.times(frequency: ScientificValue<MeasurementType.Frequency, FrequencyUnit>) = Abohm.resistance(this, frequency)
@JvmName("inductanceTimesFrequency")
infix operator fun <InductanceUnit : ElectricInductance, FrequencyUnit : Frequency> ScientificValue<MeasurementType.ElectricInductance, InductanceUnit>.times(frequency: ScientificValue<MeasurementType.Frequency, FrequencyUnit>) = Ohm.resistance(this, frequency)

@JvmName("abhenryDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.ElectricInductance, Abhenry>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Abohm.resistance(this, time)
@JvmName("inductanceDivTime")
infix operator fun <InductanceUnit : ElectricInductance, TimeUnit : Time> ScientificValue<MeasurementType.ElectricInductance, InductanceUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = Ohm.resistance(this, time)
