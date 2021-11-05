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

package com.splendo.kaluga.scientific.converter.specificEnergy

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.specificHeatCapacity.specificHeatCapacity
import com.splendo.kaluga.scientific.unit.ImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kelvin
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialTemperature
import com.splendo.kaluga.scientific.unit.MetricSpecificEnergy
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.Temperature
import com.splendo.kaluga.scientific.unit.UKImperialSpecificEnergy
import com.splendo.kaluga.scientific.unit.USCustomarySpecificEnergy
import com.splendo.kaluga.scientific.unit.USCustomaryTemperature
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricSpecificEnergyDivMetricAndUKImperialTemperature")
infix operator fun ScientificValue<PhysicalQuantity.SpecificEnergy, MetricSpecificEnergy>.div(
    temperature: ScientificValue<PhysicalQuantity.Temperature, MetricAndUKImperialTemperature>
) = (unit per temperature.unit).specificHeatCapacity(this, temperature)

@JvmName("imperialSpecificEnergyDivMetricAndUKImperialTemperature")
infix operator fun ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>.div(
    temperature: ScientificValue<PhysicalQuantity.Temperature, MetricAndUKImperialTemperature>
) = (unit per temperature.unit).specificHeatCapacity(this, temperature)

@JvmName("imperialSpecificEnergyDivUSCustomaryTemperature")
infix operator fun ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>.div(
    temperature: ScientificValue<PhysicalQuantity.Temperature, USCustomaryTemperature>
) = (unit per temperature.unit).specificHeatCapacity(this, temperature)

@JvmName("ukImperialSpecificEnergyDivMetricAndUKImperialTemperature")
infix operator fun ScientificValue<PhysicalQuantity.SpecificEnergy, UKImperialSpecificEnergy>.div(
    temperature: ScientificValue<PhysicalQuantity.Temperature, MetricAndUKImperialTemperature>
) = (unit per temperature.unit).specificHeatCapacity(this, temperature)

@JvmName("usCustomarySpecificEnergyDivUSCustomaryTemperature")
infix operator fun ScientificValue<PhysicalQuantity.SpecificEnergy, USCustomarySpecificEnergy>.div(
    temperature: ScientificValue<PhysicalQuantity.Temperature, USCustomaryTemperature>
) = (unit per temperature.unit).specificHeatCapacity(this, temperature)

@JvmName("specificEnergyDivTemperature")
infix operator fun <SpecificEnergyUnit : SpecificEnergy, TemperatureUnit : Temperature> ScientificValue<PhysicalQuantity.SpecificEnergy, SpecificEnergyUnit>.div(
    temperature: ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>
) = (Joule per Kelvin per Kilogram).specificHeatCapacity(this, temperature)
