package com.splendo.kaluga.permissions.bluetooth

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

import com.splendo.kaluga.base.IOSVersion
import com.splendo.kaluga.log.error
import com.splendo.kaluga.permissions.IOSPermissionsHelper
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionState
import platform.CoreBluetooth.*
import platform.Foundation.NSBundle
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue

actual class BluetoothPermissionManager(
    private val bundle: NSBundle,
    stateRepo: BluetoothPermissionStateRepo
) : PermissionManager<Permission.Bluetooth>(stateRepo) {

    private val centralManager = lazy {
        val options = mapOf<Any?, Any>(CBCentralManagerOptionShowPowerAlertKey to false)
        CBCentralManager(null, dispatch_get_main_queue(), options)
    }

    private val delegate = object : NSObject(), CBCentralManagerDelegateProtocol {

        override fun centralManagerDidUpdateState(central: CBCentralManager) {
            when (checkAuthorization()) {
                AuthorizationStatus.NotDeterminedcomponent -> revokePermission(false)
                AuthorizationStatus.Authorized -> grantPermission()
                AuthorizationStatus.Denied, AuthorizationStatus.Restricted -> revokePermission(true)
            }
        }
    }

    override suspend fun requestPermission() {
        if (IOSPermissionsHelper.checkDeclarationInPList(bundle, "NSBluetoothAlwaysUsageDescription", "NSBluetoothPeripheralUsageDescription").isEmpty()) {
            if (!centralManager.isInitialized()) {
                centralManager.value
            }
        } else {
            revokePermission(true)
        }
    }

    override fun initializeState(): PermissionState<Permission.Bluetooth> {
        return when (checkAuthorization()) {
            AuthorizationStatus.NotDetermined -> PermissionState.Denied.Requestable(this)
            AuthorizationStatus.Authorized -> PermissionState.Allowed(this)
            AuthorizationStatus.Denied, AuthorizationStatus.Restricted -> PermissionState.Denied.SystemLocked(this)
        }
    }

    override fun startMonitoring(interval: Long) {
        centralManager.value.delegate = delegate
    }

    override fun stopMonitoring() {
        centralManager.value.delegate = null
    }

    private fun checkAuthorization(): AuthorizationStatus {
        val version = IOSVersion.systemVersion
        return when {
            version.isOSVersionOrNewer(IOSVersion(13,0,0)) -> AuthorizationStatus.byCBManagerAuthorization(CBCentralManager().authorization)
            else -> AuthorizationStatus.byCBPeripheralManagerAuthorizationStatus(CBPeripheralManager.authorizationStatus())
        }
    }

    enum class AuthorizationStatus {
        NotDetermined,
        Restricted,
        Denied,
        Authorized;

        companion object {
            fun byCBPeripheralManagerAuthorizationStatus(state: CBPeripheralManagerAuthorizationStatus): AuthorizationStatus {
                return when(state) {
                    CBPeripheralManagerAuthorizationStatusAuthorized -> Authorized
                    CBPeripheralManagerAuthorizationStatusDenied -> Denied
                    CBPeripheralManagerAuthorizationStatusRestricted -> Restricted
                    CBPeripheralManagerAuthorizationStatusNotDetermined -> NotDetermined
                    else -> {
                        error(
                            "BluetoothPermissionManager",
                            "Unknown CBPeripheralManagerAuthorizationStatus status={$state}"
                        )
                        NotDetermined
                    }
                }
            }

            fun byCBManagerAuthorization(state: CBManagerAuthorization): AuthorizationStatus {
                return when(state) {
                    CBManagerAuthorizationAllowedAlways -> Authorized
                    CBManagerAuthorizationDenied -> Denied
                    CBManagerAuthorizationRestricted -> Restricted
                    CBManagerAuthorizationNotDetermined -> NotDetermined
                    else -> {
                        error(
                            "BluetoothPermissionManager",
                            "Unknown CBManagerAuthorization status={$state}"
                        )
                        NotDetermined
                    }
                }
            }
        }
    }
}

actual class BluetoothPermissionManagerBuilder(
    private val bundle: NSBundle = NSBundle.mainBundle) : BaseBluetoothPermissionManagerBuilder {

    override fun create(repo: BluetoothPermissionStateRepo): BluetoothPermissionManager {
        return BluetoothPermissionManager(bundle, repo)
    }

}