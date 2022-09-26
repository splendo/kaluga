/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.permissions.IOSPermissionsHelper
import com.splendo.kaluga.permissions.PermissionContext
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionRefreshScheduler
import com.splendo.kaluga.permissions.PermissionState
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerOptionShowPowerAlertKey
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
import platform.darwin.dispatch_get_main_queue

const val NSBluetoothAlwaysUsageDescription = "NSBluetoothAlwaysUsageDescription"
const val NSBluetoothPeripheralUsageDescription = "NSBluetoothPeripheralUsageDescription"

/**
 * iOS implementation of [BluetoothPermissionManager]
 *
 * @see [PermissionRefreshScheduler] for polling logic
 * @see [PermissionState] for possible [BluetoothPermission] states
 */
actual class BluetoothPermissionManager(
    private val bundle: NSBundle,
    stateRepo: BluetoothPermissionStateRepo
) : PermissionManager<BluetoothPermission>(stateRepo) {

    private val centralManager = lazy {
        val options = mapOf<Any?, Any>(CBCentralManagerOptionShowPowerAlertKey to false)
        CBCentralManager(null, dispatch_get_main_queue(), options)
    }

    private val authorizationStatus = suspend { checkAuthorization() }

    private var timerHelper = PermissionRefreshScheduler(this, authorizationStatus)

    override suspend fun requestPermission() {
        if (IOSPermissionsHelper.missingDeclarationsInPList(
                bundle,
                NSBluetoothAlwaysUsageDescription,
                NSBluetoothPeripheralUsageDescription
            ).isEmpty()
        ) {
            centralManager.value
        } else {
            revokePermission(true)
        }
    }

    override suspend fun initializeState(): PermissionState<BluetoothPermission> {
        return IOSPermissionsHelper.getPermissionState(checkAuthorization())
    }

    override suspend fun startMonitoring(interval: Long) {
        timerHelper.startMonitoring(interval)
    }

    override suspend fun stopMonitoring() {
        timerHelper.stopMonitoring()
    }

    private fun checkAuthorization(): IOSPermissionsHelper.AuthorizationStatus {
        val version = IOSVersion.systemVersion
        return if (version > IOSVersion(13)) {
            CBCentralManager.authorization.toAuthorizationStatus() //https://developer.apple.com/documentation/corebluetooth/cbmanager/3377595-authorization?changes=latest_minor
        } else {
            CBPeripheralManager.authorizationStatus().toPeripheralAuthorizationStatus()
        }
    }
}

actual class BluetoothPermissionManagerBuilder actual constructor(
    private val context: PermissionContext
) : BaseBluetoothPermissionManagerBuilder {

    override fun create(repo: BluetoothPermissionStateRepo): PermissionManager<BluetoothPermission> {
        return BluetoothPermissionManager(context, repo)
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
                "Unknown CBPeripheralManagerAuthorizationStatus status={$this}"
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
                "Unknown CBManagerAuthorization status={$this}"
            )
            IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        }
    }
}
