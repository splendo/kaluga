/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.unit.AbstractScientificUnit
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.UndefinedScientificUnit

internal fun <
    TargetQuantity : PhysicalQuantity,
    TargetUnit : ScientificUnit<TargetQuantity>,
    Value : ScientificValue<TargetQuantity, TargetUnit>,
    LeftQuantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<LeftQuantity>,
    RightQuantity : PhysicalQuantity,
    RightUnit : ScientificUnit<RightQuantity>,
    > TargetUnit.byMultiplying(
    left: ScientificValue<LeftQuantity, LeftUnit>,
    right: ScientificValue<RightQuantity, RightUnit>,
    factory: (Decimal, TargetUnit) -> Value,
) = fromSIUnit(left.unit.toSIUnit(left.decimalValue) * right.unit.toSIUnit(right.decimalValue))(this, factory)

/**
 * Creates a [DefaultScientificValue] equal to the [ScientificValue.value] multiplied by [value]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [AbstractScientificUnit] of the [ScientificValue]
 * @param value the amount to multiply the value with
 * @return the [DefaultScientificValue] where the [ScientificValue.value] is multiplied by [value]
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : AbstractScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.times(value: Number) = this * value.toDecimal()

/**
 * Creates a [DefaultScientificValue] equal to a [ScientificValue.value] multiplied by this [Number]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [AbstractScientificUnit] of the [ScientificValue]
 * @param value the [ScientificValue] to multiply the value with
 * @return a [DefaultScientificValue] where the [ScientificValue.value] of [value] is multiplied by this number
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : AbstractScientificUnit<Quantity>,
    > Number.times(value: ScientificValue<Quantity, Unit>) = toDecimal() * value

/**
 * Creates a [DefaultScientificValue] equal to the [ScientificValue.value] multiplied by [value]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [AbstractScientificUnit] of the [ScientificValue]
 * @param value the [Decimal] to multiply the value with
 * @return the [DefaultScientificValue] where the [ScientificValue.value] is multiplied by [value]
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : AbstractScientificUnit<Quantity>,
    > ScientificValue<Quantity, Unit>.times(value: Decimal) = times(value, ::DefaultScientificValue)

/**
 * Creates a [DefaultScientificValue] equal to a [ScientificValue.value] multiplied by this [Decimal]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [AbstractScientificUnit] of the [ScientificValue]
 * @param value the [ScientificValue] to multiply the value with
 * @return a [DefaultScientificValue] where the [ScientificValue.value] of [value] is multiplied by this number
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    Unit : AbstractScientificUnit<Quantity>,
    > Decimal.times(value: ScientificValue<Quantity, Unit>) = times(value, ::DefaultScientificValue)

/**
 * Creates a [DefaultUndefinedScientificValue] equal to the [UndefinedScientificValue.value] multiplied by [value]
 * @param Quantity the type of [UndefinedQuantityType] of the [UndefinedScientificValue]
 * @param Unit the type of [UndefinedScientificUnit] of the [UndefinedScientificValue]
 * @param value the [Decimal] to multiply the value with
 * @return the [DefaultUndefinedScientificValue] where the [UndefinedScientificValue.value] is multiplied by [value]
 */
infix operator fun <
    Quantity : UndefinedQuantityType,
    Unit : UndefinedScientificUnit<Quantity>,
    > UndefinedScientificValue<Quantity, Unit>.times(value: Decimal) = times(value, ::DefaultUndefinedScientificValue)

/**
 * Creates a [DefaultUndefinedScientificValue] equal to a [UndefinedScientificValue.value] multiplied by this [Decimal]
 * @param Quantity the type of [UndefinedQuantityType] of the [UndefinedScientificValue]
 * @param Unit the type of [UndefinedScientificUnit] of the [UndefinedScientificValue]
 * @param value the [UndefinedScientificValue] to multiply the value with
 * @return a [DefaultUndefinedScientificValue] where the [UndefinedScientificValue.value] of [value] is multiplied by this number
 */
infix operator fun <
    Quantity : UndefinedQuantityType,
    Unit : UndefinedScientificUnit<Quantity>,
    > Decimal.times(value: UndefinedScientificValue<Quantity, Unit>) = times(value, ::DefaultUndefinedScientificValue)

/**
 * Creates a [Value] equal to the [ScientificValue.value] multiplied by [value]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [ScientificUnit] of the [ScientificValue]
 * @param Value the type of [ScientificValue] to store the result in
 * @param value the [Decimal] amount to multiply the value with
 * @param factory method for creating [Value] from a [Decimal] and [Unit]
 * @return a [Value] where the [ScientificValue.value] is multiplied by [value]
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, Unit>,
    > ScientificValue<Quantity, Unit>.times(
    value: Decimal,
    factory: (Decimal, Unit) -> Value,
) = factory(decimalValue * value, unit)

/**
 * Creates a [Value] equal to a [ScientificValue.value] multiplied by this [Decimal]
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param Unit the type of [ScientificUnit] of the [ScientificValue]
 * @param Value the type of [ScientificValue] to store the result in
 * @param value the [ScientificValue] to multiply the value with
 * @param factory method for creating [Value] from a [Decimal] and [Unit]
 * @return a [Value] where the [ScientificValue.value] is multiplied by this [Decimal]
 */
fun <
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, Unit>,
    > Decimal.times(
    value: ScientificValue<Quantity, Unit>,
    factory: (Decimal, Unit) -> Value,
) = factory(this * value.decimalValue, value.unit)

/**
 * Multiplies the [ScientificValue.value] of two [ScientificValue] into a [DefaultScientificValue] with [LeftUnit] as its unit
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param LeftUnit the type of [AbstractScientificUnit] of the [ScientificValue] being multiplied
 * @param RightUnit the type of [ScientificUnit] of the [ScientificValue] multiplying
 * @param right the [ScientificValue] of [RightUnit] to multiply
 * @return a [DefaultScientificValue] in [LeftUnit] where [right] is multiplied by this value
 */
infix operator fun <
    Quantity : PhysicalQuantity,
    LeftUnit : AbstractScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>,
    > ScientificValue<Quantity, LeftUnit>.times(right: ScientificValue<Quantity, RightUnit>) = unit.times(this, right, ::DefaultScientificValue)

/**
 * Multiplies the [ScientificValue.value] of two [ScientificValue] into a [Value] with [TargetUnit] as its unit
 * @param Quantity the type of [PhysicalQuantity] of the [ScientificValue]
 * @param LeftUnit the type of [ScientificUnit] of the [ScientificValue] being multiplied
 * @param RightUnit the type of [ScientificUnit] of the [ScientificValue] multiplying
 * @param TargetUnit the type of [ScientificUnit] the [Value] should be in
 * @param Value the type of [ScientificValue] to return
 * @param left the [ScientificValue] of [LeftUnit] to multiply
 * @param right the [ScientificValue] of [RightUnit] to multiply with
 * @param factory a method for creating [Value] from a [Decimal] and this unit
 * @return a [Value] in [TargetUnit] where [left] and [right] are multiplied with each other
 */
fun <
    Quantity : PhysicalQuantity,
    LeftUnit : ScientificUnit<Quantity>,
    RightUnit : ScientificUnit<Quantity>,
    TargetUnit : ScientificUnit<Quantity>,
    Value : ScientificValue<Quantity, TargetUnit>,
    > TargetUnit.times(
    left: ScientificValue<Quantity, LeftUnit>,
    right: ScientificValue<Quantity, RightUnit>,
    factory: (Decimal, TargetUnit) -> Value,
) = byMultiplying(left, right, factory)
