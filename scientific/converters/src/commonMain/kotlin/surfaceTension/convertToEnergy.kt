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

package com.splendo.kaluga.scientific.converter.surfaceTension

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.energy.energy
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialSurfaceTension
import com.splendo.kaluga.scientific.unit.InchPoundForce
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricSurfaceTension
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.SquareInch
import com.splendo.kaluga.scientific.unit.SurfaceTension
import com.splendo.kaluga.scientific.unit.UKImperialSurfaceTension
import com.splendo.kaluga.scientific.unit.USCustomarySurfaceTension
import kotlin.jvm.JvmName

@JvmName("metricSurfaceTensionTimesSquareCentimeter")
infix operator fun ScientificValue<PhysicalQuantity.SurfaceTension, MetricSurfaceTension>.times(area: ScientificValue<PhysicalQuantity.Area, SquareCentimeter>) =
    Erg.energy(this, area)

@JvmName("metricSurfaceTensionTimesMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.SurfaceTension, MetricSurfaceTension>.times(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    Joule.energy(this, area)

@JvmName("imperialSurfaceTensionTimesSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.SurfaceTension, ImperialSurfaceTension>.times(area: ScientificValue<PhysicalQuantity.Area, SquareInch>) =
    InchPoundForce.energy(this, area)

@JvmName("ukImperialSurfaceTensionTimesSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.SurfaceTension, UKImperialSurfaceTension>.times(area: ScientificValue<PhysicalQuantity.Area, SquareInch>) =
    InchPoundForce.energy(this, area)

@JvmName("usCustomarySurfaceTensionTimesSquareInch")
infix operator fun ScientificValue<PhysicalQuantity.SurfaceTension, USCustomarySurfaceTension>.times(area: ScientificValue<PhysicalQuantity.Area, SquareInch>) =
    InchPoundForce.energy(this, area)

@JvmName("imperialSurfaceTensionTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.SurfaceTension, ImperialSurfaceTension>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = FootPoundForce.energy(this, area)

@JvmName("ukImperialSurfaceTensionTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.SurfaceTension, UKImperialSurfaceTension>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = FootPoundForce.energy(this, area)

@JvmName("usCustomarySurfaceTensionTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.SurfaceTension, USCustomarySurfaceTension>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = FootPoundForce.energy(this, area)

@JvmName("surfaceTensionTimesArea")
infix operator fun <SurfaceTensionUnit : SurfaceTension, AreaUnit : Area> ScientificValue<PhysicalQuantity.SurfaceTension, SurfaceTensionUnit>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = Joule.energy(this, area)
