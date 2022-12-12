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

package com.splendo.kaluga.permissions.location

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.CoreLocation.CLLocationManager

/**
 * Accessor to ensure a [CLLocationManager] is only managed from the Main Thread.
 * Use [updateLocationManager] to gain access to the LocationManager
 */
class MainCLLocationManagerAccessor(private val onInit: CLLocationManager.() -> Unit) {

    private var locationManager: CLLocationManager? = null

    private fun createLocationManagerIfNotCreated(): CLLocationManager = locationManager ?: CLLocationManager().apply {
        locationManager = this
        onInit()
    }

    suspend fun <T> updateLocationManager(update: suspend CLLocationManager.() -> T): T {
        // As per the documentation, CLLocationManager must be created and maintained via the main thread
        return withContext(Dispatchers.Main) {
            createLocationManagerIfNotCreated().update()
        }
    }
}
