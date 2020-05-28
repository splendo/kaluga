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

package com.splendo.kaluga.datetimewithklock

import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.TimezoneOffset
import com.splendo.kaluga.datetime.DateTimeComponents

fun timestampFromKlockDateTimeTz(dateTime: DateTimeTz): Double =
    dateTime.utc.unixMillisDouble

fun timestampFromKlockLocalDateTime(localDateTime: com.soywiz.klock.DateTime): Double {
    val offset =
        klockLocalTimezoneOffset()
    val localTimeTz = DateTimeTz.local(localDateTime, offset)
    return timestampFromKlockDateTimeTz(
        localTimeTz
    )
}

fun klockDateTimeFromTimestamp(timestamp: Double): com.soywiz.klock.DateTime =
    DateTime(unixMillis = timestamp)

fun klockLocalTimezoneOffset(): TimezoneOffset =
    TimezoneOffset.local(time = com.soywiz.klock.DateTime.now())

fun klockDateTimeComponentsFromDateTimeTz(dateTime: com.soywiz.klock.DateTimeTz): DateTimeComponents =
    DateTimeComponents(
        year = dateTime.yearInt,
        month = dateTime.month1,
        dayOfMonth = dateTime.dayOfMonth,
        hours = dateTime.hours,
        minutes = dateTime.minutes
    )
