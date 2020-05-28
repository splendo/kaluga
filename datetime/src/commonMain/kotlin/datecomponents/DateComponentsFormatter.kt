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

package com.splendo.kaluga.datetime.datecomponents

import com.splendo.kaluga.datetime.DateTime
import com.splendo.kaluga.datetime.dayComponent
import com.splendo.kaluga.datetime.getUtility
import com.splendo.kaluga.datetime.monthComponent
import com.splendo.kaluga.datetime.yearComponent
import com.splendo.kaluga.formatted.Formatter

interface DateComponentsFormatter : Formatter<DateComponents> {
    val dateTimeFormatter: Formatter<DateTime>

    override fun string(value: DateComponents): String =
        value.let {
            it.dateTime()
        }?.let {
            dateTimeFormatter.string(it)
        } ?: ""

    companion object {
        fun formatterForComponent(components: DateComponents): Formatter<DateComponents> =
            when (components) {
                is Day -> DayFormatter()
                is Month -> MonthFormatter()
                is Year -> YearFormatter()
                is DateComponentsUndefined -> formatterForComponent(components.components)
            }
    }
}

private class DayFormatter(override val dateTimeFormatter: Formatter<DateTime> = getFormatter("yyyy-MM-dd")) : DateComponentsFormatter {
    override fun value(string: String): DateComponents =
        dateTimeFormatter.value(string).dayComponent()
}

private class MonthFormatter(override val dateTimeFormatter: Formatter<DateTime> = getFormatter("MMMM yyyy")) : DateComponentsFormatter {
    override fun value(string: String): DateComponents =
        dateTimeFormatter.value(string).monthComponent()
}

private class YearFormatter(override val dateTimeFormatter: Formatter<DateTime> = getFormatter("yyyy")) : DateComponentsFormatter {
    override fun value(string: String): DateComponents =
        dateTimeFormatter.value(string).yearComponent()
}

private fun getFormatter(format: String): Formatter<DateTime> =
    getUtility().formatterFactory.getFormatter(format)
