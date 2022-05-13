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

package com.splendo.kaluga.permissions.microphone

import com.splendo.kaluga.permissions.BasePermissionsBuilder
import com.splendo.kaluga.permissions.PermissionContext
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionStateRepo
import com.splendo.kaluga.permissions.defaultPermissionContext
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * A [PermissionManager] for managing [MicrophonePermission]
 */
expect class MicrophonePermissionManager : PermissionManager<MicrophonePermission>

interface BaseMicrophonePermissionManagerBuilder : BasePermissionsBuilder<MicrophonePermission> {

    /**
     * Creates a [MicrophonePermissionManager]
     * @param repo The [MicrophonePermissionStateRepo] associated with the [MicrophonePermission]
     */
    fun create(repo: PermissionStateRepo<MicrophonePermission>): PermissionManager<MicrophonePermission>
}

/**
 * A builder for creating a [MicrophonePermissionManager]
 */
expect class MicrophonePermissionManagerBuilder(context: PermissionContext = defaultPermissionContext) : BaseMicrophonePermissionManagerBuilder

/**
 * A [PermissionStateRepo] for [MicrophonePermission]
 * @param builder The [MicrophonePermissionManagerBuilder] for creating the [MicrophonePermissionManager] associated with the permission
 * @param coroutineContext The [CoroutineContext] to run the state machine on.
 */
class MicrophonePermissionStateRepo(
    builder: BaseMicrophonePermissionManagerBuilder,
    monitoringInterval: Long = defaultMonitoringInterval,
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : PermissionStateRepo<MicrophonePermission>(monitoringInterval, coroutineContext, { builder.create(it) })
