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

package com.splendo.kaluga.base.utils

import com.splendo.kaluga.base.utils.Date.Companion.now

enum class TimeZoneNameStyle {
    Short,
    Long
}

expect class TimeZone {
    companion object {
        fun get(identifier: String): TimeZone?
        fun current(): TimeZone
        val availableIdentifiers: List<String>
    }

    val identifier: String
    fun displayName(style: TimeZoneNameStyle, withDaylightSavings: Boolean, locale: Locale = defaultLocale): String
    val offsetFromGMTInMilliseconds: Long
    val daylightSavingsOffsetfromGMT: Long
    fun offsetFromGMTAtDateInMilliseconds(date: Date = now()): Long
    fun usesDaylightSavingsTime(date: Date = now()): Boolean
    fun copy(): TimeZone
}
