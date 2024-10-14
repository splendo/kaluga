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

package com.splendo.kaluga.base.utils

import platform.Foundation.NSDecimalNumber
import platform.Foundation.NSDecimalNumberHandler
import platform.Foundation.NSRoundingMode
import kotlin.math.absoluteValue

/**
 * Platform specific representation of a finite immutable, arbitrary-precision signed decimal number
 * @property nsDecimal the [NSDecimalNumber] representing the finite decimal number
 */
actual data class FiniteDecimal(val nsDecimal: NSDecimalNumber) : Comparable<FiniteDecimal> {
    actual override fun compareTo(other: FiniteDecimal): Int = nsDecimal.compare(other.nsDecimal).toInt()
    override fun equals(other: Any?): Boolean = (other as? FiniteDecimal)?.let { nsDecimal.isEqualToNumber(it.nsDecimal) } ?: false
    override fun hashCode(): Int = nsDecimal.hashCode()
}

actual operator fun FiniteDecimal.plus(value: FiniteDecimal) = copy(nsDecimal = nsDecimal.decimalNumberByAdding(value.nsDecimal))

actual fun FiniteDecimal.plus(value: FiniteDecimal, scale: Int) = copy(
    nsDecimal = nsDecimal.decimalNumberByAdding(
        decimalNumber = value.nsDecimal,
        withBehavior = NSDecimalNumberHandler(
            roundingMode = NSRoundingMode.NSRoundPlain,
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true,
        ),
    ),
)

actual fun FiniteDecimal.plus(value: FiniteDecimal, scale: Int, roundingMode: RoundingMode) = copy(
    nsDecimal = nsDecimal.decimalNumberByAdding(
        decimalNumber = value.nsDecimal,
        withBehavior = NSDecimalNumberHandler(
            roundingMode = roundingMode.nsRoundingMode,
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true,
        ),
    ),
)

actual operator fun FiniteDecimal.minus(value: FiniteDecimal) = copy(nsDecimal = nsDecimal.decimalNumberBySubtracting(value.nsDecimal))

actual fun FiniteDecimal.minus(value: FiniteDecimal, scale: Int) = copy(
    nsDecimal = nsDecimal.decimalNumberBySubtracting(
        decimalNumber = value.nsDecimal,
        withBehavior = NSDecimalNumberHandler(
            roundingMode = NSRoundingMode.NSRoundPlain,
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true,
        ),
    ),
)

actual fun FiniteDecimal.minus(value: FiniteDecimal, scale: Int, roundingMode: RoundingMode) = copy(
    nsDecimal = nsDecimal.decimalNumberBySubtracting(
        decimalNumber = value.nsDecimal,
        withBehavior = NSDecimalNumberHandler(
            roundingMode = roundingMode.nsRoundingMode,
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true,
        ),
    ),
)

actual operator fun FiniteDecimal.div(value: FiniteDecimal) = copy(nsDecimal = nsDecimal.decimalNumberByDividingBy(value.nsDecimal))

actual fun FiniteDecimal.div(value: FiniteDecimal, scale: Int) = copy(
    nsDecimal = nsDecimal.decimalNumberByDividingBy(
        decimalNumber = value.nsDecimal,
        withBehavior = NSDecimalNumberHandler(
            NSRoundingMode.NSRoundBankers,
            scale.toShort(),
            raiseOnExactness = true,
            raiseOnOverflow = true,
            raiseOnUnderflow = true,
            raiseOnDivideByZero = true,
        ),
    ),
)

actual fun FiniteDecimal.div(value: FiniteDecimal, scale: Int, roundingMode: RoundingMode) = copy(
    nsDecimal = nsDecimal.decimalNumberByDividingBy(
        decimalNumber = value.nsDecimal,
        withBehavior = NSDecimalNumberHandler(
            roundingMode = roundingMode.nsRoundingMode,
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true,
        ),
    ),
)

actual operator fun FiniteDecimal.times(value: FiniteDecimal) = copy(nsDecimal = nsDecimal.decimalNumberByMultiplyingBy(value.nsDecimal))

actual fun FiniteDecimal.times(value: FiniteDecimal, scale: Int) = copy(
    nsDecimal = nsDecimal.decimalNumberByMultiplyingBy(
        decimalNumber = value.nsDecimal,
        NSDecimalNumberHandler(
            roundingMode = NSRoundingMode.NSRoundPlain,
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true,
        ),
    ),
)

actual fun FiniteDecimal.times(value: FiniteDecimal, scale: Int, roundingMode: RoundingMode) = copy(
    nsDecimal = nsDecimal.decimalNumberByMultiplyingBy(
        decimalNumber = value.nsDecimal,
        withBehavior = NSDecimalNumberHandler(
            roundingMode = roundingMode.nsRoundingMode,
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true,
        ),
    ),
)

actual infix fun FiniteDecimal.pow(n: Int): FiniteDecimal = if (n < 0) {
    1.toFiniteDecimal()!! / pow(n.absoluteValue)
} else {
    copy(nsDecimal = nsDecimal.decimalNumberByRaisingToPower(n.toULong()))
}

actual fun FiniteDecimal.pow(n: Int, scale: Int): FiniteDecimal = if (n < 0) {
    1.toFiniteDecimal()!! / pow(n.absoluteValue, scale)
} else {
    copy(
        nsDecimal = nsDecimal.decimalNumberByRaisingToPower(
            n.toULong(),
            NSDecimalNumberHandler(
                roundingMode = NSRoundingMode.NSRoundPlain,
                scale = scale.toShort(),
                raiseOnExactness = false,
                raiseOnOverflow = false,
                raiseOnUnderflow = false,
                raiseOnDivideByZero = true,
            ),
        ),
    )
}

actual fun FiniteDecimal.pow(n: Int, scale: Int, roundingMode: RoundingMode): FiniteDecimal = if (n < 0) {
    1.toFiniteDecimal()!! / pow(n.absoluteValue, scale, roundingMode)
} else {
    copy(
        nsDecimal = nsDecimal.decimalNumberByRaisingToPower(
            n.toULong(),
            withBehavior = NSDecimalNumberHandler(
                roundingMode = roundingMode.nsRoundingMode,
                scale = scale.toShort(),
                raiseOnExactness = false,
                raiseOnOverflow = false,
                raiseOnUnderflow = false,
                raiseOnDivideByZero = true,
            ),
        ),
    )
}

actual fun Number.toFiniteDecimal() = toString().toFiniteDecimal()
actual fun String.toFiniteDecimal(): FiniteDecimal? = when (val decimal = NSDecimalNumber(this)) {
    NSDecimalNumber.notANumber -> null
    else -> FiniteDecimal(decimal)
}

actual fun FiniteDecimal.toDouble() = nsDecimal.toString().toDouble()
actual fun FiniteDecimal.toInt() = nsDecimal.intValue
actual fun FiniteDecimal.toLong() = nsDecimal.longValue

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
actual fun FiniteDecimal.toString() = nsDecimal.stringValue

actual fun FiniteDecimal.round(scale: Int, roundingMode: RoundingMode) = copy(
    nsDecimal = nsDecimal.decimalNumberByRoundingAccordingToBehavior(
        NSDecimalNumberHandler(
            roundingMode = roundingMode.nsRoundingMode,
            scale = scale.toShort(),
            raiseOnExactness = false,
            raiseOnOverflow = false,
            raiseOnUnderflow = false,
            raiseOnDivideByZero = true,
        ),
    ),
)

private val RoundingMode.nsRoundingMode
    get() = when (this) {
        RoundingMode.RoundDown -> NSRoundingMode.NSRoundDown
        RoundingMode.RoundHalfEven -> NSRoundingMode.NSRoundBankers
        RoundingMode.RoundUp -> NSRoundingMode.NSRoundUp
    }
