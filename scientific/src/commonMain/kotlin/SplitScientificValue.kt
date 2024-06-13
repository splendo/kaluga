/*
 Copyright 2024 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.base.utils.minus
import com.splendo.kaluga.base.utils.plus
import com.splendo.kaluga.base.utils.round
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.unit.AbstractScientificUnit
import com.splendo.kaluga.scientific.unit.MeasurementSystem
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.SystemScientificUnit
import com.splendo.kaluga.scientific.unit.convert

/**
 * Splits a [ScientificValue] of [ValueUnit] into a [LeftValue] and [RightValue] so that left and right together are equal to the original value.
 * Splitting happens by rounding the [ValueUnit] down to [scale] and returning it and the remainder converted to [RightUnit].
 *
 * Splitting can only be done between [SystemScientificUnit] using the same [MeasurementSystem].
 * To account for rounding errors, a [roundingThreshold] can be set. If the non-rounded [LeftValue] is within this threshold, it will be rounded up.
 *
 * @param System the [MeasurementSystem] of the units to split
 * @param Quantity the [PhysicalQuantity] of the units to split
 * @param ValueUnit the [ScientificUnit] of the [ScientificValue] to split and use as the left (rounded) unit for [LeftValue]
 * @param LeftValue the [ScientificValue] that will be returned as the left (rounded) value
 * @param RightUnit the [SystemScientificUnit] to use as the right unit for [RightValue]. It is recommended that `1` [RightUnit] < `1` [ValueUnit]
 * @param RightValue the [ScientificValue] that will be returned as the right value
 * @param rightUnit the [RightUnit] to split into.
 * @param scale the number of decimals to which to round [LeftValue]
 * @param roundingThreshold the threshold at which [LeftValue] will be rounded up instead of down to the nearest value at [scale]
 * @param leftFactory a factory method for creating [LeftValue]
 * @param rightFactory a factory method for creating [RightValue]
 * @return a [Pair] of [LeftValue] and [RightValue] where [LeftValue] is rounded down to [scale] and [RightValue] contains the remainder.
 */
fun <
    System : MeasurementSystem,
    Quantity : PhysicalQuantity,
    ValueUnit : SystemScientificUnit<System, Quantity>,
    LeftValue : ScientificValue<Quantity, ValueUnit>,
    RightUnit : SystemScientificUnit<System, Quantity>,
    RightValue : ScientificValue<Quantity, RightUnit>,
    > ScientificValue<Quantity, ValueUnit>.split(
    rightUnit: RightUnit,
    scale: UInt = 0U,
    roundingThreshold: Decimal = 0.0000001.toDecimal(),
    leftFactory: (Decimal, ValueUnit) -> LeftValue,
    rightFactory: (Decimal, RightUnit) -> RightValue,
): Pair<LeftValue, RightValue> = split(unit, rightUnit, scale, roundingThreshold, leftFactory, rightFactory)

/**
 * Splits a [ScientificValue] of [ValueUnit] into a [DefaultScientificValue] of [ValueUnit] and [DefaultScientificValue] of [RightUnit] so that left and right together are equal to the original value.
 * Splitting happens by rounding the [ValueUnit] down to [scale] and returning it and the remainder converted to [RightUnit].
 *
 * Splitting can only be done between [SystemScientificUnit] using the same [MeasurementSystem].
 * To account for rounding errors, a [roundingThreshold] can be set. If the non-rounded value in [ValueUnit] is within this threshold, it will be rounded up.
 *
 * @param System the [MeasurementSystem] of the units to split
 * @param Quantity the [PhysicalQuantity] of the units to split
 * @param ValueUnit the [ScientificUnit] of the [ScientificValue] to split and to use as the left (rounded) unit the left [DefaultScientificValue]
 * @param RightUnit the [SystemScientificUnit] to use as the right unit for the right [DefaultScientificValue]. It is recommended that `1` [RightUnit] < `1` [ValueUnit]
 * @param rightUnit the [RightUnit] to split into.
 * @param scale the number of decimals to which to round the value of [ValueUnit]
 * @param roundingThreshold the threshold at which the value of [ValueUnit] will be rounded up instead of down to the nearest value at [scale]
 * @return a [Pair] of [DefaultScientificValue] and [RightUnit] where in [ValueUnit] and [RightUnit] respectively where the value in [ValueUnit] is rounded down to [scale] and the value in [RightUnit] contains the remainder.
 */
fun <
    System : MeasurementSystem,
    Quantity : PhysicalQuantity,
    ValueUnit,
    RightUnit,
    > ScientificValue<Quantity, ValueUnit>.split(
    rightUnit: RightUnit,
    scale: UInt = 0U,
    roundingThreshold: Decimal = 0.0000001.toDecimal(),
) where
    ValueUnit : AbstractScientificUnit<Quantity>,
    ValueUnit : SystemScientificUnit<System, Quantity>,
    RightUnit : AbstractScientificUnit<Quantity>,
    RightUnit : SystemScientificUnit<System, Quantity> =
    split(unit, rightUnit, scale, roundingThreshold, ::DefaultScientificValue, ::DefaultScientificValue)

/**
 * Splits a [ScientificValue] of [ValueUnit] into a [LeftValue] and [RightValue] so that left and right together are equal to the original value.
 * Splitting happens by converting the [ValueUnit] to [LeftUnit] and then rounding down to [scale] and returning it and the remainder converted to [RightUnit].
 *
 * Splitting can only be done between [SystemScientificUnit] using the same [MeasurementSystem].
 * To account for rounding errors, a [roundingThreshold] can be set. If the non-rounded [LeftValue] is within this threshold, it will be rounded up.
 *
 * @param System the [MeasurementSystem] of the units to split
 * @param Quantity the [PhysicalQuantity] of the units to split
 * @param ValueUnit the [ScientificUnit] of the [ScientificValue] to split
 * @param LeftUnit the [SystemScientificUnit] to use as the left (rounded) unit for [LeftValue]
 * @param LeftValue the [ScientificValue] that will be returned as the left (rounded) value
 * @param RightUnit the [SystemScientificUnit] to use as the right unit for [RightValue]. It is recommended that `1` [RightUnit] < `1` [LeftUnit]
 * @param RightValue the [ScientificValue] that will be returned as the right value
 * @param leftUnit the [LeftUnit] to split (and round)
 * @param rightUnit the [RightUnit] to split into.
 * @param scale the number of decimals to which to round [LeftValue]
 * @param roundingThreshold the threshold at which [LeftValue] will be rounded up instead of down to the nearest value at [scale]
 * @param leftFactory a factory method for creating [LeftValue]
 * @param rightFactory a factory method for creating [RightValue]
 * @return a [Pair] of [LeftValue] and [RightValue] where [LeftValue] is rounded down to [scale] and [RightValue] contains the remainder.
 */
fun <
    System : MeasurementSystem,
    Quantity : PhysicalQuantity,
    ValueUnit : ScientificUnit<Quantity>,
    LeftUnit : SystemScientificUnit<System, Quantity>,
    LeftValue : ScientificValue<Quantity, LeftUnit>,
    RightUnit : SystemScientificUnit<System, Quantity>,
    RightValue : ScientificValue<Quantity, RightUnit>,
    > ScientificValue<Quantity, ValueUnit>.split(
    leftUnit: LeftUnit,
    rightUnit: RightUnit,
    scale: UInt = 0U,
    roundingThreshold: Decimal = 0.0000001.toDecimal(),
    leftFactory: (Decimal, LeftUnit) -> LeftValue,
    rightFactory: (Decimal, RightUnit) -> RightValue,
): Pair<LeftValue, RightValue> {
    val valueInLeft = convert(leftUnit, leftFactory)
    val leftValueRoundedValue = (valueInLeft.decimalValue + roundingThreshold).round(scale.toInt(), RoundingMode.RoundDown)
    val remainderInLeft = if (leftValueRoundedValue < valueInLeft.decimalValue) {
        valueInLeft.decimalValue - leftValueRoundedValue
    } else {
        0.0.toDecimal()
    }
    val rightValue = leftUnit.convert(remainderInLeft, rightUnit)
    return leftFactory(leftValueRoundedValue, leftUnit) to rightFactory(rightValue, rightUnit)
}

/**
 * Splits a [ScientificValue] of [ValueUnit] into a [DefaultScientificValue] of [LeftUnit] and [DefaultScientificValue] of [RightUnit] so that left and right together are equal to the original value.
 * Splitting happens by converting the [ValueUnit] to [LeftUnit] and then rounding down to [scale] and returning it and the remainder converted to [RightUnit].
 *
 * Splitting can only be done between [SystemScientificUnit] using the same [MeasurementSystem].
 * To account for rounding errors, a [roundingThreshold] can be set. If the non-rounded value in [LeftUnit] is within this threshold, it will be rounded up.
 *
 * @param System the [MeasurementSystem] of the units to split
 * @param Quantity the [PhysicalQuantity] of the units to split
 * @param ValueUnit the [ScientificUnit] of the [ScientificValue] to split
 * @param LeftUnit the [SystemScientificUnit] to use as the left (rounded) unit the left [DefaultScientificValue]
 * @param RightUnit the [SystemScientificUnit] to use as the right unit for the right [DefaultScientificValue]. It is recommended that `1` [RightUnit] < `1` [LeftUnit]
 * @param leftUnit the [LeftUnit] to split (and round)
 * @param rightUnit the [RightUnit] to split into.
 * @param scale the number of decimals to which to round the value of [LeftUnit]
 * @param roundingThreshold the threshold at which the value of [LeftUnit] will be rounded up instead of down to the nearest value at [scale]
 * @return a [Pair] of [DefaultScientificValue] and [RightUnit] where in [LeftUnit] and [RightUnit] respectively where the value in [LeftUnit] is rounded down to [scale] and the value in [RightUnit] contains the remainder.
 */
fun <
    System : MeasurementSystem,
    Quantity : PhysicalQuantity,
    ValueUnit : ScientificUnit<Quantity>,
    LeftUnit,
    RightUnit,
    > ScientificValue<Quantity, ValueUnit>.split(
    leftUnit: LeftUnit,
    rightUnit: RightUnit,
    scale: UInt = 0U,
    roundingThreshold: Decimal = 0.0000001.toDecimal(),
) where
    LeftUnit : AbstractScientificUnit<Quantity>,
    LeftUnit : SystemScientificUnit<System, Quantity>,
    RightUnit : AbstractScientificUnit<Quantity>,
    RightUnit : SystemScientificUnit<System, Quantity> =
    split(leftUnit, rightUnit, scale, roundingThreshold, ::DefaultScientificValue, ::DefaultScientificValue)

/**
 * Breaks up a [ScientificValue] of [Quantity] into its components in [UnitOne], [UnitTwo] and executes the given [action] with these components.
 * A value is broken up into its components by converting the value to to [UnitOne] and splitting into [UnitTwo] using [scale].
 *
 * Splitting can only be done between [AbstractScientificUnit] using the same [MeasurementSystem].
 * To account for rounding errors, a [roundingThreshold] can be set. If the non-rounded value in [UnitOne] is within this threshold, it will be rounded up.
 * @param System the [MeasurementSystem] of the units to split
 * @param Quantity the [PhysicalQuantity] of the units to split
 * @param Unit the [ScientificUnit] of the [ScientificValue] to split
 * @param UnitOne the [AbstractScientificUnit] to use as the first (rounded) component.
 * @param UnitTwo the [AbstractScientificUnit] to use as the second component.
 * @param Result the type of result to be returned.
 * @param one the [UnitOne] to use.
 * @param two the [UnitTwo] to use.
 * @param scale the number of decimals to which to round the [DefaultScientificValue] in [UnitOne]
 * @param roundingThreshold the threshold at which [DefaultScientificValue] will be rounded up instead of down to the nearest value at [scale]
 * @param action the action that converts components of [DefaultScientificValue] in [UnitOne] and [UnitTwo] into [Result]
 * @return the [Result] returned by [action]
 * @see [split]
 */
fun <
    System : MeasurementSystem,
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    UnitOne,
    UnitTwo,
    Result,
    > ScientificValue<Quantity, Unit>.toComponents(
    one: UnitOne,
    two: UnitTwo,
    scale: UInt = 0U,
    roundingThreshold: Decimal = 0.0000001.toDecimal(),
    action: (DefaultScientificValue<Quantity, UnitOne>, DefaultScientificValue<Quantity, UnitTwo>) -> Result,
): Result where
    UnitOne : AbstractScientificUnit<Quantity>,
    UnitOne : SystemScientificUnit<System, Quantity>,
    UnitTwo : AbstractScientificUnit<Quantity>,
    UnitTwo : SystemScientificUnit<System, Quantity> {
    val (valueOne, valueTwo) = this.split(one, two, scale, roundingThreshold)
    return action(valueOne, valueTwo)
}

/**
 * Breaks up a [ScientificValue] of [Quantity] into its components in [UnitOne], [UnitTwo], [UnitThree] and executes the given [action] with these components.
 * A value is broken up into its components by converting the value to to [UnitOne] and splitting into [UnitTwo] using [scale], then splitting the remainder into [UnitThree].
 *
 * Splitting can only be done between [AbstractScientificUnit] using the same [MeasurementSystem].
 * To account for rounding errors, a [roundingThreshold] can be set. If the non-rounded value in any unit (except for [UnitThree]) is within this threshold, it will be rounded up.
 * @param System the [MeasurementSystem] of the units to split
 * @param Quantity the [PhysicalQuantity] of the units to split
 * @param Unit the [ScientificUnit] of the [ScientificValue] to split
 * @param UnitOne the [AbstractScientificUnit] to use as the first (rounded) component.
 * @param UnitTwo the [AbstractScientificUnit] to use as the second (rounded) component.
 * @param UnitThree the [AbstractScientificUnit] to use as the third component.
 * @param Result the type of result to be returned.
 * @param one the [UnitOne] to use.
 * @param two the [UnitTwo] to use.
 * @param three the [UnitThree] to use.
 * @param scale the number of decimals to which to round all [DefaultScientificValue] except for the value in [UnitThree]
 * @param roundingThreshold the threshold at which [DefaultScientificValue] will be rounded up instead of down to the nearest value at [scale]
 * @param action the action that converts components of [DefaultScientificValue] in [UnitOne], [UnitTwo], and [UnitThree] into [Result]
 * @return the [Result] returned by [action]
 * @see [split]
 */
fun <
    System : MeasurementSystem,
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    UnitOne,
    UnitTwo,
    UnitThree,
    Result,
    > ScientificValue<Quantity, Unit>.toComponents(
    one: UnitOne,
    two: UnitTwo,
    three: UnitThree,
    scale: UInt = 0U,
    roundingThreshold: Decimal = 0.0000001.toDecimal(),
    action: (DefaultScientificValue<Quantity, UnitOne>, DefaultScientificValue<Quantity, UnitTwo>, DefaultScientificValue<Quantity, UnitThree>) -> Result,
): Result where
    UnitOne : AbstractScientificUnit<Quantity>,
    UnitOne : SystemScientificUnit<System, Quantity>,
    UnitTwo : AbstractScientificUnit<Quantity>,
    UnitTwo : SystemScientificUnit<System, Quantity>,
    UnitThree : AbstractScientificUnit<Quantity>,
    UnitThree : SystemScientificUnit<System, Quantity> {
    val (valueOne, oneRemainder) = this.split(one, two, scale, roundingThreshold)
    val (valueTwo, valueThree) = oneRemainder.split(three, scale, roundingThreshold)
    return action(valueOne, valueTwo, valueThree)
}

/**
 * Breaks up a [ScientificValue] of [Quantity] into its components in [UnitOne], [UnitTwo], [UnitThree], [UnitFour] and executes the given [action] with these components.
 * A value is broken up into its components by converting the value to to [UnitOne] and splitting into [UnitTwo] using [scale], then splitting the remainder into [UnitThree] and so on.
 *
 * Splitting can only be done between [AbstractScientificUnit] using the same [MeasurementSystem].
 * To account for rounding errors, a [roundingThreshold] can be set. If the non-rounded value in any unit (except for [UnitFour]) is within this threshold, it will be rounded up.
 * @param System the [MeasurementSystem] of the units to split
 * @param Quantity the [PhysicalQuantity] of the units to split
 * @param Unit the [ScientificUnit] of the [ScientificValue] to split
 * @param UnitOne the [AbstractScientificUnit] to use as the first (rounded) component.
 * @param UnitTwo the [AbstractScientificUnit] to use as the second (rounded) component.
 * @param UnitThree the [AbstractScientificUnit] to use as the third (rounded) component.
 * @param UnitFour the [AbstractScientificUnit] to use as the fourth component.
 * @param Result the type of result to be returned.
 * @param one the [UnitOne] to use.
 * @param two the [UnitTwo] to use.
 * @param three the [UnitThree] to use.
 * @param four the [UnitFour] to use.
 * @param scale the number of decimals to which to round all [DefaultScientificValue] except for the value in [UnitFour]
 * @param roundingThreshold the threshold at which [DefaultScientificValue] will be rounded up instead of down to the nearest value at [scale]
 * @param action the action that converts components of [DefaultScientificValue] in [UnitOne], [UnitTwo], [UnitThree], and [UnitFour] into [Result]
 * @return the [Result] returned by [action]
 * @see [split]
 */
fun <
    System : MeasurementSystem,
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    UnitOne,
    UnitTwo,
    UnitThree,
    UnitFour,
    Result,
    > ScientificValue<Quantity, Unit>.toComponents(
    one: UnitOne,
    two: UnitTwo,
    three: UnitThree,
    four: UnitFour,
    scale: UInt = 0U,
    roundingThreshold: Decimal = 0.0000001.toDecimal(),
    action: (
        DefaultScientificValue<Quantity, UnitOne>,
        DefaultScientificValue<Quantity, UnitTwo>,
        DefaultScientificValue<Quantity, UnitThree>,
        DefaultScientificValue<Quantity, UnitFour>,
    ) -> Result,
): Result where
    UnitOne : AbstractScientificUnit<Quantity>,
    UnitOne : SystemScientificUnit<System, Quantity>,
    UnitTwo : AbstractScientificUnit<Quantity>,
    UnitTwo : SystemScientificUnit<System, Quantity>,
    UnitThree : AbstractScientificUnit<Quantity>,
    UnitThree : SystemScientificUnit<System, Quantity>,
    UnitFour : AbstractScientificUnit<Quantity>,
    UnitFour : SystemScientificUnit<System, Quantity> {
    val (valueOne, oneRemainder) = this.split(one, two, scale, roundingThreshold)
    val (valueTwo, twoRemainder) = oneRemainder.split(three, scale, roundingThreshold)
    val (valueThree, valueFour) = twoRemainder.split(four, scale, roundingThreshold)
    return action(valueOne, valueTwo, valueThree, valueFour)
}

/**
 * Breaks up a [ScientificValue] of [Quantity] into its components in [UnitOne], [UnitTwo], [UnitThree], [UnitFour], [UnitFive] and executes the given [action] with these components.
 * A value is broken up into its components by converting the value to to [UnitOne] and splitting into [UnitTwo] using [scale], then splitting the remainder into [UnitThree] and so on.
 *
 * Splitting can only be done between [AbstractScientificUnit] using the same [MeasurementSystem].
 * To account for rounding errors, a [roundingThreshold] can be set. If the non-rounded value in any unit (except for [UnitFive]) is within this threshold, it will be rounded up.
 * @param System the [MeasurementSystem] of the units to split
 * @param Quantity the [PhysicalQuantity] of the units to split
 * @param Unit the [ScientificUnit] of the [ScientificValue] to split
 * @param UnitOne the [AbstractScientificUnit] to use as the first (rounded) component.
 * @param UnitTwo the [AbstractScientificUnit] to use as the second (rounded) component.
 * @param UnitThree the [AbstractScientificUnit] to use as the third (rounded) component.
 * @param UnitFour the [AbstractScientificUnit] to use as the fourth (rounded) component.
 * @param UnitFive the [AbstractScientificUnit] to use as the fifth component.
 * @param Result the type of result to be returned.
 * @param one the [UnitOne] to use.
 * @param two the [UnitTwo] to use.
 * @param three the [UnitThree] to use.
 * @param four the [UnitFour] to use.
 * @param five the [UnitFive] to use.
 * @param scale the number of decimals to which to round all [DefaultScientificValue] except for the value in [UnitFive]
 * @param roundingThreshold the threshold at which [DefaultScientificValue] will be rounded up instead of down to the nearest value at [scale]
 * @param action the action that converts components of [DefaultScientificValue] in [UnitOne], [UnitTwo], [UnitThree], [UnitFour], and [UnitFive] into [Result]
 * @return the [Result] returned by [action]
 * @see [split]
 */
fun <
    System : MeasurementSystem,
    Quantity : PhysicalQuantity,
    Unit : ScientificUnit<Quantity>,
    UnitOne,
    UnitTwo,
    UnitThree,
    UnitFour,
    UnitFive,
    Result,
    > ScientificValue<Quantity, Unit>.toComponents(
    one: UnitOne,
    two: UnitTwo,
    three: UnitThree,
    four: UnitFour,
    five: UnitFive,
    scale: UInt = 0U,
    roundingThreshold: Decimal = 0.0000001.toDecimal(),
    action: (
        DefaultScientificValue<Quantity, UnitOne>,
        DefaultScientificValue<Quantity, UnitTwo>,
        DefaultScientificValue<Quantity, UnitThree>,
        DefaultScientificValue<Quantity, UnitFour>,
        DefaultScientificValue<Quantity, UnitFive>,
    ) -> Result,
): Result where
    UnitOne : AbstractScientificUnit<Quantity>,
    UnitOne : SystemScientificUnit<System, Quantity>,
    UnitTwo : AbstractScientificUnit<Quantity>,
    UnitTwo : SystemScientificUnit<System, Quantity>,
    UnitThree : AbstractScientificUnit<Quantity>,
    UnitThree : SystemScientificUnit<System, Quantity>,
    UnitFour : AbstractScientificUnit<Quantity>,
    UnitFour : SystemScientificUnit<System, Quantity>,
    UnitFive : AbstractScientificUnit<Quantity>,
    UnitFive : SystemScientificUnit<System, Quantity> {
    val (valueOne, oneRemainder) = this.split(one, two, scale, roundingThreshold)
    val (valueTwo, twoRemainder) = oneRemainder.split(three, scale, roundingThreshold)
    val (valueThree, threeRemainder) = twoRemainder.split(four, scale, roundingThreshold)
    val (valueFour, valueFive) = threeRemainder.split(five, scale, roundingThreshold)
    return action(valueOne, valueTwo, valueThree, valueFour, valueFive)
}