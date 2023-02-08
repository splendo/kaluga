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

import platform.Foundation.NSTimeZone
import platform.Foundation.NSTimeZoneNameStyle
import platform.Foundation.abbreviationDictionary
import platform.Foundation.daylightSavingTimeOffset
import platform.Foundation.defaultTimeZone
import platform.Foundation.isDaylightSavingTime
import platform.Foundation.knownTimeZoneNames
import platform.Foundation.localizedName
import platform.Foundation.nextDaylightSavingTimeTransition
import platform.Foundation.secondsFromGMT
import platform.Foundation.timeZoneWithAbbreviation
import platform.Foundation.timeZoneWithName
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * A default implementation of [BaseTimeZone].
 * @property timeZone the internal [NSTimeZone] tracking this timezone.
 */
actual class TimeZone internal constructor(val timeZone: NSTimeZone) : BaseTimeZone() {

    actual companion object {

        /**
         * Gets a [TimeZone] based on a given Identifier
         * @param identifier The identifier to create a [TimeZone] for
         * @return The [TimeZone] corresponding to the identifier, if it exists. Check [availableIdentifiers] for supported identifiers
         */
        actual fun get(identifier: String): TimeZone? {
            return if (NSTimeZone.abbreviationDictionary.typedMap<String, String>().containsKey(identifier)) {
                NSTimeZone.timeZoneWithAbbreviation(identifier)
            } else {
                NSTimeZone.timeZoneWithName(identifier)
            }?.let {
                TimeZone(it)
            }
        }

        /**
         * Gets the current [TimeZone] configured by the user
         * @return The current [TimeZone] of the user
         */
        actual fun current(): TimeZone = TimeZone(NSTimeZone.defaultTimeZone)

        /**
         * List of available identifiers associated with [TimeZone]s. All elements in this list can be used for creating a [TimeZone] using [TimeZone.get]
         */
        actual val availableIdentifiers: List<String> = NSTimeZone.knownTimeZoneNames.typedList()
    }

    override val identifier: String = timeZone.name
    override fun displayName(style: TimeZoneNameStyle, withDaylightSavings: Boolean, locale: Locale): String {
        val nameStyle = when (style) {
            TimeZoneNameStyle.Short -> if (withDaylightSavings) NSTimeZoneNameStyle.NSTimeZoneNameStyleShortDaylightSaving else NSTimeZoneNameStyle.NSTimeZoneNameStyleShortStandard
            TimeZoneNameStyle.Long -> if (withDaylightSavings) NSTimeZoneNameStyle.NSTimeZoneNameStyleDaylightSaving else NSTimeZoneNameStyle.NSTimeZoneNameStyleStandard
        }
        return timeZone.localizedName(nameStyle, locale.nsLocale) ?: ""
    }
    override val offsetFromGMT: Duration get() {
        val rawOffset = if (timeZone.isDaylightSavingTime())
            timeZone.secondsFromGMT.toDouble() - timeZone.daylightSavingTimeOffset
        else
            timeZone.secondsFromGMT.toDouble()

        return rawOffset.seconds
    }

    override val daylightSavingsOffset: Duration get() {
        val rawOffset = if (timeZone.isDaylightSavingTime()) {
            timeZone.daylightSavingTimeOffset.toLong()
        } else {
            timeZone.nextDaylightSavingTimeTransition?.let {
                timeZone.daylightSavingTimeOffsetForDate(it).toLong()
            } ?: 0L
        }
        return rawOffset.seconds
    }
    override fun offsetFromGMTAtDate(date: KalugaDate): Duration = timeZone.secondsFromGMTForDate(date.date).seconds
    override fun usesDaylightSavingsTime(date: KalugaDate): Boolean = timeZone.isDaylightSavingTimeForDate(date.date)
    override fun copy(): TimeZone = TimeZone(timeZone.copy() as NSTimeZone)
    override fun equals(other: Any?): Boolean {
        return (other as? TimeZone)?.let { timeZone == other.timeZone } ?: false
    }
}
