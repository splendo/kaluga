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
import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.base.AndroidPermissionState
import com.splendo.kaluga.permissions.base.AndroidPermissionStateHandler
import com.splendo.kaluga.permissions.base.AndroidPermissionsManager
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.PermissionContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.time.Duration

actual class DefaultBluetoothPermissionManager(
    context: Context,
    bluetoothAdapter: BluetoothAdapter?,
    settings: Settings,
    coroutineScope: CoroutineScope
) : BasePermissionManager<BluetoothPermission>(BluetoothPermission, settings, coroutineScope) {

    private val permissionHandler = AndroidPermissionStateHandler(sharedEvents, logTag, logger)
    private val permissionsManager = AndroidPermissionsManager(
        context,
        arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION),
        coroutineScope,
        logTag,
        logger,
        permissionHandler
    )

    private val supported: Boolean = bluetoothAdapter != null

    override fun requestPermission() {
        super.requestPermission()
        if (supported)
            permissionsManager.requestPermissions()
        else
            logger.error(logTag) { "Bluetooth Not Supported" }
    }

    override fun startMonitoring(interval: Duration) {
        super.startMonitoring(interval)
        if (supported)
            permissionsManager.startMonitoring(interval)
        else {
            val handler = permissionHandler
            launch {
                handler.emit(AndroidPermissionState.DENIED_DO_NOT_ASK)
            }
        }
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
