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
import java.math.RoundingMode

actual typealias Decimal = BigDecimal

actual operator fun Decimal.plus(value: Decimal): Decimal = this.add(value)

actual fun Decimal.plus(value: Decimal, scale: Int): Decimal = this.add(value).setScale(scale)

actual fun Decimal.plus(
    value: Decimal,
    scale: Int,
    roundingMode: com.splendo.kaluga.base.utils.RoundingMode
): Decimal =
    this.add(value).setScale(scale, toPlatformSpecificRoundCode(roundingMode))

actual operator fun Decimal.minus(value: Decimal): Decimal = this.subtract(value)

actual fun Decimal.minus(value: Decimal, scale: Int): Decimal = this.subtract(value).setScale(scale)

actual fun Decimal.minus(
    value: Decimal,
    scale: Int,
    roundingMode: com.splendo.kaluga.base.utils.RoundingMode
): Decimal =
    this.subtract(value).setScale(scale, toPlatformSpecificRoundCode(roundingMode))

actual operator fun Decimal.div(value: Decimal): Decimal =
    this.divide(value, MathContext.DECIMAL128)

actual fun Decimal.div(value: Decimal, scale: Int): Decimal =
    this.divide(value, MathContext.DECIMAL128).setScale(scale, MathContext.DECIMAL128.roundingMode)

actual fun Decimal.div(
    value: Decimal,
    scale: Int,
    roundingMode: com.splendo.kaluga.base.utils.RoundingMode
): Decimal =
    this.divide(
        value,
        MathContext(
            MathContext.DECIMAL128.precision,
            RoundingMode.valueOf(toPlatformSpecificRoundCode(roundingMode))
        )
    ).setScale(scale, toPlatformSpecificRoundCode(roundingMode))

actual operator fun Decimal.times(value: Decimal): Decimal =
    this.multiply(value, MathContext.DECIMAL128)

actual fun Decimal.times(value: Decimal, scale: Int): Decimal =
    this.multiply(value, MathContext.DECIMAL128).setScale(scale)

actual fun Decimal.times(
    value: Decimal,
    scale: Int,
    roundingMode: com.splendo.kaluga.base.utils.RoundingMode
): Decimal =
    this.multiply(
        value,
        MathContext(
            MathContext.DECIMAL128.precision,
            RoundingMode.valueOf(toPlatformSpecificRoundCode(roundingMode))
        )
    ).setScale(scale, RoundingMode.valueOf(toPlatformSpecificRoundCode(roundingMode)))

actual fun Double.toDecimal(): BigDecimal = BigDecimal.valueOf(this)
actual fun String.toDecimal() = BigDecimal(this)

actual fun Decimal.toDouble(): Double = this.toDouble()
actual fun Decimal.toString(): String = this.stripTrailingZeros().toString()

actual fun toPlatformSpecificRoundCode(roundingMode: com.splendo.kaluga.base.utils.RoundingMode) =
    when (roundingMode) {
        RoundDown -> RoundingMode.DOWN.ordinal
        RoundHalfEven -> RoundingMode.HALF_EVEN.ordinal
        RoundUp -> RoundingMode.UP.ordinal
        else -> RoundingMode.HALF_EVEN.ordinal
    }
