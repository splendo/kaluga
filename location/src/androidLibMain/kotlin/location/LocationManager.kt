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
import com.splendo.kaluga.location.BaseLocationManager.Settings
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

/**
 * A default implementation of [BaseLocationManager]
 * @param context the [Context] in which to manage the location
 * @param locationManager the [android.location.LocationManager] to manage the location
 * @param locationProvider the [LocationProvider] to provide the location
 * @param settings the [Settings] to configure this location manager
 * @param coroutineScope the [CoroutineScope] this location manager runs on
 */
actual class DefaultLocationManager(
    private val context: Context,
    locationManager: android.location.LocationManager?,
    private val locationProvider: LocationProvider,
    settings: Settings,
    coroutineScope: CoroutineScope,
) : BaseLocationManager(settings, coroutineScope) {

    /**
     * Builder for creating a [DefaultLocationManager]
     * @param context the [Context] in which to manage the location
     * @param locationManager the [android.location.LocationManager] to manage the location
     * @param createLocationProvider method for creating a [LocationProvider] from [BaseLocationManager.Settings]
     */
    class Builder(
        private val context: Context = ApplicationHolder.applicationContext,
        private val locationManager: android.location.LocationManager? = context.getSystemService(
            Context.LOCATION_SERVICE,
        ) as? android.location.LocationManager,
        private val createLocationProvider: (Settings) -> LocationProvider,
    ) : BaseLocationManager.Builder {

        /**
         * Constructor that uses a [GoogleLocationProvider]
         * @param context the [Context] in which to manage the location
         * @param locationManager the [android.location.LocationManager] to manage the location
         * @param googleLocationProviderSettings the [GoogleLocationProvider.Settings] to use to create a [GoogleLocationProvider]
         */
        constructor(
            context: Context = ApplicationHolder.applicationContext,
            locationManager: android.location.LocationManager? = context.getSystemService(
                Context.LOCATION_SERVICE,
            ) as? android.location.LocationManager,
            googleLocationProviderSettings: GoogleLocationProvider.Settings,
        ) : this(
            context,
            locationManager,
            { settings ->
                GoogleLocationProvider(context, googleLocationProviderSettings, settings.minUpdateDistanceMeters)
            },
        )

        override fun create(settings: Settings, coroutineScope: CoroutineScope): BaseLocationManager {
            return DefaultLocationManager(
                context,
                locationManager,
                createLocationProvider(settings),
                settings,
                coroutineScope,
            )
        }
    }

    actual override val locationMonitor: LocationMonitor = LocationMonitor.Builder(context, locationManager).create()
    private val monitoringMutex = Mutex()
    private var monitoringLocationJob: Job? = null

    actual override suspend fun requestEnableLocation() {
        EnableServiceActivity.showEnableServiceActivity(context, hashCode().toString(), Intent(ACTION_LOCATION_SOURCE_SETTINGS)).await()
    }

    actual override suspend fun startMonitoringLocation() {
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

    actual override suspend fun stopMonitoringLocation() {
        monitoringMutex.withLock {
            monitoringLocationJob?.let {
                monitoringLocationJob = null
                locationProvider.stopMonitoringLocation(locationPermission)
                it.cancel()
            }
        }
    }
}

/**
 * Default [BaseLocationStateRepoBuilder]
 * @param locationManagerBuilder a [BaseLocationManager.Builder] to create the [BaseLocationManager] to use
 * @param permissionsBuilder a method for creating the [Permissions] object to manage the Location permissions.
 * Needs to have [com.splendo.kaluga.permissions.location.LocationPermission] registered.
 */
actual class LocationStateRepoBuilder(
    private val locationManagerBuilder: BaseLocationManager.Builder,
    private val permissionsBuilder: suspend (CoroutineContext) -> Permissions,
) : BaseLocationStateRepoBuilder {

    /**
     * Constructor
     * @param context the [Context] in which to manage the location state
     * @param locationManager the [android.location.LocationManager] to manage the location
     * @param createLocationProvider method for creating a [LocationProvider] from [BaseLocationManager.Settings]
     */
    constructor(
        context: Context = ApplicationHolder.applicationContext,
        locationManager: android.location.LocationManager? = context.getSystemService(
            Context.LOCATION_SERVICE,
        ) as? android.location.LocationManager,
        createLocationProvider: (Settings) -> LocationProvider,
    ) : this(
        DefaultLocationManager.Builder(context, locationManager, createLocationProvider),
        { coroutineContext ->
            Permissions(
                PermissionsBuilder(PermissionContext(context)).apply { registerLocationPermissionIfNotRegistered() },
                coroutineContext,
            )
        },
    )

    /**
     * Constructor that uses a [GoogleLocationProvider]
     * @param context the [Context] in which to manage the location state
     * @param locationManager the [android.location.LocationManager] to manage the location
     * @param googleLocationProviderSettings the [GoogleLocationProvider.Settings] to use to create a [GoogleLocationProvider]
     */
    constructor(
        context: Context = ApplicationHolder.applicationContext,
        locationManager: android.location.LocationManager? = context.getSystemService(
            Context.LOCATION_SERVICE,
        ) as? android.location.LocationManager,
        googleLocationProviderSettings: GoogleLocationProvider.Settings,
    ) : this(
        context,
        locationManager,
        { settings ->
            GoogleLocationProvider(context, googleLocationProviderSettings, settings.minUpdateDistanceMeters)
        },
    )

    actual override fun create(
        locationPermission: LocationPermission,
        settingsBuilder: (LocationPermission, Permissions) -> Settings,
        coroutineContext: CoroutineContext,
    ): LocationStateRepo {
        return LocationStateRepo({ settingsBuilder(locationPermission, permissionsBuilder(it)) }, locationManagerBuilder, coroutineContext)
    }
}
