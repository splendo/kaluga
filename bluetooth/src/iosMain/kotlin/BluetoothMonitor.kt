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

import co.touchlab.stately.concurrency.AtomicBoolean
import co.touchlab.stately.concurrency.AtomicReference
import co.touchlab.stately.concurrency.value
import com.splendo.kaluga.base.monitor.DefaultServiceMonitor
import com.splendo.kaluga.base.monitor.ServiceMonitor
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn
import platform.darwin.NSObject

actual interface BluetoothMonitor : ServiceMonitor {
    actual class Builder(
        private val centralManagerBuilder: () -> CBCentralManager = { CBCentralManager() }
    ) {
        actual fun create(): BluetoothMonitor = DefaultBluetoothMonitor(centralManagerBuilder = centralManagerBuilder)
    }
}

class DefaultBluetoothMonitor internal constructor(
    private val centralManagerBuilder: () -> CBCentralManager
) : DefaultServiceMonitor(), BluetoothMonitor {

    internal class CentralManagerDelegate(
        private val updateEnabledState: () -> Unit
    ) : NSObject(), CBCentralManagerDelegateProtocol {

        val isMonitoring = AtomicBoolean(false)

        override fun centralManagerDidUpdateState(central: CBCentralManager) {
            if (isMonitoring.value) {
                updateEnabledState()
            }
        }
    }

    private val centralManager = AtomicReference<CBCentralManager?>(null)

    private val centralManagerDelegate = CentralManagerDelegate(::updateState)
    override val isServiceEnabled: Boolean
        get() = initializeCentralManagerIfNotInitialized().state == CBCentralManagerStatePoweredOn

    private fun initializeCentralManagerIfNotInitialized(): CBCentralManager = centralManager.value ?: centralManagerBuilder().apply {
        delegate = centralManagerDelegate
        centralManager.set(this)
    }

    override fun startMonitoring() {
        super.startMonitoring()
        initializeCentralManagerIfNotInitialized()
        updateState()
        centralManagerDelegate.isMonitoring.value = true
    }

    override fun stopMonitoring() {
        super.stopMonitoring()
        initializeCentralManagerIfNotInitialized()
        centralManagerDelegate.isMonitoring.value = false
    }
}
