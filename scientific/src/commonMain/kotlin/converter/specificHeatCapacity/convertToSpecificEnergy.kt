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

package com.splendo.kaluga.scientific.converter.specificHeatCapacity

import com.splendo.kaluga.scientific.Joule
import com.splendo.kaluga.scientific.Kilogram
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricAndUKImperialTemperature
import com.splendo.kaluga.scientific.MetricSpecificHeatCapacity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SpecificHeatCapacity
import com.splendo.kaluga.scientific.Temperature
import com.splendo.kaluga.scientific.UKImperialSpecificHeatCapacity
import com.splendo.kaluga.scientific.USCustomarySpecificHeatCapacity
import com.splendo.kaluga.scientific.USCustomaryTemperature
import com.splendo.kaluga.scientific.converter.specificEnergy.specificEnergy
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("metricSpecificHeatCapacityTimesMetricAndUKImperialTemperature")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.SpecificHeatCapacity, MetricSpecificHeatCapacity>.times(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (unit.energy per unit.perWeight).specificEnergy(this, temperature)
@JvmName("ukImperialSpecificHeatCapacityTimesMetricAndUKImperialTemperature")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.SpecificHeatCapacity, UKImperialSpecificHeatCapacity>.times(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (unit.energy per unit.perWeight).specificEnergy(this, temperature)
@JvmName("usCustomarySpecificHeatCapacityTimesUSCustomaryTemperature")
infix operator fun <TemperatureUnit : USCustomaryTemperature> ScientificValue<MeasurementType.SpecificHeatCapacity, USCustomarySpecificHeatCapacity>.times(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (unit.energy per unit.perWeight).specificEnergy(this, temperature)
@JvmName("heatCapacityTimesTemperature")
infix operator fun <SpecificHeatCapacityUnit : SpecificHeatCapacity, TemperatureUnit : Temperature> ScientificValue<MeasurementType.SpecificHeatCapacity, SpecificHeatCapacityUnit>.times(temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>) = (Joule per Kilogram).specificEnergy(this, temperature)
