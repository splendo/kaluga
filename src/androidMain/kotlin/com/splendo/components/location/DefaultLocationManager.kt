package com.splendo.components.location

import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager.GPS_PROVIDER
import android.location.LocationProvider

actual class DefaultLocationManager: LocationManager {

    actual constructor(configuration: Configuration) {
        val context = configuration.context
        val service = context.getSystemService(LOCATION_SERVICE) as android.location.LocationManager
        val provider = service.getProvider(GPS_PROVIDER) as LocationProvider
    }

    override var availability = Availability.NOT_DETERMINED

    override var location: Location? = null

    override fun requestAccess() {
//        println("process: ${}")
//        TODO("not implemented")
    }

    override fun addListener(listener: LocationListener) {
//        TODO("not implemented")
    }

    override fun removeListener(listener: LocationListener) {
//        TODO("not implemented")
    }

    override fun start() {
//        TODO("not implemented")
    }

    override fun stop() {
//        TODO("not implemented")
    }
}
