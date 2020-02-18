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
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermissionStateRepo
import platform.CoreBluetooth.*
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice

actual class BluetoothPermissionManager(
    stateRepo: BluetoothPermissionStateRepo
) : PermissionManager<Permission.Bluetooth>(stateRepo) {

    override suspend fun requestPermission() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initializeState(): PermissionState<Permission.Bluetooth> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startMonitoring(interval: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stopMonitoring() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun checkAuthorization(): AuthorizationStatus {
        val version = IOSVersion.systemVersion
        return when {
            version.isOSVersionOrNewer(IOSVersion(13,0,0)) -> AuthorizationStatus.byCBManagerAuthorization(CBCentralManager().authorization)
            else -> AuthorizationStatus.byCBPeripheralManagerAuthorizationStatus(CBPeripheralManager.authorizationStatus())
        }
    }

    //https://developer.apple.com/documentation/corebluetooth/cbperipheralmanagerauthorizationstatus
    enum class AuthorizationStatus {
        NotDetermined,
        Restricted,
        Denied,
        Authorized;

        companion object {
            fun byCBPeripheralManagerAuthorizationStatus(state: platform.CoreBluetooth.CBPeripheralManagerAuthorizationStatus): AuthorizationStatus {
                return byOrdinal(state.toInt())
            }

            fun byCBManagerAuthorization(state: CBManagerAuthorization): AuthorizationStatus {
                return byOrdinal(state.toInt())
            }

            private fun byOrdinal(ordinal: Int): AuthorizationStatus {
                val values = values()
                return if (values.size <= ordinal) {
                    error(
                        "BluetoothPermissionManager",
                        "Unknown CBPeripheralManagerAuthorizationStatus status={$ordinal}"
                    )

                    NotDetermined
                } else {
                    values[ordinal]
                }
            }
        }
    }
}