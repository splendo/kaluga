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

import com.splendo.kaluga.permissions.location.LocationPermission
import kotlinx.coroutines.flow.Flow

/**
 * A provider of [Location.KnownLocation]
 */
interface LocationProvider {

    /**
     * Gets a [Flow] providing updates to the [Location.KnownLocation]
     * @param permission the [LocationPermission] to use for getting the [Location.KnownLocation]
     * @return a [Flow] containing the latest [Location.KnownLocation] batched in a List
     */
    fun location(permission: LocationPermission): Flow<List<Location.KnownLocation>>

    /**
     * Starts monitoring for [Location.KnownLocation] with a given [LocationPermission]
     * @param permission the [LocationPermission] to use for monitoring the [Location.KnownLocation]
     */
    fun startMonitoringLocation(permission: LocationPermission)

    /**
     * Stops monitoring for [Location.KnownLocation] with a given [LocationPermission]
     * @param permission the [LocationPermission] to that was used for monitoring the [Location.KnownLocation]
     */
    fun stopMonitoringLocation(permission: LocationPermission)
}
