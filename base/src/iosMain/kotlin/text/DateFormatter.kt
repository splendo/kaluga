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

import com.splendo.kaluga.base.typedList
import com.splendo.kaluga.base.utils.Date
import com.splendo.kaluga.base.utils.Locale
import com.splendo.kaluga.base.utils.TimeZone
import platform.Foundation.NSCalendar
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterFullStyle
import platform.Foundation.NSDateFormatterLongStyle
import platform.Foundation.NSDateFormatterMediumStyle
import platform.Foundation.NSDateFormatterNoStyle
import platform.Foundation.NSDateFormatterShortStyle
import platform.Foundation.NSDateFormatterStyle

actual class DateFormatter private constructor(private val format: NSDateFormatter) {

    actual companion object {
        actual fun dateFormat(
            style: DateFormatStyle,
            timeZone: TimeZone,
            locale: Locale
        ): DateFormatter = createDateFormatter(style, null, timeZone, locale)

        actual fun timeFormat(
            style: DateFormatStyle,
            timeZone: TimeZone,
            locale: Locale
        ): DateFormatter = createDateFormatter(null, style, timeZone, locale)

        actual fun dateTimeFormat(
            dateStyle: DateFormatStyle,
            timeStyle: DateFormatStyle,
            timeZone: TimeZone,
            locale: Locale
        ): DateFormatter = createDateFormatter(dateStyle, timeStyle, timeZone, locale)

        actual fun patternFormat(pattern: String, timeZone: TimeZone, locale: Locale): DateFormatter = DateFormatter(
            NSDateFormatter().apply {
                this.locale = locale.nsLocale
                this.timeZone = timeZone.timeZone
                dateFormat = pattern
            }
        )

        fun createDateFormatter(
            dateStyle: DateFormatStyle?,
            timeStyle: DateFormatStyle?,
            timeZone: TimeZone,
            locale: Locale
        ): DateFormatter = DateFormatter(
            NSDateFormatter().apply {
                this.locale = locale.nsLocale
                this.timeZone = timeZone.timeZone
                this.dateStyle = dateStyle.nsDateFormatterStyle()
                this.timeStyle = timeStyle.nsDateFormatterStyle()
            }
        )
    }

    actual var pattern: String
        get() = format.dateFormat
        set(value) { format.dateFormat = value }

    actual var timeZone: TimeZone
        get() = TimeZone(format.timeZone)
        set(value) { format.timeZone = value.timeZone }

    actual var eras: List<String>
        get() = format.eraSymbols.typedList()
        set(value) { format.eraSymbols = value }

    actual var months: List<String>
        get() = format.monthSymbols.typedList()
        set(value) { format.monthSymbols = value }
    actual var shortMonths: List<String>
        get() = format.shortMonthSymbols.typedList()
        set(value) { format.shortMonthSymbols = value }

    actual var weekdays: List<String>
        get() = format.weekdaySymbols.typedList()
        set(value) { format.weekdaySymbols = value }
    actual var shortWeekdays: List<String>
        get() = format.shortWeekdaySymbols.typedList()
        set(value) { format.shortWeekdaySymbols = value }

    actual var amString: String
        get() = format.AMSymbol
        set(value) { format.AMSymbol = value }
    actual var pmString: String
        get() = format.PMSymbol
        set(value) { format.PMSymbol = value }

    actual fun format(date: Date): String = format.stringFromDate(date.date)
    actual fun parse(string: String): Date? {
        return format.dateFromString(string)?.let { date ->
            val calendar = format.calendar.copy() as NSCalendar
            calendar.timeZone = timeZone.timeZone
            Date(calendar, date)
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
