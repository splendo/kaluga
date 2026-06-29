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

package com.splendo.kaluga.scientific.converter.momentum

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.force.force
import com.splendo.kaluga.scientific.unit.ImperialMomentum
import com.splendo.kaluga.scientific.unit.MetricMomentum
import com.splendo.kaluga.scientific.unit.Momentum
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.UKImperialMomentum
import com.splendo.kaluga.scientific.unit.USCustomaryMomentum
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import kotlin.jvm.JvmName

@JvmName("metricMomentumDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Momentum, MetricMomentum>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    Newton.force(this, time)

@JvmName("imperialMomentumDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Momentum, ImperialMomentum>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    PoundForce.force(this, time)

@JvmName("ukImperialMomentumDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Momentum, UKImperialMomentum>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    PoundForce.ukImperial.force(this, time)

@JvmName("usCustomaryMomentumDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.Momentum, USCustomaryMomentum>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    PoundForce.usCustomary.force(this, time)

@JvmName("momentumDivTime")
infix operator fun <MomentumUnit : Momentum, TimeUnit : Time> ScientificValue<PhysicalQuantity.Momentum, MomentumUnit>.div(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = Newton.force(this, time)
