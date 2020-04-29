package com.splendo.kaluga.datetimewithklock

import com.splendo.kaluga.datetime.DateTime
import com.splendo.kaluga.datetime.DateTimeComponents
import com.splendo.kaluga.datetime.DateTimeTransformUtility

object KlockDateTimeTransformUtility :
    DateTimeTransformUtility {

    override fun dateTimeFromLocalComponents(components: DateTimeComponents): DateTime {
        val dt = com.soywiz.klock.DateTime(
            year = components.year,
            month = components.month,
            day = components.dayOfMonth,
            hour = components.hours,
            minute = components.minutes)

        val timestamp =
            timestampFromKlockLocalDateTime(
                dt
            )
        return DateTime(timestamp)
    }

    override fun componentsFromLocalDateTime(dateTime: DateTime): DateTimeComponents =
        klockDateTimeComponentsFromDateTimeTz(
            klockDateTimeFromTimestamp(
                dateTime.timestamp).local)

    override fun dateTimeFromGMTComponents(components: DateTimeComponents): DateTime {
        val dt = com.soywiz.klock.DateTime(
            year = components.year,
            month = components.month,
            day = components.dayOfMonth,
            hour = components.hours,
            minute = components.minutes).utc

        val timestamp = timestampFromKlockDateTimeTz(dt)
        return DateTime(timestamp)
    }

    override fun componentsFromGMTDateTime(dateTime: DateTime): DateTimeComponents =
        klockDateTimeComponentsFromDateTimeTz(
            klockDateTimeFromTimestamp(
                dateTime.timestamp
            ).utc
        )
}
