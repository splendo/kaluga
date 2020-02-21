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
import com.splendo.kaluga.permissions.PermissionState

actual class CameraPermissionManager(repo: CameraPermissionStateRepo) : PermissionManager<Permission.Camera>(repo) {

    override suspend fun requestPermission() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initializeState(): PermissionState<Permission.Camera> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startMonitoring(interval: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stopMonitoring() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

actual class CameraPermissionManagerBuilder :BaseCameraPermissionManagerBuilder {

    override fun create(repo: CameraPermissionStateRepo): CameraPermissionManager {
        return CameraPermissionManager(repo)
    }
}