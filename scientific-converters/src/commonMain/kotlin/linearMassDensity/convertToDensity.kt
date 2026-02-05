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

package com.splendo.kaluga.scientific.converter.linearMassDensity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.density.density
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialLinearMassDensity
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.LinearMassDensity
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricLinearMassDensity
import com.splendo.kaluga.scientific.unit.UKImperialLinearMassDensity
import com.splendo.kaluga.scientific.unit.USCustomaryLinearMassDensity
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricLinearMassDensityDivMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.LinearMassDensity, MetricLinearMassDensity>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit.weight per (1(unit.per) * 1(area.unit)).unit).density(this, area)

@JvmName("imperialLinearMassDensityDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.LinearMassDensity, ImperialLinearMassDensity>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit.weight per (1(unit.per) * 1(area.unit)).unit).density(this, area)

@JvmName("ukImperialLinearMassDensityDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.LinearMassDensity, UKImperialLinearMassDensity>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit.weight per (1(unit.per) * 1(area.unit)).unit).density(this, area)

@JvmName("usCustomaryLinearMassDensityDivImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.LinearMassDensity, USCustomaryLinearMassDensity>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit.weight per (1(unit.per) * 1(area.unit)).unit).density(this, area)

@JvmName("linearMassDensityDivArea")
infix operator fun <LinearMassDensityUnit : LinearMassDensity, AreaUnit : Area> ScientificValue<PhysicalQuantity.LinearMassDensity, LinearMassDensityUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (Kilogram per CubicMeter).density(this, area)
