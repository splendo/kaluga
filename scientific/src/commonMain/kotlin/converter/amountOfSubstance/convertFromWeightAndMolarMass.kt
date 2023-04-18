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
import com.splendo.kaluga.scientific.unit.MolarMass
import com.splendo.kaluga.scientific.unit.Weight
import kotlin.jvm.JvmName

@JvmName("amountOfSubstanceFromWeightAndMolarMassDefault")
fun <
    AmountOfSubstanceUnit : AmountOfSubstance,
    WeightUnit : Weight,
    MolarMassUnit : MolarMass,
    > AmountOfSubstanceUnit.amountOfSubstance(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
    molarMass: ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>,
) = amountOfSubstance(weight, molarMass, ::DefaultScientificValue)

@JvmName("amountOfSubstanceFromWeightAndMolarMass")
fun <
    AmountOfSubstanceUnit : AmountOfSubstance,
    WeightUnit : Weight,
    MolarMassUnit : MolarMass,
    Value : ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>,
    > AmountOfSubstanceUnit.amountOfSubstance(
    weight: ScientificValue<PhysicalQuantity.Weight, WeightUnit>,
    molarMass: ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>,
    factory: (Decimal, AmountOfSubstanceUnit) -> Value,
) = byDividing(weight, molarMass, factory)
