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

package com.splendo.kaluga.scientific.converter.kinematicViscosity

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.specificEnergy.specificEnergy
import com.splendo.kaluga.scientific.unit.FootPoundForce
import com.splendo.kaluga.scientific.unit.ImperialKinematicViscosity
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.KinematicViscosity
import com.splendo.kaluga.scientific.unit.MetricKinematicViscosity
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.Time
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricKinematicViscosityDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.KinematicViscosity, MetricKinematicViscosity>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (Joule per Kilogram).specificEnergy(this, time)

@JvmName("imperialKinematicViscosityDivTime")
infix operator fun <TimeUnit : Time> ScientificValue<PhysicalQuantity.KinematicViscosity, ImperialKinematicViscosity>.div(time: ScientificValue<PhysicalQuantity.Time, TimeUnit>) =
    (FootPoundForce per Pound).specificEnergy(this, time)

@JvmName("kinematicViscosityDivTime")
infix operator fun <KinematicViscosityUnit : KinematicViscosity, TimeUnit : Time> ScientificValue<PhysicalQuantity.KinematicViscosity, KinematicViscosityUnit>.div(
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = (Joule per Kilogram).specificEnergy(this, time)
