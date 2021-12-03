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

package com.splendo.kaluga.scientific.converter.electricConductance

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.ElectricConductance
import com.splendo.kaluga.scientific.unit.ElectricCurrent
import com.splendo.kaluga.scientific.unit.Voltage
import kotlin.jvm.JvmName

@JvmName("conductanceFromCurrentAndVoltageDefault")
fun <
    CurrentUnit : ElectricCurrent,
    VoltageUnit : Voltage,
    ConductanceUnit : ElectricConductance
    > ConductanceUnit.conductance(
    current: ScientificValue<PhysicalQuantity.ElectricCurrent, CurrentUnit>,
    voltage: ScientificValue<PhysicalQuantity.Voltage, VoltageUnit>
) = conductance(current, voltage, ::DefaultScientificValue)

@JvmName("conductanceFromCurrentAndVoltage")
fun <
    CurrentUnit : ElectricCurrent,
    VoltageUnit : Voltage,
    ConductanceUnit : ElectricConductance,
    Value : ScientificValue<PhysicalQuantity.ElectricConductance, ConductanceUnit>
    > ConductanceUnit.conductance(
    current: ScientificValue<PhysicalQuantity.ElectricCurrent, CurrentUnit>,
    voltage: ScientificValue<PhysicalQuantity.Voltage, VoltageUnit>,
    factory: (Decimal, ConductanceUnit) -> Value
) = byDividing(current, voltage, factory)
