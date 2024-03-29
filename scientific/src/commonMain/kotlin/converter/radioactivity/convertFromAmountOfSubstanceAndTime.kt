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

package com.splendo.kaluga.scientific.converter.radioactivity

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.AmountOfSubstance
import com.splendo.kaluga.scientific.unit.AvogadroConstant
import com.splendo.kaluga.scientific.unit.Radioactivity
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName
import kotlin.math.ln

@JvmName("radioactivityFromSubstanceAndHalfLifeDefault")
fun <
    AmountOfSubstanceUnit : AmountOfSubstance,
    TimeUnit : Time,
    RadioactivityUnit : Radioactivity,
    > RadioactivityUnit.radioactivity(
    substance: ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>,
    halfLife: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = radioactivity(substance, halfLife, ::DefaultScientificValue)

@JvmName("radioactivityFromSubstanceAndHalfLife")
fun <
    AmountOfSubstanceUnit : AmountOfSubstance,
    TimeUnit : Time,
    RadioactivityUnit : Radioactivity,
    Value : ScientificValue<PhysicalQuantity.Radioactivity, RadioactivityUnit>,
    > RadioactivityUnit.radioactivity(
    substance: ScientificValue<PhysicalQuantity.AmountOfSubstance, AmountOfSubstanceUnit>,
    halfLife: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
    factory: (Decimal, RadioactivityUnit) -> Value,
) = byDividing(
    DefaultScientificValue(
        substance.decimalValue * AvogadroConstant * ln(2.0).toDecimal(),
        substance.unit,
    ),
    halfLife,
    factory,
)
