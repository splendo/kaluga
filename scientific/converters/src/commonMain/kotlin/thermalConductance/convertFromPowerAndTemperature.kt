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

package com.splendo.kaluga.scientific.converter.thermalConductance

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.converter.temperature.deltaValue
import com.splendo.kaluga.scientific.unit.Kelvin
import com.splendo.kaluga.scientific.unit.Power
import com.splendo.kaluga.scientific.unit.Temperature
import com.splendo.kaluga.scientific.unit.ThermalConductance
import kotlin.jvm.JvmName

@JvmName("thermalConductanceFromPowerAndTemperatureDefault")
fun <
    PowerUnit : Power,
    TemperatureUnit : Temperature,
    ConductanceUnit : ThermalConductance,
    > ConductanceUnit.thermalConductance(
    power: ScientificValue<PhysicalQuantity.Power, PowerUnit>,
    temperature: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>,
) = thermalConductance(power, temperature, ::DefaultScientificValue)

@JvmName("thermalConductanceFromPowerAndTemperature")
fun <
    PowerUnit : Power,
    TemperatureUnit : Temperature,
    ConductanceUnit : ThermalConductance,
    Value : ScientificValue<PhysicalQuantity.ThermalConductance, ConductanceUnit>,
    > ConductanceUnit.thermalConductance(
    power: ScientificValue<PhysicalQuantity.Power, PowerUnit>,
    temperature: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>,
    factory: (Decimal, ConductanceUnit) -> Value,
) = byDividing(power, Kelvin.deltaValue(temperature), factory)
