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

import com.splendo.kaluga.base.externals.DateTime
import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.base.utils.KalugaDate
import com.splendo.kaluga.base.utils.KalugaLocale
import com.splendo.kaluga.base.utils.KalugaTimeZone
import com.splendo.kaluga.base.utils.newDateTimeFormat
import kotlin.time.Duration.Companion.milliseconds

/**
 * Default implementation of [BaseDateFormatter] backed by the ECMAScript
 * [`Intl.DateTimeFormat`](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Intl/DateTimeFormat)
 * API for style-based formatting and the [`luxon`](https://moment.github.io/luxon/) library for
 * custom pattern formatting and parsing.
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
        val zone = timeZone.identifier
        val tag = locale.tag
        val s = string
        val lp = luxonPattern
        val parsed = try {
            DateTime.fromFormat(s, lp, js("({zone: zone, locale: tag})"))
        } catch (_: dynamic) {
            return null
        }
        val isValid = parsed.isValid.unsafeCast<Boolean>()
        if (!isValid) return null
        val parsedMs = parsed.toMillis().unsafeCast<Double>().toLong()
        return DefaultKalugaDate.epoch(
            offset = parsedMs.milliseconds,
            timeZone = timeZone,
            locale = locale,
        )
    }

    private fun formatByStyle(ms: Double, dateStyle: DateFormatStyle?, timeStyle: DateFormatStyle?): String {
        val tag = locale.tag
        val zone = timeZone.identifier
        val opts = js("({timeZone: zone})")
        if (dateStyle != null) opts.dateStyle = dateStyle.intlValue
        if (timeStyle != null) opts.timeStyle = timeStyle.intlValue
        val date = js("new Date(ms)")
        val formatter = newDateTimeFormat(tag, opts)
        return formatter.format(date).unsafeCast<String>()
    }

    private fun formatByPattern(ms: Double, javaPattern: String): String {
        val luxonPattern = javaPatternToLuxon(javaPattern)
        val zone = timeZone.identifier
        val tag = locale.tag
        val dt = DateTime.fromMillis(ms, js("({zone: zone, locale: tag})"))
        return dt.toFormat(luxonPattern).unsafeCast<String>()
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
    val opts = js("({})")
    if (dateStyle != null) opts.dateStyle = dateStyle.intlValue
    if (timeStyle != null) opts.timeStyle = timeStyle.intlValue
    val sample = js("new Date(Date.UTC(2024, 0, 8, 13, 37, 42, 750))")
    val tag = localeTag
    val styleFormatter = newDateTimeFormat(tag, opts)
    val parts = styleFormatter.formatToParts(sample)

    val monthShortFormatter = newDateTimeFormat(tag, js("({month: 'short', timeZone: 'UTC'})"))
    val weekdayShortFormatter = newDateTimeFormat(tag, js("({weekday: 'short', timeZone: 'UTC'})"))
    val monthShort = monthShortFormatter.formatToParts(sample)
    val weekdayShort = weekdayShortFormatter.formatToParts(sample)
    val shortMonthForm = extractFirstPart(monthShort, "month")
    val shortWeekdayForm = extractFirstPart(weekdayShort, "weekday")

    val sb = StringBuilder()
    val length = parts.length.unsafeCast<Int>()
    for (i in 0 until length) {
        val p = parts[i]
        val type = p.type.unsafeCast<String>()
        val value = p.value.unsafeCast<String>()
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
    val tag = localeTag
    val eraFormatter = newDateTimeFormat(tag, js("({ era: 'short', year: 'numeric' })"))
    val ad = eraFormatter.formatToParts(js("new Date(0)"))
    val bc = eraFormatter.formatToParts(js("new Date(-100000000000000)"))
    listOf(extractFirstPart(bc, "era") ?: "BC", extractFirstPart(ad, "era") ?: "AD")
} catch (_: dynamic) {
    listOf("BC", "AD")
}

private fun defaultMonths(localeTag: String, narrow: Boolean): List<String> {
    val tag = localeTag
    val style = if (narrow) "short" else "long"
    val formatter = newDateTimeFormat(tag, js("({ month: style, timeZone: 'UTC' })"))
    return (1..12).map { month ->
        val sample = js("new Date(Date.UTC(2024, month - 1, 15))")
        val parts = formatter.formatToParts(sample)
        extractFirstPart(parts, "month") ?: month.toString()
    }
}

private fun defaultWeekdays(localeTag: String, narrow: Boolean): List<String> {
    val tag = localeTag
    val style = if (narrow) "short" else "long"
    val formatter = newDateTimeFormat(tag, js("({ weekday: style, timeZone: 'UTC' })"))
    // Reference week containing 2024-01-07 (Sunday) through 2024-01-13 (Saturday).
    return (0..6).map { offset ->
        val sample = js("new Date(Date.UTC(2024, 0, 7 + offset))")
        val parts = formatter.formatToParts(sample)
        extractFirstPart(parts, "weekday") ?: offset.toString()
    }
}

private fun defaultDayPeriod(localeTag: String, am: Boolean): String {
    val tag = localeTag
    val hour = if (am) 6 else 18
    val sample = js("new Date(Date.UTC(2024, 0, 15, hour, 0, 0))")
    val parts = try {
        val formatter = newDateTimeFormat(tag, js("({ hour: 'numeric', hour12: true, timeZone: 'UTC' })"))
        formatter.formatToParts(sample)
    } catch (_: dynamic) {
        return if (am) "AM" else "PM"
    }
    return extractFirstPart(parts, "dayPeriod") ?: if (am) "AM" else "PM"
}

private fun extractFirstPart(parts: dynamic, type: String): String? {
    val length = parts.length.unsafeCast<Int>()
    for (i in 0 until length) {
        val part = parts[i]
        if (part.type.unsafeCast<String>() == type) {
            return part.value.unsafeCast<String>()
        }
    }
    return null
}
