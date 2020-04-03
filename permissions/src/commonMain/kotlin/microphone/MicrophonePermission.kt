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
import kotlinx.coroutines.CoroutineScope

expect class MicrophonePermissionManager : PermissionManager<Permission.Microphone>

interface BaseMicrophonePermissionManagerBuilder {
    fun create(repo: MicrophonePermissionStateRepo, coroutineScope: CoroutineScope): PermissionManager<Permission.Microphone>
}

expect class MicrophonePermissionManagerBuilder :BaseMicrophonePermissionManagerBuilder

class MicrophonePermissionStateRepo(builder: BaseMicrophonePermissionManagerBuilder, coroutineScope: CoroutineScope) : PermissionStateRepo<Permission.Microphone>(coroutineScope = coroutineScope) {

    override val permissionManager: PermissionManager<Permission.Microphone> = builder.create(this, coroutineScope)

}