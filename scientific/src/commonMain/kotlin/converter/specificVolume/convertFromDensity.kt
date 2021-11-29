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

package com.splendo.kaluga.scientific.converter.specificVolume

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byInverting
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.SpecificVolume
import kotlin.jvm.JvmName

@JvmName("specificVolumeFromInvertedDensityDefault")
fun <
    SpecificVolumeUnit : SpecificVolume,
    DensityUnit : Density
    > SpecificVolumeUnit.specificVolume(
    density: ScientificValue<PhysicalQuantity.Density, DensityUnit>
) = specificVolume(density, ::DefaultScientificValue)

@JvmName("specificVolumeFromInvertedDensity")
fun <
    SpecificVolumeUnit : SpecificVolume,
    DensityUnit : Density,
    Value : ScientificValue<PhysicalQuantity.SpecificVolume, SpecificVolumeUnit>
    > SpecificVolumeUnit.specificVolume(
    density: ScientificValue<PhysicalQuantity.Density, DensityUnit>,
    factory: (Decimal, SpecificVolumeUnit) -> Value
) = byInverting(density, factory)