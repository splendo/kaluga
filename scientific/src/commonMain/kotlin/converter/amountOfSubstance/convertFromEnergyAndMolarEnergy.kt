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

package com.splendo.kaluga.scientific.converter.amountOfSubstance

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AmountOfSubstance
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.MolarEnergy
import kotlin.jvm.JvmName

@JvmName("amountOfSubstanceFromEnergyAndMolarEnergyDefault")
fun <
    EnergyUnit : Energy,
    AmountOfSubstanceUnit : AmountOfSubstance,
    MolarEnergyUnit : MolarEnergy,
    > AmountOfSubstanceUnit.amountOfSubstance(
    energy: ScientificValue<PhysicalQuantity.Energy, EnergyUnit>,
    molarEnergy: ScientificValue<PhysicalQuantity.MolarEnergy, MolarEnergyUnit>,
) = amountOfSubstance(energy, molarEnergy, ::DefaultScientificValue)

@JvmName("amountOfSubstanceFromEnergyAndMolarEnergy")
fun <
    EnergyUnit : Energy,
    AmountOfSubstanceUnit : AmountOfSubstance,
    MolarEnergyUnit : MolarEnergy,
    Value : ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>,
    > AmountOfSubstanceUnit.amountOfSubstance(
    energy: ScientificValue<PhysicalQuantity.Energy, EnergyUnit>,
    molarEnergy: ScientificValue<PhysicalQuantity.MolarEnergy, MolarEnergyUnit>,
    factory: (Decimal, AmountOfSubstanceUnit) -> Value,
) = byDividing(energy, molarEnergy, factory)
