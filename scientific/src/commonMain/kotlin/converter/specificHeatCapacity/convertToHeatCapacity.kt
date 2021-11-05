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

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.heatCapacity.heatCapacity
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kelvin
import com.splendo.kaluga.scientific.unit.MetricSpecificHeatCapacity
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.SpecificHeatCapacity
import com.splendo.kaluga.scientific.unit.UKImperialSpecificHeatCapacity
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomarySpecificHeatCapacity
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricSpecificHeatCapacityTimesMetricWeight")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<PhysicalQuantity.SpecificHeatCapacity, MetricSpecificHeatCapacity>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit.energy per unit.perTemperature).heatCapacity(this, weight)

@JvmName("ukImperialSpecificHeatCapacityTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.SpecificHeatCapacity, UKImperialSpecificHeatCapacity>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit.energy per unit.perTemperature).heatCapacity(this, weight)

@JvmName("ukImperialSpecificHeatCapacityTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.SpecificHeatCapacity, UKImperialSpecificHeatCapacity>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit.energy per unit.perTemperature).heatCapacity(this, weight)

@JvmName("usCustomarySpecificHeatCapacityTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.SpecificHeatCapacity, USCustomarySpecificHeatCapacity>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit.energy per unit.perTemperature).heatCapacity(this, weight)

@JvmName("usCustomarySpecificHeatCapacityTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.SpecificHeatCapacity, USCustomarySpecificHeatCapacity>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (unit.energy per unit.perTemperature).heatCapacity(this, weight)

@JvmName("specificHeatCapacityTimesWeight")
infix operator fun <SpecificHeatCapacityUnit : SpecificHeatCapacity, WeightUnit : Weight> ScientificValue<PhysicalQuantity.SpecificHeatCapacity, SpecificHeatCapacityUnit>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = (Joule per Kelvin).heatCapacity(this, weight)
