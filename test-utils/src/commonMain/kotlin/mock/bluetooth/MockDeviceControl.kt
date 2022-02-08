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

import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


class MockDeviceControl private constructor(builder: Builder) {
    private val _mock = MutableSharedFlow<Device>()
    val mock: Flow<Device> = _mock.asSharedFlow()

    class Builder {
        class  DeviceInfoBuilder {
            var rssi = -100
            fun build(): DeviceInfo {
                TODO("Not implemented yet")
            }
        }
        lateinit var deviceInfo: DeviceInfo
        fun buildDeviceInfo(build: DeviceInfoBuilder.() -> Unit) {
            val builder = DeviceInfoBuilder()
            build(builder)
            deviceInfo = builder.build()
        }

        companion object {
            fun build(build: Builder.() -> Unit = {}): MockDeviceControl {
                val builder = Builder()
                build(builder)
                return MockDeviceControl(builder)
            }
        }
    }

    fun discover() {
        // _mock.tryEmit(null)
    }

    fun connect() {

    }

    fun disconnect() {

    }

    fun simulate() {

    }

    fun expect() {

    }
}