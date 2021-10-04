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

expect class Decimal

expect operator fun Decimal.plus(value: Decimal): Decimal
expect fun Decimal.plus(value: Decimal, scale: Int): Decimal
expect fun Decimal.plus(
    value: Decimal,
    scale: Int = 10,
    roundingMode: Int = Decimals.ROUND_HALF_EVEN
): Decimal

expect operator fun Decimal.minus(value: Decimal): Decimal
expect fun Decimal.minus(value: Decimal, scale: Int): Decimal
expect fun Decimal.minus(
    value: Decimal,
    scale: Int = 10,
    roundingMode: Int = Decimals.ROUND_HALF_EVEN
): Decimal

expect operator fun Decimal.div(value: Decimal): Decimal
expect fun Decimal.div(value: Decimal, scale: Int): Decimal
expect fun Decimal.div(
    value: Decimal,
    scale: Int,
    roundingMode: Int = Decimals.ROUND_HALF_EVEN
): Decimal

expect operator fun Decimal.times(value: Decimal): Decimal
expect fun Decimal.times(value: Decimal, scale: Int): Decimal
expect fun Decimal.times(
    value: Decimal,
    scale: Int,
    roundingMode: Int = Decimals.ROUND_HALF_EVEN
): Decimal

expect object Decimals {

    fun Double.toDecimal(): Decimal
    fun String.toDecimal(): Decimal

    fun Decimal.toDouble(): Double
    fun Decimal.toString(): String

    // Rounding mode to round towards zero.
    val ROUND_DOWN: Int

    // Rounding mode to round towards the "nearest neighbor" unless both neighbors are equidistant, in which case, round towards the even neighbor.
    val ROUND_HALF_EVEN: Int

    // Rounding mode to round away from zero.
    val ROUND_UP: Int
}
