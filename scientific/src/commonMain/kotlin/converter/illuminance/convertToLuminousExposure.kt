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

package com.splendo.kaluga.scientific.converter.illuminance

import com.splendo.kaluga.scientific.Illuminance
import com.splendo.kaluga.scientific.ImperialIlluminance
import com.splendo.kaluga.scientific.Lux
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricIlluminance
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Time
import com.splendo.kaluga.scientific.converter.luminousExposure.luminousExposure
import com.splendo.kaluga.scientific.x
import kotlin.jvm.JvmName

@JvmName("metricIlluminanceTimesTime")
infix operator fun <IlluminanceUnit : MetricIlluminance, TimeUnit : Time> ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit x time.unit).luminousExposure(this, time)
@JvmName("imperialIlluminanceTimesTime")
infix operator fun <IlluminanceUnit : ImperialIlluminance, TimeUnit : Time> ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (unit x time.unit).luminousExposure(this, time)
@JvmName("illuminanceTimesTime")
infix operator fun <IlluminanceUnit : Illuminance, TimeUnit : Time> ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>.times(time: ScientificValue<MeasurementType.Time, TimeUnit>) = (Lux x time.unit).luminousExposure(this, time)
