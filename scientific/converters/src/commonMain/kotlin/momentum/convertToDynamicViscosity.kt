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
import com.splendo.kaluga.scientific.converter.dynamicViscosity.dynamicViscosity
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialMomentum
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricMomentum
import com.splendo.kaluga.scientific.unit.Momentum
import com.splendo.kaluga.scientific.unit.UKImperialMomentum
import com.splendo.kaluga.scientific.unit.USCustomaryMomentum
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("metricMomentumDivMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.Momentum, MetricMomentum>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (((this / 1(unit.speed.per)) / area).unit x unit.speed.per).dynamicViscosity(this, area)

@JvmName("imperialMomentumDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Momentum, ImperialMomentum>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (((this / 1(unit.speed.per)) / area).unit x unit.speed.per).dynamicViscosity(this, area)

@JvmName("ukImperialMomentumDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Momentum, UKImperialMomentum>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (((this / 1(unit.speed.per)) / area).unit x unit.speed.per).dynamicViscosity(this, area)

@JvmName("usCustomaryMomentumDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Momentum, USCustomaryMomentum>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (((this / 1(unit.speed.per)) / area).unit x unit.speed.per).dynamicViscosity(this, area)

@JvmName("momentumDivArea")
infix operator fun <MomentumUnit : Momentum, AreaUnit : Area> ScientificValue<PhysicalQuantity.Momentum, MomentumUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (((this / 1(unit.speed.per)) / area).unit x unit.speed.per).dynamicViscosity(this, area)
