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

package com.splendo.kaluga.scientific.converter.luminousFlux

import com.splendo.kaluga.scientific.Illuminance
import com.splendo.kaluga.scientific.ImperialIlluminance
import com.splendo.kaluga.scientific.LuminousFlux
import com.splendo.kaluga.scientific.MeasurementSystem
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricIlluminance
import com.splendo.kaluga.scientific.MetricMultipleUnit
import com.splendo.kaluga.scientific.Phot
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SquareCentimeter
import com.splendo.kaluga.scientific.SquareFoot
import com.splendo.kaluga.scientific.SquareMeter
import com.splendo.kaluga.scientific.converter.area.area
import kotlin.jvm.JvmName

@JvmName("fluxDivPhot")
infix operator fun <FluxUnit : LuminousFlux> ScientificValue<MeasurementType.LuminousFlux, FluxUnit>.div(phot: ScientificValue<MeasurementType.Illuminance, Phot>) = SquareCentimeter.area(this, phot)
@JvmName("fluxDivPhotMultiple")
infix operator fun <FluxUnit : LuminousFlux, PhotUnit> ScientificValue<MeasurementType.LuminousFlux, FluxUnit>.div(phot: ScientificValue<MeasurementType.Illuminance, PhotUnit>) where PhotUnit : Illuminance, PhotUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Illuminance, Phot> = SquareCentimeter.area(this, phot)
@JvmName("fluxDivMetricIlluminance")
infix operator fun <FluxUnit : LuminousFlux, IlluminanceUnit : MetricIlluminance> ScientificValue<MeasurementType.LuminousFlux, FluxUnit>.div(illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>) = SquareMeter.area(this, illuminance)
@JvmName("fluxDivImperialIlluminance")
infix operator fun <FluxUnit : LuminousFlux, IlluminanceUnit : ImperialIlluminance> ScientificValue<MeasurementType.LuminousFlux, FluxUnit>.div(illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>) = SquareFoot.area(this, illuminance)
@JvmName("fluxDivIlluminance")
infix operator fun <FluxUnit : LuminousFlux, IlluminanceUnit : Illuminance> ScientificValue<MeasurementType.LuminousFlux, FluxUnit>.div(illuminance: ScientificValue<MeasurementType.Illuminance, IlluminanceUnit>) = SquareMeter.area(this, illuminance)
