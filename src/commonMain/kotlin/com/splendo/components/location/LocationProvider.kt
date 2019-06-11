package com.splendo.components.location

interface LocationProvider {
    val location: Location?
    fun requestLocation()

    fun addListener(listener: LocationListener)
    fun removeListener(listener: LocationListener)

    fun startListening()
    fun stopListening()
}
