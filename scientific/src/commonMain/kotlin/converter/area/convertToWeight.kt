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

package com.splendo.kaluga.scientific.converter.area

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.areaDensity.times
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.AreaDensity
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialAreaDensity
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricAreaDensity
import com.splendo.kaluga.scientific.unit.UKImperialAreaDensity
import com.splendo.kaluga.scientific.unit.USCustomaryAreaDensity
import kotlin.jvm.JvmName

@JvmName("metricAreaTimesMetricAreaDensity")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.Area, AreaUnit>.times(areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, MetricAreaDensity>) =
    areaDensity * this

@JvmName("imperialAreaTimesImperialAreaDensity")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Area, AreaUnit>.times(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, ImperialAreaDensity>,
) = areaDensity * this

@JvmName("imperialAreaDensityTimesUKImperialAreaDensity")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Area, AreaUnit>.times(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, UKImperialAreaDensity>,
) = areaDensity * this

@JvmName("imperialAreaTimesImperialUSCustomaryAreaDensity")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Area, AreaUnit>.times(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, USCustomaryAreaDensity>,
) = areaDensity * this

@JvmName("areaTimesAreaDensity")
infix operator fun <AreaDensityUnit : AreaDensity, AreaUnit : Area> ScientificValue<PhysicalQuantity.Area, AreaUnit>.times(
    areaDensity: ScientificValue<PhysicalQuantity.AreaDensity, AreaDensityUnit>,
) = areaDensity * this
