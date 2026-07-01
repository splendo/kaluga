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
package com.splendo.kaluga.base.formatting

import com.splendo.kaluga.base.i18n.KalugaLocale
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException
import java.util.Currency

/**
 * Default implementation of [BaseNumberFormatter]
 * @param locale The [KalugaLocale] used for parsing. Defaults to [KalugaLocale.defaultLocale].
 * @param style The [NumberFormatStyle] to configure the format to use. Defaults to [NumberFormatStyle.Decimal].
 */
actual class NumberFormatter actual constructor(actual override val locale: KalugaLocale, style: NumberFormatStyle) : BaseNumberFormatter {

    private val format: DecimalFormat = when (style) {
        is NumberFormatStyle.Integer -> DecimalFormat.getInstance(locale.locale).apply {
            minimumIntegerDigits = style.minDigits.toInt()
            maximumIntegerDigits = style.maxDigits.toInt()
            minimumFractionDigits = 0
            maximumFractionDigits = 0
        }

        is NumberFormatStyle.Decimal -> DecimalFormat.getInstance(locale.locale).apply {
            minimumIntegerDigits = style.minIntegerDigits.toInt()
            maximumIntegerDigits = style.maxIntegerDigits.toInt()
            minimumFractionDigits = style.minFractionDigits.toInt()
            maximumFractionDigits = style.maxFractionDigits.toInt()
        }

        is NumberFormatStyle.Percentage -> DecimalFormat.getPercentInstance(locale.locale).apply {
            minimumIntegerDigits = style.minIntegerDigits.toInt()
            maximumIntegerDigits = style.maxIntegerDigits.toInt()
            minimumFractionDigits = style.minFractionDigits.toInt()
            maximumFractionDigits = style.maxFractionDigits.toInt()
        }

        is NumberFormatStyle.Permillage -> {
            val pattern = (DecimalFormat.getPercentInstance(locale.locale) as DecimalFormat).toPattern().replace("%", "\u2030")
            DecimalFormat(pattern, DecimalFormatSymbols(locale.locale)).apply {
                minimumIntegerDigits = style.minIntegerDigits.toInt()
                maximumIntegerDigits = style.maxIntegerDigits.toInt()
                minimumFractionDigits = style.minFractionDigits.toInt()
                maximumFractionDigits = style.maxFractionDigits.toInt()
            }
        }

        is NumberFormatStyle.Scientific -> DecimalFormat(style.pattern, DecimalFormatSymbols(locale.locale))

        is NumberFormatStyle.Currency -> DecimalFormat.getCurrencyInstance(locale.locale).apply {
            style.currencyCode?.let { currencyCode ->
                currency = Currency.getInstance(currencyCode)
            }
            minimumIntegerDigits = style.minIntegerDigits.toInt()
            maximumIntegerDigits = style.maxIntegerDigits.toInt()
            minimumFractionDigits = style.minFractionDigits?.toInt() ?: currency?.defaultFractionDigits ?: 0
            maximumFractionDigits = style.maxFractionDigits?.toInt() ?: currency?.defaultFractionDigits ?: 0
        }

        is NumberFormatStyle.Pattern -> DecimalFormat("${style.positivePattern};${style.negativePattern}", DecimalFormatSymbols(locale.locale))
    } as DecimalFormat

    // When the Scientific style sets a decimal-notation threshold, values within the threshold are
    // rendered with this plain decimal formatter instead; its mutable state is synced from `format`
    // at format time so symbol/grouping overrides apply to both notations.
    private val decimalThresholdStyle: NumberFormatStyle.Scientific? =
        (style as? NumberFormatStyle.Scientific)?.takeIf { it.maxExponentForDecimalNotation != null }
    private val decimalFallback: DecimalFormat? = decimalThresholdStyle?.decimalNotation?.let { decimal ->
        (DecimalFormat.getInstance(locale.locale) as DecimalFormat).apply {
            minimumIntegerDigits = decimal.minIntegerDigits.toInt()
            maximumIntegerDigits = decimal.maxIntegerDigits.toInt()
            minimumFractionDigits = decimal.minFractionDigits.toInt()
            maximumFractionDigits = decimal.maxFractionDigits.toInt()
        }
    }

    private val symbols: DecimalFormatSymbols get() = format.decimalFormatSymbols
    init {
        val javaRounding = when (style.roundingMode) {
            RoundingMode.Ceiling -> java.math.RoundingMode.CEILING
            RoundingMode.Floor -> java.math.RoundingMode.FLOOR
            RoundingMode.HalfEven -> java.math.RoundingMode.HALF_EVEN
            RoundingMode.HalfUp -> java.math.RoundingMode.HALF_UP
            RoundingMode.HalfDown -> java.math.RoundingMode.HALF_DOWN
            RoundingMode.Down -> java.math.RoundingMode.DOWN
            RoundingMode.Up -> java.math.RoundingMode.UP
        }
        format.roundingMode = javaRounding
        decimalFallback?.roundingMode = javaRounding
    }

    actual override var percentSymbol: Char
        get() = symbols.percent
        set(value) {
            applySymbols { it.percent = value }
        }
    actual override var perMillSymbol: Char
        get() = symbols.perMill
        set(value) {
            applySymbols { it.perMill = value }
        }
    actual override var minusSign: Char
        get() = symbols.minusSign
        set(value) {
            applySymbols { it.minusSign = value }
        }
    actual override var exponentSymbol: String
        get() = symbols.exponentSeparator
        set(value) {
            applySymbols { it.exponentSeparator = value }
        }
    actual override var zeroSymbol: Char
        get() = symbols.zeroDigit
        set(value) {
            applySymbols { it.zeroDigit = value }
        }
    actual override var notANumberSymbol: String
        get() = symbols.naN
        set(value) {
            applySymbols { it.naN = value }
        }
    actual override var infinitySymbol: String
        get() = symbols.infinity
        set(value) {
            applySymbols { it.infinity = value }
        }

    actual override var currencySymbol: String
        get() = symbols.currencySymbol
        set(value) {
            applySymbols { it.currencySymbol = value }
        }
    actual override var currencyCode: String
        get() = symbols.currency.currencyCode
        set(value) {
            applySymbols { it.currency = Currency.getInstance(value) }
        }

    actual override var positivePrefix: String
        get() = format.positivePrefix
        set(value) {
            format.positivePrefix = value
        }
    actual override var positiveSuffix: String
        get() = format.positiveSuffix
        set(value) {
            format.positiveSuffix = value
        }
    actual override var negativePrefix: String
        get() = format.negativePrefix
        set(value) {
            format.negativePrefix = value
        }
    actual override var negativeSuffix: String
        get() = format.negativeSuffix
        set(value) {
            format.negativeSuffix = value
        }

    actual override var groupingSeparator: Char
        get() = symbols.groupingSeparator
        set(value) {
            applySymbols { it.groupingSeparator = value }
        }
    actual override var usesGroupingSeparator: Boolean
        // The decimal fallback owns grouping so it groups like a normal decimal by default; the toggle
        // drives both notations.
        get() = (decimalFallback ?: format).isGroupingUsed
        set(value) {
            format.isGroupingUsed = value
            decimalFallback?.isGroupingUsed = value
        }
    actual override var decimalSeparator: Char
        get() = symbols.decimalSeparator
        set(value) {
            applySymbols { it.decimalSeparator = value }
        }
    actual override var alwaysShowsDecimalSeparator: Boolean
        get() = format.isDecimalSeparatorAlwaysShown
        set(value) {
            format.isDecimalSeparatorAlwaysShown = value
        }
    actual override var currencyDecimalSeparator: Char
        get() = symbols.monetaryDecimalSeparator
        set(value) {
            applySymbols { it.monetaryDecimalSeparator = value }
        }
    actual override var groupingSize: Int
        // The decimal fallback owns its grouping size (the scientific pattern has none); the setter drives both.
        get() = (decimalFallback ?: format).groupingSize
        set(value) {
            format.groupingSize = value
            decimalFallback?.groupingSize = value
        }
    actual override var multiplier: Int
        get() = format.multiplier
        set(value) {
            format.multiplier = value
        }

    actual override fun format(number: Number): String {
        val value = number.toDouble()
        val fallback = decimalFallback
        val scientific = decimalThresholdStyle
        return if (fallback != null && scientific != null && scientific.rendersAsDecimal(value)) {
            // Sync everything except grouping (which the fallback owns — see usesGroupingSeparator/groupingSize —
            // so it renders like a normal localized decimal regardless of scientific's no-grouping default).
            fallback.decimalFormatSymbols = format.decimalFormatSymbols
            fallback.isDecimalSeparatorAlwaysShown = format.isDecimalSeparatorAlwaysShown
            fallback.multiplier = format.multiplier
            fallback.positivePrefix = format.positivePrefix
            fallback.positiveSuffix = format.positiveSuffix
            fallback.negativePrefix = format.negativePrefix
            fallback.negativeSuffix = format.negativeSuffix
            fallback.format(value)
        } else {
            format.format(value)
        }
    }
    actual override fun parse(string: String): Number? = try {
        format.parse(string)
    } catch (e: ParseException) {
        null
    }
    private fun applySymbols(apply: (DecimalFormatSymbols) -> Unit) {
        val symbols = format.decimalFormatSymbols
        apply(symbols)
        format.decimalFormatSymbols = symbols
    }
}
