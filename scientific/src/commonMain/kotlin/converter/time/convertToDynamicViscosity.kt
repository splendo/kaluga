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

package com.splendo.kaluga.scientific.converter.time

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.pressure.times
import com.splendo.kaluga.scientific.unit.ImperialPressure
import com.splendo.kaluga.scientific.unit.MetricPressure
import com.splendo.kaluga.scientific.unit.Pressure
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.UKImperialPressure
import com.splendo.kaluga.scientific.unit.USCustomaryPressure
import kotlin.jvm.JvmName

@JvmName("timeTimesMetricPressure")
infix operator fun <PressureUnit : MetricPressure, TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>
) = pressure * this

@JvmName("timeTimesImperialPressure")
infix operator fun <PressureUnit : ImperialPressure, TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>
) = pressure * this

@JvmName("timeTimesUKImperialPressure")
infix operator fun <PressureUnit : UKImperialPressure, TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>
) = pressure * this

@JvmName("timeTimesUSCustomaryPressure")
infix operator fun <PressureUnit : USCustomaryPressure, TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>
) = pressure * this

@JvmName("timeTimesPressure")
infix operator fun <PressureUnit : Pressure, TimeUnit : Time> ScientificValue<PhysicalQuantity.Time, TimeUnit>.times(
    pressure: ScientificValue<PhysicalQuantity.Pressure, PressureUnit>
) = pressure * this
