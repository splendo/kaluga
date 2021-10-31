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

package com.splendo.kaluga.scientific.converter.power

import com.splendo.kaluga.scientific.Abampere
import com.splendo.kaluga.scientific.Abvolt
import com.splendo.kaluga.scientific.Ampere
import com.splendo.kaluga.scientific.ErgPerSecond
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Power
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Voltage
import com.splendo.kaluga.scientific.converter.electricCurrent.current
import kotlin.jvm.JvmName

@JvmName("ergSecondDivAbvolt")
infix operator fun ScientificValue<MeasurementType.Power, ErgPerSecond>.div(voltage: ScientificValue<MeasurementType.Voltage, Abvolt>) = Abampere.current(this, voltage)
@JvmName("powerDivVoltage")
infix operator fun <PowerUnit : Power, VoltageUnit : Voltage> ScientificValue<MeasurementType.Power, PowerUnit>.div(voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>) = Ampere.current(this, voltage)
