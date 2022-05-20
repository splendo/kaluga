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

package com.splendo.kaluga.permissions.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.splendo.kaluga.permissions.base.AndroidPermissionsManager
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionManager
import com.splendo.kaluga.permissions.base.PermissionStateRepo

actual class BluetoothPermissionManager(
    context: Context,
    bluetoothAdapter: BluetoothAdapter?,
    stateRepo: PermissionStateRepo<BluetoothPermission>
) : PermissionManager<BluetoothPermission>(stateRepo) {

    private val permissionsManager = AndroidPermissionsManager(context, this, arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION))
    private val supported: Boolean = bluetoothAdapter != null

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

actual class BluetoothPermissionManagerBuilder(
    private val context: PermissionContext,
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
) : BaseBluetoothPermissionManagerBuilder {

    actual constructor(context: PermissionContext) : this(context, BluetoothAdapter.getDefaultAdapter())

    override fun create(repo: PermissionStateRepo<BluetoothPermission>): PermissionManager<BluetoothPermission> {
        return BluetoothPermissionManager(context.context, bluetoothAdapter, repo)
    }
}
