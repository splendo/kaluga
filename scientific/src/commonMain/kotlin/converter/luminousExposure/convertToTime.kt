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

package com.splendo.kaluga.scientific.converter.luminousExposure

import com.splendo.kaluga.scientific.Illuminance
import com.splendo.kaluga.scientific.ImperialIlluminance
import com.splendo.kaluga.scientific.ImperialLuminousExposure
import com.splendo.kaluga.scientific.LuminousExposure
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricIlluminance
import com.splendo.kaluga.scientific.MetricLuminousExposure
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.time.time
import kotlin.jvm.JvmName

@JvmName("metricLuminousExposureDivMetricIlluminance")
infix operator fun <IlluminanceUnit : MetricIlluminance> ScientificValue<MeasurementType.LuminousExposure, MetricLuminousExposure>.div(illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>) = unit.time.time(this, illuminance)
@JvmName("imperialLuminousExposureDivImperialIlluminance")
infix operator fun <IlluminanceUnit : ImperialIlluminance> ScientificValue<MeasurementType.LuminousExposure, ImperialLuminousExposure>.div(illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>) = unit.time.time(this, illuminance)
@JvmName("luminousExposureDivIlluminance")
infix operator fun <IlluminanceUnit : Illuminance> ScientificValue<MeasurementType.LuminousExposure, LuminousExposure>.div(illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>) = unit.time.time(this, illuminance)
