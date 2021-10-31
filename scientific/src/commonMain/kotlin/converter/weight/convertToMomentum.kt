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

package com.splendo.kaluga.scientific.converter.weight

import com.splendo.kaluga.scientific.ImperialSpeed
import com.splendo.kaluga.scientific.ImperialWeight
import com.splendo.kaluga.scientific.Kilogram
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Meter
import com.splendo.kaluga.scientific.MetricSpeed
import com.splendo.kaluga.scientific.MetricWeight
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Second
import com.splendo.kaluga.scientific.Speed
import com.splendo.kaluga.scientific.UKImperialWeight
import com.splendo.kaluga.scientific.USCustomaryWeight
import com.splendo.kaluga.scientific.Weight
import com.splendo.kaluga.scientific.converter.momentum.momentum
import com.splendo.kaluga.scientific.per
import com.splendo.kaluga.scientific.x
import kotlin.jvm.JvmName

@JvmName("metricWeightTimesMetricSpeed")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(speed: ScientificValue<MeasurementType.Speed, MetricSpeed>) = (unit x speed.unit).momentum(this, speed)
@JvmName("imperialWeightTimesImperialSpeed")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) = (unit x speed.unit).momentum(this, speed)
@JvmName("ukImperialWeightTimesImperialSpeed")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) = (unit x speed.unit).momentum(this, speed)
@JvmName("usCustomaryWeightTimesImperialSpeed")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) = (unit x speed.unit).momentum(this, speed)
@JvmName("weightTimesSpeed")
infix operator fun <WeightUnit : Weight, SpeedUnit : Speed> ScientificValue<MeasurementType.Weight, WeightUnit>.times(speed: ScientificValue<MeasurementType.Speed, SpeedUnit>) = (Kilogram x (Meter per Second)).momentum(this, speed)
