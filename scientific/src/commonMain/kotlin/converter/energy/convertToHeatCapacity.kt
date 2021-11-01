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

import com.splendo.kaluga.scientific.Energy
import com.splendo.kaluga.scientific.ImperialEnergy
import com.splendo.kaluga.scientific.Joule
import com.splendo.kaluga.scientific.Kelvin
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.MetricAndUKImperialTemperature
import com.splendo.kaluga.scientific.MetricEnergy
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Temperature
import com.splendo.kaluga.scientific.USCustomaryTemperature
import com.splendo.kaluga.scientific.converter.heatCapacity.heatCapacity
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("metricAndImperialEnergyDivMetricAndUKImperialTemperature")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (unit per temperature.unit).heatCapacity(this, temperature)
@JvmName("metricEnergyDivMetricAndUKImperialTemperature")
infix operator fun <EnergyUnit : MetricEnergy, TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (unit per temperature.unit).heatCapacity(this, temperature)
@JvmName("imperialEnergyDivMetricAndUKImperialTemperature")
infix operator fun <EnergyUnit : ImperialEnergy, TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (unit per temperature.unit).heatCapacity(this, temperature)
@JvmName("metricAndImperialEnergyDivUSCustomaryTemperature")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, TemperatureUnit : USCustomaryTemperature> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (unit per temperature.unit).heatCapacity(this, temperature)
@JvmName("imperialEnergyDivUSCustomaryTemperature")
infix operator fun <EnergyUnit : ImperialEnergy, TemperatureUnit : USCustomaryTemperature> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (unit per temperature.unit).heatCapacity(this, temperature)
@JvmName("energyDivTemperature")
infix operator fun <EnergyUnit : Energy, TemperatureUnit : Temperature> ScientificValue<MeasurementType.Energy, EnergyUnit>.div(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (Joule per Kelvin).heatCapacity(this, temperature)
