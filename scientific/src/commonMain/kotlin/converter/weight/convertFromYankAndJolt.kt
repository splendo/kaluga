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

package com.splendo.kaluga.scientific.converter.weight

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byDividing
import com.splendo.kaluga.scientific.unit.Jolt
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.Yank
import kotlin.jvm.JvmName

@JvmName("massFromYankAndJoltDefault")
fun <
    WeightUnit : Weight,
    JoltUnit : Jolt,
    YankUnit : Yank
    > WeightUnit.mass(
    yank: ScientificValue<PhysicalQuantity.Yank, YankUnit>,
    jolt: ScientificValue<PhysicalQuantity.Jolt, JoltUnit>
) = mass(yank, jolt, ::DefaultScientificValue)

@JvmName("massFromYankAndJolt")
fun <
    WeightUnit : Weight,
    JoltUnit : Jolt,
    YankUnit : Yank,
    Value : ScientificValue<PhysicalQuantity.Weight, WeightUnit>
    > WeightUnit.mass(
    yank: ScientificValue<PhysicalQuantity.Yank, YankUnit>,
    jolt: ScientificValue<PhysicalQuantity.Jolt, JoltUnit>,
    factory: (Decimal, WeightUnit) -> Value
) = byDividing(yank, jolt, factory)
