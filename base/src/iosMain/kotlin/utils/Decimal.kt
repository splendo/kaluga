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

import platform.Foundation.NSDecimalNumber
import platform.Foundation.NSDecimalNumberHandler
import platform.Foundation.NSRoundingMode

@Suppress("CONFLICTING_OVERLOADS")
actual typealias Decimal = NSDecimalNumber

actual operator fun Decimal.plus(value: Decimal): Decimal = decimalNumberByAdding(value)

actual fun Decimal.plus(value: Decimal, scale: Int): Decimal =
    decimalNumberByAdding(value, object : NSDecimalNumberHandler() {
        override fun scale(): Short = scale.toShort()
    })

actual fun Decimal.plus(value: Decimal, scale: Int, roundingMode: Int): Decimal =
    decimalNumberByAdding(value, object : NSDecimalNumberHandler() {
        override fun scale(): Short = scale.toShort()
        override fun roundingMode(): NSRoundingMode = NSRoundingMode.byValue(roundingMode.toULong())
    })

actual operator fun Decimal.minus(value: Decimal): Decimal = decimalNumberBySubtracting(value)

actual fun Decimal.minus(value: Decimal, scale: Int): Decimal =
    decimalNumberBySubtracting(value, object : NSDecimalNumberHandler() {
        override fun scale(): Short = scale.toShort()
    })

actual fun Decimal.minus(value: Decimal, scale: Int, roundingMode: Int): Decimal =
    decimalNumberBySubtracting(value, object : NSDecimalNumberHandler() {
        override fun scale(): Short = scale.toShort()
        override fun roundingMode(): NSRoundingMode = NSRoundingMode.byValue(roundingMode.toULong())
    })

actual operator fun Decimal.div(value: Decimal): Decimal = decimalNumberByDividingBy(value)

actual fun Decimal.div(value: Decimal, scale: Int): Decimal =
    decimalNumberByDividingBy(value, object : NSDecimalNumberHandler() {
        override fun scale(): Short = scale.toShort()
    })

actual fun Decimal.div(value: Decimal, scale: Int, roundingMode: Int): Decimal =
    decimalNumberByDividingBy(value, object : NSDecimalNumberHandler() {
        override fun scale(): Short = scale.toShort()
        override fun roundingMode(): NSRoundingMode = NSRoundingMode.byValue(roundingMode.toULong())
    })

actual operator fun Decimal.times(value: Decimal): Decimal = decimalNumberByMultiplyingBy(value)

actual fun Decimal.times(value: Decimal, scale: Int): Decimal =
    decimalNumberByMultiplyingBy(value, object : NSDecimalNumberHandler() {
        override fun scale(): Short = scale.toShort()
    })

actual fun Decimal.times(value: Decimal, scale: Int, roundingMode: Int): Decimal =
    decimalNumberByMultiplyingBy(value, object : NSDecimalNumberHandler() {
        override fun scale(): Short = scale.toShort()
        override fun roundingMode(): NSRoundingMode = NSRoundingMode.byValue(roundingMode.toULong())
    })

actual object Decimals {

    actual fun Double.toDecimal() = NSDecimalNumber(this)
    actual fun String.toDecimal() = NSDecimalNumber(this)

    actual fun Decimal.toDouble(): Double = this.doubleValue
    actual fun Decimal.toString(): String = this.stringValue

    actual val ROUND_DOWN: Int
        get() = NSRoundingMode.NSRoundDown.ordinal
    actual val ROUND_HALF_EVEN: Int
        get() = NSRoundingMode.NSRoundBankers.ordinal
    actual val ROUND_UP: Int
        get() = NSRoundingMode.NSRoundUp.ordinal
}
