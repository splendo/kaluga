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

package com.splendo.kaluga.scientific.converter.molality

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.Molality
import com.splendo.kaluga.scientific.unit.MolarVolume
import com.splendo.kaluga.scientific.unit.SpecificVolume
import kotlin.jvm.JvmName

@JvmName("molalityFromSpecificVolumeAndMolarVolumeDefault")
fun <
    SpecificVolumeUnit : SpecificVolume,
    MolarVolumeUnit : MolarVolume,
    MolalityUnit : Molality
> MolalityUnit.molality(
    specificVolume: ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>,
    molarVolume: ScientificValue<MeasurementType.MolarVolume, MolarVolumeUnit>
) = molality(specificVolume, molarVolume, ::DefaultScientificValue)

@JvmName("molalityFromSpecificVolumeAndMolarVolume")
fun <
    SpecificVolumeUnit : SpecificVolume,
    MolarVolumeUnit : MolarVolume,
    MolalityUnit : Molality,
    Value : ScientificValue<MeasurementType.Molality, MolalityUnit>
> MolalityUnit.molality(
    specificVolume: ScientificValue<MeasurementType.SpecificVolume, SpecificVolumeUnit>,
    molarVolume: ScientificValue<MeasurementType.MolarVolume, MolarVolumeUnit>,
    factory: (Decimal, MolalityUnit) -> Value
) = byDividing(specificVolume, molarVolume, factory)