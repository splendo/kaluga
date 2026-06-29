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

package com.splendo.kaluga.scientific.converter.weight

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.areaDensity.areaDensity
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricWeightDivMetricArea")
infix operator fun <WeightUnit : MetricWeight, AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit per area.unit).areaDensity(this, area)

@JvmName("imperialWeightDivImperialArea")
infix operator fun <WeightUnit : ImperialWeight, AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit per area.unit).areaDensity(this, area)

@JvmName("ukImperialWeightDivImperialArea")
infix operator fun <WeightUnit : UKImperialWeight, AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit per area.unit).areaDensity(this, area)

@JvmName("usCustomaryWeightDivImperialArea")
infix operator fun <WeightUnit : USCustomaryWeight, AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit per area.unit).areaDensity(this, area)

@JvmName("weightDivArea")
infix operator fun <WeightUnit : Weight, AreaUnit : Area> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (Kilogram per SquareMeter).areaDensity(this, area)
