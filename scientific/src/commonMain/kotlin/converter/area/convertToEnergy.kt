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

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.surfaceTension.times
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialSurfaceTension
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricSurfaceTension
import com.splendo.kaluga.scientific.unit.SquareCentimeter
import com.splendo.kaluga.scientific.unit.SquareInch
import com.splendo.kaluga.scientific.unit.SurfaceTension
import com.splendo.kaluga.scientific.unit.UKImperialSurfaceTension
import com.splendo.kaluga.scientific.unit.USCustomarySurfaceTension
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
