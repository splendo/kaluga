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

import com.splendo.kaluga.base.utils.KalugaDate
import kotlin.math.absoluteValue

/**
 * A Geolocation of the system
 */
sealed class Location {

    /**
     * A [Location] where the current coordinates are known
     * @property latitude the latitude of the location
     * @property longitude the longitude of the location
     * @property altitude the altitude above mean sea level associated with a location, measured in meters.
     * @property horizontalAccuracy the estimated horizontal accuracy radius in meters of this location
     * @property verticalAccuracy the estimated altitude accuracy in meters of this location
     * @property speed the velocity (measured in meters per second) at which the device is moving
     * @property course the azimuth that is measured in degrees relative to true north
     * @property time the [KalugaDate] at which the location was detected
     */
    data class KnownLocation(
        val latitude: Double,
        val longitude: Double,
        val altitude: Double? = null,
        val horizontalAccuracy: Double? = null,
        val verticalAccuracy: Double? = null,
        val speed: Double? = null,
        val course: Double? = null,
        val time: KalugaDate,
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
     * An [location] where the current coordinates are unknown.
     * @property reason The [Reason] the location is unknown.
     */
    sealed class UnknownLocation(open val reason: Reason) : Location() {

        enum class Reason {
            /**
             * Location is unknown because location permissions have not been granted
             */
            PERMISSION_DENIED,

            /**
             * Location is unknown because GPS is disabled
             */
            NO_GPS,

            /**
             * Location is unknown for an unknown reason
             */
            NOT_CLEAR,
        }

        /**
         * An [UnknownLocation] where there is no last known location
         * @param reason The [Reason] the location is unknown.
         */
        data class WithoutLastLocation(override val reason: Reason) : UnknownLocation(reason)

        /**
         * A [UnknownLocation], but there is a last known location
         * @property lastKnownLocation The [KnownLocation] last received before the location became unknown.
         * @param reason The [Reason] the location is unknown.
         */
        data class WithLastLocation(val lastKnownLocation: KnownLocation, override val reason: Reason) : UnknownLocation(reason)
    }
}

/**
 * A Location coordinate defined by degrees, minutes and seconds.
 * @property degrees The degrees of the coordinate
 * @property minutes The minutes of the coordinate
 * @property seconds The seconds of the coordinate
 * @property windDirection The [WindDirection] of the coordinate
 */
data class DMSCoordinate(val degrees: Int, val minutes: Int, val seconds: Double, val windDirection: WindDirection) {

    enum class WindDirection {
        North,
        West,
        South,
        East,
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
     * Double representation of this coordinate
     */
    val decimalDegrees: Double get() {
        val sign = when (windDirection) {
            WindDirection.North, WindDirection.East -> 1.0
            WindDirection.South, WindDirection.West -> -1.0
        }
        return sign * degrees.toDouble() * (minutes.toDouble() / 60.0) * (seconds / 3600.0)
    }

    override fun toString(): String = "$degrees°${minutes}\'${seconds}\" ${windDirection.name}"
}

/**
 * Converts a [Location] into a [Location.UnknownLocation] given a [Location.UnknownLocation.Reason]
 * @param reason the [Location.UnknownLocation.Reason] the [Location] became unknown
 * @return the [Location.UnknownLocation] with [reason].
 * If the [Location] this method had a [Location.KnownLocation], this will return [Location.UnknownLocation.WithLastLocation], otherwise [Location.UnknownLocation.WithoutLastLocation]
 */
fun Location.unknownLocationOf(reason: Location.UnknownLocation.Reason): Location.UnknownLocation = when (this) {
    is Location.KnownLocation -> Location.UnknownLocation.WithLastLocation(this, reason)
    is Location.UnknownLocation.WithLastLocation -> Location.UnknownLocation.WithLastLocation(this.lastKnownLocation, reason)
    is Location.UnknownLocation.WithoutLastLocation -> Location.UnknownLocation.WithoutLastLocation(reason)
}

/**
 * Gets the [Location.KnownLocation] from a [Location] if it exists. Otherwise `null` is returned
 */
val Location.known: Location.KnownLocation? get() = when (this) {
    is Location.KnownLocation -> this
    is Location.UnknownLocation.WithLastLocation -> lastKnownLocation
    is Location.UnknownLocation.WithoutLastLocation -> null
}

/**
 * Converts a Nullable [Location.KnownLocation] into a [Location], where [Location.UnknownLocation.WithoutLastLocation] is returned if the location was `null`.
 */
val Location.KnownLocation?.orUnknown: Location get() = this ?: Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR)
