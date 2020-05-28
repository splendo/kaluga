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
            minute = components.minutes
        )

        val timestamp =
            timestampFromKlockLocalDateTime(
                dt
            )
        return DateTime(timestamp)
    }

    override fun componentsFromLocalDateTime(dateTime: DateTime): DateTimeComponents =
        klockDateTimeComponentsFromDateTimeTz(
            klockDateTimeFromTimestamp(
                dateTime.timestamp
            ).local
        )

    override fun dateTimeFromGMTComponents(components: DateTimeComponents): DateTime {
        val dt = com.soywiz.klock.DateTime(
            year = components.year,
            month = components.month,
            day = components.dayOfMonth,
            hour = components.hours,
            minute = components.minutes
        ).utc

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
