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

package com.splendo.kaluga.scientific.converter.force

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.power.power
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.ErgPerSecond
import com.splendo.kaluga.scientific.unit.FootPoundForcePerSecond
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.ImperialSpeed
import com.splendo.kaluga.scientific.unit.MeasurementSystem
import com.splendo.kaluga.scientific.unit.MetricForce
import com.splendo.kaluga.scientific.unit.MetricMultipleUnit
import com.splendo.kaluga.scientific.unit.MetricSpeed
import com.splendo.kaluga.scientific.unit.Speed
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import com.splendo.kaluga.scientific.unit.Watt
import com.splendo.kaluga.scientific.unit.metric
import kotlin.jvm.JvmName

@JvmName("dyneTimesMetricSpeed")
infix operator fun ScientificValue<PhysicalQuantity.Force, Dyne>.times(speed: ScientificValue<PhysicalQuantity.Speed, MetricSpeed>) =
    ErgPerSecond.metric.power(this, speed)

@JvmName("dyneMultipleTimesMetricSpeed")
infix operator fun <DyneUnit> ScientificValue<PhysicalQuantity.Force, DyneUnit>.times(speed: ScientificValue<PhysicalQuantity.Speed, MetricSpeed>) where DyneUnit : Force, DyneUnit : MetricMultipleUnit<MeasurementSystem.Metric, PhysicalQuantity.Force, Dyne> =
    ErgPerSecond.metric.power(this, speed)

@JvmName("metricForceTimesMetricSpeed")
infix operator fun <ForceUnit : MetricForce> ScientificValue<PhysicalQuantity.Force, ForceUnit>.times(
    speed: ScientificValue<PhysicalQuantity.Speed, MetricSpeed>
) = Watt.metric.power(this, speed)

@JvmName("imperialForceTimesImperialSpeed")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<PhysicalQuantity.Force, ForceUnit>.times(
    speed: ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>
) = FootPoundForcePerSecond.power(this, speed)

@JvmName("ukImperialForceTimesImperialSpeed")
infix operator fun <ForceUnit : UKImperialForce> ScientificValue<PhysicalQuantity.Force, ForceUnit>.times(
    speed: ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>
) = FootPoundForcePerSecond.power(this, speed)

@JvmName("usCustomaryForceTimesImperialSpeed")
infix operator fun <ForceUnit : USCustomaryForce> ScientificValue<PhysicalQuantity.Force, ForceUnit>.times(
    speed: ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>
) = FootPoundForcePerSecond.power(this, speed)

@JvmName("forceTimesSpeed")
infix operator fun <ForceUnit : Force, SpeedUnit : Speed> ScientificValue<PhysicalQuantity.Force, ForceUnit>.times(
    speed: ScientificValue<PhysicalQuantity.Speed, SpeedUnit>
) = Watt.power(this, speed)
