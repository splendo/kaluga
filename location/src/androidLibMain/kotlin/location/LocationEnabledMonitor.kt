/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.location.LocationEnabledMonitor.Companion.updatingLocationEnabledInBackgroundManagers
import com.splendo.kaluga.permissions.Permission
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LocationEnabledMonitor(
    private val context: Context,
    private val permission: Permission.Location,
    internal val handleLocationEnabledChanged: () -> Unit
) {

    companion object {
        val updatingLocationEnabledInBackgroundManagers: MutableMap<String, LocationEnabledMonitor> = mutableMapOf()
    }

    private val identifier = hashCode().toString()

    private val locationRequest = permission.toRequest()
    private val fusedLocationProviderClient = FusedLocationProviderClient(context)
    private val locationEnabledUpdatedPendingIntent = LocationEnabledUpdatesBroadcastReceiver.intent(context, identifier)
    private val locationEnabledCallback = object : LocationCallback() {

        override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
            super.onLocationAvailability(locationAvailability)

            handleLocationEnabledChanged()
        }
    }

    suspend fun isLocationEnabled(): Boolean {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).setNeedBle(true).setAlwaysShow(true)
        return try {
            LocationServices.getSettingsClient(context).checkLocationSettings(builder.build()).await() != null
        } catch (e: ApiException) {
            false
        }
    }

    suspend fun requestLocationEnable() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).setNeedBle(true).setAlwaysShow(true)
        try {
            LocationServices.getSettingsClient(context).checkLocationSettings(builder.build()).await() != null
        } catch (e: ApiException) {
            when (e) {
                is ResolvableApiException -> {
                    val enablingHandler = CompletableDeferred<Boolean>()
                    LocationManager.enablingHandlers[identifier] = enablingHandler

                    MainScope().launch(MainQueueDispatcher) {
                        val intent = EnableLocationActivity.intent(context, identifier, e)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                        enablingHandler.await()
                        LocationManager.enablingHandlers.remove(identifier)
                        handleLocationEnabledChanged()
                    }
                }
            }
        }
    }

    fun startMonitoringLocation() {
        if (permission.background) {
            updatingLocationEnabledInBackgroundManagers[identifier] = this
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationEnabledUpdatedPendingIntent)
        } else {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationEnabledCallback, Looper.getMainLooper())
        }
    }

    fun stopMonitoringLocation() {
        if (permission.background) {
            updatingLocationEnabledInBackgroundManagers.remove(identifier)
            fusedLocationProviderClient.removeLocationUpdates(locationEnabledUpdatedPendingIntent)
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationEnabledCallback)
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
        LocationAvailability.extractLocationAvailability(intent) ?: return
        intent.categories.forEach { category ->
            updatingLocationEnabledInBackgroundManagers[category]?.handleLocationEnabledChanged?.invoke()
        }
    }
}
