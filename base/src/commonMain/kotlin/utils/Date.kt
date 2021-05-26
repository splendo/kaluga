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

import com.splendo.kaluga.base.utils.Locale.Companion.defaultLocale

/**
 * Class describing a point in time
 * Dates are localized according to a [Locale] and relative to a given [TimeZone]
 */
expect class Date : Comparable<Date> {
    companion object {
        /**
         * Creates a [Date] relative to the current time
         * @param offsetInMilliseconds The offset in milliseconds from the current time. Defaults to 0
         * @param timeZone The [TimeZone] in which the Date is set. Defaults to [TimeZone.current]
         * @param locale The [Locale] for which the Date is configured. Defaults to [Locale.defaultLocale]
         * @return A [Date] relative to the current time
         */
        fun now(offsetInMilliseconds: Long = 0L, timeZone: TimeZone = TimeZone.current(), locale: Locale = defaultLocale): Date

        /**
         * Creates a [Date] relative to January 1st 1970 00:00:00 GMT
         * @param offsetInMilliseconds The offset in milliseconds from the epoch time. Defaults to 0
         * @param timeZone The [TimeZone] in which the Date is set. Defaults to [TimeZone.current]
         * @param locale The [Locale] for which the Date is configured. Defaults to [Locale.defaultLocale]
         * @return A [Date] relative to the current time
         */
        fun epoch(offsetInMilliseconds: Long = 0L, timeZone: TimeZone = TimeZone.current(), locale: Locale = defaultLocale): Date
    }

    /**
     * The [TimeZone] in which the Date is set
     */
    var timeZone: TimeZone

    /**
     * The number of the era, e.g., AD or BC in the Julian calendar
     */
    var era: Int

    /**
     * The year
     */
    var year: Int

    /**
     * The month of the year. Starts at 1
     */
    var month: Int

    /**
     * The number of days in the current month
     */
    val daysInMonth: Int

    /**
     * The week number within the current year.
     */
    var weekOfYear: Int

    /**
     * The week number within the current month
     */
    var weekOfMonth: Int

    /**
     * The day of the current month
     */
    var day: Int

    /**
     * The day of the current year
     */
    var dayOfYear: Int

    /**
     * The day of the week. Starts at 1
     */
    var weekDay: Int

    /**
     * The first day of the week. E.g. Sunday in the US, Monday in France. Starts at 1.
     */
    var firstWeekDay: Int

    /**
     * The hour of the current day
     */
    var hour: Int

    /**
     * The minute of the current hour
     */
    var minute: Int

    /**
     * The second of the current minute
     */
    var second: Int

    /**
     * The millisecond of the current second
     */
    var millisecond: Int

    /**
     * The number of milliseconds passed since epoch time (January 1st 1970 00:00:00:00 GMT)
     */
    var millisecondSinceEpoch: Long

    /**
     * Creates a copy of a [Date]
     * @return A copy of this [Date]
     */
    fun copy(): Date

    /**
     * Returns whether this Date is in the same [timeZone] and has the same time based on [millisecondSinceEpoch]
     * @return `true` if the two dates are equal
     */
    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}

/**
 * Creates a [Date] with the same [Locale] and [TimeZone] as the left date, but earlier by the right date millisecondSinceEpoch
 * @param date The [Date] of which the millisecondSinceEpoch to subtract should be retrieved
 * @return A new [Date] with the same [Locale] and [TimeZone] as the left date, but earlier by the right date millisecondSinceEpoch
 */
operator fun Date.minus(date: Date): Date {
    return copy().apply {
        millisecondSinceEpoch -= date.millisecondSinceEpoch
    }
}

/**
 * Creates a [Date] with the same [Locale] and [TimeZone] as the left date, but later by the right date millisecondSinceEpoch
 * @param date The [Date] of which the millisecondSinceEpoch to add should be retrieved
 * @return A new [Date] with the same [Locale] and [TimeZone] as the left date, but later by the right date millisecondSinceEpoch
 */
operator fun Date.plus(date: Date): Date {
    return copy().apply {
        millisecondSinceEpoch += date.millisecondSinceEpoch
    }
}

/**
 * Creates a [Date] relative to the current time, in the UTC timezone
 * @param offsetInMilliseconds The offset in milliseconds from the current time. Defaults to 0
 * @param locale The [Locale] for which the Date is configured. Defaults to [Locale.defaultLocale]
 * @return A [Date] relative to the current time, in the UTC timezone
 */
fun Date.Companion.nowUtc(offsetInMilliseconds: Long = 0L, locale: Locale = defaultLocale): Date =
    now(offsetInMilliseconds, TimeZone.utc, locale)

/**
 * Gets a [Date] equal to midnight on the same day as this Date
 * @return A [Date] equal to midnight on the same day as this Date
 */
fun Date.toStartOfDay() = this.copy().apply {
    hour = 0
    minute = 0
    second = 0
    millisecond = 0
}

/**
 * Gets a [Date] that is set at midnight on the same day as the current time.
 * @param timeZone The [TimeZone] in which the Date is set. Defaults to [TimeZone.current]
 * @param locale The [Locale] for which the Date is configured. Defaults to [Locale.defaultLocale]
 * @return A [Date] that is set at midnight on the same day as the current time.
 */
fun Date.Companion.today(timeZone: TimeZone = TimeZone.current(), locale: Locale = defaultLocale) = now(timeZone = timeZone, locale = locale).toStartOfDay()

/**
 * Gets a [Date] that is set at midnight on the day after the current time.
 * @param timeZone The [TimeZone] in which the Date is set. Defaults to [TimeZone.current]
 * @param locale The [Locale] for which the Date is configured. Defaults to [Locale.defaultLocale]
 * @return A [Date] that is set at midnight on the day after the current time.
 */
fun Date.Companion.tomorrow(timeZone: TimeZone = TimeZone.current(), locale: Locale = defaultLocale) = today(timeZone = timeZone, locale = locale).apply { day += 1 }

/**
 * Checks whether a [Date] is on the same day as a given Date.
 * @param date The [Date] to check
 * @return `true` if this [Date] is on the same day as [date]
 */
fun Date.isOnSameDay(date: Date): Boolean {
    return this.era == date.era &&
        this.year == date.year &&
        this.month == date.month &&
        this.day == date.day
}

/**
 * Checks whether a [Date] is on the same month as a given Date.
 * @param date The [Date] to check
 * @return `true` if this [Date] is on the same month as [date]
 */
fun Date.isOnSameMonth(date: Date): Boolean {
    return this.era == date.era &&
        this.year == date.year &&
        this.month == date.month
}

/**
 * Checks whether a [Date] is in the same year as a given Date.
 * @param date The [Date] to check
 * @return `true` if this [Date] is in the same year as [date]
 */
fun Date.isInSameYear(date: Date): Boolean {
    return this.era == date.era &&
        this.year == date.year
}

/**
 * True if this [Date] is today
 */
val Date.isToday: Boolean
    get() = isOnSameDay(Date.now(timeZone = this.timeZone))

/**
 * True if this [Date] is yesterday
 */
val Date.isYesterday: Boolean
    get() = isOnSameDay(Date.now(timeZone = this.timeZone).apply { day -= 1 })

/**
 * True if this [Date] is tomorrow
 */
val Date.isTomorrow: Boolean
    get() = isOnSameDay(Date.now(timeZone = this.timeZone).apply { day += 1 })

/**
 * True if this [Date] is this month
 */
val Date.isThisMonth: Boolean
    get() = isOnSameMonth(Date.now(timeZone = this.timeZone))

/**
 * True if this [Date] is last month
 */
val Date.isLastMonth: Boolean
    get() = isOnSameMonth(Date.now(timeZone = this.timeZone).apply { month -= 1 })

/**
 * True if this [Date] is next month
 */
val Date.isNextMonth: Boolean
    get() = isOnSameMonth(Date.now(timeZone = this.timeZone).apply { month += 1 })

/**
 * True if this [Date] is this year
 */
val Date.isThisYear: Boolean
    get() = isInSameYear(Date.now(timeZone = this.timeZone))

/**
 * True if this [Date] is last year
 */
val Date.isLastYear: Boolean
    get() = isInSameYear(Date.now(timeZone = this.timeZone).apply { year -= 1 })

/**
 * True if this [Date] is next year
 */
val Date.isNextYear: Boolean
    get() = isInSameYear(Date.now(timeZone = this.timeZone).apply { year += 1 })
