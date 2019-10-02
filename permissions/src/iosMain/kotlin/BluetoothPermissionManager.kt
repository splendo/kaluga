package com.splendo.kaluga.permissions

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

import com.splendo.kaluga.log.error
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBManagerState
import platform.CoreBluetooth.CBPeripheralManager
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

class BluetoothPermissionManager(
    private val cbCentralManager: CBCentralManager,
    internal var authorizationStatusProvider: () -> platform.CoreBluetooth.CBPeripheralManagerAuthorizationStatus = {
        CBPeripheralManager.authorizationStatus()
    }
) : PermissionManager() {

    override suspend fun openSettings() {
        UIApplication.sharedApplication.openURL(NSURL(string = "App-Prefs:root=General"))
    }

    override suspend fun checkSupport(): Support {
        return when (CBCentralManagerState.byCBManagerState(cbCentralManager.state)) {
            CBCentralManagerState.UNSUPPORTED -> Support.NOT_SUPPORTED
            CBCentralManagerState.RESETTING -> Support.RESETTING
            CBCentralManagerState.UNKNOWN -> Support.NOT_SUPPORTED
            CBCentralManagerState.UNAUTHORIZED -> Support.UNAUTHORIZED
            CBCentralManagerState.POWER_OFF -> Support.POWER_OFF
            CBCentralManagerState.POWER_ON -> Support.POWER_ON
        }
    }

    override suspend fun checkPermit(): Permit {
        //TODO: Add support for iOS 13. `authorizationStatus` is being deprecated
        //https://github.com/splendo/kaluga/issues/21
        return when (CBPeripheralManagerAuthorizationStatus.byCBPeripheralManagerAuthorizationStatus(authorizationStatusProvider())) {
            CBPeripheralManagerAuthorizationStatus.CBPeripheralManagerAuthorizationStatusNotDetermined -> Permit.UNDEFINED
            CBPeripheralManagerAuthorizationStatus.CBPeripheralManagerAuthorizationStatusRestricted -> Permit.RESTRICTED
            CBPeripheralManagerAuthorizationStatus.CBPeripheralManagerAuthorizationStatusDenied -> Permit.DENIED
            CBPeripheralManagerAuthorizationStatus.CBPeripheralManagerAuthorizationStatusAuthorized -> Permit.ALLOWED
        }
    }

    //https://developer.apple.com/documentation/corebluetooth/cbcentralmanagerstate
    enum class CBCentralManagerState {
        UNKNOWN,
        RESETTING,
        UNSUPPORTED,
        UNAUTHORIZED,
        POWER_OFF,
        POWER_ON;

        companion object {
            fun byCBManagerState(state: CBManagerState): CBCentralManagerState {
                val values = values()
                val ordinal = state.toInt()

                return if (values.size <= ordinal) {
                    error("BluetoothPermissionManager", "Unknown CBCentralManagerState state={$state}")

                    UNKNOWN
                } else {
                    values[ordinal]
                }
            }
        }
    }

    //https://developer.apple.com/documentation/corebluetooth/cbperipheralmanagerauthorizationstatus
    enum class CBPeripheralManagerAuthorizationStatus {
        CBPeripheralManagerAuthorizationStatusNotDetermined,
        CBPeripheralManagerAuthorizationStatusRestricted,
        CBPeripheralManagerAuthorizationStatusDenied,
        CBPeripheralManagerAuthorizationStatusAuthorized;

        companion object {
            fun byCBPeripheralManagerAuthorizationStatus(state: platform.CoreBluetooth.CBPeripheralManagerAuthorizationStatus): CBPeripheralManagerAuthorizationStatus {
                val values = values()
                val ordinal = state.toInt()

                return if (values.size <= ordinal) {
                    error("BluetoothPermissionManager", "Unknown CBPeripheralManagerAuthorizationStatus status={$state}")

                    CBPeripheralManagerAuthorizationStatusNotDetermined
                } else {
                    values[ordinal]
                }
            }
        }
    }
}