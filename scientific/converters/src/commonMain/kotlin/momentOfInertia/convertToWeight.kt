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

package com.splendo.kaluga.scientific.converter.momentOfInertia

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.weight.mass
import com.splendo.kaluga.scientific.unit.ImperialMomentOfInertia
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.MetricMomentOfInertia
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MomentOfInertia
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.UKImperialMomentOfInertia
import com.splendo.kaluga.scientific.unit.USCustomaryMomentOfInertia
import kotlin.jvm.JvmName

@JvmName("metricMomentOfInertiaDivMetricArea")
infix operator fun ScientificValue<PhysicalQuantity.MomentOfInertia, MetricMomentOfInertia>.div(area: ScientificValue<PhysicalQuantity.Area, MetricArea>) =
    unit.weight.mass(this, area)

@JvmName("imperialMomentOfInertiaDivImperialArea")
infix operator fun ScientificValue<PhysicalQuantity.MomentOfInertia, ImperialMomentOfInertia>.div(area: ScientificValue<PhysicalQuantity.Area, ImperialArea>) =
    unit.weight.mass(this, area)

@JvmName("ukImperialMomentOfInertiaDivImperialArea")
infix operator fun ScientificValue<PhysicalQuantity.MomentOfInertia, UKImperialMomentOfInertia>.div(area: ScientificValue<PhysicalQuantity.Area, ImperialArea>) =
    unit.weight.mass(this, area)

@JvmName("usCustomaryMomentOfInertiaDivImperialArea")
infix operator fun ScientificValue<PhysicalQuantity.MomentOfInertia, USCustomaryMomentOfInertia>.div(area: ScientificValue<PhysicalQuantity.Area, ImperialArea>) =
    unit.weight.mass(this, area)

@JvmName("momentOfInertiaDivArea")
infix operator fun <MomentOfInertiaUnit : MomentOfInertia, AreaUnit : Area> ScientificValue<PhysicalQuantity.MomentOfInertia, MomentOfInertiaUnit>.div(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = unit.weight.mass(this, area)
