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
import com.splendo.kaluga.base.utils.minus
import com.splendo.kaluga.base.utils.plus
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.base.utils.toDouble
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.convert
import kotlinx.serialization.Serializable

interface ScientificValue<Type : MeasurementType, Unit : ScientificUnit<Type>> : Comparable<ScientificValue<Type, *>> {
    val value: Number
    val unit: Unit

    override fun compareTo(other: ScientificValue<Type, *>): Int = unit.toSIUnit(decimalValue).compareTo(other.unit.toSIUnit(other.decimalValue))

    val decimalValue: Decimal get() = value.toDecimal()
}

@Serializable
data class DefaultScientificValue<Type : MeasurementType, Unit : ScientificUnit<Type>>(
    override val value: Double,
    override val unit: Unit
) : ScientificValue<Type, Unit> {
    constructor(value: Decimal, unit: Unit) : this(value.toDouble(), unit)
}

// Creation

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

// Conversion

fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>,
    TargetUnit : ScientificUnit<Type>,
    TargetValue : ScientificValue<Type, TargetUnit>
    > ScientificValue<Type, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue
): TargetValue = factory(convertValue(target), target)

fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>,
    TargetUnit : ScientificUnit<Type>
    > ScientificValue<Type, Unit>.convert(target: TargetUnit) = convert(target, ::DefaultScientificValue)

fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>,
    TargetUnit : ScientificUnit<Type>
    > ScientificValue<Type, Unit>.convertValue(target: TargetUnit): Decimal {
    return unit.convert(decimalValue, target)
}

// Calculation

infix operator fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>
    > ScientificValue<Type, Unit>.plus(factor: Number) = this + factor.toDecimal()

infix operator fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>
    > Number.plus(unit: ScientificValue<Type, Unit>) = toDecimal() + unit

infix operator fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>
    > ScientificValue<Type, Unit>.plus(factor: Decimal) = add(factor, ::DefaultScientificValue)

infix operator fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>
    > Decimal.plus(unit: ScientificValue<Type, Unit>) = unit.add(this, ::DefaultScientificValue)

fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>,
    ValueType : ScientificValue<Type, Unit>
    > ScientificValue<Type, Unit>.add(
    factor: Decimal,
    factory: (Decimal, Unit) -> ValueType
) = factory(decimalValue + factor, unit)

infix operator fun <
    Type : MeasurementType,
    Left : ScientificUnit<Type>,
    Right : ScientificUnit<Type>
    > ScientificValue<Type, Left>.plus(right: ScientificValue<Type, Right>) = unit.add(this, right, ::DefaultScientificValue)

fun <
    Type : MeasurementType,
    Left : ScientificUnit<Type>,
    Right : ScientificUnit<Type>,
    Unit : ScientificUnit<Type>,
    ValueType : ScientificValue<Type, Unit>
    > Unit.add(
    left: ScientificValue<Type, Left>,
    right: ScientificValue<Type, Right>,
    factory: (Decimal, Unit) -> ValueType
) = byAdding(left, right, factory)

infix operator fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>
    > ScientificValue<Type, Unit>.minus(value: Number) = this - value.toDecimal()

infix operator fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>
    > Number.minus(unit: ScientificValue<Type, Unit>) = toDecimal() - unit

infix operator fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>
    > ScientificValue<Type, Unit>.minus(value: Decimal) = subtract(value, ::DefaultScientificValue)

infix operator fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>
    > Decimal.minus(unit: ScientificValue<Type, Unit>) = subtract(unit, ::DefaultScientificValue)

fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>,
    ValueType : ScientificValue<Type, Unit>
    > Decimal.subtract(
    value: ScientificValue<Type, Unit>,
    factory: (Decimal, Unit) -> ValueType
) = factory(this - value.decimalValue, value.unit)

fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>,
    ValueType : ScientificValue<Type, Unit>
    > ScientificValue<Type, Unit>.subtract(
    value: Decimal,
    factory: (Decimal, Unit) -> ValueType
) = factory(decimalValue - value, unit)

infix operator fun <
    Type : MeasurementType,
    Left : ScientificUnit<Type>,
    Right : ScientificUnit<Type>
    > ScientificValue<Type, Left>.minus(right: ScientificValue<Type, Right>) = unit.subtract(this, right, ::DefaultScientificValue)

fun <
    Type : MeasurementType,
    Left : ScientificUnit<Type>,
    Right : ScientificUnit<Type>,
    Unit : ScientificUnit<Type>,
    ValueType : ScientificValue<Type, Unit>
    > Unit.subtract(
    left: ScientificValue<Type, Left>,
    right: ScientificValue<Type, Right>,
    factory: (Decimal, Unit) -> ValueType
) = bySubtracting(left, right, factory)

infix operator fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>
    > ScientificValue<Type, Unit>.times(factor: Number) = this * factor.toDecimal()

infix operator fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>
    > Number.times(unit: ScientificValue<Type, Unit>) = toDecimal() * unit

infix operator fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>
    > ScientificValue<Type, Unit>.times(factor: Decimal) = multiply(factor, ::DefaultScientificValue)

infix operator fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>
    > Decimal.times(unit: ScientificValue<Type, Unit>) = unit.multiply(this, ::DefaultScientificValue)

fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>,
    ValueType : ScientificValue<Type, Unit>
    > ScientificValue<Type, Unit>.multiply(
    factor: Decimal,
    factory: (Decimal, Unit) -> ValueType
) = factory(decimalValue * factor, unit)

infix operator fun <
    Type : MeasurementType,
    Left : ScientificUnit<Type>,
    Right : ScientificUnit<Type>
    > ScientificValue<Type, Left>.times(right: ScientificValue<Type, Right>) = unit.multiply(this, right, ::DefaultScientificValue)

fun <
    Type : MeasurementType,
    Left : ScientificUnit<Type>,
    Right : ScientificUnit<Type>,
    Unit : ScientificUnit<Type>,
    ValueType : ScientificValue<Type, Unit>
    > Unit.multiply(
    left: ScientificValue<Type, Left>,
    right: ScientificValue<Type, Right>,
    factory: (Decimal, Unit) -> ValueType
) = byMultiplying(left, right, factory)

infix operator fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>
    > ScientificValue<Type, Unit>.div(factor: Number) = this / factor.toDecimal()

infix operator fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>
    > Number.div(unit: ScientificValue<Type, Unit>) = toDecimal() / unit

infix operator fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>
    > ScientificValue<Type, Unit>.div(factor: Decimal) = divide(factor, ::DefaultScientificValue)

infix operator fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>
    > Decimal.div(unit: ScientificValue<Type, Unit>) = divide(unit, ::DefaultScientificValue)

fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>,
    ValueType : ScientificValue<Type, Unit>
    > Decimal.divide(
    factor: ScientificValue<Type, Unit>,
    factory: (Decimal, Unit) -> ValueType
) = factory(this / factor.decimalValue, factor.unit)

fun <
    Type : MeasurementType,
    Unit : ScientificUnit<Type>,
    ValueType : ScientificValue<Type, Unit>
    > ScientificValue<Type, Unit>.divide(
    factor: Decimal,
    factory: (Decimal, Unit) -> ValueType
) = factory(decimalValue / factor, unit)

infix operator fun <
    Type : MeasurementType,
    Left : ScientificUnit<Type>,
    Right : ScientificUnit<Type>
    > ScientificValue<Type, Left>.div(right: ScientificValue<Type, Right>) = unit.divide(this, right, ::DefaultScientificValue)

fun <
    Type : MeasurementType,
    Left : ScientificUnit<Type>,
    Right : ScientificUnit<Type>,
    Unit : ScientificUnit<Type>,
    ValueType : ScientificValue<Type, Unit>
    > Unit.divide(
    left: ScientificValue<Type, Left>,
    right: ScientificValue<Type, Right>,
    factory: (Decimal, Unit) -> ValueType
) = byDividing(left, right, factory)

internal fun <
    TargetType : MeasurementType,
    Unit : ScientificUnit<TargetType>,
    ValueType : ScientificValue<TargetType, Unit>,
    LeftType : MeasurementType,
    LeftUnit : ScientificUnit<LeftType>,
    RightType : MeasurementType,
    RightUnit : ScientificUnit<RightType>
    > Unit.byAdding(
    left: ScientificValue<LeftType, LeftUnit>,
    right: ScientificValue<RightType, RightUnit>,
    factory: (Decimal, Unit) -> ValueType
) = fromSIUnit(left.unit.toSIUnit(left.decimalValue) + right.unit.toSIUnit(right.decimalValue))(this, factory)

internal fun <
    TargetType : MeasurementType,
    Unit : ScientificUnit<TargetType>,
    ValueType : ScientificValue<TargetType, Unit>,
    LeftType : MeasurementType,
    LeftUnit : ScientificUnit<LeftType>,
    RightType : MeasurementType,
    RightUnit : ScientificUnit<RightType>
    > Unit.bySubtracting(
    left: ScientificValue<LeftType, LeftUnit>,
    right: ScientificValue<RightType, RightUnit>,
    factory: (Decimal, Unit) -> ValueType
) = fromSIUnit(left.unit.toSIUnit(left.decimalValue) - right.unit.toSIUnit(right.decimalValue))(this, factory)

internal fun <
    TargetType : MeasurementType,
    Unit : ScientificUnit<TargetType>,
    ValueType : ScientificValue<TargetType, Unit>,
    NominatorType : MeasurementType,
    NominatorUnit : ScientificUnit<NominatorType>,
    DividerType : MeasurementType,
    DividerUnit : ScientificUnit<DividerType>
    > Unit.byDividing(
    nominator: ScientificValue<NominatorType, NominatorUnit>,
    divider: ScientificValue<DividerType, DividerUnit>,
    factory: (Decimal, Unit) -> ValueType
) = fromSIUnit(nominator.unit.toSIUnit(nominator.decimalValue) / divider.unit.toSIUnit(divider.decimalValue))(this, factory)

internal fun <
    TargetType : MeasurementType,
    Unit : ScientificUnit<TargetType>,
    ValueType : ScientificValue<TargetType, Unit>,
    LeftType : MeasurementType,
    LeftUnit : ScientificUnit<LeftType>,
    RightType : MeasurementType,
    RightUnit : ScientificUnit<RightType>
    > Unit.byMultiplying(
    left: ScientificValue<LeftType, LeftUnit>,
    right: ScientificValue<RightType, RightUnit>,
    factory: (Decimal, Unit) -> ValueType
) = fromSIUnit(left.unit.toSIUnit(left.decimalValue) * right.unit.toSIUnit(right.decimalValue))(this, factory)

internal fun <
    InverseType : MeasurementType,
    InverseUnit : ScientificUnit<InverseType>,
    Type : MeasurementType,
    Unit : ScientificUnit<Type>,
    ValueType : ScientificValue<Type, Unit>
    > Unit.byInverting(
    inverse: ScientificValue<InverseType, InverseUnit>,
    factory: (Decimal, Unit) -> ValueType
) = fromSIUnit(1.0.toDecimal() / inverse.unit.toSIUnit(inverse.decimalValue))(this, factory)
