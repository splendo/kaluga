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
import com.splendo.kaluga.scientific.converter.radiance.radiance
import com.splendo.kaluga.scientific.unit.ImperialIrradiance
import com.splendo.kaluga.scientific.unit.MetricIrradiance
import com.splendo.kaluga.scientific.unit.SolidAngle
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricIrradianceDivSolidAngle")
infix operator fun <SolidAngleUnit : SolidAngle> ScientificValue<PhysicalQuantity.Irradiance, MetricIrradiance>.div(
    solidAngle: ScientificValue<PhysicalQuantity.SolidAngle, SolidAngleUnit>,
) = ((unit.power per solidAngle.unit) per unit.per).radiance(this, solidAngle)

@JvmName("imperialIrradianceDivSolidAngle")
infix operator fun <SolidAngleUnit : SolidAngle> ScientificValue<PhysicalQuantity.Irradiance, ImperialIrradiance>.div(
    solidAngle: ScientificValue<PhysicalQuantity.SolidAngle, SolidAngleUnit>,
) = ((unit.power per solidAngle.unit) per unit.per).radiance(this, solidAngle)
