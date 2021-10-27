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

package com.splendo.kaluga.scientific.voltage

import com.splendo.kaluga.scientific.Abampere
import com.splendo.kaluga.scientific.Abvolt
import com.splendo.kaluga.scientific.Biot
import com.splendo.kaluga.scientific.ElectricCurrent
import com.splendo.kaluga.scientific.ErgPerSecond
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Voltage
import com.splendo.kaluga.scientific.Watt
import com.splendo.kaluga.scientific.power.power
import kotlin.jvm.JvmName

@JvmName("abvoltTimesAbampere")
infix operator fun ScientificValue<MeasurementType.Voltage, Abvolt>.times(current: ScientificValue<MeasurementType.ElectricCurrent, Abampere>) = ErgPerSecond.power(this, current)
@JvmName("abvoltTimesBiot")
infix operator fun ScientificValue<MeasurementType.Voltage, Abvolt>.times(current: ScientificValue<MeasurementType.ElectricCurrent, Biot>) = ErgPerSecond.power(this, current)
@JvmName("voltageTimesCurrent")
infix operator fun <VoltageUnit : Voltage, ElectricCurrentUnit : ElectricCurrent> ScientificValue<MeasurementType.Voltage, VoltageUnit>.times(current: ScientificValue<MeasurementType.ElectricCurrent, ElectricCurrentUnit>) = Watt.power(this, current)
