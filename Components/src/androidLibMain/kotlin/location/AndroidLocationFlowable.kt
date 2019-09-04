package com.splendo.mpp.location

import com.google.android.gms.location.*
import com.splendo.mpp.location.LocationFlowableState.NoLocationClient
import com.splendo.mpp.state.State
import com.splendo.mpp.state.StateRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class LocationManagerStateRepo(val locationFlowable:LocationFlowable
): StateRepo<LocationFlowableState>() {

    override fun initialState(): LocationFlowableState {
        return NoLocationClient(this)
    }
}

sealed class LocationFlowableState(override val repo: LocationManagerStateRepo) : State<LocationFlowableState>(repo) {

    class NoLocationClient(override val repo: LocationManagerStateRepo): LocationFlowableState(repo) {
        operator fun plus(locationProvider: FusedLocationProviderClient): HasFusedLocationProvider {
            return HasFusedLocationProvider(repo, locationProvider)
        }
    }

    data class HasFusedLocationProvider(
        override val repo: LocationManagerStateRepo,
        private val fusedLocationProviderClient: FusedLocationProviderClient
        ):
        LocationFlowableState(repo), CoroutineScope by CoroutineScope(repo.coroutineContext + Dispatchers.Default + CoroutineName("Android Location Updates"))
    {
        val locationCallback:LocationCallback

        init {
            val (task, locationCallback) = fusedLocationProviderClient.onLocation(
                coroutineScope = this,
                locationRequest = LocationRequest.create().setInterval(1).setMaxWaitTime(1000).setFastestInterval(1).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY),
                available = {
                    println("available in listener: $it")
                    if (!it.isLocationAvailable) {
                        println("set location unknown..")
                        repo.locationFlowable.setUnknownLocation()
                    }
                },
                location = { result ->
                    result.toKnownLocations().forEach {
                        println("known locations: $it")
                        repo.locationFlowable.set(it)
                    }

                })
            this.locationCallback = locationCallback

            launch {
                fusedLocationProviderClient.lastLocation.await()?.let {
                    repo.locationFlowable.set(it.toKnownLocation())
                    println("last location sent: $it")
                }
                task.await()
            }
        }

        fun removeFusedLocationProvider():NoLocationClient {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            return NoLocationClient(repo)
        }
    }
}

actual class LocationFlowable:
    BaseLocationFlowable() {

    private var stateRepo = LocationManagerStateRepo(this)

    fun setFusedLocationProviderClient(provider: FusedLocationProviderClient) {
        stateRepo.changeStateBlocking {
            when (it) {
                is NoLocationClient -> it + provider
                is LocationFlowableState.HasFusedLocationProvider -> it.removeFusedLocationProvider() + provider
            }
        }
    }
}