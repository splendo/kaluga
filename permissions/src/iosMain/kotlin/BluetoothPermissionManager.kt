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

import platform.CoreBluetooth.CBCentralManager
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
        return when (cbCentralManager.state.toLong()) {
            STATE_UNKNOWN -> Support.NOT_SUPPORTED
            STATE_RESETTING -> Support.RESETTING
            STATE_UNSUPPORTED -> Support.NOT_SUPPORTED
            STATE_UNAUTHORIZED -> Support.UNAUTHORIZED
            STATE_POWERED_OFF -> Support.POWER_OFF
            STATE_POWERED_ON -> Support.POWER_ON
            else -> Support.NOT_SUPPORTED
        }
    }

    override suspend fun checkPermit(): Permit {
        //TODO: Add support for iOS 13. `authorizationStatus` is being deprecated
        return when (cbPeripheralManager.authorizationStatus().toLong()) {
            AUTHORIZATION_STATUS_NOT_DETERMINED -> Permit.UNDEFINED
            AUTHORIZATION_STATUS_RESTRICTED -> Permit.RESTRICTED
            AUTHORIZATION_STATUS_DENIED -> Permit.DENIED
            AUTHORIZATION_STATUS_AUTHORIZED -> Permit.ALLOWED
            else -> Permit.UNDEFINED
        }
    }

    companion object {
        const val STATE_UNKNOWN = 0L
        const val STATE_RESETTING = 1L
        const val STATE_UNSUPPORTED = 2L
        const val STATE_UNAUTHORIZED = 3L
        const val STATE_POWERED_OFF = 4L
        const val STATE_POWERED_ON = 5L

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