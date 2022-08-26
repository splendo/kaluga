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
import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.permissions.location.registerLocationPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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
        private val locationManager: android.location.LocationManager? = context.getSystemService(Context.LOCATION_SERVICE) as? android.location.LocationManager,
        private val locationProvider: LocationProvider = GoogleLocationProvider(context)
    ) : BaseLocationManager.Builder {

        override fun create(
            settings: Settings,
            coroutineScope: CoroutineScope
        ): BaseLocationManager {
            return DefaultLocationManager(context, locationManager, locationProvider, settings, coroutineScope)
        }
    }

    override val locationMonitor: LocationMonitor = LocationMonitor.Builder(context, locationManager).create()
    private val monitoringLocationJob: AtomicReference<Job?> = AtomicReference(null)

    override suspend fun requestEnableLocation() {
        EnableLocationActivity.showEnableLocationActivity(context, hashCode().toString()).await()
    }

    override suspend fun startMonitoringLocation() {
        if (monitoringLocationJob.get() != null) return // optimization to skip making a job

        val job = Job(this.coroutineContext[Job])

        if (monitoringLocationJob.compareAndSet(null, job)) {
            locationProvider.startMonitoringLocation(locationPermission)
            launch(job) {
                locationProvider.location(locationPermission).collect {
                    handleLocationChanged(it)
                }
            }
        }
    }

    override suspend fun stopMonitoringLocation() {
        monitoringLocationJob.get()?.let {
            if (monitoringLocationJob.compareAndSet(it, null)) {
                locationProvider.stopMonitoringLocation(locationPermission)
                it.cancel()
            }
        }
    }
}

actual class LocationStateRepoBuilder(
    private val context: Context = ApplicationHolder.applicationContext,
    private val permissionsBuilder: (CoroutineContext) -> Permissions = { coroutineContext ->
        Permissions(
            PermissionsBuilder(PermissionContext(context)).apply { registerLocationPermission() },
            coroutineContext
        )
    }
) : LocationStateRepo.Builder {

    override fun create(
        locationPermission: LocationPermission,
        settingsBuilder: (LocationPermission, Permissions) -> BaseLocationManager.Settings,
        coroutineContext: CoroutineContext
    ): LocationStateRepo {
        return LocationStateRepo({ settingsBuilder(locationPermission, permissionsBuilder(it)) }, DefaultLocationManager.Builder(context), coroutineContext)
    }
}
