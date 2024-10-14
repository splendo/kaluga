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

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

// TODO Implement with proper timezone solution for Java Script
/**
 * A default implementation of [BaseTimeZone].
 */
actual class KalugaTimeZone internal constructor() : BaseTimeZone() {

    actual companion object {

        /**
         * Gets a [KalugaTimeZone] based on a given Identifier
         * @param identifier The identifier to create a [KalugaTimeZone] for
         * @return The [KalugaTimeZone] corresponding to the identifier, if it exists. Check [availableIdentifiers] for supported identifiers
         */
        actual fun get(identifier: String): KalugaTimeZone? = KalugaTimeZone()

        /**
         * Gets the current [KalugaTimeZone] configured by the user
         * @return The current [KalugaTimeZone] of the user
         */
        actual fun current(): KalugaTimeZone = KalugaTimeZone()

        /**
         * List of available identifiers associated with [KalugaTimeZone]s. All elements in this list can be used for creating a [KalugaTimeZone] using [KalugaTimeZone.get]
         */
        actual val availableIdentifiers: List<String> = emptyList()
    }

    actual override val identifier: String = ""
    actual override fun displayName(style: TimeZoneNameStyle, withDaylightSavings: Boolean, locale: KalugaLocale): String = ""
    actual override val offsetFromGMT = 0.milliseconds
    actual override val daylightSavingsOffset = 0.milliseconds
    actual override fun offsetFromGMTAtDate(date: KalugaDate): Duration = 0.milliseconds
    actual override fun usesDaylightSavingsTime(date: KalugaDate): Boolean = false
    actual override fun copy(): KalugaTimeZone = KalugaTimeZone()
    override fun equals(other: Any?): Boolean = other is KalugaTimeZone && identifier == other.identifier
}
