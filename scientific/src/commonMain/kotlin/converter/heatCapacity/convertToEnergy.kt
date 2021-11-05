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

package com.splendo.kaluga.scientific.converter.heatCapacity

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.energy.energy
import com.splendo.kaluga.scientific.unit.HeatCapacity
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialHeatCapacity
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialTemperature
import com.splendo.kaluga.scientific.unit.MetricHeatCapacity
import com.splendo.kaluga.scientific.unit.Temperature
import com.splendo.kaluga.scientific.unit.UKImperialHeatCapacity
import com.splendo.kaluga.scientific.unit.USCustomaryHeatCapacity
import com.splendo.kaluga.scientific.unit.USCustomaryTemperature
import kotlin.jvm.JvmName

@JvmName("metricAndUKImperialHeatCapacityTimesMetricAndUKImperialTemperature")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.HeatCapacity, MetricAndUKImperialHeatCapacity>.times(
    temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>
) = (unit.energy).energy(this, temperature)

@JvmName("metricHeatCapacityTimesMetricAndUKImperialTemperature")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.HeatCapacity, MetricHeatCapacity>.times(
    temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>
) = (unit.energy).energy(this, temperature)

@JvmName("ukImperialHeatCapacityTimesMetricAndUKImperialTemperature")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.HeatCapacity, UKImperialHeatCapacity>.times(
    temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>
) = (unit.energy).energy(this, temperature)

@JvmName("usCustomaryHeatCapacityTimesUSCustomaryTemperature")
infix operator fun <TemperatureUnit : USCustomaryTemperature> ScientificValue<MeasurementType.HeatCapacity, USCustomaryHeatCapacity>.times(
    temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>
) = (unit.energy).energy(this, temperature)

@JvmName("heatCapacityTimesTemperature")
infix operator fun <HeatCapacityUnit : HeatCapacity, TemperatureUnit : Temperature> ScientificValue<MeasurementType.HeatCapacity, HeatCapacityUnit>.times(
    temperature: ScientificValue<MeasurementType.Temperature, TemperatureUnit>
) = (unit.energy).energy(this, temperature)
