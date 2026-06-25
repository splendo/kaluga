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

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.EquivalentDoseRate
import com.splendo.kaluga.scientific.unit.IonizingRadiationEquivalentDose
import com.splendo.kaluga.scientific.unit.Time
import kotlin.jvm.JvmName

@JvmName("equivalentDoseFromEquivalentDoseRateAndTimeDefault")
fun <
    EquivalentDoseUnit : IonizingRadiationEquivalentDose,
    TimeUnit : Time,
    > EquivalentDoseUnit.equivalentDose(
    equivalentDoseRate: ScientificValue<PhysicalQuantity.EquivalentDoseRate, EquivalentDoseRate>,
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
) = equivalentDose(equivalentDoseRate, time, ::DefaultScientificValue)

@JvmName("equivalentDoseFromEquivalentDoseRateAndTime")
fun <
    EquivalentDoseUnit : IonizingRadiationEquivalentDose,
    TimeUnit : Time,
    Value : ScientificValue<PhysicalQuantity.IonizingRadiationEquivalentDose, EquivalentDoseUnit>,
    > EquivalentDoseUnit.equivalentDose(
    equivalentDoseRate: ScientificValue<PhysicalQuantity.EquivalentDoseRate, EquivalentDoseRate>,
    time: ScientificValue<PhysicalQuantity.Time, TimeUnit>,
    factory: (Decimal, EquivalentDoseUnit) -> Value,
) = byMultiplying(equivalentDoseRate, time, factory)
