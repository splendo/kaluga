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

import com.splendo.kaluga.scientific.ImperialVolumetricFlow
import com.splendo.kaluga.scientific.ImperialVolumetricFlux
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricVolumetricFlow
import com.splendo.kaluga.scientific.MetricVolumetricFlux
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialVolumetricFlow
import com.splendo.kaluga.scientific.UKImperialVolumetricFlux
import com.splendo.kaluga.scientific.USCustomaryVolumetricFlow
import com.splendo.kaluga.scientific.USCustomaryVolumetricFlux
import com.splendo.kaluga.scientific.VolumetricFlow
import com.splendo.kaluga.scientific.VolumetricFlux
import com.splendo.kaluga.scientific.converter.area.area
import kotlin.jvm.JvmName

@JvmName("metricVolumetricFlowDivMetricVolumetricFlux")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, MetricVolumetricFlow>.div(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, MetricVolumetricFlux>) = volumetricFlux.unit.per.area(this, volumetricFlux)
@JvmName("imperialVolumetricFlowDivImperialVolumetricFlux")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, ImperialVolumetricFlow>.div(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, ImperialVolumetricFlux>) = volumetricFlux.unit.per.area(this, volumetricFlux)
@JvmName("imperialVolumetricFlowDivUKImperialVolumetricFlux")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, ImperialVolumetricFlow>.div(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, UKImperialVolumetricFlux>) = volumetricFlux.unit.per.area(this, volumetricFlux)
@JvmName("imperialVolumetricFlowDivUSCustomaryVolumetricFlux")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, ImperialVolumetricFlow>.div(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, USCustomaryVolumetricFlux>) = volumetricFlux.unit.per.area(this, volumetricFlux)
@JvmName("ukImperialVolumetricFlowDivImperialVolumetricFlux")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, UKImperialVolumetricFlow>.div(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, ImperialVolumetricFlux>) = volumetricFlux.unit.per.area(this, volumetricFlux)
@JvmName("ukImperialVolumetricFlowDivUKImperialVolumetricFlux")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, UKImperialVolumetricFlow>.div(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, UKImperialVolumetricFlux>) = volumetricFlux.unit.per.area(this, volumetricFlux)
@JvmName("usCustomaryVolumetricFlowDivImperialVolumetricFlux")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, USCustomaryVolumetricFlow>.div(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, ImperialVolumetricFlux>) = volumetricFlux.unit.per.area(this, volumetricFlux)
@JvmName("usCustomaryVolumetricFlowDivUSCustomaryVolumetricFlux")
infix operator fun ScientificValue<MeasurementType.VolumetricFlow, USCustomaryVolumetricFlow>.div(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, USCustomaryVolumetricFlux>) = volumetricFlux.unit.per.area(this, volumetricFlux)
@JvmName("volumetricFlowDivVolumetricFlux")
infix operator fun <VolumetricFlowUnit : VolumetricFlow, VolumetricFluxUnit : VolumetricFlux> ScientificValue<MeasurementType.VolumetricFlow, VolumetricFlowUnit>.div(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, VolumetricFluxUnit>) = volumetricFlux.unit.per.area(this, volumetricFlux)
