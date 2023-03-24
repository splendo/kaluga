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

actual typealias KalugaDateHolder = kotlin.js.Date

// TODO Implement with proper date solution for Java Script
actual class DefaultKalugaDate internal constructor(override val date: KalugaDateHolder) : KalugaDate() {

    actual companion object {
        actual fun now(offsetInMilliseconds: Long, timeZone: TimeZone, locale: Locale): KalugaDate = DefaultKalugaDate(kotlin.js.Date(kotlin.js.Date.now() + offsetInMilliseconds))
        actual fun epoch(offsetInMilliseconds: Long, timeZone: TimeZone, locale: Locale): KalugaDate = DefaultKalugaDate(kotlin.js.Date(offsetInMilliseconds))
    }

    override var timeZone: TimeZone // TODO
        get() = TimeZone()
        set(value) { }

    override var era: Int // TODO
        get() = 1
        set(value) { }
    override var year: Int
        get() = date.getUTCFullYear()
        set(value) { date.asDynamic().setUTCFullYear(value) }
    override var month: Int
        get() = date.getUTCMonth() + 1
        set(value) { date.asDynamic().setUTCMonth(value - 1) }
    override val daysInMonth: Int get() = kotlin.js.Date(kotlin.js.Date.UTC(date.getUTCFullYear(), date.getUTCMonth() + 1, 0)).getUTCDate()
    override var weekOfYear: Int // TODO
        get() = 0
        set(value) { }
    override var weekOfMonth: Int // TODO
        get() = 0
        set(value) { }
    override var day: Int
        get() = date.getUTCDate()
        set(value) { date.asDynamic().setUTCDate(value) }
    override var dayOfYear: Int // TODO
        get() = 0
        set(value) { }
    override var weekDay: Int // TODO
        get() = date.getUTCDay() + 1
        set(value) { }
    override var firstWeekDay: Int // TODO
        get() = 2
        set(value) { }

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
    override var millisecondSinceEpoch: Long
        get() = date.getTime().toLong()
        set(value) { date.asDynamic().setTime(value) }

    override fun copy(): KalugaDate = DefaultKalugaDate(kotlin.js.Date(date.getTime()))

    override fun equals(other: Any?): Boolean {
        return (other as? KalugaDate)?.let {
            millisecondSinceEpoch == other.millisecondSinceEpoch
        } ?: false
    }

    override fun compareTo(other: KalugaDate): Int {
        return when {
            millisecondSinceEpoch < other.millisecondSinceEpoch -> -1
            millisecondSinceEpoch == other.millisecondSinceEpoch -> 0
            else -> 1
        }
    }

    override fun hashCode(): Int {
        return date.hashCode()
    }
}
