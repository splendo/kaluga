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

package com.splendo.kaluga.scientific.converter.luminousExposure

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.illuminance.times
import com.splendo.kaluga.scientific.converter.luminousEnergy.luminousEnergy
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.ImperialArea
import com.splendo.kaluga.scientific.unit.ImperialLuminousExposure
import com.splendo.kaluga.scientific.unit.LuminousExposure
import com.splendo.kaluga.scientific.unit.MetricArea
import com.splendo.kaluga.scientific.unit.MetricLuminousExposure
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("metricExposureTimesMetricArea")
infix operator fun <AreaUnit : MetricArea> ScientificValue<PhysicalQuantity.LuminousExposure, MetricLuminousExposure>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>
) = ((1(unit.illuminance) * 1(area.unit)).unit x unit.time).luminousEnergy(this, area)

@JvmName("imperialExposureTimesImperialArea")
infix operator fun <AreaUnit : ImperialArea> ScientificValue<PhysicalQuantity.LuminousExposure, ImperialLuminousExposure>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>
) = ((1(unit.illuminance) * 1(area.unit)).unit x unit.time).luminousEnergy(this, area)

@JvmName("exposureTimesArea")
infix operator fun <ExposureUnit : LuminousExposure, AreaUnit : Area> ScientificValue<PhysicalQuantity.LuminousExposure, ExposureUnit>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>
) = ((1(unit.illuminance) * 1(area.unit)).unit x unit.time).luminousEnergy(this, area)
