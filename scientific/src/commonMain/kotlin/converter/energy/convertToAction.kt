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

package com.splendo.kaluga.scientific.converter.energy

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.action.action
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.ImperialEnergy
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.MetricAndImperialEnergy
import com.splendo.kaluga.scientific.unit.MetricEnergy
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("metricAndImperialEnergyTimesTime")
infix operator fun <EnergyUnit : MetricAndImperialEnergy, TimeUnit : Time> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = (unit x time.unit).action(this, time)

@JvmName("metricEnergyTimesTime")
infix operator fun <EnergyUnit : MetricEnergy, TimeUnit : Time> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = (unit x time.unit).action(this, time)

@JvmName("imperialEnergyTimesTime")
infix operator fun <EnergyUnit : ImperialEnergy, TimeUnit : Time> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = (unit x time.unit).action(this, time)

@JvmName("energyTimesTime")
infix operator fun <EnergyUnit : Energy, TimeUnit : Time> ScientificValue<PhysicalQuantity.Energy, EnergyUnit>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (Joule x time.unit).action(this, time)
