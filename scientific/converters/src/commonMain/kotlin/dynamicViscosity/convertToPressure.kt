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

package com.splendo.kaluga.scientific.converter.dynamicViscosity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.pressure.pressure
import com.splendo.kaluga.scientific.unit.DynamicViscosity
import com.splendo.kaluga.scientific.unit.ImperialDynamicViscosity
import com.splendo.kaluga.scientific.unit.MetricDynamicViscosity
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.UKImperialDynamicViscosity
import com.splendo.kaluga.scientific.unit.USCustomaryDynamicViscosity
import kotlin.jvm.JvmName

@JvmName("metricDynamicViscosityDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.DynamicViscosity, MetricDynamicViscosity>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit.pressure).pressure(this, time)

@JvmName("imperialDynamicViscosityDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.DynamicViscosity, ImperialDynamicViscosity>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit.pressure).pressure(this, time)

@JvmName("ukImperialDynamicViscosityDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.DynamicViscosity, UKImperialDynamicViscosity>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit.pressure).pressure(this, time)

@JvmName("usCustomaryDynamicViscosityDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.DynamicViscosity, USCustomaryDynamicViscosity>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (unit.pressure).pressure(this, time)

@JvmName("dynamicViscosityDivTime")
infix operator fun <DynamicViscosityUnit : DynamicViscosity, TimeUnit : Time> ScientificValue<PhysicalQuantity.DynamicViscosity, DynamicViscosityUnit>.div(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = (unit.pressure).pressure(this, time)
