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

package com.splendo.kaluga.scientific.converter.temperature

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.heatCapacity.times
import com.splendo.kaluga.scientific.unit.HeatCapacity
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialHeatCapacity
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialTemperature
import com.splendo.kaluga.scientific.unit.MetricHeatCapacity
import com.splendo.kaluga.scientific.unit.Temperature
import com.splendo.kaluga.scientific.unit.UKImperialHeatCapacity
import com.splendo.kaluga.scientific.unit.USCustomaryHeatCapacity
import com.splendo.kaluga.scientific.unit.USCustomaryTemperature
import kotlin.jvm.JvmName

@JvmName("metricAndUKImperialTemperatureTimesMetricAndUKImperialHeatCapacity")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>.times(
    heatCapacity: ScientificValue<PhysicalQuantity.HeatCapacity, MetricAndUKImperialHeatCapacity>
) = heatCapacity * this

@JvmName("metricAndUKImperialTemperatureTimesMetricHeatCapacity")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>.times(
    heatCapacity: ScientificValue<PhysicalQuantity.HeatCapacity, MetricHeatCapacity>
) = heatCapacity * this

@JvmName("metricAndUKImperialTemperatureTimesUKImperialHeatCapacity")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>.times(
    heatCapacity: ScientificValue<PhysicalQuantity.HeatCapacity, UKImperialHeatCapacity>
) = heatCapacity * this

@JvmName("usCustomaryTemperatureTimesUSCustomaryHeatCapacity")
infix operator fun <TemperatureUnit : USCustomaryTemperature> ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>.times(
    heatCapacity: ScientificValue<PhysicalQuantity.HeatCapacity, USCustomaryHeatCapacity>
) = heatCapacity * this

@JvmName("temperatureTimesHeatCapacity")
infix operator fun <HeatCapacityUnit : HeatCapacity, TemperatureUnit : Temperature> ScientificValue<PhysicalQuantity.Temperature, TemperatureUnit>.times(
    heatCapacity: ScientificValue<PhysicalQuantity.HeatCapacity, HeatCapacityUnit>
) = heatCapacity * this
