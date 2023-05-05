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

package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.PermissionState
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermission
import com.splendo.kaluga.service.DefaultServiceMonitor
import com.splendo.kaluga.service.ServiceMonitor
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

/**
 * A [ServiceMonitor] that monitors whether Bluetooth is enabled
 */
actual interface BluetoothMonitor {

    /**
     * Builder for creating a [BluetoothMonitor]
     * @param centralManagerBuilder a method cor creating a [CBCentralManager] to manage Bluetooth
     */
    actual class Builder(
        private val permissions: Permissions = Permissions(
            PermissionsBuilder().apply { registerBluetoothPermission() },
            Dispatchers.Main
        ),
        private val bluetoothPermission: BluetoothPermission = BluetoothPermission, // In case permission is changed into a class we inject it
        private val centralManagerBuilder: () -> CBCentralManager = { CBCentralManager() }
    ) {

        /**
         * Creates the [BluetoothMonitor]
         * @return the [BluetoothMonitor] created
         */
        actual fun create(coroutineContext: CoroutineContext): DefaultServiceMonitor = DefaultBluetoothMonitor(permissions, bluetoothPermission, coroutineContext, centralManagerBuilder)
    }
}

/**
 * A default implementation of [BluetoothMonitor]
 * @param centralManagerBuilder a method cor creating a [CBCentralManager] to manage Bluetooth
 */
class DefaultBluetoothMonitor internal constructor(
    private val permissions: Permissions,
    private val bluetoothPermission: Permission,
    coroutineContext: CoroutineContext,
    private val centralManagerBuilder: () -> CBCentralManager
) : DefaultServiceMonitor(coroutineContext = coroutineContext), BluetoothMonitor {

    internal class CentralManagerDelegate(
        private val updateState: (CBManagerState) -> Unit
    ) : NSObject(), CBCentralManagerDelegateProtocol {

        override fun centralManagerDidUpdateState(central: CBCentralManager) {
            updateState(central.state)
        }
    }

    private val lock = reentrantLock()
    private var centralManager: CBCentralManager? = null
    private val centralManagerDelegate by lazy { CentralManagerDelegate(::updateState) }

    private val supervisorJob = SupervisorJob(coroutineContext[Job])

    private fun initializeCentralManagerIfNotInitialized(): CBCentralManager = lock.withLock {
        centralManager ?: centralManagerBuilder().also { this.centralManager = it }
    }

    override fun monitoringDidStart() {
        supervisorJob.cancelChildren()
        CoroutineScope(coroutineContext + supervisorJob).launch {
            permissions[bluetoothPermission].collect {
                when (it) {
                    is PermissionState.Allowed -> {
                        initializeCentralManagerIfNotInitialized().run {
                            delegate = centralManagerDelegate
                            updateState(state)
                        }
                    }

                    is PermissionState.Denied -> {
                        updateAndTearDownCentralManager(CBCentralManagerStateUnauthorized)
                    }

                    is PermissionState.Inactive -> {
                        updateAndTearDownCentralManager(CBCentralManagerStateUnknown)
                    }
                    else -> {
                        // DOUBLE_CHECK
                    }
                }
            }
        }
    }

    override fun monitoringDidStop() {
        centralManager?.delegate = null
    }

    private fun updateAndTearDownCentralManager(status: CBManagerState) {
        centralManager?.delegate = null
        centralManager = null
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
