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

package com.splendo.kaluga.bluetooth.beacons

import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.test.base.SimpleFlowTest
import com.splendo.kaluga.test.bluetooth.MockBluetoothService
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

abstract class BeaconFlowTest(timeout: Duration = 3.seconds) : SimpleFlowTest<Set<BeaconInfo>>() {

    private val bluetooth = MockBluetoothService()
    private val beacons = DefaultBeacons(bluetooth, beaconLifetime = timeout, coroutineContext = scope.coroutineContext)

    override val flow = suspend { beacons.beacons }

    suspend fun start() = beacons.startMonitoring()
    suspend fun stop() = beacons.stopMonitoring()
    suspend fun discoverDevices(vararg devices: Device) = bluetooth.filteredDevicesFlow.emit(mapOf(emptySet<UUID>() to devices.toList()))
}
