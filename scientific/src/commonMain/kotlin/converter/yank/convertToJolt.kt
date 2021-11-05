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

package com.splendo.kaluga.scientific.converter.yank

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.converter.jolt.jolt
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.ImperialYank
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.MetricYank
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.UKImperialYank
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.USCustomaryYank
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.Yank
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricYankDivMetricWeight")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Yank, MetricYank>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = ((1.0(unit.force) / weight).unit per unit.per).jolt(this, weight)

@JvmName("imperialYankDivImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Yank, ImperialYank>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = ((1.0(unit.force) / weight).unit per unit.per).jolt(this, weight)

@JvmName("imperialYankDivUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Yank, ImperialYank>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = ((1.0(unit.force) / weight).unit per unit.per).jolt(this, weight)

@JvmName("imperialYankDivUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Yank, ImperialYank>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = ((1.0(unit.force) / weight).unit per unit.per).jolt(this, weight)

@JvmName("ukImperialYankDivImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Yank, UKImperialYank>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = ((1.0(unit.force) / weight).unit per unit.per).jolt(this, weight)

@JvmName("ukImperialYankDivUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Yank, UKImperialYank>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = ((1.0(unit.force) / weight).unit per unit.per).jolt(this, weight)

@JvmName("usCustomaryYankDivImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Yank, USCustomaryYank>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = ((1.0(unit.force) / weight).unit per unit.per).jolt(this, weight)

@JvmName("usCustomaryYankDivUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Yank, USCustomaryYank>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = ((1.0(unit.force) / weight).unit per unit.per).jolt(this, weight)

@JvmName("yankDivWeight")
infix operator fun <YankUnit : Yank, WeightUnit : Weight> ScientificValue<MeasurementType.Yank, YankUnit>.div(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>
) = ((1.0(unit.force) / weight).unit per unit.per).jolt(this, weight)
