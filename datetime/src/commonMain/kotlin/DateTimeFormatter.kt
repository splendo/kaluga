package com.splendo.kaluga.datetime

import com.splendo.kaluga.formatted.Formatter

interface DateTimeFormatter: Formatter<DateTime> {
    val format: String
    val defaultTimeZoneOffset: Int
}

fun DateTimeFormatter.dateTimeFromAny(value: Any?): DateTime? {
    return value?.let {
        when(it) {
            is String -> value(it)
            is Long -> DateTime(it.toDouble())
            else -> throw Exception("Value: $$value. Cannot convert to date")
        }
    }
}
