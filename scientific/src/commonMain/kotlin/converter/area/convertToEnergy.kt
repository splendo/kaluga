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
import com.splendo.kaluga.scientific.ImperialSurfaceTension
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricArea
import com.splendo.kaluga.scientific.MetricSurfaceTension
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SquareCentimeter
import com.splendo.kaluga.scientific.SquareInch
import com.splendo.kaluga.scientific.SurfaceTension
import com.splendo.kaluga.scientific.UKImperialSurfaceTension
import com.splendo.kaluga.scientific.USCustomarySurfaceTension
import com.splendo.kaluga.scientific.converter.surfaceTension.times
import kotlin.jvm.JvmName

@JvmName("squareCentimeterTimesMetricSurfaceTension")
infix operator fun ScientificValue<MeasurementType.Area, SquareCentimeter>.times(surfaceTension: ScientificValue<MeasurementType.SurfaceTension, MetricSurfaceTension>) = surfaceTension * this
@JvmName("metricAreaTimesMetricSurfaceTension")
infix operator fun <AreaUnit : MetricArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(surfaceTension: ScientificValue<MeasurementType.SurfaceTension, MetricSurfaceTension>) = surfaceTension * this
@JvmName("squareInchTimesImperialSurfaceTension")
infix operator fun ScientificValue<MeasurementType.Area, SquareInch>.times(surfaceTension: ScientificValue<MeasurementType.SurfaceTension, ImperialSurfaceTension>) = surfaceTension * this
@JvmName("squareInchTimesUKImperialSurfaceTension")
infix operator fun ScientificValue<MeasurementType.Area, SquareInch>.times(surfaceTension: ScientificValue<MeasurementType.SurfaceTension, UKImperialSurfaceTension>) = surfaceTension * this
@JvmName("squareInchTimesUSCustomarySurfaceTension")
infix operator fun ScientificValue<MeasurementType.Area, SquareInch>.times(surfaceTension: ScientificValue<MeasurementType.SurfaceTension, USCustomarySurfaceTension>) = surfaceTension * this
@JvmName("imperialAreaTimesImperialSurfaceTensionTimes")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(surfaceTension: ScientificValue<MeasurementType.SurfaceTension, ImperialSurfaceTension>) = surfaceTension * this
@JvmName("imperialAreaTimesUKImperialSurfaceTension")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(surfaceTension: ScientificValue<MeasurementType.SurfaceTension, UKImperialSurfaceTension>) = surfaceTension * this
@JvmName("imperialAreaTimesUSCustomarySurfaceTension")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<MeasurementType.Area, AreaUnit>.times(surfaceTension: ScientificValue<MeasurementType.SurfaceTension, USCustomarySurfaceTension>) = surfaceTension * this
@JvmName("areTimesSurfaceTension")
infix operator fun <SurfaceTensionUnit : SurfaceTension, AreaUnit : Area> ScientificValue<MeasurementType.Area, AreaUnit>.times(surfaceTension: ScientificValue<MeasurementType.SurfaceTension, SurfaceTensionUnit>) = surfaceTension * this
