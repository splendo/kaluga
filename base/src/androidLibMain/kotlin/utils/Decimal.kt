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

@file:JvmName("DecimalAndroid")
@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")
package com.splendo.kaluga.base.utils

import com.splendo.kaluga.base.utils.RoundingMode.RoundDown
import com.splendo.kaluga.base.utils.RoundingMode.RoundHalfEven
import com.splendo.kaluga.base.utils.RoundingMode.RoundUp
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode as NativeRoundingMode

actual typealias Decimal = BigDecimal

actual operator fun Decimal.plus(value: Decimal) = this.add(value)

actual fun Decimal.plus(value: Decimal, scale: Int) =
    this.add(value).setScale(scale, NativeRoundingMode.HALF_EVEN)

actual fun Decimal.plus(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode
) = this.add(value).setScale(scale, roundingMode.android)

actual operator fun Decimal.minus(value: Decimal) = this.subtract(value)

actual fun Decimal.minus(value: Decimal, scale: Int) =
    this.subtract(value).setScale(scale, NativeRoundingMode.HALF_EVEN)

actual fun Decimal.minus(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode
) = this.subtract(value).setScale(scale, roundingMode.android)

actual operator fun Decimal.div(value: Decimal) = if ((value.compareTo(Decimal.ZERO)) != 0) this.divide(value, MathContext.DECIMAL128) else throw DecimalException("Divide by zero")

actual fun Decimal.div(value: Decimal, scale: Int) = if ((value.compareTo(Decimal.ZERO)) != 0)
    this.divide(value, MathContext.DECIMAL128).setScale(scale, NativeRoundingMode.HALF_EVEN)
else throw DecimalException("Divide by zero")

actual fun Decimal.div(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode
) = if ((value.compareTo(Decimal.ZERO)) != 0)
    this.divide(
        value,
        MathContext(
            MathContext.DECIMAL128.precision,
            roundingMode.android
        )
    ).setScale(scale, roundingMode.android)
else throw DecimalException("Divide by zero")

actual operator fun Decimal.times(value: Decimal) =
    this.multiply(value, MathContext.DECIMAL128)

actual fun Decimal.times(value: Decimal, scale: Int) =
    this.multiply(value, MathContext.DECIMAL128).setScale(scale, NativeRoundingMode.HALF_EVEN)

actual fun Decimal.times(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode
) = this.multiply(
    value,
    MathContext(
        MathContext.DECIMAL128.precision,
        roundingMode.android
    )
).setScale(scale, roundingMode.android)

actual fun Decimal.round(scale: Int, roundingMode: RoundingMode) =
    this.setScale(
        scale,
        roundingMode.android
    )

actual infix fun Decimal.pow(n: Int): Decimal = this.pow(n, MathContext.DECIMAL128)
actual fun Decimal.pow(n: Int, scale: Int): Decimal = this.pow(n, MathContext.DECIMAL128).setScale(scale, NativeRoundingMode.HALF_EVEN)
actual fun Decimal.pow(
    n: Int,
    scale: Int,
    roundingMode: RoundingMode
): Decimal = this.pow(
    n,
    MathContext(
        MathContext.DECIMAL128.precision,
        roundingMode.android
    )
).setScale(scale, roundingMode.android)

actual fun Number.toDecimal() = BigDecimal(toString())
actual fun String.toDecimal() = BigDecimal(this)

actual fun Decimal.toDouble() = this.toDouble()
actual fun Decimal.toInt() = this.toInt()
actual fun Decimal.toString() = this.stripTrailingZeros().toString()

val RoundingMode.android
    get() = when (this) {
        RoundDown -> NativeRoundingMode.DOWN
        RoundHalfEven -> NativeRoundingMode.HALF_EVEN
        RoundUp -> NativeRoundingMode.UP
    }
