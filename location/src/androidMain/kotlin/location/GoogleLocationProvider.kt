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
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.splendo.kaluga.base.collections.concurrentMutableMapOf
import com.splendo.kaluga.permissions.location.LocationPermission
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.milliseconds

/**
 * A [LocationProvider] using the Google Location Services
 * @param context The [Context] to run this [LocationProvider] in.
 * @param settings The [GoogleLocationProvider.Settings] to use to configure this location provider.
 * @param minUpdateDistanceMeters The minimum distance in meters traversed for a location update to be reported.
 */
class GoogleLocationProvider(private val context: Context, private val settings: Settings, private val minUpdateDistanceMeters: Float) : LocationProvider {

    /**
     * Settings for a [GoogleLocationProvider]
     * @param interval The desired interval of location updates. (Best effort)
     * @param maxUpdateDelay The longest a location update may be delayed.
     * @param minUpdateInterval The fastest allowed interval of location updates.
     */
    data class Settings(val interval: Duration = 100.milliseconds, val maxUpdateDelay: Duration = ZERO, val minUpdateInterval: Duration = interval)

    internal sealed class FusedLocationProviderClientType(permission: LocationPermission, context: Context, settings: Settings, minUpdateDistanceMeters: Float) {
        protected val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        protected val locationRequest =
            LocationRequest.Builder(settings.interval.inWholeMilliseconds)
                .setMaxUpdateDelayMillis(settings.maxUpdateDelay.inWholeMilliseconds)
                .setMinUpdateIntervalMillis(settings.minUpdateInterval.inWholeMilliseconds)
                .setMinUpdateDistanceMeters(minUpdateDistanceMeters)
                .setPriority(
                    if (permission.precise) {
                        Priority.PRIORITY_HIGH_ACCURACY
                    } else {
                        Priority.PRIORITY_BALANCED_POWER_ACCURACY
                    },
                )
                .build()
        protected val locationsState: MutableStateFlow<List<Location.KnownLocation>> =
            MutableStateFlow(emptyList())
        val locations: Flow<List<Location.KnownLocation>> = locationsState

        abstract fun startRequestingUpdates()
        abstract fun stopRequestingUpdates()

        class Foreground(permission: LocationPermission, context: Context, settings: Settings, minUpdateDistanceMeters: Float) :
            FusedLocationProviderClientType(permission, context, settings, minUpdateDistanceMeters) {

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

        class Background(permission: LocationPermission, context: Context, settings: Settings, minUpdateDistanceMeters: Float) :
            FusedLocationProviderClientType(permission, context, settings, minUpdateDistanceMeters) {

            private val identifier = hashCode().toString()

            companion object {
                val updatingLocationInBackgroundManagers: MutableMap<String, MutableStateFlow<List<Location.KnownLocation>>> = concurrentMutableMapOf()
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
        mapOf(),
    )

    override fun location(permission: LocationPermission): Flow<List<Location.KnownLocation>> = fusedLocationProviderClients.flatMapLatest {
        it[permission]?.locations ?: flowOf(emptyList())
    }

    override fun startMonitoringLocation(permission: LocationPermission) {
        val fusedLocationProviderClient = getLocationProviderForPermission(permission)
        fusedLocationProviderClient.startRequestingUpdates()
    }

    override fun stopMonitoringLocation(permission: LocationPermission) {
        fusedLocationProviderClients.value[permission]?.stopRequestingUpdates()
        fusedLocationProviderClients.value = fusedLocationProviderClients.value.toMutableMap().apply { remove(permission) }
    }

    private fun getLocationProviderForPermission(permission: LocationPermission): FusedLocationProviderClientType = fusedLocationProviderClients.value[permission] ?: run {
        val fusedLocationProviderClient = if (permission.background) {
            FusedLocationProviderClientType.Background(permission, context, settings, minUpdateDistanceMeters)
        } else {
            FusedLocationProviderClientType.Foreground(permission, context, settings, minUpdateDistanceMeters)
        }

        fusedLocationProviderClients.value = fusedLocationProviderClients.value.toMutableMap().apply {
            put(permission, fusedLocationProviderClient)
        }
        fusedLocationProviderClient
    }
}

/**
 * A [BroadcastReceiver] for receiving updates to a Google Location from the background
 */
class GoogleLocationUpdatesBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val ACTION_NAME = "com.splendo.kaluga.location.locationupdates.action"

        internal fun intent(context: Context, locationManagerId: String): PendingIntent {
            val intent = Intent(context, GoogleLocationUpdatesBroadcastReceiver::class.java).apply {
                action = ACTION_NAME
                addCategory(locationManagerId)
            }
            return PendingIntent.getBroadcast(
                context,
                0,
                intent,
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                },
            )
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
