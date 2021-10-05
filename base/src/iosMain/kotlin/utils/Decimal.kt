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

import kotlinx.cinterop.convert
import platform.Foundation.NSDecimalNumber
import platform.Foundation.NSDecimalNumberHandler
import platform.Foundation.NSRoundingMode

@Suppress("CONFLICTING_OVERLOADS")
actual typealias Decimal = NSDecimalNumber

actual operator fun Decimal.plus(value: Decimal) = decimalNumberByAdding(value)

actual fun Decimal.plus(value: Decimal, scale: Int) =
    decimalNumberByAdding(
        decimalNumber = value,
        withBehavior = NSDecimalNumberHandler(
            roundingMode = NSRoundingMode.NSRoundPlain,
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true
        )
    )

actual fun Decimal.plus(value: Decimal, scale: Int, roundingMode: RoundingMode) =
    decimalNumberByAdding(
        decimalNumber = value,
        withBehavior = NSDecimalNumberHandler(
            roundingMode = toNSRoundingMode(roundingMode),
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true
        )
    )

actual operator fun Decimal.minus(value: Decimal) = decimalNumberBySubtracting(value)

actual fun Decimal.minus(value: Decimal, scale: Int) =
    decimalNumberBySubtracting(
        decimalNumber = value,
        withBehavior = NSDecimalNumberHandler(
            roundingMode = NSRoundingMode.NSRoundPlain,
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true
        )
    )

actual fun Decimal.minus(value: Decimal, scale: Int, roundingMode: RoundingMode) =
    decimalNumberBySubtracting(
        decimalNumber = value,
        withBehavior = NSDecimalNumberHandler(
            roundingMode = toNSRoundingMode(roundingMode),
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true
        )
    )

actual operator fun Decimal.div(value: Decimal) = decimalNumberByDividingBy(value)

actual fun Decimal.div(value: Decimal, scale: Int) =
    decimalNumberByDividingBy(
        decimalNumber = value,
        withBehavior = NSDecimalNumberHandler(
            roundingMode = NSRoundingMode.NSRoundPlain,
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true
        )
    )

actual fun Decimal.div(value: Decimal, scale: Int, roundingMode: RoundingMode) =
    decimalNumberByDividingBy(
        decimalNumber = value,
        withBehavior = NSDecimalNumberHandler(
            roundingMode = toNSRoundingMode(roundingMode),
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true
        )
    )

actual operator fun Decimal.times(value: Decimal) = decimalNumberByMultiplyingBy(value)

actual fun Decimal.times(value: Decimal, scale: Int) =
    decimalNumberByMultiplyingBy(
        decimalNumber = value,
        NSDecimalNumberHandler(
            roundingMode = NSRoundingMode.NSRoundPlain,
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true
        )
    )

actual fun Decimal.times(value: Decimal, scale: Int, roundingMode: RoundingMode) =
    decimalNumberByMultiplyingBy(
        decimalNumber = value,
        withBehavior = NSDecimalNumberHandler(
            roundingMode = toNSRoundingMode(roundingMode),
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true
        )
    )

actual fun Double.toDecimal() = NSDecimalNumber(this)
actual fun Int.toDecimal() = NSDecimalNumber(this)
actual fun String.toDecimal() = NSDecimalNumber(this)

actual fun Decimal.toDouble() = this.doubleValue
actual fun Decimal.toInt() = this.intValue
actual fun Decimal.toString() = this.stringValue

actual fun Decimal.round(scale: Int, roundingMode: RoundingMode) = decimalNumberByRoundingAccordingToBehavior(
    NSDecimalNumberHandler(
        roundingMode = toNSRoundingMode(roundingMode),
        scale = scale.toShort(),
        raiseOnExactness = false,
        raiseOnOverflow = false,
        raiseOnUnderflow = false,
        raiseOnDivideByZero = true
    )
)

actual fun RoundingMode.toNativeRoundCode() =
    when (this) {
        RoundingMode.RoundDown -> NSRoundingMode.NSRoundDown.ordinal
        RoundingMode.RoundHalfEven -> NSRoundingMode.NSRoundBankers.ordinal
        RoundingMode.RoundUp -> NSRoundingMode.NSRoundUp.ordinal
    }

private fun toNSRoundingMode(roundingMode: RoundingMode) =
    NSRoundingMode.byValue(roundingMode.toNativeRoundCode().convert())
