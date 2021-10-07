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

package com.splendo.kaluga.scientific

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.toDecimal

data class ScientificValue<System : MeasurementSystem, Type : MeasurementType, Unit : ScientificUnit<System, Type>>(
    val value: Decimal,
    val unit: Unit
) {
    constructor(value: Double, unit: Unit) : this(value.toDecimal(), unit)
}

fun <
    Type : MeasurementType,
    Unit : ScientificUnit<*, Type>,
    TargetSystem : MeasurementSystem,
    TargetUnit : ScientificUnit<TargetSystem, Type>
    > ScientificValue<*, Type, Unit>.convert(target: TargetUnit) : ScientificValue<TargetSystem, Type,  TargetUnit> {
    return ScientificValue(unit.convert(value, target), target)
}