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

import com.splendo.kaluga.base.utils.DefaultKalugaDate.Companion.now
import com.splendo.kaluga.base.utils.KalugaLocale.Companion.defaultLocale
import kotlin.time.Duration

/**
 * Style for writing the name of a [KalugaTimeZone]
 * Can be either [TimeZoneNameStyle.Short] or [TimeZoneNameStyle.Long]
 */
enum class TimeZoneNameStyle {

    /**
     * A style specifier for [KalugaTimeZone.displayName] indicating a short name, such as "CET"
     */
    Short,

    /**
     * A style specifier for [KalugaTimeZone.displayName] indicating a long name, such as "Central European Time"
     */
    Long,
}

/**
 * Gets the UTC [KalugaTimeZone]
 */
val KalugaTimeZone.Companion.utc: KalugaTimeZone by lazy { KalugaTimeZone.get("UTC")!! }

/**
 * A TimeZone represents a time zone. Accounts for Daylight Savings
 */
abstract class BaseTimeZone {

    /**
     * The identifier of the [KalugaTimeZone]
     */
    abstract val identifier: String

    /**
     * Gets the display name for this [KalugaTimeZone] formatted for a given [KalugaLocale]
     * @param style The [TimeZoneNameStyle] of the name. Can be either [TimeZoneNameStyle.Short] or [TimeZoneNameStyle.Long]
     * @param withDaylightSavings If `true` the name for the DaylightSavings version of the [KalugaTimeZone] will be used. Defaults to [KalugaTimeZone.usesDaylightSavingsTime]
     * @param locale The [KalugaLocale] used for naming the [KalugaTimeZone]. Defaults to [KalugaLocale.defaultLocale]
     */
    abstract fun displayName(style: TimeZoneNameStyle, withDaylightSavings: Boolean = usesDaylightSavingsTime(), locale: KalugaLocale = defaultLocale): String

    /**
     * The [Duration] this timezone differs from GMT when daylight savings time is not active
     */
    abstract val offsetFromGMT: Duration

    /**
     * The number of milliseconds this timezone differs from GMT when daylight savings time is not active
     */
    val offsetFromGMTInMilliseconds: Long get() = offsetFromGMT.inWholeMilliseconds

    /**
     * The [Duration] this timezone differs from itself during Daylight Savings
     */
    abstract val daylightSavingsOffset: Duration

    /**
     * The number of milliseconds this timezone differs from itself during Daylight Savings
     */
    val daylightSavingsOffsetInMilliseconds: Long get() = daylightSavingsOffset.inWholeMilliseconds

    /**
     * The [Duration] this [KalugaTimeZone] differs from GMT at a given [KalugaDate]
     * @param date The [KalugaDate] for which to check the offset. Defaults to [DefaultKalugaDate.now]
     * @return The number of milliseconds this [KalugaTimeZone] differs from GMT at [date]
     */
    abstract fun offsetFromGMTAtDate(date: KalugaDate = now()): Duration

    /**
     * The number of milliseconds this [KalugaTimeZone] differs from GMT at a given [KalugaDate]
     * @param date The [KalugaDate] for which to check the offset. Defaults to [DefaultKalugaDate.now]
     * @return The number of milliseconds this [KalugaTimeZone] differs from GMT at [date]
     */
    fun offsetFromGMTAtDateInMilliseconds(date: KalugaDate = now()): Long = offsetFromGMTAtDate(date).inWholeMilliseconds

    /**
     * Returns `true` if this [KalugaTimeZone] is observing daylight savings at a given [KalugaDate]
     * @param date The [KalugaDate] for which to check whether daylight savings is observed. Defaults to [DefaultKalugaDate.now]
     * @return `true` if this [KalugaTimeZone] if observing daylight savings at [date]
     */
    abstract fun usesDaylightSavingsTime(date: KalugaDate = now()): Boolean

    /**
     * Creates a copy of this [KalugaTimeZone]
     */
    abstract fun copy(): BaseTimeZone

    override fun toString(): String = displayName(TimeZoneNameStyle.Long)
}

/**
 * A default implementation of [BaseTimeZone].
 */
expect class KalugaTimeZone : BaseTimeZone {
    companion object {

        /**
         * Gets a [KalugaTimeZone] based on a given Identifier
         * @param identifier The identifier to create a [KalugaTimeZone] for
         * @return The [KalugaTimeZone] corresponding to the identifier, if it exists. Check [availableIdentifiers] for supported identifiers
         */
        fun get(identifier: String): KalugaTimeZone?

        /**
         * Gets the current [KalugaTimeZone] configured by the user
         * @return The current [KalugaTimeZone] of the user
         */
        fun current(): KalugaTimeZone

        /**
         * List of available identifiers associated with [KalugaTimeZone]s. All elements in this list can be used for creating a [KalugaTimeZone] using [KalugaTimeZone.get]
         */
        val availableIdentifiers: List<String>
    }

    override val identifier: String
    override fun displayName(style: TimeZoneNameStyle, withDaylightSavings: Boolean, locale: KalugaLocale): String
    override val offsetFromGMT: Duration
    override val daylightSavingsOffset: Duration

    override fun offsetFromGMTAtDate(date: KalugaDate): Duration
    override fun usesDaylightSavingsTime(date: KalugaDate): Boolean
    override fun copy(): KalugaTimeZone
}

@Deprecated(
    "Due to name clashes with platform classes and API changes this class has been renamed and changed to an interface. It will be removed in a future release.",
    ReplaceWith("KalugaTimeZone"),
)
typealias TimeZone = KalugaTimeZone
