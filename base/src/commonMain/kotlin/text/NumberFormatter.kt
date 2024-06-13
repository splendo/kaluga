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

package com.splendo.kaluga.base.text

import com.splendo.kaluga.base.utils.KalugaLocale
import com.splendo.kaluga.base.utils.KalugaLocale.Companion.defaultLocale
import kotlin.math.max

/**
 * Rounding mode applied when rounding aa given [Number] while formatting
 */
enum class RoundingMode {
    /**
     * Round towards positive infinity.
     */
    Ceiling,

    /**
     * Round towards negative infinity.
     */
    Floor,

    /**
     * Round towards zero.
     */
    Down,

    /**
     * Round away from zero.
     */
    Up,

    /**
     * Round towards the nearest integer, or towards an even number if equidistant.
     */
    HalfEven,

    /**
     * Round towards the nearest integer, or towards zero if equidistant.
     */
    HalfDown,

    /**
     * Round towards the nearest integer, or away from zero if equidistant.
     */
    HalfUp,
}

@ExperimentalUnsignedTypes
/**
 * Style used for formatting a [Number] to and from a [String]
 */
sealed class NumberFormatStyle(open val roundingMode: RoundingMode) {

    /**
     * Formats a number to an integer representation
     * @param minDigits The minimum number of digits to show. Defaults to `0U`.
     * @param maxDigits The maximum number of digits to show. Defaults to `309U`.
     * @param roundingMode The [RoundingMode] to be applied. Defaults to [RoundingMode.HalfEven].
     */
    data class Integer(
        val minDigits: UInt = 0U,
        val maxDigits: UInt = 309U,
        override val roundingMode: RoundingMode = RoundingMode.HalfEven,
    ) : NumberFormatStyle(roundingMode)

    /**
     * Formats a number to a decimal representation
     * @param minIntegerDigits The minimum number of integer digits to show. Defaults to `0U`.
     * @param maxIntegerDigits The maximum number of integer digits to show. Defaults to `309U`.
     * @param minFractionDigits The minimum number of decimal digits to show. Defaults to `0U`.
     * @param maxFractionDigits The maximum number of decimal digits to show. Defaults to `325U`.
     * @param roundingMode The [RoundingMode] to be applied. Defaults to [RoundingMode.HalfEven].
     */
    data class Decimal(
        val minIntegerDigits: UInt = 0U,
        val maxIntegerDigits: UInt = 309U,
        val minFractionDigits: UInt = 0U,
        val maxFractionDigits: UInt = 325U,
        override val roundingMode: RoundingMode = RoundingMode.HalfEven,
    ) : NumberFormatStyle(roundingMode)

    /**
     * Formats a number to a percentage value.
     * 100% is represented by 1.0, so 0.8 will be formatted as 80%.
     * @param minIntegerDigits The minimum number of integer digits to show. Defaults to `0U`.
     * @param maxIntegerDigits The maximum number of integer digits to show. Defaults to `309U`.
     * @param minFractionDigits The minimum number of decimal digits to show. Defaults to `0U`.
     * @param maxFractionDigits The maximum number of decimal digits to show. Defaults to `325U`.
     * @param roundingMode The [RoundingMode] to be applied. Defaults to [RoundingMode.HalfEven].
     */
    data class Percentage(
        val minIntegerDigits: UInt = 0U,
        val maxIntegerDigits: UInt = 309U,
        val minFractionDigits: UInt = 0U,
        val maxFractionDigits: UInt = 325U,
        override val roundingMode: RoundingMode = RoundingMode.HalfEven,
    ) : NumberFormatStyle(roundingMode)

    /**
     * Formats a number to a permillage value.
     * 1000‰ is represented by 1.0, so 0.8 will be formatted as 800‰.
     * @param minIntegerDigits The minimum number of integer digits to show. Defaults to `0U`.
     * @param maxIntegerDigits The maximum number of integer digits to show. Defaults to `309U`.
     * @param minFractionDigits The minimum number of decimal digits to show. Defaults to `0U`.
     * @param maxFractionDigits The maximum number of decimal digits to show. Defaults to  `325U`.
     * @param roundingMode The [RoundingMode] to be applied. Defaults to [RoundingMode.HalfEven].
     */
    data class Permillage(
        val minIntegerDigits: UInt = 0U,
        val maxIntegerDigits: UInt = 309U,
        val minFractionDigits: UInt = 0U,
        val maxFractionDigits: UInt = 325U,
        override val roundingMode: RoundingMode = RoundingMode.HalfEven,
    ) : NumberFormatStyle(roundingMode)

    /**
     * Formats a number to its scientific notation.
     * @param minIntegerDigits The minimum number of integer digits to show in the mantissa. Defaults to `1U`.
     * @param maxIntegerDigits The maximum number of integer digits to show in the mantissa.
     * If this is greater than [minIntegerDigits], it forces the exponent to be a multiple of maximum number of integer values.
     * Otherwise the minimum number of integer digits is achieved by adjusting the exponent.
     * Defaults to [minIntegerDigits]
     * @param minFractionDigits The minimum number of decimal digits to show in the mantissa. Defaults to `1U`.
     * @param maxFractionDigits The maximum number of decimal digits to show in the mantissa. Defaults to `16U`.
     * @param minExponent The minimum number of digits to show in the exponent. Defaults to `1U`.
     * @param roundingMode The [RoundingMode] to be applied. Defaults to [RoundingMode.HalfEven].
     */
    data class Scientific(
        val minIntegerDigits: UInt = 1U,
        val maxIntegerDigits: UInt = minIntegerDigits,
        val minFractionDigits: UInt = 1U,
        val maxFractionDigits: UInt = 16U,
        val minExponent: UInt = 1U,
        override val roundingMode: RoundingMode = RoundingMode.HalfEven,
    ) : NumberFormatStyle(roundingMode) {
        val pattern: String get() {
            val mantissaInteger = "${"#".repeat(max(maxIntegerDigits.toInt() - minIntegerDigits.toInt(), 0))}${"0".repeat(max(minIntegerDigits.toInt(), 1))}"
            val mantissaDecimal = "${"0".repeat(minFractionDigits.toInt())}${"#".repeat(max(maxFractionDigits.toInt() - minFractionDigits.toInt(), 0))}"
            val mantissa = if (mantissaDecimal.isNotEmpty()) "$mantissaInteger.$mantissaDecimal" else mantissaInteger
            val exponent = "E${"0".repeat(if (minExponent > 0U) minExponent.toInt() else 0)}"
            return "$mantissa$exponent"
        }
    }

    /**
     * Formats a number to a currency of a given currency code fitting its locale.
     * Note: Some platforms may use different delimiters between the currency symbol and the number (if present for the locale).
     * For instance, iOS often uses a non-breaking space, whereas Android uses a common space.
     * This inconsistency is not corrected by this library and should thus be manually corrected if required.
     * @param currencyCode The currency code to format for. When `null` defaults to the currency of the locale Defaults to `null`.
     * @param minIntegerDigits The minimum number of integer digits to show. Defaults to `0U`.
     * @param maxIntegerDigits The maximum number of integer digits to show. Defaults to `309U`.
     * @param minFractionDigits The minimum number of decimal digits to show. Defaults to `0U`.
     * @param maxFractionDigits The maximum number of decimal digits to show. When `null` uses the preferred faction digits of the currency. Defaults to `null`.
     * @param roundingMode The [RoundingMode] to be applied. Defaults to [RoundingMode.HalfEven].
     */
    data class Currency(
        val currencyCode: String? = null,
        val minIntegerDigits: UInt = 0U,
        val maxIntegerDigits: UInt = 309U,
        val minFractionDigits: UInt? = null,
        val maxFractionDigits: UInt? = null,
        override val roundingMode: RoundingMode = RoundingMode.HalfEven,
    ) : NumberFormatStyle(roundingMode)

    /**
     * Formats a number to a pattern.
     * @param positivePattern The pattern to apply for positive numbers.
     * @param negativePattern The pattern to apply for negative numbers. Defaults to "-[positivePattern]".
     * @param roundingMode The [RoundingMode] to be applied. Defaults to [RoundingMode.HalfEven].
     */
    data class Pattern(
        val positivePattern: String,
        val negativePattern: String = "-$positivePattern",
        override val roundingMode: RoundingMode = RoundingMode.HalfEven,
    ) : NumberFormatStyle(roundingMode)
}

/**
 * Class for parsing and formatting a [Number] from/to a [String].
 */
interface BaseNumberFormatter {
    /**
     * [KalugaLocale] used for formatting.
     */
    val locale: KalugaLocale

    /**
     * The symbol used to represent a percent sign.
     */
    var percentSymbol: Char

    /**
     * The symbol used to represent a permille sign.
     */
    var perMillSymbol: Char

    /**
     * The symbol used to represent a minus sign.
     */
    var minusSign: Char

    /**
     * The symbol used to represent an exponent sign.
     */
    var exponentSymbol: String

    /**
     * The symbol used to represent a zero sign.
     */
    var zeroSymbol: Char

    /**
     * The text used to represent a `Not a Number` value.
     */
    var notANumberSymbol: String

    /**
     * The text used to represent infinity.
     */
    var infinitySymbol: String

    /**
     * The text used to represent the currency for the current locale.
     */
    var currencySymbol: String

    /**
     * The ISO 4217 currency code of this currency
     */
    var currencyCode: String

    /**
     * Text to be added in front of the number when positive
     */
    var positivePrefix: String

    /**
     * Text to be added behind the number when positive
     */
    var positiveSuffix: String

    /**
     * Text to be added in front of the number when negative
     */
    var negativePrefix: String

    /**
     * Text to be added behind the number when negative
     */
    var negativeSuffix: String

    /**
     * The symbol used to represent a grouping sign.
     */
    var groupingSeparator: Char

    /**
     * When set to `true` number grouping is applied
     */
    var usesGroupingSeparator: Boolean

    /**
     * The symbol used to represent a decimal separator.
     */
    var decimalSeparator: Char

    /**
     * When set to `true` always shows a decimal separator, even when no decimal will be shown.
     */
    var alwaysShowsDecimalSeparator: Boolean

    /**
     * The symbol used to represent a decimal separator when formatting currency.
     */
    var currencyDecimalSeparator: Char

    /**
     * The number of digits that will be grouped by [groupingSeparator].
     */
    var groupingSize: Int

    /**
     * The value a number will be multiplied with when formatted.
     */
    var multiplier: Int

    /**
     * Formats a [Number] into a [String].
     * @param number The [Number] to format.
     * @return The [String] representation of [number] according to this format.
     */
    fun format(number: Number): String

    /**
     * Tries to parse a [String] into a [Number]
     * @param string The [String] to parse.
     * @return A [Number] if the String could by parsed by this formatter, `null` otherwise.
     */
    fun parse(string: String): Number?
}

/**
 * Default implementation of [BaseNumberFormatter]
 * @param locale The [KalugaLocale] used for parsing. Defaults to [KalugaLocale.defaultLocale].
 * @param style The [NumberFormatStyle] to configure the format to use. Defaults to [NumberFormatStyle.Decimal].
 */
expect class NumberFormatter constructor(locale: KalugaLocale = defaultLocale, style: NumberFormatStyle = NumberFormatStyle.Decimal()) : BaseNumberFormatter {
    override val locale: KalugaLocale
    override var percentSymbol: Char
    override var perMillSymbol: Char
    override var minusSign: Char
    override var exponentSymbol: String
    override var zeroSymbol: Char
    override var notANumberSymbol: String
    override var infinitySymbol: String
    override var currencySymbol: String
    override var currencyCode: String
    override var positivePrefix: String
    override var positiveSuffix: String
    override var negativePrefix: String
    override var negativeSuffix: String
    override var groupingSeparator: Char
    override var usesGroupingSeparator: Boolean
    override var decimalSeparator: Char
    override var alwaysShowsDecimalSeparator: Boolean
    override var currencyDecimalSeparator: Char
    override var groupingSize: Int
    override var multiplier: Int

    override fun format(number: Number): String
    override fun parse(string: String): Number?
}
