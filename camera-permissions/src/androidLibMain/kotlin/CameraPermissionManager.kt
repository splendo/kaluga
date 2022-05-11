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

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import com.splendo.kaluga.permissions.AndroidPermissionsManager
import com.splendo.kaluga.permissions.PermissionContext
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionState

actual class CameraPermissionManager(
    context: Context,
    stateRepo: CameraPermissionStateRepo
) : PermissionManager<CameraPermission>(stateRepo) {

    private val permissionsManager = AndroidPermissionsManager(context, this, arrayOf(Manifest.permission.CAMERA))
    private val supported = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)

    override suspend fun requestPermission() {
        if (supported)
            permissionsManager.requestPermissions()
    }

    override suspend fun startMonitoring(interval: Long) {
        if (supported)
            permissionsManager.startMonitoring(interval)
        else
            revokePermission(true)
    }

    override suspend fun stopMonitoring() {
        if (supported)
            permissionsManager.stopMonitoring()
    }
}

actual class CameraPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseCameraPermissionManagerBuilder {

    override fun create(repo: CameraPermissionStateRepo): PermissionManager<CameraPermission> {
        return CameraPermissionManager(context.context, repo)
    }
}
