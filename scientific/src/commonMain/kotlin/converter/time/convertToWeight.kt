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

package com.splendo.kaluga.scientific.converter.time

import com.splendo.kaluga.scientific.ImperialMassFlowRate
import com.splendo.kaluga.scientific.MassFlowRate
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricMassFlowRate
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.UKImperialMassFlowRate
import com.splendo.kaluga.scientific.USCustomaryMassFlowRate
import com.splendo.kaluga.scientific.converter.massFlowRate.times
import kotlin.jvm.JvmName

@JvmName("timeTimesMetricMassFlowRate")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, MetricMassFlowRate>) = massFlowRate * this
@JvmName("timeTimesImperialMassFlowRate")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, ImperialMassFlowRate>) = massFlowRate * this
@JvmName("timeUKImperialMassFlowRate")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, UKImperialMassFlowRate>) = massFlowRate * this
@JvmName("timeTimesUSCustomaryMassFlowRate")
infix operator fun <TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, USCustomaryMassFlowRate>) = massFlowRate * this
@JvmName("timeTimesMassFlowRate")
infix operator fun <MassFlowRateUnit : MassFlowRate, TimeUnit : Time> ScientificValue<MeasurementType.Time, TimeUnit>.times(massFlowRate: ScientificValue<MeasurementType.MassFlowRate, MassFlowRateUnit>) = massFlowRate * this
