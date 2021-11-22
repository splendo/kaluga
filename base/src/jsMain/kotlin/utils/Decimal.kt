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
package com.splendo.kaluga.base.utils

import com.splendo.kaluga.base.utils.RoundingMode.RoundDown
import com.splendo.kaluga.base.utils.RoundingMode.RoundHalfEven
import com.splendo.kaluga.base.utils.RoundingMode.RoundUp

// 34 digits of the IEEE 754R Decimal128 format
val DECIMAL_128_SIGNIFICANT_DIGITS = 34
// Max decimal digits + 1 for intermediate division
val DIV_DECIMAL_128_SIGNIFICANT_DIGITS = DECIMAL_128_SIGNIFICANT_DIGITS + 1

// Default is half-even
val ROUNDING_MODE = "half-even"
val CEIL = "ceil"
val FLOOR = "floor"

data class Rounding(
    val maximumFractionDigits: Int? = null,
    val roundingMode: String = ROUNDING_MODE,
    val maximumSignificantDigits: Int = DECIMAL_128_SIGNIFICANT_DIGITS
)

actual typealias Decimal = BigDecimal

val ZERO = BigDecimal.BigDecimal(0)

fun round(
    a: BigDecimal,
    roundingMode: RoundingMode,
    maximumFractionDigits: Int? = null
): BigDecimal {
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
    return BigDecimal.round(a, Rounding(maximumFractionDigits, mode))
}

actual operator fun Decimal.plus(value: Decimal): Decimal = BigDecimal.add(this, value)

actual fun Decimal.plus(
    value: Decimal,
    scale: Int
): Decimal = BigDecimal.add(this, value, Rounding(scale))

actual fun Decimal.plus(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode
): Decimal = round(BigDecimal.add(this, value), roundingMode, scale)

actual operator fun Decimal.minus(value: Decimal): Decimal = BigDecimal.subtract(this, value)

actual fun Decimal.minus(
    value: Decimal,
    scale: Int
): Decimal = BigDecimal.subtract(this, value, Rounding(scale))

actual fun Decimal.minus(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode
): Decimal = round(BigDecimal.subtract(this, value), roundingMode, scale)

actual operator fun Decimal.div(
    value: Decimal
): Decimal = BigDecimal.divide(this, value, Rounding())

actual fun Decimal.div(
    value: Decimal,
    scale: Int
): Decimal = BigDecimal.divide(this, value, Rounding(scale))

actual fun Decimal.div(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode
): Decimal = round(BigDecimal.divide(this, value, Rounding(scale + 1, ROUNDING_MODE, DIV_DECIMAL_128_SIGNIFICANT_DIGITS)), roundingMode, scale)

actual operator fun Decimal.times(
    value: Decimal
): Decimal = BigDecimal.multiply(this, value, Rounding())

actual fun Decimal.times(
    value: Decimal,
    scale: Int
): Decimal = BigDecimal.multiply(this, value, Rounding(scale))

actual fun Decimal.times(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode
): Decimal = round(BigDecimal.multiply(this, value), roundingMode, scale)

actual infix fun Decimal.pow(n: Int): Decimal = Decimal(this.double.pow(n.toInt()))
actual fun Decimal.pow(n: Int, scale: Int): Decimal = this pow n
actual fun Decimal.pow(
    n: Int,
    scale: Int,
    roundingMode: RoundingMode
): Decimal = this pow n

actual fun Decimal.round(
    scale: Int,
    roundingMode: RoundingMode
): Decimal = round(this, roundingMode, scale)

actual fun Number.toDecimal(): Decimal = this.toString().toDecimal()
actual fun String.toDecimal(): Decimal = BigDecimal.BigDecimal(this)

actual fun Decimal.toDouble(): Double = this.toString().toDouble()
actual fun Decimal.toInt(): Int = this.toFixed(0).toInt()
actual fun Decimal.toString(): String = this.toString()

fun Decimal.equals(a: Decimal, b: Decimal): Boolean = BigDecimal.equal(a, b)
