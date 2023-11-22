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

package com.splendo.kaluga.scientific.converter.power

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.voltage.voltage
import com.splendo.kaluga.scientific.unit.Abampere
import com.splendo.kaluga.scientific.unit.Abvolt
import com.splendo.kaluga.scientific.unit.Biot
import com.splendo.kaluga.scientific.unit.ElectricCurrent
import com.splendo.kaluga.scientific.unit.ErgPerSecond
import com.splendo.kaluga.scientific.unit.Power
import com.splendo.kaluga.scientific.unit.Volt
import kotlin.jvm.JvmName

@JvmName("ergSecondDivAbampere")
infix operator fun ScientificValue<PhysicalQuantity.Power, ErgPerSecond>.div(current: ScientificValue<PhysicalQuantity.ElectricCurrent, Abampere>) = Abvolt.voltage(this, current)

@JvmName("ergSecondDivBiot")
infix operator fun ScientificValue<PhysicalQuantity.Power, ErgPerSecond>.div(current: ScientificValue<PhysicalQuantity.ElectricCurrent, Biot>) = Abvolt.voltage(this, current)

@JvmName("powerDivCurrent")
infix operator fun <PowerUnit : Power, CurrentUnit : ElectricCurrent> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    current: ScientificValue<PhysicalQuantity.ElectricCurrent, CurrentUnit>,
) = Volt.voltage(this, current)
