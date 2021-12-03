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

package com.splendo.kaluga.scientific.converter.amountOfSubstance

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molarMass.times
import com.splendo.kaluga.scientific.converter.weight.weight
import com.splendo.kaluga.scientific.unit.AmountOfSubstance
import com.splendo.kaluga.scientific.unit.ImperialMolality
import com.splendo.kaluga.scientific.unit.ImperialMolarMass
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricMolality
import com.splendo.kaluga.scientific.unit.MetricMolarMass
import com.splendo.kaluga.scientific.unit.Molality
import com.splendo.kaluga.scientific.unit.MolarMass
import com.splendo.kaluga.scientific.unit.UKImperialMolality
import com.splendo.kaluga.scientific.unit.UKImperialMolarMass
import com.splendo.kaluga.scientific.unit.USCustomaryMolality
import com.splendo.kaluga.scientific.unit.USCustomaryMolarMass
import kotlin.jvm.JvmName

@JvmName("amountOfSubstanceDivMetricMolality")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.div(
    molality: ScientificValue<PhysicalQuantity.Molality, MetricMolality>
) = molality.unit.per.weight(this, molality)

@JvmName("amountOfSubstanceDivImperialMolality")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.div(
    molality: ScientificValue<PhysicalQuantity.Molality, ImperialMolality>
) = molality.unit.per.weight(this, molality)

@JvmName("amountOfSubstanceDivUKImperialMolality")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.div(
    molality: ScientificValue<PhysicalQuantity.Molality, UKImperialMolality>
) = molality.unit.per.weight(this, molality)

@JvmName("amountOfSubstanceDivUSCustomaryMolality")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.div(
    molality: ScientificValue<PhysicalQuantity.Molality, USCustomaryMolality>
) = molality.unit.per.weight(this, molality)

@JvmName("amountOfSubstanceDivMolality")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance, MolalityUnit : Molality> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.div(
    molality: ScientificValue<PhysicalQuantity.Molality, MolalityUnit>
) = Kilogram.weight(this, molality)

@JvmName("amountOfSubstanceTimesMetricMolarMass")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.times(
    molarMass: ScientificValue<PhysicalQuantity.MolarMass, MetricMolarMass>
) = molarMass * this

@JvmName("amountOfSubstanceTimesImperialMolarMass")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.times(
    molarMass: ScientificValue<PhysicalQuantity.MolarMass, ImperialMolarMass>
) = molarMass * this

@JvmName("amountOfSubstanceTimesUKImperialMolarMass")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.times(
    molarMass: ScientificValue<PhysicalQuantity.MolarMass, UKImperialMolarMass>
) = molarMass * this

@JvmName("amountOfSubstanceTimesUSCustomaryMolarMass")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.times(
    molarMass: ScientificValue<PhysicalQuantity.MolarMass, USCustomaryMolarMass>
) = molarMass * this

@JvmName("amountOfSubstanceTimesMolarMass")
infix operator fun <MolarMassUnit : MolarMass, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.times(
    molarMass: ScientificValue<PhysicalQuantity.MolarMass, MolarMassUnit>
) = molarMass * this
