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
import android.bluetooth.BluetoothManager
import android.content.Context
import com.splendo.kaluga.permissions.base.AndroidPermissionsManager
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.handleAndroidPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration

actual class DefaultBluetoothPermissionManager(
    context: Context,
    bluetoothAdapter: BluetoothAdapter?,
    settings: Settings,
    coroutineScope: CoroutineScope
) : BasePermissionManager<BluetoothPermission>(BluetoothPermission, settings, coroutineScope) {

    private val permissionsManager = AndroidPermissionsManager(
        context,
        arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION),
        coroutineScope,
        ::logDebug,
        ::logError,
        ::handleAndroidPermissionState
    )

    private val supported: Boolean = bluetoothAdapter != null

    override fun requestPermission() {
        super.requestPermission()
        if (supported)
            permissionsManager.requestPermissions()
        else
            logError { "Bluetooth Not Supported" }
    }

    override fun startMonitoring(interval: Duration) {
        super.startMonitoring(interval)
        if (supported)
            permissionsManager.startMonitoring(interval)
        else
            revokePermission(true)
    }

    override fun stopMonitoring() {
        super.stopMonitoring()
        if (supported)
            permissionsManager.stopMonitoring()
    }
}

actual class BluetoothPermissionManagerBuilder(
    private val context: PermissionContext,
    private val bluetoothAdapter: BluetoothAdapter?
) : BaseBluetoothPermissionManagerBuilder {

    actual constructor(context: PermissionContext) : this(context, (context.context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)?.adapter)

    override fun create(settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): BluetoothPermissionManager {
        return DefaultBluetoothPermissionManager(context.context, bluetoothAdapter, settings, coroutineScope)
    }
}
