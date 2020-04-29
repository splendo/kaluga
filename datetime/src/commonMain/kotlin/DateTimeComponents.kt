package com.splendo.kaluga.datetime

import com.splendo.kaluga.datetime.datecomponents.Day
import com.splendo.kaluga.datetime.datecomponents.Month
import com.splendo.kaluga.datetime.datecomponents.Year

data class DateTimeComponents(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val hours: Int,
    val minutes: Int
) {

    constructor(yearComponents: Year) : this(
        year = yearComponents.year,
        month = 0, dayOfMonth = 0, hours = 0, minutes = 0)

    constructor(monthComponents: Month) : this(
        year = monthComponents.year,
        month = monthComponents.month,
        dayOfMonth = 0, hours = 0, minutes = 0)

    constructor(dayComponents: Day) : this(
        year = dayComponents.year,
        month = dayComponents.month,
        dayOfMonth = dayComponents.day,
        hours = 0, minutes = 0)
}

fun DateTimeComponents.month0() = month - 1

fun DateTimeComponents.localDateTime(): DateTime =
        DateTimeUtility.transform.dateTimeFromLocalComponents(this)

fun DateTimeComponents.gmtDateTime(): DateTime =
        DateTimeUtility.transform.dateTimeFromGMTComponents(this)

fun DateTimeComponents.yearDateComponent(): Year = Year(year)

fun DateTimeComponents.monthDateComponent(): Month = Month(year, month)

fun DateTimeComponents.dayDateComponent(): Day = Day(year, month, dayOfMonth)
