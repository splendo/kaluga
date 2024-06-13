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
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.minus
import com.splendo.kaluga.base.utils.plus
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.base.utils.toDouble
import com.splendo.kaluga.scientific.unit.AbstractScientificUnit
import com.splendo.kaluga.scientific.unit.ScientificUnit
import kotlinx.serialization.Serializable

/**
 * A Value in a given [ScientificUnit]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [ScientificUnit] this value represents
 */
interface ScientificValue<Quantity : PhysicalQuantity, Unit : ScientificUnit<Quantity>> : Comparable<ScientificValue<Quantity, *>>, com.splendo.kaluga.base.utils.Serializable {

    /**
     * The value component
     */
    val value: Number

    /**
     * The [Unit] component
     */
    val unit: Unit

    override fun compareTo(other: ScientificValue<Quantity, *>): Int = unit.toSIUnit(decimalValue).compareTo(other.unit.toSIUnit(other.decimalValue))

    /**
     * A [Decimal] representation of [value]
     */
    val decimalValue: Decimal get() = value.toDecimal()
}

/**
 * A class implementation of [ScientificValue]
 * @param Quantity the type of [PhysicalQuantity] of the unit
 * @param Unit the type of [AbstractScientificUnit] this value represents
 * @param value the [Decimal] component
 * @param unit the [Unit] component
 */
@Serializable
data class DefaultScientificValue<Quantity : PhysicalQuantity, Unit : AbstractScientificUnit<Quantity>>(
    override val value: Double,
    override val unit: Unit,
) : ScientificValue<Quantity, Unit> {

    /**
     * Constructor
     * @param value the [Decimal] component
     * @param unit the [Unit] component
     */
    constructor(value: Decimal, unit: Unit) : this(value.toDouble(), unit)
}

// Calculation

/**
 * Creates a [DefaultScientificValue] equal to the [ScientificValue.value] increased by [value]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [AbstractScientificUnit] of the [ScientificValue]
 * @param value the amount to add to the value
 * @return the [DefaultScientificValue] where the [ScientificValue.value] is increased by [value]
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : AbstractScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.plus(value: Number) =
    this + value.toDecimal()

/**
 * Creates a [DefaultScientificValue] equal to a [ScientificValue.value] increased by this [Number]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [AbstractScientificUnit] of the [ScientificValue]
 * @param value the [ScientificValue] to add the value to
 * @return a [DefaultScientificValue] where the [ScientificValue.value] of [value] is increased by this number
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : AbstractScientificUnit<Quantity>,
    > Number.plus(value: ScientificValue<Quantity, Unit>) = toDecimal() + value

/**
 * Creates a [DefaultScientificValue] equal to the [ScientificValue.value] increased by [value]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [AbstractScientificUnit] of the [ScientificValue]
 * @param value the [Decimal] amount to add to the value
 * @return a [DefaultScientificValue] where the [ScientificValue.value] is increased by [value]
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : AbstractScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.plus(value: Decimal) =
    plus(value, ::DefaultScientificValue)

/**
 * Creates a [DefaultScientificValue] equal to a [ScientificValue.value] increased by this [Decimal]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [AbstractScientificUnit] of the [ScientificValue]
 * @param value the [ScientificValue] to add the value to
 * @return a [DefaultScientificValue] where the [ScientificValue.value] of [value] is increased by this [Decimal]
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : AbstractScientificUnit<Quantity>,
    > Decimal.plus(value: ScientificValue<Quantity, Unit>) =
    plus(value, ::DefaultScientificValue)

/**
 * Creates a [Value] equal to the [ScientificValue.value] increased by [value]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [ScientificUnit] of the [ScientificValue]
 * @param Value the type of [ScientificValue] to store the result in
 * @param value the [Decimal] amount to add to the value
 * @param factory method for creating [Value] from a [Decimal] and [Unit]
 * @return a [Value] where the [ScientificValue.value] is increased by [value]
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, Unit>,
    > ScientificValue<Quantity, Unit>.plus(
    value: Decimal,
    factory: (Decimal, Unit) -> Value,
) = factory(decimalValue + value, unit)

/**
 * Creates a [Value] equal to a [ScientificValue.value] increased by this [Decimal]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [ScientificUnit] of the [ScientificValue]
 * @param Value the type of [ScientificValue] to store the result in
 * @param value the [ScientificValue] to add the value to
 * @param factory method for creating [Value] from a [Decimal] and [Unit]
 * @return a [Value] where the [ScientificValue.value] is increased by this [Decimal]
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, Unit>,
    > Decimal.plus(
    value: ScientificValue<Quantity, Unit>,
    factory: (Decimal, Unit) -> Value,
) = factory(this + value.decimalValue, value.unit)

/**
 * Adds the [ScientificValue.value] of two [ScientificValue] into a [DefaultScientificValue] with [LeftUnit] as its unit
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param LeftUnit the type of [AbstractScientificUnit] of the [ScientificValue] being added to
 * @param RightUnit the type of [ScientificUnit] of the [ScientificValue] being added
 * @param right the [ScientificValue] of [RightUnit] to add
 * @return a [DefaultScientificValue] in [LeftUnit] where [right] is added to this value
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    LeftUnit : AbstractScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>,
    > ScientificValue<Quantity, LeftUnit>.plus(
    right: ScientificValue<Quantity, RightUnit>,
) = unit.plus(this, right, ::DefaultScientificValue)

/**
 * Adds the [ScientificValue.value] of two [ScientificValue] into a [Value] with [TargetUnit] as its unit
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param LeftUnit the type of [ScientificUnit] of the [ScientificValue] being added to
 * @param RightUnit the type of [ScientificUnit] of the [ScientificValue] being added
 * @param TargetUnit the type of [ScientificUnit] the [Value] should be in
 * @param Value the type of [ScientificValue] to return
 * @param left the [ScientificValue] of [LeftUnit] to add to
 * @param right the [ScientificValue] of [RightUnit] to add
 * @param factory a method for creating [Value] from a [Decimal] and this unit
 * @return a [Value] in [TargetUnit] where [left] and [right] are added to each other
 */
fun <
    Quantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, TargetUnit>,
    > TargetUnit.plus(
    left: ScientificValue<Quantity, LeftUnit>,
    right: ScientificValue<Quantity, RightUnit>,
    factory: (Decimal, TargetUnit) -> Value,
) = byAdding(left, right, factory)

/**
 * Creates a [DefaultScientificValue] equal to the [ScientificValue.value] decreased by [value]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [AbstractScientificUnit] of the [ScientificValue]
 * @param value the amount to subtract from the value
 * @return a [DefaultScientificValue] where the [ScientificValue.value] is decreased by [value]
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : AbstractScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.minus(value: Number) =
    this - value.toDecimal()

/**
 * Creates a [DefaultScientificValue] equal to a [ScientificValue.value] decreased from this [Number]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [AbstractScientificUnit] of the [ScientificValue]
 * @param value the [ScientificValue] whose value should be subtracted from this [Number]
 * @return a [DefaultScientificValue] where the [ScientificValue.value] of [value] is subtracted from this number
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : AbstractScientificUnit<Quantity>,
    > Number.minus(value: ScientificValue<Quantity, Unit>) = toDecimal() - value

/**
 * Creates a [DefaultScientificValue] equal to the [ScientificValue.value] decreased by [value]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [AbstractScientificUnit] of the [ScientificValue]
 * @param value the [Decimal] to subtract from the value
 * @return a [DefaultScientificValue] where the [ScientificValue.value] is decreased by [value]
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : AbstractScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.minus(value: Decimal) =
    minus(value, ::DefaultScientificValue)

/**
 * Creates a [DefaultScientificValue] equal to a [ScientificValue.value] decreased from this [Decimal]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [AbstractScientificUnit] of the [ScientificValue]
 * @param value the [ScientificValue] whose value should be subtracted from this [Decimal]
 * @return a [DefaultScientificValue] where the [ScientificValue.value] of [value] is subtracted from this number
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : AbstractScientificUnit<Quantity>,
    > Decimal.minus(value: ScientificValue<Quantity, Unit>) =
    minus(value, ::DefaultScientificValue)

/**
 * Creates a [Value] equal to the [ScientificValue.value] decreased by [value]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [ScientificUnit] of the [ScientificValue]
 * @param Value the type of [ScientificValue] to store the result in
 * @param value the [Decimal] to subtract from the value
 * @param factory method for creating [Value] from a [Decimal] and [Unit]
 * @return a [Value] where the [ScientificValue.value] is subtracted by [value]
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, Unit>,
    > ScientificValue<Quantity, Unit>.minus(
    value: Decimal,
    factory: (Decimal, Unit) -> Value,
) = factory(decimalValue - value, unit)

/**
 * Creates a [Value] equal to a [ScientificValue.value] decreased from this [Decimal]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [ScientificUnit] of the [ScientificValue]
 * @param Value the type of [ScientificValue] to store the result in
 * @param value the [ScientificValue] whose value should be subtracted from this [Decimal]
 * @param factory method for creating [Value] from a [Decimal] and [Unit]
 * @return a [Value] where the [ScientificValue.value] of [value] is subtracted from this [Decimal]
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, Unit>,
    > Decimal.minus(
    value: ScientificValue<Quantity, Unit>,
    factory: (Decimal, Unit) -> Value,
) = factory(this - value.decimalValue, value.unit)

/**
 * Subtracts the [ScientificValue.value] of two [ScientificValue] into a [DefaultScientificValue] with [LeftUnit] as its unit
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param LeftUnit the type of [AbstractScientificUnit] of the [ScientificValue] being subtracted from
 * @param RightUnit the type of [ScientificUnit] of the [ScientificValue] being subtracted
 * @param right the [ScientificValue] of [RightUnit] to subtract
 * @return a [DefaultScientificValue] in [LeftUnit] where [right] is subtracted from this value
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    LeftUnit : AbstractScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>,
    > ScientificValue<Quantity, LeftUnit>.minus(
    right: ScientificValue<Quantity, RightUnit>,
) = unit.minus(this, right, ::DefaultScientificValue)

/**
 * Subtracts the [ScientificValue.value] of two [ScientificValue] into a [Value] with [TargetUnit] as its unit
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param LeftUnit the type of [ScientificUnit] of the [ScientificValue] being subtracted from
 * @param RightUnit the type of [ScientificUnit] of the [ScientificValue] being subtracted
 * @param TargetUnit the type of [ScientificUnit] the [Value] should be in
 * @param Value the type of [ScientificValue] to return
 * @param left the [ScientificValue] of [LeftUnit] to subtract from
 * @param right the [ScientificValue] of [RightUnit] to subtract
 * @param factory a method for creating [Value] from a [Decimal] and this unit
 * @return a [Value] in [TargetUnit] where [left] is subtracted by [right]
 */
fun <
    Quantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, TargetUnit>,
    > TargetUnit.minus(
    left: ScientificValue<Quantity, LeftUnit>,
    right: ScientificValue<Quantity, RightUnit>,
    factory: (Decimal, TargetUnit) -> Value,
) = bySubtracting(left, right, factory)

/**
 * Creates a [DefaultScientificValue] equal to the [ScientificValue.value] divided by [value]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [AbstractScientificUnit] of the [ScientificValue]
 * @param value the amount to divide from the value
 * @return a [DefaultScientificValue] where the [ScientificValue.value] is divided by [value]
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : AbstractScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.div(value: Number) =
    this / value.toDecimal()

/**
 * Creates a [DefaultScientificValue] equal to a [ScientificValue.value] divided from this [Number]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [AbstractScientificUnit] of the [ScientificValue]
 * @param value the [ScientificValue] whose value should be divided from this [Number]
 * @return a [DefaultScientificValue] where the [ScientificValue.value] of [unit] is divided from this number
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : AbstractScientificUnit<Quantity>,
    > Number.div(value: ScientificValue<Quantity, Unit>) = toDecimal() / value

/**
 * Creates a [DefaultScientificValue] equal to the [ScientificValue.value] divided by [value]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [AbstractScientificUnit] of the [ScientificValue]
 * @param value the [Decimal] to divided from the value
 * @return a [DefaultScientificValue] where the [ScientificValue.value] is divided by [value]
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : AbstractScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.div(value: Decimal) =
    div(value, ::DefaultScientificValue)

/**
 * Creates a [DefaultScientificValue] equal to a [ScientificValue.value] divided from this [Decimal]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [AbstractScientificUnit] of the [ScientificValue]
 * @param value the [ScientificValue] whose value should be divided from this [Decimal]
 * @return a [DefaultScientificValue] where the [ScientificValue.value] of [value] is divided from this number
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : AbstractScientificUnit<Quantity>,
    > Decimal.div(value: ScientificValue<Quantity, Unit>) =
    div(value, ::DefaultScientificValue)

/**
 * Creates a [Value] equal to the [ScientificValue.value] divided by [value]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [ScientificUnit] of the [ScientificValue]
 * @param Value the type of [ScientificValue] to store the result in
 * @param value the [Decimal] to divide from the value
 * @param factory method for creating [Value] from a [Decimal] and [Unit]
 * @return a [Value] where the [ScientificValue.value] is divided by [value]
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, Unit>,
    > ScientificValue<Quantity, Unit>.div(
    value: Decimal,
    factory: (Decimal, Unit) -> Value,
) = factory(decimalValue / value, unit)

/**
 * Creates a [Value] equal to a [ScientificValue.value] divided from this [Decimal]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [ScientificUnit] of the [ScientificValue]
 * @param Value the type of [ScientificValue] to store the result in
 * @param value the [ScientificValue] whose value should be divide from this [Decimal]
 * @param factory method for creating [Value] from a [Decimal] and [Unit]
 * @return a [Value] where the [ScientificValue.value] of [value] is divide from this [Decimal]
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, Unit>,
    > Decimal.div(
    value: ScientificValue<Quantity, Unit>,
    factory: (Decimal, Unit) -> Value,
) = factory(this / value.decimalValue, value.unit)

/**
 * Divides the [ScientificValue.value] of two [ScientificValue] into a [DefaultScientificValue] with [LeftUnit] as its unit
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param LeftUnit the type of [AbstractScientificUnit] of the [ScientificValue] being divided from
 * @param RightUnit the type of [ScientificUnit] of the [ScientificValue] being divided
 * @param right the [ScientificValue] of [RightUnit] to divide with
 * @return a [DefaultScientificValue] in [LeftUnit] where [right] is divided from this value
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    LeftUnit : AbstractScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>,
    > ScientificValue<Quantity, LeftUnit>.div(
    right: ScientificValue<Quantity, RightUnit>,
) = unit.div(this, right, ::DefaultScientificValue)

/**
 * Divides the [ScientificValue.value] of two [ScientificValue] into a [Value] with [TargetUnit] as its unit
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param LeftUnit the type of [ScientificUnit] of the [ScientificValue] being divided from
 * @param RightUnit the type of [ScientificUnit] of the [ScientificValue] being divided
 * @param TargetUnit the type of [ScientificUnit] the [Value] should be in
 * @param Value the type of [ScientificValue] to return
 * @param left the [ScientificValue] of [LeftUnit] to divided from
 * @param right the [ScientificValue] of [RightUnit] to divided
 * @param factory a method for creating [Value] from a [Decimal] and this unit
 * @return a [Value] in [TargetUnit] where [left] is divided by [right]
 */
fun <
    Quantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, TargetUnit>,
    > TargetUnit.div(
    left: ScientificValue<Quantity, LeftUnit>,
    right: ScientificValue<Quantity, RightUnit>,
    factory: (Decimal, TargetUnit) -> Value,
) = byDividing(left, right, factory)

internal fun <
    TargetQuantity : PhysicalQuantity,
    TargetUnit : ScientificUnit<TargetQuantity>,
    Value : ScientificValue<TargetQuantity, TargetUnit>,
    LeftQuantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<LeftQuantity>,
    RightQuantity : PhysicalQuantity,
    RightUnit : ScientificUnit<RightQuantity>,
    > TargetUnit.byAdding(
    left: ScientificValue<LeftQuantity, LeftUnit>,
    right: ScientificValue<RightQuantity, RightUnit>,
    factory: (Decimal, TargetUnit) -> Value,
) = fromSIUnit(left.unit.toSIUnit(left.decimalValue) + right.unit.toSIUnit(right.decimalValue))(this, factory)

internal fun <
    TargetQuantity : PhysicalQuantity,
    TargetUnit : ScientificUnit<TargetQuantity>,
    Value : ScientificValue<TargetQuantity, TargetUnit>,
    LeftQuantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<LeftQuantity>,
    RightQuantity : PhysicalQuantity,
    RightUnit : ScientificUnit<RightQuantity>,
    > TargetUnit.bySubtracting(
    left: ScientificValue<LeftQuantity, LeftUnit>,
    right: ScientificValue<RightQuantity, RightUnit>,
    factory: (Decimal, TargetUnit) -> Value,
) = fromSIUnit(left.unit.toSIUnit(left.decimalValue) - right.unit.toSIUnit(right.decimalValue))(this, factory)

internal fun <
    TargetQuantity : PhysicalQuantity,
    Unit : ScientificUnit<TargetQuantity>,
    Value : ScientificValue<TargetQuantity, Unit>,
    NominatorQuantity : PhysicalQuantity,
    NominatorUnit : ScientificUnit<NominatorQuantity>,
    DividerQuantity : PhysicalQuantity,
    DividerUnit : ScientificUnit<DividerQuantity>,
    > Unit.byDividing(
    nominator: ScientificValue<NominatorQuantity, NominatorUnit>,
    divider: ScientificValue<DividerQuantity, DividerUnit>,
    factory: (Decimal, Unit) -> Value,
) = fromSIUnit(nominator.unit.toSIUnit(nominator.decimalValue) / divider.unit.toSIUnit(divider.decimalValue))(this, factory)

internal fun <
    InverseQuantity : PhysicalQuantity,
    InverseUnit : ScientificUnit<InverseQuantity>,
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, Unit>,
    > Unit.byInverting(
    inverse: ScientificValue<InverseQuantity, InverseUnit>,
    factory: (Decimal, Unit) -> Value,
) = fromSIUnit(1.0.toDecimal() / inverse.unit.toSIUnit(inverse.decimalValue))(this, factory)

/**
 * Returns the value of a [ScientificValue] with [PhysicalQuantity.Dimensionless] as a fraction. I.e.
 * ```
val percent = 12(Percent)
print(percent.value) // 12
print(percent.decimalFraction) // 0.12
 * ```
 * @param Unit the type of [ScientificUnit] with a [PhysicalQuantity.Dimensionless]
 * @return the dimensionless [Decimal] value as a fraction
 */
val <Unit : ScientificUnit<PhysicalQuantity.Dimensionless>> ScientificValue<PhysicalQuantity.Dimensionless, Unit>.decimalFraction: Decimal get() = unit.toSIUnit(value.toDecimal())
