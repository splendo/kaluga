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

package com.splendo.kaluga.test.location

import com.splendo.kaluga.location.BaseLocationManager
import com.splendo.kaluga.location.LocationStateRepo
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock
import kotlin.coroutines.CoroutineContext

/**
 * Mock implementation of [LocationStateRepo.Builder]
 * @param permissionsBuilder Builds the [Permissions] to request permissions from
 * @param locationManagerBuilder The [BaseLocationManager.Builder] for building the location manager
 * @param setupMocks If `true` sets up [createMock] to automatically create a [LocationStateRepo]
 */
class MockLocationStateRepoBuilder<LMB : BaseLocationManager.Builder>(
    private val permissionsBuilder: suspend () -> Permissions,
    val locationManagerBuilder: LMB,
    setupMocks: Boolean = true
) : LocationStateRepo.Builder {

    /**
     * List of build [LocationStateRepo]
     */
    val builtLocationStateRepo = mutableListOf<LocationStateRepo>()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [create]
     */
    val createMock = ::create.mock()

    init {
        if (setupMocks) {
            createMock.on()
                .doExecute { (locationPermission, settingsBuilder, coroutineContext) ->
                    LocationStateRepo(
                        { settingsBuilder(locationPermission, permissionsBuilder()) },
                        locationManagerBuilder,
                        coroutineContext
                    ).also {
                        builtLocationStateRepo.add(it)
                    }
                }
        }
    }

    override fun create(
        locationPermission: LocationPermission,
        settingsBuilder: (LocationPermission, Permissions) -> BaseLocationManager.Settings,
        coroutineContext: CoroutineContext
    ): LocationStateRepo = createMock.call(locationPermission, settingsBuilder, coroutineContext)
}
