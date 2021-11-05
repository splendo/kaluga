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

package com.splendo.kaluga.scientific.converter.molarMass

import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.weight.mass
import com.splendo.kaluga.scientific.unit.AmountOfSubstance
import com.splendo.kaluga.scientific.unit.ImperialMolarMass
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MetricMolarMass
import com.splendo.kaluga.scientific.unit.MolarMass
import com.splendo.kaluga.scientific.unit.UKImperialMolarMass
import com.splendo.kaluga.scientific.unit.USCustomaryMolarMass
import kotlin.jvm.JvmName

@JvmName("metricMolarMassTimesAmountOfSubstance")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.MolarMass, MetricMolarMass>.times(
    amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>
) = unit.weight.mass(this, amountOfSubstance)

@JvmName("imperialMolarMassTimesAmountOfSubstance")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.MolarMass, ImperialMolarMass>.times(
    amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>
) = unit.weight.mass(this, amountOfSubstance)

@JvmName("ukImperialMolarMassTimesAmountOfSubstance")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.MolarMass, UKImperialMolarMass>.times(
    amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>
) = unit.weight.mass(this, amountOfSubstance)

@JvmName("usCustomaryMolarMassTimesAmountOfSubstance")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.MolarMass, USCustomaryMolarMass>.times(
    amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>
) = unit.weight.mass(this, amountOfSubstance)

@JvmName("molarMassTimesAmountOfSubstance")
infix operator fun <MolarMassUnit : MolarMass, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<MeasurementType.MolarMass, MolarMassUnit>.times(
    amountOfSubstance: ScientificValue<MeasurementType.AmountOfSubstance, AmountOfSubstanceUnit>
) = Kilogram.mass(this, amountOfSubstance)
