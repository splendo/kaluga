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
import com.splendo.kaluga.base.utils.newNumberFormat
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

/**
 * Default implementation of [BaseNumberFormatter] backed by the ECMAScript
 * [`Intl.NumberFormat`](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Intl/NumberFormat)
 * API. Modern Node.js and browsers ship with full ICU data, giving feature parity with
 * `java.text.DecimalFormat` (Android/JVM) and `NSNumberFormatter` (iOS).
 *
 * Pattern, scientific and permillage styles aren't directly representable in `Intl.NumberFormat`
 * — they're handled by composing native locale data (separators, currency symbols) with manual
 * formatting/parsing.
 *
 * @param locale The [KalugaLocale] used for parsing. Defaults to [KalugaLocale.defaultLocale].
 * @param style The [NumberFormatStyle] to configure the format to use. Defaults to [NumberFormatStyle.Decimal].
 */
actual class NumberFormatter actual constructor(actual override val locale: KalugaLocale, private val style: NumberFormatStyle) : BaseNumberFormatter {

    private val intlOpts: dynamic = buildBaseOptions(style)
    private val isPercent = style is NumberFormatStyle.Percentage
    private val isPermillage = style is NumberFormatStyle.Permillage
    private val isCurrency = style is NumberFormatStyle.Currency
    private val isScientific = style is NumberFormatStyle.Scientific
    private val patternStyle: NumberFormatStyle.Pattern? = style as? NumberFormatStyle.Pattern

    private val localeTag: String = locale.tag

    private val defaultMultiplier: Int = when (style) {
        is NumberFormatStyle.Percentage -> 100
        is NumberFormatStyle.Permillage -> 1000
        else -> 1
    }

    private val requestedMinIntegerDigits: Int = when (style) {
        is NumberFormatStyle.Integer -> style.minDigits.toInt()
        is NumberFormatStyle.Decimal -> style.minIntegerDigits.toInt()
        is NumberFormatStyle.Percentage -> style.minIntegerDigits.toInt()
        is NumberFormatStyle.Permillage -> style.minIntegerDigits.toInt()
        is NumberFormatStyle.Currency -> style.minIntegerDigits.toInt()
        else -> 1
    }

    private var _multiplier: Int = defaultMultiplier
    private val symbolOverrides = HashMap<String, String>()

    private val localeSeparators: LocaleSeparators by lazy { resolveLocaleSeparators(localeTag) }

    init {
        if (isCurrency) {
            val styleCurrency = (style as NumberFormatStyle.Currency).currencyCode
            intlOpts.currency = styleCurrency ?: defaultCurrencyForLocale(locale)
        }
        if (locale.variantCode.equals("POSIX", ignoreCase = true)) {
            intlOpts.useGrouping = false
        }
    }

    private fun makeFormatter(): dynamic = newNumberFormat(localeTag, intlOpts)

    private fun partsFor(value: Double): dynamic = makeFormatter().formatToParts(value)

    private fun rawFormat(value: Double): String {
        val raw = makeFormatter().format(value).unsafeCast<String>()
        val withOverrides = applySymbolOverrides(raw)
        return stripLeadingZeroIfRequested(withOverrides, value)
    }

    private fun stripLeadingZeroIfRequested(formatted: String, value: Double): String {
        if (requestedMinIntegerDigits > 0) return formatted
        if (abs(value) >= 1.0) return formatted
        val zero = localeSeparators.zero
        // Strip a single leading zero before the decimal separator, preserving any sign.
        val signChar = formatted.firstOrNull()?.takeIf { it == '-' || it == '+' || it == localeSeparators.minusSign }
        val body = if (signChar != null) formatted.substring(1) else formatted
        return if (body.startsWith(zero) && body.length > 1 && body[1] == localeSeparators.decimal) {
            (signChar?.toString() ?: "") + body.substring(1)
        } else {
            formatted
        }
    }

    private fun applySymbolOverrides(raw: String): String {
        if (symbolOverrides.isEmpty()) return raw
        val parts = partsFor(-12345.678)
        var result = raw
        for ((type, override) in symbolOverrides) {
            val original = extractPart(parts, type) ?: continue
            if (original != override) result = result.replace(original, override)
        }
        return result
    }

    actual override fun format(number: Number): String {
        val raw = number.toDouble()
        val effective = if (_multiplier != defaultMultiplier) raw * (_multiplier.toDouble() / defaultMultiplier) else raw
        return when {
            patternStyle != null -> formatWithPattern(effective)
            isScientific -> formatScientific(effective, style as NumberFormatStyle.Scientific)
            isPermillage -> formatPermillage(effective)
            else -> rawFormat(effective)
        }
    }

    actual override fun parse(string: String): Number? {
        val normalized = normalizeForParsing(string) ?: return null
        val asDouble = normalized.toDoubleOrNull() ?: return null
        val withDefaultMultiplier = if (defaultMultiplier != 1) asDouble / defaultMultiplier.toDouble() else asDouble
        val effective = if (_multiplier != defaultMultiplier && _multiplier != 0) {
            withDefaultMultiplier * (defaultMultiplier.toDouble() / _multiplier)
        } else {
            withDefaultMultiplier
        }
        return narrowNumeric(effective)
    }

    private fun normalizeForParsing(input: String): String? {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return null

        patternStyle?.let { ps ->
            val positiveBody = stripPatternAffixes(trimmed, ps.positivePattern, sign = "")
            if (positiveBody != null) return normalizeNumericString(positiveBody, negative = false)
            val negativeBody = stripPatternAffixes(trimmed, ps.negativePattern, sign = "-")
            if (negativeBody != null) return normalizeNumericString(negativeBody, negative = true)
            return null
        }

        var body = trimmed
        val minus = minusSign
        val negative = body.startsWith(minus.toString()) || body.startsWith("-")
        if (negative) body = body.trimStart(minus, '-')
        if (isCurrency) body = body.replace(currencySymbol, "").trim()
        if (isPercent) body = body.replace(percentSymbol.toString(), "").trim()
        if (isPermillage) body = body.replace(perMillSymbol.toString(), "").trim()
        return normalizeNumericString(body, negative)
    }

    private fun stripPatternAffixes(input: String, pattern: String, sign: String): String? {
        val (prefix, suffix) = extractPatternAffixes(pattern)
        val expectedPrefix = if (sign == "-" && !prefix.startsWith("-")) "-$prefix" else prefix
        if (!input.startsWith(expectedPrefix) || !input.endsWith(suffix)) return null
        val body = input.removePrefix(expectedPrefix)
        return body.removeSuffix(suffix)
    }

    private fun normalizeNumericString(body: String, negative: Boolean): String? {
        val grouping = localeSeparators.grouping
        val decimal = localeSeparators.decimal
        // Replace grouping with empty, decimal with '.'. Both must use locale-aware chars
        // so that we don't accidentally strip a decimal separator that doubles as grouping (e.g. nl-NL).
        val sb = StringBuilder()
        for (ch in body) {
            when (ch) {
                grouping -> { /* skip */ }
                decimal -> sb.append('.')
                else -> sb.append(ch)
            }
        }
        val asPlain = sb.toString().trim()
        if (asPlain.isEmpty()) return null
        val ok = asPlain.all { it.isDigit() || it == '.' || it == 'e' || it == 'E' || it == '+' || it == '-' }
        if (!ok) return null
        return if (negative) "-$asPlain" else asPlain
    }

    private fun narrowNumeric(value: Double): Number = if (value.isFinite() && value == value.toLong().toDouble()) {
        value.toLong()
    } else {
        value
    }

    // region Pattern formatting

    private fun formatWithPattern(value: Double): String {
        val ps = patternStyle ?: return value.toString()
        val pattern = if (value < 0) ps.negativePattern else ps.positivePattern
        val (prefix, suffix) = extractPatternAffixes(pattern)
        val body = extractPatternBody(pattern)
        val magnitude = abs(value)
        val grouping = body.contains(',')
        val (minFrac, maxFrac) = patternFractionRange(body)
        val minInt = patternMinIntegerDigits(body)
        val numericFormatter = numericFormatterFor(minInt, minFrac, maxFrac, grouping)
        val numericPart = numericFormatter(magnitude)
        return prefix + numericPart + suffix
    }

    private fun numericFormatterFor(minIntDigits: Int, minFrac: Int, maxFrac: Int, grouping: Boolean): (Double) -> String {
        val opts = js("({})")
        opts.minimumIntegerDigits = if (minIntDigits > 0) minIntDigits else 1
        opts.minimumFractionDigits = minFrac
        opts.maximumFractionDigits = maxFrac
        opts.useGrouping = grouping
        val formatter = newNumberFormat(localeTag, opts)
        return { v -> formatter.format(v).unsafeCast<String>() }
    }

    /**
     * Walks the pattern keeping format chars (`0`, `#`, `.`, `,`, `E`, `e`) and treating everything
     * else — including the contents of quoted literals — as affix text. Returns (prefix, suffix).
     * Anything appearing after the first format char ends up in the suffix.
     */
    private fun extractPatternAffixes(pattern: String): Pair<String, String> {
        val prefix = StringBuilder()
        val suffix = StringBuilder()
        var inBody = false
        var i = 0
        while (i < pattern.length) {
            val c = pattern[i]
            if (c == '\'') {
                val end = pattern.indexOf('\'', i + 1)
                val literal = if (end < 0) pattern.substring(i + 1) else pattern.substring(i + 1, end)
                val target = if (inBody) suffix else prefix
                if (literal.isEmpty()) {
                    target.append('\'') // Escaped quote ('').
                } else {
                    target.append(literal)
                }
                i = if (end < 0) pattern.length else end + 1
            } else if (c in formatChars) {
                inBody = true
                i++
            } else {
                (if (inBody) suffix else prefix).append(c)
                i++
            }
        }
        return prefix.toString() to suffix.toString()
    }

    /** Returns the contiguous run of format chars (`0`, `#`, `.`, `,`, `E`/`e`) from [pattern]. */
    private fun extractPatternBody(pattern: String): String {
        val sb = StringBuilder()
        var i = 0
        while (i < pattern.length) {
            val c = pattern[i]
            if (c == '\'') {
                val end = pattern.indexOf('\'', i + 1)
                i = if (end < 0) pattern.length else end + 1
            } else {
                if (c in formatChars) sb.append(c)
                i++
            }
        }
        return sb.toString()
    }

    private fun patternFractionRange(body: String): Pair<Int, Int> {
        val dot = body.indexOf('.')
        if (dot < 0) return 0 to 0
        val fractionPart = body.substring(dot + 1).takeWhile { it == '0' || it == '#' }
        val min = fractionPart.count { it == '0' }
        val max = fractionPart.length
        return min to max
    }

    private fun patternMinIntegerDigits(body: String): Int {
        val dot = body.indexOf('.')
        val integerPart = (if (dot < 0) body else body.substring(0, dot))
            .replace(",", "")
            .takeLastWhile { it == '0' || it == '#' }
        return integerPart.count { it == '0' }
    }

    // endregion

    // region Scientific formatting

    private fun formatScientific(value: Double, style: NumberFormatStyle.Scientific): String {
        if (value == 0.0 || !value.isFinite()) {
            return rawFormat(value)
        }
        val negative = value < 0
        val absValue = abs(value)
        val rawExp = floor(log10(absValue)).toInt()
        val mantissaIntDigits = style.minIntegerDigits.toInt().coerceAtLeast(1)
        val engineering = style.maxIntegerDigits > style.minIntegerDigits
        val exponent = if (engineering) {
            val step = style.maxIntegerDigits.toInt().coerceAtLeast(1)
            (rawExp / step) * step - (if (rawExp < 0 && rawExp % step != 0) step else 0)
        } else {
            rawExp - (mantissaIntDigits - 1)
        }
        val mantissa = absValue / 10.0.pow(exponent)
        val mantissaStr = rawFormatWithDigits(
            mantissa,
            minInt = mantissaIntDigits,
            minFrac = style.minFractionDigits.toInt(),
            maxFrac = style.maxFractionDigits.toInt(),
        )
        val sign = if (negative) minusSign.toString() else ""
        val expSign = if (exponent < 0) minusSign.toString() else ""
        val expDigits = abs(exponent).toString().padStart(style.minExponent.toInt(), '0')
        return "$sign$mantissaStr$exponentSymbol$expSign$expDigits"
    }

    private fun rawFormatWithDigits(value: Double, minInt: Int, minFrac: Int, maxFrac: Int, grouping: Boolean = false): String {
        val opts = js("({})")
        opts.minimumIntegerDigits = if (minInt > 0) minInt else 1
        opts.minimumFractionDigits = minFrac
        opts.maximumFractionDigits = maxFrac
        opts.useGrouping = grouping
        val formatter = newNumberFormat(localeTag, opts)
        return formatter.format(value).unsafeCast<String>()
    }

    // endregion

    // region Permillage formatting

    private fun formatPermillage(value: Double): String {
        val ps = style as NumberFormatStyle.Permillage
        // `value` has already been normalized by `format()` to [raw * _multiplier / defaultMultiplier];
        // re-apply defaultMultiplier (1000) so the rendered mantissa is raw * _multiplier.
        val scaled = value * defaultMultiplier
        val mantissaStr = rawFormatWithDigits(
            scaled,
            minInt = ps.minIntegerDigits.toInt().coerceAtLeast(1),
            minFrac = ps.minFractionDigits.toInt(),
            maxFrac = ps.maxFractionDigits.toInt(),
            grouping = usesGroupingSeparator,
        )
        return mantissaStr + perMillSymbol
    }

    // endregion

    // region Symbol properties

    actual override var percentSymbol: Char
        get() = symbolOverrides["percentSign"]?.firstOrNull() ?: localeSeparators.percentSign
        set(value) {
            symbolOverrides["percentSign"] = value.toString()
        }
    actual override var perMillSymbol: Char
        get() = symbolOverrides["perMill"]?.firstOrNull() ?: '‰'
        set(value) {
            symbolOverrides["perMill"] = value.toString()
        }
    actual override var minusSign: Char
        get() = symbolOverrides["minusSign"]?.firstOrNull() ?: localeSeparators.minusSign
        set(value) {
            symbolOverrides["minusSign"] = value.toString()
        }
    actual override var exponentSymbol: String
        get() = symbolOverrides["exponent"] ?: "E"
        set(value) {
            symbolOverrides["exponent"] = value
        }
    actual override var zeroSymbol: Char
        get() = symbolOverrides["zero"]?.firstOrNull() ?: localeSeparators.zero
        set(value) {
            symbolOverrides["zero"] = value.toString()
        }
    actual override var notANumberSymbol: String
        get() = symbolOverrides["nan"] ?: extractPart(partsFor(Double.NaN), "nan") ?: "NaN"
        set(value) {
            symbolOverrides["nan"] = value
        }
    actual override var infinitySymbol: String
        get() = symbolOverrides["infinity"] ?: extractPart(partsFor(Double.POSITIVE_INFINITY), "infinity") ?: "∞"
        set(value) {
            symbolOverrides["infinity"] = value
        }
    actual override var currencySymbol: String
        get() {
            symbolOverrides["currency"]?.let { return it }
            if (!isCurrency) return ""
            return extractPart(partsFor(1.0), "currency") ?: currencyCode
        }
        set(value) {
            symbolOverrides["currency"] = value
        }
    actual override var currencyCode: String
        get() = (intlOpts.currency.unsafeCast<String?>()) ?: ""
        set(value) {
            intlOpts.currency = value
        }
    actual override var positivePrefix: String = ""
    actual override var positiveSuffix: String = ""
    actual override var negativePrefix: String
        get() = minusSign.toString()
        set(_) {
            // Not separately settable: change `minusSign` instead.
        }
    actual override var negativeSuffix: String = ""

    actual override var groupingSeparator: Char
        get() = symbolOverrides["group"]?.firstOrNull() ?: localeSeparators.grouping
        set(value) {
            symbolOverrides["group"] = value.toString()
        }
    actual override var usesGroupingSeparator: Boolean
        get() = intlOpts.useGrouping.unsafeCast<Boolean?>() ?: true
        set(value) {
            intlOpts.useGrouping = value
        }
    actual override var decimalSeparator: Char
        get() = symbolOverrides["decimal"]?.firstOrNull() ?: localeSeparators.decimal
        set(value) {
            symbolOverrides["decimal"] = value.toString()
        }
    actual override var alwaysShowsDecimalSeparator: Boolean = false
    actual override var currencyDecimalSeparator: Char
        get() = decimalSeparator
        set(value) {
            decimalSeparator = value
        }
    actual override var groupingSize: Int = 3
    actual override var multiplier: Int
        get() = _multiplier
        set(value) {
            _multiplier = value
        }

    // endregion

    private fun buildBaseOptions(style: NumberFormatStyle): dynamic {
        val opts = js("({})")
        when (style) {
            is NumberFormatStyle.Integer -> {
                opts.style = "decimal"
                opts.minimumIntegerDigits = clampMinIntegerDigits(style.minDigits.toInt())
                opts.minimumFractionDigits = 0
                opts.maximumFractionDigits = 0
                opts.useGrouping = false
                opts.roundingMode = intlRoundingMode(style.roundingMode)
            }

            is NumberFormatStyle.Decimal -> {
                opts.style = "decimal"
                opts.minimumIntegerDigits = clampMinIntegerDigits(style.minIntegerDigits.toInt())
                opts.minimumFractionDigits = style.minFractionDigits.toInt()
                opts.maximumFractionDigits = clampMaxFraction(style.maxFractionDigits.toInt())
                opts.useGrouping = true
                opts.roundingMode = intlRoundingMode(style.roundingMode)
            }

            is NumberFormatStyle.Percentage -> {
                opts.style = "percent"
                opts.minimumIntegerDigits = clampMinIntegerDigits(style.minIntegerDigits.toInt())
                opts.minimumFractionDigits = style.minFractionDigits.toInt()
                opts.maximumFractionDigits = clampMaxFraction(style.maxFractionDigits.toInt())
                opts.useGrouping = true
                opts.roundingMode = intlRoundingMode(style.roundingMode)
            }

            is NumberFormatStyle.Permillage -> {
                opts.style = "decimal"
                opts.minimumIntegerDigits = clampMinIntegerDigits(style.minIntegerDigits.toInt())
                opts.minimumFractionDigits = style.minFractionDigits.toInt()
                opts.maximumFractionDigits = clampMaxFraction(style.maxFractionDigits.toInt())
                opts.useGrouping = true
                opts.roundingMode = intlRoundingMode(style.roundingMode)
            }

            is NumberFormatStyle.Scientific -> {
                opts.style = "decimal"
                opts.useGrouping = false
                opts.roundingMode = intlRoundingMode(style.roundingMode)
            }

            is NumberFormatStyle.Currency -> {
                opts.style = "currency"
                opts.minimumIntegerDigits = clampMinIntegerDigits(style.minIntegerDigits.toInt())
                // Leave fraction-digit bounds unset so Intl picks the currency's natural digits.
                style.minFractionDigits?.toInt()?.let { opts.minimumFractionDigits = it }
                style.maxFractionDigits?.toInt()?.let { opts.maximumFractionDigits = it }
                opts.useGrouping = true
                opts.currencyDisplay = "symbol"
                opts.roundingMode = intlRoundingMode(style.roundingMode)
            }

            is NumberFormatStyle.Pattern -> {
                opts.style = "decimal"
                opts.useGrouping = false
                opts.roundingMode = intlRoundingMode(style.roundingMode)
            }
        }
        return opts
    }
}

/**
 * Locale-aware separators resolved from `Intl.NumberFormat`. Probing with `useGrouping: true`
 * guarantees the grouping separator part is present even for styles (Integer, Scientific) where
 * the active formatter has grouping disabled.
 */
private data class LocaleSeparators(val decimal: Char, val grouping: Char, val minusSign: Char, val zero: Char, val percentSign: Char)

private fun resolveLocaleSeparators(localeTag: String): LocaleSeparators {
    val numericFormatter = newNumberFormat(localeTag, js("({useGrouping: true, minimumFractionDigits: 1})"))
    val percentFormatter = newNumberFormat(localeTag, js("({style: 'percent'})"))
    val zeroFormatter = newNumberFormat(localeTag, js("({})"))
    val probe = numericFormatter.formatToParts(-12345.678)
    val percentProbe = percentFormatter.formatToParts(0.5)
    val zeroProbe = zeroFormatter.formatToParts(0)
    return LocaleSeparators(
        decimal = extractPart(probe, "decimal")?.firstOrNull() ?: '.',
        grouping = extractPart(probe, "group")?.firstOrNull() ?: ',',
        minusSign = extractPart(probe, "minusSign")?.firstOrNull() ?: '-',
        zero = extractPart(zeroProbe, "integer")?.firstOrNull() ?: '0',
        percentSign = extractPart(percentProbe, "percentSign")?.firstOrNull() ?: '%',
    )
}

private val formatChars = setOf('0', '#', '.', ',', 'E')

private fun clampMinIntegerDigits(value: Int): Int = value.coerceIn(1, 21)

private fun clampMaxFraction(value: Int): Int = value.coerceIn(0, 100)

private fun intlRoundingMode(mode: RoundingMode): String = when (mode) {
    RoundingMode.Ceiling -> "ceil"
    RoundingMode.Floor -> "floor"
    RoundingMode.Down -> "trunc"
    RoundingMode.Up -> "expand"
    RoundingMode.HalfEven -> "halfEven"
    RoundingMode.HalfDown -> "halfTrunc"
    RoundingMode.HalfUp -> "halfExpand"
}

private fun extractPart(parts: dynamic, type: String): String? {
    val len = parts.length.unsafeCast<Int>()
    for (i in 0 until len) {
        val p = parts[i]
        if (p.type.unsafeCast<String>() == type) {
            return p.value.unsafeCast<String>()
        }
    }
    return null
}

// `defaultCurrencyForCountry` is generated by `./gradlew :base:generateDefaultCurrencyMap`.
private fun defaultCurrencyForLocale(locale: KalugaLocale): String {
    val country = locale.countryCode.ifEmpty { return "USD" }
    return defaultCurrencyForCountry[country.uppercase()] ?: "USD"
}
