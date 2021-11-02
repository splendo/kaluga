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

import com.splendo.kaluga.scientific.ImperialWeight
import com.splendo.kaluga.scientific.Kilogram
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricWeight
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.UKImperialWeight
import com.splendo.kaluga.scientific.USCustomaryWeight
import com.splendo.kaluga.scientific.Weight
import com.splendo.kaluga.scientific.converter.massFlowRate.massFlowRate
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("metricWeightDivTime")
infix operator fun <WeightUnit : MetricWeight, TimeUnit : Time> ScientificValue<MeasurementType.Weight, WeightUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).massFlowRate(this, time)
@JvmName("imperialWeightDivTime")
infix operator fun <WeightUnit : ImperialWeight, TimeUnit : Time> ScientificValue<MeasurementType.Weight, WeightUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).massFlowRate(this, time)
@JvmName("ukImperialWeightDivTime")
infix operator fun <WeightUnit : UKImperialWeight, TimeUnit : Time> ScientificValue<MeasurementType.Weight, WeightUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).massFlowRate(this, time)
@JvmName("usCustomaryWeightDivTime")
infix operator fun <WeightUnit : USCustomaryWeight, TimeUnit : Time> ScientificValue<MeasurementType.Weight, WeightUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit per time.unit).massFlowRate(this, time)
@JvmName("weightDivTime")
infix operator fun <WeightUnit : Weight, TimeUnit : Time> ScientificValue<MeasurementType.Weight, WeightUnit>.div(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Kilogram per time.unit).massFlowRate(this, time)
