package com.splendo.components.location

interface LocationManager {
    val location: Location?
    var availability: Availability

    fun requestLocation()
    fun requestAccess()

    fun addListener(listener: LocationListener)
    fun removeListener(listener: LocationListener)

    fun startListening()
    fun stopListening()
}
