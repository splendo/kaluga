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
