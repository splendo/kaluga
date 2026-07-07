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

package com.splendo.kaluga.scientific.converter.power

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.radiantIntensity.radiantIntensity
import com.splendo.kaluga.scientific.unit.ImperialPower
import com.splendo.kaluga.scientific.unit.MetricAndImperialPower
import com.splendo.kaluga.scientific.unit.MetricPower
import com.splendo.kaluga.scientific.unit.Power
import com.splendo.kaluga.scientific.unit.SolidAngle
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricAndImperialPowerDivSolidAngle")
infix operator fun <PowerUnit : MetricAndImperialPower, SolidAngleUnit : SolidAngle> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    solidAngle: ScientificValue<PhysicalQuantity.SolidAngle, SolidAngleUnit>,
) = (unit per solidAngle.unit).radiantIntensity(this, solidAngle)

@JvmName("metricPowerDivSolidAngle")
infix operator fun <PowerUnit : MetricPower, SolidAngleUnit : SolidAngle> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    solidAngle: ScientificValue<PhysicalQuantity.SolidAngle, SolidAngleUnit>,
) = (unit per solidAngle.unit).radiantIntensity(this, solidAngle)

@JvmName("imperialPowerDivSolidAngle")
infix operator fun <PowerUnit : ImperialPower, SolidAngleUnit : SolidAngle> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    solidAngle: ScientificValue<PhysicalQuantity.SolidAngle, SolidAngleUnit>,
) = (unit per solidAngle.unit).radiantIntensity(this, solidAngle)

@JvmName("powerDivSolidAngle")
infix operator fun <PowerUnit : Power, SolidAngleUnit : SolidAngle> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(
    solidAngle: ScientificValue<PhysicalQuantity.SolidAngle, SolidAngleUnit>,
) = (Watt per solidAngle.unit).radiantIntensity(this, solidAngle)
