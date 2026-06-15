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
import com.splendo.kaluga.scientific.converter.weight.mass
import com.splendo.kaluga.scientific.unit.ImperialMomentum
import com.splendo.kaluga.scientific.unit.ImperialSpeed
import com.splendo.kaluga.scientific.unit.MetricMomentum
import com.splendo.kaluga.scientific.unit.MetricSpeed
import com.splendo.kaluga.scientific.unit.Momentum
import com.splendo.kaluga.scientific.unit.Speed
import com.splendo.kaluga.scientific.unit.UKImperialMomentum
import com.splendo.kaluga.scientific.unit.USCustomaryMomentum
import kotlin.jvm.JvmName

@JvmName("metricMomentumDivMetricSpeed")
infix operator fun ScientificValue<PhysicalQuantity.Momentum, MetricMomentum>.div(speed: ScientificValue<PhysicalQuantity.Speed, MetricSpeed>) = unit.mass.mass(this, speed)

@JvmName("imperialMomentumDivImperialSpeed")
infix operator fun ScientificValue<PhysicalQuantity.Momentum, ImperialMomentum>.div(speed: ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>) = unit.mass.mass(this, speed)

@JvmName("ukImperialMomentumDivImperialSpeed")
infix operator fun ScientificValue<PhysicalQuantity.Momentum, UKImperialMomentum>.div(speed: ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>) = unit.mass.mass(this, speed)

@JvmName("usCustomaryMomentumDivImperialSpeed")
infix operator fun ScientificValue<PhysicalQuantity.Momentum, USCustomaryMomentum>.div(speed: ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>) = unit.mass.mass(this, speed)

@JvmName("momentumDivSpeed")
infix operator fun <MomentumUnit : Momentum, SpeedUnit : Speed> ScientificValue<PhysicalQuantity.Momentum, MomentumUnit>.div(
    speed: ScientificValue<PhysicalQuantity.Speed, SpeedUnit>,
) = unit.mass.mass(this, speed)
