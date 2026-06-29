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

package com.splendo.kaluga.scientific.converter.speed

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.weight.times
import com.splendo.kaluga.scientific.unit.ImperialSpeed
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.MetricSpeed
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.Speed
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import kotlin.jvm.JvmName

@JvmName("metricSpeedTimesMetricWeight")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<PhysicalQuantity.Speed, MetricSpeed>.times(weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    weight * this

@JvmName("imperialSpeedTimesImperialWeight")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>.times(weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    weight * this

@JvmName("imperialSpeedTimesUKImperialWeight")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>.times(weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    weight * this

@JvmName("imperialSpeedTimesUSCustomaryWeight")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>.times(weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    weight * this

@JvmName("speedTimesWeight")
infix operator fun <WeightUnit : Weight, SpeedUnit : Speed> ScientificValue<PhysicalQuantity.Speed, SpeedUnit>.times(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = weight * this
