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

import com.splendo.kaluga.base.externals.DateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

private const val DEFAULT_LOCALE_TAG = "en-US"
private val MILLISECONDS_PER_DAY = 24.hours.toDouble(DurationUnit.MILLISECONDS)

actual typealias KalugaDateHolder = kotlin.js.Date

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
actual class DefaultKalugaDate internal constructor(private var dt: dynamic) : KalugaDate() {

    actual companion object {

        actual fun now(offset: Duration, timeZone: KalugaTimeZone, locale: KalugaLocale): KalugaDate {
            val ms = (kotlin.js.Date.now() + offset.inWholeMilliseconds.toDouble())
            return DefaultKalugaDate(DateTime.fromMillis(ms, buildOpts(timeZone, locale)))
        }

        actual fun epoch(offset: Duration, timeZone: KalugaTimeZone, locale: KalugaLocale): KalugaDate =
            DefaultKalugaDate(DateTime.fromMillis(offset.inWholeMilliseconds.toDouble(), buildOpts(timeZone, locale)))
    }

    actual override var timeZone: KalugaTimeZone
        get() = KalugaTimeZone(dt.zoneName.unsafeCast<String>())
        set(value) {
            val zoneName = value.identifier
            dt = dt.setZone(zoneName)
        }

    actual override var era: Int
        get() = if (dt.year.unsafeCast<Int>() >= 1) 1 else 0
        set(_) {
            // luxon has no settable era.
        }

    actual override var year: Int
        get() = dt.year.unsafeCast<Int>()
        set(value) {
            // year has effectively unlimited range — always use `set` to preserve other fields.
            setField("year", value)
        }

    actual override var month: Int
        get() = dt.month.unsafeCast<Int>()
        set(value) {
            setOrShift("month", "months", value, dt.month.unsafeCast<Int>(), 1, 12)
        }

    actual override val daysInMonth: Int
        get() = dt.daysInMonth.unsafeCast<Int>()

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
        get() = dt.day.unsafeCast<Int>()
        set(value) {
            setOrShift("day", "days", value, dt.day.unsafeCast<Int>(), 1, dt.daysInMonth.unsafeCast<Int>())
        }

    actual override var dayOfYear: Int
        get() = dt.ordinal.unsafeCast<Int>()
        set(value) {
            val max = if (dt.isInLeapYear.unsafeCast<Boolean>()) 366 else 365
            setOrShift("ordinal", "days", value, dt.ordinal.unsafeCast<Int>(), 1, max)
        }

    actual override var weekDay: Int
        get() = isoToCalendarWeekday(dt.weekday.unsafeCast<Int>())
        set(value) {
            val currentCalendar = isoToCalendarWeekday(dt.weekday.unsafeCast<Int>())
            if (value in 1..7) {
                val isoValue = calendarToIsoWeekday(value)
                setField("weekday", isoValue)
            } else {
                shift("days", value - currentCalendar)
            }
        }

    actual override var firstWeekDay: Int
        get() = firstWeekDayForLocale(dt.locale.unsafeCast<String>())
        set(_) {
            // Derived from locale; not directly settable per-date with luxon.
        }

    actual override var hour: Int
        get() = dt.hour.unsafeCast<Int>()
        set(value) {
            setOrShift("hour", "hours", value, dt.hour.unsafeCast<Int>(), 0, 23)
        }

    actual override var minute: Int
        get() = dt.minute.unsafeCast<Int>()
        set(value) {
            setOrShift("minute", "minutes", value, dt.minute.unsafeCast<Int>(), 0, 59)
        }

    actual override var second: Int
        get() = dt.second.unsafeCast<Int>()
        set(value) {
            setOrShift("second", "seconds", value, dt.second.unsafeCast<Int>(), 0, 59)
        }

    actual override var millisecond: Int
        get() = dt.millisecond.unsafeCast<Int>()
        set(value) {
            setOrShift("millisecond", "milliseconds", value, dt.millisecond.unsafeCast<Int>(), 0, 999)
        }

    actual override var durationSinceEpoch: Duration
        get() = dt.toMillis().unsafeCast<Double>().toLong().milliseconds
        set(value) {
            val zone = dt.zoneName.unsafeCast<String>()
            val locale = dt.locale.unsafeCast<String?>() ?: DEFAULT_LOCALE_TAG
            dt = DateTime.fromMillis(value.inWholeMilliseconds.toDouble(), js("({zone: zone, locale: locale})"))
        }

    actual override val date: KalugaDateHolder
        get() = dt.toJSDate().unsafeCast<KalugaDateHolder>()

    actual override fun copy(): KalugaDate = DefaultKalugaDate(dt)

    actual override fun equals(other: Any?): Boolean = (other as? KalugaDate)?.let {
        timeZone == it.timeZone && durationSinceEpoch == it.durationSinceEpoch
    } ?: false

    actual override fun hashCode(): Int = dt.toMillis().unsafeCast<Double>().hashCode()

    actual override fun compareTo(other: KalugaDate): Int = durationSinceEpoch.compareTo(other.durationSinceEpoch)

    private fun setOrShift(field: String, plural: String, value: Int, current: Int, min: Int, max: Int) {
        if (value in min..max) {
            setField(field, value)
        } else {
            shift(plural, value - current)
        }
    }

    private fun setField(field: String, value: Int) {
        val opts: dynamic = js("({})")
        opts[field] = value
        dt = dt.set(opts)
    }

    private fun shift(unit: String, delta: Int) {
        if (delta == 0) return
        val duration: dynamic = js("({})")
        duration[unit] = delta
        dt = dt.plus(duration)
    }
}

private fun buildOpts(timeZone: KalugaTimeZone, locale: KalugaLocale): dynamic {
    val zone = timeZone.identifier
    val tag = locale.tag
    return js("({zone: zone, locale: tag})")
}

// luxon: 1=Monday … 7=Sunday (ISO). Java Calendar: 1=Sunday … 7=Saturday.
private fun isoToCalendarWeekday(isoWeekday: Int): Int = (isoWeekday % 7) + 1

private fun calendarToIsoWeekday(calendarWeekday: Int): Int = if (calendarWeekday == 1) 7 else calendarWeekday - 1

private fun firstWeekDayForLocale(localeTag: String): Int = try {
    val intlLocale = newIntlLocale(localeTag)
    val weekInfo = js("(typeof intlLocale.getWeekInfo === 'function' ? intlLocale.getWeekInfo() : intlLocale.weekInfo)")
    val firstDay = weekInfo?.firstDay?.unsafeCast<Int?>() ?: 1
    isoToCalendarWeekday(firstDay)
} catch (_: dynamic) {
    1 // Sunday — matches `java.util.Calendar`'s default for unknown locales.
}

private fun minimalDaysForLocale(localeTag: String): Int = try {
    val intlLocale = newIntlLocale(localeTag)
    val weekInfo = js("(typeof intlLocale.getWeekInfo === 'function' ? intlLocale.getWeekInfo() : intlLocale.weekInfo)")
    weekInfo?.minimalDays?.unsafeCast<Int?>() ?: 1
} catch (_: dynamic) {
    1
}

private fun computeWeekOfYear(dt: dynamic): Int {
    val localeTag = dt.locale.unsafeCast<String?>() ?: DEFAULT_LOCALE_TAG
    val firstDayCalendar = firstWeekDayForLocale(localeTag)
    val firstDayIso = calendarToIsoWeekday(firstDayCalendar)
    val minimalDays = minimalDaysForLocale(localeTag)
    val zone = dt.zoneName.unsafeCast<String>()
    val year = dt.year.unsafeCast<Int>()
    val firstWeekStart = computeFirstWeekStart(year, firstDayIso, minimalDays, zone)
    val dayStart = dt.startOf("day").toMillis().unsafeCast<Double>()
    val weekStartMs = firstWeekStart.toMillis().unsafeCast<Double>()
    if (dayStart < weekStartMs) {
        // Date is in the last week of the previous year.
        val prevFirst = computeFirstWeekStart(year - 1, firstDayIso, minimalDays, zone)
        val daysSince = ((dayStart - prevFirst.toMillis().unsafeCast<Double>()) / MILLISECONDS_PER_DAY).toInt()
        return (daysSince / 7) + 1
    }
    val daysSince = ((dayStart - weekStartMs) / MILLISECONDS_PER_DAY).toInt()
    return (daysSince / 7) + 1
}

private fun computeFirstWeekStart(year: Int, firstDayIso: Int, minimalDays: Int, zone: String): dynamic {
    val y = year
    val z = zone
    val jan1 = DateTime.fromObject(js("({year: y, month: 1, day: 1})"), js("({zone: z})"))
    val jan1Weekday = jan1.weekday.unsafeCast<Int>()
    val daysBack = (jan1Weekday - firstDayIso + 7) % 7
    val daysBackInt = daysBack
    val weekStart = jan1.minus(js("({days: daysBackInt})"))
    val daysInJan1Week = 7 - daysBack
    return if (daysInJan1Week >= minimalDays) {
        weekStart
    } else {
        weekStart.plus(js("({weeks: 1})"))
    }
}

private fun computeWeekOfMonth(dt: dynamic): Int {
    val localeTag = dt.locale.unsafeCast<String?>() ?: DEFAULT_LOCALE_TAG
    val firstDayCalendar = firstWeekDayForLocale(localeTag)
    val firstDayIso = calendarToIsoWeekday(firstDayCalendar)
    val firstOfMonth = dt.startOf("month")
    val firstOfMonthWeekday = firstOfMonth.weekday.unsafeCast<Int>()
    val daysBack = (firstOfMonthWeekday - firstDayIso + 7) % 7
    val daysBackInt = daysBack
    val firstWeekStart = firstOfMonth.minus(js("({days: daysBackInt})"))
    val dayStart = dt.startOf("day").toMillis().unsafeCast<Double>()
    val weekStartMs = firstWeekStart.toMillis().unsafeCast<Double>()
    val daysSince = ((dayStart - weekStartMs) / MILLISECONDS_PER_DAY).toInt()
    return (daysSince / 7) + 1
}
