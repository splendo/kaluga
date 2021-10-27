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

package com.splendo.kaluga.scientific.electricCharge

import com.splendo.kaluga.scientific.Abcoulomb
import com.splendo.kaluga.scientific.Abvolt
import com.splendo.kaluga.scientific.ElectricCharge
import com.splendo.kaluga.scientific.Erg
import com.splendo.kaluga.scientific.Joule
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Voltage
import com.splendo.kaluga.scientific.energy.energy
import kotlin.jvm.JvmName

@JvmName("abcoulombTimesAbvolt")
infix operator fun ScientificValue<MeasurementType.ElectricCharge, Abcoulomb>.times(voltage: ScientificValue<MeasurementType.Voltage, Abvolt>) = Erg.energy(this, voltage)
@JvmName("chargeTimesVoltage")
infix operator fun <ChargeUnit : ElectricCharge, VoltageUnit : Voltage> ScientificValue<MeasurementType.ElectricCharge, ChargeUnit>.times(voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>) = Joule.energy(this, voltage)
