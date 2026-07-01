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
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.SpecificWeight
import com.splendo.kaluga.scientific.unit.Volume
import com.splendo.kaluga.scientific.unit.Force
import kotlin.jvm.JvmName

@JvmName("specificWeightFromForceAndVolumeDefault")
fun <
    ForceUnit : Force,
    VolumeUnit : Volume,
    SpecificWeightUnit : SpecificWeight,
    > SpecificWeightUnit.specificWeight(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
) = specificWeight(force, volume, ::DefaultScientificValue)

@JvmName("specificWeightFromForceAndVolume")
fun <
    ForceUnit : Force,
    VolumeUnit : Volume,
    SpecificWeightUnit : SpecificWeight,
    Value : ScientificValue<PhysicalQuantity.SpecificWeight, SpecificWeightUnit>,
    > SpecificWeightUnit.specificWeight(
    force: ScientificValue<PhysicalQuantity.Force, ForceUnit>,
    volume: ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
    factory: (Decimal, SpecificWeightUnit) -> Value,
) = byDividing(force, volume, factory)
