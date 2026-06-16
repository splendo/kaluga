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

package com.splendo.kaluga.datetime
import com.splendo.kaluga.base.i18n.KalugaLocale

import com.splendo.kaluga.datetime.externals.LuxonDateTime
import com.splendo.kaluga.datetime.externals.luxonDateValues
import com.splendo.kaluga.datetime.externals.luxonFromMillis
import com.splendo.kaluga.datetime.externals.luxonFromObject
import com.splendo.kaluga.datetime.externals.luxonMinus
import com.splendo.kaluga.datetime.externals.luxonOptions
import com.splendo.kaluga.datetime.externals.luxonPlus
import com.splendo.kaluga.datetime.externals.luxonSet
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

private const val DEFAULT_LOCALE_TAG = "en-US"
private val MILLISECONDS_PER_DAY = 24.hours.toDouble(DurationUnit.MILLISECONDS)

/**
 * Converts a [LuxonDateTime] to the platform [KalugaDateHolder] (`js`: `kotlin.js.Date`; `wasmJs`: a
 * millisecond holder). Kept per-target because there is no JS `Date` class shared by the JS family.
 */
internal expect fun luxonToDateHolder(dateTime: LuxonDateTime): KalugaDateHolder

/**
 * Default implementation of [KalugaDate] backed by the
 * [`luxon`](https://moment.github.io/luxon/) JS library. Mirrors `java.util.Calendar` semantics:
 *
 * - Setters are lenient: in-range values map to luxon's `set` (preserves the rest of the date,
 *   correct around DST gaps where luxon normalises forward). Out-of-range values fall back to
 *   `plus`, so e.g. `month += 22` rolls the year over.
 * - Calendar arithmetic is timezone-aware: `day += 1` uses `plus({days: 1})` which preserves the
 *   local time-of-day across DST transitions.
 */
actual class DefaultKalugaDate internal constructor(private var dt: LuxonDateTime) : KalugaDate() {

    actual companion object {

        actual fun now(offset: Duration, timeZone: KalugaTimeZone, locale: KalugaLocale): KalugaDate {
            val ms = jsDateNow() + offset.inWholeMilliseconds.toDouble()
            return DefaultKalugaDate(luxonFromMillis(ms, luxonOptions(timeZone.identifier, locale.tag)))
        }

        actual fun epoch(offset: Duration, timeZone: KalugaTimeZone, locale: KalugaLocale): KalugaDate =
            DefaultKalugaDate(luxonFromMillis(offset.inWholeMilliseconds.toDouble(), luxonOptions(timeZone.identifier, locale.tag)))
    }

    actual override var timeZone: KalugaTimeZone
        get() = KalugaTimeZone(dt.zoneName)
        set(value) {
            dt = dt.setZone(value.identifier)
        }

    actual override var era: Int
        get() = if (dt.year >= 1) 1 else 0
        set(_) {
            // luxon has no settable era.
        }

    actual override var year: Int
        get() = dt.year
        set(value) {
            // year has effectively unlimited range — always use `set` to preserve other fields.
            setField("year", value)
        }

    actual override var month: Int
        get() = dt.month
        set(value) {
            setOrShift("month", "months", value, dt.month, 1, 12)
        }

    actual override val daysInMonth: Int
        get() = dt.daysInMonth

    actual override var weekOfYear: Int
        get() = computeWeekOfYear(dt)
        set(value) {
            shift("weeks", value - computeWeekOfYear(dt))
        }

    actual override var weekOfMonth: Int
        get() = computeWeekOfMonth(dt)
        set(value) {
            shift("weeks", value - computeWeekOfMonth(dt))
        }

    actual override var day: Int
        get() = dt.day
        set(value) {
            setOrShift("day", "days", value, dt.day, 1, dt.daysInMonth)
        }

    actual override var dayOfYear: Int
        get() = dt.ordinal
        set(value) {
            val max = if (dt.isInLeapYear) 366 else 365
            setOrShift("ordinal", "days", value, dt.ordinal, 1, max)
        }

    actual override var weekDay: Int
        get() = isoToCalendarWeekday(dt.weekday)
        set(value) {
            val currentCalendar = isoToCalendarWeekday(dt.weekday)
            if (value in 1..7) {
                val isoValue = calendarToIsoWeekday(value)
                setField("weekday", isoValue)
            } else {
                shift("days", value - currentCalendar)
            }
        }

    actual override var firstWeekDay: Int
        get() = firstWeekDayForLocale(dt.locale ?: DEFAULT_LOCALE_TAG)
        set(_) {
            // Derived from locale; not directly settable per-date with luxon.
        }

    actual override var hour: Int
        get() = dt.hour
        set(value) {
            setOrShift("hour", "hours", value, dt.hour, 0, 23)
        }

    actual override var minute: Int
        get() = dt.minute
        set(value) {
            setOrShift("minute", "minutes", value, dt.minute, 0, 59)
        }

    actual override var second: Int
        get() = dt.second
        set(value) {
            setOrShift("second", "seconds", value, dt.second, 0, 59)
        }

    actual override var millisecond: Int
        get() = dt.millisecond
        set(value) {
            setOrShift("millisecond", "milliseconds", value, dt.millisecond, 0, 999)
        }

    actual override var durationSinceEpoch: Duration
        get() = dt.toMillis().toLong().milliseconds
        set(value) {
            val zone = dt.zoneName
            val locale = dt.locale ?: DEFAULT_LOCALE_TAG
            dt = luxonFromMillis(value.inWholeMilliseconds.toDouble(), luxonOptions(zone, locale))
        }

    actual override val date: KalugaDateHolder
        get() = luxonToDateHolder(dt)

    actual override fun copy(): KalugaDate = DefaultKalugaDate(dt)

    actual override fun equals(other: Any?): Boolean = (other as? KalugaDate)?.let {
        timeZone == it.timeZone && durationSinceEpoch == it.durationSinceEpoch
    } ?: false

    actual override fun hashCode(): Int = dt.toMillis().hashCode()

    actual override fun compareTo(other: KalugaDate): Int = durationSinceEpoch.compareTo(other.durationSinceEpoch)

    private fun setOrShift(field: String, plural: String, value: Int, current: Int, min: Int, max: Int) {
        if (value in min..max) {
            setField(field, value)
        } else {
            shift(plural, value - current)
        }
    }

    private fun setField(field: String, value: Int) {
        dt = luxonSet(dt, field, value)
    }

    private fun shift(unit: String, delta: Int) {
        if (delta == 0) return
        dt = luxonPlus(dt, unit, delta)
    }
}

// luxon: 1=Monday … 7=Sunday (ISO). Java Calendar: 1=Sunday … 7=Saturday.
private fun isoToCalendarWeekday(isoWeekday: Int): Int = (isoWeekday % 7) + 1

private fun calendarToIsoWeekday(calendarWeekday: Int): Int = if (calendarWeekday == 1) 7 else calendarWeekday - 1

private fun firstWeekDayForLocale(localeTag: String): Int = try {
    isoToCalendarWeekday(localeFirstWeekday(localeTag))
} catch (_: Throwable) {
    1 // Sunday — matches `java.util.Calendar`'s default for unknown locales.
}

private fun minimalDaysForLocale(localeTag: String): Int = try {
    localeMinimalDays(localeTag)
} catch (_: Throwable) {
    1
}

private fun computeWeekOfYear(dt: LuxonDateTime): Int {
    val localeTag = dt.locale ?: DEFAULT_LOCALE_TAG
    val firstDayCalendar = firstWeekDayForLocale(localeTag)
    val firstDayIso = calendarToIsoWeekday(firstDayCalendar)
    val minimalDays = minimalDaysForLocale(localeTag)
    val zone = dt.zoneName
    val year = dt.year
    val firstWeekStart = computeFirstWeekStart(year, firstDayIso, minimalDays, zone)
    val dayStart = dt.startOf("day").toMillis()
    val weekStartMs = firstWeekStart.toMillis()
    if (dayStart < weekStartMs) {
        // Date is in the last week of the previous year.
        val prevFirst = computeFirstWeekStart(year - 1, firstDayIso, minimalDays, zone)
        val daysSince = ((dayStart - prevFirst.toMillis()) / MILLISECONDS_PER_DAY).toInt()
        return (daysSince / 7) + 1
    }
    val daysSince = ((dayStart - weekStartMs) / MILLISECONDS_PER_DAY).toInt()
    return (daysSince / 7) + 1
}

private fun computeFirstWeekStart(year: Int, firstDayIso: Int, minimalDays: Int, zone: String): LuxonDateTime {
    val jan1 = luxonFromObject(luxonDateValues(year, 1, 1), luxonOptions(zone))
    val jan1Weekday = jan1.weekday
    val daysBack = (jan1Weekday - firstDayIso + 7) % 7
    val weekStart = luxonMinus(jan1, "days", daysBack)
    val daysInJan1Week = 7 - daysBack
    return if (daysInJan1Week >= minimalDays) {
        weekStart
    } else {
        luxonPlus(weekStart, "weeks", 1)
    }
}

private fun computeWeekOfMonth(dt: LuxonDateTime): Int {
    val localeTag = dt.locale ?: DEFAULT_LOCALE_TAG
    val firstDayCalendar = firstWeekDayForLocale(localeTag)
    val firstDayIso = calendarToIsoWeekday(firstDayCalendar)
    val firstOfMonth = dt.startOf("month")
    val firstOfMonthWeekday = firstOfMonth.weekday
    val daysBack = (firstOfMonthWeekday - firstDayIso + 7) % 7
    val firstWeekStart = luxonMinus(firstOfMonth, "days", daysBack)
    val dayStart = dt.startOf("day").toMillis()
    val weekStartMs = firstWeekStart.toMillis()
    val daysSince = ((dayStart - weekStartMs) / MILLISECONDS_PER_DAY).toInt()
    return (daysSince / 7) + 1
}

// Reads `Intl.Locale`'s week info (`getWeekInfo()` / `.weekInfo`), defaulting to ISO Monday / 1 minimal day
// when the runtime doesn't expose it. Returned as scalars to stay free of JS-object interop across the JS family.
private fun localeFirstWeekday(tag: String): Int =
    js("(function(){ var l = new Intl.Locale(tag); var w = (typeof l.getWeekInfo === 'function' ? l.getWeekInfo() : l.weekInfo); return (w && w.firstDay) ? w.firstDay : 1; })()")

private fun localeMinimalDays(tag: String): Int = js(
    "(function(){ var l = new Intl.Locale(tag); var w = (typeof l.getWeekInfo === 'function' ? l.getWeekInfo() : l.weekInfo); return (w && w.minimalDays) ? w.minimalDays : 1; })()",
)
