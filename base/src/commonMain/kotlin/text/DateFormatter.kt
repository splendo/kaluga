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
import com.splendo.kaluga.base.utils.Locale.Companion.defaultLocale
import com.splendo.kaluga.base.utils.TimeZone

enum class DateFormatStyle {
    Short,
    Medium,
    Long,
    Full
}

expect class DateFormatter {

    companion object {
        fun dateFormat(style: DateFormatStyle, timeZone: TimeZone = TimeZone.current(), locale: Locale = defaultLocale): DateFormatter
        fun timeFormat(style: DateFormatStyle, timeZone: TimeZone = TimeZone.current(), locale: Locale = defaultLocale): DateFormatter
        fun dateTimeFormat(
            dateStyle: DateFormatStyle,
            timeStyle: DateFormatStyle,
            timeZone: TimeZone = TimeZone.current(),
            locale: Locale = defaultLocale
        ): DateFormatter
        fun patternFormat(pattern: String, timeZone: TimeZone = TimeZone.current(), locale: Locale = defaultLocale): DateFormatter
    }

    var timeZone: TimeZone

    var eras: List<String>

    var months: List<String>
    var shortMonths: List<String>

    var weekdays: List<String>
    var shortWeekdays: List<String>

    var amString: String
    var pmString: String

    fun format(date: Date): String
    fun parse(string: String): Date?
}
