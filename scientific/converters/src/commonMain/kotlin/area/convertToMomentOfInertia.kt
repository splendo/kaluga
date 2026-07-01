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

package com.splendo.kaluga.scientific.converter.area

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.weight.times
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import kotlin.jvm.JvmName

@JvmName("metricAreaTimesMetricWeight")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<PhysicalQuantity.Area, MetricArea>.times(weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    weight * this

@JvmName("imperialAreaTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Area, ImperialArea>.times(weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    weight * this

@JvmName("imperialAreaTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Area, ImperialArea>.times(weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    weight * this

@JvmName("imperialAreaTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Area, ImperialArea>.times(weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    weight * this

@JvmName("areaTimesWeight")
infix operator fun <WeightUnit : Weight, AreaUnit : Area> ScientificValue<PhysicalQuantity.Area, AreaUnit>.times(weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    weight * this
