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

package com.splendo.kaluga.example.feature.bluetooth.generation

import com.splendo.kaluga.bluetooth.annotations.Advertising
import com.splendo.kaluga.bluetooth.annotations.AdvertisingName
import com.splendo.kaluga.bluetooth.annotations.Bluetooth
import com.splendo.kaluga.bluetooth.annotations.BluetoothCharacteristic
import com.splendo.kaluga.bluetooth.annotations.BluetoothDescriptor
import com.splendo.kaluga.bluetooth.annotations.BluetoothService
import com.splendo.kaluga.bluetooth.annotations.Indicatable
import com.splendo.kaluga.bluetooth.annotations.Notifiable
import com.splendo.kaluga.bluetooth.annotations.Readable
import com.splendo.kaluga.bluetooth.annotations.Writable

@Bluetooth
@AdvertisingName("KalugaDemo")
interface DemoDevice {
    @Advertising
    val demoService: DemoService
}

@BluetoothService("d000")
interface DemoService {
    val sensor: SensorCharacteristic
    val config: ConfigCharacteristic
}

@BluetoothCharacteristic("d001")
interface SensorCharacteristic {
    @Readable
    val reading: Int

    @Notifiable
    val live: Short
}

@BluetoothCharacteristic("d002")
interface ConfigCharacteristic {
    @BluetoothDescriptor("d003")
    interface Info {
        @Readable
        val name: String
    }

    @Writable
    val threshold: Int

    @Indicatable
    val status: Short

    val info: Info
}
