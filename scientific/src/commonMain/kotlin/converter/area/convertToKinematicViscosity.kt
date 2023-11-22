/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.scientific.converter.kinematicViscosity.kinematicViscosity
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricAreaDivTime")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.Area, AreaUnit>.div(time: ScientificValue<PhysicalQuantity.Time, Time>) =
    (unit per time.unit).kinematicViscosity(this, time)

@JvmName("imperialAreaDivTime")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.Area, AreaUnit>.div(time: ScientificValue<PhysicalQuantity.Time, Time>) =
    (unit per time.unit).kinematicViscosity(this, time)

@JvmName("areaDivTime")
infix operator fun <AreaUnit : Area> ScientificValue<PhysicalQuantity.Area, AreaUnit>.div(time: ScientificValue<PhysicalQuantity.Time, Time>) =
    (SquareMeter per time.unit).kinematicViscosity(this, time)
