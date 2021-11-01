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

package com.splendo.kaluga.scientific.converter.luminance

import com.splendo.kaluga.scientific.FootCandle
import com.splendo.kaluga.scientific.ImperialLuminance
import com.splendo.kaluga.scientific.Lambert
import com.splendo.kaluga.scientific.Luminance
import com.splendo.kaluga.scientific.Lux
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricLuminance
import com.splendo.kaluga.scientific.Phot
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SolidAngle
import com.splendo.kaluga.scientific.Stilb
import com.splendo.kaluga.scientific.converter.illuminance.illuminance
import kotlin.jvm.JvmName

@JvmName("stilbTimesSolidAngle")
infix operator fun <SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.Luminance, Stilb>.times(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) = Phot.illuminance(this, solidAngle)
@JvmName("lambertTimesSolidAngle")
infix operator fun <SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.Luminance, Lambert>.times(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) = Phot.illuminance(this, solidAngle)
@JvmName("metricLuminanceTimesSolidAngle")
infix operator fun <LuminanceUnit : MetricLuminance, SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.Luminance, LuminanceUnit>.times(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) = Lux.illuminance(this, solidAngle)
@JvmName("imperialLuminanceTimesSolidAngle")
infix operator fun <LuminanceUnit : ImperialLuminance, SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.Luminance, LuminanceUnit>.times(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) = FootCandle.illuminance(this, solidAngle)
@JvmName("luminanceTimesSolidAngle")
infix operator fun <LuminanceUnit : Luminance, SolidAngleUnit : SolidAngle> ScientificValue<MeasurementType.Luminance, LuminanceUnit>.times(solidAngle: ScientificValue<MeasurementType.SolidAngle, SolidAngleUnit>) = Lux.illuminance(this, solidAngle)
