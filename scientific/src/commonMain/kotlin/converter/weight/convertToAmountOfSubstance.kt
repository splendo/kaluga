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

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.amountOfSubstance.amountOfSubstance
import com.splendo.kaluga.scientific.converter.molality.times
import com.splendo.kaluga.scientific.unit.Molality
import com.splendo.kaluga.scientific.unit.MolarMass
import com.splendo.kaluga.scientific.unit.Weight
import kotlin.jvm.JvmName

@JvmName("weightTimesMolality")
infix operator fun <MolalityUnit : Molality, WeightUnit : Weight> ScientificValue<MeasurementType.Weight, WeightUnit>.times(molality: ScientificValue<MeasurementType.Molality, MolalityUnit>) = molality * this

@JvmName("weightDivMolarMass")
infix operator fun <WeightUnit : Weight, MolarMassUnit : MolarMass> ScientificValue<MeasurementType.Weight, WeightUnit>.div(molarMass: ScientificValue<MeasurementType.MolarMass, MolarMassUnit>) = molarMass.unit.per.amountOfSubstance(this, molarMass)
