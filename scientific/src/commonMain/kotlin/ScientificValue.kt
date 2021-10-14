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
import com.splendo.kaluga.base.utils.toDouble

data class ScientificValue<Type : MeasurementType, Unit : ScientificUnit<Type>> (
    val value: Decimal,
    val unit: Unit
) : MeasurementUsage by unit.system, Comparable<ScientificValue<Type, *>> {
    constructor(value: Double, unit: Unit) : this(value.toDecimal(), unit)
    constructor(value: Int, unit: Unit) : this(value.toDecimal(), unit)

    override fun compareTo(other: ScientificValue<Type, *>): Int = unit.toSIUnit(value).compareTo(other.unit.toSIUnit(other.value))

    val doubleValue: Double get() = value.toDouble()
}

fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>,
    TargetUnit : ScientificUnit<Type>
    > ScientificValue<Type, Unit>.convert(target: TargetUnit) : ScientificValue<Type,  TargetUnit> {
    return ScientificValue(convertValue(target), target)
}

fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>,
    TargetUnit : ScientificUnit<Type>
    > ScientificValue<Type, Unit>.convertValue(target: TargetUnit) : Decimal {
    return unit.convert(value, target)
}

operator fun <
    Type : MeasurementType,
    UnitType : ScientificUnit<Type>
    > Int.invoke(unit: UnitType) = ScientificValue(this, unit)
operator fun <
    Type : MeasurementType,
    UnitType : ScientificUnit<Type>
    > Double.invoke(unit: UnitType) = ScientificValue(this, unit)
operator fun <
    Type : MeasurementType,
    UnitType : ScientificUnit<Type>
    > Decimal.invoke(unit: UnitType) = ScientificValue(this, unit)
