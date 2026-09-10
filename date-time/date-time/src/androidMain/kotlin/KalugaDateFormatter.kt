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

@file:JvmName("AndroidDateFormatter")

package com.splendo.kaluga.datetime

import com.splendo.kaluga.base.i18n.KalugaLocale
import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * Default implementation of [BaseDateFormatter]
 */
actual class KalugaDateFormatter private constructor(private val format: SimpleDateFormat) : BaseDateFormatter {

    actual companion object {

        /**
         * Creates a [KalugaDateFormatter] that only formats the date components of a [KalugaDate]
         * @param style The [DateFormatStyle] used for formatting the date components of the [KalugaDate]. Defaults to [DateFormatStyle.Medium].
         * @param timeZone The [KalugaTimeZone] for which the date should be formatted. Defaults to [KalugaTimeZone.current].
         * @param locale The [KalugaLocale] for which the date should be formatted. Defaults to [KalugaLocale.defaultLocale].
         */
        actual fun dateFormat(style: DateFormatStyle, timeZone: KalugaTimeZone, locale: KalugaLocale): KalugaDateFormatter =
            createDateFormatter(DateFormat.getDateInstance(style.javaStyle(), locale.locale) as SimpleDateFormat, timeZone)

        /**
         * Creates a [KalugaDateFormatter] that only formats the time components of a [KalugaDate]
         * @param style The [DateFormatStyle] used for formatting the time components of the [KalugaDate]. Defaults to [DateFormatStyle.Medium].
         * @param timeZone The [KalugaTimeZone] for which the date should be formatted. Defaults to [KalugaTimeZone.current].
         * @param locale The [KalugaLocale] for which the date should be formatted. Defaults to [KalugaLocale.defaultLocale].
         */
        actual fun timeFormat(style: DateFormatStyle, timeZone: KalugaTimeZone, locale: KalugaLocale): KalugaDateFormatter =
            createDateFormatter(DateFormat.getTimeInstance(style.javaStyle(), locale.locale) as SimpleDateFormat, timeZone)

        /**
         * Creates a [KalugaDateFormatter] that formats both date and time components of a [KalugaDate]
         * @param dateStyle The [DateFormatStyle] used for formatting the date components of the [KalugaDate]. Defaults to [DateFormatStyle.Medium].
         * @param timeStyle The [DateFormatStyle] used for formatting the time components of the [KalugaDate]. Defaults to [DateFormatStyle.Medium].
         * @param timeZone The [KalugaTimeZone] for which the date should be formatted. Defaults to [KalugaTimeZone.current].
         * @param locale The [KalugaLocale] for which the date should be formatted. Defaults to [KalugaLocale.defaultLocale].
         */
        actual fun dateTimeFormat(dateStyle: DateFormatStyle, timeStyle: DateFormatStyle, timeZone: KalugaTimeZone, locale: KalugaLocale): KalugaDateFormatter =
            createDateFormatter(DateFormat.getDateTimeInstance(dateStyle.javaStyle(), timeStyle.javaStyle(), locale.locale) as SimpleDateFormat, timeZone)

        /**
         * Creates a [KalugaDateFormatter] using a custom Date format pattern.
         * On iOS some user settings may take precedent over the format (i.e. using 12 hour clock).
         * To prevent this, ensure that the provided [locale] is of a `POSIX` type.
         * A convenience [fixedPatternFormat] method exists to default to this behaviour.
         * @param pattern The pattern to apply.
         * @param timeZone The [KalugaTimeZone] for which the date should be formatted. Defaults to [KalugaTimeZone.current].
         * @param locale The [KalugaLocale] for which the date should be formatted. Defaults to [KalugaLocale.defaultLocale].
         */
        actual fun patternFormat(pattern: String, timeZone: KalugaTimeZone, locale: KalugaLocale): KalugaDateFormatter =
            createDateFormatter(SimpleDateFormat(pattern, locale.locale), timeZone)

        private fun createDateFormatter(simpleDateFormat: SimpleDateFormat, timeZone: KalugaTimeZone): KalugaDateFormatter = KalugaDateFormatter(simpleDateFormat).apply {
            this.timeZone = timeZone
        }
    }

    // SimpleDateFormat is not thread-safe: format and parse both mutate the shared internal Calendar,
    // so racing them on one instance corrupts results. Parsing therefore gets its own instance,
    // making format/parse cross-contamination impossible while each path pays only a single
    // (typically uncontended) monitor. The instances double as the locks, so they must stay private
    // and never escape this class. Configuration writes go through [update] to both instances so the
    // format/parse round-trip stays consistent. DateFormat.clone() deep-copies the calendar and
    // symbols, so the two instances share no mutable state.
    private val parser = format.clone() as SimpleDateFormat

    // DateFormat.getDateFormatSymbols returns a copy, so it is safe to read outside the lock once
    // obtained.
    private val symbols: DateFormatSymbols get() = read { it.dateFormatSymbols }

    private inline fun <T> read(action: (SimpleDateFormat) -> T): T = synchronized(format) { action(format) }

    // Nested in a fixed order (format then parse) so writes cannot deadlock and neither instance is
    // observed mid-update by format or parse.
    private inline fun update(action: (SimpleDateFormat) -> Unit) = synchronized(format) {
        synchronized(parser) {
            action(format)
            action(parser)
        }
    }

    actual override var pattern: String
        get() = read { it.toPattern() }
        set(value) {
            update { it.applyPattern(value) }
        }

    actual override var timeZone: KalugaTimeZone
        get() = read { KalugaTimeZone(it.timeZone) }
        set(value) {
            update { it.timeZone = value.timeZone }
        }

    actual override var eras: List<String>
        get() = symbols.eras.toList()
        set(value) {
            updateSymbols { it.eras = value.toTypedArray() }
        }

    actual override var months: List<String>
        get() = symbols.months.toList()
        set(value) {
            updateSymbols { it.months = value.toTypedArray() }
        }
    actual override var shortMonths: List<String>
        get() = symbols.shortMonths.toList()
        set(value) {
            updateSymbols { it.shortMonths = value.toTypedArray() }
        }

    actual override var weekdays: List<String>
        get() {
            val weekdaysWithEmptyFirst = symbols.weekdays.toList()
            return if (weekdaysWithEmptyFirst.size > 1) {
                weekdaysWithEmptyFirst.subList(1, weekdaysWithEmptyFirst.size)
            } else {
                emptyList()
            }
        }
        set(value) {
            updateSymbols {
                val weekdaysWithEmptyFirst = value.toMutableList().apply {
                    add(0, "")
                }
                it.weekdays = weekdaysWithEmptyFirst.toTypedArray()
            }
        }
    actual override var shortWeekdays: List<String>
        get() {
            val weekdaysWithEmptyFirst = symbols.shortWeekdays.toList()
            return if (weekdaysWithEmptyFirst.size > 1) {
                weekdaysWithEmptyFirst.subList(1, weekdaysWithEmptyFirst.size)
            } else {
                emptyList()
            }
        }
        set(value) {
            updateSymbols {
                val weekdaysWithEmptyFirst = value.toMutableList().apply {
                    add(0, "")
                }
                it.shortWeekdays = weekdaysWithEmptyFirst.toTypedArray()
            }
        }

    actual override var amString: String
        get() = symbols.amPmStrings.toList()[0]
        set(value) {
            updateSymbols { it.amPmStrings = it.amPmStrings.toMutableList().apply { this[0] = value }.toTypedArray() }
        }
    actual override var pmString: String
        get() = symbols.amPmStrings.toList()[1]
        set(value) {
            updateSymbols { it.amPmStrings = it.amPmStrings.toMutableList().apply { this[1] = value }.toTypedArray() }
        }

    actual override fun format(date: KalugaDate): String = synchronized(format) { format.format(date.date) }
    actual override fun parse(string: String): KalugaDate? = synchronized(parser) {
        // Read and restore against the parser's own zone; reading the format instance here would nest
        // the locks in the reverse order of [update].
        val currentTimeZone = KalugaTimeZone(parser.timeZone)
        try {
            parser.parse(string)?.let { date ->
                val calendar = parser.calendar.clone() as Calendar
                DefaultKalugaDate(
                    calendar.apply {
                        time = date
                        timeZone = currentTimeZone.timeZone
                    },
                )
            }
        } catch (e: ParseException) {
            null
        } finally {
            // Parse may change the timezone to the timezone parsed by the String. This restores the original timezone
            parser.timeZone = currentTimeZone.timeZone
        }
    }

    private fun updateSymbols(transform: (DateFormatSymbols) -> Unit) = update {
        val symbols = it.dateFormatSymbols
        transform(symbols)
        it.dateFormatSymbols = symbols
    }
}

private fun DateFormatStyle.javaStyle(): Int = when (this) {
    DateFormatStyle.Short -> DateFormat.SHORT
    DateFormatStyle.Medium -> DateFormat.MEDIUM
    DateFormatStyle.Long -> DateFormat.LONG
    DateFormatStyle.Full -> DateFormat.FULL
}
