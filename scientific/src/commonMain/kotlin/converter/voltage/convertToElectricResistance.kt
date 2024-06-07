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
import com.splendo.kaluga.scientific.converter.electricResistance.resistance
import com.splendo.kaluga.scientific.unit.Abampere
import com.splendo.kaluga.scientific.unit.Abohm
import com.splendo.kaluga.scientific.unit.Abvolt
import com.splendo.kaluga.scientific.unit.Biot
import com.splendo.kaluga.scientific.unit.ElectricCurrent
import com.splendo.kaluga.scientific.unit.Ohm
import com.splendo.kaluga.scientific.unit.Voltage
import kotlin.jvm.JvmName

@JvmName("abvoltDivAbampere")
infix operator fun ScientificValue<PhysicalQuantity.Voltage, Abvolt>.div(current: ScientificValue<PhysicalQuantity.ElectricCurrent, Abampere>) = Abohm.resistance(this, current)

@JvmName("abvoltDivBiot")
infix operator fun ScientificValue<PhysicalQuantity.Voltage, Abvolt>.div(current: ScientificValue<PhysicalQuantity.ElectricCurrent, Biot>) = Abohm.resistance(this, current)

@JvmName("voltageDivCurrent")
infix operator fun <CurrentUnit : ElectricCurrent, VoltageUnit : Voltage> ScientificValue<PhysicalQuantity.Voltage, VoltageUnit>.div(
    current: ScientificValue<PhysicalQuantity.ElectricCurrent, CurrentUnit>,
) = Ohm.resistance(this, current)
