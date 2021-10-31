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

import com.splendo.kaluga.scientific.ElectricCapacitance
import com.splendo.kaluga.scientific.ElectricResistance
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.byDividing
import kotlin.jvm.JvmName

@JvmName("resistanceFromTimeAndCapacitance")
fun <
    CapacitanceUnit : ElectricCapacitance,
    TimeUnit : Time,
    ResistanceUnit : ElectricResistance
    >
    ResistanceUnit.resistance(
    time: ScientificValue<MeasurementType.Time, TimeUnit>,
    capacitance: ScientificValue<MeasurementType.ElectricCapacitance, CapacitanceUnit>
) : ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit> = byDividing(time, capacitance)
