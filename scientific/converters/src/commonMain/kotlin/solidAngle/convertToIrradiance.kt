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

package com.splendo.kaluga.scientific.converter.solidAngle

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.irradiance.irradiance
import com.splendo.kaluga.scientific.unit.ImperialRadiance
import com.splendo.kaluga.scientific.unit.MetricRadiance
import com.splendo.kaluga.scientific.unit.SolidAngle
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("solidAngleTimesMetricRadiance")
infix operator fun <SolidAngleUnit : SolidAngle> ScientificValue<PhysicalQuantity.SolidAngle, SolidAngleUnit>.times(
    radiance: ScientificValue<PhysicalQuantity.Radiance, MetricRadiance>,
) = (radiance.unit.radiantIntensity.power per radiance.unit.per).irradiance(radiance, this)

@JvmName("solidAngleTimesImperialRadiance")
infix operator fun <SolidAngleUnit : SolidAngle> ScientificValue<PhysicalQuantity.SolidAngle, SolidAngleUnit>.times(
    radiance: ScientificValue<PhysicalQuantity.Radiance, ImperialRadiance>,
) = (radiance.unit.radiantIntensity.power per radiance.unit.per).irradiance(radiance, this)
