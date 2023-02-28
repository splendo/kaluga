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

package com.splendo.kaluga.scientific.converter.energy

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.heatCapacity.heatCapacity
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.ImperialEnergy
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kelvin
import com.splendo.kaluga.scientific.unit.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialTemperature
import com.splendo.kaluga.scientific.unit.MetricEnergy
import com.splendo.kaluga.scientific.unit.Temperature
import com.splendo.kaluga.scientific.unit.USCustomaryTemperature
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricAndImperialEnergyDivMetricAndUKImperialTemperature")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    temperature: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>
) = (unit per temperature.unit).heatCapacity(this, temperature)

@JvmName("metricEnergyDivMetricAndUKImperialTemperature")
infix operator fun <EnergyUnit : MetricEnergy, TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    temperature: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>
) = (unit per temperature.unit).heatCapacity(this, temperature)

@JvmName("imperialEnergyDivMetricAndUKImperialTemperature")
infix operator fun <EnergyUnit : ImperialEnergy, TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    temperature: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>
) = (unit per temperature.unit).heatCapacity(this, temperature)

@JvmName("metricAndImperialEnergyDivUSCustomaryTemperature")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, TemperatureUnit : USCustomaryTemperature> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    temperature: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>
) = (unit per temperature.unit).heatCapacity(this, temperature)

@JvmName("imperialEnergyDivUSCustomaryTemperature")
infix operator fun <EnergyUnit : ImperialEnergy, TemperatureUnit : USCustomaryTemperature> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    temperature: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>
) = (unit per temperature.unit).heatCapacity(this, temperature)

@JvmName("energyDivTemperature")
infix operator fun <EnergyUnit : Energy, TemperatureUnit : Temperature> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.div(
    temperature: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>
) = (Joule per Kelvin).heatCapacity(this, temperature)
