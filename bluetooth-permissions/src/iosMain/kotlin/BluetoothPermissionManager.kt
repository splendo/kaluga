/*

Copyright 2022 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.base.IOSVersion
import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.BasePermissionManager.Settings
import com.splendo.kaluga.permissions.base.CurrentAuthorizationStatusProvider
import com.splendo.kaluga.permissions.base.DefaultAuthorizationStatusHandler
import com.splendo.kaluga.permissions.base.IOSPermissionsHelper
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionRefreshScheduler
import kotlinx.coroutines.CoroutineScope
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerOptionShowPowerAlertKey
import platform.CoreBluetooth.CBManager
import platform.CoreBluetooth.CBManagerAuthorization
import platform.CoreBluetooth.CBManagerAuthorizationAllowedAlways
import platform.CoreBluetooth.CBManagerAuthorizationDenied
import platform.CoreBluetooth.CBManagerAuthorizationNotDetermined
import platform.CoreBluetooth.CBManagerAuthorizationRestricted
import platform.CoreBluetooth.CBPeripheralManager
import platform.CoreBluetooth.CBPeripheralManagerAuthorizationStatus
import platform.CoreBluetooth.CBPeripheralManagerAuthorizationStatusAuthorized
import platform.CoreBluetooth.CBPeripheralManagerAuthorizationStatusDenied
import platform.CoreBluetooth.CBPeripheralManagerAuthorizationStatusNotDetermined
import platform.CoreBluetooth.CBPeripheralManagerAuthorizationStatusRestricted
import platform.Foundation.NSBundle
import platform.darwin.dispatch_queue_create
import kotlin.time.Duration

private const val NS_BLUETOOTH_ALWAYS_USAGE_DESCRIPTION = "NSBluetoothAlwaysUsageDescription"
private const val NS_BLUETOOTH_PERIPHERAL_USAGE_DESCRIPTION = "NSBluetoothPeripheralUsageDescription"

/**
 * The [BasePermissionManager] to use as a default for [BluetoothPermission]
 * @param bundle the [NSBundle] the [BluetoothPermission] is to be granted in
 * @param settings the [Settings] to apply to this manager.
 * @param coroutineScope the [CoroutineScope] of this manager.
 */
actual class DefaultBluetoothPermissionManager(
    private val bundle: NSBundle,
    settings: Settings,
    coroutineScope: CoroutineScope,
) : BasePermissionManager<BluetoothPermission>(BluetoothPermission, settings, coroutineScope) {

    companion object {
        private fun checkAuthorization(): IOSPermissionsHelper.AuthorizationStatus {
            val version = IOSVersion.systemVersion
            return when {
                version >= IOSVersion(13) -> CBManager.authorization.toAuthorizationStatus()
                else -> CBPeripheralManager.authorizationStatus().toPeripheralAuthorizationStatus()
            }
        }
    }

    private class Provider : CurrentAuthorizationStatusProvider {
        override suspend fun provide(): IOSPermissionsHelper.AuthorizationStatus = checkAuthorization()
    }

    private val permissionsQueue = dispatch_queue_create("BluetoothPermissionsMonitor", null)
    private val provider = Provider()

    private val centralManager = lazy {
        val options = mapOf<Any?, Any>(CBCentralManagerOptionShowPowerAlertKey to false)
        CBCentralManager(null, permissionsQueue, options)
    }

    private val permissionHandler: DefaultAuthorizationStatusHandler
    private val timerHelper: PermissionRefreshScheduler

    init {
        permissionHandler = DefaultAuthorizationStatusHandler(eventChannel, logTag, logger)
        timerHelper = PermissionRefreshScheduler(provider, permissionHandler, coroutineScope)
    }

    actual override fun requestPermissionDidStart() {
        if (IOSPermissionsHelper.missingDeclarationsInPList(
                bundle,
                NS_BLUETOOTH_ALWAYS_USAGE_DESCRIPTION,
                NS_BLUETOOTH_PERIPHERAL_USAGE_DESCRIPTION,
            ).isEmpty()
        ) {
            centralManager.value
        } else {
            val permissionHandler = permissionHandler
            permissionHandler.status(IOSPermissionsHelper.AuthorizationStatus.Restricted)
        }
    }

    actual override fun monitoringDidStart(interval: Duration) {
        val permissionHandler = permissionHandler
        permissionHandler.status(checkAuthorization())
        timerHelper.startMonitoring(interval)
    }

    actual override fun monitoringDidStop() {
        timerHelper.stopMonitoring()
    }
}

/**
 * A [BaseBluetoothPermissionManagerBuilder]
 * @param context the [PermissionContext] this permissions manager builder runs on
 */
actual class BluetoothPermissionManagerBuilder actual constructor(
    private val context: PermissionContext,
) : BaseBluetoothPermissionManagerBuilder {

    actual override fun create(settings: Settings, coroutineScope: CoroutineScope): BluetoothPermissionManager {
        return DefaultBluetoothPermissionManager(context, settings, coroutineScope)
    }
}

private fun CBPeripheralManagerAuthorizationStatus.toPeripheralAuthorizationStatus(): IOSPermissionsHelper.AuthorizationStatus {
    return when (this) {
        CBPeripheralManagerAuthorizationStatusAuthorized -> IOSPermissionsHelper.AuthorizationStatus.Authorized
        CBPeripheralManagerAuthorizationStatusDenied -> IOSPermissionsHelper.AuthorizationStatus.Denied
        CBPeripheralManagerAuthorizationStatusRestricted -> IOSPermissionsHelper.AuthorizationStatus.Restricted
        CBPeripheralManagerAuthorizationStatusNotDetermined -> IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        else -> {
            error(
                "BluetoothPermissionManager",
                "Unknown CBPeripheralManagerAuthorizationStatus status={$this}",
            )
            IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        }
    }
}

private fun CBManagerAuthorization.toAuthorizationStatus(): IOSPermissionsHelper.AuthorizationStatus {
    return when (this) {
        CBManagerAuthorizationAllowedAlways -> IOSPermissionsHelper.AuthorizationStatus.Authorized
        CBManagerAuthorizationDenied -> IOSPermissionsHelper.AuthorizationStatus.Denied
        CBManagerAuthorizationRestricted -> IOSPermissionsHelper.AuthorizationStatus.Restricted
        CBManagerAuthorizationNotDetermined -> IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        else -> {
            error(
                "BluetoothPermissionManager",
                "Unknown CBManagerAuthorization status={$this}",
            )
            IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        }
    }
}
