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

package com.splendo.kaluga.scientific.converter.molarEnergy

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AmountOfSubstance
import com.splendo.kaluga.scientific.unit.Energy
import com.splendo.kaluga.scientific.unit.MolarEnergy
import kotlin.jvm.JvmName

@JvmName("molarEnergyFromEnergyAndAmountOfSubstanceDefault")
fun <
    EnergyUnit : Energy,
    AmountOfSubstanceUnit : AmountOfSubstance,
    MolarEnergyUnit : MolarEnergy
    > MolarEnergyUnit.molarEnergy(
    energy: ScientificValue<PhysicalQuantity.Energy, EnergyUnit>,
    amountOfSubstance: ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>
) = molarEnergy(energy, amountOfSubstance, ::DefaultScientificValue)

@JvmName("molarEnergyFromEnergyAndAmountOfSubstance")
fun <
    EnergyUnit : Energy,
    AmountOfSubstanceUnit : AmountOfSubstance,
    MolarEnergyUnit : MolarEnergy,
    Value : ScientificValue<PhysicalQuantity.MolarEnergy, MolarEnergyUnit>
    > MolarEnergyUnit.molarEnergy(
    energy: ScientificValue<PhysicalQuantity.Energy, EnergyUnit>,
    amountOfSubstance: ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>,
    factory: (Decimal, MolarEnergyUnit) -> Value
) = byDividing(energy, amountOfSubstance, factory)
