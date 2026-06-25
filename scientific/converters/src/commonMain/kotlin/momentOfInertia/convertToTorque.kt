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

package com.splendo.kaluga.scientific.converter.momentOfInertia

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.torque.torque
import com.splendo.kaluga.scientific.unit.AngularAcceleration
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.ImperialMomentOfInertia
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MomentOfInertia
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.x
import kotlin.jvm.JvmName

@JvmName("imperialMomentOfInertiaTimesAngularAcceleration")
infix operator fun ScientificValue<PhysicalQuantity.MomentOfInertia, ImperialMomentOfInertia>.times(
    angularAcceleration: ScientificValue<PhysicalQuantity.AngularAcceleration, AngularAcceleration>,
) = (PoundForce x Foot).torque(this, angularAcceleration)

@JvmName("momentOfInertiaTimesAngularAcceleration")
infix operator fun <MomentOfInertiaUnit : MomentOfInertia> ScientificValue<PhysicalQuantity.MomentOfInertia, MomentOfInertiaUnit>.times(
    angularAcceleration: ScientificValue<PhysicalQuantity.AngularAcceleration, AngularAcceleration>,
) = (Newton x Meter).torque(this, angularAcceleration)
