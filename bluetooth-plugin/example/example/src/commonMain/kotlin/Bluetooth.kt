/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.example

import com.splendo.kaluga.bluetooth.annotations.Bluetooth
import com.splendo.kaluga.bluetooth.annotations.BluetoothCharacteristic
import com.splendo.kaluga.bluetooth.annotations.BluetoothDescriptor
import com.splendo.kaluga.bluetooth.annotations.BluetoothService
import com.splendo.kaluga.bluetooth.annotations.Notifiable
import com.splendo.kaluga.bluetooth.annotations.Readable
import com.splendo.kaluga.bluetooth.annotations.Writable
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

@Bluetooth
interface BluetoothTest {
    @Bluetooth
    interface Nested

    val testService: TestService
}

@BluetoothService("abcd")
interface TestService {
    val testCharacteristic: TestCharacteristic
}

@BluetoothCharacteristic("1234")
interface TestCharacteristic {
    @BluetoothDescriptor("5678")
    interface TestDescriptor {
        @Readable
        val name: String
        @Writable
        val age: Int
    }

    @Readable
    val status: String
    @Writable
    val shouldUpdate: Boolean
    @Notifiable
    val state: Short

    val testDescriptor: TestDescriptor
}

