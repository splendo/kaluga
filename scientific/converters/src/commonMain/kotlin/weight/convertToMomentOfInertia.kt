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
import com.splendo.kaluga.scientific.converter.momentOfInertia.momentOfInertia
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("metricWeightTimesMetricArea")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(area: ScientificValue<PhysicalQuantity.Area, MetricArea>) =
    (unit x area.unit).momentOfInertia(this, area)

@JvmName("imperialWeightTimesImperialArea")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(area: ScientificValue<PhysicalQuantity.Area, ImperialArea>) =
    (unit x area.unit).momentOfInertia(this, area)

@JvmName("ukImperialWeightTimesImperialArea")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(area: ScientificValue<PhysicalQuantity.Area, ImperialArea>) =
    (unit x area.unit).momentOfInertia(this, area)

@JvmName("usCustomaryWeightTimesImperialArea")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(area: ScientificValue<PhysicalQuantity.Area, ImperialArea>) =
    (unit x area.unit).momentOfInertia(this, area)

@JvmName("weightTimesArea")
infix operator fun <WeightUnit : Weight, AreaUnit : Area> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (Kilogram x SquareMeter).momentOfInertia(this, area)
