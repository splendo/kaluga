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

package com.splendo.kaluga.scientific.converter.thermalResistance

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.converter.temperature.deltaValue
import com.splendo.kaluga.scientific.unit.Kelvin
import com.splendo.kaluga.scientific.unit.Power
import com.splendo.kaluga.scientific.unit.Temperature
import com.splendo.kaluga.scientific.unit.ThermalResistance
import kotlin.jvm.JvmName

@JvmName("thermalResistanceFromTemperatureAndPowerDefault")
fun <
    TemperatureUnit : Temperature,
    PowerUnit : Power,
    ThermalResistanceUnit : ThermalResistance,
    > ThermalResistanceUnit.thermalResistance(
    temperature: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>,
    power: ScientificValue<PhysicalQuantity.Power, PowerUnit>,
) = thermalResistance(temperature, power, ::DefaultScientificValue)

@JvmName("thermalResistanceFromTemperatureAndPower")
fun <
    TemperatureUnit : Temperature,
    PowerUnit : Power,
    ThermalResistanceUnit : ThermalResistance,
    Value : ScientificValue<PhysicalQuantity.ThermalResistance, ThermalResistanceUnit>,
    > ThermalResistanceUnit.thermalResistance(
    temperature: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>,
    power: ScientificValue<PhysicalQuantity.Power, PowerUnit>,
    factory: (Decimal, ThermalResistanceUnit) -> Value,
) = byDividing(Kelvin.deltaValue(temperature), power, factory)
