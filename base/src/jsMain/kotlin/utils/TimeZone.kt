/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

// TODO Implement with proper timezone solution for Java Script
/**
 * A default implementation of [BaseTimeZone].
 */
actual class TimeZone internal constructor() : BaseTimeZone() {

    actual companion object {

        /**
         * Gets a [TimeZone] based on a given Identifier
         * @param identifier The identifier to create a [TimeZone] for
         * @return The [TimeZone] corresponding to the identifier, if it exists. Check [availableIdentifiers] for supported identifiers
         */
        actual fun get(identifier: String): TimeZone? = TimeZone()

        /**
         * Gets the current [TimeZone] configured by the user
         * @return The current [TimeZone] of the user
         */
        actual fun current(): TimeZone = TimeZone()

        /**
         * List of available identifiers associated with [TimeZone]s. All elements in this list can be used for creating a [TimeZone] using [TimeZone.get]
         */
        actual val availableIdentifiers: List<String> = emptyList()
    }

    override val identifier: String = ""
    override fun displayName(style: TimeZoneNameStyle, withDaylightSavings: Boolean, locale: Locale): String = ""
    override val offsetFromGMTInMilliseconds = 0L
    override val daylightSavingsOffsetInMilliseconds: Long = 0L
    override fun offsetFromGMTAtDateInMilliseconds(date: KalugaDate): Long = 0L
    override fun usesDaylightSavingsTime(date: KalugaDate): Boolean = false
    override fun copy(): TimeZone = TimeZone()
}
