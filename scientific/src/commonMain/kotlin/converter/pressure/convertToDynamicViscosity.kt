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

package com.splendo.kaluga.scientific.converter.pressure

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.dynamicViscosity.dynamicViscosity
import com.splendo.kaluga.scientific.unit.ImperialPressure
import com.splendo.kaluga.scientific.unit.MetricPressure
import com.splendo.kaluga.scientific.unit.Pascal
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.UKImperialPressure
import com.splendo.kaluga.scientific.unit.USCustomaryPressure
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("metricPressureTimesTime")
infix operator fun <PressureUnit : MetricPressure, TimeUnit : Time> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>
) = (unit x time.unit).dynamicViscosity(this, time)

@JvmName("imperialPressureTimesTime")
infix operator fun <PressureUnit : ImperialPressure, TimeUnit : Time> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>
) = (unit x time.unit).dynamicViscosity(this, time)

@JvmName("ukImperialPressureTimesTime")
infix operator fun <PressureUnit : UKImperialPressure, TimeUnit : Time> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>
) = (unit x time.unit).dynamicViscosity(this, time)

@JvmName("usCustomaryPressureTimesTime")
infix operator fun <PressureUnit : USCustomaryPressure, TimeUnit : Time> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>
) = (unit x time.unit).dynamicViscosity(this, time)

@JvmName("pressureTimesTime")
infix operator fun <PressureUnit : Pressure, TimeUnit : Time> ScientificValue<PhysicalQuantity.Pressure, PressureUnit>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>
) = (Pascal x time.unit).dynamicViscosity(this, time)
