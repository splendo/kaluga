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

package com.splendo.kaluga.scientific.converter.yank

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.MeasurementType
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.byMultiplying
import com.splendo.kaluga.scientific.unit.Jolt
import com.splendo.kaluga.scientific.unit.Weight
import com.splendo.kaluga.scientific.unit.Yank
import kotlin.jvm.JvmName

@JvmName("yankFromMassAndJoltDefault")
fun <
    WeightUnit : Weight,
    JoltUnit : Jolt,
    YankUnit : Yank
> YankUnit.yank(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>,
    jolt: ScientificValue<MeasurementType.Jolt, JoltUnit>
) = yank(weight, jolt, ::DefaultScientificValue)

@JvmName("yankFromMassAndJolt")
fun <
    WeightUnit : Weight,
    JoltUnit : Jolt,
    YankUnit : Yank,
    Value : ScientificValue<MeasurementType.Yank, YankUnit>
> YankUnit.yank(
    weight: ScientificValue<MeasurementType.Weight, WeightUnit>,
    jolt: ScientificValue<MeasurementType.Jolt, JoltUnit>,
    factory: (Decimal, YankUnit) -> Value
) = byMultiplying(weight, jolt, factory)
