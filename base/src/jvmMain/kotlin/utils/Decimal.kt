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

actual typealias Decimal = BigDecimal

actual fun Decimal.plus(value: Decimal): Decimal = this.add(value)
actual fun Decimal.plus(value: Decimal, scale: Int): Decimal = this.add(value).setScale(scale)
actual fun Decimal.plus(value: Decimal, scale: Int, roundingMode: Int): Decimal =
    this.add(value).setScale(scale, roundingMode)

actual fun Decimal.minus(value: Decimal): Decimal = this.subtract(value)
actual fun Decimal.minus(value: Decimal, scale: Int): Decimal = this.subtract(value).setScale(scale)
actual fun Decimal.minus(value: Decimal, scale: Int, roundingMode: Int): Decimal =
    this.subtract(value).setScale(scale, roundingMode)

actual fun Decimal.divide(value: Decimal): Decimal = this.divide(value)
actual fun Decimal.divide(value: Decimal, scale: Int): Decimal = this.divide(value).setScale(scale)
actual fun Decimal.divide(value: Decimal, scale: Int, roundingMode: Int): Decimal =
    this.divide(value).setScale(scale, roundingMode)

actual fun Decimal.multiply(value: Decimal): Decimal = this.multiply(value)
actual fun Decimal.multiply(value: Decimal, scale: Int): Decimal = this.multiply(value).setScale(scale)
actual fun Decimal.multiply(value: Decimal, scale: Int, roundingMode: Int): Decimal =
    this.multiply(value).setScale(scale, roundingMode)

actual object Decimals {
    actual val ROUND_DOWN: Int
        get() = BigDecimal.ROUND_DOWN
    actual val ROUND_HALF_EVEN: Int
        get() = BigDecimal.ROUND_HALF_EVEN
    actual val ROUND_UP: Int
        get() = BigDecimal.ROUND_UP
}

