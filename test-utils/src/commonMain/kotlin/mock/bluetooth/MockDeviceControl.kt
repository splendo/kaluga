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

package com.splendo.kaluga.test.mock.bluetooth

import com.splendo.kaluga.bluetooth.connect
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceInfo
import com.splendo.kaluga.test.mock.bluetooth.device.MockDeviceInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class MockDeviceControl private constructor(
    mocksFlowReplay: Int,
    private val deviceInfo: DeviceInfo
    ) {
    private val mockDeviceFactory = MockDeviceFactory(Dispatchers.Main.immediate)

    private val _mock = MutableSharedFlow<Device>(replay = mocksFlowReplay)
    val mock: Flow<Device> = _mock.asSharedFlow()

    class Builder {
        var mocksFlowReplay: Int = 10
        var deviceInfo: DeviceInfo? = null
        fun deviceInfo(build: MockDeviceInfo.Builder.() -> Unit) {
            val builder = MockDeviceInfo.Builder()
            build(builder)
            deviceInfo = builder.build()
        }
    }

    companion object {
        fun build(
            build: Builder.() -> Unit = {}
        ) : MockDeviceControl {
            val builder = Builder()
            build(builder)
            return MockDeviceControl(
                mocksFlowReplay = builder.mocksFlowReplay,
                deviceInfo = builder.deviceInfo ?: MockDeviceInfo.build { }
            )
        }
    }

    suspend fun discover() {
        val device = mockDeviceFactory.build(deviceInfo)
        _mock.emit(device)
    }

    suspend fun connect() {
        _mock.connect()
    }

    suspend fun disconnect() {

    }

    suspend fun simulate() {

    }

    suspend fun expect() {

    }
}