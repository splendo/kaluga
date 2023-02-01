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

import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.base.utils.KalugaDate
import com.splendo.kaluga.base.utils.Locale
import com.splendo.kaluga.base.utils.TimeZone
import com.splendo.kaluga.base.utils.typedList
import platform.Foundation.NSCalendar
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterFullStyle
import platform.Foundation.NSDateFormatterLongStyle
import platform.Foundation.NSDateFormatterMediumStyle
import platform.Foundation.NSDateFormatterNoStyle
import platform.Foundation.NSDateFormatterShortStyle
import platform.Foundation.NSDateFormatterStyle

/**
 * Default implementation of [BaseDateFormatter]
 */
actual class KalugaDateFormatter private constructor(private val format: NSDateFormatter) : BaseDateFormatter {

    actual companion object {

        /**
         * Creates a [KalugaDateFormatter] that only formats the date components of a [KalugaDate]
         * @param style The [DateFormatStyle] used for formatting the date components of the [KalugaDate]. Defaults to [DateFormatStyle.Medium].
         * @param timeZone The [TimeZone] for which the date should be formatted. Defaults to [TimeZone.current].
         * @param locale The [Locale] for which the date should be formatted. Defaults to [Locale.defaultLocale].
         */
        actual fun dateFormat(
            style: DateFormatStyle,
            timeZone: TimeZone,
            locale: Locale
        ): KalugaDateFormatter = createDateFormatter(style, null, timeZone, locale)

        /**
         * Creates a [KalugaDateFormatter] that only formats the time components of a [KalugaDate]
         * @param style The [DateFormatStyle] used for formatting the time components of the [KalugaDate]. Defaults to [DateFormatStyle.Medium].
         * @param timeZone The [TimeZone] for which the date should be formatted. Defaults to [TimeZone.current].
         * @param locale The [Locale] for which the date should be formatted. Defaults to [Locale.defaultLocale].
         */
        actual fun timeFormat(
            style: DateFormatStyle,
            timeZone: TimeZone,
            locale: Locale
        ): KalugaDateFormatter = createDateFormatter(null, style, timeZone, locale)

        /**
         * Creates a [KalugaDateFormatter] that formats both date and time components of a [KalugaDate]
         * @param dateStyle The [DateFormatStyle] used for formatting the date components of the [KalugaDate]. Defaults to [DateFormatStyle.Medium].
         * @param timeStyle The [DateFormatStyle] used for formatting the time components of the [KalugaDate]. Defaults to [DateFormatStyle.Medium].
         * @param timeZone The [TimeZone] for which the date should be formatted. Defaults to [TimeZone.current].
         * @param locale The [Locale] for which the date should be formatted. Defaults to [Locale.defaultLocale].
         */
        actual fun dateTimeFormat(
            dateStyle: DateFormatStyle,
            timeStyle: DateFormatStyle,
            timeZone: TimeZone,
            locale: Locale
        ): KalugaDateFormatter = createDateFormatter(dateStyle, timeStyle, timeZone, locale)

        /**
         * Creates a [KalugaDateFormatter] using a custom Date format pattern.
         * On iOS some user settings may take precedent over the format (i.e. using 12 hour clock).
         * To prevent this, ensure that the provided [locale] is of a `POSIX` type.
         * A convenience [fixedPatternFormat] method exists to default to this behaviour.
         * @param pattern The pattern to apply.
         * @param timeZone The [TimeZone] for which the date should be formatted. Defaults to [TimeZone.current].
         * @param locale The [Locale] for which the date should be formatted. Defaults to [Locale.defaultLocale].
         */
        actual fun patternFormat(pattern: String, timeZone: TimeZone, locale: Locale): KalugaDateFormatter = KalugaDateFormatter(
            NSDateFormatter().apply {
                this.locale = locale.nsLocale
                this.timeZone = timeZone.timeZone
                this.defaultDate = defaultDate(timeZone)
                dateFormat = pattern
            }
        )

        fun createDateFormatter(
            dateStyle: DateFormatStyle?,
            timeStyle: DateFormatStyle?,
            timeZone: TimeZone,
            locale: Locale
        ): KalugaDateFormatter = KalugaDateFormatter(
            NSDateFormatter().apply {
                this.locale = locale.nsLocale
                this.timeZone = timeZone.timeZone
                this.defaultDate = defaultDate(timeZone)
                this.dateStyle = dateStyle.nsDateFormatterStyle()
                this.timeStyle = timeStyle.nsDateFormatterStyle()
            }
        )

        // Due to a problem related to the commonizer we need to supply all the
        // default arguments expected from the method signature
        private fun defaultDate(timeZone: TimeZone) =
            DefaultKalugaDate.now(
                offsetInMilliseconds = 0L,
                timeZone = timeZone,
                locale = Locale.defaultLocale
            ).apply {
                // Cannot use .utc since it may not be available when this method is called
                // This is likely caused by https://youtrack.jetbrains.com/issue/KT-38181
                // TODO When moving Date and Date formatter to separate modules, this should be updated to use .utc
                val epoch = DefaultKalugaDate.epoch(
                    offsetInMilliseconds = 0L,
                    timeZone = TimeZone.get("UTC")!!,
                    locale = Locale.defaultLocale
                )
                this.era = epoch.era
                this.year = epoch.year
                this.month = epoch.month
                this.day = epoch.day
                this.hour = epoch.hour
                this.minute = epoch.minute
                this.second = epoch.second
            }.date
    }

    override var pattern: String
        get() = format.dateFormat
        set(value) { format.dateFormat = value }

    override var timeZone: TimeZone
        get() = TimeZone(format.timeZone)
        set(value) { format.timeZone = value.timeZone }

    override var eras: List<String>
        get() = format.eraSymbols.typedList()
        set(value) { format.eraSymbols = value }

    override var months: List<String>
        get() = format.monthSymbols.typedList()
        set(value) { format.monthSymbols = value }
    override var shortMonths: List<String>
        get() = format.shortMonthSymbols.typedList()
        set(value) { format.shortMonthSymbols = value }

    override var weekdays: List<String>
        get() = format.weekdaySymbols.typedList()
        set(value) { format.weekdaySymbols = value }
    override var shortWeekdays: List<String>
        get() = format.shortWeekdaySymbols.typedList()
        set(value) { format.shortWeekdaySymbols = value }

    override var amString: String
        get() = format.AMSymbol
        set(value) { format.AMSymbol = value }
    override var pmString: String
        get() = format.PMSymbol
        set(value) { format.PMSymbol = value }

    override fun format(date: KalugaDate): String = format.stringFromDate(date.date)
    override fun parse(string: String): KalugaDate? {
        return format.dateFromString(string)?.let { date ->
            val calendar = format.calendar.copy() as NSCalendar
            calendar.timeZone = timeZone.timeZone
            DefaultKalugaDate(calendar, date)
        }
    }
}

private fun DateFormatStyle?.nsDateFormatterStyle(): NSDateFormatterStyle = when (this) {
    DateFormatStyle.Short -> NSDateFormatterShortStyle
    DateFormatStyle.Medium -> NSDateFormatterMediumStyle
    DateFormatStyle.Long -> NSDateFormatterLongStyle
    DateFormatStyle.Full -> NSDateFormatterFullStyle
    null -> NSDateFormatterNoStyle
}
