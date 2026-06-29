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

package com.splendo.kaluga.scientific.converter.jolt

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.weight.times
import com.splendo.kaluga.scientific.unit.ImperialJolt
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.Jolt
import com.splendo.kaluga.scientific.unit.MetricJolt
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import kotlin.jvm.JvmName

@JvmName("metricJoltTimesMetricWeight")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<PhysicalQuantity.Jolt, MetricJolt>.times(weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    weight * this

@JvmName("imperialJoltTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Jolt, ImperialJolt>.times(weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    weight * this

@JvmName("imperialJoltTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Jolt, ImperialJolt>.times(weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    weight * this

@JvmName("imperialJoltTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Jolt, ImperialJolt>.times(weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    weight * this

@JvmName("joltTimesWeight")
infix operator fun <WeightUnit : Weight, JoltUnit : Jolt> ScientificValue<PhysicalQuantity.Jolt, JoltUnit>.times(weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    weight * this
