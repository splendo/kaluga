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

package com.splendo.kaluga.scientific.converter.luminousIntensity

import com.splendo.kaluga.scientific.ImperialLuminance
import com.splendo.kaluga.scientific.Lambert
import com.splendo.kaluga.scientific.Luminance
import com.splendo.kaluga.scientific.LuminousIntensity
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricLuminance
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SquareCentimeter
import com.splendo.kaluga.scientific.SquareFoot
import com.splendo.kaluga.scientific.SquareMeter
import com.splendo.kaluga.scientific.Stilb
import com.splendo.kaluga.scientific.converter.area.area
import kotlin.jvm.JvmName

@JvmName("luminousIntensityDivStilb")
infix operator fun <LuminousIntensityUnit : LuminousIntensity> ScientificValue<MeasurementType.LuminousIntensity, LuminousIntensityUnit>.div(luminance: ScientificValue<MeasurementType.Luminance, Stilb>) = SquareCentimeter.area(this, luminance)
@JvmName("luminousIntensityDivLambert")
infix operator fun <LuminousIntensityUnit : LuminousIntensity> ScientificValue<MeasurementType.LuminousIntensity, LuminousIntensityUnit>.div(luminance: ScientificValue<MeasurementType.Luminance, Lambert>) = SquareCentimeter.area(this, luminance)
@JvmName("luminousIntensityDivMetricLuminance")
infix operator fun <LuminousIntensityUnit : LuminousIntensity, LuminanceUnit : MetricLuminance> ScientificValue<MeasurementType.LuminousIntensity, LuminousIntensityUnit>.div(luminance: ScientificValue<MeasurementType.Luminance, LuminanceUnit>) = SquareMeter.area(this, luminance)
@JvmName("luminousIntensityDivImperialLuminance")
infix operator fun <LuminousIntensityUnit : LuminousIntensity, LuminanceUnit : ImperialLuminance> ScientificValue<MeasurementType.LuminousIntensity, LuminousIntensityUnit>.div(luminance: ScientificValue<MeasurementType.Luminance, LuminanceUnit>) = SquareFoot.area(this, luminance)
@JvmName("luminousIntensityDivLuminance")
infix operator fun <LuminousIntensityUnit : LuminousIntensity, LuminanceUnit : Luminance> ScientificValue<MeasurementType.LuminousIntensity, LuminousIntensityUnit>.div(luminance: ScientificValue<MeasurementType.Luminance, LuminanceUnit>) = SquareMeter.area(this, luminance)
