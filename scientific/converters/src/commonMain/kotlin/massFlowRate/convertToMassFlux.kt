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

package com.splendo.kaluga.scientific.converter.massFlowRate

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.massFlux.massFlux
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialMassFlowRate
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricMassFlowRate
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.UKImperialMassFlowRate
import com.splendo.kaluga.scientific.unit.USCustomaryMassFlowRate
import com.splendo.kaluga.scientific.unit.MassFlowRate
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricMassFlowRateDivMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.MassFlowRate, MetricMassFlowRate>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (unit per area.unit).massFlux(this, area)

@JvmName("imperialMassFlowRateDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.MassFlowRate, ImperialMassFlowRate>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (unit per area.unit).massFlux(this, area)

@JvmName("ukImperialMassFlowRateDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.MassFlowRate, UKImperialMassFlowRate>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (unit per area.unit).massFlux(this, area)

@JvmName("usCustomaryMassFlowRateDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.MassFlowRate, USCustomaryMassFlowRate>.div(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (unit per area.unit).massFlux(this, area)

@JvmName("massFlowRateDivArea")
infix operator fun <MassFlowRateUnit : MassFlowRate, AreaUnit : Area> ScientificValue<PhysicalQuantity.MassFlowRate, MassFlowRateUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (Kilogram per Second per SquareMeter).massFlux(this, area)
