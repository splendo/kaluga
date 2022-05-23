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

package com.splendo.kaluga.test.permissions

import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import kotlin.coroutines.CoroutineContext

/**
 * Mock implementation of [PermissionStateRepo]
 */
class MockPermissionStateRepo<P : Permission>(
    val builder: MockPermissionManager.Builder<P> = MockPermissionManager.Builder(),
    coroutineContext: CoroutineContext
) : PermissionStateRepo<P>(builder.monitoringInterval, coroutineContext, { builder.create(it) }) {

    /**
     * The [MockPermissionManager] of this repo
     */
    val permissionManager get() = builder.createdManagers.first()
}
