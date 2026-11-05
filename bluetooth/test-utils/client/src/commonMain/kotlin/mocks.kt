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

package com.splendo.kaluga.bluetooth.test

import com.splendo.kaluga.bluetooth.RemoteServiceWrapper
import com.splendo.kaluga.bluetooth.device.PortableDeviceWrapper
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.bluetooth.device.Identifier

expect fun randomIdentifier(): Identifier

/**
 * A [DeviceWrapper] carrying only a [name] and an [identifier], for devices driven by a mock connection manager.
 */
data class MockDeviceWrapper(override val name: String? = null, override val identifier: Identifier = randomIdentifier()) : PortableDeviceWrapper()

fun createDeviceWrapper(deviceName: String? = null, identifier: Identifier = randomIdentifier()): DeviceWrapper = MockDeviceWrapper(deviceName, identifier)

fun createServiceWrapper(builder: ServiceWrapperBuilder.() -> Unit): RemoteServiceWrapper = ServiceWrapperBuilder().apply(builder).build()

interface ContainsMockValue {
    var value: ByteArray?
    fun updateValue(value: ByteArray?) {
        this.value = value
    }
}
