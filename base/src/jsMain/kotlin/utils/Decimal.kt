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

/**
 * Rounding for [BigDecimal]
 * @property maximumFractionDigits The maximum number of digits as part of the fraction.
 * @property roundingMode Describes the rounding mode. Can be either `half-even`, `ceil`, or `floor`
 * @property maximumSignificantDigits The maximum number of significant digits
 */
data class Rounding(
    val maximumFractionDigits: Int? = null,
    val roundingMode: String = ROUNDING_MODE,
    val maximumSignificantDigits: Int = DECIMAL_128_SIGNIFICANT_DIGITS,
)

/**
 * Platform specific representation of a finite immutable, arbitrary-precision signed decimal number
 * @property bd the [BigDecimal] representing the finite decimal number
 */
actual data class FiniteDecimal(val bd: BigDecimal) : Comparable<FiniteDecimal> {
    override fun compareTo(other: FiniteDecimal): Int = BigDecimal.compare(bd, other.bd)
    override fun equals(other: Any?): Boolean = (other as? FiniteDecimal)?.let { BigDecimal.equal(bd, it.bd) } ?: false
    override fun hashCode(): Int {
        return bd.hashCode()
    }
}

private val ZERO = BigDecimal.BigDecimal(0)

private fun round(a: BigDecimal, roundingMode: RoundingMode, maximumFractionDigits: Int? = null): FiniteDecimal {
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
    return FiniteDecimal(BigDecimal.round(a, Rounding(maximumFractionDigits, mode)))
}

actual operator fun FiniteDecimal.plus(value: FiniteDecimal): FiniteDecimal = FiniteDecimal(BigDecimal.add(bd, value.bd))

actual fun FiniteDecimal.plus(value: FiniteDecimal, scale: Int): FiniteDecimal = FiniteDecimal(BigDecimal.add(bd, value.bd, Rounding(scale)))

actual fun FiniteDecimal.plus(value: FiniteDecimal, scale: Int, roundingMode: RoundingMode): FiniteDecimal = round(BigDecimal.add(bd, value.bd), roundingMode, scale)

actual operator fun FiniteDecimal.minus(value: FiniteDecimal): FiniteDecimal = FiniteDecimal(BigDecimal.subtract(bd, value.bd))

actual fun FiniteDecimal.minus(value: FiniteDecimal, scale: Int): FiniteDecimal = FiniteDecimal(BigDecimal.subtract(bd, value.bd, Rounding(scale)))

actual fun FiniteDecimal.minus(value: FiniteDecimal, scale: Int, roundingMode: RoundingMode): FiniteDecimal = round(BigDecimal.subtract(bd, value.bd), roundingMode, scale)

actual operator fun FiniteDecimal.div(value: FiniteDecimal): FiniteDecimal = FiniteDecimal(BigDecimal.divide(bd, value.bd, Rounding()))

actual fun FiniteDecimal.div(value: FiniteDecimal, scale: Int): FiniteDecimal = FiniteDecimal(BigDecimal.divide(bd, value.bd, Rounding(scale)))

actual fun FiniteDecimal.div(value: FiniteDecimal, scale: Int, roundingMode: RoundingMode): FiniteDecimal =
    round(BigDecimal.divide(bd, value.bd, Rounding(scale + 1, ROUNDING_MODE, DIV_DECIMAL_128_SIGNIFICANT_DIGITS)), roundingMode, scale)

actual operator fun FiniteDecimal.times(value: FiniteDecimal): FiniteDecimal = FiniteDecimal(BigDecimal.multiply(bd, value.bd, Rounding()))

actual fun FiniteDecimal.times(value: FiniteDecimal, scale: Int): FiniteDecimal = FiniteDecimal(BigDecimal.multiply(bd, value.bd, Rounding(scale)))

actual fun FiniteDecimal.times(value: FiniteDecimal, scale: Int, roundingMode: RoundingMode): FiniteDecimal = round(BigDecimal.multiply(bd, value.bd), roundingMode, scale)

actual infix fun FiniteDecimal.pow(n: Int): FiniteDecimal = FiniteDecimal(BigDecimal.BigDecimal(toDouble().pow(n)))
actual fun FiniteDecimal.pow(n: Int, scale: Int): FiniteDecimal = FiniteDecimal(BigDecimal.round((this pow n).bd, Rounding(scale)))
actual fun FiniteDecimal.pow(n: Int, scale: Int, roundingMode: RoundingMode): FiniteDecimal = this pow n

actual fun FiniteDecimal.round(scale: Int, roundingMode: RoundingMode): FiniteDecimal = round(bd, roundingMode, scale)

actual fun Number.toFiniteDecimal(): FiniteDecimal? = this.toString().toFiniteDecimal()
actual fun String.toFiniteDecimal(): FiniteDecimal? = try {
    FiniteDecimal(BigDecimal.BigDecimal(this))
} catch (e: dynamic) {
    null
}

actual fun FiniteDecimal.toDouble(): Double = bd.toString().toDouble()
actual fun FiniteDecimal.toInt(): Int = bd.toFixed(0).toInt()
actual fun FiniteDecimal.toLong() = bd.toFixed(0).toLong()
actual fun FiniteDecimal.toString(): String = bd.toString()
