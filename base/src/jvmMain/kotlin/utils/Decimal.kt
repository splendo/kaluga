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

/**
 * Platform specific representation of a finite immutable, arbitrary-precision signed decimal number
 */
actual typealias FiniteDecimal = BigDecimal

actual operator fun FiniteDecimal.plus(value: FiniteDecimal) = this.add(value)

actual fun FiniteDecimal.plus(value: FiniteDecimal, scale: Int) = this.add(value).setScale(scale, NativeRoundingMode.HALF_EVEN)

actual fun FiniteDecimal.plus(value: FiniteDecimal, scale: Int, roundingMode: RoundingMode) = this.add(value).setScale(scale, roundingMode.java)

actual operator fun FiniteDecimal.minus(value: FiniteDecimal) = this.subtract(value)

actual fun FiniteDecimal.minus(value: FiniteDecimal, scale: Int) = this.subtract(value).setScale(scale, NativeRoundingMode.HALF_EVEN)

actual fun FiniteDecimal.minus(value: FiniteDecimal, scale: Int, roundingMode: RoundingMode) = this.subtract(value).setScale(scale, roundingMode.java)

actual operator fun FiniteDecimal.div(value: FiniteDecimal) = this.divide(value, MathContext.DECIMAL128)

actual fun FiniteDecimal.div(value: FiniteDecimal, scale: Int) = this.divide(value, MathContext.DECIMAL128).setScale(scale, NativeRoundingMode.HALF_EVEN)

actual fun FiniteDecimal.div(value: FiniteDecimal, scale: Int, roundingMode: RoundingMode) = this.divide(
    value,
    MathContext(
        MathContext.DECIMAL128.precision,
        roundingMode.java,
    ),
).setScale(scale, roundingMode.java)

actual operator fun FiniteDecimal.times(value: FiniteDecimal) = this.multiply(value, MathContext.DECIMAL128)

actual fun FiniteDecimal.times(value: FiniteDecimal, scale: Int) = this.multiply(value, MathContext.DECIMAL128).setScale(scale, NativeRoundingMode.HALF_EVEN)

actual fun FiniteDecimal.times(value: FiniteDecimal, scale: Int, roundingMode: RoundingMode) = this.multiply(
    value,
    MathContext(
        MathContext.DECIMAL128.precision,
        roundingMode.java,
    ),
).setScale(scale, roundingMode.java)

actual fun FiniteDecimal.round(scale: Int, roundingMode: RoundingMode) = this.setScale(
    scale,
    roundingMode.java,
)

actual infix fun FiniteDecimal.pow(n: Int): FiniteDecimal = this.pow(n, MathContext.DECIMAL128)
actual fun FiniteDecimal.pow(n: Int, scale: Int): FiniteDecimal = this.pow(n, MathContext.DECIMAL128).setScale(scale, NativeRoundingMode.HALF_EVEN)
actual fun FiniteDecimal.pow(n: Int, scale: Int, roundingMode: RoundingMode): FiniteDecimal = this.pow(
    n,
    MathContext(
        MathContext.DECIMAL128.precision,
        roundingMode.java,
    ),
).setScale(scale, roundingMode.java)

actual fun Number.toFiniteDecimal() = toString().toFiniteDecimal()
actual fun String.toFiniteDecimal() = try {
    BigDecimal(this)
} catch (e: NumberFormatException) {
    null
}

actual fun FiniteDecimal.toDouble() = this.toDouble()
actual fun FiniteDecimal.toInt() = this.toInt()
actual fun FiniteDecimal.toLong() = this.toLong()
actual fun FiniteDecimal.toString() = this.stripTrailingZeros().toString()

private val RoundingMode.java
    get() = when (this) {
        RoundDown -> NativeRoundingMode.DOWN
        RoundHalfEven -> NativeRoundingMode.HALF_EVEN
        RoundUp -> NativeRoundingMode.UP
    }
