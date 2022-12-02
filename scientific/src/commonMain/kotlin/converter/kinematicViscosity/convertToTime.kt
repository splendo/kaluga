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

package com.splendo.kaluga.scientific.converter.kinematicViscosity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.time.time
import com.splendo.kaluga.scientific.unit.ImperialKinematicViscosity
import com.splendo.kaluga.scientific.unit.KinematicViscosity
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricKinematicViscosity
import kotlin.jvm.JvmName

@JvmName("metricKinematicViscosityDivMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.KinematicViscosity, MetricKinematicViscosity>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>
) = this.unit.time.time(area, this)

@JvmName("imperialKinematicViscosityTimesDivImperialArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.KinematicViscosity, ImperialKinematicViscosity>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>
) = this.unit.time.time(area, this)

@JvmName("kinematicViscosityTimesDivArea")
infix operator fun <AreaUnit : MetricArea, KinematicViscosityUnit : KinematicViscosity> ScientificValue<PhysicalQuantity.KinematicViscosity, KinematicViscosityUnit>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>
) = this.unit.time.time(area, this)
