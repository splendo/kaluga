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

import kotlin.math.pow

/**
 * Immutable, arbitrary-precision signed decimal numbers.
 */
sealed class Decimal : Comparable<Decimal> {

    /**
     * [Decimal] representing Not A Number
     */
    data object NaN : Decimal()

    /**
     * [Decimal] representing Positive Infinity
     */
    data object PositiveInfinity : Decimal()

    /**
     * [Decimal] representing Negative Infinity
     */
    data object NegativeInfinity : Decimal()

    /**
     * [Decimal] representing a finite number
     * @param finiteDecimal the [FiniteDecimal] describing the finite number
     */
    data class Finite(internal val finiteDecimal: FiniteDecimal) : Decimal() {
        override fun equals(other: Any?): Boolean = (other as? Finite)?.let {
            finiteDecimal == it.finiteDecimal
        } ?: false

        override fun hashCode(): Int = finiteDecimal.hashCode()

        override fun toString(): String = finiteDecimal.toString()
    }

    override fun compareTo(other: Decimal): Int = if (this is Finite && other is Finite) {
        finiteDecimal.compareTo(other.finiteDecimal)
    } else {
        toDouble().compareTo(other.toDouble())
    }
}

/**
 * Platform specific representation of a finite immutable, arbitrary-precision signed decimal number
 */
expect class FiniteDecimal : Comparable<FiniteDecimal>

/**
 * Adds two [Decimal] together.
 * @param value the [Decimal] to add
 * @return the [Decimal] that is the total of the two provided decimals.
 */
operator fun Decimal.plus(value: Decimal): Decimal = if (this is Decimal.Finite && value is Decimal.Finite) {
    Decimal.Finite(finiteDecimal + value.finiteDecimal)
} else {
    calculateToDecimal(value, Double::plus)
}

/**
 * Adds two [Decimal] together scaled to a given precision.
 * @param value the [Decimal] to add
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @return the [Decimal] that is the total of the two provided decimals.
 */
fun Decimal.plus(value: Decimal, scale: Int): Decimal = if (this is Decimal.Finite && value is Decimal.Finite) {
    Decimal.Finite(finiteDecimal.plus(value.finiteDecimal, scale))
} else {
    calculateToDecimal(value, Double::plus)
}

/**
 * Adds two [Decimal] together scaled to a given precision.
 * @param value the [Decimal] to add
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @param roundingMode The [RoundingMode] to apply when scaling.
 * @return the [Decimal] that is the total of the two provided decimals.
 */
fun Decimal.plus(value: Decimal, scale: Int, roundingMode: RoundingMode = RoundingMode.RoundHalfEven): Decimal = if (this is Decimal.Finite && value is Decimal.Finite) {
    Decimal.Finite(finiteDecimal.plus(value.finiteDecimal, scale, roundingMode))
} else {
    calculateToDecimal(value, Double::plus)
}

internal expect operator fun FiniteDecimal.plus(value: FiniteDecimal): FiniteDecimal
internal expect fun FiniteDecimal.plus(value: FiniteDecimal, scale: Int): FiniteDecimal
internal expect fun FiniteDecimal.plus(value: FiniteDecimal, scale: Int, roundingMode: RoundingMode = RoundingMode.RoundHalfEven): FiniteDecimal

/**
 * Subtracts two [Decimal].
 * @param value the [Decimal] to subtract
 * @return the [Decimal] that is the subtraction of the two provided decimals.
 */
operator fun Decimal.minus(value: Decimal): Decimal = if (this is Decimal.Finite && value is Decimal.Finite) {
    Decimal.Finite(finiteDecimal - value.finiteDecimal)
} else {
    calculateToDecimal(value, Double::minus)
}

/**
 * Subtracts two [Decimal] to a given precision.
 * @param value the [Decimal] to subtract
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @return the [Decimal] that is the subtraction of the two provided decimals.
 */
fun Decimal.minus(value: Decimal, scale: Int): Decimal = if (this is Decimal.Finite && value is Decimal.Finite) {
    Decimal.Finite(finiteDecimal.minus(value.finiteDecimal, scale))
} else {
    calculateToDecimal(value, Double::minus)
}

/**
 * Subtracts two [Decimal] to a given precision.
 * @param value the [Decimal] to subtract
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @param roundingMode The [RoundingMode] to apply when scaling.
 * @return the [Decimal] that is the subtraction of the two provided decimals.
 */
fun Decimal.minus(value: Decimal, scale: Int, roundingMode: RoundingMode = RoundingMode.RoundHalfEven): Decimal = if (this is Decimal.Finite && value is Decimal.Finite) {
    Decimal.Finite(finiteDecimal.minus(value.finiteDecimal, scale, roundingMode))
} else {
    calculateToDecimal(value, Double::minus)
}

internal expect operator fun FiniteDecimal.minus(value: FiniteDecimal): FiniteDecimal
internal expect fun FiniteDecimal.minus(value: FiniteDecimal, scale: Int): FiniteDecimal
internal expect fun FiniteDecimal.minus(value: FiniteDecimal, scale: Int, roundingMode: RoundingMode = RoundingMode.RoundHalfEven): FiniteDecimal

/**
 * Divides two [Decimal].
 * @param value the [Decimal] to divide
 * @return the [Decimal] that is the division of the two provided decimals.
 */
operator fun Decimal.div(value: Decimal): Decimal = if (this is Decimal.Finite && value is Decimal.Finite) {
    if (value.toDouble() == 0.0) {
        (toDouble() / value.toDouble()).toDecimal()
    } else {
        Decimal.Finite(finiteDecimal / value.finiteDecimal)
    }
} else {
    calculateToDecimal(value, Double::div)
}

/**
 * Divides two [Decimal] to a given precision.
 * @param value the [Decimal] to divide
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @return the [Decimal] that is the division of the two provided decimals.
 */
fun Decimal.div(value: Decimal, scale: Int): Decimal = if (this is Decimal.Finite && value is Decimal.Finite) {
    if (value.toDouble() == 0.0) {
        (toDouble() / value.toDouble()).toDecimal()
    } else {
        Decimal.Finite(finiteDecimal.div(value.finiteDecimal, scale))
    }
} else {
    calculateToDecimal(value, Double::div)
}

/**
 * Divides two [Decimal] to a given precision.
 * @param value the [Decimal] to divide
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @param roundingMode The [RoundingMode] to apply when scaling.
 * @return the [Decimal] that is the division of the two provided decimals.
 */
fun Decimal.div(value: Decimal, scale: Int, roundingMode: RoundingMode = RoundingMode.RoundHalfEven): Decimal = if (this is Decimal.Finite && value is Decimal.Finite) {
    if (value.toDouble() == 0.0) {
        (toDouble() / value.toDouble()).toDecimal()
    } else {
        Decimal.Finite(finiteDecimal.div(value.finiteDecimal, scale, roundingMode))
    }
} else {
    calculateToDecimal(value, Double::div)
}

internal expect operator fun FiniteDecimal.div(value: FiniteDecimal): FiniteDecimal
internal expect fun FiniteDecimal.div(value: FiniteDecimal, scale: Int): FiniteDecimal
internal expect fun FiniteDecimal.div(value: FiniteDecimal, scale: Int, roundingMode: RoundingMode = RoundingMode.RoundHalfEven): FiniteDecimal

/**
 * Multiplies two [Decimal].
 * @param value the [Decimal] to multiply
 * @return the [Decimal] that is the multiplication of the two provided decimals.
 */
operator fun Decimal.times(value: Decimal): Decimal = if (this is Decimal.Finite && value is Decimal.Finite) {
    Decimal.Finite(finiteDecimal * value.finiteDecimal)
} else {
    calculateToDecimal(value, Double::times)
}

/**
 * Multiplies two [Decimal] to a given precision.
 * @param value the [Decimal] to multiply
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @return the [Decimal] that is the multiplication of the two provided decimals.
 */
fun Decimal.times(value: Decimal, scale: Int): Decimal = if (this is Decimal.Finite && value is Decimal.Finite) {
    Decimal.Finite(finiteDecimal.times(value.finiteDecimal, scale))
} else {
    calculateToDecimal(value, Double::times)
}

/**
 * Multiplies two [Decimal] to a given precision.
 * @param value the [Decimal] to multiply
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @param roundingMode The [RoundingMode] to apply when scaling.
 * @return the [Decimal] that is the multiplication of the two provided decimals.
 */
fun Decimal.times(value: Decimal, scale: Int, roundingMode: RoundingMode = RoundingMode.RoundHalfEven): Decimal = if (this is Decimal.Finite && value is Decimal.Finite) {
    Decimal.Finite(finiteDecimal.times(value.finiteDecimal, scale, roundingMode))
} else {
    calculateToDecimal(value, Double::times)
}

internal expect operator fun FiniteDecimal.times(value: FiniteDecimal): FiniteDecimal
internal expect fun FiniteDecimal.times(value: FiniteDecimal, scale: Int): FiniteDecimal
internal expect fun FiniteDecimal.times(value: FiniteDecimal, scale: Int, roundingMode: RoundingMode = RoundingMode.RoundHalfEven): FiniteDecimal

/**
 * Raises two [Decimal].
 * @param n the [Decimal] to raise to
 * @return the [Decimal] that is the exponent of the two provided decimals.
 */
infix fun Decimal.pow(n: Int): Decimal = if (this is Decimal.Finite) {
    Decimal.Finite(finiteDecimal.pow(n))
} else {
    (this.toDouble().pow(n)).toDecimal()
}

/**
 * Raises two [Decimal] to a given precision.
 * @param n the [Decimal] to raise to
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @return the [Decimal] that is the exponent of the two provided decimals.
 */
fun Decimal.pow(n: Int, scale: Int): Decimal = if (this is Decimal.Finite) {
    Decimal.Finite(finiteDecimal.pow(n, scale))
} else {
    (this.toDouble().pow(n)).toDecimal()
}

/**
 * Raises two [Decimal] to a given precision.
 * @param n the [Decimal] to raise to
 * @param scale The number of digits a rounded value should have after its decimal point.
 * @param roundingMode The [RoundingMode] to apply when scaling.
 * @return the [Decimal] that is the exponent of the two provided decimals.
 */
fun Decimal.pow(n: Int, scale: Int, roundingMode: RoundingMode = RoundingMode.RoundHalfEven): Decimal = if (this is Decimal.Finite) {
    Decimal.Finite(finiteDecimal.pow(n, scale, roundingMode))
} else {
    (this.toDouble().pow(n)).toDecimal()
}

internal expect infix fun FiniteDecimal.pow(n: Int): FiniteDecimal
internal expect fun FiniteDecimal.pow(n: Int, scale: Int): FiniteDecimal
internal expect fun FiniteDecimal.pow(n: Int, scale: Int, roundingMode: RoundingMode = RoundingMode.RoundHalfEven): FiniteDecimal

/**
 * Rounding Mode for rounding a [Decimal]
 */
sealed class RoundingMode {
    /**
     * Rounds values down
     */
    data object RoundDown : RoundingMode()

    /**
     * Rounds values to the closest possible return value; when halfway between two possibilities, return the possibility whose last digit is even.
     */
    data object RoundHalfEven : RoundingMode()

    /**
     * Rounds values up
     */
    data object RoundUp : RoundingMode()
}

/**
 * Rounds a [Decimal] to a [scale].
 * @param scale The number of digits the rounded value should have after its decimal point.
 * @param roundingMode The [RoundingMode] to apply when scaling.
 * @return A [Decimal] rounded to [scale] digits after the decimal point.
 */
fun Decimal.round(scale: Int, roundingMode: RoundingMode = RoundingMode.RoundHalfEven): Decimal = when (this) {
    is Decimal.Finite -> Decimal.Finite(finiteDecimal.round(scale, roundingMode))
    else -> this
}
internal expect fun FiniteDecimal.round(scale: Int, roundingMode: RoundingMode = RoundingMode.RoundHalfEven): FiniteDecimal

/**
 * Converts a [Number] to a [Decimal]
 */
fun Number.toDecimal(): Decimal = when (this) {
    is Long -> toFiniteDecimalOrNaN()
    is Int -> toFiniteDecimalOrNaN()
    is Short -> toFiniteDecimalOrNaN()
    else -> when {
        toDouble().isFinite() -> toFiniteDecimalOrNaN()
        toDouble().isNaN() -> Decimal.NaN
        toDouble() == Double.POSITIVE_INFINITY -> Decimal.PositiveInfinity
        else -> Decimal.NegativeInfinity
    }
}

/**
 * Converts a String to a [Decimal]
 */
fun String.toDecimal(): Decimal = when (lowercase()) {
    Double.NaN.toString().lowercase() -> Decimal.NaN
    Double.POSITIVE_INFINITY.toString().lowercase(),
    '\u221E'.toString(),
    -> Decimal.PositiveInfinity
    Double.NEGATIVE_INFINITY.toString().lowercase(),
    "-${'\u221E'}",
    -> Decimal.NegativeInfinity
    else -> toFiniteDecimal()?.let { Decimal.Finite(it) } ?: Decimal.NaN
}

internal fun Number.toFiniteDecimalOrNaN() = toFiniteDecimal()?.let { Decimal.Finite(it) } ?: Decimal.NaN
internal expect fun Number.toFiniteDecimal(): FiniteDecimal?
internal expect fun String.toFiniteDecimal(): FiniteDecimal?

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
fun Decimal.toDouble(): Double = when (this) {
    is Decimal.Finite -> finiteDecimal.toDouble()
    is Decimal.NaN -> Double.NaN
    is Decimal.NegativeInfinity -> Double.NEGATIVE_INFINITY
    is Decimal.PositiveInfinity -> Double.POSITIVE_INFINITY
}

/**
 * Gets the string value of a [Decimal]
 */
fun Decimal.toString(): String = when (this) {
    is Decimal.Finite -> finiteDecimal.toString()
    is Decimal.NaN -> Double.NaN.toString()
    is Decimal.NegativeInfinity -> Double.NEGATIVE_INFINITY.toString()
    is Decimal.PositiveInfinity -> Double.POSITIVE_INFINITY.toString()
}

/**
 * Gets the integer value of a [Decimal]
 */
fun Decimal.toInt(): Int = when (this) {
    is Decimal.Finite -> finiteDecimal.toInt()
    is Decimal.NaN -> 0
    is Decimal.NegativeInfinity -> Int.MIN_VALUE
    is Decimal.PositiveInfinity -> Int.MAX_VALUE
}

/**
 * Gets the long value of a [Decimal]
 */
fun Decimal.toLong(): Long = when (this) {
    is Decimal.Finite -> finiteDecimal.toLong()
    is Decimal.NaN -> 0L
    is Decimal.NegativeInfinity -> Long.MIN_VALUE
    is Decimal.PositiveInfinity -> Long.MAX_VALUE
}

internal expect fun FiniteDecimal.toDouble(): Double
internal expect fun FiniteDecimal.toString(): String
internal expect fun FiniteDecimal.toInt(): Int
internal expect fun FiniteDecimal.toLong(): Long

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

/**
 * Converts a collection of [Decimal] to a [LongArray]
 */
fun Collection<Decimal>.toLongArray(): LongArray = map { it.toLong() }.toLongArray()

private fun Decimal.calculateToDecimal(other: Decimal, operator: Double.(Double) -> Double) = this.toDouble().operator(other.toDouble()).toDecimal()
