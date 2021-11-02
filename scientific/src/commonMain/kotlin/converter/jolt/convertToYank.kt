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

package com.splendo.kaluga.scientific.converter.jolt

import com.splendo.kaluga.scientific.ImperialJolt
import com.splendo.kaluga.scientific.ImperialWeight
import com.splendo.kaluga.scientific.Jolt
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricJolt
import com.splendo.kaluga.scientific.MetricWeight
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialWeight
import com.splendo.kaluga.scientific.USCustomaryWeight
import com.splendo.kaluga.scientific.Weight
import com.splendo.kaluga.scientific.converter.weight.times
import kotlin.jvm.JvmName

@JvmName("metricJoltTimesMetricWeight")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Jolt, MetricJolt>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = weight * this
@JvmName("imperialJoltTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Jolt, ImperialJolt>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = weight * this
@JvmName("imperialJoltTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Jolt, ImperialJolt>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = weight * this
@JvmName("imperialJoltTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Jolt, ImperialJolt>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = weight * this
@JvmName("joltTimesWeight")
infix operator fun <WeightUnit : Weight, JoltUnit : Jolt> ScientificValue<MeasurementType.Jolt, JoltUnit>.times(weight: ScientificValue<MeasurementType.Weight, WeightUnit>) = weight * this
