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

import com.splendo.kaluga.scientific.HeatCapacity
import com.splendo.kaluga.scientific.Kilogram
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricAndUKImperialHeatCapacity
import com.splendo.kaluga.scientific.MetricHeatCapacity
import com.splendo.kaluga.scientific.MetricSpecificHeatCapacity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SpecificHeatCapacity
import com.splendo.kaluga.scientific.UKImperialHeatCapacity
import com.splendo.kaluga.scientific.UKImperialSpecificHeatCapacity
import com.splendo.kaluga.scientific.USCustomaryHeatCapacity
import com.splendo.kaluga.scientific.USCustomarySpecificHeatCapacity
import com.splendo.kaluga.scientific.converter.weight.weight
import kotlin.jvm.JvmName

@JvmName("metricAndUKImperialHeatCapacityDivMetricSpecificHeatCapacity")
infix operator fun ScientificValue<MeasurementType.HeatCapacity, MetricAndUKImperialHeatCapacity>.div(specificHeatCapacity: ScientificValue<MeasurementType.SpecificHeatCapacity, MetricSpecificHeatCapacity>) = specificHeatCapacity.unit.perWeight.weight(this, specificHeatCapacity)
@JvmName("metricAndUKImperialHeatCapacityDivUKImperialSpecificHeatCapacity")
infix operator fun ScientificValue<MeasurementType.HeatCapacity, MetricAndUKImperialHeatCapacity>.div(specificHeatCapacity: ScientificValue<MeasurementType.SpecificHeatCapacity, UKImperialSpecificHeatCapacity>) = specificHeatCapacity.unit.perWeight.weight(this, specificHeatCapacity)
@JvmName("metricHeatCapacityDivMetricSpecificHeatCapacity")
infix operator fun ScientificValue<MeasurementType.HeatCapacity, MetricHeatCapacity>.div(specificHeatCapacity: ScientificValue<MeasurementType.SpecificHeatCapacity, MetricSpecificHeatCapacity>) = specificHeatCapacity.unit.perWeight.weight(this, specificHeatCapacity)
@JvmName("ukImperialHeatCapacityDivUKImperialSpecificHeatCapacity")
infix operator fun ScientificValue<MeasurementType.HeatCapacity, UKImperialHeatCapacity>.div(specificHeatCapacity: ScientificValue<MeasurementType.SpecificHeatCapacity, UKImperialSpecificHeatCapacity>) = specificHeatCapacity.unit.perWeight.weight(this, specificHeatCapacity)
@JvmName("usCustomaryHeatCapacityDivUSCustomarySpecificHeatCapacity")
infix operator fun ScientificValue<MeasurementType.HeatCapacity, USCustomaryHeatCapacity>.div(specificHeatCapacity: ScientificValue<MeasurementType.SpecificHeatCapacity, USCustomarySpecificHeatCapacity>) = specificHeatCapacity.unit.perWeight.weight(this, specificHeatCapacity)
@JvmName("heatCapacityDivSpecificHeatCapacity")
infix operator fun <HeatCapacityUnit : HeatCapacity, SpecificHeatCapacityUnit : SpecificHeatCapacity> ScientificValue<MeasurementType.HeatCapacity, HeatCapacityUnit>.div(specificHeatCapacity: ScientificValue<MeasurementType.SpecificHeatCapacity, SpecificHeatCapacityUnit>) = Kilogram.weight(this, specificHeatCapacity)
