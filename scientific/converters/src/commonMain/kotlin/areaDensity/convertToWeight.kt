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

package com.splendo.kaluga.scientific.converter.areaDensity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.weight.mass
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.AreaDensity
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialAreaDensity
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricAreaDensity
import com.splendo.kaluga.scientific.unit.UKImperialAreaDensity
import com.splendo.kaluga.scientific.unit.USCustomaryAreaDensity
import kotlin.jvm.JvmName

@JvmName("metricAreaDensityTimesMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.AreaDensity, MetricAreaDensity>.times(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    unit.weight.mass(this, area)

@JvmName("imperialAreaDensityTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.AreaDensity, ImperialAreaDensity>.times(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    unit.weight.mass(this, area)

@JvmName("ukImperialAreaDensityTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.AreaDensity, UKImperialAreaDensity>.times(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    unit.weight.mass(this, area)

@JvmName("usCustomaryAreaDensityTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.AreaDensity, USCustomaryAreaDensity>.times(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    unit.weight.mass(this, area)

@JvmName("areaDensityTimesArea")
infix operator fun <AreaDensityUnit : AreaDensity, AreaUnit : Area> ScientificValue<PhysicalQuantity.AreaDensity, AreaDensityUnit>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = Kilogram.mass(this, area)
