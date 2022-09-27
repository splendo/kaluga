/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth

import co.touchlab.stately.concurrency.AtomicReference
import co.touchlab.stately.concurrency.value
import com.splendo.kaluga.base.monitor.DefaultServiceMonitor
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBCentralManagerStatePoweredOff
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn
import platform.CoreBluetooth.CBCentralManagerStateResetting
import platform.CoreBluetooth.CBCentralManagerStateUnauthorized
import platform.CoreBluetooth.CBCentralManagerStateUnknown
import platform.CoreBluetooth.CBCentralManagerStateUnsupported
import platform.CoreBluetooth.CBManagerState
import platform.darwin.NSObject
import kotlin.coroutines.CoroutineContext

actual interface BluetoothMonitor {
    /**
     * Builder for [BluetoothMonitor].
     * @param centralManager [CBCentralManager] used to set/unset a delegate and get info about bluetooth service status.
     */
    actual class Builder(
        private val permissions: Permissions = Permissions(
            PermissionsBuilder().apply { registerBluetoothPermission() },
            Dispatchers.Main
        ),
        private val bluetoothPermission: BluetoothPermission = BluetoothPermission // In case permission is changed into a class we inject it
    ) {
        /**
         * Builder's create method.
         * @param coroutineContext [CoroutineContext] used to define the coroutine context where code will run.
         * @return [DefaultServiceMonitor]
         */
        actual fun create(coroutineContext: CoroutineContext): DefaultServiceMonitor =
            DefaultBluetoothMonitor(permissions, bluetoothPermission, coroutineContext)
    }
}

class DefaultBluetoothMonitor internal constructor(
    private val permissions: Permissions,
    private val bluetoothPermission: Permission,
    coroutineContext: CoroutineContext
) : DefaultServiceMonitor(coroutineContext), BluetoothMonitor {

    internal class CentralManagerDelegate(
        private val updateState: (CBManagerState) -> Unit
    ) : NSObject(), CBCentralManagerDelegateProtocol {
        override fun centralManagerDidUpdateState(central: CBCentralManager) {
            updateState(central.state)
        }
    }

    private val centralManager = AtomicReference<CBCentralManager?>(null)
    private val centralManagerDelegate by lazy { CentralManagerDelegate(::updateState) }

    private val supervisorJob = SupervisorJob(coroutineContext[Job])

    override fun startMonitoring() {
        super.startMonitoring()

        supervisorJob.cancelChildren()
        CoroutineScope(coroutineContext + supervisorJob).launch {
            permissions[bluetoothPermission].collect {
                when (it) {
                    is PermissionState.Allowed -> if (centralManager.compareAndSet(null, CBCentralManager())) {
                        centralManager.value?.run {
                            delegate = centralManagerDelegate
                            updateState(state)
                        }

                    }
                    is PermissionState.Denied -> {
                        updateAndTearDownCentralManager(CBCentralManagerStateUnauthorized)
                    }
                    is PermissionState.Unknown -> {
                        updateAndTearDownCentralManager(CBCentralManagerStateUnknown)
                    }
                }
            }
        }

    }

    override fun stopMonitoring() {
        super.stopMonitoring()

        supervisorJob.cancelChildren()
        centralManager.value?.delegate = null
        centralManager.value = null
    }

    private fun updateAndTearDownCentralManager(status: CBManagerState) {
        centralManager.value?.delegate = null
        centralManager.value = null
        updateState(status)
    }

    private fun updateState(status: CBManagerState) {
        launchTakeAndChangeState {
            {
                when (status) {
                    CBCentralManagerStatePoweredOn -> ServiceMonitorStateImpl.Initialized.Enabled
                    CBCentralManagerStatePoweredOff,
                    CBCentralManagerStateResetting -> ServiceMonitorStateImpl.Initialized.Disabled
                    CBCentralManagerStateUnsupported -> ServiceMonitorStateImpl.NotSupported
                    CBCentralManagerStateUnauthorized -> ServiceMonitorStateImpl.Initialized.Unauthorized
                    CBCentralManagerStateUnknown -> ServiceMonitorStateImpl.NotInitialized
                    else -> ServiceMonitorStateImpl.NotInitialized
                }
            }
        }
    }
}
