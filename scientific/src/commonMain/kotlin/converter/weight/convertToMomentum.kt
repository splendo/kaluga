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
import com.splendo.kaluga.scientific.converter.momentum.momentum
import com.splendo.kaluga.scientific.unit.ImperialSpeed
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricSpeed
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Speed
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("metricWeightTimesMetricSpeed")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(speed: ScientificValue<PhysicalQuantity.Speed, MetricSpeed>) =
    (unit x speed.unit).momentum(this, speed)

@JvmName("imperialWeightTimesImperialSpeed")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(speed: ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>) =
    (unit x speed.unit).momentum(this, speed)

@JvmName("ukImperialWeightTimesImperialSpeed")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(speed: ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>) =
    (unit x speed.unit).momentum(this, speed)

@JvmName("usCustomaryWeightTimesImperialSpeed")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(speed: ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>) =
    (unit x speed.unit).momentum(this, speed)

@JvmName("weightTimesSpeed")
infix operator fun <WeightUnit : Weight, SpeedUnit : Speed> ScientificValue<PhysicalQuantity.Weight, WeightUnit>.times(speed: ScientificValue<PhysicalQuantity.Speed, SpeedUnit>) =
    (Kilogram x (Meter per Second)).momentum(this, speed)
