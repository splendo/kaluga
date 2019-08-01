package com.splendo.components.location

interface LocationManager {
    var location: Location?
    var availability: Availability

    fun requestAccess()

    fun addListener(listener: LocationListener)
    fun removeListener(listener: LocationListener)

    fun start()
    fun stop()
}
