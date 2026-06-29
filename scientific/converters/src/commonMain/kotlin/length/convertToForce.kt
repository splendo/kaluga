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

package com.splendo.kaluga.scientific.converter.length

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.surfaceTension.times
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.ImperialSurfaceTension
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.MetricSurfaceTension
import com.splendo.kaluga.scientific.unit.SurfaceTension
import com.splendo.kaluga.scientific.unit.UKImperialSurfaceTension
import com.splendo.kaluga.scientific.unit.USCustomarySurfaceTension
import kotlin.jvm.JvmName

@JvmName("metricLengthTimesMetricSurfaceTension")
infix operator fun <LengthUnit : MetricLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, MetricSurfaceTension>,
) = surfaceTension * this

@JvmName("imperialLengthTimesImperialSurfaceTension")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, ImperialSurfaceTension>,
) = surfaceTension * this

@JvmName("imperialLengthTimesUKImperialSurfaceTension")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, UKImperialSurfaceTension>,
) = surfaceTension * this

@JvmName("imperialLengthTimesUSCustomarySurfaceTension")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, USCustomarySurfaceTension>,
) = surfaceTension * this

@JvmName("lengthTimesSurfaceTension")
infix operator fun <SurfaceTensionUnit : SurfaceTension, LengthUnit : Length> ScientificValue<PhysicalQuantity.Length, LengthUnit>.times(
    surfaceTension: ScientificValue<PhysicalQuantity.SurfaceTension, SurfaceTensionUnit>,
) = surfaceTension * this
