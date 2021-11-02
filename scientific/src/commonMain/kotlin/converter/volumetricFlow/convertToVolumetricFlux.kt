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

package com.splendo.kaluga.scientific.converter.volumetricFlow

import com.splendo.kaluga.scientific.Area
import com.splendo.kaluga.scientific.CubicMeter
import com.splendo.kaluga.scientific.ImperialArea
import com.splendo.kaluga.scientific.ImperialVolumetricFlow
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricArea
import com.splendo.kaluga.scientific.MetricVolumetricFlow
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Second
import com.splendo.kaluga.scientific.SquareMeter
import com.splendo.kaluga.scientific.UKImperialVolumetricFlow
import com.splendo.kaluga.scientific.USCustomaryVolumetricFlow
import com.splendo.kaluga.scientific.VolumetricFlow
import com.splendo.kaluga.scientific.converter.volumetricFlux.volumetricFlux
import com.splendo.kaluga.scientific.per
import kotlin.jvm.JvmName

@JvmName("metricVolumetricFlowDivMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.VolumetricFlow, MetricVolumetricFlow>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit per area.unit).volumetricFlux(this, area)
@JvmName("imperialVolumetricFlowDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.VolumetricFlow, ImperialVolumetricFlow>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit per area.unit).volumetricFlux(this, area)
@JvmName("ukImperialVolumetricFlowDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.VolumetricFlow, UKImperialVolumetricFlow>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit per area.unit).volumetricFlux(this, area)
@JvmName("usCustomaryVolumetricFlowDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.VolumetricFlow, USCustomaryVolumetricFlow>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (unit per area.unit).volumetricFlux(this, area)
@JvmName("volumetricFlowDivArea")
infix operator fun <VolumetricFlowUnit : VolumetricFlow, AreaUnit : Area> ScientificValue<MeasurementType.VolumetricFlow, VolumetricFlowUnit>.div(area: ScientificValue<MeasurementType.Area, AreaUnit>) = (CubicMeter per Second per SquareMeter).volumetricFlux(this, area)
