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

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.molarEntropy.molarEntropy
import com.splendo.kaluga.scientific.unit.AmountOfSubstance
import com.splendo.kaluga.scientific.unit.HeatCapacity
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kelvin
import com.splendo.kaluga.scientific.unit.MetricAndUKImperialHeatCapacity
import com.splendo.kaluga.scientific.unit.MetricHeatCapacity
import com.splendo.kaluga.scientific.unit.Mole
import com.splendo.kaluga.scientific.unit.UKImperialHeatCapacity
import com.splendo.kaluga.scientific.unit.USCustomaryHeatCapacity
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("metricAndUKImperialHeatCapacityDivAmountOfSubstance")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.HeatCapacity, MetricAndUKImperialHeatCapacity>.div(
    amountOfSubstance: ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>,
) = (unit per amountOfSubstance.unit).molarEntropy(this, amountOfSubstance)

@JvmName("metricHeatCapacityDivAmountOfSubstance")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.HeatCapacity, MetricHeatCapacity>.div(
    amountOfSubstance: ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>,
) = (unit per amountOfSubstance.unit).molarEntropy(this, amountOfSubstance)

@JvmName("ukImperialHeatCapacityDivAmountOfSubstance")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.HeatCapacity, UKImperialHeatCapacity>.div(
    amountOfSubstance: ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>,
) = (unit per amountOfSubstance.unit).molarEntropy(this, amountOfSubstance)

@JvmName("usCustomaryHeatCapacityDivAmountOfSubstance")
infix operator fun <AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.HeatCapacity, USCustomaryHeatCapacity>.div(
    amountOfSubstance: ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>,
) = (unit per amountOfSubstance.unit).molarEntropy(this, amountOfSubstance)

@JvmName("heatCapacityDivAmountOfSubstance")
infix operator fun <HeatCapacityUnit : HeatCapacity, AmountOfSubstanceUnit : AmountOfSubstance> ScientificValue<PhysicalQuantity.HeatCapacity, HeatCapacityUnit>.div(
    amountOfSubstance: ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>,
) = ((Joule per Kelvin) per Mole).molarEntropy(this, amountOfSubstance)
