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
import com.splendo.kaluga.scientific.converter.specificHeatCapacity.specificHeatCapacity
import com.splendo.kaluga.scientific.unit.HeatCapacity
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kelvin
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialHeatCapacity
import com.splendo.kaluga.scientific.unit.MetricHeatCapacity
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.UKImperialHeatCapacity
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryHeatCapacity
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricAndUKImperialHeatCapacityDivMetricWeight")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.HeatCapacity, MetricAndUKImperialHeatCapacity>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (unit per weight.unit).specificHeatCapacity(this, weight)

@JvmName("metricAndUKImperialHeatCapacityDivImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.HeatCapacity, MetricAndUKImperialHeatCapacity>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (unit per weight.unit).specificHeatCapacity(this, weight)

@JvmName("metricAndUKImperialHeatCapacityDivUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.HeatCapacity, MetricAndUKImperialHeatCapacity>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (unit per weight.unit).specificHeatCapacity(this, weight)

@JvmName("metricHeatCapacityDivMetricWeight")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.HeatCapacity, MetricHeatCapacity>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (unit per weight.unit).specificHeatCapacity(this, weight)

@JvmName("ukImperialHeatCapacityDivImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.HeatCapacity, UKImperialHeatCapacity>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (unit per weight.unit).specificHeatCapacity(this, weight)

@JvmName("ukImperialHeatCapacityDivUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.HeatCapacity, UKImperialHeatCapacity>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (unit per weight.unit).specificHeatCapacity(this, weight)

@JvmName("usCustomaryHeatCapacityDivImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.HeatCapacity, USCustomaryHeatCapacity>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (unit per weight.unit).specificHeatCapacity(this, weight)

@JvmName("usCustomaryHeatCapacityDivUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.HeatCapacity, USCustomaryHeatCapacity>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (unit per weight.unit).specificHeatCapacity(this, weight)

@JvmName("heatCapacityDivWeight")
infix operator fun <HeatCapacityUnit : HeatCapacity, WeightUnit : Weight> ScientificValue<MeasurementType.HeatCapacity, HeatCapacityUnit>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = (Joule per Kelvin per Kilogram).specificHeatCapacity(this, weight)
