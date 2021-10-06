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
) = this.add(value).setScale(scale, roundingMode.java)

actual operator fun Decimal.minus(value: Decimal) = this.subtract(value)

actual fun Decimal.minus(value: Decimal, scale: Int) =
    this.subtract(value).setScale(scale, NativeRoundingMode.HALF_EVEN)

actual fun Decimal.minus(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode
) = this.subtract(value).setScale(scale, roundingMode.java)

actual operator fun Decimal.div(value: Decimal) = this.divide(value, MathContext.DECIMAL128)

actual fun Decimal.div(value: Decimal, scale: Int) =
    this.divide(value, MathContext.DECIMAL128).setScale(scale, NativeRoundingMode.HALF_EVEN)

actual fun Decimal.div(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode
) = this.divide(
    value,
    MathContext(
        MathContext.DECIMAL128.precision,
        NativeRoundingMode.valueOf(roundingMode.java)
    )
).setScale(scale, roundingMode.java)

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
        NativeRoundingMode.valueOf(roundingMode.java)
    )
).setScale(scale, NativeRoundingMode.valueOf(roundingMode.java))

actual fun Decimal.round(scale: Int, roundingMode: RoundingMode) =
    this.round(
        MathContext(
            scale + this.toInt().length(),
            NativeRoundingMode.valueOf(roundingMode.java)
        )
    )

actual fun Double.toDecimal() = BigDecimal.valueOf(this)
actual fun Int.toDecimal() = BigDecimal.valueOf(this.toDouble())
actual fun String.toDecimal() = BigDecimal(this)

actual fun Decimal.toDouble() = this.toDouble()
actual fun Decimal.toInt() = this.toInt()
actual fun Decimal.toString() = this.stripTrailingZeros().toString()

val RoundingMode.java
    get() = when (this) {
        RoundDown -> NativeRoundingMode.DOWN.ordinal
        RoundHalfEven -> NativeRoundingMode.HALF_EVEN.ordinal
        RoundUp -> NativeRoundingMode.UP.ordinal
    }
