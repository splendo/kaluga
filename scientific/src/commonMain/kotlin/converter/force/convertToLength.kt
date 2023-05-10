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

package com.splendo.kaluga.scientific.converter.force

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.length.length
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.ImperialSurfaceTension
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricForce
import com.splendo.kaluga.scientific.unit.MetricSurfaceTension
import com.splendo.kaluga.scientific.unit.SurfaceTension
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.UKImperialSurfaceTension
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import com.splendo.kaluga.scientific.unit.USCustomarySurfaceTension
import kotlin.jvm.JvmName

@JvmName("metricForceDivMetricSurfaceTension")
infix operator fun <ForceUnit : MetricForce> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, MetricSurfaceTension>,
) = surfaceTension.unit.per.length(this, surfaceTension)

@JvmName("imperialForceDivImperialSurfaceTension")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, ImperialSurfaceTension>,
) = surfaceTension.unit.per.length(this, surfaceTension)

@JvmName("imperialForceDivUKImperialSurfaceTension")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, UKImperialSurfaceTension>,
) = surfaceTension.unit.per.length(this, surfaceTension)

@JvmName("imperialForceDivUSCustomarySurfaceTension")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, USCustomarySurfaceTension>,
) = surfaceTension.unit.per.length(this, surfaceTension)

@JvmName("ukImperialForceDivImperialSurfaceTension")
infix operator fun <ForceUnit : UKImperialForce> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, ImperialSurfaceTension>,
) = surfaceTension.unit.per.length(this, surfaceTension)

@JvmName("ukImperialForceDivUKImperialSurfaceTension")
infix operator fun <ForceUnit : UKImperialForce> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, UKImperialSurfaceTension>,
) = surfaceTension.unit.per.length(this, surfaceTension)

@JvmName("usCustomaryForceDivImperialSurfaceTension")
infix operator fun <ForceUnit : USCustomaryForce> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, ImperialSurfaceTension>,
) = surfaceTension.unit.per.length(this, surfaceTension)

@JvmName("usCustomaryForceDivUSCustomarySurfaceTension")
infix operator fun <ForceUnit : USCustomaryForce> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, USCustomarySurfaceTension>,
) = surfaceTension.unit.per.length(this, surfaceTension)

@JvmName("forceDivSurfaceTension")
infix operator fun <ForceUnit : Force, SurfaceTensionUnit : SurfaceTension> ScientificValue<PhysicalQuantity.Force, ForceUnit>.div(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, SurfaceTensionUnit>,
) = Meter.length(this, surfaceTension)
