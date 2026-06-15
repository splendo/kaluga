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

import com.splendo.kaluga.base.externals.luxonFromFormat
import com.splendo.kaluga.base.externals.luxonFromMillis
import com.splendo.kaluga.base.externals.luxonOptions
import com.splendo.kaluga.base.utils.DateTimeFormatPart
import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.base.utils.KalugaDate
import com.splendo.kaluga.base.utils.KalugaLocale
import com.splendo.kaluga.base.utils.KalugaTimeZone
import com.splendo.kaluga.base.utils.createDateTimeFormat
import com.splendo.kaluga.base.utils.dateTimeFormatParts
import com.splendo.kaluga.base.utils.emptyDateTimeFormatOptions
import com.splendo.kaluga.base.utils.firstPartValue
import com.splendo.kaluga.base.utils.jsDate
import com.splendo.kaluga.base.utils.jsDateUTC
import kotlin.time.Duration.Companion.milliseconds

/**
 * Default implementation of [BaseDateFormatter] backed by the ECMAScript
 * [`Intl.DateTimeFormat`](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Intl/DateTimeFormat)
 * API for style-based formatting and the [`luxon`](https://moment.github.io/luxon/) library for
 * custom pattern formatting and parsing. Shared by the JS family (js + wasmJs).
 *
 * Java-style pattern tokens (`yyyy`, `MM`, `dd`, `HH`, `'literal'`, `Z`, …) are translated to
 * luxon's tokens so consumers can keep using the same patterns they use on Android/JVM.
 */
actual class KalugaDateFormatter private constructor(private var mode: FormatterMode, actual override var timeZone: KalugaTimeZone, private val locale: KalugaLocale) :
    BaseDateFormatter {

    internal sealed class FormatterMode {
        data class Style(val dateStyle: DateFormatStyle?, val timeStyle: DateFormatStyle?) : FormatterMode()
        data class Pattern(val javaPattern: String) : FormatterMode()
    }

    actual companion object {

        actual fun dateFormat(style: DateFormatStyle, timeZone: KalugaTimeZone, locale: KalugaLocale): KalugaDateFormatter =
            KalugaDateFormatter(FormatterMode.Style(style, null), timeZone, locale)

        actual fun timeFormat(style: DateFormatStyle, timeZone: KalugaTimeZone, locale: KalugaLocale): KalugaDateFormatter =
            KalugaDateFormatter(FormatterMode.Style(null, style), timeZone, locale)

        actual fun dateTimeFormat(dateStyle: DateFormatStyle, timeStyle: DateFormatStyle, timeZone: KalugaTimeZone, locale: KalugaLocale): KalugaDateFormatter =
            KalugaDateFormatter(FormatterMode.Style(dateStyle, timeStyle), timeZone, locale)

        actual fun patternFormat(pattern: String, timeZone: KalugaTimeZone, locale: KalugaLocale): KalugaDateFormatter =
            KalugaDateFormatter(FormatterMode.Pattern(pattern), timeZone, locale)
    }

    actual override var pattern: String
        get() = when (val m = mode) {
            is FormatterMode.Pattern -> m.javaPattern
            is FormatterMode.Style -> derivePatternFromStyle(m.dateStyle, m.timeStyle, locale.tag)
        }
        set(value) {
            mode = FormatterMode.Pattern(value)
        }

    // Symbol getters/setters — back the value with Intl-derived defaults; setters store overrides.
    private val symbolOverrides = HashMap<String, Any>()

    actual override var eras: List<String>
        get() = (symbolOverrides["eras"] as? List<String>) ?: defaultEras(locale.tag)
        set(value) {
            symbolOverrides["eras"] = value
        }

    actual override var months: List<String>
        get() = (symbolOverrides["months"] as? List<String>) ?: defaultMonths(locale.tag, narrow = false)
        set(value) {
            symbolOverrides["months"] = value
        }
    actual override var shortMonths: List<String>
        get() = (symbolOverrides["shortMonths"] as? List<String>) ?: defaultMonths(locale.tag, narrow = true)
        set(value) {
            symbolOverrides["shortMonths"] = value
        }

    actual override var weekdays: List<String>
        get() = (symbolOverrides["weekdays"] as? List<String>) ?: defaultWeekdays(locale.tag, narrow = false)
        set(value) {
            symbolOverrides["weekdays"] = value
        }
    actual override var shortWeekdays: List<String>
        get() = (symbolOverrides["shortWeekdays"] as? List<String>) ?: defaultWeekdays(locale.tag, narrow = true)
        set(value) {
            symbolOverrides["shortWeekdays"] = value
        }

    actual override var amString: String
        get() = (symbolOverrides["am"] as? String) ?: defaultDayPeriod(locale.tag, am = true)
        set(value) {
            symbolOverrides["am"] = value
        }
    actual override var pmString: String
        get() = (symbolOverrides["pm"] as? String) ?: defaultDayPeriod(locale.tag, am = false)
        set(value) {
            symbolOverrides["pm"] = value
        }

    actual override fun format(date: KalugaDate): String {
        val ms = date.durationSinceEpoch.inWholeMilliseconds.toDouble()
        return when (val m = mode) {
            is FormatterMode.Style -> formatByStyle(ms, m.dateStyle, m.timeStyle)
            is FormatterMode.Pattern -> formatByPattern(ms, m.javaPattern)
        }
    }

    actual override fun parse(string: String): KalugaDate? {
        val luxonPattern = javaPatternToLuxon(pattern, forParsing = true)
        val parsed = try {
            luxonFromFormat(string, luxonPattern, luxonOptions(timeZone.identifier, locale.tag))
        } catch (_: Throwable) {
            return null
        }
        if (!parsed.isValid) return null
        val parsedMs = parsed.toMillis().toLong()
        return DefaultKalugaDate.epoch(
            offset = parsedMs.milliseconds,
            timeZone = timeZone,
            locale = locale,
        )
    }

    private fun formatByStyle(ms: Double, dateStyle: DateFormatStyle?, timeStyle: DateFormatStyle?): String {
        val options = emptyDateTimeFormatOptions()
        options.timeZone = timeZone.identifier
        if (dateStyle != null) options.dateStyle = dateStyle.intlValue
        if (timeStyle != null) options.timeStyle = timeStyle.intlValue
        val formatter = createDateTimeFormat(locale.tag, options)
        return formatter.format(jsDate(ms))
    }

    private fun formatByPattern(ms: Double, javaPattern: String): String {
        val luxonPattern = javaPatternToLuxon(javaPattern)
        val dt = luxonFromMillis(ms, luxonOptions(timeZone.identifier, locale.tag))
        return dt.toFormat(luxonPattern)
    }
}

private val DateFormatStyle.intlValue: String
    get() = when (this) {
        DateFormatStyle.Short -> "short"
        DateFormatStyle.Medium -> "medium"
        DateFormatStyle.Long -> "long"
        DateFormatStyle.Full -> "full"
    }

private fun derivePatternFromStyle(dateStyle: DateFormatStyle?, timeStyle: DateFormatStyle?, localeTag: String): String {
    val options = emptyDateTimeFormatOptions()
    if (dateStyle != null) options.dateStyle = dateStyle.intlValue
    if (timeStyle != null) options.timeStyle = timeStyle.intlValue
    val sample = jsDateUTC(2024, 0, 8, 13, 37, 42, 750)
    val styleFormatter = createDateTimeFormat(localeTag, options)
    val parts = dateTimeFormatParts(styleFormatter, sample)

    val monthOptions = emptyDateTimeFormatOptions()
    monthOptions.month = "short"
    monthOptions.timeZone = "UTC"
    val weekdayOptions = emptyDateTimeFormatOptions()
    weekdayOptions.weekday = "short"
    weekdayOptions.timeZone = "UTC"
    val monthShort = dateTimeFormatParts(createDateTimeFormat(localeTag, monthOptions), sample)
    val weekdayShort = dateTimeFormatParts(createDateTimeFormat(localeTag, weekdayOptions), sample)
    val shortMonthForm = firstPartValue(monthShort, "month")
    val shortWeekdayForm = firstPartValue(weekdayShort, "weekday")

    val sb = StringBuilder()
    for (p in parts) {
        val type = p.type
        val value = p.value
        sb.append(
            when (type) {
                "year" -> if (value.length == 2) "yy" else "yyyy"

                "month" -> when {
                    value.all { it.isDigit() } && value.length == 1 -> "M"
                    value.all { it.isDigit() } -> "MM"
                    shortMonthForm != null && value == shortMonthForm -> "MMM"
                    else -> "MMMM"
                }

                "day" -> if (value.length == 1) "d" else "dd"

                "hour" -> if (value.length == 1) "H" else "HH"

                "minute" -> if (value.length == 1) "m" else "mm"

                "second" -> if (value.length == 1) "s" else "ss"

                "dayPeriod" -> "a"

                "weekday" -> when {
                    shortWeekdayForm != null && value == shortWeekdayForm -> "EEE"
                    else -> "EEEE"
                }

                "era" -> "G"

                "timeZoneName" -> "z"

                "fractionalSecond" -> "SSS"

                "literal" -> if (value.isEmpty()) "" else "'${value.replace("'", "''")}'"

                else -> ""
            },
        )
    }
    return sb.toString()
}

/**
 * Translate a subset of Java [SimpleDateFormat](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html)
 * tokens to luxon tokens. Quoted literals are preserved verbatim.
 *
 * Notable differences from Java's parser:
 * - Java `Z` (RFC 822 offset, e.g. `+0000`) maps to luxon `ZZZ`.
 * - Java `z` (locale tz name) maps to luxon `ZZZZ`.
 * - Java `X` (ISO 8601 offset) maps to luxon `ZZ`.
 * - Java `E`/`EE`/`EEE` collapse to luxon `EEE` (short); `EEEE` maps to `EEEE` (long).
 */
internal fun javaPatternToLuxon(pattern: String, forParsing: Boolean = false): String {
    val sb = StringBuilder()
    var i = 0
    while (i < pattern.length) {
        val c = pattern[i]
        if (c == '\'') {
            val end = pattern.indexOf('\'', i + 1)
            if (end < 0) {
                sb.append(pattern.substring(i))
                break
            }
            sb.append(pattern, i, end + 1)
            i = end + 1
        } else if (c in javaTokenChars) {
            var end = i
            while (end < pattern.length && pattern[end] == c) end++
            val count = end - i
            sb.append(translateToken(c, count, forParsing))
            i = end
        } else {
            sb.append(c)
            i++
        }
    }
    return sb.toString()
}

private val javaTokenChars = setOf('y', 'Y', 'M', 'd', 'H', 'h', 'k', 'K', 'm', 's', 'S', 'a', 'z', 'Z', 'X', 'G', 'E', 'D', 'w', 'W')

private fun translateToken(c: Char, count: Int, forParsing: Boolean): String = when (c) {
    'y', 'Y' -> "y".repeat(count.coerceAtMost(4).coerceAtLeast(1))
    'M' -> "M".repeat(count.coerceAtMost(4).coerceAtLeast(1))
    'd' -> "d".repeat(count.coerceAtMost(2).coerceAtLeast(1))
    'H' -> "H".repeat(count.coerceAtMost(2).coerceAtLeast(1))
    'h' -> "h".repeat(count.coerceAtMost(2).coerceAtLeast(1))
    'k' -> "H".repeat(count.coerceAtMost(2).coerceAtLeast(1))
    'K' -> "h".repeat(count.coerceAtMost(2).coerceAtLeast(1))
    'm' -> "m".repeat(count.coerceAtMost(2).coerceAtLeast(1))
    's' -> "s".repeat(count.coerceAtMost(2).coerceAtLeast(1))
    'S' -> "S".repeat(count.coerceAtMost(3).coerceAtLeast(1))
    'a' -> "a"
    'Z' -> "ZZZ"
    'z' -> if (forParsing) "z" else "ZZZZ"
    'X' -> "ZZ"
    'G' -> "G"
    'E' -> if (count >= 4) "EEEE" else "EEE"
    'D' -> "o"
    'w', 'W' -> "WW"
    else -> c.toString().repeat(count)
}

private fun defaultEras(localeTag: String): List<String> = try {
    val options = emptyDateTimeFormatOptions()
    options.era = "short"
    options.year = "numeric"
    val eraFormatter = createDateTimeFormat(localeTag, options)
    val ad = dateTimeFormatParts(eraFormatter, jsDate(0.0))
    val bc = dateTimeFormatParts(eraFormatter, jsDate(-100000000000000.0))
    listOf(firstPartValue(bc, "era") ?: "BC", firstPartValue(ad, "era") ?: "AD")
} catch (_: Throwable) {
    listOf("BC", "AD")
}

private fun defaultMonths(localeTag: String, narrow: Boolean): List<String> {
    val options = emptyDateTimeFormatOptions()
    options.month = if (narrow) "short" else "long"
    options.timeZone = "UTC"
    val formatter = createDateTimeFormat(localeTag, options)
    return (1..12).map { month ->
        val parts = dateTimeFormatParts(formatter, jsDateUTC(2024, month - 1, 15))
        firstPartValue(parts, "month") ?: month.toString()
    }
}

private fun defaultWeekdays(localeTag: String, narrow: Boolean): List<String> {
    val options = emptyDateTimeFormatOptions()
    options.weekday = if (narrow) "short" else "long"
    options.timeZone = "UTC"
    val formatter = createDateTimeFormat(localeTag, options)
    // Reference week containing 2024-01-07 (Sunday) through 2024-01-13 (Saturday).
    return (0..6).map { offset ->
        val parts = dateTimeFormatParts(formatter, jsDateUTC(2024, 0, 7 + offset))
        firstPartValue(parts, "weekday") ?: offset.toString()
    }
}

private fun defaultDayPeriod(localeTag: String, am: Boolean): String {
    val hour = if (am) 6 else 18
    val parts: List<DateTimeFormatPart> = try {
        val options = emptyDateTimeFormatOptions()
        options.hour = "numeric"
        options.hour12 = true
        options.timeZone = "UTC"
        val formatter = createDateTimeFormat(localeTag, options)
        dateTimeFormatParts(formatter, jsDateUTC(2024, 0, 15, hour, 0, 0))
    } catch (_: Throwable) {
        return if (am) "AM" else "PM"
    }
    return firstPartValue(parts, "dayPeriod") ?: if (am) "AM" else "PM"
}
