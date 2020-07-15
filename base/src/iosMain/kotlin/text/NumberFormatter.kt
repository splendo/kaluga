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
import com.splendo.kaluga.logging.debug
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.Foundation.NSNumberFormatterDecimalStyle
import platform.Foundation.NSNumberFormatterPercentStyle
import platform.Foundation.NSNumberFormatterRoundCeiling
import platform.Foundation.NSNumberFormatterRoundDown
import platform.Foundation.NSNumberFormatterRoundFloor
import platform.Foundation.NSNumberFormatterRoundHalfDown
import platform.Foundation.NSNumberFormatterRoundHalfEven
import platform.Foundation.NSNumberFormatterRoundUp
import platform.Foundation.NSNumberFormatterScientificStyle
import platform.Foundation.numberWithDouble
import platform.darwin.NSUInteger

actual class NumberFormatter actual constructor(actual val locale: Locale, style: NumberFormatStyle) {

    @ExperimentalUnsignedTypes
    private val formatter = NSNumberFormatter().apply {
        locale = this@NumberFormatter.locale
        when (style) {
            is NumberFormatStyle.Integer -> {
                numberStyle = NSNumberFormatterDecimalStyle
                minimumIntegerDigits = style.minInteger.toULong() as NSUInteger
                maximumIntegerDigits = style.maxInteger.toULong() as NSUInteger
                minimumFractionDigits = 0.toULong() as NSUInteger
                maximumFractionDigits = 0.toULong() as NSUInteger
            }
            is NumberFormatStyle.Decimal -> {
                numberStyle = NSNumberFormatterDecimalStyle
                minimumIntegerDigits = style.minInteger.toULong() as NSUInteger
                maximumIntegerDigits = style.maxInteger.toULong() as NSUInteger
                minimumFractionDigits = style.minFraction.toULong() as NSUInteger
                maximumFractionDigits = style.maxFraction.toULong() as NSUInteger
            }
            is NumberFormatStyle.Percentage -> {
                numberStyle = NSNumberFormatterPercentStyle
                minimumIntegerDigits = style.minInteger.toULong() as NSUInteger
                maximumIntegerDigits = style.maxInteger.toULong() as NSUInteger
                minimumFractionDigits = style.minFraction.toULong() as NSUInteger
                maximumFractionDigits = style.maxFraction.toULong() as NSUInteger
            }
            is NumberFormatStyle.Scientific -> {
                numberStyle = NSNumberFormatterScientificStyle
                positiveFormat = style.pattern
            }
            is NumberFormatStyle.Currency -> {
                numberStyle = NSNumberFormatterCurrencyStyle
                minimumIntegerDigits = style.minInteger.toULong() as NSUInteger
                maximumIntegerDigits = style.maxInteger.toULong() as NSUInteger
                minimumFractionDigits = style.minFraction.toULong() as NSUInteger
                maximumFractionDigits = style.maxFraction.toULong() as NSUInteger
            }
        }
        usesSignificantDigits = false
        roundingMode = when (style.roundingMode) {
            RoundingMode.Ceiling -> NSNumberFormatterRoundCeiling
            RoundingMode.Floor -> NSNumberFormatterRoundFloor
            RoundingMode.HalfEven -> NSNumberFormatterRoundHalfEven
            RoundingMode.HalfUp -> NSNumberFormatterRoundFloor
            RoundingMode.HalfDown -> NSNumberFormatterRoundHalfDown
            RoundingMode.Down -> NSNumberFormatterRoundDown
            RoundingMode.Up -> NSNumberFormatterRoundUp
        }
    }

    actual var percentSymbol: Char
        get() = formatter.percentSymbol.getOrNull(0) ?: Char.MIN_VALUE
        set(value) { formatter.percentSymbol = String(charArrayOf(value)) }
    actual var perMillSymbol: Char
        get() = formatter.perMillSymbol.getOrNull(0) ?: Char.MIN_VALUE
        set(value) { formatter.perMillSymbol = String(charArrayOf(value)) }
    actual var minusSign: Char
        get() = formatter.minusSign.getOrNull(0) ?: Char.MIN_VALUE
        set(value) { formatter.minusSign = String(charArrayOf(value)) }
    actual var exponentSymbol: String
        get() = formatter.exponentSymbol
        set(value) { formatter.exponentSymbol = value }
    actual var zeroSymbol: Char
        get() = formatter.zeroSymbol?.getOrNull(0) ?: '0'
        set(value) { formatter.zeroSymbol = String(charArrayOf(value)) }
    actual var notANumberSymbol: String
        get() = formatter.notANumberSymbol
        set(value) { formatter.notANumberSymbol = value }
    actual var infinitySymbol: String
        get() = formatter.positiveInfinitySymbol
        set(value) {
            formatter.positiveInfinitySymbol = value
            formatter.negativeInfinitySymbol = value
        }
    actual var currencySymbol: String
        get() = formatter.currencySymbol
        set(value) { formatter.currencySymbol = value }
    actual var currencyCode: String
        get() = formatter.currencyCode
        set(value) { formatter.currencyCode = value }
    actual var internationalCurrencySymbol: String
        get() = formatter.internationalCurrencySymbol
        set(value) { formatter.internationalCurrencySymbol = value }
    actual var positivePrefix: String
        get() = formatter.positivePrefix
        set(value) { formatter.positivePrefix = value }
    actual var positiveSuffix: String
        get() = formatter.positiveSuffix
        set(value) { formatter.positiveSuffix = value }
    actual var negativePrefix: String
        get() = formatter.negativePrefix
        set(value) { formatter.negativePrefix = value }
    actual var negativeSuffix: String
        get() = formatter.negativeSuffix
        set(value) { formatter.negativeSuffix = value }
    actual var groupingSeparator: Char
        get() = formatter.groupingSeparator.getOrNull(0) ?: Char.MIN_VALUE
        set(value) {
            val charValue = String(charArrayOf(value))
            formatter.groupingSeparator = charValue
            formatter.currencyGroupingSeparator = charValue
        }
    actual var usesGroupingSeparator: Boolean
        get() = formatter.usesGroupingSeparator
        set(value) { formatter.usesGroupingSeparator = value }
    actual var decimalSeparator: Char
        get() = formatter.decimalSeparator.getOrNull(0) ?: Char.MIN_VALUE
        set(value) { formatter.decimalSeparator = String(charArrayOf(value)) }
    actual var alwaysShowsDecimalSeparator: Boolean
        get() = formatter.alwaysShowsDecimalSeparator
        set(value) { formatter.alwaysShowsDecimalSeparator = value }
    actual var currencyDecimalSeparator: Char
        get() = formatter.currencyDecimalSeparator.getOrNull(0) ?: Char.MIN_VALUE
        set(value) { formatter.currencyDecimalSeparator = String(charArrayOf(value)) }
    actual var groupingSize: Int
        get() = formatter.groupingSize.toInt()
        set(value) {
            formatter.groupingSize = value.toULong() as NSUInteger
            formatter.secondaryGroupingSize = value.toULong() as NSUInteger
        }

    actual fun format(number: Number): String {
        return (formatter.stringFromNumber(NSNumber.numberWithDouble(number.toDouble())) ?: "").also { debug(it) }
    }

    actual fun parse(string: String): Number? {
        return formatter.numberFromString(string) as? Number
    }
}
