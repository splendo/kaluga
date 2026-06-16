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

package com.splendo.kaluga.scientific.converter.volume

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.Volume
import com.splendo.kaluga.scientific.unit.Weight
import kotlin.jvm.JvmName

@JvmName("volumeFromSpecificVolumeAndWeightDefault")
fun <
    VolumeUnit : Volume,
    SpecificVolumeUnit : SpecificVolume,
    WeightUnit : Weight,
    > VolumeUnit.volume(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, SpecificVolumeUnit>,
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
) = volume(specificVolume, weight, ::DefaultScientificValue)

@JvmName("volumeFromSpecificVolumeAndWeight")
fun <
    VolumeUnit : Volume,
    SpecificVolumeUnit : SpecificVolume,
    WeightUnit : Weight,
    Value : ScientificValue<PhysicalQuantity.Volume, VolumeUnit>,
    > VolumeUnit.volume(
    specificVolume: ScientificValue<PhysicalQuantity.SpecificVolume, SpecificVolumeUnit>,
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
    factory: (Decimal, VolumeUnit) -> Value,
) = byMultiplying(specificVolume, weight, factory)
