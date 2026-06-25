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
import com.splendo.kaluga.scientific.converter.massFlux.times
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialMassFlux
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricMassFlux
import com.splendo.kaluga.scientific.unit.UKImperialMassFlux
import com.splendo.kaluga.scientific.unit.USCustomaryMassFlux
import com.splendo.kaluga.scientific.unit.MassFlux
import kotlin.jvm.JvmName

@JvmName("metricAreaTimesMetricMassFlux")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.Area, AreaUnit>.times(massFlux: ScientificValue<PhysicalQuantity.MassFlux, MetricMassFlux>) =
    massFlux * this

@JvmName("imperialAreaTimesImperialMassFlux")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Area, AreaUnit>.times(massFlux: ScientificValue<PhysicalQuantity.MassFlux, ImperialMassFlux>) =
    massFlux * this

@JvmName("imperialAreaTimesUKImperialMassFlux")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Area, AreaUnit>.times(massFlux: ScientificValue<PhysicalQuantity.MassFlux, UKImperialMassFlux>) =
    massFlux * this

@JvmName("imperialAreaTimesUSCustomaryMassFlux")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Area, AreaUnit>.times(massFlux: ScientificValue<PhysicalQuantity.MassFlux, USCustomaryMassFlux>) =
    massFlux * this

@JvmName("areaTimesMassFlux")
infix operator fun <MassFluxUnit : MassFlux, AreaUnit : Area> ScientificValue<PhysicalQuantity.Area, AreaUnit>.times(
    massFlux: ScientificValue<PhysicalQuantity.MassFlux, MassFluxUnit>,
) = massFlux * this
