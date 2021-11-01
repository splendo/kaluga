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

package com.splendo.kaluga.scientific.converter.momentum

import com.splendo.kaluga.scientific.Area
import com.splendo.kaluga.scientific.ImperialArea
import com.splendo.kaluga.scientific.ImperialMomentum
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricArea
import com.splendo.kaluga.scientific.MetricMomentum
import com.splendo.kaluga.scientific.Momentum
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialMomentum
import com.splendo.kaluga.scientific.USCustomaryMomentum
import com.splendo.kaluga.scientific.converter.dynamicViscosity.dynamicViscosity
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.x
import kotlin.jvm.JvmName

@JvmName("metricMomentumDivMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.Momentum, MetricMomentum>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (((this / 1(unit.speed.per)) / area).unit x unit.speed.per).dynamicViscosity(this, area)
@JvmName("imperialMomentumDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Momentum, ImperialMomentum>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (((this / 1(unit.speed.per)) / area).unit x unit.speed.per).dynamicViscosity(this, area)
@JvmName("ukImperialMomentumDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Momentum, UKImperialMomentum>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (((this / 1(unit.speed.per)) / area).unit x unit.speed.per).dynamicViscosity(this, area)
@JvmName("usCustomaryMomentumDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Momentum, USCustomaryMomentum>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (((this / 1(unit.speed.per)) / area).unit x unit.speed.per).dynamicViscosity(this, area)
@JvmName("momentumDivArea")
infix operator fun <MomentumUnit : Momentum, AreaUnit : Area> ScientificValue<MeasurementType.Momentum, MomentumUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (((this / 1(unit.speed.per)) / area).unit x unit.speed.per).dynamicViscosity(this, area)
