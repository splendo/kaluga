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

package com.splendo.kaluga.scientific.converter.volumetricFlow

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.volumetricFlux.volumetricFlux
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricVolumetricFlow
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.UKImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.USCustomaryVolumetricFlow
import com.splendo.kaluga.scientific.unit.VolumetricFlow
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricVolumetricFlowDivMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.VolumetricFlow, MetricVolumetricFlow>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (unit per area.unit).volumetricFlux(this, area)

@JvmName("imperialVolumetricFlowDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (unit per area.unit).volumetricFlux(this, area)

@JvmName("ukImperialVolumetricFlowDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit per area.unit).volumetricFlux(this, area)

@JvmName("usCustomaryVolumetricFlowDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit per area.unit).volumetricFlux(this, area)

@JvmName("volumetricFlowDivArea")
infix operator fun <VolumetricFlowUnit : VolumetricFlow, AreaUnit : Area> ScientificValue<PhysicalQuantity.VolumetricFlow, VolumetricFlowUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (CubicMeter per Second per SquareMeter).volumetricFlux(this, area)
