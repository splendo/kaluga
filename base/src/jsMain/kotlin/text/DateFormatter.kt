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

actual class DateFormatter(private val formatter: (kotlin.js.Date) -> String) {

    actual companion object {
        actual fun dateFormat(style: DateFormatStyle, locale: Locale): DateFormatter = DateFormatter { date -> date.toLocaleDateString(arrayOf("${locale.language}-${locale.country}")) }
        actual fun timeFormat(style: DateFormatStyle, locale: Locale): DateFormatter = DateFormatter { date -> date.toLocaleTimeString(arrayOf("${locale.language}-${locale.country}")) }
        actual fun dateTimeFormat(dateStyle: DateFormatStyle, timeStyle: DateFormatStyle, locale: Locale): DateFormatter = DateFormatter { date -> date.toLocaleString(arrayOf("${locale.language}-${locale.country}")) }
        actual fun patternFormat(pattern: String, locale: Locale): DateFormatter = DateFormatter { date -> date.toLocaleString(arrayOf("${locale.language}-${locale.country}")) }
    }

    actual var timeZone: TimeZone = TimeZone()
    actual var eras: List<String> = emptyList()

    actual var months: List<String> = emptyList()
    actual var shortMonths: List<String> = emptyList()

    actual var weekdays: List<String> = emptyList()
    actual var shortWeekdays: List<String> = emptyList()

    actual var amString: String = ""
    actual var pmString: String = ""

    actual fun format(date: Date): String = formatter(date.date)
    actual fun parse(string: String): Date? = null
}

private fun DateFormatStyle.stringValue(): String = when (this) {
    DateFormatStyle.Short -> "short"
    DateFormatStyle.Medium -> "medium"
    DateFormatStyle.Long -> "long"
    DateFormatStyle.Full -> "full"
}
