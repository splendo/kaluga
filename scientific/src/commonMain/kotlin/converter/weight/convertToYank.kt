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
import com.splendo.kaluga.scientific.converter.yank.yank
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.ImperialJolt
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.Jolt
import com.splendo.kaluga.scientific.unit.MetricJolt
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricWeightTimesMetricJolt")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(jolt: ScientificValue<PhysicalQuantity.Jolt, MetricJolt>) =
    ((this * 1.0(jolt.unit.acceleration)).unit per jolt.unit.per).yank(this, jolt)

@JvmName("imperialWeightTimesImperialJolt")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(jolt: ScientificValue<PhysicalQuantity.Jolt, ImperialJolt>) =
    ((this * 1.0(jolt.unit.acceleration)).unit per jolt.unit.per).yank(this, jolt)

@JvmName("ukImperialWeightTimesImperialJolt")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(jolt: ScientificValue<PhysicalQuantity.Jolt, ImperialJolt>) =
    ((this * 1.0(jolt.unit.acceleration)).unit per jolt.unit.per).yank(this, jolt)

@JvmName("usCustomaryWeightTimesImperialJolt")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(jolt: ScientificValue<PhysicalQuantity.Jolt, ImperialJolt>) =
    ((this * 1.0(jolt.unit.acceleration)).unit per jolt.unit.per).yank(this, jolt)

@JvmName("weightTimesJolt")
infix operator fun <WeightUnit : Weight, JoltUnit : Jolt> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(jolt: ScientificValue<PhysicalQuantity.Jolt, JoltUnit>) =
    ((this * 1.0(jolt.unit.acceleration)).unit per jolt.unit.per).yank(this, jolt)
