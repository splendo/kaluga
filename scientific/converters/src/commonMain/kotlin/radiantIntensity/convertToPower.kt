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

package com.splendo.kaluga.scientific.converter.radiantIntensity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.power.power
import com.splendo.kaluga.scientific.unit.ImperialRadiantIntensity
import com.splendo.kaluga.scientific.unit.MetricAndImperialRadiantIntensity
import com.splendo.kaluga.scientific.unit.MetricRadiantIntensity
import com.splendo.kaluga.scientific.unit.RadiantIntensity
import com.splendo.kaluga.scientific.unit.SolidAngle
import kotlin.jvm.JvmName

@JvmName("metricAndImperialRadiantIntensityTimesSolidAngle")
infix operator fun <SolidAngleUnit : SolidAngle> ScientificValue<PhysicalQuantity.RadiantIntensity, MetricAndImperialRadiantIntensity>.times(
    solidAngle: ScientificValue<PhysicalQuantity.SolidAngle, SolidAngleUnit>,
) = unit.power.power(this, solidAngle)

@JvmName("metricRadiantIntensityTimesSolidAngle")
infix operator fun <SolidAngleUnit : SolidAngle> ScientificValue<PhysicalQuantity.RadiantIntensity, MetricRadiantIntensity>.times(
    solidAngle: ScientificValue<PhysicalQuantity.SolidAngle, SolidAngleUnit>,
) = unit.power.power(this, solidAngle)

@JvmName("imperialRadiantIntensityTimesSolidAngle")
infix operator fun <SolidAngleUnit : SolidAngle> ScientificValue<PhysicalQuantity.RadiantIntensity, ImperialRadiantIntensity>.times(
    solidAngle: ScientificValue<PhysicalQuantity.SolidAngle, SolidAngleUnit>,
) = unit.power.power(this, solidAngle)

@JvmName("radiantIntensityTimesSolidAngle")
infix operator fun <RadiantIntensityUnit : RadiantIntensity, SolidAngleUnit : SolidAngle> ScientificValue<PhysicalQuantity.RadiantIntensity, RadiantIntensityUnit>.times(
    solidAngle: ScientificValue<PhysicalQuantity.SolidAngle, SolidAngleUnit>,
) = unit.power.power(this, solidAngle)
