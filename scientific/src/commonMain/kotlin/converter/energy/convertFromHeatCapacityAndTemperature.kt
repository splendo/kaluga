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

package com.splendo.kaluga.scientific.converter.energy

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.converter.temperature.deltaValue
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.HeatCapacity
import com.splendo.kaluga.scientific.unit.Kelvin
import com.splendo.kaluga.scientific.unit.Temperature
import kotlin.jvm.JvmName

@JvmName("energyFromHeatCapacityAndTemperatureDefault")
fun <
    EnergyUnit : Energy,
    TemperatureUnit : Temperature,
    HeatCapacityUnit : HeatCapacity
    > EnergyUnit.energy(
    heatCapacity: ScientificValue<PhysicalQuantity.HeatCapacity, HeatCapacityUnit>,
    temperature: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>
) = energy(heatCapacity, temperature, ::DefaultScientificValue)

@JvmName("energyFromHeatCapacityAndTemperature")
fun <
    EnergyUnit : Energy,
    TemperatureUnit : Temperature,
    HeatCapacityUnit : HeatCapacity,
    Value : ScientificValue<PhysicalQuantity.Energy, EnergyUnit>
    > EnergyUnit.energy(
    heatCapacity: ScientificValue<PhysicalQuantity.HeatCapacity, HeatCapacityUnit>,
    temperature: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>,
    factory: (Decimal, EnergyUnit) -> Value
) = byMultiplying(heatCapacity, Kelvin.deltaValue(temperature), factory)
