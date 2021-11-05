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

actual typealias Decimal = BigDecimal

actual operator fun Decimal.plus(value: Decimal): Decimal = this.add(this, value)

actual fun Decimal.plus(value: Decimal, scale: Int): Decimal = this + value

actual fun Decimal.plus(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode
): Decimal = this + value

actual operator fun Decimal.minus(value: Decimal): Decimal = this.subtract(this, value)

actual fun Decimal.minus(value: Decimal, scale: Int): Decimal = this - value

actual fun Decimal.minus(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode
): Decimal = this - value

actual operator fun Decimal.div(value: Decimal): Decimal = this.divide(this, value)

actual fun Decimal.div(value: Decimal, scale: Int): Decimal = this / value

actual fun Decimal.div(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode
): Decimal = this / value

actual operator fun Decimal.times(value: Decimal): Decimal = this.multiply(this, value)

actual fun Decimal.times(value: Decimal, scale: Int): Decimal = this * value

actual fun Decimal.times(
    value: Decimal,
    scale: Int,
    roundingMode: RoundingMode
): Decimal = this * value

actual fun Decimal.round(scale: Int, roundingMode: RoundingMode): Decimal = this

actual fun Double.toDecimal(): Decimal = BigDecimal().BigDecimal(this)
actual fun Int.toDecimal(): Decimal = BigDecimal().BigDecimal(this)
actual fun String.toDecimal(): Decimal = BigDecimal().BigDecimal(this)

actual fun Decimal.toDouble(): Double = this.toNumber(this)
actual fun Decimal.toInt(): Int = this.toFixed(0)
actual fun Decimal.toString(): String = this.toString()
