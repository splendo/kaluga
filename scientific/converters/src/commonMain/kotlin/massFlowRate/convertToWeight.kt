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

package com.splendo.kaluga.scientific.converter.massFlowRate

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.weight.mass
import com.splendo.kaluga.scientific.unit.ImperialMassFlowRate
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MassFlowRate
import com.splendo.kaluga.scientific.unit.MetricMassFlowRate
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.UKImperialMassFlowRate
import com.splendo.kaluga.scientific.unit.USCustomaryMassFlowRate
import kotlin.jvm.JvmName

@JvmName("metricMassFlowRateTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.MassFlowRate, MetricMassFlowRate>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    unit.weight.mass(this, time)

@JvmName("imperialMassFlowRateTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.MassFlowRate, ImperialMassFlowRate>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    unit.weight.mass(this, time)

@JvmName("ukImperialMassFlowRateTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.MassFlowRate, UKImperialMassFlowRate>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    unit.weight.mass(this, time)

@JvmName("usCustomaryMassFlowRateTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.MassFlowRate, USCustomaryMassFlowRate>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    unit.weight.mass(this, time)

@JvmName("massFlowRateTimesTime")
infix operator fun <MassFlowRateUnit : MassFlowRate, TimeUnit : Time> ScientificValue<PhysicalQuantity.MassFlowRate, MassFlowRateUnit>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = Kilogram.mass(this, time)
