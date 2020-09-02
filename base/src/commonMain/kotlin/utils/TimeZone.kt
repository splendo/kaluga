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
import com.splendo.kaluga.base.utils.Locale.Companion.defaultLocale

/**
 * Style for writing the name of a [TimeZone]
 * Can be either [Short] or [Long]
 */
enum class TimeZoneNameStyle {
    Short,
    Long
}

val TimeZone.Companion.utc: TimeZone by lazy { TimeZone.get("UTC")!! }

expect class TimeZone {
    companion object {
        /**
         * Gets a [TimeZone] based on a given Identifier
         * @param identifier The identifier to create a [TimeZone] for
         * @return The [TimeZone] corresponding to the identifier, if it exists. Check [availableIdentifiers] for supported identifiers
         */
        fun get(identifier: String): TimeZone?

        /**
         * Gets the current [TimeZone] configured by the user
         * @return The current [TimeZone] of the user
         */
        fun current(): TimeZone

        /**
         * List of available identifiers associated with [TimeZone]s. All elements in this list can be used for creating a [TimeZone] using [TimeZone.get]
         */
        val availableIdentifiers: List<String>
    }

    /**
     * The identifier of the [TimeZone]
     */
    val identifier: String

    /**
     * Gets the display name for this [TimeZone] formatted for a given [Locale]
     * @param style The [TimeZoneNameStyle] of the name. Can be either [TimeZoneNameStyle.Short] or [TimeZoneNameStyle.Long]
     * @param withDaylightSavings If `true` the name for the DaylightSavings version of the [TimeZone] will be used. Defaults to [TimeZone.usesDaylightSavingsTime]
     * @param locale The [Locale] used for naming the [TimeZone]. Defaults to [Locale.defaultLocale]
     */
    fun displayName(style: TimeZoneNameStyle, withDaylightSavings: Boolean = usesDaylightSavingsTime(), locale: Locale = defaultLocale): String

    /**
     * The number of milliseconds this timezone differs from GMT when daylight savings time is not active
     */
    val offsetFromGMTInMilliseconds: Long

    /**
     * The number of milliseconds this timezone differs from itself during Daylight Savings
     */
    val daylightSavingsOffsetInMilliseconds: Long

    /**
     * The number of milliseconds this [TimeZone] differs from GMT at a given [Date]
     * @param date The [Date] for which to check the offset. Defaults to [Date.now]
     * @return The number of milliseconds this [TimeZone] differs from GMT at [date]
     */
    fun offsetFromGMTAtDateInMilliseconds(date: Date = now()): Long

    /**
     * Returns `true` if this [TimeZone] is observing daylight savings at a given [Date]
     * @param date The [Date] for which to check whether daylight savings is observed. Defaults to [Date.now]
     * @return `true` if this [TimeZone] if observing daylight savings at [date]
     */
    fun usesDaylightSavingsTime(date: Date = now()): Boolean

    /**
     * Creates a copy of this [TimeZone]
     */
    fun copy(): TimeZone
}
