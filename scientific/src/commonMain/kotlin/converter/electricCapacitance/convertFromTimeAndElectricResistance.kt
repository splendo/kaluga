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

package com.splendo.kaluga.scientific.converter.electricCapacitance

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.ElectricCapacitance
import com.splendo.kaluga.scientific.unit.ElectricResistance
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("capacitanceFromTimeAndResistanceDefault")
fun <
    CapacitanceUnit : ElectricCapacitance,
    TimeUnit : Time,
    ResistanceUnit : ElectricResistance,
    > CapacitanceUnit.capacitance(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
    resistance: ScientificValue<PhysicalQuantity.ElectricResistance, ResistanceUnit>,
) = capacitance(time, resistance, ::DefaultScientificValue)

@JvmName("capacitanceFromTimeAndResistance")
fun <
    CapacitanceUnit : ElectricCapacitance,
    TimeUnit : Time,
    ResistanceUnit : ElectricResistance,
    Value : ScientificValue<PhysicalQuantity.ElectricCapacitance, CapacitanceUnit>,
    > CapacitanceUnit.capacitance(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
    resistance: ScientificValue<PhysicalQuantity.ElectricResistance, ResistanceUnit>,
    factory: (Decimal, CapacitanceUnit) -> Value,
) = byDividing(time, resistance, factory)
