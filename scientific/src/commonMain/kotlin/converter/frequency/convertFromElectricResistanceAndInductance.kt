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

package com.splendo.kaluga.scientific.converter.frequency

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.ElectricInductance
import com.splendo.kaluga.scientific.unit.ElectricResistance
import com.splendo.kaluga.scientific.unit.Frequency
import kotlin.jvm.JvmName

@JvmName("frequencyFromResistanceAndInductanceDefault")
fun <
    ResistanceUnit : ElectricResistance,
    FrequencyUnit : Frequency,
    InductanceUnit : ElectricInductance
    > FrequencyUnit.frequency(
    resistance: ScientificValue<PhysicalQuantity.ElectricResistance, ResistanceUnit>,
    inductance: ScientificValue<PhysicalQuantity.ElectricInductance, InductanceUnit>
) = frequency(resistance, inductance, ::DefaultScientificValue)

@JvmName("frequencyFromResistanceAndInductance")
fun <
    ResistanceUnit : ElectricResistance,
    FrequencyUnit : Frequency,
    InductanceUnit : ElectricInductance,
    Value : ScientificValue<PhysicalQuantity.Frequency, FrequencyUnit>
    > FrequencyUnit.frequency(
    resistance: ScientificValue<PhysicalQuantity.ElectricResistance, ResistanceUnit>,
    inductance: ScientificValue<PhysicalQuantity.ElectricInductance, InductanceUnit>,
    factory: (Decimal, FrequencyUnit) -> Value
) = byDividing(resistance, inductance, factory)
