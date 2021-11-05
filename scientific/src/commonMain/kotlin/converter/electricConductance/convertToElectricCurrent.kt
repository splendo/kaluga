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

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.electricCurrent.current
import com.splendo.kaluga.scientific.unit.Abampere
import com.splendo.kaluga.scientific.unit.Absiemens
import com.splendo.kaluga.scientific.unit.Abvolt
import com.splendo.kaluga.scientific.unit.Ampere
import com.splendo.kaluga.scientific.unit.ElectricConductance
import com.splendo.kaluga.scientific.unit.Voltage
import kotlin.jvm.JvmName

@JvmName("absiemensTimesAbvolt")
infix operator fun ScientificValue<MeasurementType.ElectricConductance, Absiemens>.times(voltage: ScientificValue<MeasurementType.Voltage, Abvolt>) = Abampere.current(this, voltage)
@JvmName("conductanceTimesVoltage")
infix operator fun <ConductanceUnit : ElectricConductance, VoltageUnit : Voltage> ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>.times(voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>) = Ampere.current(this, voltage)
