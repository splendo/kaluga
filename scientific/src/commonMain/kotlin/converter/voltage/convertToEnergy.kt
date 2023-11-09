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
import com.splendo.kaluga.scientific.converter.electricCharge.times
import com.splendo.kaluga.scientific.unit.Abcoulomb
import com.splendo.kaluga.scientific.unit.Abvolt
import com.splendo.kaluga.scientific.unit.ElectricCharge
import com.splendo.kaluga.scientific.unit.Voltage
import kotlin.jvm.JvmName

@JvmName("abvoltTimesAbcoulomb")
infix operator fun ScientificValue<PhysicalQuantity.Voltage, Abvolt>.times(charge: ScientificValue<PhysicalQuantity.ElectricCharge, Abcoulomb>) = charge * this

@JvmName("voltageTimesCharge")
infix operator fun <ChargeUnit : ElectricCharge, VoltageUnit : Voltage> ScientificValue<PhysicalQuantity.Voltage, VoltageUnit>.times(
    charge: ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>,
) = charge * this
