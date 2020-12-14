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

import kotlinx.cinterop.useContents
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarOptions
import platform.Foundation.NSCalendarUnit
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitEra
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitNanosecond
import platform.Foundation.NSCalendarUnitSecond
import platform.Foundation.NSCalendarUnitWeekOfMonth
import platform.Foundation.NSCalendarUnitWeekOfYear
import platform.Foundation.NSCalendarUnitWeekday
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.compare
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.dateWithTimeIntervalSinceNow
import platform.Foundation.timeIntervalSince1970
import platform.darwin.NSInteger
import platform.darwin.NSUInteger
import kotlin.math.round

actual class Date internal constructor(private val calendar: NSCalendar, initialDate: NSDate) : Comparable<Date> {
    actual companion object {

        const val nanoSecondPerMilliSecond = 1000 * 1000

        actual fun now(offsetInMilliseconds: Long, timeZone: TimeZone, locale: Locale): Date {
            val calendar = NSCalendar.currentCalendar.apply {
                this.locale = locale.nsLocale
                this.timeZone = timeZone.timeZone
            }
            val date = NSDate.dateWithTimeIntervalSinceNow(offsetInMilliseconds.toDouble() / 1000.0)
            return Date(calendar, date)
        }
        actual fun epoch(offsetInMilliseconds: Long, timeZone: TimeZone, locale: Locale): Date {
            val calendar = NSCalendar.currentCalendar.apply {
                this.locale = locale.nsLocale
                this.timeZone = timeZone.timeZone
            }
            val date = NSDate.dateWithTimeIntervalSince1970(offsetInMilliseconds.toDouble() / 1000.0)
            return Date(calendar, date)
        }
    }

    internal var date: NSDate = initialDate

    actual var timeZone: TimeZone
        get() = TimeZone(calendar.timeZone)
        set(value) { calendar.timeZone = value.timeZone }

    actual var era: Int
        get() = calendar.component(NSCalendarUnitEra, fromDate = date).toInt()
        set(value) { updateDateForComponent(NSCalendarUnitEra, value) }
    actual var year: Int
        get() = calendar.component(NSCalendarUnitYear, fromDate = date).toInt()
        set(value) { updateDateForComponent(NSCalendarUnitYear, value) }
    actual var month: Int
        get() = calendar.component(NSCalendarUnitMonth, fromDate = date).toInt()
        set(value) { updateDateForComponent(NSCalendarUnitMonth, value) }
    actual val daysInMonth: Int get() = calendar.rangeOfUnit(NSCalendarUnitDay, NSCalendarUnitMonth, forDate = date).useContents { this.length.toInt() }
    actual var weekOfYear: Int
        get() = calendar.component(NSCalendarUnitWeekOfYear, fromDate = date).toInt()
        set(value) { updateDateForComponent(NSCalendarUnitWeekOfYear, value) }
    actual var weekOfMonth: Int
        get() = calendar.component(NSCalendarUnitWeekOfMonth, fromDate = date).toInt()
        set(value) { updateDateForComponent(NSCalendarUnitWeekOfMonth, value) }
    actual var day: Int
        get() = calendar.component(NSCalendarUnitDay, fromDate = date).toInt()
        set(value) { updateDateForComponent(NSCalendarUnitDay, value) }
    actual var dayOfYear: Int
        get() = calendar.ordinalityOfUnit(NSCalendarUnitDay, NSCalendarUnitYear, date).toInt()
        set(value) { updateDateForComponent(NSCalendarUnitDay, value - dayOfYear + day) }
    actual var weekDay: Int
        get() = calendar.component(NSCalendarUnitWeekday, fromDate = date).toInt()
        set(value) { updateDateForComponent(NSCalendarUnitWeekday, value) }
    actual var firstWeekDay: Int
        get() = (calendar.firstWeekday.toInt())
        set(value) { calendar.firstWeekday = value.toULong() as NSUInteger }

    actual var hour: Int
        get() = calendar.component(NSCalendarUnitHour, fromDate = date).toInt()
        set(value) { updateDateForComponent(NSCalendarUnitHour, value) }
    actual var minute: Int
        get() = calendar.component(NSCalendarUnitMinute, fromDate = date).toInt()
        set(value) { updateDateForComponent(NSCalendarUnitMinute, value) }
    actual var second: Int
        get() = calendar.component(NSCalendarUnitSecond, fromDate = date).toInt()
        set(value) { updateDateForComponent(NSCalendarUnitSecond, value) }
    actual var millisecond: Int
        get() = calendar.component(NSCalendarUnitNanosecond, fromDate = date).toInt() / nanoSecondPerMilliSecond
        set(value) { updateDateForComponent(NSCalendarUnitNanosecond, value * nanoSecondPerMilliSecond) }
    actual var millisecondSinceEpoch: Long
        get() {
            val time = date.timeIntervalSince1970
            val decimalDigits = (time % 1.0) * 1000
            return time.toLong() * 1000L + round(decimalDigits).toLong()
        }
        set(value) { date = NSDate.dateWithTimeIntervalSince1970(value.toDouble() / 1000.0) }

    actual fun copy(): Date = Date(calendar.copy() as NSCalendar, date.copy() as NSDate)

    actual override fun equals(other: Any?): Boolean {
        return (other as? Date)?.let { other ->
            calendar.calendarIdentifier == other.calendar.calendarIdentifier && millisecondSinceEpoch == other.millisecondSinceEpoch && this.calendar.timeZone == other.calendar.timeZone
        } ?: false
    }

    actual override fun hashCode(): Int {
        var result = calendar.calendarIdentifier.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }

    override fun compareTo(other: Date): Int = this.date.compare(other.date).toInt()

    private fun updateDateForComponent(component: NSCalendarUnit, value: Int) {
        val previousValue = calendar.component(component, this.date)
        calendar.dateByAddingUnit(component, (value - previousValue).toLong() as NSInteger, date, 0.toULong() as NSCalendarOptions)?.let {
            date = it
        }
    }

    override fun toString(): String {
        return date.toString()
    }
}
