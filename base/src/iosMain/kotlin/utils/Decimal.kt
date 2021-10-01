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

actual fun Decimal.plus(value: Decimal): Decimal = decimalNumberByAdding(value)
actual fun Decimal.plus(value: Decimal, scale: Int): Decimal =
    decimalNumberByAdding(value, object : NSDecimalNumberHandler() {
        override fun scale(): Short {
            return scale.toShort()
        }
    })
actual fun Decimal.plus(value: Decimal, scale: Int, roundingMode: Int): Decimal =
    decimalNumberByAdding(value, object : NSDecimalNumberHandler() {
        override fun scale(): Short {
            return scale.toShort()
        }

        override fun roundingMode(): NSRoundingMode {
            return NSRoundingMode.byValue(roundingMode.toULong())
        }
    })

actual fun Decimal.minus(value: Decimal): Decimal = decimalNumberBySubtracting(value)
actual fun Decimal.minus(value: Decimal, scale: Int): Decimal =
    decimalNumberBySubtracting(value, object : NSDecimalNumberHandler() {
        override fun scale(): Short {
            return scale.toShort()
        }
    })
actual fun Decimal.minus(value: Decimal, scale: Int, roundingMode: Int): Decimal =
    decimalNumberBySubtracting(value, object : NSDecimalNumberHandler() {
        override fun scale(): Short {
            return scale.toShort()
        }

        override fun roundingMode(): NSRoundingMode {
            return NSRoundingMode.byValue(roundingMode.toULong())
        }
    })

actual fun Decimal.divide(value: Decimal): Decimal = decimalNumberByDividingBy(value)
actual fun Decimal.divide(value: Decimal, scale: Int): Decimal =
    decimalNumberByDividingBy(value, object : NSDecimalNumberHandler() {
        override fun scale(): Short {
            return scale.toShort()
        }
    })
actual fun Decimal.divide(value: Decimal, scale: Int, roundingMode: Int): Decimal =
    decimalNumberByDividingBy(value, object : NSDecimalNumberHandler() {
        override fun scale(): Short {
            return scale.toShort()
        }

        override fun roundingMode(): NSRoundingMode {
            return NSRoundingMode.byValue(roundingMode.toULong())
        }
    })

actual fun Decimal.multiply(value: Decimal): Decimal = decimalNumberByMultiplyingBy(value)
actual fun Decimal.multiply(value: Decimal, scale: Int): Decimal =
    decimalNumberByMultiplyingBy(value, object : NSDecimalNumberHandler() {
        override fun scale(): Short {
            return scale.toShort()
        }
    })
actual fun Decimal.multiply(value: Decimal, scale: Int, roundingMode: Int): Decimal =
    decimalNumberByMultiplyingBy(value, object : NSDecimalNumberHandler() {
        override fun scale(): Short {
            return scale.toShort()
        }

        override fun roundingMode(): NSRoundingMode {
            return NSRoundingMode.byValue(roundingMode.toULong())
        }
    })

actual object Decimals {
    actual val ROUND_DOWN: Int
        get() = NSRoundingMode.NSRoundDown.ordinal
    actual val ROUND_HALF_EVEN: Int
        get() = NSRoundingMode.NSRoundBankers.ordinal
    actual val ROUND_UP: Int
        get() = NSRoundingMode.NSRoundUp.ordinal
}
