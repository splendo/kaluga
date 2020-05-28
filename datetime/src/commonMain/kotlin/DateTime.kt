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

package com.splendo.kaluga.datetime

import com.splendo.kaluga.datetime.datecomponents.Day
import com.splendo.kaluga.datetime.datecomponents.Month
import com.splendo.kaluga.datetime.datecomponents.Year

data class DateTime(
    val timestamp: Double
) {
    companion object {
        fun now(): DateTime {
            val millis = getTimeMillis()
            return DateTime(millis.toDouble())
        }
    }

    override fun equals(other: Any?): Boolean =
        if (other is DateTime) timestamp == other.timestamp
        else false
}

fun DateTime.millisUntil(dateTime: DateTime): Long =
    (dateTime.timestamp - timestamp).toLong()

// components

fun DateTime.components(): DateTimeComponents =
    getUtility().transform.componentsFromLocalDateTime(this)

fun DateTime.yearComponent(): Year {
    val components = this.components()
    return Year(components.year)
}

fun DateTime.monthComponent(): Month {
    val components = this.components()
    return Month(
        year = components.year,
        month = components.month
    )
}

fun DateTime.dayComponent(): Day {
    val components = this.components()
    return Day(
        year = components.year,
        month = components.month,
        day = components.dayOfMonth
    )
}
