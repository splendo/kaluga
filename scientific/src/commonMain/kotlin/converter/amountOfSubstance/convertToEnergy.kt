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

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molarEnergy.times
import com.splendo.kaluga.scientific.unit.AmountOfSubstance
import com.splendo.kaluga.scientific.unit.ImperialMolarEnergy
import com.splendo.kaluga.scientific.unit.MetricAndImperialMolarEnergy
import com.splendo.kaluga.scientific.unit.MetricMolarEnergy
import com.splendo.kaluga.scientific.unit.MolarEnergy
import kotlin.jvm.JvmName

@JvmName("amountOfSubstanceTimesMetricAndImperialMolarEnergy")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.times(
    molarEnergy: ScientificValue<PhysicalQuantity.MolarEnergy, MetricAndImperialMolarEnergy>
) = molarEnergy * this

@JvmName("amountOfSubstanceTimesMetricMolarEnergy")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.times(
    molarEnergy: ScientificValue<PhysicalQuantity.MolarEnergy, MetricMolarEnergy>
) = molarEnergy * this

@JvmName("amountOfSubstanceTimesImperialMolarEnergy")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.times(
    molarEnergy: ScientificValue<PhysicalQuantity.MolarEnergy, ImperialMolarEnergy>
) = molarEnergy * this

@JvmName("amountOfSubstanceTimesMolarEnergy")
infix operator fun <MolarEnergyUnit : MolarEnergy, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>.times(
    molarEnergy: ScientificValue<PhysicalQuantity.MolarEnergy, MolarEnergyUnit>
) = molarEnergy * this
