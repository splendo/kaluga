/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.converter.specificEnergy

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.kinematicViscosity.kinematicViscosity
import com.splendo.kaluga.scientific.unit.*
import kotlin.jvm.JvmName

@JvmName("metricSpecificEnergyTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.SpecificEnergy, MetricSpecificEnergy>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (SquareMeter per time.unit).kinematicViscosity(this, time)

@JvmName("imperialSpecificEnergyTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.SpecificEnergy, ImperialSpecificEnergy>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (SquareFoot per time.unit).kinematicViscosity(this, time)

@JvmName("ukImperialSpecificEnergyTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.SpecificEnergy, UKImperialSpecificEnergy>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (SquareFoot per time.unit).kinematicViscosity(this, time)

@JvmName("usCustomarySpecificEnergyTimesTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.SpecificEnergy, USCustomarySpecificEnergy>.times(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (SquareFoot per time.unit).kinematicViscosity(this, time)

@JvmName("specificEnergyTimesTime")
infix operator fun <SpecificEnergyUnit : SpecificEnergy, TimeUnit : Time> ScientificValue<PhysicalQuantity.SpecificEnergy, SpecificEnergyUnit>.times(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = (SquareMeter per time.unit).kinematicViscosity(this, time)
