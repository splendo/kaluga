package com.splendo.kaluga.datetimewithklock

import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.TimezoneOffset
import com.soywiz.klock.parse
import com.splendo.kaluga.datetime.DateTimeFormatter

class KlockDateTimeFormatter(override val format: String, override val defaultTimeZoneOffset: Int) : DateTimeFormatter {
    private companion object {
        const val millisInAnHour = 1000 * 60 * 60
    }
    private val dateFormat = DateFormat(format)
    override fun value(string: String): com.splendo.kaluga.datetime.DateTime {
        val dt = dateFormat.parse(string)
        var timestamp =
            timestampFromKlockDateTimeTz(
                dt
            )

        if (!includedTimeZone(dateFormat.format)) {
            timestamp -= defaultTimeZoneOffset * millisInAnHour
        }

        return com.splendo.kaluga.datetime.DateTime(timestamp)
    }

    override fun string(value: com.splendo.kaluga.datetime.DateTime): String =
        if (!includedTimeZone(dateFormat.format)) {
            DateTime(unixMillis = value.timestamp + defaultTimeZoneOffset * millisInAnHour).format(dateFormat)
            } else {
            val dateTime = DateTime(unixMillis = value.timestamp)
            val timezoneOffset = TimezoneOffset((defaultTimeZoneOffset * millisInAnHour).toDouble())
            val dateTimeTz = DateTimeTz.local(dateTime, timezoneOffset)
            dateTimeTz.format(dateFormat)
            }

    private val timezoneRegEx = "(z|z{3}|Z|x{1,3}|X{1,3})".toRegex()
    private fun includedTimeZone(format: String) =
        timezoneRegEx.containsMatchIn(format)
}
