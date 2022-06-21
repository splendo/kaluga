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

package com.splendo.kaluga.bluetooth.beacons

import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.test.SimpleFlowTest
import kotlinx.coroutines.CoroutineScope

open class BeaconFlowTest(
    timeoutMs: Long = 3_000
) : SimpleFlowTest<Set<BeaconInfo>>() {

    private val bluetooth = BluetoothMock(scope)
    private val beacons = Beacons(bluetooth, timeoutMs = timeoutMs)

    override val flow = suspend { beacons.beacons }

    fun start(coroutineScope: CoroutineScope) = beacons.startMonitoring(coroutineScope)
    fun stop() = beacons.stopMonitoring()
    suspend fun discoverDevices(vararg devices: Device) = bluetooth.discoveredDevices.emit(devices.toList())
}
