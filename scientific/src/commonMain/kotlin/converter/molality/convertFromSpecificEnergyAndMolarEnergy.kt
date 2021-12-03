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
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.Molality
import com.splendo.kaluga.scientific.unit.MolarEnergy
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import kotlin.jvm.JvmName

@JvmName("molalityFromSpecificEnergyAndMolarEnergyDefault")
fun <
    SpecificEnergyUnit : SpecificEnergy,
    MolarEnergyUnit : MolarEnergy,
    MolalityUnit : Molality
    > MolalityUnit.molality(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, SpecificEnergyUnit>,
    molarEnergy: ScientificValue<PhysicalQuantity.MolarEnergy, MolarEnergyUnit>
) = molality(specificEnergy, molarEnergy, ::DefaultScientificValue)

@JvmName("molalityFromSpecificEnergyAndMolarEnergy")
fun <
    SpecificEnergyUnit : SpecificEnergy,
    MolarEnergyUnit : MolarEnergy,
    MolalityUnit : Molality,
    Value : ScientificValue<PhysicalQuantity.Molality, MolalityUnit>
    > MolalityUnit.molality(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, SpecificEnergyUnit>,
    molarEnergy: ScientificValue<PhysicalQuantity.MolarEnergy, MolarEnergyUnit>,
    factory: (Decimal, MolalityUnit) -> Value
) = byDividing(specificEnergy, molarEnergy, factory)
