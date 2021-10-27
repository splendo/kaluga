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

package com.splendo.kaluga.scientific.power

import com.splendo.kaluga.scientific.Dyne
import com.splendo.kaluga.scientific.ErgPerSecond
import com.splendo.kaluga.scientific.FootPoundForcePerSecond
import com.splendo.kaluga.scientific.ImperialPower
import com.splendo.kaluga.scientific.ImperialSpeed
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricAndImperialPower
import com.splendo.kaluga.scientific.MetricPower
import com.splendo.kaluga.scientific.MetricSpeed
import com.splendo.kaluga.scientific.Newton
import com.splendo.kaluga.scientific.PoundForce
import com.splendo.kaluga.scientific.Power
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Speed
import com.splendo.kaluga.scientific.force.force
import kotlin.jvm.JvmName

@JvmName("ergPerSecondDivMetricSpeed")
infix operator fun ScientificValue<MeasurementType.Power, ErgPerSecond>.div(speed: ScientificValue<MeasurementType.Speed, MetricSpeed>) = Dyne.force(this, speed)
@JvmName("metricPowerDivMetricSpeed")
infix operator fun <PowerUnit : MetricPower> ScientificValue<MeasurementType.Power, PowerUnit>.div(speed: ScientificValue<MeasurementType.Speed, MetricSpeed>) = Newton.force(this, speed)
@JvmName("imperialPowerDivImperialSpeed")
infix operator fun <PowerUnit : ImperialPower> ScientificValue<MeasurementType.Power, PowerUnit>.div(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) = PoundForce.force(this, speed)
@JvmName("metricAndImperialPowerDivImperialSpeed")
infix operator fun <PowerUnit : MetricAndImperialPower> ScientificValue<MeasurementType.Power, PowerUnit>.div(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) = PoundForce.force(this, speed)
@JvmName("powerDivSpeed")
infix operator fun <PowerUnit : Power, SpeedUnit : Speed> ScientificValue<MeasurementType.Power, PowerUnit>.div(speed: ScientificValue<MeasurementType.Speed, SpeedUnit>) = Newton.force(this, speed)
