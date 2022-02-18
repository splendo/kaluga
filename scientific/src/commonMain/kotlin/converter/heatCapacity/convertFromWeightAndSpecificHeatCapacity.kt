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

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.HeatCapacity
import com.splendo.kaluga.scientific.unit.SpecificHeatCapacity
import com.splendo.kaluga.scientific.unit.Weight
import kotlin.jvm.JvmName

@JvmName("heatCapacityFromWeightAndSpecificHeatCapacityDefault")
fun <
    HeatCapacityUnit : HeatCapacity,
    WeightUnit : Weight,
    SpecificHeatCapacityUnit : SpecificHeatCapacity
    > HeatCapacityUnit.heatCapacity(
    specificHeatCapacity: ScientificValue<PhysicalQuantity.SpecificHeatCapacity, SpecificHeatCapacityUnit>,
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>
) = heatCapacity(specificHeatCapacity, weight, ::DefaultScientificValue)

@JvmName("heatCapacityFromWeightAndSpecificHeatCapacity")
fun <
    HeatCapacityUnit : HeatCapacity,
    WeightUnit : Weight,
    SpecificHeatCapacityUnit : SpecificHeatCapacity,
    Value : ScientificValue<PhysicalQuantity.HeatCapacity, HeatCapacityUnit>
    > HeatCapacityUnit.heatCapacity(
    specificHeatCapacity: ScientificValue<PhysicalQuantity.SpecificHeatCapacity, SpecificHeatCapacityUnit>,
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
    factory: (Decimal, HeatCapacityUnit) -> Value
) = byMultiplying(specificHeatCapacity, weight, factory)
