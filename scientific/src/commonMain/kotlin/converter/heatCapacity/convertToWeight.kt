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

package com.splendo.kaluga.scientific.converter.heatCapacity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.weight.weight
import com.splendo.kaluga.scientific.unit.HeatCapacity
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialHeatCapacity
import com.splendo.kaluga.scientific.unit.MetricHeatCapacity
import com.splendo.kaluga.scientific.unit.MetricSpecificHeatCapacity
import com.splendo.kaluga.scientific.unit.SpecificHeatCapacity
import com.splendo.kaluga.scientific.unit.UKImperialHeatCapacity
import com.splendo.kaluga.scientific.unit.UKImperialSpecificHeatCapacity
import com.splendo.kaluga.scientific.unit.USCustomaryHeatCapacity
import com.splendo.kaluga.scientific.unit.USCustomarySpecificHeatCapacity
import kotlin.jvm.JvmName

@JvmName("metricAndUKImperialHeatCapacityDivMetricSpecificHeatCapacity")
infix operator fun ScientificValue<PhysicalQuantity.HeatCapacity, MetricAndUKImperialHeatCapacity>.div(
    specificHeatCapacity: ScientificValue<PhysicalQuantity.SpecificHeatCapacity, MetricSpecificHeatCapacity>,
) = specificHeatCapacity.unit.perWeight.weight(this, specificHeatCapacity)

@JvmName("metricAndUKImperialHeatCapacityDivUKImperialSpecificHeatCapacity")
infix operator fun ScientificValue<PhysicalQuantity.HeatCapacity, MetricAndUKImperialHeatCapacity>.div(
    specificHeatCapacity: ScientificValue<PhysicalQuantity.SpecificHeatCapacity, UKImperialSpecificHeatCapacity>,
) = specificHeatCapacity.unit.perWeight.weight(this, specificHeatCapacity)

@JvmName("metricHeatCapacityDivMetricSpecificHeatCapacity")
infix operator fun ScientificValue<PhysicalQuantity.HeatCapacity, MetricHeatCapacity>.div(
    specificHeatCapacity: ScientificValue<PhysicalQuantity.SpecificHeatCapacity, MetricSpecificHeatCapacity>,
) = specificHeatCapacity.unit.perWeight.weight(this, specificHeatCapacity)

@JvmName("ukImperialHeatCapacityDivUKImperialSpecificHeatCapacity")
infix operator fun ScientificValue<PhysicalQuantity.HeatCapacity, UKImperialHeatCapacity>.div(
    specificHeatCapacity: ScientificValue<PhysicalQuantity.SpecificHeatCapacity, UKImperialSpecificHeatCapacity>,
) = specificHeatCapacity.unit.perWeight.weight(this, specificHeatCapacity)

@JvmName("usCustomaryHeatCapacityDivUSCustomarySpecificHeatCapacity")
infix operator fun ScientificValue<PhysicalQuantity.HeatCapacity, USCustomaryHeatCapacity>.div(
    specificHeatCapacity: ScientificValue<PhysicalQuantity.SpecificHeatCapacity, USCustomarySpecificHeatCapacity>,
) = specificHeatCapacity.unit.perWeight.weight(this, specificHeatCapacity)

@JvmName("heatCapacityDivSpecificHeatCapacity")
infix operator fun <HeatCapacityUnit : HeatCapacity, SpecificHeatCapacityUnit : SpecificHeatCapacity> ScientificValue<PhysicalQuantity.HeatCapacity, HeatCapacityUnit>.div(
    specificHeatCapacity: ScientificValue<PhysicalQuantity.SpecificHeatCapacity, SpecificHeatCapacityUnit>,
) = Kilogram.weight(this, specificHeatCapacity)
