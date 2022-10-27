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
actual class DefaultKalugaDate internal constructor(override val date: KalugaDateHolder) : KalugaDate {

    actual companion object {
        actual fun now(offsetInMilliseconds: Long, timeZone: TimeZone, locale: Locale): KalugaDate = DefaultKalugaDate(kotlin.js.Date(kotlin.js.Date.now() + offsetInMilliseconds))
        actual fun epoch(offsetInMilliseconds: Long, timeZone: TimeZone, locale: Locale): KalugaDate = DefaultKalugaDate(kotlin.js.Date(offsetInMilliseconds))
    }

    override var timeZone: TimeZone
        get() = TimeZone()
        set(value) { }

    override var era: Int
        get() = 0
        set(value) { }
    override var year: Int
        get() = date.getFullYear()
        set(value) { }
    override var month: Int
        get() = date.getMonth()
        set(value) { }
    override val daysInMonth: Int = 0
    override var weekOfYear: Int
        get() = 0
        set(value) { }
    override var weekOfMonth: Int
        get() = 0
        set(value) { }
    override var day: Int
        get() = 0
        set(value) { }
    override var dayOfYear: Int
        get() = date.getDay()
        set(value) { }
    override var weekDay: Int
        get() = date.getDate() + 1
        set(value) { }
    override var firstWeekDay: Int
        get() = 1
        set(value) { }

    override var hour: Int
        get() = date.getHours()
        set(value) { }
    override var minute: Int
        get() = date.getMinutes()
        set(value) { }
    override var second: Int
        get() = date.getSeconds()
        set(value) { }
    override var millisecond: Int
        get() = date.getMilliseconds()
        set(value) { }
    override var millisecondSinceEpoch: Long
        get() = date.getTime().toLong()
        set(value) { }

    override fun copy(): KalugaDate = DefaultKalugaDate(kotlin.js.Date(date.getTime()))

    override fun equals(other: Any?): Boolean {
        return (other as? KalugaDate)?.let {
            timeZone == other.timeZone && millisecondSinceEpoch == other.millisecondSinceEpoch
        } ?: false
    }

    override fun compareTo(other: KalugaDate): Int {
        return when {
            date.getTime().toLong() < other.millisecondSinceEpoch -> -1
            date.getTime().toLong() == other.millisecondSinceEpoch -> 0
            else -> 1
        }
    }

    override fun hashCode(): Int {
        return date.hashCode()
    }
}
