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

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.force.force
import com.splendo.kaluga.scientific.unit.ImperialLength
import com.splendo.kaluga.scientific.unit.ImperialSurfaceTension
import com.splendo.kaluga.scientific.unit.Length
import com.splendo.kaluga.scientific.unit.MetricLength
import com.splendo.kaluga.scientific.unit.MetricSurfaceTension
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.SurfaceTension
import com.splendo.kaluga.scientific.unit.UKImperialSurfaceTension
import com.splendo.kaluga.scientific.unit.USCustomarySurfaceTension
import kotlin.jvm.JvmName

@JvmName("metricSurfaceTensionTimesMetricLength")
infix operator fun <LengthUnit : MetricLength> ScientificValue<PhysicalQuantity.SurfaceTension, MetricSurfaceTension>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>
) = unit.force.force(this, length)

@JvmName("imperialSurfaceTensionTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.SurfaceTension, ImperialSurfaceTension>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>
) = unit.force.force(this, length)

@JvmName("ukImperialSurfaceTensionTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.SurfaceTension, UKImperialSurfaceTension>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>
) = unit.force.force(this, length)

@JvmName("usCustomarySurfaceTensionTimesImperialLength")
infix operator fun <LengthUnit : ImperialLength> ScientificValue<PhysicalQuantity.SurfaceTension, USCustomarySurfaceTension>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>
) = unit.force.force(this, length)

@JvmName("surfaceTensionTimesLength")
infix operator fun <SurfaceTensionUnit : SurfaceTension, LengthUnit : Length> ScientificValue<PhysicalQuantity.SurfaceTension, SurfaceTensionUnit>.times(
    length: ScientificValue<PhysicalQuantity.Length, LengthUnit>
) = Newton.force(this, length)
