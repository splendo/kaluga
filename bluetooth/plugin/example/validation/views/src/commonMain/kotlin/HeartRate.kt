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

package com.splendo.kaluga.bluetooth.views

import com.splendo.kaluga.bluetooth.annotations.Advertising
import com.splendo.kaluga.bluetooth.annotations.Bluetooth
import com.splendo.kaluga.bluetooth.annotations.BluetoothCharacteristic
import com.splendo.kaluga.bluetooth.annotations.BluetoothDescriptor
import com.splendo.kaluga.bluetooth.annotations.BluetoothService
import com.splendo.kaluga.bluetooth.annotations.Notifiable
import com.splendo.kaluga.bluetooth.annotations.Readable
import com.splendo.kaluga.bluetooth.annotations.Writable
import com.splendo.kaluga.bytes.annotations.BytesLayout
import com.splendo.kaluga.bytes.annotations.Generate
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.Unsigned

@BytesLayout
interface HeartRate {
    @Size(Length.`8_BIT`)
    @Unsigned
    val bpm: Int

    @Size(Length.`16_BIT`)
    @Unsigned
    val energy: Int
}

@BytesLayout(generate = [Generate.DATA])
interface BatteryLevel {
    @Size(Length.`8_BIT`)
    @Unsigned
    val percentage: Int
}

@Bluetooth
interface HeartRateDevice {
    @Advertising
    val heartRateService: HeartRateService
}

@BluetoothService("180d")
interface HeartRateService {
    val measurement: MeasurementCharacteristic
    val battery: BatteryCharacteristic
}

@BluetoothCharacteristic("2a37")
interface MeasurementCharacteristic {
    @Readable
    @Writable
    @Notifiable
    val heartRate: HeartRate

    val limit: LimitDescriptor
}

@BluetoothDescriptor("2901")
interface LimitDescriptor {
    @Readable
    @Writable
    val limit: HeartRate
}

@BluetoothCharacteristic("2a19")
interface BatteryCharacteristic {
    @Readable
    @Notifiable
    val level: BatteryLevel
}
