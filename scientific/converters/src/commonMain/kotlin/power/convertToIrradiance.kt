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

package com.splendo.kaluga.scientific.converter.power

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.irradiance.irradiance
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialPower
import com.splendo.kaluga.scientific.unit.MetricAndImperialPower
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricPower
import com.splendo.kaluga.scientific.unit.Power
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricAndImperialPowerDivMetricArea")
infix operator fun <PowerUnit : MetricAndImperialPower, AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit per area.unit).irradiance(this, area)

@JvmName("metricAndImperialPowerDivImperialArea")
infix operator fun <PowerUnit : MetricAndImperialPower, AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit per area.unit).irradiance(this, area)

@JvmName("metricPowerDivMetricArea")
infix operator fun <PowerUnit : MetricPower, AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit per area.unit).irradiance(this, area)

@JvmName("imperialPowerDivImperialArea")
infix operator fun <PowerUnit : ImperialPower, AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit per area.unit).irradiance(this, area)

@JvmName("powerDivArea")
infix operator fun <PowerUnit : Power, AreaUnit : Area> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (Watt per SquareMeter).irradiance(this, area)
