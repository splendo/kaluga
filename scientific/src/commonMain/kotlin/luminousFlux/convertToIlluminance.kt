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

package com.splendo.kaluga.scientific.luminousFlux

import com.splendo.kaluga.scientific.Area
import com.splendo.kaluga.scientific.FootCandle
import com.splendo.kaluga.scientific.ImperialArea
import com.splendo.kaluga.scientific.LuminousFlux
import com.splendo.kaluga.scientific.Lux
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricArea
import com.splendo.kaluga.scientific.Phot
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SquareCentimeter
import com.splendo.kaluga.scientific.illuminance.illuminance
import kotlin.jvm.JvmName

@JvmName("lumenDivSquareCentimeter")
infix operator fun <FluxUnit : LuminousFlux> ScientificValue<MeasurementType.LuminousFlux, FluxUnit>.div(squareCentimeter: ScientificValue<MeasurementType.Area, SquareCentimeter>) = Phot.illuminance(this, squareCentimeter)
@JvmName("lumenDivMetricArea")
infix operator fun <FluxUnit : LuminousFlux, AreaUnit : MetricArea> ScientificValue<MeasurementType.LuminousFlux, FluxUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Lux.illuminance(this, area)
@JvmName("lumenDivImperialArea")
infix operator fun <FluxUnit : LuminousFlux, AreaUnit : ImperialArea> ScientificValue<MeasurementType.LuminousFlux, FluxUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = FootCandle.illuminance(this, area)
@JvmName("lumenDivArea")
infix operator fun <FluxUnit : LuminousFlux, AreaUnit : Area> ScientificValue<MeasurementType.LuminousFlux, FluxUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Lux.illuminance(this, area)
