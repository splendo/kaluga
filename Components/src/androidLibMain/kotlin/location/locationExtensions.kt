package com.splendo.mpp.location

import android.os.Looper
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun FusedLocationProviderClient.onLocation(
    coroutineScope: CoroutineScope,
    locationRequest: LocationRequest,
    available:suspend(LocationAvailability)->Unit,
    location:suspend(LocationResult)->Unit
): Pair<Task<Void>, LocationCallback> {
    val locationCallback = object: LocationCallback() {
        override fun onLocationAvailability(availability: LocationAvailability?) {
            println("Availability: $availability")
            availability?.let {
                coroutineScope.launch {
                    println("Availability inside scope: $availability")
                    available(it) } }
        }

        override fun onLocationResult(result: LocationResult?) {
            println("Result: $result")
            result?.let { coroutineScope.launch { location(it)}}
        }
    }

    return Pair(requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper()), locationCallback)
}

fun android.location.Location.toKnownLocation(): Location.KnownLocation {
    return Location.KnownLocation(
        latitude = latitude,
        longitude = longitude,
        altitude = altitude,
        horizontalAccuracy = accuracy.toDouble(),
        verticalAccuracy = verticalAccuracyMeters.toDouble(),
        speed = speed.toDouble(),
        time = Location.Time.MeasuredTime(time)
    )
}

fun LocationResult.toKnownLocations(): List<Location.KnownLocation> {
    return locations.mapNotNull {
        it.toKnownLocation()
    }
}
