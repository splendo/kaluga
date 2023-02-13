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

import android.content.Context
import android.content.Intent
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.permissions.location.registerLocationPermissionIfNotRegistered
import com.splendo.kaluga.service.EnableServiceActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext

actual class DefaultLocationManager(
    private val context: Context,
    locationManager: android.location.LocationManager?,
    private val locationProvider: LocationProvider,
    settings: Settings,
    coroutineScope: CoroutineScope
) : BaseLocationManager(settings, coroutineScope) {

    class Builder(
        private val context: Context = ApplicationHolder.applicationContext,
        private val locationManager: android.location.LocationManager? = context.getSystemService(
            Context.LOCATION_SERVICE
        ) as? android.location.LocationManager,
        private val googleLocationProviderSettings: GoogleLocationProvider.Settings = GoogleLocationProvider.Settings()
    ) : BaseLocationManager.Builder {

        override fun create(
            settings: Settings,
            coroutineScope: CoroutineScope
        ): BaseLocationManager {
            val locationProvider: LocationProvider = GoogleLocationProvider(
                context,
                googleLocationProviderSettings,
                settings.minUpdateDistanceMeters
            )
            return DefaultLocationManager(
                context,
                locationManager,
                locationProvider,
                settings,
                coroutineScope
            )
        }
    }

    override val locationMonitor: LocationMonitor = LocationMonitor.Builder(context, locationManager).create()
    private val monitoringMutex = Mutex()
    private var monitoringLocationJob: Job? = null

    override suspend fun requestEnableLocation() {
        EnableServiceActivity.showEnableServiceActivity(context, hashCode().toString(), Intent(ACTION_LOCATION_SOURCE_SETTINGS)).await()
    }

    override suspend fun startMonitoringLocation() {
        monitoringMutex.withLock {
            if (monitoringLocationJob != null) return // optimization to skip making a job

            val job = Job(this.coroutineContext[Job])
            monitoringLocationJob = job
            locationProvider.startMonitoringLocation(locationPermission)
            launch(job) {
                locationProvider.location(locationPermission).collect {
                    handleLocationChanged(it)
                }
            }
        }
    }

    override suspend fun stopMonitoringLocation() {
        monitoringMutex.withLock {
            monitoringLocationJob?.let {
                monitoringLocationJob = it
                locationProvider.stopMonitoringLocation(locationPermission)
                it.cancel()
            }
        }
    }
}

actual class LocationStateRepoBuilder(
    private val context: Context = ApplicationHolder.applicationContext,
    private val permissionsBuilder: suspend (CoroutineContext) -> Permissions = { coroutineContext ->
        Permissions(
            PermissionsBuilder(PermissionContext(context)).apply { registerLocationPermissionIfNotRegistered() },
            coroutineContext
        )
    }
) : BaseLocationStateRepoBuilder {

    override fun create(
        locationPermission: LocationPermission,
        settingsBuilder: (LocationPermission, Permissions) -> BaseLocationManager.Settings,
        coroutineContext: CoroutineContext
    ): LocationStateRepo {
        return LocationStateRepo({ settingsBuilder(locationPermission, permissionsBuilder(it)) }, DefaultLocationManager.Builder(context), coroutineContext)
    }
}
