package com.splendo.mpp.location

sealed class Location {

    enum class UnknownReason {
        NO_PERMISSION_GRANTED,
        PERMISSION_DENIED,
        NO_GPS,
        NOT_CLEAR
    }

    /**
     * [ms] holds the unixtime (ms since 1970) for when the location was detected. Either a time provided by the location framework,
     * or a time guessed upon receival by the implementation.
     */
    sealed class Time(open val ms:Long) {
        data class MeasuredTime(override val ms:Long):Time(ms)
        data class GuessedTime(override val ms:Long):Time(ms)
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
    ):Location()

    open class UnknownLocation protected constructor(open val reason:UnknownReason):Location()

    /**
     * The current location is unknown, and there is no last known location
     */
    data class UnknownLocationWithNoLastLocation(override val reason:UnknownReason):UnknownLocation(reason)

    /**
     * The current location is unknown, but there is a last known location
     */
    data class UnknownLocationWithLastLocation(val lastKnownLocation:KnownLocation, override val reason:UnknownReason):UnknownLocation(reason)
}