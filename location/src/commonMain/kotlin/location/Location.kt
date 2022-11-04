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

package com.splendo.kaluga.location

import kotlin.math.absoluteValue

sealed class Location {

    /**
     * [ms] holds the unixtime (ms since 1970) for when the location was detected. Either a time provided by the location framework,
     * or a time guessed upon receival by the implementation.
     */
    sealed class Time(open val ms: Long) {
        data class MeasuredTime(override val ms: Long) : Time(ms)
        data class GuessedTime(override val ms: Long) : Time(ms)
    }

    /**
     * A known location, [latitude], [longitude] and [time] are always available, the rest are optional
     */
    data class KnownLocation(
        val latitude: Double,
        val longitude: Double,
        val altitude: Double? = null,
        val horizontalAccuracy: Double? = null,
        val verticalAccuracy: Double? = null,
        val speed: Double? = null,
        val course: Double? = null,
        val time: Time
    ) : Location() {

        /**
         * The [DMSCoordinate] associated with the latitude of this location
         */
        val latitudeDMS: DMSCoordinate = DMSCoordinate.fromLatitude(latitude)

        /**
         * The [DMSCoordinate] associated with the latitude of this longitude
         */
        val longitudeDMS: DMSCoordinate = DMSCoordinate.fromLongitude(longitude)
    }

    /**
     * An unknown location.
     * @param reason The [Reason] the location is unknown.
     */
    sealed class UnknownLocation(open val reason: Reason) : Location() {

        enum class Reason {
            PERMISSION_DENIED,
            NO_GPS,
            NOT_CLEAR
        }

        /**
         * The current location is unknown, and there is no last known location
         * @param reason The [Reason] the location is unknown.
         */
        data class WithoutLastLocation(override val reason: Reason) : UnknownLocation(reason)

        /**
         * The current location is unknown, but there is a last known location
         * @param lastKnownLocation The [KnownLocation] last received before the location became unknown.
         * @param reason The [Reason] the location is unknown.
         */
        data class WithLastLocation(val lastKnownLocation: KnownLocation, override val reason: Reason) : UnknownLocation(reason)
    }
}

/**
 * A Location coordinate defined by degrees, minutes and seconds.
 * @param degrees The degrees of the coordinate
 * @param minutes The minutes of the coordinate
 * @param seconds The seconds of the coordinate
 * @param windDirection The [WindDirection] of the coordinate
 */
data class DMSCoordinate(val degrees: Int, val minutes: Int, val seconds: Double, val windDirection: WindDirection) {

    enum class WindDirection {
        North,
        West,
        South,
        East
    }

    companion object {

        /**
         * Converts a latitude coordinate to its [DMSCoordinate]
         */
        fun fromLatitude(latitude: Double): DMSCoordinate {
            val windDirection = if (latitude >= 0.0) WindDirection.North else WindDirection.South
            return fromDecimalDegrees(latitude, windDirection)
        }

        /**
         * Converts a longitude coordinate to its [DMSCoordinate]
         */
        fun fromLongitude(longitude: Double): DMSCoordinate {
            val windDirection = if (longitude >= 0.0) WindDirection.East else WindDirection.West
            return fromDecimalDegrees(longitude, windDirection)
        }

        private fun fromDecimalDegrees(decimalDegrees: Double, windDirection: WindDirection): DMSCoordinate {
            val degrees = decimalDegrees.absoluteValue.toInt()
            val minutesWithRemainder = (decimalDegrees - degrees) * 60
            val minutes = minutesWithRemainder.toInt()
            val seconds = ((minutesWithRemainder - minutes) * 60)
            return DMSCoordinate(degrees, minutes, seconds, windDirection)
        }
    }

    /**
     * Decimal representation of this coordinate
     */
    val decimalDegrees: Double get() {
        val sign = when (windDirection) {
            WindDirection.North, WindDirection.East -> 1.0
            WindDirection.South, WindDirection.West -> -1.0
        }
        return sign * degrees.toDouble() * (minutes.toDouble() / 60.0) * (seconds / 3600.0)
    }

    override fun toString(): String {
        return "$degrees°${minutes}\'${seconds}\" ${windDirection.name}"
    }
}

fun Location.unknownLocationOf(reason: Location.UnknownLocation.Reason): Location {
    return when (this) {
        is Location.KnownLocation -> Location.UnknownLocation.WithLastLocation(this, reason)
        is Location.UnknownLocation.WithLastLocation -> Location.UnknownLocation.WithLastLocation(this.lastKnownLocation, reason)
        is Location.UnknownLocation.WithoutLastLocation -> Location.UnknownLocation.WithoutLastLocation(reason)
    }
}

val Location.known: Location.KnownLocation? get() = when (this) {
    is Location.KnownLocation -> this
    is Location.UnknownLocation.WithLastLocation -> lastKnownLocation
    is Location.UnknownLocation.WithoutLastLocation -> null
}

val Location.KnownLocation?.orUnknown: Location get() = this ?: Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR)
