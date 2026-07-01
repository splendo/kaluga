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

package com.splendo.kaluga.scientific.converter.electricCurrent

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.magneticDipoleMoment.magneticDipoleMoment
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.ElectricCurrent
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("electricCurrentTimesMetricArea")
infix operator fun <CurrentUnit : ElectricCurrent, AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.ElectricCurrent, CurrentUnit>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit x area.unit).magneticDipoleMoment(this, area)

@JvmName("electricCurrentTimesImperialArea")
infix operator fun <CurrentUnit : ElectricCurrent, AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.ElectricCurrent, CurrentUnit>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit x area.unit).magneticDipoleMoment(this, area)

@JvmName("electricCurrentTimesArea")
infix operator fun <CurrentUnit : ElectricCurrent, AreaUnit : Area> ScientificValue<PhysicalQuantity.ElectricCurrent, CurrentUnit>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = (unit x SquareMeter).magneticDipoleMoment(this, area)
