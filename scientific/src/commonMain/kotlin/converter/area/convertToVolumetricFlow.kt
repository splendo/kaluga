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

package com.splendo.kaluga.scientific.converter.area

import com.splendo.kaluga.scientific.Area
import com.splendo.kaluga.scientific.ImperialArea
import com.splendo.kaluga.scientific.ImperialVolumetricFlux
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricArea
import com.splendo.kaluga.scientific.MetricVolumetricFlux
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.UKImperialVolumetricFlux
import com.splendo.kaluga.scientific.USCustomaryVolumetricFlux
import com.splendo.kaluga.scientific.VolumetricFlux
import com.splendo.kaluga.scientific.converter.volumetricFlux.times
import kotlin.jvm.JvmName

@JvmName("metricAreaTimesMetricVolumetricFlux")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, MetricVolumetricFlux>) = volumetricFlux * this
@JvmName("imperialAreaTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, ImperialVolumetricFlux>) = volumetricFlux * this
@JvmName("imperialAreaTimesUKImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, UKImperialVolumetricFlux>) = volumetricFlux * this
@JvmName("imperialAreaTimesUSCustomaryArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, USCustomaryVolumetricFlux>) = volumetricFlux * this
@JvmName("areaTimesVolumetricFlux")
infix operator fun <VolumetricFluxUnit : VolumetricFlux, AreaUnit : Area> ScientificValue<MeasurementType.Area, AreaUnit>.times(volumetricFlux: ScientificValue<MeasurementType.VolumetricFlux, VolumetricFluxUnit>) = volumetricFlux * this
