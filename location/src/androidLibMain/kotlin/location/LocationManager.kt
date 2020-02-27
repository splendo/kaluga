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
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.log.debug
import com.splendo.kaluga.permissions.location.LocationPermissionStateRepo
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

actual class LocationManager(private val context: Context,
                             private val locationRequest: LocationRequest,
                             private val inBackground: Boolean,
                             locationPermissionRepo: LocationPermissionStateRepo,
                             autoRequestPermission: Boolean,
                             autoEnableLocations: Boolean,
                             locationStateRepo: LocationStateRepo
) : BaseLocationManager(locationPermissionRepo, autoRequestPermission, autoEnableLocations, locationStateRepo) {

    class Builder(private val context: Context = ApplicationHolder.applicationContext,
                  private val locationRequest: LocationRequest = LocationRequest.create().setInterval(1).setMaxWaitTime(1000).setFastestInterval(1).setPriority(
                      LocationRequest.PRIORITY_HIGH_ACCURACY
                  ),
                  private val inBackground: Boolean) : BaseLocationManager.Builder {

        override fun create(
            locationPermissionRepo: LocationPermissionStateRepo,
            autoRequestPermission: Boolean,
            autoEnableLocations: Boolean,
            locationStateRepo: LocationStateRepo
        ): BaseLocationManager {
            return LocationManager(context, locationRequest, inBackground, locationPermissionRepo, autoRequestPermission, autoEnableLocations, locationStateRepo)
        }
    }

    companion object {
        val updatingLocationInBackgroundManagers: MutableMap<String, LocationManager> = mutableMapOf()
        val updatingLocationEnabledInBackgroundManagers: MutableMap<String, LocationManager> = mutableMapOf()
        val enablingHandlers: MutableMap<String, CompletableDeferred<Boolean>> = mutableMapOf()
    }

    private val identifier = hashCode().toString()

    private val fusedLocationProviderClient = FusedLocationProviderClient(context)
    private val locationEnabledCallback = object : LocationCallback() {

        override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
            super.onLocationAvailability(locationAvailability)

            handleLocationEnabledChanged()
        }

    }

    private val locationCallback = object : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            locationResult?.let {
                handleLocationChanged(it.toKnownLocations())
            }
        }

    }

    private val locationEnabledUpdatedPendingIntent = LocationEnabledUpdatesBroadcastReceiver.intent(context, identifier)
    private val locationUpdatedPendingIntent = LocationUpdatesBroadcastReceiver.intent(context, identifier)

    override suspend fun startMonitoringLocationEnabled() {
        if (inBackground) {
            updatingLocationEnabledInBackgroundManagers[identifier] = this
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationEnabledUpdatedPendingIntent)
        } else {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationEnabledCallback, Looper.getMainLooper())
        }
    }

    override suspend fun stopMonitoringLocationEnabled() {
        if (inBackground) {
            updatingLocationEnabledInBackgroundManagers.remove(identifier)
            fusedLocationProviderClient.removeLocationUpdates(locationEnabledUpdatedPendingIntent)
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationEnabledCallback)
        }
    }

    override suspend fun isLocationEnabled(): Boolean {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).setNeedBle(true).setAlwaysShow(true)
        return try {
            LocationServices.getSettingsClient(context).checkLocationSettings(builder.build()).await() != null
        } catch (e: ApiException) {
            false
        }
    }

    override suspend fun requestLocationEnable(){
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).setNeedBle(true).setAlwaysShow(true)
        try {
            LocationServices.getSettingsClient(context).checkLocationSettings(builder.build()).await() != null
        } catch (e: ApiException) {
            when(e) {
                is ResolvableApiException -> {
                    val enablingHandler = CompletableDeferred<Boolean>()
                    enablingHandlers[identifier] = enablingHandler

                    MainScope().launch(MainQueueDispatcher) {
                        val intent = EnableLocationActivity.intent(context, identifier, e)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                        enablingHandler.await()
                        enablingHandlers.remove(identifier)
                        handleLocationEnabledChanged()
                    }
                }
            }
        }
    }

    override suspend fun startMonitoringLocation() {
        if (inBackground) {
            updatingLocationInBackgroundManagers[identifier] = this
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationUpdatedPendingIntent)
        } else {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    override suspend fun stopMonitoringLocation() {
        if (inBackground) {
            updatingLocationEnabledInBackgroundManagers.remove(identifier)
            fusedLocationProviderClient.removeLocationUpdates(locationEnabledUpdatedPendingIntent)
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
        intent.categories.forEach {category ->
            LocationManager.updatingLocationInBackgroundManagers[category]?.handleLocationChanged(locationResult.toKnownLocations())
        }
    }
}

class LocationEnabledUpdatesBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val ACTION_NAME = "com.splendo.kaluga.location.locationenabledupdates.action"

        fun intent(context: Context, locationManagerId: String): PendingIntent {
            val intent = Intent(context, LocationEnabledUpdatesBroadcastReceiver::class.java).apply {
                action = ACTION_NAME
                addCategory(locationManagerId)
            }
            return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) return
        val locationAvailability = LocationAvailability.extractLocationAvailability(intent) ?: return
        intent.categories.forEach {category ->
            LocationManager.updatingLocationEnabledInBackgroundManagers[category]?.handleLocationEnabledChanged()
        }
    }
}