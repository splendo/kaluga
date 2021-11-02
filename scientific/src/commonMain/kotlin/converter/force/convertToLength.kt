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

package com.splendo.kaluga.scientific.converter.force

import com.splendo.kaluga.scientific.Force
import com.splendo.kaluga.scientific.ImperialForce
import com.splendo.kaluga.scientific.ImperialSurfaceTension
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Meter
import com.splendo.kaluga.scientific.MetricForce
import com.splendo.kaluga.scientific.MetricSurfaceTension
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SurfaceTension
import com.splendo.kaluga.scientific.UKImperialForce
import com.splendo.kaluga.scientific.USCustomaryForce
import com.splendo.kaluga.scientific.converter.length.length
import kotlin.jvm.JvmName

@JvmName("metricForceDivMetricSurfaceTension")
infix operator fun <ForceUnit : MetricForce> ScientificValue<MeasurementType.Force, ForceUnit>.div(surfaceTension: ScientificValue<MeasurementType.SurfaceTension, MetricSurfaceTension>) = surfaceTension.unit.per.length(this, surfaceTension)
@JvmName("imperialForceDivImperialSurfaceTension")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<MeasurementType.Force, ForceUnit>.div(surfaceTension: ScientificValue<MeasurementType.SurfaceTension, ImperialSurfaceTension>) = surfaceTension.unit.per.length(this, surfaceTension)
@JvmName("ukImperialForceDivImperialSurfaceTension")
infix operator fun <ForceUnit : UKImperialForce> ScientificValue<MeasurementType.Force, ForceUnit>.div(surfaceTension: ScientificValue<MeasurementType.SurfaceTension, ImperialSurfaceTension>) = surfaceTension.unit.per.length(this, surfaceTension)
@JvmName("usCustomaryForceDivImperialSurfaceTension")
infix operator fun <ForceUnit : USCustomaryForce> ScientificValue<MeasurementType.Force, ForceUnit>.div(surfaceTension: ScientificValue<MeasurementType.SurfaceTension, ImperialSurfaceTension>) = surfaceTension.unit.per.length(this, surfaceTension)
@JvmName("forceDivSurfaceTension")
infix operator fun <ForceUnit : Force, SurfaceTensionUnit : SurfaceTension> ScientificValue<MeasurementType.Force, ForceUnit>.div(surfaceTension: ScientificValue<MeasurementType.SurfaceTension, SurfaceTensionUnit>) = Meter.length(this, surfaceTension)
