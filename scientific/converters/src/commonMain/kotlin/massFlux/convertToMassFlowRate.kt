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

package com.splendo.kaluga.scientific.converter.massFlux

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.massFlowRate.massFlowRate
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialMassFlux
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricMassFlux
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.UKImperialMassFlux
import com.splendo.kaluga.scientific.unit.USCustomaryMassFlux
import com.splendo.kaluga.scientific.unit.MassFlux
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricMassFluxTimesMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.MassFlux, MetricMassFlux>.times(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (unit.massFlowRate).massFlowRate(this, area)

@JvmName("imperialMassFluxTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.MassFlux, ImperialMassFlux>.times(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (unit.massFlowRate).massFlowRate(this, area)

@JvmName("ukImperialMassFluxTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.MassFlux, UKImperialMassFlux>.times(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (unit.massFlowRate).massFlowRate(this, area)

@JvmName("usCustomaryMassFluxTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.MassFlux, USCustomaryMassFlux>.times(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    (unit.massFlowRate).massFlowRate(this, area)

@JvmName("massFluxTimesArea")
infix operator fun <MassFluxUnit : MassFlux, AreaUnit : Area> ScientificValue<PhysicalQuantity.MassFlux, MassFluxUnit>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (Kilogram per Second).massFlowRate(this, area)
