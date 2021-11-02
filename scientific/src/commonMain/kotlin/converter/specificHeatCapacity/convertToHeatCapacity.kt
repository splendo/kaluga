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

import com.splendo.kaluga.scientific.ImperialWeight
import com.splendo.kaluga.scientific.Joule
import com.splendo.kaluga.scientific.Kelvin
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricSpecificHeatCapacity
import com.splendo.kaluga.scientific.MetricWeight
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SpecificHeatCapacity
import com.splendo.kaluga.scientific.UKImperialSpecificHeatCapacity
import com.splendo.kaluga.scientific.UKImperialWeight
import com.splendo.kaluga.scientific.USCustomarySpecificHeatCapacity
import com.splendo.kaluga.scientific.USCustomaryWeight
import com.splendo.kaluga.scientific.Weight
import com.splendo.kaluga.scientific.converter.heatCapacity.heatCapacity
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("metricSpecificHeatCapacityTimesMetricWeight")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.SpecificHeatCapacity, MetricSpecificHeatCapacity>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy per unit.perTemperature).heatCapacity(this, weight)
@JvmName("ukImperialSpecificHeatCapacityTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.SpecificHeatCapacity, UKImperialSpecificHeatCapacity>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy per unit.perTemperature).heatCapacity(this, weight)
@JvmName("ukImperialSpecificHeatCapacityTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.SpecificHeatCapacity, UKImperialSpecificHeatCapacity>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy per unit.perTemperature).heatCapacity(this, weight)
@JvmName("usCustomarySpecificHeatCapacityTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.SpecificHeatCapacity, USCustomarySpecificHeatCapacity>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy per unit.perTemperature).heatCapacity(this, weight)
@JvmName("usCustomarySpecificHeatCapacityTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.SpecificHeatCapacity, USCustomarySpecificHeatCapacity>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (unit.energy per unit.perTemperature).heatCapacity(this, weight)
@JvmName("specificHeatCapacityTimesWeight")
infix operator fun <SpecificHeatCapacityUnit : SpecificHeatCapacity, WeightUnit : Weight> ScientificValue<MeasurementType.SpecificHeatCapacity, SpecificHeatCapacityUnit>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = (Joule per Kelvin).heatCapacity(this, weight)
