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
import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/**
 * [Permission] to access the users Microphone
 */
data object MicrophonePermission : Permission() {
    override val name: String = "Microphone"
}

/**
 * Registers a [BaseMicrophonePermissionManagerBuilder] and [PermissionStateRepo] for [MicrophonePermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * Only one builder can be registered.
 * @param microphonePermissionManagerBuilderBuilder method for creating a [BaseMicrophonePermissionManagerBuilder] from a [PermissionContext]
 * @param monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
 * @param settings the [BasePermissionManager.Settings] to apply to any [BasePermissionManager] created using the registered builders.
 * @return the [BaseMicrophonePermissionManagerBuilder] registered
 * @throws [com.splendo.kaluga.permissions.base.PermissionsBuilderError] if either the [BaseMicrophonePermissionManagerBuilder] or [PermissionStateRepo] have already been registered
 */
fun PermissionsBuilder.registerMicrophonePermission(
    microphonePermissionManagerBuilderBuilder: (PermissionContext) -> BaseMicrophonePermissionManagerBuilder = ::MicrophonePermissionManagerBuilder,
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
) = registerMicrophonePermission(microphonePermissionManagerBuilderBuilder) { builder, coroutineContext ->
    MicrophonePermissionStateRepo(builder, monitoringInterval, settings, coroutineContext)
}

/**
 * Registers a [BaseMicrophonePermissionManagerBuilder] and [PermissionStateRepo] for [MicrophonePermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * Only one builder can be registered.
 * @param microphonePermissionManagerBuilderBuilder method for creating a [BaseMicrophonePermissionManagerBuilder] from a [PermissionContext]
 * @param microphonePermissionStateRepoBuilder method for creating a [PermissionStateRepo] for [MicrophonePermission] given a [BaseMicrophonePermissionManagerBuilder] and [CoroutineContext]
 * @return the [BaseMicrophonePermissionManagerBuilder] registered
 * @throws [com.splendo.kaluga.permissions.base.PermissionsBuilderError] if either the [BaseMicrophonePermissionManagerBuilder] or [PermissionStateRepo] have already been registered
 */
fun PermissionsBuilder.registerMicrophonePermission(
    microphonePermissionManagerBuilderBuilder: (PermissionContext) -> BaseMicrophonePermissionManagerBuilder = ::MicrophonePermissionManagerBuilder,
    microphonePermissionStateRepoBuilder: (BaseMicrophonePermissionManagerBuilder, CoroutineContext) -> PermissionStateRepo<MicrophonePermission>,
) = microphonePermissionManagerBuilderBuilder(context).also {
    register(it)
    registerPermissionStateRepoBuilder<MicrophonePermission> { _, coroutineContext ->
        microphonePermissionStateRepoBuilder(it, coroutineContext)
    }
}

/**
 * Gets the [BaseMicrophonePermissionManagerBuilder] registered
 * If not yet registered, this will register a [BaseMicrophonePermissionManagerBuilder] and [PermissionStateRepo] for [MicrophonePermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * @param microphonePermissionManagerBuilderBuilder method for creating a [BaseMicrophonePermissionManagerBuilder] from a [PermissionContext]
 * @param monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
 * @param settings the [BasePermissionManager.Settings] to apply to any [BasePermissionManager] created using the registered builders.
 * @return the [BaseMicrophonePermissionManagerBuilder] registered
 */
fun PermissionsBuilder.registerMicrophonePermissionIfNotRegistered(
    microphonePermissionManagerBuilderBuilder: (PermissionContext) -> BaseMicrophonePermissionManagerBuilder = ::MicrophonePermissionManagerBuilder,
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
) = registerMicrophonePermissionIfNotRegistered(microphonePermissionManagerBuilderBuilder) { builder, coroutineContext ->
    MicrophonePermissionStateRepo(builder, monitoringInterval, settings, coroutineContext)
}

/**
 * Gets the [BaseMicrophonePermissionManagerBuilder] registered
 * If not yet registered, this will register a [BaseMicrophonePermissionManagerBuilder] and [PermissionStateRepo] for [MicrophonePermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * @param microphonePermissionManagerBuilderBuilder method for creating a [BaseMicrophonePermissionManagerBuilder] from a [PermissionContext]
 * @param microphonePermissionStateRepoBuilder method for creating a [PermissionStateRepo] for [MicrophonePermission] given a [BaseMicrophonePermissionManagerBuilder] and [CoroutineContext]
 * @return the [BaseMicrophonePermissionManagerBuilder] registered
 */
fun PermissionsBuilder.registerMicrophonePermissionIfNotRegistered(
    microphonePermissionManagerBuilderBuilder: (PermissionContext) -> BaseMicrophonePermissionManagerBuilder = ::MicrophonePermissionManagerBuilder,
    microphonePermissionStateRepoBuilder: (BaseMicrophonePermissionManagerBuilder, CoroutineContext) -> PermissionStateRepo<MicrophonePermission>,
) = microphonePermissionManagerBuilderBuilder(context).also {
    registerOrGet(it)
    registerOrGetPermissionStateRepoBuilder<MicrophonePermission> { _, coroutineContext ->
        microphonePermissionStateRepoBuilder(it, coroutineContext)
    }
}
