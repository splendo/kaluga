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

package com.splendo.kaluga.resources

enum class RoundingMode {
    Ceiling,
    Floor,
    Down,
    Up,
    HalfEven,
    HalfDown,
    HalfUp
}

sealed class NumberFormatStyle(open val roundingMode: RoundingMode) {
    data class Integer(
        val minInteger: Int = 0,
        val maxInteger: Int = Int.MAX_VALUE,
        override val roundingMode: RoundingMode = RoundingMode.HalfEven
    ) : NumberFormatStyle(roundingMode)
    data class Decimal(
        val minInteger: Int = 0,
        val maxInteger: Int = Int.MAX_VALUE,
        val minFraction: Int = 0,
        val maxFraction: Int = Int.MAX_VALUE,
        override val roundingMode: RoundingMode = RoundingMode.HalfEven
    ) : NumberFormatStyle(roundingMode)
    data class Percentage(
        val minInteger: Int = 0,
        val maxInteger: Int = Int.MAX_VALUE,
        val minFraction: Int = 0,
        val maxFraction: Int = Int.MAX_VALUE,
        override val roundingMode: RoundingMode = RoundingMode.HalfEven
    ) : NumberFormatStyle(roundingMode)
    data class Scientific(
        val numberOfDigits: Int = 10,
        override val roundingMode: RoundingMode = RoundingMode.HalfEven
    ) : NumberFormatStyle(roundingMode) {
        val pattern: String = "0.${"#".repeat(numberOfDigits - 1)}E0"
    }
    data class Currency(
        val minInteger: Int = 0,
        val maxInteger: Int = Int.MAX_VALUE,
        val minFraction: Int = 0,
        val maxFraction: Int = Int.MAX_VALUE,
        override val roundingMode: RoundingMode = RoundingMode.HalfEven
    ) : NumberFormatStyle(roundingMode)
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

    fun format(number: Number): String
    fun parse(string: String): Number?
}
