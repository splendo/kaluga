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

package com.splendo.kaluga.permissions.camera

import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionStateRepo

expect class CameraPermissionManager : PermissionManager<Permission.Camera>

interface BaseCameraPermissionManagerBuilder {
    fun create(repo: CameraPermissionStateRepo): CameraPermissionManager
}

expect class CameraPermissionManagerBuilder :BaseCameraPermissionManagerBuilder

class CameraPermissionStateRepo(builder: CameraPermissionManagerBuilder) : PermissionStateRepo<Permission.Camera>() {

    override val permissionManager: PermissionManager<Permission.Camera> = builder.create(this)

}