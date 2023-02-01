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

@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package com.splendo.kaluga.base.utils

class DecimalException(message: String?) : ArithmeticException(message)

/**
 * Immutable, arbitrary-precision signed decimal numbers.
 */
expect class Decimal : Comparable<Decimal>

/**
 * Adds two [Decimal] together.
 * @param value the [Decimal] to add
 * @return the [Decimal] that is the total of the two provided decimals.
 */
expect operator fun Decimal.plus(value: Decimal): Decimal

/**
 * Adds two [Decimal] together scaled to a given precision.
 * @param value the [Decimal] to add
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @return the [Decimal] that is the total of the two provided decimals.
 */
expect fun Decimal.plus(value: Decimal, scale: Int): Decimal

/**
 * Adds two [Decimal] together scaled to a given precision.
 * @param value the [Decimal] to add
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @param roundingMode The [RoundingMode] to apply when scaling.
 * @return the [Decimal] that is the total of the two provided decimals.
 */
expect fun Decimal.plus(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven
): Decimal

/**
 * Subtracts two [Decimal].
 * @param value the [Decimal] to subtract
 * @return the [Decimal] that is the subtraction of the two provided decimals.
 */
expect operator fun Decimal.minus(value: Decimal): Decimal

/**
 * Subtracts two [Decimal] to a given precision.
 * @param value the [Decimal] to subtract
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @return the [Decimal] that is the subtraction of the two provided decimals.
 */
expect fun Decimal.minus(value: Decimal, scale: Int): Decimal

/**
 * Subtracts two [Decimal] to a given precision.
 * @param value the [Decimal] to subtract
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @param roundingMode The [RoundingMode] to apply when scaling.
 * @return the [Decimal] that is the subtraction of the two provided decimals.
 */
expect fun Decimal.minus(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven
): Decimal

/**
 * Divides two [Decimal].
 * @param value the [Decimal] to divide
 * @return the [Decimal] that is the division of the two provided decimals.
 */
expect operator fun Decimal.div(value: Decimal): Decimal

/**
 * Divides two [Decimal] to a given precision.
 * @param value the [Decimal] to divide
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @return the [Decimal] that is the division of the two provided decimals.
 */
expect fun Decimal.div(value: Decimal, scale: Int): Decimal

/**
 * Divides two [Decimal] to a given precision.
 * @param value the [Decimal] to divide
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @param roundingMode The [RoundingMode] to apply when scaling.
 * @return the [Decimal] that is the division of the two provided decimals.
 */
expect fun Decimal.div(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven
): Decimal

/**
 * Multiplies two [Decimal].
 * @param value the [Decimal] to multiply
 * @return the [Decimal] that is the multiplication of the two provided decimals.
 */
expect operator fun Decimal.times(value: Decimal): Decimal

/**
 * Multiplies two [Decimal] to a given precision.
 * @param value the [Decimal] to multiply
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @return the [Decimal] that is the multiplication of the two provided decimals.
 */
expect fun Decimal.times(value: Decimal, scale: Int): Decimal

/**
 * Multiplies two [Decimal] to a given precision.
 * @param value the [Decimal] to multiply
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @param roundingMode The [RoundingMode] to apply when scaling.
 * @return the [Decimal] that is the multiplication of the two provided decimals.
 */
expect fun Decimal.times(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven
): Decimal

/**
 * Raises two [Decimal].
 * @param n the [Decimal] to raise to
 * @return the [Decimal] that is the exponent of the two provided decimals.
 */
expect infix fun Decimal.pow(n: Int): Decimal

/**
 * Raises two [Decimal] to a given precision.
 * @param n the [Decimal] to raise to
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @return the [Decimal] that is the exponent of the two provided decimals.
 */
expect fun Decimal.pow(n: Int, scale: Int): Decimal

/**
 * Raises two [Decimal] to a given precision.
 * @param n the [Decimal] to raise to
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @param roundingMode The [RoundingMode] to apply when scaling.
 * @return the [Decimal] that is the exponent of the two provided decimals.
 */
expect fun Decimal.pow(
    n: Int,
    scale: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven
): Decimal

/**
 * Rounding Mode for rounding a [Decimal]
 */
sealed class RoundingMode {
    /**
     * Rounds values down
     */
    object RoundDown : RoundingMode()

    /**
     * Rounds values to the closest possible return value; when halfway between two possibilities, return the possibility whose last digit is even.
     */
    object RoundHalfEven : RoundingMode()

    /**
     * Rounds values up
     */
    object RoundUp : RoundingMode()
}

/**
 * Rounds a [Decimal] to a [scale].
 * @param scale The number of digits the rounded value should have after its decimal point.
 * @param roundingMode The [RoundingMode] to apply when scaling.
 * @return A [Decimal] rounded to [scale] digits after the decimal point.
 */
expect fun Decimal.round(scale: Int, roundingMode: RoundingMode = RoundingMode.RoundHalfEven): Decimal

/**
 * Converts a [Number] to a [Decimal]
 */
expect fun Number.toDecimal(): Decimal

/**
 * Converts a String to a [Decimal]
 */
expect fun String.toDecimal(): Decimal

/**
 * Converts a [ByteArray] to a list of [Decimal]
 */
fun ByteArray.toDecimalList(): List<Decimal> = map { it.toDecimal() }

/**
 * Converts a [DoubleArray] to a list of [Decimal]
 */
fun DoubleArray.toDecimalList(): List<Decimal> = map { it.toDecimal() }

/**
 * Converts a [FloatArray] to a list of [Decimal]
 */
fun FloatArray.toDecimalList(): List<Decimal> = map { it.toDecimal() }

/**
 * Converts an [IntArray] to a list of [Decimal]
 */
fun IntArray.toDecimalList(): List<Decimal> = map { it.toDecimal() }

/**
 * Converts a [ShortArray] to a list of [Decimal]
 */
fun ShortArray.toDecimalList(): List<Decimal> = map { it.toDecimal() }

/**
 * Converts a [LongArray] to a list of [Decimal]
 */
fun LongArray.toDecimalList(): List<Decimal> = map { it.toDecimal() }

/**
 * Converts a [Collection] of [NumberType] to a list of [Decimal]
 * @param NumberType the type of [Number] in the collection.
 */
fun <NumberType : Number> Collection<NumberType>.toDecimalList(): List<Decimal> = map { it.toDecimal() }

/**
 * Gets the double value of a [Decimal]
 */
expect fun Decimal.toDouble(): Double

/**
 * Gets the string value of a [Decimal]
 */
expect fun Decimal.toString(): String

/**
 * Gets the integer value of a [Decimal]
 */
expect fun Decimal.toInt(): Int

/**
 * Converts a collection of [Decimal] to a [DoubleArray]
 */
fun Collection<Decimal>.toDoubleArray(): DoubleArray = map { it.toDouble() }.toDoubleArray()

/**
 * Converts a collection of [Decimal] to a list of String
 */
fun Collection<Decimal>.toStringList(): List<String> = map { it.toString() }

/**
 * Converts a collection of [Decimal] to an [IntArray]
 */
fun Collection<Decimal>.toIntArray(): IntArray = map { it.toInt() }.toIntArray()
