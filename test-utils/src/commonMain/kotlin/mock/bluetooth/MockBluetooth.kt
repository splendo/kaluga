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

package com.splendo.kaluga.test.mock.bluetooth

import com.splendo.kaluga.bluetooth.BluetoothService
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.Device
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.js.JsName

class MockBluetooth(
    private val coroutineScope: CoroutineScope
) : BluetoothService {

    @JsName("jsDevice")
    val devices = MutableStateFlow(emptyList<Device>())
    @JsName("jsIsScanning")
    val isScanning = MutableStateFlow(false)

    override val isEnabled = MutableStateFlow(true)

    override fun startScanning(filter: Set<UUID>) { isScanning.value = true }
    override fun stopScanning() { isScanning.value = false }
    override fun devices() = devices.asStateFlow()
    override suspend fun isScanning() = isScanning.asStateFlow()
}
