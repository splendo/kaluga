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

import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionStateRepo

/**
 * A [PermissionManager] for managing [Permission.Microphone]
 */
expect class MicrophonePermissionManager : PermissionManager<Permission.Microphone>

/**
 * A builder for creating a [MicrophonePermissionManager]
 */
expect class MicrophonePermissionManagerBuilder {

    /**
     * Creates a [MicrophonePermissionManager]
     * @param repo The [MicrophonePermissionStateRepo] associated with the [Permission.Microphone]
     */
    fun create(repo: MicrophonePermissionStateRepo): MicrophonePermissionManager
}

/**
 * A [PermissionStateRepo] for [Permission.Microphone]
 * @param builder The [MicrophonePermissionManagerBuilder] for creating the [MicrophonePermissionManager] associated with the permission
 */
class MicrophonePermissionStateRepo(builder: MicrophonePermissionManagerBuilder) : PermissionStateRepo<Permission.Microphone>() {

    override val permissionManager: PermissionManager<Permission.Microphone> = builder.create(this)
}
