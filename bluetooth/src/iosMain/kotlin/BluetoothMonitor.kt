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

import com.splendo.kaluga.service.DefaultServiceMonitor
import com.splendo.kaluga.service.ServiceMonitor
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.darwin.NSObject

/**
 * A [ServiceMonitor] that monitors whether Bluetooth is enabled
 */
actual interface BluetoothMonitor : ServiceMonitor {

    /**
     * Builder for creating a [BluetoothMonitor]
     * @param centralManagerBuilder a method cor creating a [CBCentralManager] to manage Bluetooth
     */
    actual class Builder(private val centralManagerBuilder: () -> CBCentralManager = { CBCentralManager() }) {

        /**
         * Creates the [BluetoothMonitor]
         * @return the [BluetoothMonitor] created
         */
        actual fun create(): BluetoothMonitor = DefaultBluetoothMonitor(centralManagerBuilder = centralManagerBuilder)
    }
}

/**
 * A default implementation of [BluetoothMonitor]
 * @param centralManagerBuilder a method cor creating a [CBCentralManager] to manage Bluetooth
 */
class DefaultBluetoothMonitor internal constructor(private val centralManagerBuilder: () -> CBCentralManager) :
    DefaultServiceMonitor(),
    BluetoothMonitor {

    internal class CentralManagerDelegate(private val updateEnabledState: () -> Unit) :
        NSObject(),
        CBCentralManagerDelegateProtocol {

        override fun centralManagerDidUpdateState(central: CBCentralManager) {
            updateEnabledState()
        }
    }

    private val lock = reentrantLock()
    private var centralManager: CBCentralManager? = null

    private val centralManagerDelegate = CentralManagerDelegate(::updateState)
    override val isServiceEnabled: Boolean
        get() = initializeCentralManagerIfNotInitialized().state == CBManagerStatePoweredOn

    private fun initializeCentralManagerIfNotInitialized(): CBCentralManager = lock.withLock {
        centralManager ?: centralManagerBuilder().also { this.centralManager = it }
    }

    override fun monitoringDidStart() {
        initializeCentralManagerIfNotInitialized().delegate = centralManagerDelegate
        updateState()
    }

    override fun monitoringDidStop() {
        centralManager?.delegate = null
    }
}
