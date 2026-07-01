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

package com.splendo.kaluga.scientific.converter.speed

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.force.times
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.DyneMultiple
import com.splendo.kaluga.scientific.unit.Force
import com.splendo.kaluga.scientific.unit.ImperialForce
import com.splendo.kaluga.scientific.unit.ImperialSpeed
import com.splendo.kaluga.scientific.unit.MetricSpeed
import com.splendo.kaluga.scientific.unit.Speed
import com.splendo.kaluga.scientific.unit.UKImperialForce
import com.splendo.kaluga.scientific.unit.USCustomaryForce
import kotlin.jvm.JvmName

@JvmName("metricSpeedTimesDyne")
infix operator fun ScientificValue<PhysicalQuantity.Speed, MetricSpeed>.times(force: ScientificValue<PhysicalQuantity.Force, Dyne>) = force * this

@JvmName("metricSpeedTimesDyneMultiple")
infix operator fun <DyneUnit : DyneMultiple> ScientificValue<PhysicalQuantity.Speed, MetricSpeed>.times(force: ScientificValue<PhysicalQuantity.Force, DyneUnit>) = force * this

@JvmName("imperialSpeedTimesImperialForce")
infix operator fun <ForceUnit : ImperialForce> ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>.times(force: ScientificValue<PhysicalQuantity.Force, ForceUnit>) =
    force * this

@JvmName("imperialSpeedTimesUKImperialForce")
infix operator fun <ForceUnit : UKImperialForce> ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>.times(force: ScientificValue<PhysicalQuantity.Force, ForceUnit>) =
    force * this

@JvmName("imperialSpeedTimesUSCustomaryForce")
infix operator fun <ForceUnit : USCustomaryForce> ScientificValue<PhysicalQuantity.Speed, ImperialSpeed>.times(force: ScientificValue<PhysicalQuantity.Force, ForceUnit>) =
    force * this

@JvmName("speedTimesMetricForce")
infix operator fun <ForceUnit : Force, SpeedUnit : Speed> ScientificValue<PhysicalQuantity.Speed, SpeedUnit>.times(force: ScientificValue<PhysicalQuantity.Force, ForceUnit>) =
    force * this
