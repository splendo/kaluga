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

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import com.splendo.kaluga.permissions.AndroidPermissionsManager
import com.splendo.kaluga.permissions.PermissionContext
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionStateRepo

actual class MicrophonePermissionManager(
    context: Context,
    stateRepo: PermissionStateRepo<MicrophonePermission>
) : PermissionManager<MicrophonePermission>(stateRepo) {

    private val permissionsManager = AndroidPermissionsManager(context, this, arrayOf(Manifest.permission.RECORD_AUDIO))
    private val supported = context.packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)

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

actual class MicrophonePermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseMicrophonePermissionManagerBuilder {

    override fun create(repo: PermissionStateRepo<MicrophonePermission>): PermissionManager<MicrophonePermission> {
        return MicrophonePermissionManager(context.context, repo)
    }
}
