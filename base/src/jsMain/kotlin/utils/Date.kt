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

// TODO Implement with proper date solution for Java Script
actual class Date internal constructor(internal val date: kotlin.js.Date) : Comparable<Date> {

    actual companion object {
        actual fun now(offsetInMilliseconds: Long, timeZone: TimeZone, locale: Locale): Date = Date(kotlin.js.Date(kotlin.js.Date.now() + offsetInMilliseconds))
        actual fun epoch(offsetInMilliseconds: Long, timeZone: TimeZone, locale: Locale): Date = Date(kotlin.js.Date(offsetInMilliseconds))
    }

    actual var timeZone: TimeZone
        get() = TimeZone()
        set(value) { }

    actual var era: Int
        get() = 0
        set(value) { }
    actual var year: Int
        get() = date.getFullYear()
        set(value) { }
    actual var month: Int
        get() = date.getMonth()
        set(value) { }
    actual val daysInMonth: Int = 0
    actual var weekOfYear: Int
        get() = 0
        set(value) { }
    actual var weekOfMonth: Int
        get() = 0
        set(value) { }
    actual var day: Int
        get() = 0
        set(value) { }
    actual var dayOfYear: Int
        get() = date.getDay()
        set(value) { }
    actual var weekDay: Int
        get() = date.getDate() + 1
        set(value) { }
    actual var firstWeekDay: Int
        get() = 1
        set(value) { }

    actual var hour: Int
        get() = date.getHours()
        set(value) { }
    actual var minute: Int
        get() = date.getMinutes()
        set(value) { }
    actual var second: Int
        get() = date.getSeconds()
        set(value) { }
    actual var millisecond: Int
        get() = date.getMilliseconds()
        set(value) { }
    actual var millisecondSinceEpoch: Long
        get() = date.getTime().toLong()
        set(value) { }

    actual fun copy(): Date = Date(kotlin.js.Date(date.getMilliseconds()))

    actual override fun equals(other: Any?): Boolean {
        return (other as? Date)?.let {
            timeZone == other.timeZone && millisecondSinceEpoch == other.millisecondSinceEpoch
        } ?: false
    }

    override fun compareTo(other: Date): Int {
        return when {
            date.getMilliseconds() < other.millisecond -> -1
            date.getMilliseconds() == other.millisecond -> 0
            else -> 1
        }
    }

    actual override fun hashCode(): Int {
        return date.hashCode()
    }
}
