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

package com.splendo.kaluga.scientific

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.RoundingMode
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.minus
import com.splendo.kaluga.base.utils.plus
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.base.utils.toDouble
import com.splendo.kaluga.scientific.converter.ScientificConverterException
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.convert
import kotlinx.serialization.Serializable

interface ScientificValue<Quantity : PhysicalQuantity, Unit : ScientificUnit<Quantity>> : Comparable<ScientificValue<Quantity, *>>, com.splendo.kaluga.base.utils.Serializable {
    val value: Number
    val unit: Unit

    override fun compareTo(other: ScientificValue<Quantity, *>): Int = unit.toSIUnit(decimalValue).compareTo(other.unit.toSIUnit(other.decimalValue))

    val decimalValue: Decimal get() = value.toDecimal()
}

@Serializable
data class DefaultScientificValue<Quantity : PhysicalQuantity, Unit : ScientificUnit<Quantity>>(
    override val value: Double,
    override val unit: Unit
) : ScientificValue<Quantity, Unit> {
    constructor(value: Decimal, unit: Unit) : this(value.toDouble(), unit)
}

// Creation

operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>
    > Number.invoke(unit: Unit) = this.toDecimal()(unit)
operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, Unit>
    > Number.invoke(unit: Unit, factory: (Decimal, Unit) -> Value) = this.toDecimal()(unit, factory)

operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>
    > Decimal.invoke(unit: Unit) = this(unit, ::DefaultScientificValue)
operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, Unit>
    > Decimal.invoke(unit: Unit, factory: (Decimal, Unit) -> Value) = factory(this, unit)

// Conversion

fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>,
    TargetValue : ScientificValue<Quantity, TargetUnit>
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    factory: (Decimal, TargetUnit) -> TargetValue
): TargetValue = factory(convertValue(target), target)

fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>
    > ScientificValue<Quantity, Unit>.convert(target: TargetUnit) = convert(target, ::DefaultScientificValue)

fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>
    > ScientificValue<Quantity, Unit>.convertValue(target: TargetUnit): Decimal {
    return unit.convert(decimalValue, target)
}

fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>,
    TargetValue : ScientificValue<Quantity, TargetUnit>
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
    factory: (Decimal, TargetUnit) -> TargetValue
): TargetValue = factory(convertValue(target, round, roundingMode), target)

fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>
    > ScientificValue<Quantity, Unit>.convert(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven
) = convert(target, round, roundingMode, ::DefaultScientificValue)

fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>
    > ScientificValue<Quantity, Unit>.convertValue(
    target: TargetUnit,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven
): Decimal {
    return unit.convert(decimalValue, target, round, roundingMode)
}

// Calculation

infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>
    > ScientificValue<Quantity, Unit>.plus(factor: Number) = this + factor.toDecimal()

infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>
    > Number.plus(unit: ScientificValue<Quantity, Unit>) = toDecimal() + unit

infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>
    > ScientificValue<Quantity, Unit>.plus(factor: Decimal) = plus(factor, ::DefaultScientificValue)

infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>
    > Decimal.plus(unit: ScientificValue<Quantity, Unit>) = unit.plus(this, ::DefaultScientificValue)

fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, Unit>
    > ScientificValue<Quantity, Unit>.plus(
    factor: Decimal,
    factory: (Decimal, Unit) -> Value
) = factory(decimalValue + factor, unit)

infix operator fun <
    Quantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>
    > ScientificValue<Quantity, LeftUnit>.plus(right: ScientificValue<Quantity, RightUnit>) = unit.plus(this, right, ::DefaultScientificValue)

fun <
    Quantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, TargetUnit>
    > TargetUnit.plus(
    left: ScientificValue<Quantity, LeftUnit>,
    right: ScientificValue<Quantity, RightUnit>,
    factory: (Decimal, TargetUnit) -> Value
) = byAdding(left, right, factory)

infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>
    > ScientificValue<Quantity, Unit>.minus(value: Number) = this - value.toDecimal()

infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>
    > Number.minus(unit: ScientificValue<Quantity, Unit>) = toDecimal() - unit

infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>
    > ScientificValue<Quantity, Unit>.minus(value: Decimal) = minus(value, ::DefaultScientificValue)

infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>
    > Decimal.minus(unit: ScientificValue<Quantity, Unit>) = minus(unit, ::DefaultScientificValue)

fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, Unit>
    > Decimal.minus(
    value: ScientificValue<Quantity, Unit>,
    factory: (Decimal, Unit) -> Value
) = factory(this - value.decimalValue, value.unit)

fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, Unit>
    > ScientificValue<Quantity, Unit>.minus(
    value: Decimal,
    factory: (Decimal, Unit) -> Value
) = factory(decimalValue - value, unit)

infix operator fun <
    Quantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>
    > ScientificValue<Quantity, LeftUnit>.minus(right: ScientificValue<Quantity, RightUnit>) = unit.minus(this, right, ::DefaultScientificValue)

fun <
    Quantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, TargetUnit>
    > TargetUnit.minus(
    left: ScientificValue<Quantity, LeftUnit>,
    right: ScientificValue<Quantity, RightUnit>,
    factory: (Decimal, TargetUnit) -> Value
) = bySubtracting(left, right, factory)

infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>
    > ScientificValue<Quantity, Unit>.times(factor: Number) = this * factor.toDecimal()

infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>
    > Number.times(unit: ScientificValue<Quantity, Unit>) = toDecimal() * unit

infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>
    > ScientificValue<Quantity, Unit>.times(factor: Decimal) = times(factor, ::DefaultScientificValue)

infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>
    > Decimal.times(unit: ScientificValue<Quantity, Unit>) = unit.times(this, ::DefaultScientificValue)

fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, Unit>
    > ScientificValue<Quantity, Unit>.times(
    factor: Decimal,
    factory: (Decimal, Unit) -> Value
) = factory(decimalValue * factor, unit)

infix operator fun <
    Quantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>
    > ScientificValue<Quantity, LeftUnit>.times(right: ScientificValue<Quantity, RightUnit>) = unit.times(this, right, ::DefaultScientificValue)

fun <
    Quantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, TargetUnit>
    > TargetUnit.times(
    left: ScientificValue<Quantity, LeftUnit>,
    right: ScientificValue<Quantity, RightUnit>,
    factory: (Decimal, TargetUnit) -> Value
) = byMultiplying(left, right, factory)

infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>
    > ScientificValue<Quantity, Unit>.div(factor: Number) = this / factor.toDecimal()

infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>
    > ScientificValue<Quantity, Unit>.div(factor: Decimal) = div(factor, ::DefaultScientificValue)

fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, Unit>
    > Decimal.div(
    factor: ScientificValue<Quantity, Unit>,
    factory: (Decimal, Unit) -> Value
) = if (factor.value != 0.0) {
    factory(this / factor.decimalValue, factor.unit)
} else {
    throw ScientificConverterException("Dividing by 0.0")
}

fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, Unit>
    > ScientificValue<Quantity, Unit>.div(
    factor: Decimal,
    factory: (Decimal, Unit) -> Value
) = if (factor.toDouble() == 0.0) {
    factory(decimalValue / factor, unit)
} else {
    throw ScientificConverterException("Dividing by 0.0")
}

infix operator fun <
    Quantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>
    > ScientificValue<Quantity, LeftUnit>.div(right: ScientificValue<Quantity, RightUnit>) = unit.div(this, right, ::DefaultScientificValue)

fun <
    Quantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, TargetUnit>
    > TargetUnit.div(
    left: ScientificValue<Quantity, LeftUnit>,
    right: ScientificValue<Quantity, RightUnit>,
    factory: (Decimal, TargetUnit) -> Value
) = byDividing(left, right, factory)

internal fun <
    TargetQuantity : PhysicalQuantity,
    TargetUnit : ScientificUnit<TargetQuantity>,
    Value : ScientificValue<TargetQuantity, TargetUnit>,
    LeftQuantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<LeftQuantity>,
    RightQuantity : PhysicalQuantity,
    RightUnit : ScientificUnit<RightQuantity>
    > TargetUnit.byAdding(
    left: ScientificValue<LeftQuantity, LeftUnit>,
    right: ScientificValue<RightQuantity, RightUnit>,
    factory: (Decimal, TargetUnit) -> Value
) = fromSIUnit(left.unit.toSIUnit(left.decimalValue) + right.unit.toSIUnit(right.decimalValue))(this, factory)

internal fun <
    TargetQuantity : PhysicalQuantity,
    TargetUnit : ScientificUnit<TargetQuantity>,
    Value : ScientificValue<TargetQuantity, TargetUnit>,
    LeftQuantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<LeftQuantity>,
    RightQuantity : PhysicalQuantity,
    RightUnit : ScientificUnit<RightQuantity>
    > TargetUnit.bySubtracting(
    left: ScientificValue<LeftQuantity, LeftUnit>,
    right: ScientificValue<RightQuantity, RightUnit>,
    factory: (Decimal, TargetUnit) -> Value
) = fromSIUnit(left.unit.toSIUnit(left.decimalValue) - right.unit.toSIUnit(right.decimalValue))(this, factory)

internal fun <
    TargetQuantity : PhysicalQuantity,
    Unit : ScientificUnit<TargetQuantity>,
    Value : ScientificValue<TargetQuantity, Unit>,
    NominatorQuantity : PhysicalQuantity,
    NominatorUnit : ScientificUnit<NominatorQuantity>,
    DividerQuantity : PhysicalQuantity,
    DividerUnit : ScientificUnit<DividerQuantity>
    > Unit.byDividing(
    nominator: ScientificValue<NominatorQuantity, NominatorUnit>,
    divider: ScientificValue<DividerQuantity, DividerUnit>,
    factory: (Decimal, Unit) -> Value
) = if (divider.value != 0.0) {
    fromSIUnit(nominator.unit.toSIUnit(nominator.decimalValue) / divider.unit.toSIUnit(divider.decimalValue))(this, factory)
} else {
    throw ScientificConverterException("Dividing by 0.0")
}

internal fun <
    TargetQuantity : PhysicalQuantity,
    TargetUnit : ScientificUnit<TargetQuantity>,
    Value : ScientificValue<TargetQuantity, TargetUnit>,
    LeftQuantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<LeftQuantity>,
    RightQuantity : PhysicalQuantity,
    RightUnit : ScientificUnit<RightQuantity>
    > TargetUnit.byMultiplying(
    left: ScientificValue<LeftQuantity, LeftUnit>,
    right: ScientificValue<RightQuantity, RightUnit>,
    factory: (Decimal, TargetUnit) -> Value
) = fromSIUnit(left.unit.toSIUnit(left.decimalValue) * right.unit.toSIUnit(right.decimalValue))(this, factory)

internal fun <
    InverseQuantity : PhysicalQuantity,
    InverseUnit : ScientificUnit<InverseQuantity>,
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, Unit>
    > Unit.byInverting(
    inverse: ScientificValue<InverseQuantity, InverseUnit>,
    factory: (Decimal, Unit) -> Value
) = if (inverse.value != 0.0) {
    fromSIUnit(1.0.toDecimal() / inverse.unit.toSIUnit(inverse.decimalValue))(this, factory)
} else {
    throw ScientificConverterException("Inverting by 0.0")
}
