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

package com.splendo.kaluga.scientific.converter.frequency

import com.splendo.kaluga.scientific.ElectricInductance
import com.splendo.kaluga.scientific.ElectricResistance
import com.splendo.kaluga.scientific.Frequency
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import kotlin.jvm.JvmName

@JvmName("frequencyFromResistanceAndInductance")
fun <
    ResistanceUnit : ElectricResistance,
    FrequencyUnit : Frequency,
    InductanceUnit : ElectricInductance
    >
    FrequencyUnit.frequency(
    resistance: ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>,
    inductance: ScientificValue<MeasurementType.ElectricInductance, InductanceUnit>
) : ScientificValue<MeasurementType.Frequency, FrequencyUnit> = byDividing(resistance, inductance)
