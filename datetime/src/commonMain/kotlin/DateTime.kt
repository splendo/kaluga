package com.splendo.kaluga.datetime

import com.splendo.kaluga.datetime.datecomponents.Day
import com.splendo.kaluga.datetime.datecomponents.Month
import com.splendo.kaluga.datetime.datecomponents.Year

data class DateTime(
    val timestamp: Double
) {
    companion object {
        fun now(): DateTime {
            val millis = getTimeMillis()
            return DateTime(millis.toDouble())
        }
    }

    override fun equals(other: Any?): Boolean =
            if (other is DateTime) timestamp == other.timestamp
            else false
}

fun DateTime.millisUntil(dateTime: DateTime): Long =
        (dateTime.timestamp - timestamp).toLong()

// components

fun DateTime.components(): DateTimeComponents =
        getUtility().transform.componentsFromLocalDateTime(this)

fun DateTime.yearComponent(): Year {
    val components = this.components()
    return Year(components.year)
}

fun DateTime.monthComponent(): Month {
    val components = this.components()
    return Month(
        year = components.year,
        month = components.month
    )
}

fun DateTime.dayComponent(): Day {
    val components = this.components()
    return Day(
        year = components.year,
        month = components.month,
        day = components.dayOfMonth
    )
}
