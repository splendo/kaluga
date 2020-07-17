/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.base.text

import com.splendo.kaluga.base.utils.Locale
import com.splendo.kaluga.base.utils.Locale.Companion.defaultLocale

enum class RoundingMode {
    Ceiling,
    Floor,
    Down,
    Up,
    HalfEven,
    HalfDown,
    HalfUp
}

@ExperimentalUnsignedTypes
sealed class NumberFormatStyle(open val roundingMode: RoundingMode) {
    data class Integer(
        val minInteger: UInt = 0U,
        val maxInteger: UInt = Int.MAX_VALUE.toUInt(),
        override val roundingMode: RoundingMode = RoundingMode.HalfEven
    ) : NumberFormatStyle(roundingMode)
    data class Decimal(
        val minInteger: UInt = 0U,
        val maxInteger: UInt = Int.MAX_VALUE.toUInt(),
        val minFraction: UInt = 0U,
        val maxFraction: UInt = Int.MAX_VALUE.toUInt(),
        override val roundingMode: RoundingMode = RoundingMode.HalfEven
    ) : NumberFormatStyle(roundingMode)
    data class Percentage(
        val minInteger: UInt = 0U,
        val maxInteger: UInt = Int.MAX_VALUE.toUInt(),
        val minFraction: UInt = 0U,
        val maxFraction: UInt = Int.MAX_VALUE.toUInt(),
        override val roundingMode: RoundingMode = RoundingMode.HalfEven
    ) : NumberFormatStyle(roundingMode)
    data class Permillage(
        val minInteger: UInt = 0U,
        val maxInteger: UInt = Int.MAX_VALUE.toUInt(),
        val minFraction: UInt = 0U,
        val maxFraction: UInt = Int.MAX_VALUE.toUInt(),
        override val roundingMode: RoundingMode = RoundingMode.HalfEven
    ) : NumberFormatStyle(roundingMode)
    data class Scientific(
        val numberOfDigits: UInt = 10U,
        val minExponent: UInt = 1U,
        override val roundingMode: RoundingMode = RoundingMode.HalfEven
    ) : NumberFormatStyle(roundingMode) {
        val pattern: String get() {
            val mantissa = if (numberOfDigits > 1U) "0.${"0".repeat(numberOfDigits.toInt() - 1)}" else "0"
            val exponent = "E${"0".repeat(if (minExponent > 0U) minExponent.toInt() else 0)}"
            return "$mantissa$exponent"
        }
    }
    data class Currency(
        val minInteger: UInt = 0U,
        val maxInteger: UInt = Int.MAX_VALUE.toUInt(),
        val minFraction: UInt = 0U,
        val maxFraction: UInt = Int.MAX_VALUE.toUInt(),
        override val roundingMode: RoundingMode = RoundingMode.HalfEven
    ) : NumberFormatStyle(roundingMode)
    data class Pattern(val positivePattern: String, val negativePattern: String = "-$positivePattern", override val roundingMode: RoundingMode = RoundingMode.HalfEven) : NumberFormatStyle(roundingMode)
}

expect class NumberFormatter constructor(locale: Locale = defaultLocale, style: NumberFormatStyle = NumberFormatStyle.Decimal()) {
    val locale: Locale

    var percentSymbol: Char
    var perMillSymbol: Char
    var minusSign: Char
    var exponentSymbol: String
    var zeroSymbol: Char
    var notANumberSymbol: String
    var infinitySymbol: String

    var currencySymbol: String
    var currencyCode: String
    var internationalCurrencySymbol: String

    var positivePrefix: String
    var positiveSuffix: String
    var negativePrefix: String
    var negativeSuffix: String

    var groupingSeparator: Char
    var usesGroupingSeparator: Boolean
    var decimalSeparator: Char
    var alwaysShowsDecimalSeparator: Boolean
    var currencyDecimalSeparator: Char
    var groupingSize: Int
    var multiplier: Int

    fun format(number: Number): String
    fun parse(string: String): Number?
}
