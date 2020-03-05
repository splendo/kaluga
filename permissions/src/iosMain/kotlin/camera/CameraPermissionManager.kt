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
import com.splendo.kaluga.permissions.av.AVPermissionHelper
import platform.Foundation.NSBundle

actual class CameraPermissionManager(
    private val bundle: NSBundle,
    stateRepo: CameraPermissionStateRepo
) : PermissionManager<Permission.Camera>(stateRepo) {

    private val avPermissionHelper = AVPermissionHelper(bundle, AVPermissionHelper.Type.Camera(this))

    override suspend fun requestPermission() {
        avPermissionHelper.requestPermission()
    }

    override suspend fun initializeState(): PermissionState<Permission.Camera> {
        return avPermissionHelper.initializeState()
    }

    override suspend fun startMonitoring(interval: Long) {
        avPermissionHelper.startMonitoring(interval)
    }

    override suspend fun stopMonitoring() {
        avPermissionHelper.stopMonitoring()
    }
}

actual class CameraPermissionManagerBuilder(private val bundle: NSBundle = NSBundle.mainBundle) : BaseCameraPermissionManagerBuilder {

    override fun create(repo: CameraPermissionStateRepo): PermissionManager<Permission.Camera> {
        return CameraPermissionManager(bundle, repo)
    }

}