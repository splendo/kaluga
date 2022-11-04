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

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.splendo.kaluga.permissions.location.LocationPermission
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class GoogleLocationProvider(private val context: Context) : LocationProvider {

    sealed class FusedLocationProviderClientType(
        permission: LocationPermission,
        protected val context: Context
    ) {
        protected val fusedLocationProviderClient = FusedLocationProviderClient(context)
        protected val locationRequest = LocationRequest.create().setInterval(1).setMaxWaitTime(1000).setFastestInterval(1).setPriority(
            if (permission.precise) Priority.PRIORITY_HIGH_ACCURACY else Priority.PRIORITY_BALANCED_POWER_ACCURACY
        )
        protected val locationsState: MutableStateFlow<List<Location.KnownLocation>> = MutableStateFlow(emptyList())
        val locations: Flow<List<Location.KnownLocation>> = locationsState

        abstract fun startRequestingUpdates()
        abstract fun stopRequestingUpdates()

        class Foreground(permission: LocationPermission, context: Context) : FusedLocationProviderClientType(permission, context) {

            private val locationCallback = object : LocationCallback() {

                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)

                    locationsState.value = locationResult.toKnownLocations()
                }
            }

            @SuppressLint("MissingPermission")
            override fun startRequestingUpdates() {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            }

            override fun stopRequestingUpdates() {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }

        class Background(permission: LocationPermission, context: Context) : FusedLocationProviderClientType(permission, context) {

            private val identifier = hashCode().toString()

            companion object {
                val updatingLocationInBackgroundManagers: MutableMap<String, MutableStateFlow<List<Location.KnownLocation>>> = mutableMapOf()
            }

            private val locationUpdatedPendingIntent = GoogleLocationUpdatesBroadcastReceiver.intent(context, identifier)

            @SuppressLint("MissingPermission")
            override fun startRequestingUpdates() {
                updatingLocationInBackgroundManagers[identifier] = locationsState
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationUpdatedPendingIntent)
            }

            override fun stopRequestingUpdates() {
                updatingLocationInBackgroundManagers.remove(identifier)
                fusedLocationProviderClient.removeLocationUpdates(locationUpdatedPendingIntent)
            }
        }
    }

    private val fusedLocationProviderClients: MutableStateFlow<Map<LocationPermission, FusedLocationProviderClientType>> = MutableStateFlow(
        mapOf()
    )

    override fun location(permission: LocationPermission): Flow<List<Location.KnownLocation>> = fusedLocationProviderClients.flatMapLatest {
        it[permission]?.locations ?: flowOf(emptyList())
    }

    override fun startMonitoringLocation(permission: LocationPermission) {
        val fusedLocationProviderClient = getLocationProviderForPermission(permission)
        fusedLocationProviderClient.startRequestingUpdates()
    }

    override fun stopMonitoringLocation(permissions: LocationPermission) {
        fusedLocationProviderClients.value[permissions]?.startRequestingUpdates()
        fusedLocationProviderClients.value = fusedLocationProviderClients.value.toMutableMap().apply { remove(permissions) }
    }

    private fun getLocationProviderForPermission(permission: LocationPermission): FusedLocationProviderClientType {
        return fusedLocationProviderClients.value[permission] ?: run {
            val fusedLocationProviderClient = if (permission.background) {
                FusedLocationProviderClientType.Background(permission, context)
            } else {
                FusedLocationProviderClientType.Foreground(permission, context)
            }

            fusedLocationProviderClients.value = fusedLocationProviderClients.value.toMutableMap().apply {
                put(permission, fusedLocationProviderClient)
            }
            fusedLocationProviderClient
        }
    }
}

class GoogleLocationUpdatesBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val ACTION_NAME = "com.splendo.kaluga.location.locationupdates.action"

        fun intent(context: Context, locationManagerId: String): PendingIntent {
            val intent = Intent(context, GoogleLocationUpdatesBroadcastReceiver::class.java).apply {
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
            GoogleLocationProvider.FusedLocationProviderClientType.Background.updatingLocationInBackgroundManagers[category]?.value = locationResult.toKnownLocations()
        }
    }
}
