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

package com.splendo.kaluga.scientific.converter.temperature

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.Kelvin
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.SpecificHeatCapacity
import com.splendo.kaluga.scientific.unit.Temperature
import kotlin.jvm.JvmName

@JvmName("temperatureFromSpecificEnergyAndSpecificHeatCapacityDefault")
fun <
    SpecificEnergyUnit : SpecificEnergy,
    TemperatureUnit : Temperature,
    SpecificHeatCapacityUnit : SpecificHeatCapacity,
    > TemperatureUnit.temperature(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, SpecificEnergyUnit>,
    specificHeatCapacity: ScientificValue<PhysicalQuantity.SpecificHeatCapacity, SpecificHeatCapacityUnit>,
) = temperature(specificEnergy, specificHeatCapacity, ::DefaultScientificValue)

@JvmName("temperatureFromSpecificEnergyAndSpecificHeatCapacity")
fun <
    SpecificEnergyUnit : SpecificEnergy,
    TemperatureUnit : Temperature,
    SpecificHeatCapacityUnit : SpecificHeatCapacity,
    Value : ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>,
    > TemperatureUnit.temperature(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, SpecificEnergyUnit>,
    specificHeatCapacity: ScientificValue<PhysicalQuantity.SpecificHeatCapacity, SpecificHeatCapacityUnit>,
    factory: (Decimal, TemperatureUnit) -> Value,
) = deltaValue(
    Kelvin.byDividing(specificEnergy, specificHeatCapacity, ::DefaultScientificValue),
    factory,
)
