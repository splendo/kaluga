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

package com.splendo.kaluga.scientific.converter.ionizingRadiationEquivalentDose

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.unit.IonizingRadiationEquivalentDose
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.Sievert
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("specificEnergyFromEquivalentDoseDefault")
fun <
    EquivalentDoseUnit : IonizingRadiationEquivalentDose,
    SpecificEnergyUnit : SpecificEnergy,
    > EquivalentDoseUnit.equivalentDose(
    specificEnergy: ScientificValue<PhysicalQuantity.SpecificEnergy, SpecificEnergyUnit>,
) = equivalentDose(specificEnergy, ::DefaultScientificValue)

@JvmName("specificEnergyFromEquivalentDose")
fun <
    EquivalentDoseUnit : IonizingRadiationEquivalentDose,
    SpecificEnergyUnit : SpecificEnergy,
    Value : ScientificValue<PhysicalQuantity.IonizingRadiationEquivalentDose, EquivalentDoseUnit>,
    > EquivalentDoseUnit.equivalentDose(
    equivalentDose: ScientificValue<PhysicalQuantity.SpecificEnergy, SpecificEnergyUnit>,
    factory: (Decimal, EquivalentDoseUnit) -> Value,
) = DefaultScientificValue(equivalentDose.convert(Joule per Kilogram).value, Sievert).convert(
    this,
    factory,
)
