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
import com.splendo.kaluga.scientific.converter.speed.speed
import com.splendo.kaluga.scientific.unit.ImperialMomentum
import com.splendo.kaluga.scientific.unit.ImperialWeight
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricMomentum
import com.splendo.kaluga.scientific.unit.MetricWeight
import com.splendo.kaluga.scientific.unit.Momentum
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.UKImperialMomentum
import com.splendo.kaluga.scientific.unit.UKImperialWeight
import com.splendo.kaluga.scientific.unit.USCustomaryMomentum
import com.splendo.kaluga.scientific.unit.USCustomaryWeight
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricMomentumDivMetricMass")
infix operator fun <WeightUnit : MetricWeight> ScientificValue<PhysicalQuantity.Momentum, MetricMomentum>.div(mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    unit.speed.speed(this, mass)

@JvmName("imperialMomentumDivImperialMass")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Momentum, ImperialMomentum>.div(mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    unit.speed.speed(this, mass)

@JvmName("imperialMomentumDivUKImperialMass")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Momentum, ImperialMomentum>.div(mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    unit.speed.speed(this, mass)

@JvmName("imperialMomentumDivUSCustomaryMass")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Momentum, ImperialMomentum>.div(mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    unit.speed.speed(this, mass)

@JvmName("ukImperialMomentumDivImperialMass")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Momentum, UKImperialMomentum>.div(mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    unit.speed.speed(this, mass)

@JvmName("ukImperialMomentumDivUKImperialMass")
infix operator fun <WeightUnit : UKImperialWeight> ScientificValue<PhysicalQuantity.Momentum, UKImperialMomentum>.div(mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    unit.speed.speed(this, mass)

@JvmName("usCustomaryMomentumDivImperialMass")
infix operator fun <WeightUnit : ImperialWeight> ScientificValue<PhysicalQuantity.Momentum, USCustomaryMomentum>.div(mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>) =
    unit.speed.speed(this, mass)

@JvmName("usCustomaryMomentumDivUSCustomaryMass")
infix operator fun <WeightUnit : USCustomaryWeight> ScientificValue<PhysicalQuantity.Momentum, USCustomaryMomentum>.div(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = unit.speed.speed(this, mass)

@JvmName("momentumDivMass")
infix operator fun <MomentumUnit : Momentum, WeightUnit : Weight> ScientificValue<PhysicalQuantity.Momentum, MomentumUnit>.div(
    mass: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = (Meter per Second).speed(this, mass)
