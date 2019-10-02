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

import com.splendo.mpp.permissions.BluetoothPermissionManager.CBCentralManagerState.*
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBManagerState
import platform.CoreBluetooth.CBPeripheralManager
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

class BluetoothPermissionManager(
    private val cbCentralManager: CBCentralManager,
    private val cbPeripheralManager: CBPeripheralManager
) : PermissionManager() {

    override suspend fun openSettings() {
        UIApplication.sharedApplication.openURL(NSURL(string = "App-Prefs:root=General"))
    }

    override suspend fun checkSupport(): Support {
        return when (CBCentralManagerState.byCBManagerState(cbCentralManager.state)) {
            unsupported -> Support.NOT_SUPPORTED
            resetting -> Support.RESETTING
            unknown -> Support.NOT_SUPPORTED
            unauthorized -> Support.UNAUTHORIZED
            powerOff -> Support.POWER_OFF
            powerOn -> Support.POWER_ON
        }
    }

    override suspend fun checkPermit(): Permit {
        //TODO: Add support for iOS 13. `authorizationStatus` is being deprecated
        //https://github.com/splendo/kaluga/issues/21
        return when (cbPeripheralManager.authorizationStatus().toLong()) {
            AUTHORIZATION_STATUS_NOT_DETERMINED -> Permit.UNDEFINED
            AUTHORIZATION_STATUS_RESTRICTED -> Permit.RESTRICTED
            AUTHORIZATION_STATUS_DENIED -> Permit.DENIED
            AUTHORIZATION_STATUS_AUTHORIZED -> Permit.ALLOWED
            else -> Permit.UNDEFINED
        }
    }

    //https://developer.apple.com/documentation/corebluetooth/cbcentralmanagerstate
    enum class CBCentralManagerState {
        unknown,
        resetting,
        unsupported,
        unauthorized,
        powerOff,
        powerOn;

        companion object {
            fun byCBManagerState(state: CBManagerState): CBCentralManagerState {
                val values = values()
                val ordinal = state.toInt()

                return values[ordinal]
            }
        }
    }

    companion object {


        const val AUTHORIZATION_STATUS_NOT_DETERMINED = 0L
        const val AUTHORIZATION_STATUS_RESTRICTED = 1L
        const val AUTHORIZATION_STATUS_DENIED = 2L
        const val AUTHORIZATION_STATUS_AUTHORIZED = 3L
    }

}

/**
 * Extension for mocking support
 */
public fun CBPeripheralManager.authorizationStatus(): platform.CoreBluetooth.CBPeripheralManagerAuthorizationStatus {
    return CBPeripheralManager.authorizationStatus()
}