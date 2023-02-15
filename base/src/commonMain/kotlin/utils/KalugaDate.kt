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
@file:JvmName("KalugaDateJvm")

package com.splendo.kaluga.base.utils

import com.splendo.kaluga.base.text.KalugaDateFormatter
import com.splendo.kaluga.base.text.iso8601Pattern
import com.splendo.kaluga.base.utils.Locale.Companion.defaultLocale
import kotlin.jvm.JvmName

/**
 * Class holding the platform value of the desired Date.
 */
expect class KalugaDateHolder

/**
 * Class describing a point in time
 * Dates are localized according to a [Locale] and relative to a given [KalugaTimeZone]
 */
abstract class KalugaDate : Comparable<KalugaDate> {
    /**
     * The [KalugaTimeZone] in which the Date is set
     */
    abstract var timeZone: KalugaTimeZone

    /**
     * The number of the era, e.g., AD or BC in the Julian calendar
     */
    abstract var era: Int

    /**
     * The year
     */
    abstract var year: Int

    /**
     * The month of the year. Starts at 1
     */
    abstract var month: Int

    /**
     * The number of days in the current month
     */
    abstract val daysInMonth: Int

    /**
     * The week number within the current year.
     */
    abstract var weekOfYear: Int

    /**
     * The week number within the current month
     */
    abstract var weekOfMonth: Int

    /**
     * The day of the current month
     */
    abstract var day: Int

    /**
     * The day of the current year
     */
    abstract var dayOfYear: Int

    /**
     * The day of the week. Starts at 1
     */
    abstract var weekDay: Int

    /**
     * The first day of the week. E.g. Sunday in the US, Monday in France. Starts at 1.
     */
    abstract var firstWeekDay: Int

    /**
     * The hour of the current day
     */
    abstract var hour: Int

    /**
     * The minute of the current hour
     */
    abstract var minute: Int

    /**
     * The second of the current minute
     */
    abstract var second: Int

    /**
     * The millisecond of the current second
     */
    abstract var millisecond: Int

    /**
     * The number of milliseconds passed since epoch time (January 1st 1970 00:00:00:00 GMT)
     */
    abstract var millisecondSinceEpoch: Long

    /**
     * Creates a copy of a [KalugaDate]
     * @return A copy of this [KalugaDate]
     */
    abstract fun copy(): KalugaDate

    /**
     * Returns whether this Date is in the same [timeZone] and has the same time based on [millisecondSinceEpoch]
     * @return `true` if the two dates are equal
     */
    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int

    override fun toString(): String {
        val formatter = KalugaDateFormatter.iso8601Pattern(timeZone)
        return formatter.format(this)
    }

    /**
     * Reference to the underlying [KalugaDateHolder]
     */
    abstract val date: KalugaDateHolder
}

/**
 * Default implementation of [KalugaDate]
 */
expect class DefaultKalugaDate : KalugaDate {
    companion object {

        /**
         * Creates a [KalugaDate] relative to the current time
         * @param offsetInMilliseconds The offset in milliseconds from the current time. Defaults to 0
         * @param timeZone The [KalugaTimeZone] in which the Date is set. Defaults to [KalugaTimeZone.current]
         * @param locale The [Locale] for which the Date is configured. Defaults to [Locale.defaultLocale]
         * @return A [KalugaDate] relative to the current time
         */
        fun now(offsetInMilliseconds: Long = 0L, timeZone: KalugaTimeZone = KalugaTimeZone.current(), locale: Locale = Locale.defaultLocale): KalugaDate

        /**
         * Creates a [KalugaDate] relative to January 1st 1970 00:00:00 GMT
         * @param offsetInMilliseconds The offset in milliseconds from the epoch time. Defaults to 0
         * @param timeZone The [KalugaTimeZone] in which the Date is set. Defaults to [KalugaTimeZone.current]
         * @param locale The [Locale] for which the Date is configured. Defaults to [Locale.defaultLocale]
         * @return A [KalugaDate] relative to the current time
         */
        fun epoch(offsetInMilliseconds: Long = 0L, timeZone: KalugaTimeZone = KalugaTimeZone.current(), locale: Locale = Locale.defaultLocale): KalugaDate
    }
}

@Deprecated("Due to name clashes with platform classes and API changes this class has been renamed and changed to an interface. It will be removed in a future release.", ReplaceWith("KalugaDate"))
typealias Date = KalugaDate

/**
 * Creates a [KalugaDate] with the same [Locale] and [KalugaTimeZone] as the left date, but earlier by the right date millisecondSinceEpoch
 * @param date The [KalugaDate] of which the millisecondSinceEpoch to subtract should be retrieved
 * @return A new [KalugaDate] with the same [Locale] and [KalugaTimeZone] as the left date, but earlier by the right date millisecondSinceEpoch
 */
operator fun KalugaDate.minus(date: KalugaDate): KalugaDate {
    return copy().apply {
        millisecondSinceEpoch -= date.millisecondSinceEpoch
    }
}

/**
 * Creates a [KalugaDate] with the same [Locale] and [KalugaTimeZone] as the left date, but later by the right date millisecondSinceEpoch
 * @param date The [KalugaDate] of which the millisecondSinceEpoch to add should be retrieved
 * @return A new [KalugaDate] with the same [Locale] and [KalugaTimeZone] as the left date, but later by the right date millisecondSinceEpoch
 */
operator fun KalugaDate.plus(date: KalugaDate): KalugaDate {
    return copy().apply {
        millisecondSinceEpoch += date.millisecondSinceEpoch
    }
}

/**
 * Creates a [KalugaDate] relative to the current time, in the UTC timezone
 * @param offsetInMilliseconds The offset in milliseconds from the current time. Defaults to 0
 * @param locale The [Locale] for which the Date is configured. Defaults to [Locale.defaultLocale]
 * @return A [KalugaDate] relative to the current time, in the UTC timezone
 */
fun DefaultKalugaDate.Companion.nowUtc(offsetInMilliseconds: Long = 0L, locale: Locale = defaultLocale): KalugaDate =
    now(offsetInMilliseconds, KalugaTimeZone.utc, locale)

/**
 * Gets a [KalugaDate] equal to midnight on the same day as this Date
 * @return A [KalugaDate] equal to midnight on the same day as this Date
 */
fun KalugaDate.toStartOfDay() = this.copy().apply {
    hour = 0
    minute = 0
    second = 0
    millisecond = 0
}

/**
 * Gets a [KalugaDate] that is set at midnight on the same day as the current time.
 * @param timeZone The [KalugaTimeZone] in which the Date is set. Defaults to [KalugaTimeZone.current]
 * @param locale The [Locale] for which the Date is configured. Defaults to [Locale.defaultLocale]
 * @return A [KalugaDate] that is set at midnight on the same day as the current time.
 */
fun DefaultKalugaDate.Companion.today(timeZone: KalugaTimeZone = KalugaTimeZone.current(), locale: Locale = defaultLocale) = now(timeZone = timeZone, locale = locale).toStartOfDay()

/**
 * Gets a [KalugaDate] that is set at midnight on the day after the current time.
 * @param timeZone The [KalugaTimeZone] in which the Date is set. Defaults to [KalugaTimeZone.current]
 * @param locale The [Locale] for which the Date is configured. Defaults to [Locale.defaultLocale]
 * @return A [KalugaDate] that is set at midnight on the day after the current time.
 */
fun DefaultKalugaDate.Companion.tomorrow(timeZone: KalugaTimeZone = KalugaTimeZone.current(), locale: Locale = defaultLocale) = today(timeZone = timeZone, locale = locale).apply { day += 1 }

/**
 * Checks whether a [KalugaDate] is on the same day as a given Date.
 * @param date The [KalugaDate] to check
 * @return `true` if this [KalugaDate] is on the same day as [date]
 */
fun KalugaDate.isOnSameDay(date: KalugaDate): Boolean {
    return this.era == date.era &&
        this.year == date.year &&
        this.month == date.month &&
        this.day == date.day
}

/**
 * Checks whether a [KalugaDate] is on the same month as a given Date.
 * @param date The [KalugaDate] to check
 * @return `true` if this [KalugaDate] is on the same month as [date]
 */
fun KalugaDate.isOnSameMonth(date: KalugaDate): Boolean {
    return this.era == date.era &&
        this.year == date.year &&
        this.month == date.month
}

/**
 * Checks whether a [KalugaDate] is in the same year as a given Date.
 * @param date The [KalugaDate] to check
 * @return `true` if this [KalugaDate] is in the same year as [date]
 */
fun KalugaDate.isInSameYear(date: KalugaDate): Boolean {
    return this.era == date.era &&
        this.year == date.year
}

/**
 * True if this [KalugaDate] is today
 */
val KalugaDate.isToday: Boolean
    get() = isOnSameDay(DefaultKalugaDate.now(timeZone = this.timeZone))

/**
 * True if this [KalugaDate] is yesterday
 */
val KalugaDate.isYesterday: Boolean
    get() = isOnSameDay(DefaultKalugaDate.now(timeZone = this.timeZone).apply { day -= 1 })

/**
 * True if this [KalugaDate] is tomorrow
 */
val KalugaDate.isTomorrow: Boolean
    get() = isOnSameDay(DefaultKalugaDate.now(timeZone = this.timeZone).apply { day += 1 })

/**
 * True if this [KalugaDate] is this month
 */
val KalugaDate.isThisMonth: Boolean
    get() = isOnSameMonth(DefaultKalugaDate.now(timeZone = this.timeZone))

/**
 * True if this [KalugaDate] is last month
 */
val KalugaDate.isLastMonth: Boolean
    get() = isOnSameMonth(DefaultKalugaDate.now(timeZone = this.timeZone).apply { month -= 1 })

/**
 * True if this [KalugaDate] is next month
 */
val KalugaDate.isNextMonth: Boolean
    get() = isOnSameMonth(DefaultKalugaDate.now(timeZone = this.timeZone).apply { month += 1 })

/**
 * True if this [KalugaDate] is this year
 */
val KalugaDate.isThisYear: Boolean
    get() = isInSameYear(DefaultKalugaDate.now(timeZone = this.timeZone))

/**
 * True if this [KalugaDate] is last year
 */
val KalugaDate.isLastYear: Boolean
    get() = isInSameYear(DefaultKalugaDate.now(timeZone = this.timeZone).apply { year -= 1 })

/**
 * True if this [KalugaDate] is next year
 */
val KalugaDate.isNextYear: Boolean
    get() = isInSameYear(DefaultKalugaDate.now(timeZone = this.timeZone).apply { year += 1 })
