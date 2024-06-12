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

import com.splendo.kaluga.base.text.KalugaDateFormatter
import com.splendo.kaluga.base.text.iso8601Pattern
import com.splendo.kaluga.base.utils.KalugaLocale.Companion.defaultLocale
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Class holding the platform value of the desired Date.
 */
expect class KalugaDateHolder

/**
 * Class describing a point in time
 * Dates are localized according to a [KalugaLocale] and relative to a given [KalugaTimeZone]
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
     * The [Duration] passed since epoch time (January 1st 1970 00:00:00:00 GMT)
     */
    abstract var durationSinceEpoch: Duration

    /**
     * The number of milliseconds passed since epoch time (January 1st 1970 00:00:00:00 GMT)
     */
    var millisecondSinceEpoch: Long
        get() = durationSinceEpoch.inWholeMilliseconds
        set(value) {
            durationSinceEpoch = value.milliseconds
        }

    /**
     * Creates a copy of a [KalugaDate]
     * @return A copy of this [KalugaDate]
     */
    abstract fun copy(): KalugaDate

    /**
     * Returns whether this Date is in the same [timeZone] and has the same time based on [durationSinceEpoch]
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
         * @param offset The [Duration] from the current time. Defaults to 0 milliseconds
         * @param timeZone The [KalugaTimeZone] in which the Date is set. Defaults to [KalugaTimeZone.current]
         * @param locale The [KalugaLocale] for which the Date is configured. Defaults to [KalugaLocale.defaultLocale]
         * @return A [KalugaDate] relative to the current time
         */
        fun now(offset: Duration = 0.milliseconds, timeZone: KalugaTimeZone = KalugaTimeZone.current(), locale: KalugaLocale = defaultLocale): KalugaDate

        /**
         * Creates a [KalugaDate] relative to January 1st 1970 00:00:00 GMT
         * @param offset The [Duration] from the epoch time. Defaults to 0 milliseconds
         * @param timeZone The [KalugaTimeZone] in which the Date is set. Defaults to [KalugaTimeZone.current]
         * @param locale The [KalugaLocale] for which the Date is configured. Defaults to [KalugaLocale.defaultLocale]
         * @return A [KalugaDate] relative to the current time
         */
        fun epoch(offset: Duration = 0.milliseconds, timeZone: KalugaTimeZone = KalugaTimeZone.current(), locale: KalugaLocale = defaultLocale): KalugaDate
    }

    override var timeZone: KalugaTimeZone
    override var era: Int
    override var year: Int
    override var month: Int
    override val daysInMonth: Int
    override var weekOfYear: Int
    override var weekOfMonth: Int
    override var day: Int
    override var dayOfYear: Int
    override var weekDay: Int
    override var firstWeekDay: Int
    override var hour: Int
    override var minute: Int
    override var second: Int
    override var millisecond: Int
    override var durationSinceEpoch: Duration
    override val date: KalugaDateHolder
    override fun copy(): KalugaDate
    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int

    override fun compareTo(other: KalugaDate): Int
}

/**
 * Creates a [KalugaDate] relative to the current time
 * @param offsetInMilliseconds The offset in milliseconds from the current time. Defaults to 0
 * @param timeZone The [KalugaTimeZone] in which the Date is set. Defaults to [KalugaTimeZone.current]
 * @param locale The [KalugaLocale] for which the Date is configured. Defaults to [KalugaLocale.defaultLocale]
 * @return A [KalugaDate] relative to the current time
 */
fun DefaultKalugaDate.Companion.now(offsetInMilliseconds: Long, timeZone: KalugaTimeZone = KalugaTimeZone.current(), locale: KalugaLocale = defaultLocale): KalugaDate =
    now(offsetInMilliseconds.milliseconds, timeZone, locale)

/**
 * Creates a [KalugaDate] relative to January 1st 1970 00:00:00 GMT
 * @param offsetInMilliseconds The offset in milliseconds from the epoch time. Defaults to 0
 * @param timeZone The [KalugaTimeZone] in which the Date is set. Defaults to [KalugaTimeZone.current]
 * @param locale The [KalugaLocale] for which the Date is configured. Defaults to [KalugaLocale.defaultLocale]
 * @return A [KalugaDate] relative to the current time
 */
fun DefaultKalugaDate.Companion.epoch(offsetInMilliseconds: Long, timeZone: KalugaTimeZone = KalugaTimeZone.current(), locale: KalugaLocale = defaultLocale): KalugaDate =
    epoch(offsetInMilliseconds.milliseconds, timeZone, locale)

/**
 * Gets the [Duration] between two [KalugaDate]
 * @param other the [KalugaDate] to subtract
 * @return the [Duration] between both dates.
 */
infix operator fun KalugaDate.minus(other: KalugaDate) = durationSinceEpoch - other.durationSinceEpoch

/**
 * Gets a [KalugaDate] that is [duration] after this date.
 * @param duration the [Duration] to add to this date.
 * @return A [KalugaDate] that is [duration] after this date
 */
infix operator fun KalugaDate.plus(duration: Duration) = copy().apply {
    durationSinceEpoch += duration
}

/**
 * Gets a [KalugaDate] that is [duration] before this date.
 * @param duration the [Duration] to subtract from this date.
 * @return A [KalugaDate] that is [duration] before this date
 */
infix operator fun KalugaDate.minus(duration: Duration) = this + (-duration)

/**
 * Creates a [KalugaDate] relative to the current time, in the UTC timezone
 * @param offset The [Duration] from the current time. Defaults to 0 milliseconds
 * @param locale The [KalugaLocale] for which the Date is configured. Defaults to [KalugaLocale.defaultLocale]
 * @return A [KalugaDate] relative to the current time, in the UTC timezone
 */
fun DefaultKalugaDate.Companion.nowUtc(offset: Duration = 0.milliseconds, locale: KalugaLocale = defaultLocale): KalugaDate = now(offset, KalugaTimeZone.utc, locale)

/**
 * Creates a [KalugaDate] relative to the current time, in the UTC timezone
 * @param offsetInMilliseconds The offset in milliseconds from the current time. Defaults to 0
 * @param locale The [KalugaLocale] for which the Date is configured. Defaults to [KalugaLocale.defaultLocale]
 * @return A [KalugaDate] relative to the current time, in the UTC timezone
 */
fun DefaultKalugaDate.Companion.nowUtc(offsetInMilliseconds: Long, locale: KalugaLocale = defaultLocale): KalugaDate = nowUtc(offsetInMilliseconds.milliseconds, locale)

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
 * Gets a [KalugaDate] equal to `23:59:59:999` on the same day as this Date
 * @return A [KalugaDate] equal to `23:59:59:999` on the same day as this Date
 */
fun KalugaDate.toEndOfDay() = this.copy().apply {
    hour = 23
    minute = 59
    second = 59
    millisecond = 999
}

/**
 * Gets a [KalugaDate] that is set at midnight on the same day as the current time.
 * @param timeZone The [KalugaTimeZone] in which the Date is set. Defaults to [KalugaTimeZone.current]
 * @param locale The [KalugaLocale] for which the Date is configured. Defaults to [KalugaLocale.defaultLocale]
 * @return A [KalugaDate] that is set at midnight on the same day as the current time.
 */
fun DefaultKalugaDate.Companion.today(timeZone: KalugaTimeZone = KalugaTimeZone.current(), locale: KalugaLocale = defaultLocale) =
    now(timeZone = timeZone, locale = locale).toStartOfDay()

/**
 * Gets a [KalugaDate] that is set at midnight on the day after the current time.
 * @param timeZone The [KalugaTimeZone] in which the Date is set. Defaults to [KalugaTimeZone.current]
 * @param locale The [KalugaLocale] for which the Date is configured. Defaults to [KalugaLocale.defaultLocale]
 * @return A [KalugaDate] that is set at midnight on the day after the current time.
 */
fun DefaultKalugaDate.Companion.tomorrow(timeZone: KalugaTimeZone = KalugaTimeZone.current(), locale: KalugaLocale = defaultLocale) =
    today(timeZone = timeZone, locale = locale).apply { day += 1 }

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
