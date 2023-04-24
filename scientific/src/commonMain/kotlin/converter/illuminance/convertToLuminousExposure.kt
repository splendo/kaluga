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

package com.splendo.kaluga.scientific.converter.illuminance

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.luminousExposure.luminousExposure
import com.splendo.kaluga.scientific.unit.Illuminance
import com.splendo.kaluga.scientific.unit.ImperialIlluminance
import com.splendo.kaluga.scientific.unit.Lux
import com.splendo.kaluga.scientific.unit.MetricIlluminance
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("metricIlluminanceTimesTime")
infix operator fun <IlluminanceUnit : MetricIlluminance, TimeUnit : Time> ScientificValue<PhysicalQuantity.Illuminance, IlluminanceUnit>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = (unit x time.unit).luminousExposure(this, time)

@JvmName("imperialIlluminanceTimesTime")
infix operator fun <IlluminanceUnit : ImperialIlluminance, TimeUnit : Time> ScientificValue<PhysicalQuantity.Illuminance, IlluminanceUnit>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = (unit x time.unit).luminousExposure(this, time)

@JvmName("illuminanceTimesTime")
infix operator fun <IlluminanceUnit : Illuminance, TimeUnit : Time> ScientificValue<PhysicalQuantity.Illuminance, IlluminanceUnit>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = (Lux x time.unit).luminousExposure(this, time)
