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

package com.splendo.kaluga.scientific.electricCurrent

import com.splendo.kaluga.scientific.Abampere
import com.splendo.kaluga.scientific.Absiemens
import com.splendo.kaluga.scientific.Abvolt
import com.splendo.kaluga.scientific.Biot
import com.splendo.kaluga.scientific.ElectricCurrent
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Siemens
import com.splendo.kaluga.scientific.Voltage
import com.splendo.kaluga.scientific.electricConductance.conductance
import kotlin.jvm.JvmName

@JvmName("abampereDivAbvolt")
infix operator fun ScientificValue<MeasurementType.ElectricCurrent, Abampere>.div(voltage: ScientificValue<MeasurementType.Voltage, Abvolt>) = Absiemens.conductance(this, voltage)
@JvmName("biotDivAbvolt")
infix operator fun ScientificValue<MeasurementType.ElectricCurrent, Biot>.div(voltage: ScientificValue<MeasurementType.Voltage, Abvolt>) = Absiemens.conductance(this, voltage)
@JvmName("currentDivVoltage")
infix operator fun <CurrentUnit : ElectricCurrent, VoltageUnit : Voltage> ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>.div(voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>) = Siemens.conductance(this, voltage)
