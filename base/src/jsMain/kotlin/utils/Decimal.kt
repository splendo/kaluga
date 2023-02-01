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

import com.splendo.kaluga.base.utils.RoundingMode.RoundDown
import com.splendo.kaluga.base.utils.RoundingMode.RoundHalfEven
import com.splendo.kaluga.base.utils.RoundingMode.RoundUp
import kotlin.math.pow

// 34 digits of the IEEE 754R Decimal128 format
private const val DECIMAL_128_SIGNIFICANT_DIGITS = 34
// Max decimal digits + 1 for intermediate division
private const val DIV_DECIMAL_128_SIGNIFICANT_DIGITS = DECIMAL_128_SIGNIFICANT_DIGITS + 1

// Default is half-even
private const val ROUNDING_MODE = "half-even"
private const val CEIL = "ceil"
private const val FLOOR = "floor"

private data class Rounding(
    val maximumFractionDigits: Int? = null,
    val roundingMode: String = ROUNDING_MODE,
    val maximumSignificantDigits: Int = DECIMAL_128_SIGNIFICANT_DIGITS
)

/**
 * Immutable, arbitrary-precision signed decimal numbers.
 * @param bd the internal [BigDecimal] corresponding to this decimal
 */
actual data class Decimal(val bd: BigDecimal) : Comparable<Decimal> {
    override fun compareTo(other: Decimal): Int = BigDecimal.compare(bd, other.bd)
    override fun equals(other: Any?): Boolean = (other as? Decimal)?.let { BigDecimal.equal(bd, it.bd) } ?: false
    override fun hashCode(): Int {
        return bd.hashCode()
    }
}

private val ZERO = BigDecimal.BigDecimal(0)

private fun round(
    a: BigDecimal,
    roundingMode: RoundingMode,
    maximumFractionDigits: Int? = null
): Decimal {
    val mode = if (BigDecimal.lessThan(a, ZERO)) {
        when (roundingMode) {
            RoundDown -> CEIL
            RoundHalfEven -> ROUNDING_MODE
            RoundUp -> FLOOR
        }
    } else {
        when (roundingMode) {
            RoundDown -> FLOOR
            RoundHalfEven -> ROUNDING_MODE
            RoundUp -> CEIL
        }
    }
    return Decimal(BigDecimal.round(a, Rounding(maximumFractionDigits, mode)))
}

/**
 * Adds two [Decimal] together.
 * @param value the [Decimal] to add
 * @return the [Decimal] that is the total of the two provided decimals.
 */
actual operator fun Decimal.plus(value: Decimal): Decimal = Decimal(BigDecimal.add(bd, value.bd))

/**
 * Adds two [Decimal] together scaled to a given precision.
 * @param value the [Decimal] to add
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @return the [Decimal] that is the total of the two provided decimals.
 */
actual fun Decimal.plus(
    value: Decimal,
    scale: Int
): Decimal = Decimal(BigDecimal.add(bd, value.bd, Rounding(scale)))

/**
 * Adds two [Decimal] together scaled to a given precision.
 * @param value the [Decimal] to add
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @param roundingMode The [RoundingMode] to apply when scaling.
 * @return the [Decimal] that is the total of the two provided decimals.
 */
actual fun Decimal.plus(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode
): Decimal = round(BigDecimal.add(bd, value.bd), roundingMode, scale)

/**
 * Subtracts two [Decimal].
 * @param value the [Decimal] to subtract
 * @return the [Decimal] that is the subtraction of the two provided decimals.
 */
actual operator fun Decimal.minus(value: Decimal): Decimal = Decimal(BigDecimal.subtract(bd, value.bd))

/**
 * Subtracts two [Decimal] to a given precision.
 * @param value the [Decimal] to subtract
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @return the [Decimal] that is the subtraction of the two provided decimals.
 */
actual fun Decimal.minus(
    value: Decimal,
    scale: Int
): Decimal = Decimal(BigDecimal.subtract(bd, value.bd, Rounding(scale)))

/**
 * Subtracts two [Decimal] to a given precision.
 * @param value the [Decimal] to subtract
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @param roundingMode The [RoundingMode] to apply when scaling.
 * @return the [Decimal] that is the subtraction of the two provided decimals.
 */
actual fun Decimal.minus(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode
): Decimal = round(BigDecimal.subtract(bd, value.bd), roundingMode, scale)

/**
 * Divides two [Decimal].
 * @param value the [Decimal] to divide
 * @return the [Decimal] that is the division of the two provided decimals.
 */
actual operator fun Decimal.div(
    value: Decimal
): Decimal = if (!BigDecimal.equal(value.bd, ZERO)) Decimal(BigDecimal.divide(bd, value.bd, Rounding())) else throw DecimalException("Divide by zero")

/**
 * Divides two [Decimal] to a given precision.
 * @param value the [Decimal] to divide
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @return the [Decimal] that is the division of the two provided decimals.
 */
actual fun Decimal.div(
    value: Decimal,
    scale: Int
): Decimal = if (!BigDecimal.equal(value.bd, ZERO)) Decimal(BigDecimal.divide(bd, value.bd, Rounding(scale))) else throw DecimalException("Divide by zero")

/**
 * Divides two [Decimal] to a given precision.
 * @param value the [Decimal] to divide
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @param roundingMode The [RoundingMode] to apply when scaling.
 * @return the [Decimal] that is the division of the two provided decimals.
 */
actual fun Decimal.div(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode
): Decimal = if (!BigDecimal.equal(value.bd, ZERO))
    round(BigDecimal.divide(bd, value.bd, Rounding(scale + 1, ROUNDING_MODE, DIV_DECIMAL_128_SIGNIFICANT_DIGITS)), roundingMode, scale)
else
    throw DecimalException("Divide by zero")

/**
 * Multiplies two [Decimal].
 * @param value the [Decimal] to multiply
 * @return the [Decimal] that is the multiplication of the two provided decimals.
 */
actual operator fun Decimal.times(
    value: Decimal
): Decimal = Decimal(BigDecimal.multiply(bd, value.bd, Rounding()))

/**
 * Multiplies two [Decimal] to a given precision.
 * @param value the [Decimal] to multiply
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @return the [Decimal] that is the multiplication of the two provided decimals.
 */
actual fun Decimal.times(
    value: Decimal,
    scale: Int
): Decimal = Decimal(BigDecimal.multiply(bd, value.bd, Rounding(scale)))

/**
 * Multiplies two [Decimal] to a given precision.
 * @param value the [Decimal] to multiply
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @param roundingMode The [RoundingMode] to apply when scaling.
 * @return the [Decimal] that is the multiplication of the two provided decimals.
 */
actual fun Decimal.times(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode
): Decimal = round(BigDecimal.multiply(bd, value.bd), roundingMode, scale)

/**
 * Raises two [Decimal].
 * @param n the [Decimal] to raise to
 * @return the [Decimal] that is the exponent of the two provided decimals.
 */
actual infix fun Decimal.pow(n: Int): Decimal = Decimal(BigDecimal.BigDecimal(toDouble().pow(n)))

/**
 * Raises two [Decimal] to a given precision.
 * @param n the [Decimal] to raise to
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @return the [Decimal] that is the exponent of the two provided decimals.
 */
actual fun Decimal.pow(n: Int, scale: Int): Decimal = Decimal(BigDecimal.round((this pow n).bd, Rounding(scale)))

/**
 * Raises two [Decimal] to a given precision.
 * @param n the [Decimal] to raise to
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @param roundingMode The [RoundingMode] to apply when scaling.
 * @return the [Decimal] that is the exponent of the two provided decimals.
 */
actual fun Decimal.pow(
    n: Int,
    scale: Int,
    roundingMode: RoundingMode
): Decimal = this pow n

/**
 * Rounds a [Decimal] to a [scale].
 * @param scale The number of digits the rounded value should have after its decimal point.
 * @param roundingMode The [RoundingMode] to apply when scaling.
 * @return A [Decimal] rounded to [scale] digits after the decimal point.
 */
actual fun Decimal.round(
    scale: Int,
    roundingMode: RoundingMode
): Decimal = round(bd, roundingMode, scale)

/**
 * Converts a [Number] to a [Decimal]
 */
actual fun Number.toDecimal(): Decimal = this.toString().toDecimal()

/**
 * Converts a String to a [Decimal]
 */
actual fun String.toDecimal(): Decimal = Decimal(BigDecimal.BigDecimal(this))

/**
 * Gets the double value of a [Decimal]
 */
actual fun Decimal.toDouble(): Double = bd.toString().toDouble()

/**
 * Gets the integer value of a [Decimal]
 */
actual fun Decimal.toInt(): Int = bd.toFixed(0).toInt()

/**
 * Gets the string value of a [Decimal]
 */
actual fun Decimal.toString(): String = bd.toString()
