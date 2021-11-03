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
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.base.utils.toDouble
import kotlinx.serialization.Serializable

interface ScientificValue<Type : MeasurementType, Unit : ScientificUnit<Type>> : Comparable<ScientificValue<Type, *>> {
    val value: Number
    val unit: Unit

    override fun compareTo(other: ScientificValue<Type, *>): Int = unit.toSIUnit(value.toDecimal()).compareTo(other.unit.toSIUnit(other.value.toDecimal()))

    val decimalValue: Decimal get() = value.toDecimal()
}

@Serializable
data class DefaultScientificValue<Type : MeasurementType, Unit : ScientificUnit<Type>>(
    override val value: Double,
    override val unit: Unit
) : ScientificValue<Type, Unit> {
    constructor(value: Decimal, unit: Unit) : this(value.toDouble(), unit)
}

fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>,
    TargetUnit : ScientificUnit<Type>,
    TargetValue : ScientificValue<Type, TargetUnit>
    > ScientificValue<Type, Unit>.convert(target: TargetUnit, factory: (Decimal, TargetUnit) -> TargetValue) : TargetValue {
    return factory(convertValue(target), target)
}

fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>,
    TargetUnit : ScientificUnit<Type>
    > ScientificValue<Type, Unit>.convert(target: TargetUnit) = convert(target, ::DefaultScientificValue)

fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>,
    TargetUnit : ScientificUnit<Type>
    > ScientificValue<Type, Unit>.convertValue(target: TargetUnit) : Decimal {
    return unit.convert(decimalValue, target)
}

operator fun <
    Type : MeasurementType,
    UnitType : ScientificUnit<Type>
    > Number.invoke(unit: UnitType) = this.toDecimal()(unit)
operator fun <
    Type : MeasurementType,
    UnitType : ScientificUnit<Type>,
    ValueType : ScientificValue<Type, UnitType>
    > Number.invoke(unit: UnitType, factory: (Decimal, UnitType) -> ValueType) = this.toDecimal()(unit, factory)

operator fun <
    Type : MeasurementType,
    UnitType : ScientificUnit<Type>
    > Decimal.invoke(unit: UnitType) = this(unit, ::DefaultScientificValue)
operator fun <
    Type : MeasurementType,
    UnitType : ScientificUnit<Type>,
    ValueType : ScientificValue<Type, UnitType>
    > Decimal.invoke(unit: UnitType, factory: (Decimal, UnitType) -> ValueType) = factory(this, unit)

internal fun <
    TargetType : MeasurementType,
    Unit : ScientificUnit<TargetType>,
    NominatorType : MeasurementType,
    NominatorUnit : ScientificUnit<NominatorType>,
    DividerType : MeasurementType,
    DividerUnit : ScientificUnit<DividerType>
    >
    Unit.byDividing(
    nominator: ScientificValue<NominatorType, NominatorUnit>,
    divider: ScientificValue<DividerType, DividerUnit>
): ScientificValue<TargetType, Unit> = fromSIUnit(nominator.unit.toSIUnit(nominator.decimalValue) / divider.unit.toSIUnit(divider.decimalValue))(this)

internal fun <
    TargetType : MeasurementType,
    Unit : ScientificUnit<TargetType>,
    LeftType : MeasurementType,
    LeftUnit : ScientificUnit<LeftType>,
    RightType : MeasurementType,
    RightUnit : ScientificUnit<RightType>
    >
    Unit.byMultiplying(
    left: ScientificValue<LeftType, LeftUnit>,
    right: ScientificValue<RightType, RightUnit>
): ScientificValue<TargetType, Unit> = fromSIUnit(left.unit.toSIUnit(left.decimalValue) * right.unit.toSIUnit(right.decimalValue))(this)

internal fun <
    InverseType : MeasurementType,
    InverseUnit : ScientificUnit<InverseType>,
    Type : MeasurementType,
    Unit : ScientificUnit<Type>
    >
    Unit.byInverting(
    inverse: ScientificValue<InverseType, InverseUnit>
): ScientificValue<Type, Unit> = fromSIUnit(1.0.toDecimal() / inverse.unit.toSIUnit(inverse.decimalValue))(this)
