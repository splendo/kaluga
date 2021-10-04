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

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

actual typealias Decimal = BigDecimal

actual operator fun Decimal.plus(value: Decimal): Decimal = this.add(value)

actual fun Decimal.plus(value: Decimal, scale: Int): Decimal = this.add(value).setScale(scale)

actual fun Decimal.plus(value: Decimal, scale: Int, roundingMode: Int): Decimal =
    this.add(value).setScale(scale, roundingMode)

actual operator fun Decimal.minus(value: Decimal): Decimal = this.subtract(value)

actual fun Decimal.minus(value: Decimal, scale: Int): Decimal = this.subtract(value).setScale(scale)

actual fun Decimal.minus(value: Decimal, scale: Int, roundingMode: Int): Decimal =
    this.subtract(value).setScale(scale, roundingMode)

actual operator fun Decimal.div(value: Decimal): Decimal =
    this.divide(value, MathContext.DECIMAL128)

actual fun Decimal.div(value: Decimal, scale: Int): Decimal =
    this.divide(value, MathContext.DECIMAL128).setScale(scale, MathContext.DECIMAL128.getRoundingMode())

actual fun Decimal.div(value: Decimal, scale: Int, roundingMode: Int): Decimal =
    this.divide(
        value,
        MathContext(MathContext.DECIMAL128.getPrecision(), RoundingMode.valueOf(roundingMode))
    ).setScale(scale, roundingMode)

actual operator fun Decimal.times(value: Decimal): Decimal =
    this.multiply(value, MathContext.DECIMAL128)

actual fun Decimal.times(value: Decimal, scale: Int): Decimal =
    this.multiply(value, MathContext.DECIMAL128).setScale(scale)

actual fun Decimal.times(value: Decimal, scale: Int, roundingMode: Int): Decimal =
    this.multiply(
        value,
        MathContext(MathContext.DECIMAL128.getPrecision(), RoundingMode.valueOf(roundingMode))
    ).setScale(scale, roundingMode)

actual object Decimals {

    actual fun Double.toDecimal() = BigDecimal(this)
    actual fun String.toDecimal() = BigDecimal(this)

    actual fun Decimal.toDouble(): Double = this.toDouble()
    actual fun Decimal.toString(): String = this.toString()

    actual val ROUND_DOWN: Int
        get() = BigDecimal.ROUND_DOWN
    actual val ROUND_HALF_EVEN: Int
        get() = BigDecimal.ROUND_HALF_EVEN
    actual val ROUND_UP: Int
        get() = BigDecimal.ROUND_UP
}
