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
import com.splendo.kaluga.scientific.converter.area.area
import com.splendo.kaluga.scientific.unit.ImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.ImperialVolumetricFlux
import com.splendo.kaluga.scientific.unit.MetricVolumetricFlow
import com.splendo.kaluga.scientific.unit.MetricVolumetricFlux
import com.splendo.kaluga.scientific.unit.UKImperialVolumetricFlow
import com.splendo.kaluga.scientific.unit.UKImperialVolumetricFlux
import com.splendo.kaluga.scientific.unit.USCustomaryVolumetricFlow
import com.splendo.kaluga.scientific.unit.USCustomaryVolumetricFlux
import com.splendo.kaluga.scientific.unit.VolumetricFlow
import com.splendo.kaluga.scientific.unit.VolumetricFlux
import kotlin.jvm.JvmName

@JvmName("metricVolumetricFlowDivMetricVolumetricFlux")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, MetricVolumetricFlow>.div(
    volumetricFlux: ScientificValue<PhysicalQuantity.VolumetricFlux, MetricVolumetricFlux>
) = volumetricFlux.unit.per.area(this, volumetricFlux)

@JvmName("imperialVolumetricFlowDivImperialVolumetricFlux")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>.div(
    volumetricFlux: ScientificValue<PhysicalQuantity.VolumetricFlux, ImperialVolumetricFlux>
) = volumetricFlux.unit.per.area(this, volumetricFlux)

@JvmName("imperialVolumetricFlowDivUKImperialVolumetricFlux")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>.div(
    volumetricFlux: ScientificValue<PhysicalQuantity.VolumetricFlux, UKImperialVolumetricFlux>
) = volumetricFlux.unit.per.area(this, volumetricFlux)

@JvmName("imperialVolumetricFlowDivUSCustomaryVolumetricFlux")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, ImperialVolumetricFlow>.div(
    volumetricFlux: ScientificValue<PhysicalQuantity.VolumetricFlux, USCustomaryVolumetricFlux>
) = volumetricFlux.unit.per.area(this, volumetricFlux)

@JvmName("ukImperialVolumetricFlowDivImperialVolumetricFlux")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>.div(
    volumetricFlux: ScientificValue<PhysicalQuantity.VolumetricFlux, ImperialVolumetricFlux>
) = volumetricFlux.unit.per.area(this, volumetricFlux)

@JvmName("ukImperialVolumetricFlowDivUKImperialVolumetricFlux")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, UKImperialVolumetricFlow>.div(
    volumetricFlux: ScientificValue<PhysicalQuantity.VolumetricFlux, UKImperialVolumetricFlux>
) = volumetricFlux.unit.per.area(this, volumetricFlux)

@JvmName("usCustomaryVolumetricFlowDivImperialVolumetricFlux")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>.div(
    volumetricFlux: ScientificValue<PhysicalQuantity.VolumetricFlux, ImperialVolumetricFlux>
) = volumetricFlux.unit.per.area(this, volumetricFlux)

@JvmName("usCustomaryVolumetricFlowDivUSCustomaryVolumetricFlux")
infix operator fun ScientificValue<PhysicalQuantity.VolumetricFlow, USCustomaryVolumetricFlow>.div(
    volumetricFlux: ScientificValue<PhysicalQuantity.VolumetricFlux, USCustomaryVolumetricFlux>
) = volumetricFlux.unit.per.area(this, volumetricFlux)

@JvmName("volumetricFlowDivVolumetricFlux")
infix operator fun <VolumetricFlowUnit : VolumetricFlow, VolumetricFluxUnit : VolumetricFlux> ScientificValue<PhysicalQuantity.VolumetricFlow, VolumetricFlowUnit>.div(
    volumetricFlux: ScientificValue<PhysicalQuantity.VolumetricFlux, VolumetricFluxUnit>
) = volumetricFlux.unit.per.area(this, volumetricFlux)
