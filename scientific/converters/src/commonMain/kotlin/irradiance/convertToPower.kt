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

package com.splendo.kaluga.scientific.converter.irradiance

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.power.power
import com.splendo.kaluga.scientific.unit.Area
import com.splendo.kaluga.scientific.unit.ImperialIrradiance
import com.splendo.kaluga.scientific.unit.Irradiance
import com.splendo.kaluga.scientific.unit.MetricIrradiance
import kotlin.jvm.JvmName

@JvmName("metricIrradianceTimesArea")
infix operator fun <AreaUnit : Area> ScientificValue<PhysicalQuantity.Irradiance, MetricIrradiance>.times(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    unit.power.power(this, area)

@JvmName("imperialIrradianceTimesArea")
infix operator fun <AreaUnit : Area> ScientificValue<PhysicalQuantity.Irradiance, ImperialIrradiance>.times(area: ScientificValue<PhysicalQuantity.Area, AreaUnit>) =
    unit.power.power(this, area)

@JvmName("irradianceTimesArea")
infix operator fun <IrradianceUnit : Irradiance, AreaUnit : Area> ScientificValue<PhysicalQuantity.Irradiance, IrradianceUnit>.times(
    area: ScientificValue<PhysicalQuantity.Area, AreaUnit>,
) = unit.power.power(this, area)
