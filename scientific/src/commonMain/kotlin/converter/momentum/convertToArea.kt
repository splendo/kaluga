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

import com.splendo.kaluga.scientific.DynamicViscosity
import com.splendo.kaluga.scientific.ImperialDynamicViscosity
import com.splendo.kaluga.scientific.ImperialMomentum
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricDynamicViscosity
import com.splendo.kaluga.scientific.MetricMomentum
import com.splendo.kaluga.scientific.Momentum
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialDynamicViscosity
import com.splendo.kaluga.scientific.UKImperialMomentum
import com.splendo.kaluga.scientific.USCustomaryDynamicViscosity
import com.splendo.kaluga.scientific.USCustomaryMomentum
import com.splendo.kaluga.scientific.converter.area.area
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.invoke
import kotlin.jvm.JvmName

@JvmName("metricMomentumDivMetricDynamicViscosity")
infix operator fun ScientificValue<MeasurementType.Momentum, MetricMomentum>.div(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, MetricDynamicViscosity>) = ((this / 1(unit.speed.per)) / 1(dynamicViscosity.unit.pressure)).unit.area(this, dynamicViscosity)
@JvmName("imperialMomentumDivImperialDynamicViscosity")
infix operator fun ScientificValue<MeasurementType.Momentum, ImperialMomentum>.div(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, ImperialDynamicViscosity>) = ((this / 1(unit.speed.per)) / 1(dynamicViscosity.unit.pressure)).unit.area(this, dynamicViscosity)
@JvmName("imperialMomentumDivUKImperialDynamicViscosity")
infix operator fun ScientificValue<MeasurementType.Momentum, ImperialMomentum>.div(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, UKImperialDynamicViscosity>) = ((this / 1(unit.speed.per)) / 1(dynamicViscosity.unit.pressure)).unit.area(this, dynamicViscosity)
@JvmName("imperialMomentumDivUSCustomaryDynamicViscosity")
infix operator fun ScientificValue<MeasurementType.Momentum, ImperialMomentum>.div(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, USCustomaryDynamicViscosity>) = ((this / 1(unit.speed.per)) / 1(dynamicViscosity.unit.pressure)).unit.area(this, dynamicViscosity)
@JvmName("ukImperialMomentumDivImperialDynamicViscosity")
infix operator fun ScientificValue<MeasurementType.Momentum, UKImperialMomentum>.div(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, ImperialDynamicViscosity>) = ((this / 1(unit.speed.per)) / 1(dynamicViscosity.unit.pressure)).unit.area(this, dynamicViscosity)
@JvmName("ukImperialMomentumDivUKImperialDynamicViscosity")
infix operator fun ScientificValue<MeasurementType.Momentum, UKImperialMomentum>.div(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, UKImperialDynamicViscosity>) = ((this / 1(unit.speed.per)) / 1(dynamicViscosity.unit.pressure)).unit.area(this, dynamicViscosity)
@JvmName("usCustomaryMomentumDivImperialDynamicViscosity")
infix operator fun ScientificValue<MeasurementType.Momentum, USCustomaryMomentum>.div(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, ImperialDynamicViscosity>) = ((this / 1(unit.speed.per)) / 1(dynamicViscosity.unit.pressure)).unit.area(this, dynamicViscosity)
@JvmName("usCustomaryMomentumDivUSCustomaryDynamicViscosity")
infix operator fun ScientificValue<MeasurementType.Momentum, USCustomaryMomentum>.div(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, USCustomaryDynamicViscosity>) = ((this / 1(unit.speed.per)) / 1(dynamicViscosity.unit.pressure)).unit.area(this, dynamicViscosity)
@JvmName("momentumDivDynamicViscosity")
infix operator fun <DynamicViscosityUnit : DynamicViscosity, MomentumUnit : Momentum> ScientificValue<MeasurementType.Momentum, MomentumUnit>.div(dynamicViscosity: ScientificValue<MeasurementType.DynamicViscosity, DynamicViscosityUnit>) = ((this / 1(unit.speed.per)) / 1(dynamicViscosity.unit.pressure)).unit.area(this, dynamicViscosity)
