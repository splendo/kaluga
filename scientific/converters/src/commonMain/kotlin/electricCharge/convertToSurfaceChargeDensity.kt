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

package com.splendo.kaluga.scientific.converter.electricCharge

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.surfaceChargeDensity.surfaceChargeDensity
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.ElectricCharge
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("electricChargeDivMetricArea")
infix operator fun <ChargeUnit : ElectricCharge, AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit per area.unit).surfaceChargeDensity(this, area)

@JvmName("electricChargeDivImperialArea")
infix operator fun <ChargeUnit : ElectricCharge, AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit per area.unit).surfaceChargeDensity(this, area)

@JvmName("electricChargeDivArea")
infix operator fun <ChargeUnit : ElectricCharge, AreaUnit : Area> ScientificValue<PhysicalQuantity.ElectricCharge, ChargeUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit per SquareMeter).surfaceChargeDensity(this, area)
