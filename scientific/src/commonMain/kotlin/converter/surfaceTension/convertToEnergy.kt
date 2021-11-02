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

package com.splendo.kaluga.scientific.converter.surfaceTension

import com.splendo.kaluga.scientific.Area
import com.splendo.kaluga.scientific.Erg
import com.splendo.kaluga.scientific.FootPoundForce
import com.splendo.kaluga.scientific.ImperialArea
import com.splendo.kaluga.scientific.ImperialSurfaceTension
import com.splendo.kaluga.scientific.InchPoundForce
import com.splendo.kaluga.scientific.Joule
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricArea
import com.splendo.kaluga.scientific.MetricSurfaceTension
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SquareCentimeter
import com.splendo.kaluga.scientific.SquareInch
import com.splendo.kaluga.scientific.SurfaceTension
import com.splendo.kaluga.scientific.UKImperialSurfaceTension
import com.splendo.kaluga.scientific.USCustomarySurfaceTension
import com.splendo.kaluga.scientific.converter.energy.energy
import kotlin.jvm.JvmName

@JvmName("metricSurfaceTensionTimesSquareCentimeter")
infix operator fun ScientificValue<MeasurementType.SurfaceTension, MetricSurfaceTension>.times(area: ScientificValue<MeasurementType.Area, SquareCentimeter>) = Erg.energy(this, area)
@JvmName("metricSurfaceTensionTimesMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.SurfaceTension, MetricSurfaceTension>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Joule.energy(this, area)
@JvmName("imperialSurfaceTensionTimesSquareInch")
infix operator fun ScientificValue<MeasurementType.SurfaceTension, ImperialSurfaceTension>.times(area: ScientificValue<MeasurementType.Area, SquareInch>) = InchPoundForce.energy(this, area)
@JvmName("ukImperialSurfaceTensionTimesSquareInch")
infix operator fun ScientificValue<MeasurementType.SurfaceTension, UKImperialSurfaceTension>.times(area: ScientificValue<MeasurementType.Area, SquareInch>) = InchPoundForce.energy(this, area)
@JvmName("usCustomarySurfaceTensionTimesSquareInch")
infix operator fun ScientificValue<MeasurementType.SurfaceTension, USCustomarySurfaceTension>.times(area: ScientificValue<MeasurementType.Area, SquareInch>) = InchPoundForce.energy(this, area)
@JvmName("imperialSurfaceTensionTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.SurfaceTension, ImperialSurfaceTension>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = FootPoundForce.energy(this, area)
@JvmName("ukImperialSurfaceTensionTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.SurfaceTension, UKImperialSurfaceTension>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = FootPoundForce.energy(this, area)
@JvmName("usCustomarySurfaceTensionTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.SurfaceTension, USCustomarySurfaceTension>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = FootPoundForce.energy(this, area)
@JvmName("surfaceTensionTimesArea")
infix operator fun <SurfaceTensionUnit : SurfaceTension, AreaUnit : Area> ScientificValue<MeasurementType.SurfaceTension, SurfaceTensionUnit>.times(area: ScientificValue<MeasurementType.Area, AreaUnit>) = Joule.energy(this, area)
