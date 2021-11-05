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

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.luminance.luminance
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.FootLambert
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.LuminousIntensity
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.Nit
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.Stilb
import kotlin.jvm.JvmName

@JvmName("luminousIntensityDivSquareCentimeter")
infix operator fun <LuminousIntensityUnit : LuminousIntensity> ScientificValue<MeasurementType.LuminousIntensity, LuminousIntensityUnit>.div(area: ScientificValue<MeasurementType.Area, SquareCentimeter>) = Stilb.luminance(this, area)
@JvmName("luminousIntensityDivMetricArea")
infix operator fun <LuminousIntensityUnit : LuminousIntensity, AreaUnit : MetricArea> ScientificValue<MeasurementType.LuminousIntensity, LuminousIntensityUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Nit.luminance(this, area)
@JvmName("luminousIntensityDivIMperialArea")
infix operator fun <LuminousIntensityUnit : LuminousIntensity, AreaUnit : ImperialArea> ScientificValue<MeasurementType.LuminousIntensity, LuminousIntensityUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = FootLambert.luminance(this, area)
@JvmName("luminousIntensityDivArea")
infix operator fun <LuminousIntensityUnit : LuminousIntensity, AreaUnit : Area> ScientificValue<MeasurementType.LuminousIntensity, LuminousIntensityUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Nit.luminance(this, area)