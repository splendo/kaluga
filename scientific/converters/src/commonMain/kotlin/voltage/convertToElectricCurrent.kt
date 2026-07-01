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

package com.splendo.kaluga.scientific.converter.voltage

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.electricConductance.times
import com.splendo.kaluga.scientific.converter.electricCurrent.current
import com.splendo.kaluga.scientific.unit.Abampere
import com.splendo.kaluga.scientific.unit.Abohm
import com.splendo.kaluga.scientific.unit.Absiemens
import com.splendo.kaluga.scientific.unit.Abvolt
import com.splendo.kaluga.scientific.unit.Ampere
import com.splendo.kaluga.scientific.unit.ElectricConductance
import com.splendo.kaluga.scientific.unit.ElectricResistance
import com.splendo.kaluga.scientific.unit.Voltage
import kotlin.jvm.JvmName

@JvmName("abvoltTimesAbsiemens")
infix operator fun ScientificValue<PhysicalQuantity.Voltage, Abvolt>.times(conductance: ScientificValue<PhysicalQuantity.ElectricConductance, Absiemens>) = conductance * this

@JvmName("voltageTimesConductance")
infix operator fun <ConductanceUnit : ElectricConductance, VoltageUnit : Voltage> ScientificValue<PhysicalQuantity.Voltage, VoltageUnit>.times(
    conductance: ScientificValue<PhysicalQuantity.ElectricConductance, ConductanceUnit>,
) = conductance * this

@JvmName("abvoltDivAbohm")
infix operator fun ScientificValue<PhysicalQuantity.Voltage, Abvolt>.div(resistance: ScientificValue<PhysicalQuantity.ElectricResistance, Abohm>) =
    Abampere.current(this, resistance)

@JvmName("voltageDivResistance")
infix operator fun <VoltageUnit : Voltage, ResistanceUnit : ElectricResistance> ScientificValue<PhysicalQuantity.Voltage, VoltageUnit>.div(
    resistance: ScientificValue<PhysicalQuantity.ElectricResistance, ResistanceUnit>,
) = Ampere.current(this, resistance)
