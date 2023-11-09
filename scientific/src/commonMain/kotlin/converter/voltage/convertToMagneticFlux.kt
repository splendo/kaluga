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
import com.splendo.kaluga.scientific.converter.magneticFlux.flux
import com.splendo.kaluga.scientific.unit.Abvolt
import com.splendo.kaluga.scientific.unit.Maxwell
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.Voltage
import com.splendo.kaluga.scientific.unit.Weber
import kotlin.jvm.JvmName

@JvmName("abvoltTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Voltage, Abvolt>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) = Maxwell.flux(this, time)

@JvmName("voltageTimesTime")
infix operator fun <VoltageUnit : Voltage, TimeUnit : Time> ScientificValue<PhysicalQuantity.Voltage, VoltageUnit>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    Weber.flux(this, time)
