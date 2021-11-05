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
import platform.darwin.NSUInteger
import kotlin.math.absoluteValue

actual data class Decimal(val nsDecimal: NSDecimalNumber) : Comparable<Decimal> {
    override fun compareTo(other: Decimal): Int = nsDecimal.compare(other.nsDecimal).toInt()
}

actual operator fun Decimal.plus(value: Decimal) = copy(nsDecimal = nsDecimal.decimalNumberByAdding(value.nsDecimal))

actual fun Decimal.plus(value: Decimal, scale: Int) = copy(
    nsDecimal = nsDecimal.decimalNumberByAdding(
        decimalNumber = value.nsDecimal,
        withBehavior = NSDecimalNumberHandler(
            roundingMode = NSRoundingMode.NSRoundPlain,
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true
        )
    )
)

actual fun Decimal.plus(value: Decimal, scale: Int, roundingMode: RoundingMode) = copy(
    nsDecimal = nsDecimal.decimalNumberByAdding(
        decimalNumber = value.nsDecimal,
        withBehavior = NSDecimalNumberHandler(
            roundingMode = roundingMode.nsRoundingMode,
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true
        )
    )
)

actual operator fun Decimal.minus(value: Decimal) = copy(nsDecimal = nsDecimal.decimalNumberBySubtracting(value.nsDecimal))

actual fun Decimal.minus(value: Decimal, scale: Int) = copy(
    nsDecimal = nsDecimal.decimalNumberBySubtracting(
        decimalNumber = value.nsDecimal,
        withBehavior = NSDecimalNumberHandler(
            roundingMode = NSRoundingMode.NSRoundPlain,
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true
        )
    )
)

actual fun Decimal.minus(value: Decimal, scale: Int, roundingMode: RoundingMode) = copy(
    nsDecimal = nsDecimal.decimalNumberBySubtracting(
        decimalNumber = value.nsDecimal,
        withBehavior = NSDecimalNumberHandler(
            roundingMode = roundingMode.nsRoundingMode,
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true
        )
    )
)

actual operator fun Decimal.div(value: Decimal) = copy(nsDecimal = nsDecimal.decimalNumberByDividingBy(value.nsDecimal))

actual fun Decimal.div(value: Decimal, scale: Int) = copy(
    nsDecimal = nsDecimal.decimalNumberByDividingBy(
        decimalNumber = value.nsDecimal,
        withBehavior = NSDecimalNumberHandler(
            NSRoundingMode.NSRoundBankers,
            scale.toShort(),
            raiseOnExactness = true,
            raiseOnOverflow = true,
            raiseOnUnderflow = true,
            raiseOnDivideByZero = true
        )
    )
)

actual fun Decimal.div(value: Decimal, scale: Int, roundingMode: RoundingMode) = copy(
    nsDecimal = nsDecimal.decimalNumberByDividingBy(
        decimalNumber = value.nsDecimal,
        withBehavior = NSDecimalNumberHandler(
            roundingMode = roundingMode.nsRoundingMode,
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true
        )
    )
)

actual operator fun Decimal.times(value: Decimal) = copy(nsDecimal = nsDecimal.decimalNumberByMultiplyingBy(value.nsDecimal))

actual fun Decimal.times(value: Decimal, scale: Int) = copy(
    nsDecimal = nsDecimal.decimalNumberByMultiplyingBy(
        decimalNumber = value.nsDecimal,
        NSDecimalNumberHandler(
            roundingMode = NSRoundingMode.NSRoundPlain,
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true
        )
    )
)

actual fun Decimal.times(value: Decimal, scale: Int, roundingMode: RoundingMode) = copy(
    nsDecimal = nsDecimal.decimalNumberByMultiplyingBy(
        decimalNumber = value.nsDecimal,
        withBehavior = NSDecimalNumberHandler(
            roundingMode = roundingMode.nsRoundingMode,
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true
        )
    )
)

actual infix fun Decimal.pow(n: Int): Decimal = if (n < 0)
    1.toDecimal() / pow(n.absoluteValue)
else
    copy(nsDecimal = nsDecimal.decimalNumberByRaisingToPower(n.toULong() as NSUInteger))

actual fun Decimal.pow(n: Int, scale: Int): Decimal = if (n < 0)
    1.toDecimal() / pow(n.absoluteValue, scale)
else
    copy(
        nsDecimal = nsDecimal.decimalNumberByRaisingToPower(
            n.toULong() as NSUInteger,
            NSDecimalNumberHandler(
                roundingMode = NSRoundingMode.NSRoundPlain,
                scale = scale.toShort(),
                raiseOnExactness = false,
                raiseOnOverflow = false,
                raiseOnUnderflow = false,
                raiseOnDivideByZero = true
            )
        )
    )
actual fun Decimal.pow(
    n: Int,
    scale: Int,
    roundingMode: RoundingMode
): Decimal = if (n < 0)
    1.toDecimal() / pow(n.absoluteValue, scale, roundingMode)
else
    copy(
        nsDecimal = nsDecimal.decimalNumberByRaisingToPower(
            n.toULong() as NSUInteger,
            withBehavior = NSDecimalNumberHandler(
                roundingMode = roundingMode.nsRoundingMode,
                scale = scale.toShort(),
                raiseOnExactness = false,
                raiseOnOverflow = false,
                raiseOnUnderflow = false,
                raiseOnDivideByZero = true
            )
        )
    )

actual fun Number.toDecimal() = Decimal(NSDecimalNumber(this.toDouble()))
actual fun String.toDecimal() = Decimal(NSDecimalNumber(this))

actual fun Decimal.toDouble() = nsDecimal.toString().toDouble()
actual fun Decimal.toInt() = nsDecimal.intValue
actual fun Decimal.toString() = nsDecimal.stringValue

actual fun Decimal.round(scale: Int, roundingMode: RoundingMode) = copy(
    nsDecimal = nsDecimal.decimalNumberByRoundingAccordingToBehavior(
        NSDecimalNumberHandler(
            roundingMode = roundingMode.nsRoundingMode,
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true
        )
    )
)

val RoundingMode.nsRoundingMode
    get() = when (this) {
        RoundingMode.RoundDown -> NSRoundingMode.NSRoundDown
        RoundingMode.RoundHalfEven -> NSRoundingMode.NSRoundBankers
        RoundingMode.RoundUp -> NSRoundingMode.NSRoundUp
    }
