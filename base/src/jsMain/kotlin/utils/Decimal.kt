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

actual typealias Decimal = Double

actual operator fun Decimal.plus(value: Decimal): Decimal = this.plus(value)
actual fun Decimal.plus(value: Decimal, scale: Int): Decimal = this.plus(value)
actual fun Decimal.plus(value: Decimal, scale: Int, roundingMode: Int): Decimal =
    this.plus(value)

actual operator fun Decimal.minus(value: Decimal): Decimal = this.minus(value)
actual fun Decimal.minus(value: Decimal, scale: Int): Decimal = this.minus(value)
actual fun Decimal.minus(value: Decimal, scale: Int, roundingMode: Int): Decimal =
    this.minus(value)

actual operator fun Decimal.div(value: Decimal): Decimal = this.div(value)
actual fun Decimal.div(value: Decimal, scale: Int): Decimal = this.div(value)
actual fun Decimal.div(value: Decimal, scale: Int, roundingMode: Int): Decimal =
    this.div(value)

actual operator fun Decimal.times(value: Decimal): Decimal = this.times(value)
actual fun Decimal.times(value: Decimal, scale: Int): Decimal = this.times(value)
actual fun Decimal.times(value: Decimal, scale: Int, roundingMode: Int): Decimal =
    this.times(value)

actual object Decimals {
    actual val ROUND_DOWN: Int
        get() = BigDecimal.ROUND_DOWN
    actual val ROUND_HALF_EVEN: Int
        get() = BigDecimal.ROUND_HALF_EVEN
    actual val ROUND_UP: Int
        get() = BigDecimal.ROUND_UP

    actual fun decimalFrom(value: Double): Decimal {
        TODO("Not yet implemented")
    }

    actual fun Double.toDecimal(): Decimal {
        TODO("Not yet implemented")
    }
}