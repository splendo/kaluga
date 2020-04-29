package com.splendo.kaluga.datetimewithklock

import com.splendo.kaluga.datetime.DateTimeFormatter
import com.splendo.kaluga.datetime.DateTimeFormatterFactory

object KlockDateTimeFormatterFactory : DateTimeFormatterFactory {
    override fun getFormatter(format: String, defaultTimeZoneOffset: Int): DateTimeFormatter =
            KlockDateTimeFormatter(format, defaultTimeZoneOffset = defaultTimeZoneOffset)

    override fun defaultTimeZoneOffset(): Int =
            klockLocalTimezoneOffset().totalMinutesInt / 60
}
