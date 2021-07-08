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

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue

actual class BluetoothMonitor(
    private val centralManager: CBCentralManager = CBCentralManager(
        delegate = null, dispatch_get_main_queue(), emptyMap<Any?, Any>()
    )
) {

    private val centralManagerDelegate = object : NSObject(), CBCentralManagerDelegateProtocol {
        override fun centralManagerDidUpdateState(central: CBCentralManager) {
            updateEnabledState()
        }
    }

    private val _isEnabled = MutableStateFlow(false)
    actual val isEnabled = _isEnabled.asStateFlow()

    actual fun startMonitoring() {
        centralManager.delegate = centralManagerDelegate
    }
    actual fun stopMonitoring() {
        centralManager.delegate = null
    }

    private fun updateEnabledState() {
        _isEnabled.value = centralManager.state == CBCentralManagerStatePoweredOn
    }
}
