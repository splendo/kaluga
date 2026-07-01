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

package com.splendo.kaluga.scientific.converter.heatCapacity

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.AmountOfSubstance
import com.splendo.kaluga.scientific.unit.HeatCapacity
import com.splendo.kaluga.scientific.unit.MolarEntropy
import kotlin.jvm.JvmName

@JvmName("heatCapacityFromMolarEntropyAndAmountOfSubstanceDefault")
fun <
    HeatCapacityUnit : HeatCapacity,
    AmountOfSubstanceUnit : AmountOfSubstance,
    MolarEntropyUnit : MolarEntropy,
    > HeatCapacityUnit.heatCapacity(
    molarEntropy: ScientificValue<PhysicalQuantity.MolarEntropy, MolarEntropyUnit>,
    amountOfSubstance: ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>,
) = heatCapacity(molarEntropy, amountOfSubstance, ::DefaultScientificValue)

@JvmName("heatCapacityFromMolarEntropyAndAmountOfSubstance")
fun <
    HeatCapacityUnit : HeatCapacity,
    AmountOfSubstanceUnit : AmountOfSubstance,
    MolarEntropyUnit : MolarEntropy,
    Value : ScientificValue<PhysicalQuantity.HeatCapacity, HeatCapacityUnit>,
    > HeatCapacityUnit.heatCapacity(
    molarEntropy: ScientificValue<PhysicalQuantity.MolarEntropy, MolarEntropyUnit>,
    amountOfSubstance: ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>,
    factory: (Decimal, HeatCapacityUnit) -> Value,
) = byMultiplying(molarEntropy, amountOfSubstance, factory)
