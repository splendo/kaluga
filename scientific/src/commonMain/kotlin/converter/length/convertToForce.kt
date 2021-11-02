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

package com.splendo.kaluga.scientific.converter.length

import com.splendo.kaluga.scientific.ImperialLength
import com.splendo.kaluga.scientific.ImperialSurfaceTension
import com.splendo.kaluga.scientific.Length
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricLength
import com.splendo.kaluga.scientific.MetricSurfaceTension
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SurfaceTension
import com.splendo.kaluga.scientific.UKImperialSurfaceTension
import com.splendo.kaluga.scientific.USCustomarySurfaceTension
import com.splendo.kaluga.scientific.converter.surfaceTension.times
import kotlin.jvm.JvmName

@JvmName("metricLengthTimesMetricSurfaceTension")
infix operator fun <LengthUnit : MetricLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(surfaceTension: ScientificValue<MeasurementType.SurfaceTension, MetricSurfaceTension>) = surfaceTension * this
@JvmName("imperialLengthTimesImperialSurfaceTension")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(surfaceTension: ScientificValue<MeasurementType.SurfaceTension, ImperialSurfaceTension>) = surfaceTension * this
@JvmName("imperialLengthTimesUKImperialSurfaceTension")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(surfaceTension: ScientificValue<MeasurementType.SurfaceTension, UKImperialSurfaceTension>) = surfaceTension * this
@JvmName("imperialLengthTimesUSCustomarySurfaceTension")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<MeasurementType.Length, LengthUnit>.times(surfaceTension: ScientificValue<MeasurementType.SurfaceTension, USCustomarySurfaceTension>) = surfaceTension * this
@JvmName("lengthTimesSurfaceTension")
infix operator fun <SurfaceTensionUnit : SurfaceTension, LengthUnit : Length> ScientificValue<MeasurementType.Length, LengthUnit>.times(surfaceTension: ScientificValue<MeasurementType.SurfaceTension, SurfaceTensionUnit>) = surfaceTension * this
