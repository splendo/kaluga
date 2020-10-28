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

package com.splendo.kaluga.base.utils

import java.util.Calendar

actual class Date internal constructor(internal val calendar: Calendar) : Comparable<Date> {

    actual companion object {
        actual fun now(offsetInMilliseconds: Long, timeZone: TimeZone, locale: Locale): Date = Date(Calendar.getInstance(timeZone.timeZone, locale.locale).apply {
                add(Calendar.MILLISECOND, offsetInMilliseconds.toInt())
            })
        actual fun epoch(offsetInMilliseconds: Long, timeZone: TimeZone, locale: Locale): Date = Date(Calendar.getInstance(timeZone.timeZone, locale.locale).apply {
            timeInMillis = offsetInMilliseconds
        })
    }

    actual var timeZone: TimeZone
        get() = TimeZone(calendar.timeZone)
        set(value) { calendar.timeZone = value.timeZone }

    actual var era: Int
        get() = calendar.get(Calendar.ERA)
        set(value) { calendar.set(Calendar.ERA, value) }
    actual var year: Int
        get() = calendar.get(Calendar.YEAR)
        set(value) { calendar.set(Calendar.YEAR, value) }
    actual var month: Int
        get() = calendar.get(Calendar.MONTH) + 1
        set(value) { calendar.set(Calendar.MONTH, value - 1) }
    actual var weekOfYear: Int
        get() = calendar.get(Calendar.WEEK_OF_YEAR)
        set(value) { calendar.set(Calendar.WEEK_OF_YEAR, value) }
    actual var weekOfMonth: Int
        get() = calendar.get(Calendar.WEEK_OF_MONTH)
        set(value) { calendar.set(Calendar.WEEK_OF_MONTH, value) }
    actual var day: Int
        get() = calendar.get(Calendar.DAY_OF_MONTH)
        set(value) { calendar.set(Calendar.DAY_OF_MONTH, value) }
    actual var dayOfYear: Int
        get() = calendar.get(Calendar.DAY_OF_YEAR)
        set(value) { calendar.set(Calendar.DAY_OF_YEAR, value) }
    actual var weekDay: Int
        get() = calendar.get(Calendar.DAY_OF_WEEK)
        set(value) { calendar.set(Calendar.DAY_OF_WEEK, value) }
    actual var firstWeekDay: Int
        get() = calendar.firstDayOfWeek
        set(value) { calendar.firstDayOfWeek = value }

    actual var hour: Int
        get() = calendar.get(Calendar.HOUR_OF_DAY)
        set(value) { calendar.set(Calendar.HOUR_OF_DAY, value) }
    actual var minute: Int
        get() = calendar.get(Calendar.MINUTE)
        set(value) { calendar.set(Calendar.MINUTE, value) }
    actual var second: Int
        get() = calendar.get(Calendar.SECOND)
        set(value) { calendar.set(Calendar.SECOND, value) }
    actual var millisecond: Int
        get() = calendar.get(Calendar.MILLISECOND)
        set(value) { calendar.set(Calendar.MILLISECOND, value) }
    actual var millisecondSinceEpoch: Long
        get() = calendar.timeInMillis
        set(value) { calendar.timeInMillis = value }

    actual fun copy(): Date = Date(calendar.clone() as Calendar)

    actual override fun equals(other: Any?): Boolean {
        return (other as? Date)?.let {
            timeZone == other.timeZone && millisecondSinceEpoch == other.millisecondSinceEpoch
        } ?: false
    }

    override fun compareTo(other: Date): Int {
        return this.calendar.time.compareTo(other.calendar.time)
    }

    actual override fun hashCode(): Int {
        return calendar.hashCode()
    }
}
