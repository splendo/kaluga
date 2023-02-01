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

@file:JvmName("DecimalJVM")
@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package com.splendo.kaluga.base.utils

import com.splendo.kaluga.base.utils.RoundingMode.RoundDown
import com.splendo.kaluga.base.utils.RoundingMode.RoundHalfEven
import com.splendo.kaluga.base.utils.RoundingMode.RoundUp
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode as NativeRoundingMode

actual typealias Decimal = BigDecimal

/**
 * Adds two [Decimal] together.
 * @param value the [Decimal] to add
 * @return the [Decimal] that is the total of the two provided decimals.
 */
actual operator fun Decimal.plus(value: Decimal) = this.add(value)

/**
 * Adds two [Decimal] together scaled to a given precision.
 * @param value the [Decimal] to add
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @return the [Decimal] that is the total of the two provided decimals.
 */
actual fun Decimal.plus(value: Decimal, scale: Int) =
    this.add(value).setScale(scale, NativeRoundingMode.HALF_EVEN)

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
) = this.add(value).setScale(scale, roundingMode.java)

/**
 * Subtracts two [Decimal].
 * @param value the [Decimal] to subtract
 * @return the [Decimal] that is the subtraction of the two provided decimals.
 */
actual operator fun Decimal.minus(value: Decimal) = this.subtract(value)

/**
 * Subtracts two [Decimal] to a given precision.
 * @param value the [Decimal] to subtract
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @return the [Decimal] that is the subtraction of the two provided decimals.
 */
actual fun Decimal.minus(value: Decimal, scale: Int) =
    this.subtract(value).setScale(scale, NativeRoundingMode.HALF_EVEN)

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
) = this.subtract(value).setScale(scale, roundingMode.java)

/**
 * Divides two [Decimal].
 * @param value the [Decimal] to divide
 * @return the [Decimal] that is the division of the two provided decimals.
 */
actual operator fun Decimal.div(value: Decimal) = if ((value.compareTo(Decimal.ZERO)) != 0)
    this.divide(value, MathContext.DECIMAL128)
else throw DecimalException("Divide by zero")

/**
 * Divides two [Decimal] to a given precision.
 * @param value the [Decimal] to divide
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @return the [Decimal] that is the division of the two provided decimals.
 */
actual fun Decimal.div(value: Decimal, scale: Int) = if ((value.compareTo(Decimal.ZERO)) != 0)
    this.divide(value, MathContext.DECIMAL128).setScale(scale, NativeRoundingMode.HALF_EVEN)
else throw DecimalException("Divide by zero")

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
) = if ((value.compareTo(Decimal.ZERO)) != 0)
    this.divide(
        value,
        MathContext(
            MathContext.DECIMAL128.precision,
            roundingMode.java
        )
    ).setScale(scale, roundingMode.java)
else throw DecimalException("Divide by zero")

/**
 * Multiplies two [Decimal].
 * @param value the [Decimal] to multiply
 * @return the [Decimal] that is the multiplication of the two provided decimals.
 */
actual operator fun Decimal.times(value: Decimal) =
    this.multiply(value, MathContext.DECIMAL128)

/**
 * Multiplies two [Decimal] to a given precision.
 * @param value the [Decimal] to multiply
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @return the [Decimal] that is the multiplication of the two provided decimals.
 */
actual fun Decimal.times(value: Decimal, scale: Int) =
    this.multiply(value, MathContext.DECIMAL128).setScale(scale, NativeRoundingMode.HALF_EVEN)

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
) = this.multiply(
    value,
    MathContext(
        MathContext.DECIMAL128.precision,
        roundingMode.java
    )
).setScale(scale, roundingMode.java)

/**
 * Rounds a [Decimal] to a [scale].
 * @param scale The number of digits the rounded value should have after its decimal point.
 * @param roundingMode The [RoundingMode] to apply when scaling.
 * @return A [Decimal] rounded to [scale] digits after the decimal point.
 */
actual fun Decimal.round(scale: Int, roundingMode: RoundingMode) =
    this.setScale(
        scale,
        roundingMode.java
    )

/**
 * Raises two [Decimal].
 * @param n the [Decimal] to raise to
 * @return the [Decimal] that is the exponent of the two provided decimals.
 */
actual infix fun Decimal.pow(n: Int): Decimal = this.pow(n, MathContext.DECIMAL128)

/**
 * Raises two [Decimal] to a given precision.
 * @param n the [Decimal] to raise to
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @return the [Decimal] that is the exponent of the two provided decimals.
 */
actual fun Decimal.pow(n: Int, scale: Int): Decimal = this.pow(n, MathContext.DECIMAL128).setScale(scale, NativeRoundingMode.HALF_EVEN)

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
): Decimal = this.pow(
    n,
    MathContext(
        MathContext.DECIMAL128.precision,
        roundingMode.java
    )
).setScale(scale, roundingMode.java)

/**
 * Converts a [Number] to a [Decimal]
 */
actual fun Number.toDecimal() = BigDecimal(toString())

/**
 * Converts a String to a [Decimal]
 */
actual fun String.toDecimal() = BigDecimal(this)

/**
 * Gets the double value of a [Decimal]
 */
actual fun Decimal.toDouble() = this.toDouble()

/**
 * Gets the integer value of a [Decimal]
 */
actual fun Decimal.toInt() = this.toInt()

/**
 * Gets the string value of a [Decimal]
 */
actual fun Decimal.toString() = this.stripTrailingZeros().toString()

private val RoundingMode.java
    get() = when (this) {
        RoundDown -> NativeRoundingMode.DOWN
        RoundHalfEven -> NativeRoundingMode.HALF_EVEN
        RoundUp -> NativeRoundingMode.UP
    }
