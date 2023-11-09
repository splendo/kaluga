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
import com.splendo.kaluga.scientific.converter.force.force
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.ErgPerSecond
import com.splendo.kaluga.scientific.unit.ImperialPower
import com.splendo.kaluga.scientific.unit.ImperialSpeed
import com.splendo.kaluga.scientific.unit.MetricAndImperialPower
import com.splendo.kaluga.scientific.unit.MetricPower
import com.splendo.kaluga.scientific.unit.MetricSpeed
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Power
import com.splendo.kaluga.scientific.unit.Speed
import kotlin.jvm.JvmName

@JvmName("ergPerSecondDivMetricSpeed")
infix operator fun ScientificValue<PhysicalQuantity.Power, ErgPerSecond>.div(speed: ScientificValue<PhysicalQuantity.Speed, MetricSpeed>) = Dyne.force(this, speed)

@JvmName("metricPowerDivMetricSpeed")
infix operator fun <PowerUnit : MetricPower> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(speed: ScientificValue<PhysicalQuantity.Speed, MetricSpeed>) =
    Newton.force(this, speed)

@JvmName("imperialPowerDivImperialSpeed")
infix operator fun <PowerUnit : ImperialPower> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(speed: ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>) =
    PoundForce.force(this, speed)

@JvmName("metricAndImperialPowerDivImperialSpeed")
infix operator fun <PowerUnit : MetricAndImperialPower> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(speed: ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>) =
    PoundForce.force(this, speed)

@JvmName("powerDivSpeed")
infix operator fun <PowerUnit : Power, SpeedUnit : Speed> ScientificValue<PhysicalQuantity.Power, PowerUnit>.div(speed: ScientificValue<PhysicalQuantity.Speed, SpeedUnit>) =
    Newton.force(this, speed)
