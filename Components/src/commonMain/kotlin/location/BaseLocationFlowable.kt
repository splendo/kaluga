package com.splendo.mpp.location

import com.splendo.mpp.flow.BaseFlowable
import com.splendo.mpp.location.Location.UnknownReason

open class BaseLocationFlowable: BaseFlowable<Location>() {

    private var lastLocation: Location.KnownLocation? = null

    suspend fun set(location: Location.KnownLocation) {
        lastLocation = location
        super.set(location)
        println("location sent")
    }

    suspend fun setUnknownLocation(reason: UnknownReason = UnknownReason.NOT_CLEAR) {
        val location = lastLocation?.let {
            Location.UnknownLocationWithLastLocation(it, reason)
        } ?: Location.UnknownLocationWithNoLastLocation(reason)
        println("Send to channel: $location")
        set(location)
        println("unknown location sent")
    }

}