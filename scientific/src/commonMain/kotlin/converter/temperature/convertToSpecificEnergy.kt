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

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricAndUKImperialTemperature
import com.splendo.kaluga.scientific.MetricSpecificHeatCapacity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SpecificHeatCapacity
import com.splendo.kaluga.scientific.Temperature
import com.splendo.kaluga.scientific.UKImperialSpecificHeatCapacity
import com.splendo.kaluga.scientific.USCustomarySpecificHeatCapacity
import com.splendo.kaluga.scientific.USCustomaryTemperature
import com.splendo.kaluga.scientific.converter.specificHeatCapacity.times
import kotlin.jvm.JvmName

@JvmName("metricAndUKImperialTemperatureTimesMetricSpecificHeatCapacity")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.Temperature, TemperatureUnit>.times(specificHeatCapacity: ScientificValue<MeasurementType.SpecificHeatCapacity, MetricSpecificHeatCapacity>) = specificHeatCapacity * this
@JvmName("metricAndUKImperialTemperatureTimesUKImperialSpecificHeatCapacity")
infix operator fun <TemperatureUnit : MetricAndUKImperialTemperature> ScientificValue<MeasurementType.Temperature, TemperatureUnit>.times(specificHeatCapacity: ScientificValue<MeasurementType.SpecificHeatCapacity, UKImperialSpecificHeatCapacity>) = specificHeatCapacity * this
@JvmName("usCustomaryTemperatureTimesUSCustomarySpecificHeatCapacity")
infix operator fun <TemperatureUnit : USCustomaryTemperature> ScientificValue<MeasurementType.Temperature, TemperatureUnit>.times(specificHeatCapacity: ScientificValue<MeasurementType.SpecificHeatCapacity, USCustomarySpecificHeatCapacity>) = specificHeatCapacity * this
@JvmName("temperatureTimesHeatCapacity")
infix operator fun <SpecificHeatCapacityUnit : SpecificHeatCapacity, TemperatureUnit : Temperature> ScientificValue<MeasurementType.Temperature, TemperatureUnit>.times(specificHeatCapacity: ScientificValue<MeasurementType.SpecificHeatCapacity, SpecificHeatCapacityUnit>) = specificHeatCapacity * this
