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

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

actual typealias KalugaDateHolder = kotlin.js.Date

// TODO Implement with proper date solution for Java Script
/**
 * Default implementation of [KalugaDate]
 */
actual class DefaultKalugaDate internal constructor(override val date: KalugaDateHolder) : KalugaDate() {

    actual companion object {

        /**
         * Creates a [KalugaDate] relative to the current time
         * @param offset The [Duration] from the current time. Defaults to 0 milliseconds
         * @param timeZone The [KalugaTimeZone] in which the Date is set. Defaults to [KalugaTimeZone.current]
         * @param locale The [KalugaLocale] for which the Date is configured. Defaults to [KalugaLocale.defaultLocale]
         * @return A [KalugaDate] relative to the current time
         */
        actual fun now(
            offset: Duration,
            timeZone: KalugaTimeZone,
            locale: KalugaLocale,
        ): KalugaDate = DefaultKalugaDate(
            kotlin.js.Date(kotlin.js.Date.now() + offset.inWholeMilliseconds),
        )

        /**
         * Creates a [KalugaDate] relative to January 1st 1970 00:00:00 GMT
         * @param offset The [Duration] from the epoch time. Defaults to 0 milliseconds
         * @param timeZone The [KalugaTimeZone] in which the Date is set. Defaults to [KalugaTimeZone.current]
         * @param locale The [KalugaLocale] for which the Date is configured. Defaults to [KalugaLocale.defaultLocale]
         * @return A [KalugaDate] relative to the current time
         */
        actual fun epoch(offset: Duration, timeZone: KalugaTimeZone, locale: KalugaLocale): KalugaDate = DefaultKalugaDate(kotlin.js.Date(offset.inWholeMilliseconds))
    }

    override var timeZone: KalugaTimeZone // TODO
        get() = KalugaTimeZone()
        set(_) { }

    override var era: Int // TODO
        get() = 1
        set(_) { }
    override var year: Int
        get() = date.getUTCFullYear()
        set(value) { date.asDynamic().setUTCFullYear(value) }
    override var month: Int
        get() = date.getUTCMonth() + 1
        set(value) { date.asDynamic().setUTCMonth(value - 1) }
    override val daysInMonth: Int
        get() = kotlin.js.Date(kotlin.js.Date.UTC(date.getUTCFullYear(), date.getUTCMonth() + 1, 0)).getUTCDate()
    override var weekOfYear: Int // TODO
        get() = 0
        set(_) { }
    override var weekOfMonth: Int // TODO
        get() = 0
        set(_) { }
    override var day: Int // TODO
        get() = date.getUTCDate()
        set(value) { date.asDynamic().setUTCDate(value) }
    override var dayOfYear: Int // TODO
        get() = 0
        set(_) { }
    override var weekDay: Int // TODO
        get() = date.getUTCDay() + 1
        set(_) { }
    override var firstWeekDay: Int // TODO
        get() = 2
        set(_) { }

    override var hour: Int
        get() = date.getUTCHours()
        set(value) { date.asDynamic().setUTCHours(value) }
    override var minute: Int
        get() = date.getUTCMinutes()
        set(value) { date.asDynamic().setUTCMinutes(value) }
    override var second: Int
        get() = date.getUTCSeconds()
        set(value) { date.asDynamic().setUTCSeconds(value) }
    override var millisecond: Int
        get() = date.getUTCMilliseconds()
        set(value) { date.asDynamic().setUTCMilliseconds(value) }
    override var durationSinceEpoch: Duration
        get() = date.getTime().milliseconds
        set(value) { date.asDynamic().setTime(value.inWholeMilliseconds) }

    override fun copy(): KalugaDate = DefaultKalugaDate(kotlin.js.Date(date.getTime()))

    override fun equals(other: Any?): Boolean {
        return (other as? KalugaDate)?.let {
            durationSinceEpoch.inWholeMilliseconds == other.durationSinceEpoch.inWholeMilliseconds
        } ?: false
    }

    override fun compareTo(other: KalugaDate): Int {
        return when {
            durationSinceEpoch.inWholeMilliseconds < other.durationSinceEpoch.inWholeMilliseconds -> -1
            durationSinceEpoch.inWholeMilliseconds == other.durationSinceEpoch.inWholeMilliseconds -> 0
            else -> 1
        }
    }

    override fun hashCode(): Int {
        return date.hashCode()
    }
}
