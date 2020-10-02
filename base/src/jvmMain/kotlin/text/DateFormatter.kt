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

import com.splendo.kaluga.base.utils.Date
import com.splendo.kaluga.base.utils.Locale
import com.splendo.kaluga.base.utils.TimeZone
import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar

actual class DateFormatter(private val format: SimpleDateFormat) {

    actual companion object {
        actual fun dateFormat(style: DateFormatStyle, timeZone: TimeZone, locale: Locale): DateFormatter = createDateFormatter(DateFormat.getDateInstance(style.javaStyle(), locale.locale) as SimpleDateFormat, timeZone)
        actual fun timeFormat(style: DateFormatStyle, timeZone: TimeZone, locale: Locale): DateFormatter = createDateFormatter(DateFormat.getTimeInstance(style.javaStyle(), locale.locale) as SimpleDateFormat, timeZone)
        actual fun dateTimeFormat(
            dateStyle: DateFormatStyle,
            timeStyle: DateFormatStyle,
            timeZone: TimeZone,
            locale: Locale
        ): DateFormatter = createDateFormatter(DateFormat.getDateTimeInstance(dateStyle.javaStyle(), timeStyle.javaStyle(), locale.locale) as SimpleDateFormat, timeZone)
        actual fun patternFormat(pattern: String, timeZone: TimeZone, locale: Locale): DateFormatter = createDateFormatter(SimpleDateFormat(pattern, locale.locale), timeZone)

        fun createDateFormatter(simpleDateFormat: SimpleDateFormat, timeZone: TimeZone): DateFormatter {
            return DateFormatter(simpleDateFormat).apply {
                this.timeZone = timeZone
            }
        }
    }

    private val symbols: DateFormatSymbols get() = format.dateFormatSymbols

    actual var timeZone: TimeZone
        get() = TimeZone(format.timeZone)
        set(value) { format.timeZone = value.timeZone }

    actual var eras: List<String>
        get() = symbols.eras.toList()
        set(value) { updateSymbols { it.eras = value.toTypedArray() } }

    actual var months: List<String>
        get() = symbols.months.toList()
        set(value) { updateSymbols { it.months = value.toTypedArray() } }
    actual var shortMonths: List<String>
        get() = symbols.shortMonths.toList()
        set(value) { updateSymbols { it.shortMonths = value.toTypedArray() } }

    actual var weekdays: List<String>
        get() {
            val weekdaysWithEmptyFirst = symbols.weekdays.toList()
            return if (weekdaysWithEmptyFirst.size > 1) {
                weekdaysWithEmptyFirst.subList(1, weekdaysWithEmptyFirst.size)
            } else {
                emptyList()
            }
        }
        set(value) { updateSymbols {
            val weekdaysWithEmptyFirst = value.toMutableList().apply {
                add(0, "")
            }
            it.weekdays = weekdaysWithEmptyFirst.toTypedArray()
        } }
    actual var shortWeekdays: List<String>
        get() = symbols.shortWeekdays.toList()
        set(value) { updateSymbols { it.shortWeekdays = value.toTypedArray() } }

    actual var amString: String
        get() = symbols.amPmStrings.toList()[0]
        set(value) { updateSymbols { it.amPmStrings = it.amPmStrings.toMutableList().apply { this[0] = value }.toTypedArray() } }
    actual var pmString: String
        get() = symbols.amPmStrings.toList()[1]
        set(value) { updateSymbols { it.amPmStrings = it.amPmStrings.toMutableList().apply { this[1] = value }.toTypedArray() } }

    actual fun format(date: Date): String = format.format(date.calendar.time)
    actual fun parse(string: String): Date? {
        return format.parse(string)?.let { date ->
            val calendar = format.calendar.clone() as Calendar
            Date(calendar.apply { time = date })
        }
    }

    private fun updateSymbols(transform: (DateFormatSymbols) -> Unit) {
        val symbols = this.symbols
        transform(symbols)
        format.dateFormatSymbols = symbols
    }
}

private fun DateFormatStyle.javaStyle(): Int = when (this) {
    DateFormatStyle.Short -> DateFormat.SHORT
    DateFormatStyle.Medium -> DateFormat.MEDIUM
    DateFormatStyle.Long -> DateFormat.LONG
    DateFormatStyle.Full -> DateFormat.FULL
}
