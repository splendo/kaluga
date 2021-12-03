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

package com.splendo.kaluga.scientific.converter.weight

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AmountOfSubstance
import com.splendo.kaluga.scientific.unit.Molality
import com.splendo.kaluga.scientific.unit.Weight
import kotlin.jvm.JvmName

@JvmName("weightFromAmountOfSubstanceAndMolalityDefault")
fun <
    AmountOfSubstanceUnit : AmountOfSubstance,
    WeightUnit : Weight,
    MolalityUnit : Molality
    > WeightUnit.weight(
    amountOfSubstance: ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>,
    molality: ScientificValue<PhysicalQuantity.Molality, MolalityUnit>
) = weight(amountOfSubstance, molality, ::DefaultScientificValue)

@JvmName("weightFromAmountOfSubstanceAndMolality")
fun <
    AmountOfSubstanceUnit : AmountOfSubstance,
    WeightUnit : Weight,
    MolalityUnit : Molality,
    Value : ScientificValue<PhysicalQuantity.Weight, WeightUnit>
    > WeightUnit.weight(
    amountOfSubstance: ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>,
    molality: ScientificValue<PhysicalQuantity.Molality, MolalityUnit>,
    factory: (Decimal, WeightUnit) -> Value
) = byDividing(amountOfSubstance, molality, factory)
