package com.splendo.kaluga.datetime.datecomponents

import com.splendo.kaluga.datetime.DateTime
import com.splendo.kaluga.datetime.DateTimeUtility
import com.splendo.kaluga.formatted.Formatter

interface DateComponentsFormatter : Formatter<DateComponents> {
    val dateTimeFormatter: Formatter<DateTime>

    override fun string(value: DateComponents): String =
        value.let {
            it.dateTime()
        }?.let {
            dateTimeFormatter.string(it)
        } ?: ""

    companion object {
        fun formatterForComponent(components: DateComponents): Formatter<DateComponents> =
                when (components) {
                is Day -> DayFormatter()
                is Month -> MonthFormatter()
                is Year -> YearFormatter()
                is DateComponentsUndefined -> formatterForComponent(components.components)
            }
    }
}

private class DayFormatter(override val dateTimeFormatter: Formatter<DateTime> = getFormatter("yyyy-MM-dd")) : DateComponentsFormatter {
    override fun value(string: String): DateComponents =
            dateTimeFormatter.value(string).dayComponent()
}

private class MonthFormatter(override val dateTimeFormatter: Formatter<DateTime> = getFormatter("MMMM yyyy")) : DateComponentsFormatter {
    override fun value(string: String): DateComponents =
            dateTimeFormatter.value(string).dayComponent()
}

private class YearFormatter(override val dateTimeFormatter: Formatter<DateTime> = getFormatter("yyyy")) : DateComponentsFormatter {
    override fun value(string: String): DateComponents =
            dateTimeFormatter.value(string).dayComponent()
}

private fun getFormatter(format: String): Formatter<DateTime> =
        DateTimeUtility.formatterFactory.getFormatter(format)
