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

package com.splendo.kaluga.datetime.datecomponents

import com.splendo.kaluga.datetime.DateTime
import com.splendo.kaluga.datetime.DateTimeComponents
import com.splendo.kaluga.datetime.components
import com.splendo.kaluga.datetime.dayDateComponent
import com.splendo.kaluga.datetime.gmtDateTime
import com.splendo.kaluga.datetime.monthDateComponent
import com.splendo.kaluga.datetime.yearDateComponent

sealed class DateComponents {
    abstract fun dateTime(): DateTime?

    companion object {
        fun undefinedYear(): DateComponentsUndefined =
            DateComponentsUndefined(
                Year(0)
            )

        fun undefinedMonth(): DateComponentsUndefined =
            DateComponentsUndefined(
                Month(0, 0)
            )

        fun undefinedDay(): DateComponentsUndefined =
            DateComponentsUndefined(
                Day(0, 0, 0)
            )
    }

    fun intYear(): Int? =
        when (this) {
            is Year -> this.year
            is Month -> this.year
            is Day -> this.year
            is DateComponentsUndefined -> null
        }

    fun intMonth(): Int? =
        when (this) {
            is Year -> null
            is Month -> this.month
            is Day -> this.month
            is DateComponentsUndefined -> null
        }

    fun intMonth0(): Int? = intMonth()?.let { it - 1 }

    fun intDay(): Int? =
        when (this) {
            is Year -> null
            is Month -> null
            is Day -> this.day
            is DateComponentsUndefined -> null
        }
}

data class Year(val year: Int) : DateComponents() {
    override fun dateTime(): DateTime? {
        val components =
            DateTimeComponents(
                year = year,
                month = 1,
                dayOfMonth = 1,
                hours = 0,
                minutes = 0
            )
        return components.gmtDateTime()
    }
}

data class Month(val year: Int, val month: Int) : DateComponents() {
    override fun dateTime(): DateTime? {
        val components =
            DateTimeComponents(
                year = year,
                month = month,
                dayOfMonth = 1,
                hours = 0,
                minutes = 0
            )
        return components.gmtDateTime()
    }
}

data class Day(val year: Int, val month: Int, val day: Int) : DateComponents() {
    override fun dateTime(): DateTime? {
        val components =
            DateTimeComponents(
                year = year,
                month = month,
                dayOfMonth = day,
                hours = 0,
                minutes = 0
            )
        return components.gmtDateTime()
    }
}

data class DateComponentsUndefined(val components: DateComponents) : DateComponents() {
    override fun dateTime(): DateTime? = null
}

fun DateComponents.new(dateTime: DateTime): DateComponents =
    when (this) {
        is Year -> dateTime.components().yearDateComponent()
        is Month -> dateTime.components().monthDateComponent()
        is Day -> dateTime.components().dayDateComponent()
        is DateComponentsUndefined -> components.new(dateTime)
    }
