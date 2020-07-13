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

expect class Date : Comparable<Date> {
    companion object {
        fun now(offsetInMilliseconds: Long = 0L, timeZone: TimeZone = TimeZone.current(), locale: Locale = defaultLocale): Date
        fun epoch(offsetInMilliseconds: Long = 0L, timeZone: TimeZone = TimeZone.current(), locale: Locale = defaultLocale): Date
    }

    var timeZone: TimeZone

    var era: Int
    var year: Int
    var month: Int
    var weekOfYear: Int
    var weekOfMonth: Int
    var day: Int
    var dayOfYear: Int
    var weekDay: Int

    var hour: Int
    var minute: Int
    var second: Int
    var millisecond: Int
    var millisecondSinceEpoch: Long

    fun minus(date: Date): Date
    fun plus(date: Date): Date
    fun copy(): Date
}