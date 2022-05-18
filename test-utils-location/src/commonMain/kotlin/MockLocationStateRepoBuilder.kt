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

import co.touchlab.stately.collections.IsoMutableList
import com.splendo.kaluga.location.BaseLocationManager
import com.splendo.kaluga.location.LocationStateRepo
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.test.mock.call
import com.splendo.kaluga.test.mock.on
import com.splendo.kaluga.test.mock.parameters.mock
import kotlin.coroutines.CoroutineContext

class MockLocationStateRepoBuilder<LMB : BaseLocationManager.Builder>(private val permissions: Permissions, val locationManagerBuilder: LMB, setupMocks: Boolean = true) :
    LocationStateRepo.Builder {

    val builtLocationStateRepo = IsoMutableList<LocationStateRepo>()
    val createMock = ::create.mock()

    init {
        if (setupMocks) {
            createMock.on()
                .doExecute { (locationPermission, autoRequestPermission, autoEnableLocations, coroutineContext) ->
                    LocationStateRepo(
                        locationPermission,
                        permissions,
                        autoRequestPermission,
                        autoEnableLocations,
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
        autoRequestPermission: Boolean,
        autoEnableLocations: Boolean,
        coroutineContext: CoroutineContext
    ): LocationStateRepo = createMock.call(locationPermission, autoRequestPermission, autoEnableLocations, coroutineContext)
}
