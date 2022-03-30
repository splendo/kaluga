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
import java.util.Date

actual typealias KalugaDateHolder = Date

actual class DefaultKalugaDate internal constructor(internal val calendar: Calendar) : KalugaDate {

    actual companion object {
        actual fun now(offsetInMilliseconds: Long, timeZone: TimeZone, locale: Locale): KalugaDate = DefaultKalugaDate(
            Calendar.getInstance(timeZone.timeZone, locale.locale).apply {
                add(Calendar.MILLISECOND, offsetInMilliseconds.toInt())
            }
        )
        actual fun epoch(offsetInMilliseconds: Long, timeZone: TimeZone, locale: Locale): KalugaDate = DefaultKalugaDate(
            Calendar.getInstance(timeZone.timeZone, locale.locale).apply {
                timeInMillis = offsetInMilliseconds
            }
        )
    }

    override var timeZone: TimeZone
        get() = TimeZone(calendar.timeZone)
        set(value) { calendar.timeZone = value.timeZone }

    override var era: Int
        get() = calendar.get(Calendar.ERA)
        set(value) { calendar.set(Calendar.ERA, value) }
    override var year: Int
        get() = calendar.get(Calendar.YEAR)
        set(value) { calendar.set(Calendar.YEAR, value) }
    override var month: Int
        get() = calendar.get(Calendar.MONTH) + 1
        set(value) { calendar.set(Calendar.MONTH, value - 1) }
    override val daysInMonth: Int get() = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    override var weekOfYear: Int
        get() = calendar.get(Calendar.WEEK_OF_YEAR)
        set(value) { calendar.set(Calendar.WEEK_OF_YEAR, value) }
    override var weekOfMonth: Int
        get() = calendar.get(Calendar.WEEK_OF_MONTH)
        set(value) { calendar.set(Calendar.WEEK_OF_MONTH, value) }
    override var day: Int
        get() = calendar.get(Calendar.DAY_OF_MONTH)
        set(value) { calendar.set(Calendar.DAY_OF_MONTH, value) }
    override var dayOfYear: Int
        get() = calendar.get(Calendar.DAY_OF_YEAR)
        set(value) { calendar.set(Calendar.DAY_OF_YEAR, value) }
    override var weekDay: Int
        get() = calendar.get(Calendar.DAY_OF_WEEK)
        set(value) { calendar.set(Calendar.DAY_OF_WEEK, value) }
    override var firstWeekDay: Int
        get() = calendar.firstDayOfWeek
        set(value) { calendar.firstDayOfWeek = value }

    override var hour: Int
        get() = calendar.get(Calendar.HOUR_OF_DAY)
        set(value) { calendar.set(Calendar.HOUR_OF_DAY, value) }
    override var minute: Int
        get() = calendar.get(Calendar.MINUTE)
        set(value) { calendar.set(Calendar.MINUTE, value) }
    override var second: Int
        get() = calendar.get(Calendar.SECOND)
        set(value) { calendar.set(Calendar.SECOND, value) }
    override var millisecond: Int
        get() = calendar.get(Calendar.MILLISECOND)
        set(value) { calendar.set(Calendar.MILLISECOND, value) }
    override var millisecondSinceEpoch: Long
        get() = calendar.timeInMillis
        set(value) { calendar.timeInMillis = value }

    override fun copy(): KalugaDate = DefaultKalugaDate(calendar.clone() as Calendar)

    override fun equals(other: Any?): Boolean {
        return (other as? KalugaDate)?.let {
            timeZone == other.timeZone && millisecondSinceEpoch == other.millisecondSinceEpoch
        } ?: false
    }

    override fun hashCode(): Int = calendar.hashCode()
    override val date: KalugaDateHolder get() = calendar.time

    override fun compareTo(other: KalugaDate): Int {
        return this.calendar.time.compareTo(other.date)
    }
}
