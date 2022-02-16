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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockDeviceControl private constructor(
    private val deviceInfo: DeviceInfo
    ) {
    private val mockDeviceFactory = MockDeviceFactory(Dispatchers.Main)

    private val _mock = MutableStateFlow<Device?>(value = null)
    val mock: Flow<Device?> = _mock.asStateFlow()

    class Builder {
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
                deviceInfo = builder.deviceInfo ?: MockDeviceInfo.build { }
            )
        }
    }

    suspend fun discover() {
        val device = mockDeviceFactory.build(deviceInfo)
        _mock.emit(device)
    }

    suspend fun connect() {
        mock.connect()
    }

    suspend fun disconnect() {

    }

    suspend fun simulate() {

    }

    suspend fun expect() {

    }
}