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

package com.splendo.kaluga.scientific.converter.specificWeight

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.Acceleration
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.SpecificWeight
import kotlin.jvm.JvmName

@JvmName("specificWeightFromDensityAndAccelerationDefault")
fun <
    SpecificWeightUnit : SpecificWeight,
    DensityUnit : Density,
    AccelerationUnit : Acceleration,
    > SpecificWeightUnit.specificWeight(
    density: ScientificValue<PhysicalQuantity.Density, DensityUnit>,
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, AccelerationUnit>,
) = specificWeight(density, acceleration, ::DefaultScientificValue)

@JvmName("specificWeightFromDensityAndAcceleration")
fun <
    SpecificWeightUnit : SpecificWeight,
    DensityUnit : Density,
    AccelerationUnit : Acceleration,
    Value : ScientificValue<PhysicalQuantity.SpecificWeight, SpecificWeightUnit>,
    > SpecificWeightUnit.specificWeight(
    density: ScientificValue<PhysicalQuantity.Density, DensityUnit>,
    acceleration: ScientificValue<PhysicalQuantity.Acceleration, AccelerationUnit>,
    factory: (Decimal, SpecificWeightUnit) -> Value,
) = byMultiplying(density, acceleration, factory)
