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

package com.splendo.kaluga.scientific.converter.luminousEnergy

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.luminousExposure.luminousExposure
import com.splendo.kaluga.scientific.converter.luminousFlux.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.LuminousEnergy
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("luminousEnergyDivMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.LuminousEnergy, LuminousEnergy>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    ((1(unit.luminousFlux) / 1(area.unit)).unit x unit.time).luminousExposure(this, area)

@JvmName("luminousEnergyDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.LuminousEnergy, LuminousEnergy>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    ((1(unit.luminousFlux) / 1(area.unit)).unit x unit.time).luminousExposure(this, area)

@JvmName("luminousEnergyDivArea")
infix operator fun <AreaUnit : Area> ScientificValue<PhysicalQuantity.LuminousEnergy, LuminousEnergy>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    ((1(unit.luminousFlux) / 1(area.unit)).unit x unit.time).luminousExposure(this, area)
