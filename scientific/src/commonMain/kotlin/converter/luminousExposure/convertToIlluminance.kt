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

package com.splendo.kaluga.scientific.converter.luminousExposure

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.illuminance.illuminance
import com.splendo.kaluga.scientific.unit.ImperialLuminousExposure
import com.splendo.kaluga.scientific.unit.LuminousExposure
import com.splendo.kaluga.scientific.unit.MetricLuminousExposure
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("metricLuminousExposureDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.LuminousExposure, MetricLuminousExposure>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    unit.illuminance.illuminance(this, time)

@JvmName("imperialLuminousExposureDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.LuminousExposure, ImperialLuminousExposure>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    unit.illuminance.illuminance(this, time)

@JvmName("luminousExposureDivTime")
infix operator fun <LuminousExposureUnit : LuminousExposure, TimeUnit : Time> ScientificValue<PhysicalQuantity.LuminousExposure, LuminousExposureUnit>.div(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = unit.illuminance.illuminance(this, time)
