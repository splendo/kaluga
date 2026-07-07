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

package com.splendo.kaluga.scientific.converter.density

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.specificWeight.specificWeight
import com.splendo.kaluga.scientific.unit.Acceleration
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.ImperialDensity
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("imperialDensityTimesAcceleration")
infix operator fun <AccelerationUnit : Acceleration> ScientificValue<PhysicalQuantity.Density, ImperialDensity>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, AccelerationUnit>,
) = (PoundForce per CubicFoot).specificWeight(this, acceleration)

@JvmName("densityTimesAcceleration")
infix operator fun <DensityUnit : Density, AccelerationUnit : Acceleration> ScientificValue<PhysicalQuantity.Density, DensityUnit>.times(
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, AccelerationUnit>,
) = (Newton per CubicMeter).specificWeight(this, acceleration)
