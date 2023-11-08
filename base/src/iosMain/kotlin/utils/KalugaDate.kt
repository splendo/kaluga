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

package com.splendo.kaluga.base.utils

import kotlinx.cinterop.useContents
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarMatchNextTimePreservingSmallerUnits
import platform.Foundation.NSCalendarMatchPreviousTimePreservingSmallerUnits
import platform.Foundation.NSCalendarSearchBackwards
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
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

actual typealias KalugaDateHolder = NSDate

/**
 * Default implementation of [KalugaDate]
 */
actual class DefaultKalugaDate internal constructor(private val calendar: NSCalendar, initialDate: NSDate) : KalugaDate() {
    actual companion object {

        private val nanoSecondPerMilliSecond = 1.milliseconds.inWholeNanoseconds.toInt()

        /**
         * Creates a [KalugaDate] relative to the current time
         * @param offset The [Duration] from the current time. Defaults to 0 milliseconds
         * @param timeZone The [KalugaTimeZone] in which the Date is set. Defaults to [KalugaTimeZone.current]
         * @param locale The [KalugaLocale] for which the Date is configured. Defaults to [KalugaLocale.defaultLocale]
         * @return A [KalugaDate] relative to the current time
         */
        actual fun now(offset: Duration, timeZone: KalugaTimeZone, locale: KalugaLocale): KalugaDate {
            val calendar = NSCalendar.currentCalendar.apply {
                this.locale = locale.nsLocale
                this.timeZone = timeZone.timeZone
            }
            val date = NSDate.dateWithTimeIntervalSinceNow(offset.toDouble(DurationUnit.SECONDS))
            return DefaultKalugaDate(calendar, date)
        }

        /**
         * Creates a [KalugaDate] relative to January 1st 1970 00:00:00 GMT
         * @param offset The [Duration] from the epoch time. Defaults to 0 milliseconds
         * @param timeZone The [KalugaTimeZone] in which the Date is set. Defaults to [KalugaTimeZone.current]
         * @param locale The [KalugaLocale] for which the Date is configured. Defaults to [KalugaLocale.defaultLocale]
         * @return A [KalugaDate] relative to the current time
         */
        actual fun epoch(offset: Duration, timeZone: KalugaTimeZone, locale: KalugaLocale): KalugaDate {
            val calendar = NSCalendar.currentCalendar.apply {
                this.locale = locale.nsLocale
                this.timeZone = timeZone.timeZone
            }
            val date = NSDate.dateWithTimeIntervalSince1970(offset.toDouble(DurationUnit.SECONDS))
            return DefaultKalugaDate(calendar, date)
        }
    }

    override var date: NSDate = initialDate

    override var timeZone: KalugaTimeZone
        get() = KalugaTimeZone(calendar.timeZone)
        set(value) {
            calendar.timeZone = value.timeZone
        }
    override var era: Int
        get() = calendar.component(NSCalendarUnitEra, fromDate = date).toInt()
        set(value) {
            updateDateForComponent(NSCalendarUnitEra, value)
        }
    override var year: Int
        get() = calendar.component(NSCalendarUnitYear, fromDate = date).toInt()
        set(value) {
            updateDateForComponent(NSCalendarUnitYear, value)
        }
    override var month: Int
        get() = calendar.component(NSCalendarUnitMonth, fromDate = date).toInt()
        set(value) {
            updateDateForComponent(NSCalendarUnitMonth, value)
        }
    override val daysInMonth: Int get() = calendar.rangeOfUnit(NSCalendarUnitDay, NSCalendarUnitMonth, forDate = date).useContents { this.length.toInt() }
    override var weekOfYear: Int
        get() = calendar.component(NSCalendarUnitWeekOfYear, fromDate = date).toInt()
        set(value) {
            updateDateForComponent(NSCalendarUnitWeekOfYear, value)
        }
    override var weekOfMonth: Int
        get() = calendar.component(NSCalendarUnitWeekOfMonth, fromDate = date).toInt()
        set(value) {
            updateDateForComponent(NSCalendarUnitWeekOfMonth, value)
        }
    override var day: Int
        get() = calendar.component(NSCalendarUnitDay, fromDate = date).toInt()
        set(value) {
            updateDateForComponent(NSCalendarUnitDay, value)
        }
    override var dayOfYear: Int
        get() = calendar.ordinalityOfUnit(NSCalendarUnitDay, NSCalendarUnitYear, date).toInt()
        set(value) {
            updateDateForComponent(NSCalendarUnitDay, value - dayOfYear + day)
        }
    override var weekDay: Int
        get() = calendar.component(NSCalendarUnitWeekday, fromDate = date).toInt()
        set(value) {
            updateDateForComponent(NSCalendarUnitWeekday, value)
        }
    override var firstWeekDay: Int
        get() = (calendar.firstWeekday.toInt())
        set(value) {
            calendar.firstWeekday = value.toULong()
        }

    override var hour: Int
        get() = calendar.component(NSCalendarUnitHour, fromDate = date).toInt()
        set(value) {
            updateDateForComponent(NSCalendarUnitHour, value)
        }
    override var minute: Int
        get() = calendar.component(NSCalendarUnitMinute, fromDate = date).toInt()
        set(value) {
            updateDateForComponent(NSCalendarUnitMinute, value)
        }
    override var second: Int
        get() = calendar.component(NSCalendarUnitSecond, fromDate = date).toInt()
        set(value) {
            updateDateForComponent(NSCalendarUnitSecond, value)
        }
    override var millisecond: Int
        get() = calendar.component(NSCalendarUnitNanosecond, fromDate = date).toInt() / nanoSecondPerMilliSecond
        set(value) {
            updateDateForComponent(NSCalendarUnitNanosecond, value * nanoSecondPerMilliSecond)
        }
    override var durationSinceEpoch: Duration
        get() = date.timeIntervalSince1970.seconds
        set(value) {
            date = NSDate.dateWithTimeIntervalSince1970(value.toDouble(DurationUnit.SECONDS))
        }

    override fun copy(): KalugaDate = DefaultKalugaDate(calendar.copy() as NSCalendar, date.copy() as NSDate)

    override fun equals(other: Any?): Boolean {
        return if (other is DefaultKalugaDate) {
            calendar.calendarIdentifier == other.calendar.calendarIdentifier && durationSinceEpoch == other.durationSinceEpoch && this.calendar.timeZone == other.calendar.timeZone
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        var result = calendar.calendarIdentifier.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }

    override fun compareTo(other: KalugaDate): Int = this.date.compare(other.date).toInt()

    private fun updateDateForComponent(component: NSCalendarUnit, value: Int) {
        // Check whether this component update can use dateBySettingUnit.
        // This doesn't work properly when going out of bounds for a component (e.g. setting hour to 26)
        // Therefore we must ensure that the value is within bounds.
        // This is especially important for Minutes and Hours due to daylight savings,
        // as using dateByAddingUnit will be incorrect if changing between a date that has DLS and one that hasn't.
        val canSetComponent = when (component) {
            NSCalendarUnitMinute -> {
                calendar.rangeOfUnit(NSCalendarUnitMinute, NSCalendarUnitHour, date).useContents {
                    value >= location.toInt() && value < (location + length).toInt()
                }
            }
            NSCalendarUnitHour -> {
                calendar.rangeOfUnit(NSCalendarUnitHour, NSCalendarUnitDay, date).useContents {
                    value >= location.toInt() && value < (location + length).toInt()
                }
            }
            else -> {
                false
            }
        }
        if (canSetComponent) {
            // If the new value is lower than the old one, make sure we go backwards as otherwise the next highest component will increase
            val calendarOptions = if (value.toLong() < calendar.component(component, date)) {
                NSCalendarMatchPreviousTimePreservingSmallerUnits or NSCalendarSearchBackwards
            } else {
                NSCalendarMatchNextTimePreservingSmallerUnits
            }
            calendar.dateBySettingUnit(component, value.toLong(), date, calendarOptions)
        } else {
            val previousValue = calendar.component(component, this.date)
            calendar.dateByAddingUnit(
                component,
                (value - previousValue),
                date,
                0UL,
            )
        }?.let {
            date = it
        }
    }

    override fun toString(): String {
        return date.toString()
    }
}
