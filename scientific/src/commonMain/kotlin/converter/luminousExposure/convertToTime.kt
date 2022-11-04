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
import com.splendo.kaluga.scientific.converter.time.time
import com.splendo.kaluga.scientific.unit.Illuminance
import com.splendo.kaluga.scientific.unit.ImperialIlluminance
import com.splendo.kaluga.scientific.unit.ImperialLuminousExposure
import com.splendo.kaluga.scientific.unit.LuminousExposure
import com.splendo.kaluga.scientific.unit.MetricIlluminance
import com.splendo.kaluga.scientific.unit.MetricLuminousExposure
import kotlin.jvm.JvmName

@JvmName("metricLuminousExposureDivMetricIlluminance")
infix operator fun <IlluminanceUnit : MetricIlluminance> ScientificValue<PhysicalQuantity.LuminousExposure, MetricLuminousExposure>.div(
    illuminance: ScientificValue<PhysicalQuantity.Illuminance, IlluminanceUnit>
) = unit.time.time(this, illuminance)

@JvmName("imperialLuminousExposureDivImperialIlluminance")
infix operator fun <IlluminanceUnit : ImperialIlluminance> ScientificValue<PhysicalQuantity.LuminousExposure, ImperialLuminousExposure>.div(
    illuminance: ScientificValue<PhysicalQuantity.Illuminance, IlluminanceUnit>
) = unit.time.time(this, illuminance)

@JvmName("luminousExposureDivIlluminance")
infix operator fun <IlluminanceUnit : Illuminance> ScientificValue<PhysicalQuantity.LuminousExposure, LuminousExposure>.div(
    illuminance: ScientificValue<PhysicalQuantity.Illuminance, IlluminanceUnit>
) = unit.time.time(this, illuminance)
