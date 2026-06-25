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

package com.splendo.kaluga.scientific.converter.radiantIntensity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.radiance.radiance
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialRadiantIntensity
import com.splendo.kaluga.scientific.unit.MetricAndImperialRadiantIntensity
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricRadiantIntensity
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricAndImperialRadiantIntensityDivMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.RadiantIntensity, MetricAndImperialRadiantIntensity>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit per area.unit).radiance(this, area)

@JvmName("metricAndImperialRadiantIntensityDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.RadiantIntensity, MetricAndImperialRadiantIntensity>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit per area.unit).radiance(this, area)

@JvmName("metricRadiantIntensityDivMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.RadiantIntensity, MetricRadiantIntensity>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (unit per area.unit).radiance(this, area)

@JvmName("imperialRadiantIntensityDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.RadiantIntensity, ImperialRadiantIntensity>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit per area.unit).radiance(this, area)
