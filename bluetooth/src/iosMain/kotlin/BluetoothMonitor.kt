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

import com.splendo.kaluga.base.DefaultServiceMonitor
import com.splendo.kaluga.base.ServiceMonitor
import com.splendo.kaluga.base.monitor.ServiceMonitorStateImpl
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

actual interface BluetoothMonitor : ServiceMonitor {
    /**
     * Builder for [BluetoothMonitor].
     * @param centralManager [CBCentralManager] used to set/unset a delegate and get info about bluetooth service status.
     */
    actual class Builder(
        private val centralManager: CBCentralManager = CBCentralManager()
    ) {
        actual constructor() : this(CBCentralManager())

        /**
         * Builder's create method.
         * @param coroutineContext [CoroutineContext] used to define the coroutine context where code will run.
         * @return [DefaultServiceMonitor]
         */
        actual fun create(coroutineContext: CoroutineContext): DefaultServiceMonitor =
            DefaultBluetoothMonitor(
                centralManager = centralManager,
                coroutineContext = coroutineContext
            )
    }
}

class DefaultBluetoothMonitor internal constructor(
    private val centralManager: CBCentralManager,
    coroutineContext: CoroutineContext
) : DefaultServiceMonitor(coroutineContext), BluetoothMonitor {

    internal class CentralManagerDelegate(
        private val updateState: (CBManagerState) -> Unit
    ) : NSObject(), CBCentralManagerDelegateProtocol {
        override fun centralManagerDidUpdateState(central: CBCentralManager) {
            updateState(central.state)
        }
    }

    private val centralManagerDelegate = CentralManagerDelegate(::updateState)

    override fun startMonitoring() {
        super.startMonitoring()
        centralManager.delegate = centralManagerDelegate

        updateState(centralManager.state)
    }

    override fun stopMonitoring() {
        super.stopMonitoring()
        centralManager.delegate = null
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
