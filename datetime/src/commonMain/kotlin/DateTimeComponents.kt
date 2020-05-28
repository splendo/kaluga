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
        month = 0, dayOfMonth = 0, hours = 0, minutes = 0
    )

    constructor(monthComponents: Month) : this(
        year = monthComponents.year,
        month = monthComponents.month,
        dayOfMonth = 0, hours = 0, minutes = 0
    )

    constructor(dayComponents: Day) : this(
        year = dayComponents.year,
        month = dayComponents.month,
        dayOfMonth = dayComponents.day,
        hours = 0, minutes = 0
    )
}

fun DateTimeComponents.month0() = month - 1

fun DateTimeComponents.localDateTime(): DateTime =
    getUtility().transform.dateTimeFromLocalComponents(this)

fun DateTimeComponents.gmtDateTime(): DateTime =
    getUtility().transform.dateTimeFromGMTComponents(this)

fun DateTimeComponents.yearDateComponent(): Year = Year(year)

fun DateTimeComponents.monthDateComponent(): Month = Month(year, month)

fun DateTimeComponents.dayDateComponent(): Day = Day(year, month, dayOfMonth)
