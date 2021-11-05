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

package com.splendo.kaluga.scientific.converter.electricCurrent

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.ElectricConductance
import com.splendo.kaluga.scientific.unit.ElectricCurrent
import com.splendo.kaluga.scientific.unit.Voltage
import kotlin.jvm.JvmName

@JvmName("currentFromVoltageAndConductanceDefault")
fun <
    CurrentUnit : ElectricCurrent,
    VoltageUnit : Voltage,
    ConductanceUnit : ElectricConductance
> CurrentUnit.current(
    conductance: ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>,
    voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>
) = current(conductance, voltage, ::DefaultScientificValue)

@JvmName("currentFromVoltageAndConductance")
fun <
    CurrentUnit : ElectricCurrent,
    VoltageUnit : Voltage,
    ConductanceUnit : ElectricConductance,
    Value : ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>
> CurrentUnit.current(
    conductance: ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>,
    voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>,
    factory: (Decimal, CurrentUnit) -> Value
) = byMultiplying(conductance, voltage, factory)
