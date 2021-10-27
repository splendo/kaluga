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

package com.splendo.kaluga.scientific.force

import com.splendo.kaluga.scientific.Dyne
import com.splendo.kaluga.scientific.ErgPerSecond
import com.splendo.kaluga.scientific.FootPoundForcePerSecond
import com.splendo.kaluga.scientific.Force
import com.splendo.kaluga.scientific.ImperialForce
import com.splendo.kaluga.scientific.ImperialSpeed
import com.splendo.kaluga.scientific.MeasurementSystem
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.MetricForce
import com.splendo.kaluga.scientific.MetricMultipleUnit
import com.splendo.kaluga.scientific.MetricSpeed
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.Speed
import com.splendo.kaluga.scientific.UKImperialForce
import com.splendo.kaluga.scientific.USCustomaryForce
import com.splendo.kaluga.scientific.Watt
import com.splendo.kaluga.scientific.metric
import com.splendo.kaluga.scientific.power.power
import kotlin.jvm.JvmName

@JvmName("dyneTimesMetricSpeed")
infix operator fun ScientificValue<MeasurementType.Force, Dyne>.times(speed: ScientificValue<MeasurementType.Speed, MetricSpeed>) = ErgPerSecond.metric.power(this, speed)
@JvmName("dyneMultipleTimesMetricSpeed")
infix operator fun <DyneUnit> ScientificValue<MeasurementType.Force, DyneUnit>.times(speed: ScientificValue<MeasurementType.Speed, MetricSpeed>) where DyneUnit : Force, DyneUnit : MetricMultipleUnit<MeasurementSystem.Metric, MeasurementType.Force, Dyne> = ErgPerSecond.metric.power(this, speed)
@JvmName("metricForceTimesMetricSpeed")
infix operator fun <ForceUnit : MetricForce> ScientificValue<MeasurementType.Force, ForceUnit>.times(speed: ScientificValue<MeasurementType.Speed, MetricSpeed>) = Watt.metric.power(this, speed)
@JvmName("imperialForceTimesImperialSpeed")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<MeasurementType.Force, ForceUnit>.times(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) = FootPoundForcePerSecond.power(this, speed)
@JvmName("ukImperialForceTimesImperialSpeed")
infix operator fun <ForceUnit : UKImperialForce> ScientificValue<MeasurementType.Force, ForceUnit>.times(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) = FootPoundForcePerSecond.power(this, speed)
@JvmName("usCustomaryForceTimesImperialSpeed")
infix operator fun <ForceUnit : USCustomaryForce> ScientificValue<MeasurementType.Force, ForceUnit>.times(speed: ScientificValue<MeasurementType.Speed, ImperialSpeed>) = FootPoundForcePerSecond.power(this, speed)
@JvmName("forceTimesSpeed")
infix operator fun <ForceUnit : Force, SpeedUnit : Speed> ScientificValue<MeasurementType.Force, ForceUnit>.times(speed: ScientificValue<MeasurementType.Speed, SpeedUnit>) = Watt.power(this, speed)
