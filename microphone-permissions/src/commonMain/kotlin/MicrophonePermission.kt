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

import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.BasePermissionsBuilder
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionManager
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import com.splendo.kaluga.permissions.base.defaultPermissionContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/**
 * A [PermissionManager] for managing [MicrophonePermission]
 */
typealias MicrophonePermissionManager = PermissionManager<MicrophonePermission>
expect class DefaultMicrophonePermissionManager : BasePermissionManager<MicrophonePermission>

interface BaseMicrophonePermissionManagerBuilder : BasePermissionsBuilder<MicrophonePermission> {

    /**
     * Creates a [MicrophonePermissionManager]
     * @param settings [BasePermissionManager.Settings] to configure the manager
     * @param coroutineScope The [CoroutineScope] the manager runs on
     */
    fun create(settings: BasePermissionManager.Settings = BasePermissionManager.Settings(), coroutineScope: CoroutineScope): MicrophonePermissionManager
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
    monitoringInterval: Duration = defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : PermissionStateRepo<MicrophonePermission>(monitoringInterval, { builder.create(settings, it) }, coroutineContext)
