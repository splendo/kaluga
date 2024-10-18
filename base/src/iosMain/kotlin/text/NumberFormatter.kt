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
import platform.Foundation.numberWithInt

/**
 * Default implementation of [BaseNumberFormatter]
 * @param locale The [KalugaLocale] used for parsing. Defaults to [KalugaLocale.defaultLocale].
 * @param style The [NumberFormatStyle] to configure the format to use. Defaults to [NumberFormatStyle.Decimal].
 */
actual class NumberFormatter actual constructor(actual override val locale: KalugaLocale, style: NumberFormatStyle) : BaseNumberFormatter {

    @ExperimentalUnsignedTypes
    private val formatter = NSNumberFormatter().apply {
        locale = this@NumberFormatter.locale.nsLocale
        when (style) {
            is NumberFormatStyle.Integer -> {
                numberStyle = NSNumberFormatterDecimalStyle
                minimumIntegerDigits = style.minDigits.toULong()
                maximumIntegerDigits = style.maxDigits.toULong()
                minimumFractionDigits = 0.toULong()
                maximumFractionDigits = 0.toULong()
            }
            is NumberFormatStyle.Decimal -> {
                numberStyle = NSNumberFormatterDecimalStyle
                minimumIntegerDigits = style.minIntegerDigits.toULong()
                maximumIntegerDigits = style.maxIntegerDigits.toULong()
                minimumFractionDigits = style.minFractionDigits.toULong()
                maximumFractionDigits = style.maxFractionDigits.toULong()
            }
            is NumberFormatStyle.Percentage -> {
                numberStyle = NSNumberFormatterPercentStyle
                minimumIntegerDigits = style.minIntegerDigits.toULong()
                maximumIntegerDigits = style.maxIntegerDigits.toULong()
                minimumFractionDigits = style.minFractionDigits.toULong()
                maximumFractionDigits = style.maxFractionDigits.toULong()
            }
            is NumberFormatStyle.Permillage -> {
                numberStyle = NSNumberFormatterPercentStyle
                positiveFormat = positiveFormat.replace('%', '‰')
                negativeFormat = negativeFormat.replace('%', '‰')
                minimumIntegerDigits = style.minIntegerDigits.toULong()
                maximumIntegerDigits = style.maxIntegerDigits.toULong()
                minimumFractionDigits = style.minFractionDigits.toULong()
                maximumFractionDigits = style.maxFractionDigits.toULong()
            }
            is NumberFormatStyle.Scientific -> {
                numberStyle = NSNumberFormatterScientificStyle
                positiveFormat = style.pattern
                negativeFormat = "-${style.pattern}"
            }
            is NumberFormatStyle.Currency -> {
                numberStyle = NSNumberFormatterCurrencyStyle
                style.currencyCode?.let { currencyCode ->
                    this.currencyCode = currencyCode
                }
                minimumIntegerDigits = style.minIntegerDigits.toULong()
                maximumIntegerDigits = style.maxIntegerDigits.toULong()
                style.minFractionDigits?.let {
                    minimumFractionDigits = it.toULong()
                }
                style.maxFractionDigits?.let {
                    maximumFractionDigits = it.toULong()
                }
            }
            is NumberFormatStyle.Pattern -> {
                numberStyle = NSNumberFormatterDecimalStyle
                positiveFormat = style.positivePattern
                negativeFormat = style.negativePattern
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

    actual override var percentSymbol: Char
        get() = formatter.percentSymbol.getOrNull(0) ?: Char.MIN_VALUE
        set(value) {
            formatter.percentSymbol = charArrayOf(value).concatToString()
        }
    actual override var perMillSymbol: Char
        get() = formatter.perMillSymbol.getOrNull(0) ?: Char.MIN_VALUE
        set(value) {
            formatter.perMillSymbol = charArrayOf(value).concatToString()
        }
    actual override var minusSign: Char
        get() = formatter.minusSign.getOrNull(0) ?: Char.MIN_VALUE
        set(value) {
            formatter.minusSign = charArrayOf(value).concatToString()
        }
    actual override var exponentSymbol: String
        get() = formatter.exponentSymbol
        set(value) {
            formatter.exponentSymbol = value
        }
    actual override var zeroSymbol: Char
        get() = formatter.zeroSymbol?.getOrNull(0) ?: '0'
        set(value) {
            formatter.zeroSymbol = charArrayOf(value).concatToString()
        }
    actual override var notANumberSymbol: String
        get() = formatter.notANumberSymbol
        set(value) {
            formatter.notANumberSymbol = value
        }
    actual override var infinitySymbol: String
        get() = formatter.positiveInfinitySymbol
        set(value) {
            formatter.positiveInfinitySymbol = value
            formatter.negativeInfinitySymbol = value
        }
    actual override var currencySymbol: String
        get() = formatter.currencySymbol
        set(value) {
            formatter.currencySymbol = value
        }
    actual override var currencyCode: String
        get() = formatter.currencyCode
        set(value) {
            formatter.currencyCode = value
        }
    actual override var positivePrefix: String
        get() = formatter.positivePrefix
        set(value) {
            formatter.positivePrefix = value
        }
    actual override var positiveSuffix: String
        get() = formatter.positiveSuffix
        set(value) {
            formatter.positiveSuffix = value
        }
    actual override var negativePrefix: String
        get() = formatter.negativePrefix
        set(value) {
            formatter.negativePrefix = value
        }
    actual override var negativeSuffix: String
        get() = formatter.negativeSuffix
        set(value) {
            formatter.negativeSuffix = value
        }
    actual override var groupingSeparator: Char
        get() = formatter.groupingSeparator.getOrNull(0) ?: Char.MIN_VALUE
        set(value) {
            val charValue = charArrayOf(value).concatToString()
            formatter.groupingSeparator = charValue
            formatter.currencyGroupingSeparator = charValue
        }
    actual override var usesGroupingSeparator: Boolean
        get() = formatter.usesGroupingSeparator
        set(value) {
            formatter.usesGroupingSeparator = value
        }
    actual override var decimalSeparator: Char
        get() = formatter.decimalSeparator.getOrNull(0) ?: Char.MIN_VALUE
        set(value) {
            formatter.decimalSeparator = charArrayOf(value).concatToString()
        }
    actual override var alwaysShowsDecimalSeparator: Boolean
        get() = formatter.alwaysShowsDecimalSeparator
        set(value) {
            formatter.alwaysShowsDecimalSeparator = value
        }
    actual override var currencyDecimalSeparator: Char
        get() = formatter.currencyDecimalSeparator.getOrNull(0) ?: Char.MIN_VALUE
        set(value) {
            formatter.currencyDecimalSeparator = charArrayOf(value).concatToString()
        }
    actual override var groupingSize: Int
        get() = formatter.groupingSize.toInt()
        set(value) {
            formatter.groupingSize = value.toULong()
            formatter.secondaryGroupingSize = value.toULong()
        }
    actual override var multiplier: Int
        get() = formatter.multiplier?.intValue ?: 1
        set(value) {
            formatter.multiplier = NSNumber.numberWithInt(value)
        }

    @Suppress("CAST_NEVER_SUCCEEDS") // Should succeed just fine
    actual override fun format(number: Number): String = (formatter.stringFromNumber(number as NSNumber) ?: "")

    @Suppress("CAST_NEVER_SUCCEEDS") // Should succeed just fine
    actual override fun parse(string: String): Number? = formatter.numberFromString(string) as? Number
}
