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

package com.splendo.kaluga.scientific.converter.energy

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.voltage.voltage
import com.splendo.kaluga.scientific.unit.Abcoulomb
import com.splendo.kaluga.scientific.unit.Abvolt
import com.splendo.kaluga.scientific.unit.ElectricCharge
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.ErgMultiple
import com.splendo.kaluga.scientific.unit.Volt
import kotlin.jvm.JvmName

@JvmName("ergDivAbcoulomb")
infix operator fun ScientificValue<PhysicalQuantity.Energy, Erg>.div(charge: ScientificValue<PhysicalQuantity.ElectricCharge, Abcoulomb>) = Abvolt.voltage(this, charge)

@JvmName("ergMultipleDivAbcoulomb")
infix operator fun <ErgUnit : ErgMultiple> ScientificValue<PhysicalQuantity.Energy, ErgUnit>.div(charge: ScientificValue<PhysicalQuantity.ElectricCharge, Abcoulomb>) =
    Abvolt.voltage(this, charge)

@JvmName("energyDivCharge")
infix operator fun <EnergyUnit : Energy, ChargeUnit : ElectricCharge> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    charge: ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>,
) = Volt.voltage(this, charge)
