/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.PermissionsBuilder
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CompletableDeferred

actual class LocationManager(
    context: Context,
    locationPermission: Permission.Location,
    permissions: Permissions,
    autoRequestPermission: Boolean,
    autoEnableLocations: Boolean,
    locationStateRepo: LocationStateRepo
) : BaseLocationManager(locationPermission, permissions, autoRequestPermission, autoEnableLocations, locationStateRepo) {

    class Builder(private val context: Context = ApplicationHolder.applicationContext) : BaseLocationManager.Builder {

        override fun create(
            locationPermission: Permission.Location,
            permissions: Permissions,
            autoRequestPermission: Boolean,
            autoEnableLocations: Boolean,
            locationStateRepo: LocationStateRepo
        ): BaseLocationManager {
            return LocationManager(context, locationPermission, permissions, autoRequestPermission, autoEnableLocations, locationStateRepo)
        }
    }

    companion object {
        val updatingLocationInBackgroundManagers: MutableMap<String, LocationManager> = mutableMapOf()
        val enablingHandlers: MutableMap<String, CompletableDeferred<Boolean>> = mutableMapOf()
    }

    private val identifier = hashCode().toString()

    private val locationMonitor = LocationEnabledMonitor(context, locationPermission) { handleLocationEnabledChanged() }
    private val locationRequest = locationPermission.toRequest()
    private val fusedLocationProviderClient = FusedLocationProviderClient(context)

    private val locationCallback = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            locationResult?.let {
                handleLocationChanged(it.toKnownLocations())
            }
        }
    }

    private val locationUpdatedPendingIntent = LocationUpdatesBroadcastReceiver.intent(context, identifier)

    override suspend fun startMonitoringLocationEnabled() = locationMonitor.startMonitoringLocation()

    override suspend fun stopMonitoringLocationEnabled() = locationMonitor.stopMonitoringLocation()

    override suspend fun isLocationEnabled(): Boolean = locationMonitor.isLocationEnabled()

    override suspend fun requestLocationEnable() = locationMonitor.requestLocationEnable()

    override suspend fun startMonitoringLocation() {
        if (locationPermission.background) {
            updatingLocationInBackgroundManagers[identifier] = this
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationUpdatedPendingIntent)
        } else {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    override suspend fun stopMonitoringLocation() {
        if (locationPermission.background) {
            updatingLocationInBackgroundManagers.remove(identifier)
            fusedLocationProviderClient.removeLocationUpdates(locationUpdatedPendingIntent)
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }
}

class LocationUpdatesBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val ACTION_NAME = "com.splendo.kaluga.location.locationupdates.action"

        fun intent(context: Context, locationManagerId: String): PendingIntent {
            val intent = Intent(context, LocationUpdatesBroadcastReceiver::class.java).apply {
                action = ACTION_NAME
                addCategory(locationManagerId)
            }
            return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) return
        val locationResult = LocationResult.extractResult(intent) ?: return
        intent.categories.forEach { category ->
            LocationManager.updatingLocationInBackgroundManagers[category]?.handleLocationChanged(locationResult.toKnownLocations())
        }
    }
}

actual class LocationStateRepoBuilder(
    private val context: Context = ApplicationHolder.applicationContext,
    private val permissions: Permissions = Permissions(PermissionsBuilder(context), MainQueueDispatcher)
) : LocationStateRepo.Builder {

    override fun create(locationPermission: Permission.Location, autoRequestPermission: Boolean, autoEnableLocations: Boolean, coroutineContext: CoroutineContext): LocationStateRepo {
        return LocationStateRepo(locationPermission, permissions, autoRequestPermission, autoEnableLocations, LocationManager.Builder(context), coroutineContext)
    }
}

fun Permission.Location.toRequest(): LocationRequest = LocationRequest.create().setInterval(1).setMaxWaitTime(1000).setFastestInterval(1).setPriority(
        if (precise) LocationRequest.PRIORITY_HIGH_ACCURACY else LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
)
