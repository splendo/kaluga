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

package com.splendo.kaluga.scientific.converter.electricResistance

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.ElectricInductance
import com.splendo.kaluga.scientific.unit.ElectricResistance
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("resistanceFromInductanceAndTimeDefault")
fun <
    ResistanceUnit : ElectricResistance,
    TimeUnit : Time,
    InductanceUnit : ElectricInductance
    > ResistanceUnit.resistance(
    inductance: ScientificValue<MeasurementType.ElectricInductance, InductanceUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>
) = resistance(inductance, time, ::DefaultScientificValue)

@JvmName("resistanceFromInductanceAndTime")
fun <
    ResistanceUnit : ElectricResistance,
    TimeUnit : Time,
    InductanceUnit : ElectricInductance,
    Value : ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit>
    > ResistanceUnit.resistance(
    inductance: ScientificValue<MeasurementType.ElectricInductance, InductanceUnit>,
    time: ScientificValue<MeasurementType.Time, TimeUnit>,
    factory: (Decimal, ResistanceUnit) -> Value
) = byDividing(inductance, time, factory)
