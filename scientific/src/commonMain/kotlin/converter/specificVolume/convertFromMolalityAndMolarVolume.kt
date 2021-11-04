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
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.Molality
import com.splendo.kaluga.scientific.MolarVolume
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.SpecificVolume
import com.splendo.kaluga.scientific.byMultiplying
import kotlin.jvm.JvmName

@JvmName("specificVolumeFromMolalityAndMolarVolumeDefault")
fun <
    SpecificVolumeUnit : SpecificVolume,
    MolalityUnit : Molality,
    MolarVolumeUnit : MolarVolume
> SpecificVolumeUnit.specificVolume(
    molarVolume: ScientificValue<MeasurementType.MolarVolume, MolarVolumeUnit>,
    molality: ScientificValue<MeasurementType.Molality, MolalityUnit>
) = specificVolume(molarVolume, molality, ::DefaultScientificValue)

@JvmName("specificVolumeFromMolalityAndMolarVolume")
fun <
    SpecificVolumeUnit : SpecificVolume,
    MolalityUnit : Molality,
    MolarVolumeUnit : MolarVolume,
    Value : ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>
> SpecificVolumeUnit.specificVolume(
    molarVolume: ScientificValue<MeasurementType.MolarVolume, MolarVolumeUnit>,
    molality: ScientificValue<MeasurementType.Molality, MolalityUnit>,
    factory: (Decimal, SpecificVolumeUnit) -> Value
) = byMultiplying(molarVolume, molality, factory)
